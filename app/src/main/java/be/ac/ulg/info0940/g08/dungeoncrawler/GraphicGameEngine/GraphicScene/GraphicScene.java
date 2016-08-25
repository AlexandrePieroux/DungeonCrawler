package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene;

import android.graphics.PointF;

import java.util.LinkedList;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.QuadTree.QuadTree;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;

/**
 * Created by alexandre on 15/03/16.
 */
public class GraphicScene extends LinkedList<SceneComponent>{
    private boolean changed;
    private PointF[] bounds;
    private QuadTree tree;

    public GraphicScene(){
        super();
        this.changed = false;
    }



    public void setSceneChanged(){
        this.changed = true;
    }

    public void unsetSceneChanged(){
        this.changed = false;
    }

    public boolean sceneChanged(){
        return this.changed;
    }

    public void setBounds(PointF[] bounds){
        this.bounds = bounds;
    }

    public QuadTree getTree() {
        return tree;
    }

    public void setTree(QuadTree tree) {
        this.tree = tree;
    }

    public PointF[] getBounds(){
        return this.bounds;
    }
}
