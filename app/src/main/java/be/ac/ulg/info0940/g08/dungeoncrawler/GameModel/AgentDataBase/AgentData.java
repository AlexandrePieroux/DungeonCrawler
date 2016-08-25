package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase;

import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexp_000 on 08-04-16.
 */
public class AgentData {
    // Fields
    private static final String TEX_PATH_FIELD = "sprites_folder";
    private static final String NAME_FIELD = "name";
    private static final String BOUNDING_BOX_FIELD = "bounding_box";

    // Data
    private String tex;
    private String name;
    private PointF[] boundingBox;
    // TODO fields to add

    public AgentData(JSONObject agent){
        try{
            this.tex = agent.getString(AgentData.TEX_PATH_FIELD);
            this.name = agent.getString(AgentData.NAME_FIELD);

            JSONArray boundingJson = agent.getJSONArray(AgentData.BOUNDING_BOX_FIELD);
            this.boundingBox = new PointF[boundingJson.length()];
            for(int i = 0; i < boundingJson.length(); i++){
                this.boundingBox[i] = new PointF((float) boundingJson.getJSONArray(i).getDouble(0),
                        (float) boundingJson.getJSONArray(i).getDouble(1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTexPath(){
        return this.tex;
    }

    public String getName(){
        return this.name;
    }

    public PointF[] getBoundingBox() {
        return boundingBox;
    }
}
