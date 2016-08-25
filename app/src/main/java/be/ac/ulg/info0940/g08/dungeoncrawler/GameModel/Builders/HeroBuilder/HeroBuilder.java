package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.HeroBuilder;

import android.content.Context;
import android.graphics.PointF;

import org.json.JSONException;
import org.json.JSONObject;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Hero.Hero;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.SceneComponent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.AgentDataBase.AgentData;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.Builders.Builder;

/**
 * Created by alexp_000 on 07-04-16.
 */
public class HeroBuilder extends Builder {

    private AgentData heroData;

    public HeroBuilder(AgentData hero, Context context){
        super.context = context;
        this.heroData = hero;
    }

    @Override
    public SceneComponent build(JSONObject pos) {
        Hero hero = null;
        try {
            String tex = heroData.getTexPath();
            String name = heroData.getName();
            float[] spawn = new float[]{pos.getInt("x"), pos.getInt("y")};

            // Load default sprite
            String defaultTex = tex + name + super.DEFAULT_SPRITE + super.IMAGE_EXTENSION;
            float[] dim = this.getDimensions(defaultTex, super.NB_FRAMES, super.NB_STANCES);

            // Set the hero
            hero = new Hero(tex,
                    name,
                    centerHero(spawn, dim),
                    dim,
                    this.getDefaultUvs());

            // Set the bounding box
            PointF[] bounds = this.heroData.getBoundingBox();
            PointF[] boundingBox = new PointF[bounds.length];
            for(int i = 0; i < bounds.length; i++) {
                boundingBox[i] = new PointF(spawn[0] + (dim[1] * bounds[i].x) - (dim[1] / 2),
                        spawn[1] + (dim[0] * bounds[i].y) - (dim[0] / 2));
            }

            hero.setBoundingBox(boundingBox);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hero;
    }

    private float[] centerHero(float[] center, float[] dim){
        return new float[]{center[0] - dim[1]/2, center[1] - dim[0]/2};
    }
}
