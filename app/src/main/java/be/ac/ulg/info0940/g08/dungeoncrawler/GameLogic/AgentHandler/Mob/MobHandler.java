package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.Mob;

import android.content.Context;
import android.view.MotionEvent;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.AgentHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Living;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexp_000 on 04-04-16.
 */
public class MobHandler extends AgentHandler {
    private Living[] mobs;

    public MobHandler(Living[] mobs, GraphicScene scene, Context context, GlSurface surface){
        super(scene, context, surface);
        this.mobs = mobs;
    }

    @Override
    public void run(){
        // TODO Handle the agent
    }

    @Override
    public void updateState(MotionEvent e) {

    }

    @Override
    public void updateScreen() {

    }
}
