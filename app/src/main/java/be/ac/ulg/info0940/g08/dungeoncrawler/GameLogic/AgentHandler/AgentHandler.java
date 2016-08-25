package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler;

import android.content.Context;
import android.view.MotionEvent;

import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexp_000 on 06-04-16.
 */
public abstract class AgentHandler extends Thread{
    protected final GlSurface surface;
    protected final Context context;
    protected final GraphicScene scene;

    public AgentHandler(GraphicScene scene, Context context, GlSurface surface){
        this.scene = scene;
        this.context = context;
        this.surface = surface;
    }

    // What is needed to be implemented to create an agent handler
    public abstract void run();
    public abstract void updateState(MotionEvent e);
    public abstract void updateScreen();
}
