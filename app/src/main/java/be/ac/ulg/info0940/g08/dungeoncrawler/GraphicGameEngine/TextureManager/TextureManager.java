package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.TextureManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexandre on 15/03/16.
 */
public class TextureManager {
    private static final String defaultTexturePath = "drawable/dft";
    private static Context contextManager;
    private static HashMap<String, Integer> texture_db = new HashMap<>();
    private static int defaultId;
    private static boolean initialized = false;

    public static void initialize(Context context){
        if(initialized)
            return;

        contextManager = context;
        releaseTextures();

        // Load default texture
        loadDefaultTexture();
        initialized = true;
    }

    public static int loadTexture(String path){
        int id = getTexture(path);
        if(defaultId != id)
            return id;
        return loadTextureOpenGL(path);
    }

    public static int getTexture(String path){
        Integer id = texture_db.get(path);
        if(id == null)
            return defaultId;
        return id.intValue();
    }

    public static String getTextureName(int id){
        Set<Map.Entry<String, Integer>> entrySet = texture_db.entrySet();
        String name = null;
        for (Map.Entry<String, Integer> entry: entrySet){
            if(entry.getValue().intValue() == id)
                name = entry.getKey();
        }
        return name;
    }

    public static void releaseTextures(){
        Set<String> nameSet = texture_db.keySet();
        for(String name: nameSet){
            deleteTexture(name);
        }
    }

    public static void deleteTexture(String path){
        int id = texture_db.get(path);
        if(id == defaultId)
            return;
        texture_db.remove(path);
        int texture[] = new int[]{id};
        GLES20.glDeleteTextures(1, texture, 0);
    }

    public static void deleteTexture(int id){
        texture_db.remove(getTextureName(id));
        int texture[] = new int[]{id};
        GLES20.glDeleteTextures(1, texture, 0);
    }

    private static int loadTextureOpenGL(String path){
        // Load the texture from assets folder
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(contextManager.getAssets().open(path));
        } catch (IOException e) {e.printStackTrace(); return defaultId;}

        // Bind to the texture in OpenGL
        int id = bindOpenGl(bitmap);
        bitmap.recycle();

        texture_db.put(path, new Integer(id));
        return id;
    }

    private static void loadDefaultTexture(){
        // Load the texture from resource folder
        int id = contextManager.getResources().getIdentifier(defaultTexturePath, null, contextManager.getPackageName());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        final Bitmap bitmap = BitmapFactory.decodeResource(contextManager.getResources(), id, options);

        int idTex = bindOpenGl(bitmap);
        bitmap.recycle();

        texture_db.put("default", new Integer(idTex));
        defaultId = idTex;
    }

    private static int bindOpenGl(Bitmap bitmap){
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
            throw new RuntimeException("Error loading texture.");

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        return textureHandle[0];
    }
}
