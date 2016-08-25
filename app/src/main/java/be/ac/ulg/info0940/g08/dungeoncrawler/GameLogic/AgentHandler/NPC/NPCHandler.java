package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.NPC;

import android.content.Context;
import android.view.MotionEvent;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.AgentHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Living;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexp_000 on 06-04-16.
 */
public class NPCHandler extends AgentHandler {
    private Living[] npcs;

    public NPCHandler(Living[] npcs, GraphicScene scene, Context context, GlSurface surface){
        super(scene, context, surface);
        this.npcs = npcs;
    }

    @Override
    public void run(){
        // TODO handle the npcs
    }

    @Override
    public void updateState(MotionEvent e) {

    }

    @Override
    public void updateScreen() {

    }
}
