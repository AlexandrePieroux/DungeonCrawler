package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by alexp_000 on 08-04-16.
 */
public class AgentDatabase {
    private static final String NAME_FIELD = "name";
    private HashMap<String, AgentData> hashMap;

    public AgentDatabase(JSONArray jsonAgents){
        this.hashMap = new HashMap<>();
        try {
            for(int i = 0; i < jsonAgents.length(); i++) {
                JSONObject agent = jsonAgents.getJSONObject(i);
                this.hashMap.put(agent.getString(AgentDatabase.NAME_FIELD),
                                 new AgentData(agent));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AgentData getAgent(String name){
        return this.hashMap.get(name);
    }
}
