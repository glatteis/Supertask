package me.glatteis.supertask.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import me.glatteis.supertask.objects.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Linus on 09.12.2015.
 */
public class LevelHandler {

    protected ArrayList<Level> levels;
    protected int currentFocusLevel = 0;
    protected World world;
    protected Random random;
    private WorldHandler worldHandler;

    public LevelHandler(final World world, WorldHandler worldHandler) {
        random = new Random();
        this.world = world;
        levels = new ArrayList<Level>();
        TiledMap map = new TmxMapLoader().load("maps/start/startmap.tmx");
        Level levelOne = new Level(world, -10, 7, map, new Vector2(0, -20));
        levels.add(levelOne);
    }

    public void newLevelFellDown(Level l) {
        l.getBody().setType(BodyDef.BodyType.StaticBody);
     }

    public ArrayList<Level> getLevels() { return levels; }

    public void spawnNewLevel() {
        Level highestLevel = levels.get(levels.size() - 1);
        TiledMap map = highestLevel.getTiledMap();
        int ladder1 = getLadder(map, true);
        String option = "normal"; // TODO: 17.12.2015
        FileHandle thisFile = Gdx.files.internal("maps/" + option + "/" +
                MathUtils.random(1, 4) + ".tmx");
        TiledMap nextMap = new TmxMapLoader().load(thisFile.path());
        int ladder2 = getLadder(nextMap, false);
        if (ladder1 == -1) throw new RuntimeException("Ladder does not exist in " + highestLevel);
        if (ladder2 == -1) throw new RuntimeException("Ladder does not exist in " + thisFile);
        float nextX = ladder1 - ladder2 + highestLevel.getBody().getPosition().x;
        float nextY = highestLevel.getBody().getPosition().y + 30;
        Level newLevel = new Level(world, nextX, nextY, nextMap, new Vector2(0, 0));
        levels.add(newLevel);
    }

    private int getLadder(TiledMap map, boolean up) {
        TiledMapTileLayer ladders = (TiledMapTileLayer) map.getLayers().get("ladders");
        if (ladders == null) throw new RuntimeException("Found a level that has no ladders layer.");
        for (int x = 0; x < ladders.getWidth(); x++) {
            TiledMapTileLayer.Cell c = ladders.getCell(x, up ? ladders.getHeight() - 1 : 0);
            System.out.println(c);
            if (c != null) {
                return x;
            }
        }
        return -1;
    }

}
