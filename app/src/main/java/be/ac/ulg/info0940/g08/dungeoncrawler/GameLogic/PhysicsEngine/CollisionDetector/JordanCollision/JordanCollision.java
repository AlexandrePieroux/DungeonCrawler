package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector.JordanCollision;

import android.graphics.PointF;
import android.util.Log;

import java.util.Arrays;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector.CollisionDetector;

/**
 * Created by alexp_000 on 01-05-16.
 */
public class JordanCollision implements CollisionDetector {
    @Override
    public boolean collide(PointF[] objectA, PointF[] objectB) {
        for(PointF p : objectB)
            if(collide(objectA, p))
                return true;
        return false;
    }

    @Override
    public boolean fit(PointF[] area, PointF[] object) {
        for(PointF p : object)
            if(!collide(area, p))
                return false;
        return true;
    }

    // Using Jordan curve theorem at its best
    @Override
    public boolean collide(PointF[] edges, PointF point){
        boolean result = false;

        for(int i = 0; i < edges.length; i++){
            int j = (i+1) % edges.length;
            float[] point1 = new float[]{edges[i].x, edges[i].y};
            float[] point2 = new float[]{edges[j].x, edges[j].y};


            // Check if a straight line along the x axis intersect the edge
            if((point.y < point1[1] && point.y >= point2[1] ||
                point.y < point2[1] && point.y >= point1[1])&&
               (point.x >= point2[0] || point.x >= point1[0])){
                float x = point2[0]+(point.y-point2[1])/(point1[1]-point2[1])*(point1[0]-point2[0]);
                if(x < point.x)
                    result = !result;
            }
        }
        return result;
    }
}
