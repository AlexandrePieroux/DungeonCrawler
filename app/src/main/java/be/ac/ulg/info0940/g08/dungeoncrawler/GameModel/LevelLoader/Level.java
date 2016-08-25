package be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Agent.Agent;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Living.Hero.Hero;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.SceneComponent.Map.Map;

/**
 * Created by alexp_000 on 26-04-16.
 */
public class Level {
    private float[] viewInitPos;
    private Hero hero;
    private Map map;
    private Agent[] mobs;
    private Agent[] npcs;

    public Level(){
        this.viewInitPos = null;
        this.hero = null;
        this.map = null;
        this.mobs = null;
        this.npcs = null;
    }

    public float[] getViewInitPos() {
        return viewInitPos;
    }

    public Agent[] getNpcs() {
        return npcs;
    }

    public Agent[] getMobs() {
        return mobs;
    }

    public Map getMap() {
        return map;
    }

    public Hero getHero() {
        return hero;
    }

    public void setViewInitPos(float[] pos){
        this.viewInitPos = pos;
    }

    public void setHero(Hero hero){
        this.hero = hero;
    }

    public void setMobs(Agent[] mobs){
        this.mobs = mobs;
    }

    public void setNpcs(Agent[] npcs){
        this.npcs = npcs;
    }

    public void setMap(Map map){
        this.map = map;
    }
}
