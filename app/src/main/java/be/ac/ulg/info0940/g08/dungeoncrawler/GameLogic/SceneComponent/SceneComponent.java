package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent;

import android.graphics.PointF;

import java.util.Stack;

import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.Shape;

/**
 * Created by alexp_000 on 23-03-16.
 */
public abstract class SceneComponent {

    // OpenGL information
    protected Stack<Shape> drawingStack;
    protected Stack<PointF[]> polygonsStack;
    protected Shape shape;

    // Default constructor
    public SceneComponent(){
        this.drawingStack = new Stack<>();
        this.polygonsStack = new Stack<>();
    }

    // Return the drawing stack of the item
    public Stack<Shape> getDrawingStack() {
        return this.drawingStack;
    }

    // Return the polygons that defines the edges of the objects
    public Stack<PointF[]> getPolygonsStack() {
        return this.polygonsStack;
    }

    // Return the pure graphical representation of the item
    public Shape getShape(){
        return this.shape;
    }
}
