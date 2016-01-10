package me.glatteis.supertask.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.glatteis.supertask.constants.Constants;
import me.glatteis.supertask.handlers.LevelTiledMapRenderer;

public class Level {

    private Body body;
    private int levelNumber;
    private Sprite sprite;

    private TiledMap tiledMap;
    private LevelTiledMapRenderer renderer;

    private float width;
    private float height;

    public Body getBody() { return body; }

    public int getLevelNumber() { return levelNumber; }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Level(World world, float x, float y, TiledMap tiledMap, Vector2 velocity) {
        this(world, x, y, tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class) / Constants.PIXELS_TO_METERS,
                tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class) / Constants.PIXELS_TO_METERS, tiledMap, velocity);
    }

    public Level(World world, float x, float y, float width, float height, TiledMap tiledMap, Vector2 velocity) {
        this.tiledMap = tiledMap;
        this.width = width;
        this.height = height;

        renderer = new LevelTiledMapRenderer(tiledMap, new Vector2(x, y), new Vector2(width, height));

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.linearVelocity.set(velocity);

        body = world.createBody(def);

        for (MapLayer l : tiledMap.getLayers()) {
            if (!(l instanceof  TiledMapTileLayer)) continue;
            generateBody((TiledMapTileLayer) l);
        }
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.updatePosition(body.getPosition());
        renderer.render();
    }

    public void generateBody(TiledMapTileLayer layer) {
        float tileSize = layer.getTileWidth();
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        float width = layer.getWidth() / 2;
        float height = layer.getHeight() / 2;
        // go through all the cells in the layer
        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check if cell exists
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                ChainShape cs = new ChainShape();
                Vector2[] v = {
                        new Vector2(col + 0.5f + tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f - height + tileSize / 2 / Constants.PIXELS_TO_TILES),
                        new Vector2(col + 0.5f - tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f  - height  + tileSize / 2 / Constants.PIXELS_TO_TILES),
                        new Vector2(col + 0.5f - tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f - height - tileSize / 2 / Constants.PIXELS_TO_TILES),
                        new Vector2(col + 0.5f + tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f  - height - tileSize / 2 / Constants.PIXELS_TO_TILES),
                         new Vector2(col + 0.5f + tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f  - height + tileSize / 2 / Constants.PIXELS_TO_TILES)
                };



                cs.createChain(v);
                fDef.shape = cs;
                fDef.density = 1;
                fDef.friction = 1;

                fDef.filter.groupIndex = 2;
                fDef.filter.maskBits = 1 | 2;
                fDef.filter.categoryBits = 2;


                body.createFixture(fDef).setUserData(new LevelUserData(this, new Vector2(new Vector2(col + 0.5f + tileSize / 2 / Constants.PIXELS_TO_TILES, row + 0.5f - height + tileSize / 2 / Constants.PIXELS_TO_TILES)), layer.getName()));
                cs.dispose();
            }
        }

        FixtureDef outerDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.set(new float[] { 0, -height, width * 2, -height, width * 2, height, 0, height});
        outerDef.shape = shape;

        outerDef.filter.categoryBits = Constants.COLLISION_LEVEL_OUTISDE;
        outerDef.filter.maskBits = Constants.COLLISION_LEVEL_OUTISDE;

        body.createFixture(outerDef).setUserData(new LevelUserData(this, null, "outer"));

    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public class LevelUserData {
        public String type;
        public Level level;
        public Vector2 position;
        protected LevelUserData(Level level, Vector2 position, String type) {
            this.type = type;
            this.level = level;
            this.position = position;
        }
    }


}
