package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Display;
import android.view.WindowManager;

import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GraphicScene.GraphicScene;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.Shape;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.ShapeManager.ShapeType;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.TextureManager.TextureManager;

/**
 * Created by alexandre on 16/03/16.
 */
public class GlRenderer implements GLSurfaceView.Renderer {

    // Public attributes
    private GraphicScene scene;

    // Matrices
    private float[] mtrxProjection = new float[16];
    private float[] mtrxView = new float[16];
    private float[] mtrxEffectiveRepresentation = new float[16];

    // GraphicScene resolution
    int screenWidth;
    int screenHeight;

    // Misc
    private Context context;
    private long lastTime;
    private float[] viewPos;

    // Handlers
    private int mPositionHandle;
    private int mTexCoordLoc;
    private int mtrxhandle;
    private int mSamplerLoc;

    public GlRenderer(Context context){
        this.context = context;
        this.lastTime = System.currentTimeMillis();

        // Set screen dimensions
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        // Set default matrix
        Matrix.setLookAtM(mtrxView, 0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.orthoM(mtrxProjection, 0, 0f, screenWidth, 0.0f, screenHeight, 0, 50);
        Matrix.multiplyMM(mtrxEffectiveRepresentation, 0, mtrxProjection, 0, mtrxView, 0);

        // Default scene
        this.scene = null;
    }

    public void onPause(){
        synchronized (this.scene) {
            this.scene.setSceneChanged();
            this.scene.notify();
        }
    }

    public void onResume(){
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Initialize TextureManager
        TextureManager.initialize(context);

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Create the shaders, images
        int vertexShader = GlShader.loadShader(GLES20.GL_VERTEX_SHADER, GlShader.vs_Image);
        int fragmentShader = GlShader.loadShader(GLES20.GL_FRAGMENT_SHADER, GlShader.fs_Image);
        GlShader.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(GlShader.sp_Image, vertexShader);
        GLES20.glAttachShader(GlShader.sp_Image, fragmentShader);
        GLES20.glLinkProgram(GlShader.sp_Image);

        // Set our shader programm
        GLES20.glUseProgram(GlShader.sp_Image);

        // Get handles in shader program
        this.mPositionHandle = GLES20.glGetAttribLocation(GlShader.sp_Image, "vPosition");
        this.mTexCoordLoc = GLES20.glGetAttribLocation(GlShader.sp_Image, "a_texCoord" );
        this.mtrxhandle = GLES20.glGetUniformLocation(GlShader.sp_Image, "uMVPMatrix");
        this.mSamplerLoc = GLES20.glGetUniformLocation(GlShader.sp_Image, "s_texture" );
    }

    @Override
    public void onSurfaceChanged(GL10 glNotUsed, int width, int height) {
        this.screenHeight = height;
        this.screenWidth = width;

        // Define the new viewport to be fullscreen again
        GLES20.glViewport(0, 0, this.screenWidth, this.screenHeight);

        synchronized (this.scene) {
            this.mtrxEffectiveRepresentation = new float[16];
            this.mtrxProjection = new float[16];

            // Redefine projection matrix with new width and height
            Matrix.orthoM(this.mtrxProjection, 0, 0f, this.screenWidth, 0.0f, this.screenHeight, 0, 50);

            // Compute the effective representation matrix on the screen
            Matrix.multiplyMM(this.mtrxEffectiveRepresentation, 0, this.mtrxProjection, 0, this.mtrxView, 0);

            this.scene.setSceneChanged();
            this.scene.notify();
        }
    }

    @Override
    public void onDrawFrame(GL10 glNotUsed) {
        // Check if we are valid and sane
        long now = System.currentTimeMillis();
        if(lastTime > now)
            return;

        /*
        * Have to block the queue when the list of item to draw is updated by the game state logic.
        * (This method can sleep if nothing changed).
        */
        synchronized (this.scene) {
            while (!this.scene.sceneChanged()) {
                try {
                    this.scene.wait();
                } catch (InterruptedException e) {/*No big deal*/}
            }
            this.scene.unsetSceneChanged();

            // Clear GraphicScene and Depth Buffer,
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            for(SceneComponent g:this.scene)
                render(g.getDrawingStack());

            this.scene.notify();
        }
        this.lastTime = now;
    }

    public void setScene(GraphicScene scene){
        this.scene = scene;
        this.scene.setSceneChanged();
    }

    public void lookAt(float[] posToView) {
        setView(new float[]{posToView[0] - (this.screenWidth / 2), posToView[1] - (this.screenHeight / 2)});
    }

    private void setView(float[] view){
        this.viewPos = view;
        synchronized (this.scene){
            this.mtrxView = new float[16];
            this.mtrxEffectiveRepresentation = new float[16];

            Matrix.setLookAtM(this.mtrxView, 0, view[0], view[1], 1f, view[0], view[1], 0f, 0f, 1.0f, 0.0f);
            Matrix.multiplyMM(this.mtrxEffectiveRepresentation, 0, this.mtrxProjection, 0, this.mtrxView, 0);

            this.scene.setSceneChanged();
            this.scene.notify();
        }
    }

    private void render(Stack<Shape> t){
        for(Shape s: t){
            if(visible(s))
                draw(s);
        }
    }

    private boolean visible(Shape s) {
        float[] pos = s.getPos();
        PointF[] shape = new PointF[]{
                new PointF(pos[0], pos[1]),
                new PointF(pos[0] + s.getDim()[1], pos[1]),
                new PointF(pos[0] + s.getDim()[1], pos[1] + s.getDim()[0]),
                new PointF(pos[0], pos[1] + s.getDim()[0])
        };
        for(PointF p : shape)
            if(p.x >= this.viewPos[0] && p.x <= (this.viewPos[0] + this.screenWidth) &&
                    p.y >= this.viewPos[1] && p.y <= (this.viewPos[1] + this.screenHeight))
                return true;
        return false;
    }

    private void draw(Shape shape)
    {
        // Bufferize the shape
        if(shape.getBuffers() == null)
            bufferize(shape);

        // Set position information of the vertex
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shape.getBuffers()[0]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);

        // Set the active texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
                TextureManager.loadTexture(shape.getTexturePath()));

        if(shape.getType() == ShapeType.ANIMATED){
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            shape.getTextureCoordinate().position(0);
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false,
                    0, shape.getTextureCoordinate());
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        } else {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, shape.getBuffers()[1]);
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        }

        // Allow transparency
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // Use the given texture
        GLES20.glUniform1i(mSamplerLoc, 0);

        // This multiplies the effective representation matrix by the model matrix
        float mer[] = new float[16];
        Matrix.multiplyMM(mer, 0, mtrxEffectiveRepresentation, 0, shape.getModelMatrix(), 0);
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mer, 0);

        // Draw the element
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                shape.getIndicesLength(),
                GLES20.GL_UNSIGNED_SHORT,
                shape.getVertexIndicesBuffer());

        // Disable vertex array
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void bufferize(Shape s){
        int[] buffers = new int[2];
        if(s.getType() == ShapeType.ANIMATED) {
            GLES20.glGenBuffers(1, buffers, 0);
        } else {
            GLES20.glGenBuffers(2, buffers, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                    s.getTextureCoordinate().capacity() * 4,
                    s.getTextureCoordinate(),
                    GLES20.GL_STATIC_DRAW);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                s.getVertex().capacity() * 4,
                s.getVertex(),
                GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        s.setBuffers(new Integer[]{new Integer(buffers[0]), new Integer(buffers[1])});
    }
}
