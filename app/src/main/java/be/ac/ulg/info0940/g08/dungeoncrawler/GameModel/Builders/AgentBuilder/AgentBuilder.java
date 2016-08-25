package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.AgentBuilder;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Agent.Agent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentData;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentDatabase;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.Builder;

/**
 * Created by alexp_000 on 02-04-16.
 */
public class AgentBuilder extends Builder{

    // AgentData field
    private static final String AGENT_NAME = "name";
    private static final String AGENT_POS = "pos";

    // Data
    private final AgentDatabase agentDatabase;

    // Constructor
    public AgentBuilder(AgentDatabase agentDatabase, Context context){
        super.context = context;
        this.agentDatabase = agentDatabase;
    }

    @Override
    public SceneComponent build(JSONObject object) {
        Agent agent = null;
        try{
                String name = object.getString(AgentBuilder.AGENT_NAME);
                JSONArray pos = object.getJSONArray(AgentBuilder.AGENT_POS);
                AgentData agentData = this.agentDatabase.getAgent(name);
                String tex = agentData.getTexPath();
                String nameTex = agentData.getName();
                String texturePath = tex + nameTex + super.DEFAULT_SPRITE + super.IMAGE_EXTENSION;
                float[] dim = getDimensions(texturePath, super.NB_FRAMES, super.NB_STANCES);

                // Create the agent
                agent = new Agent(tex,
                        nameTex,
                        new float[]{pos.getInt(0), pos.getInt(1)},
                        dim,
                        getDefaultUvs());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return agent;
    }
}
