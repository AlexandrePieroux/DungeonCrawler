package be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.QuadTree;

import android.graphics.PointF;

import java.util.ArrayList;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.PhysicsEngine.CollisionDetector.CollisionDetector;

/**
 * Created by alexp_000 on 30-04-16.
 */
public class QuadTree {
    private static int MAX_OBJECTS = 10;
    private static int MAX_LEVELS = 5;
    public static PointF[] getBoundingBox(PointF[] bounds){
        // Get the bounding box of the bounds
        float xMax = bounds[0].x;
        float xMin = bounds[0].x;
        float yMax = bounds[0].y;
        float yMin = bounds[0].y;
        for(PointF p : bounds){
            xMax = (p.x > xMax)? p.x : xMax;
            yMax = (p.y > yMax)? p.y : yMax;
            xMin = (p.x < xMin)? p.x : xMin;
            yMin = (p.y < yMin)? p.y : yMin;
        }

        PointF[] result = new PointF[4];
        result[0] = new PointF(xMin, yMin);
        result[1] = new PointF(xMax, yMin);
        result[2] = new PointF(xMax, yMax);
        result[3] = new PointF(xMin, yMax);
        return result;
    }

    private int level;
    private ArrayList<PointF[]> objects;
    private PointF[] bounds;
    private QuadTree[] nodes;
    private CollisionDetector detector;

    public QuadTree(int level, PointF[] bounds, CollisionDetector detector){
        this.level = level;
        this.objects = new ArrayList();
        this.bounds = bounds;
        this.nodes = new QuadTree[4];
        this.detector = detector;
    }

    // Clear recursively all objects in the QuadTree
    public void clear(){
        this.objects.clear();
        for(QuadTree n : this.nodes)
            n.clear();
    }

    /*
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(PointF[] object){
        if (this.nodes[0] != null) {
            int index = getIndex(object);
            if (index != -1) {
                this.nodes[index].insert(object);
                return;
            }
        }

        this.objects.add(object);
        if (this.objects.size() > MAX_OBJECTS && this.level < MAX_LEVELS) {
            if (this.nodes[0] == null) {
                split();
            }
            int i = 0;
            while (i < this.objects.size()) {
                int index = getIndex(this.objects.get(i));
                if (index != -1) {
                    nodes[index].insert(this.objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }

    /*
     * Return all objects that could collide with the given object
     */
    public ArrayList<PointF[]> retrieve(ArrayList<PointF[]> objects, PointF[] ObjectBounds){
        int index = getIndex(ObjectBounds);
        if (index != -1 && this.nodes[0] != null) {
            this.nodes[index].retrieve(objects, ObjectBounds);
        }
        objects.addAll(this.objects);
        return objects;
    }

    // Splits the node into four subnodes
    private void split(){
        ArrayList<PointF[]> subArea = splitInFour();
        this.nodes[0] = new QuadTree(this.level + 1, subArea.get(0), this.detector);
        this.nodes[1] = new QuadTree(this.level + 1, subArea.get(1), this.detector);
        this.nodes[2] = new QuadTree(this.level + 1, subArea.get(2), this.detector);
        this.nodes[3] = new QuadTree(this.level + 1, subArea.get(3), this.detector);
    }

    /*
     * Determine which node the object belongs to. -1 means
     * object cannot completely fit within a child node and is part
     * of the parent node
     */
    private int getIndex(PointF[] area){
        ArrayList<PointF[]> subArea = splitInFour();
        if(this.detector.fit(subArea.get(0), area))
            return 0;
        if(this.detector.fit(subArea.get(1), area))
            return 1;
        if(this.detector.fit(subArea.get(2), area))
            return 2;
        if(this.detector.fit(subArea.get(3), area))
            return 3;
        return -1;
    }

    // Split the current area in 4 equals parts
    private ArrayList<PointF[]> splitInFour(){
        float width = this.bounds[1].x - this.bounds[0].x;
        float height = this.bounds[3].y - this.bounds[0].y;

        PointF[] upperRight = new PointF[4];
        upperRight[0] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + (height/2));
        upperRight[1] = new PointF(this.bounds[0].x + width, this.bounds[0].y + (height/2));
        upperRight[2] = new PointF(this.bounds[0].x + width, this.bounds[0].y + height);
        upperRight[3] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + height);

        PointF[] downRight = new PointF[4];
        downRight[0] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y);
        downRight[1] = new PointF(this.bounds[0].x + width, this.bounds[0].y);
        downRight[2] = new PointF(this.bounds[0].x + width, this.bounds[0].y + (height/2));
        downRight[3] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + (height/2));

        PointF[] downLeft = new PointF[4];
        downLeft[0] = new PointF(this.bounds[0].x, this.bounds[0].y);
        downLeft[1] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y);
        downLeft[2] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + (height/2));
        downLeft[3] = new PointF(this.bounds[0].x, this.bounds[0].y + (height/2));

        PointF[] upperLeft = new PointF[4];
        upperLeft[0] = new PointF(this.bounds[0].x, this.bounds[0].y + (height/2));
        upperLeft[1] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + (height/2));
        upperLeft[2] = new PointF(this.bounds[0].x + (width/2), this.bounds[0].y + height);
        upperLeft[3] = new PointF(this.bounds[0].x, this.bounds[0].y + height);

        ArrayList<PointF[]> out = new ArrayList<>();
        out.add(upperRight);
        out.add(downRight);
        out.add(downLeft);
        out.add(upperLeft);
        return out;
    }
}
