package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic;

import android.content.Context;
import android.graphics.PointF;

import org.json.JSONObject;

import java.util.Stack;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.Hero.HeroHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.Mob.MobHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.NPC.NPCHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector.JordanCollision.JordanCollision;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.QuadTree.QuadTree;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Agent.Agent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader.Level;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader.LevelLoader;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexp_000 on 01-04-16.
 */
public class GameLogic {
    private final LevelLoader loader;
    private final GlSurface surface;
    private final GraphicScene scene;
    private final Context context;
    private final JSONObject initLevel;

    public GameLogic(LevelLoader loader, JSONObject initLevel, GlSurface surface, Context context){
        this.loader = loader;
        this.initLevel = initLevel;
        this.surface = surface;
        this.scene = new GraphicScene();
        this.context = context;
    }

    public void startGame(){
        // Initial level
        Level init = this.loader.load(this.initLevel);

        // Creating the QuadTree and init the limits and obstacles
        PointF[] mapBounds = init.getMap().getBounds();
        this.scene.setBounds(mapBounds);
        Stack<PointF[]> obstacles = init.getMap().getPolygonsStack();
        QuadTree tree = new QuadTree(0, QuadTree.getBoundingBox(mapBounds), new JordanCollision());
        for(PointF[] o : obstacles)
            tree.insert(o);
        this.scene.setTree(tree);

        // Add all the component to the scene and create the handlers
        this.scene.add(init.getMap());
        HeroHandler heroHandler = new HeroHandler(init.getHero(), this.scene, this.context, this.surface);

        NPCHandler npcs = null;
        if(init.getNpcs() != null) {
            for(Agent a: init.getNpcs())
                this.scene.add(a);
            npcs = new NPCHandler(init.getNpcs(), this.scene, this.context, this.surface);
        }

        MobHandler mobs = null;
        if(init.getMobs() != null) {
            for(Agent a: init.getMobs())
                this.scene.add(a);
            mobs = new MobHandler(init.getMobs(), this.scene, this.context, this.surface);
        }
        this.scene.add(init.getHero());

        // Set the scene
        this.surface.setScene(this.scene);

        // Set the view
        this.surface.lookAt(init.getViewInitPos());

        // Start all the handlers
        heroHandler.start();
        if(npcs != null)
            npcs.start();
        if(mobs != null)
            mobs.start();
    }
}
