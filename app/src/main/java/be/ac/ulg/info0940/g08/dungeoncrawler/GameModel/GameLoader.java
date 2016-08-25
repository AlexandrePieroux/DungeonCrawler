package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentData;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentDatabase;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader.LevelLoader;

/**
 * Created by alexp_000 on 01-04-16.
 */
public class GameLoader {
    // Static field
    private static final String JSON_EXTENSION = ".json";
    private static final String ENCODING = "UTF-8";
    private static final String HERO_FIELD = "hero";
    private static final String NPCS_FIELD = "npcs";
    private static final String BESTIARY_FIELD = "bestiary";
    private static final String MOBS_FIELD = "mobs";
    private static final String MAP_FIELD = "map";

    // Data
    private final Context context;

    private JSONObject initMap;
    private AgentData hero;
    private AgentDatabase npcs;
    private AgentDatabase bestiary;

    // Level loader
    LevelLoader loader;

    public GameLoader(Context context, String initFilePath){
        this.context = context;

        // Load init file
        JSONObject jsonInit = getFile(initFilePath);
        try {
            // Get the init map file path
            this.initMap = getFile(jsonInit.getString(GameLoader.MAP_FIELD));

            // Get hero file data
            this.hero = new AgentData(getFile(jsonInit.getString(GameLoader.HERO_FIELD)));

            // Get the npcs data (if it exist)
            if(jsonInit.has(GameLoader.NPCS_FIELD)) {
                this.npcs = new AgentDatabase(getFile(jsonInit.getString(GameLoader.NPCS_FIELD))
                        .getJSONArray(GameLoader.NPCS_FIELD));
            }

            // Get the bestiary data (if it exist)
            if(jsonInit.has(GameLoader.BESTIARY_FIELD)) {
                this.bestiary = new AgentDatabase(getFile(jsonInit.getString(GameLoader.BESTIARY_FIELD))
                        .getJSONArray(GameLoader.MOBS_FIELD));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Init the loader
        this.loader = new LevelLoader(this.hero, this.npcs, this.bestiary, this.context);
    }

    // Get the level loader (can be used to load new maps)
    public LevelLoader getLevelLoader(){
        return this.loader;
    }

    // Get the initial map
    public JSONObject getInitMap(){
        return this.initMap;
    }

    private JSONObject getFile(String path){
        JSONObject result = null;
        try {
            InputStream is = this.context.getAssets().open(path + GameLoader.JSON_EXTENSION);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            result = new JSONObject(new String(buffer, GameLoader.ENCODING));
        } catch (JSONException|IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
