package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector;

import android.graphics.PointF;

/**
 * Created by alexp_000 on 01-05-16.
 */
public interface CollisionDetector {
    boolean collide(PointF[] objectA, PointF[] objectB);
    boolean collide(PointF[] object, PointF point);
    boolean fit(PointF[] area, PointF[] object);
}
