package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by alexp_000 on 18-03-16.
 */
public class Shape {
    // Default uvs matrix
    private static final short[] defaultIndices = new short[] {0, 1, 2, 3, 4, 5};
    private static final float[] defaultUvs = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    private final FloatBuffer vertexBuffer;
    private final ShapeType shapeType;
    private FloatBuffer textureCoordinateBuffer;

    // Data of the shape
    private Integer[] buffers;
    private String texturePath;
    private ShortBuffer vertexIndicesBuffer;
    protected float[] modelMatrix;
    protected float[] uvs;
    protected float[] pos;
    private final float[] dim;

    // Getter functions
    public Integer[] getBuffers() {return this.buffers;}
    public ShortBuffer getVertexIndicesBuffer() {return vertexIndicesBuffer;}
    public String getTexturePath() {return this.texturePath;}
    public int getIndicesLength() {return this.defaultIndices.length;}
    public float[] getModelMatrix() {return this.modelMatrix;}
    public FloatBuffer getVertex() {return this.vertexBuffer;}
    public FloatBuffer getTextureCoordinate() {return this.textureCoordinateBuffer;}
    public ShapeType getType(){return this.shapeType;}
    public float[] getPos(){return this.pos;}
    public float[] getDim(){return this.dim;}

    // Setter functions
    public void setTexturePath(String texturePath){this.texturePath = texturePath;}
    public void setUvs(float[] uvs){this.uvs = uvs;this.textureCoordinateBuffer.put(uvs).position(0);}
    public void setPos(float[] pos){this.pos = pos;this.setModelMatrix(pos[0], pos[1]);}

    // Setter function
    public void setBuffers(Integer[] buffers) {
        this.buffers = buffers;}

    // Constructors
    public Shape(ShapeType type, float[] coordinate, float[] dimensions, float[] uvs, String texturePath){
        this.shapeType = type;
        this.vertexBuffer = getVertexBuffer(dimensions[0], dimensions[1]);
        this.uvs = uvs;
        this.textureCoordinateBuffer = getTextureInformation();
        this.texturePath = texturePath;
        this.modelMatrix = new float[16];
        this.buffers = null;
        this.pos = coordinate;
        this.dim = dimensions;

        setVertexIndices();
        setModelMatrix(coordinate[0], coordinate[1]);
    }

    public Shape(ShapeType type, float[] coordinate, float[] dimensions, String texturePath) {
        this(type, coordinate, dimensions, Shape.defaultUvs.clone(), texturePath);
    }

    // Set the vertex buffer of the object (square)
    private FloatBuffer getVertexBuffer(float height, float width){
        float vertex[] = new float[]{
                0.0f, height, 0.0f,
                0.0f, 0.0f, 0.0f,
                width, height, 0.0f,
                0.0f, 0.0f, 0.0f,
                width, 0.0f, 0.0f,
                width, height, 0.0f
        };
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertex).position(0);
        return vertexBuffer;
    }

    // Set the order of rendering the vertex (counter clockwise)
    private void setVertexIndices(){
        this.vertexIndicesBuffer = ByteBuffer.allocateDirect(defaultIndices.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        this.vertexIndicesBuffer.put(defaultIndices).position(0);
    }

    // Set the texture location information (cover the whole tile)
    private FloatBuffer getTextureInformation() {
        FloatBuffer textureCoordinateBuffer = ByteBuffer.allocateDirect(uvs.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureCoordinateBuffer.put(uvs).position(0);
        return textureCoordinateBuffer;
    }

    // Set the position of the object in the "World"
    private void setModelMatrix(float x, float y){
        Matrix.setIdentityM(this.modelMatrix, 0);
        Matrix.translateM(this.modelMatrix, 0, x, y, 0);
    }
}
