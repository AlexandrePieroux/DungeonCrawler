package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.Hero;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.ArrayList;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.AgentHandler.AgentHandler;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector.JordanCollision.JordanCollision;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.QuadTree.QuadTree;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Living;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;

/**
 * Created by alexp_000 on 04-04-16.
 */
public class HeroHandler extends AgentHandler {
    private static final int DEFAULT_SPEED = 3;

    // Collision algorithm
    private JordanCollision jordan;
    private QuadTree tree;

    // Polygon that define the area where to press in order to make the agent move
    private PointF[] up;
    private PointF[] down;
    private PointF[] left;
    private PointF[] right;

    // Hero information
    private Living hero;
    private MotionEvent touchEvent;
    private float[] dim;
    private int speed = DEFAULT_SPEED;

    // Synchronization lock
    private final Object lock;
    private boolean newEvent;

    public HeroHandler(Living hero, GraphicScene scene, Context context, GlSurface surface){
        super(scene, context, surface);
        this.hero = hero;
        this.dim = hero.getShape().getDim();
        surface.attach(this);

        // Set screen dimensions
        this.updateScreen();

        this.lock = new Object();
        this.newEvent = false;
        this.jordan = new JordanCollision();
        this.tree = this.scene.getTree();
    }

    @Override
    public void run() {
        while(true) {
            int action = 0;
            synchronized (this.lock) {
                try {
                    if(!this.newEvent)
                        this.lock.wait();

                    action = this.touchEvent.getActionMasked();
                    this.newEvent = false;
                    this.lock.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
                move(new PointF(this.touchEvent.getX(), this.touchEvent.getY()));
            } else if(action == MotionEvent.ACTION_UP){
                synchronized (this.scene) {
                    this.hero.setFrame(0);
                    this.scene.setSceneChanged();
                    this.scene.notify();
                }
            }
        }
    }

    @Override
    public void updateState(MotionEvent e) {
        synchronized (this.lock){
            this.touchEvent = e;
            this.newEvent = true;
            this.lock.notify();
        }
    }

    @Override
    public void updateScreen(){
        WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        this.up = new PointF[]{new PointF(0, 0), new PointF(size.x/2, size.y/2), new PointF(size.x, 0)};
        this.down = new PointF[]{new PointF(0, size.y), new PointF(size.x/2, size.y/2), new PointF(size.x, size.y)};
        this.left = new PointF[]{new PointF(0, 0), new PointF(size.x/2, size.y/2), new PointF(0, size.y)};
        this.right = new PointF[]{new PointF(size.x, 0), new PointF(size.x/2, size.y/2), new PointF(size.x, size.y)};
    }

    private void move(PointF point){
        if(this.jordan.collide(this.up, point)) {
            moveUp();
        }else if(this.jordan.collide(this.down, point)) {
            moveDown();
        }else if(this.jordan.collide(this.left, point)) {
            moveLeft();
        }else if(this.jordan.collide(this.right, point)) {
            moveRight();
        }
    }

    private void lookAtHero(){
        float[] view = new float[]{
                this.hero.getShape().getPos()[0] + this.dim[1]/2,
                this.hero.getShape().getPos()[1] + this.dim[0]/2
        };
        this.surface.lookAt(view);
    }

    private void moveUp(){
        this.hero.setStance(1);

        PointF[] simulationBox = new PointF[4];
        PointF[] heroBox = this.hero.getBoundingBox();
        for(int i = 0; i < 4; i++){
            simulationBox[i] = new PointF(heroBox[i].x, heroBox[i].y  + this.speed);
        }

        boolean collide = collide(simulationBox);
        synchronized (this.scene) {
            if(!collide) {
                this.hero.moveUp(this.speed);
                this.scene.setSceneChanged();
            }
            this.scene.notify();
        }
        lookAtHero();
    }

    private void moveDown(){
        this.hero.setStance(0);

        PointF[] simulationBox = new PointF[4];
        PointF[] heroBox = this.hero.getBoundingBox();
        for(int i = 0; i < 4; i++){
            simulationBox[i] = new PointF(heroBox[i].x, heroBox[i].y - this.speed);
        }

        boolean collide = collide(simulationBox);
        synchronized (this.scene) {
            if(!collide) {
                this.hero.moveDown(this.speed);
                this.scene.setSceneChanged();
            }
            this.scene.notify();
        }
        lookAtHero();
    }


    private void moveLeft(){
        this.hero.setStance(2);

        PointF[] simulationBox = new PointF[4];
        PointF[] heroBox = this.hero.getBoundingBox();
        for(int i = 0; i < 4; i++){
            simulationBox[i] = new PointF(heroBox[i].x - this.speed, heroBox[i].y);
        }

        boolean collide = collide(simulationBox);
        synchronized (this.scene) {
            if(!collide) {
                this.hero.moveLeft(this.speed);
                this.scene.setSceneChanged();
            }
            this.scene.notify();
        }
        lookAtHero();
    }

    private void moveRight(){
        this.hero.setStance(3);

        PointF[] simulationBox = new PointF[4];
        PointF[] heroBox = this.hero.getBoundingBox();
        for(int i = 0; i < 4; i++){
            simulationBox[i] = new PointF(heroBox[i].x + this.speed, heroBox[i].y);
        }

        boolean collide = collide(simulationBox);
        synchronized (this.scene) {
            if(!collide) {
                this.hero.moveRight(this.speed);
                this.scene.setSceneChanged();
            }
            this.scene.notify();
        }
        lookAtHero();
    }

    // Check if the hero is still in the bounds of the map and do not collide with anything
    private boolean collide(PointF[] simulationBox){
        // Get all the objects that might collide with the hero
        ArrayList<PointF[]> objects = new ArrayList<>();
        this.tree.retrieve(objects, this.hero.getBoundingBox());

        boolean result = true;
        if(this.jordan.fit(this.scene.getBounds(), simulationBox)){
            result = false;
            for(PointF[] obj : objects) {
                if (this.jordan.collide(obj, simulationBox)) {
                    this.hero.setFrame(0);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
