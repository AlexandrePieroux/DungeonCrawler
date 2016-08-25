package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living;

import android.graphics.PointF;

import java.util.Stack;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.Shape;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.ShapeType;

/**
 * Created by alexp_000 on 26-04-16.
 */
public class Living extends SceneComponent {
    protected enum SpriteType{
        WALK,
        RUN,
        ATTACK,
        ATTACK_IDLE
    }

    // Sprite sheet format
    private static final String WALK_SPRITE = "_walk.png";
    private static final int NB_WALK_FRAMES = 6;
    private static final int NB_WALK_STANCES = 4;

    private static final String RUN_SPRITE = "_run.png";
    private static final int NB_RUN_FRAMES = 6;
    private static final int NB_RUN_STANCES = 4;

    private static final String ATTACK_SPRITE = "_attack.png";
    private static final int NB_ATTACK_FRAMES = 5;
    private static final int NB_ATTACK_STANCES = 1;

    private static final String ATTACK_IDLE_SPRITE = "_attack_idle.png";
    private static final int NB_ATTACK_IDLE_FRAMES = 4;
    private static final int NB_ATTACK_IDLE_STANCES = 2;

    // Texture information
    protected String path;
    protected String name;

    // Textures
    protected String spriteSheetWalk;
    protected String spriteSheetRun;
    protected String spriteSheetAttack;
    protected String spriteSheetAttackIdle;

    // Current sprite info
    private int currentStance = 1;
    private int currentFrame = 1;
    private int nbFrames;
    private int nbStances;

    // Logical information
    private float[] agentPosition;
    private int counterFrame = 5;

    public Living(String path, String name, float[] coord, float[] dim, float[] uvs){
        super();
        this.shape = new Shape(ShapeType.ANIMATED, coord, dim, uvs, "");
        this.drawingStack.add(this.shape);

        this.spriteSheetWalk = path + name + WALK_SPRITE;
        this.spriteSheetRun = path + name + RUN_SPRITE;
        this.spriteSheetAttack = path + name + ATTACK_SPRITE;
        this.spriteSheetAttackIdle = path + name + ATTACK_SPRITE;
        this.path = path;
        this.name = name;

        this.agentPosition = this.shape.getPos();

        this.setSpriteSheet(SpriteType.WALK);
    }

    public void setBoundingBox(PointF[] polygon){
        this.polygonsStack = new Stack<>();
        this.polygonsStack.add(polygon);
    }

    public PointF[] getBoundingBox(){
        return this.polygonsStack.get(0);
    }

    public void setSpriteSheet(SpriteType sprite){
        switch (sprite){
            case WALK:
                this.shape.setTexturePath(this.spriteSheetWalk);
                this.nbFrames = NB_WALK_FRAMES;
                this.nbStances = NB_WALK_STANCES;
                break;
            case RUN:
                this.shape.setTexturePath(this.spriteSheetRun);
                this.nbFrames = NB_RUN_FRAMES;
                this.nbStances = NB_RUN_STANCES;
                break;
            case ATTACK:
                this.shape.setTexturePath(this.spriteSheetAttack);
                this.nbFrames = NB_ATTACK_FRAMES;
                this.nbStances = NB_ATTACK_STANCES;
                break;
            case ATTACK_IDLE:
                this.shape.setTexturePath(this.spriteSheetAttackIdle);
                this.nbFrames = NB_ATTACK_IDLE_FRAMES;
                this.nbStances = NB_ATTACK_IDLE_STANCES;
                break;
            default:
                this.shape.setTexturePath(this.spriteSheetWalk);
                this.nbFrames = NB_WALK_FRAMES;
                this.nbStances = NB_WALK_STANCES;
        }
    }

    public void setFrame(int frameNmbr){
        this.currentFrame = (frameNmbr % this.nbFrames) + 1;
        float x =  this.currentFrame/(float)this.nbFrames;
        float y = this.currentStance/(float)this.nbStances;
        this.shape.setUvs(this.translateM(x, y));
    }

    public void setStance(int stance){
        this.currentStance = (stance % this.nbStances) + 1;
        this.setFrame(this.currentFrame - 1);
    }

    public void moveUp(int distance){
        this.setStance(1);
        updateFrame();

        this.agentPosition[1] += distance;
        PointF[] boundingBox = this.getBoundingBox();
        for(PointF p : boundingBox)
            p.y += distance;
        this.shape.setPos(this.agentPosition);
        this.counterFrame--;
    }

    public void moveDown(int distance){
        this.setStance(0);
        updateFrame();

        PointF[] boundingBox = this.getBoundingBox();
        for(PointF p : boundingBox)
            p.y -= distance;
        this.agentPosition[1] -= distance;
        this.shape.setPos(this.agentPosition);
        this.counterFrame--;
    }

    public void moveLeft(int distance){
        this.setStance(2);
        updateFrame();

        PointF[] boundingBox = this.getBoundingBox();
        for(PointF p : boundingBox)
            p.x -= distance;
        this.agentPosition[0] -= distance;
        this.shape.setPos(this.agentPosition);
        this.counterFrame--;
    }

    public void moveRight(int distance){
        this.setStance(3);
        updateFrame();

        PointF[] boundingBox = this.getBoundingBox();
        for(PointF p : boundingBox)
            p.x += distance;
        this.agentPosition[0] += distance;
        this.shape.setPos(this.agentPosition);
        this.counterFrame--;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    private float[] translateM(float x, float y){
        float xprime = x - (1.0f/this.nbFrames);
        float yprime = y - (1.0f/this.nbStances);
        return new float[]{
                xprime, yprime,
                xprime, y,
                x, yprime,
                xprime, y,
                x, y,
                x, yprime
        };
    }

    private void updateFrame(){
        if(this.counterFrame == 0) {
            this.currentFrame = (this.currentFrame == 6)?1:this.currentFrame;
            this.setFrame(this.currentFrame);
            this.counterFrame = 5;
        }
    }
}
