package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.MapBuilder;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Map.Map;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.Builder;

/**
 * Created by alexp_000 on 29-03-16.
 */
public class MapBuilder extends Builder {
    private static final String VOID_TEX = "void";
    private static final String TEXTURE_FOLDER_FIELD = "textures_path";

    // Folder that contain all the sprites
    private String spritePath;

    // First layer fields
    private static final String MAP_GROUND = "ground";
    private static final String MAP_GROUND_TILE_RES = "tile_map_resolution";
    private static final String MAP_LAYERS = "layers";
    private static final String MAP_POLYS = "polygons";
    private static final String MAP_BOUNDS = "bounds";

    // Additional layers fields
    private static final String ELEMENT_POSITION = "pos";
    private static final String ELEMENT_TEXTURE_NAME = "tex";

    private JSONArray supLayerMap = null;
    private JSONArray ground = null;
    private JSONArray polys = null;
    private JSONArray bounds = null;
    private int tileResHeight;
    private int tileResWidth;
    private PointF[] edges;

    public MapBuilder(Context context) {
        super.context = context;
    }

    @Override
    public SceneComponent build(JSONObject object) {
        Map result = new Map();

        try {
            // Parsing the object
            this.spritePath = object.getString(MapBuilder.TEXTURE_FOLDER_FIELD);
            this.tileResHeight = object.getJSONArray(MapBuilder.MAP_GROUND_TILE_RES).getInt(0);
            this.tileResWidth = object.getJSONArray(MapBuilder.MAP_GROUND_TILE_RES).getInt(1);
            this.ground = object.getJSONArray(MapBuilder.MAP_GROUND);
            this.bounds = object.getJSONArray(MapBuilder.MAP_BOUNDS);
            if (object.has(MapBuilder.MAP_POLYS))
                this.polys = object.getJSONArray(MapBuilder.MAP_POLYS);
            if (object.has(MapBuilder.MAP_LAYERS))
                this.supLayerMap = object.getJSONArray(MapBuilder.MAP_LAYERS);

            // Adding the primary layer: the ground
            getGroundLayer(result);

            // Get the bounds
            getBounds(result);

            // There is no additional layers
            if (this.supLayerMap == null)
                return result;

            // Adding the additional layers
            if (object.has(MapBuilder.MAP_LAYERS))
                getAdditionalLayers(result);

            // Get the obstacles
            if (object.has(MapBuilder.MAP_POLYS))
                getPoly(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getGroundLayer(Map out) throws JSONException {
        int height = this.ground.length();
        for (int i = 0; i < height; i++) {
            JSONArray line = this.ground.getJSONArray(i);
            int width = line.length();
            for (int j = 0; j < width; j++) {
                String tileName = line.getString(j) + super.IMAGE_EXTENSION;
                if (!line.getString(j).equals(MapBuilder.VOID_TEX)) {
                    out.addTile(this.spritePath + tileName,
                            new float[]{j * this.tileResWidth, (height - i - 1) * this.tileResHeight},
                            getDimensions(this.spritePath + tileName, 1, 1));
                }
            }
        }
    }

    private void getAdditionalLayers(Map out) throws JSONException {
        int nbLayers = this.supLayerMap.length();
        for (int i = 0; i < nbLayers; i++) {
            JSONArray layer = this.supLayerMap.getJSONArray(i);
            int layerLength = layer.length();
            for (int j = 0; j < layerLength; j++) {
                JSONObject element = layer.getJSONObject(j);
                String texName = this.spritePath + element.getString(MapBuilder.ELEMENT_TEXTURE_NAME) + super.IMAGE_EXTENSION;
                out.addTile(texName,
                        new float[]{element.getJSONArray(MapBuilder.ELEMENT_POSITION).getInt(0),
                                element.getJSONArray(MapBuilder.ELEMENT_POSITION).getInt(1)},
                        getDimensions(texName, 1, 1));
            }
        }
    }

    private void getBounds(Map out) throws JSONException {
        PointF[] result = new PointF[this.bounds.length()];
        for(int i = 0;i < this.bounds.length(); i++) {
            result[i] = new PointF(this.bounds.getJSONArray(i).getInt(0),
                    this.bounds.getJSONArray(i).getInt(1));
        }
        out.setBounds(result);
    }

    private void getPoly(Map out) throws JSONException {
        int nbPolys = this.polys.length();
        for(int i = 0; i < nbPolys; i++){
            JSONArray poly = this.polys.getJSONArray(i);
            int nbVertex = poly.length();
            PointF[] polygon = new PointF[nbVertex];
            for(int j = 0; j < nbVertex; j++){
                JSONArray vertex = poly.getJSONArray(j);
                polygon[j] = new PointF(vertex.getInt(0), vertex.getInt(1));
            }
            out.addPolygon(polygon);
        }
    }
}
