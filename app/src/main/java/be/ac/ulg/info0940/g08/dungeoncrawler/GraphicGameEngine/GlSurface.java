package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.LinkedList;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.AgentHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexandre on 16/03/16.
 */
public class GlSurface extends GLSurfaceView {
    private static final int UPDATE_TIME = 20;

    private final GlRenderer renderer;
    private final LinkedList<AgentHandler> listeners;

    private long lastUpdate;

    public GlSurface(Context context){
        super(context);

        setEGLContextClientVersion(2);
        this.renderer = new GlRenderer(context);
        this.listeners = new LinkedList<>();
        setRenderer(this.renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // Init time
        this.lastUpdate = System.currentTimeMillis();
    }

    public void onPause(){
        super.onPause();
        renderer.onPause();
    }

    public void onResume(){
        super.onResume();
        renderer.onResume();
    }

    public void setScene(GraphicScene g){
        renderer.setScene(g);
    }

    public void lookAt(float[] view){
        renderer.lookAt(view);
    }

    public void attach(AgentHandler l){
        this.listeners.add(l);
    }

    private void notifyListeners(MotionEvent e){
        for(AgentHandler l : this.listeners)
            l.updateState(e);
    }

    // Listener
    @Override
    public boolean onTouchEvent(MotionEvent e){
        long time = System.currentTimeMillis() - this.lastUpdate;
        if (e.getActionMasked() == MotionEvent.ACTION_UP || time >= UPDATE_TIME) {
            notifyListeners(e);
            this.lastUpdate = System.currentTimeMillis();
        }
        return true;
    }

    // Give the size of the screen to each agent that listen to the screen
    @Override
    public void onSizeChanged(int h, int w, int oldh, int oldw){
        for(AgentHandler l : this.listeners)
            l.updateScreen();
    }
}
