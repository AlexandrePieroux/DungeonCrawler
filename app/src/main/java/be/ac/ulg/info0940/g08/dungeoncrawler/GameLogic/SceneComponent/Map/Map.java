package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Map;

import android.graphics.PointF;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.Shape;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.ShapeType;

/**
 * Created by alexandre on 15/03/16.
 */
public class Map extends SceneComponent {
    public Shape[][] getGroundMatrix() {
        return groundMatrix;
    }

    public void setGroundMatrix(Shape[][] groundMatrix) {
        this.groundMatrix = groundMatrix;
    }

    private Shape[][] groundMatrix;
    private PointF[] bounds;

    // Add a tile to the map
    public void addTile(String g, float[] pos, float[] dim) {
        this.drawingStack.add(new Shape(ShapeType.STATIC, pos, dim, g));
    }

    public void addPolygon(PointF[] polygon) {
        this.polygonsStack.add(polygon);
    }

    public void setBounds(PointF[] bounds){
        this.bounds = bounds;
    }

    public PointF[] getBounds(){
        return this.bounds;
    }
}
