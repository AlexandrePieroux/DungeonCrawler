package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Agent.Agent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Hero.Hero;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Map.Map;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentData;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentDatabase;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.AgentBuilder.AgentBuilder;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.HeroBuilder.HeroBuilder;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.MapBuilder.MapBuilder;

/**
 * Created by alexp_000 on 06-04-16.
 */
public class LevelLoader {
    private static final String MAP_FIELD = "map";
    private static final String MAP_BOUNDS_FIELD = "bounds";
    private static final String NPC_FIELD = "npcs";
    private static final String MOB_FIELD = "mobs";
    private static final String HERO_SPAWN_POS_FIELD = "hero_spawn";

    private final Context context;

    private MapBuilder mapBuilder;
    private AgentBuilder npcBuilder;
    private AgentBuilder mobBuilder;
    private HeroBuilder heroBuilder;

    public LevelLoader(AgentData hero, AgentDatabase npcs, AgentDatabase bestiary, Context context){
        // Initialisation
        this.context = context;
        this.mapBuilder = new MapBuilder(this.context);
        this.npcBuilder = new AgentBuilder(npcs, this.context);
        this.mobBuilder = new AgentBuilder(bestiary, this.context);
        this.heroBuilder = new HeroBuilder(hero, this.context);
    }

    public Level load(JSONObject level){
        Level result = new Level();
        try {
            // Get where the view must point at on the map at start
            result.setViewInitPos(new float[]{level.getJSONArray(LevelLoader.HERO_SPAWN_POS_FIELD).getInt(0),
                    level.getJSONArray(LevelLoader.HERO_SPAWN_POS_FIELD).getInt(1)});

            // Getting the hero
            JSONArray names = new JSONArray().put("x").put("y");
            JSONObject pos = level.getJSONArray(LevelLoader.HERO_SPAWN_POS_FIELD).toJSONObject(names);
            result.setHero((Hero) this.heroBuilder.build(pos));

            // Getting the map
            result.setMap((Map) this.mapBuilder.build(level.getJSONObject(LevelLoader.MAP_FIELD)));

            // Getting the npcs (if exist)
            if(level.has(LevelLoader.NPC_FIELD)) {
                JSONArray npcsJson = level.getJSONArray(LevelLoader.NPC_FIELD);
                Agent[] npcs = new Agent[npcsJson.length()];
                for(int i = 0; i  < npcsJson.length(); i++)
                    npcs[i] = (Agent) this.npcBuilder.build(npcsJson.getJSONObject(i));
                result.setNpcs(npcs);
            }

            // Getting the mobs (if exist)
            if(level.has(LevelLoader.MOB_FIELD)) {
                JSONArray mobsJson = level.getJSONArray(LevelLoader.MOB_FIELD);
                Agent[] mobs = new Agent[mobsJson.length()];
                for(int i = 0; i  < mobsJson.length(); i++)
                    mobs[i] = (Agent) this.mobBuilder.build(mobsJson.getJSONObject(i));
                result.setMobs(mobs);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
