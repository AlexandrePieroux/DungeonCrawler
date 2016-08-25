package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.IOException;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;

/**
 * Created by alexp_000 on 29-03-16.
 */
public abstract class Builder {
    // Public enum
    public enum BuilderType{
        HERO,
        NPCS,
        MOBS
    }

    // Default data
    protected static final int NB_FRAMES = 6;
    protected static final int NB_STANCES = 4;
    protected static final String DEFAULT_SPRITE = "_walk";
    protected static final String IMAGE_EXTENSION = ".png";

    // Context
    protected Context context;

    // Create the graphical representation
    public abstract SceneComponent build(JSONObject object);

    // Return the dimensions of a spritesheet in pixels
    protected float[] getDimensions(String path, int nbFrames, int nbStances){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.context.getAssets().open(path));
        } catch (IOException e) {e.printStackTrace();}
        return new float[]{bitmap.getHeight()/nbStances, bitmap.getWidth()/nbFrames};
    }

    // Return a matrix that represent the default drawing matrix in openGL
    protected float[] getDefaultUvs(){
        float stance = 1.0f/this.NB_STANCES;
        float frame = 1.0f/this.NB_FRAMES;
        return new float[] {
                0.0f, 0.0f,
                0.0f, stance,
                frame, 0.0f,
                0.0f, stance,
                frame, stance,
                frame, 0.0f
        };
    }
}
