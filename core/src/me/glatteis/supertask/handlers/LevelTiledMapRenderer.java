package me.glatteis.supertask.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class LevelTiledMapRenderer extends BatchTiledMapRenderer {

    private Vector2 position;
    private Vector2 size;

    public LevelTiledMapRenderer (TiledMap map, Vector2 position, Vector2 size) {
        super(map);
        this.position = position;
        this.size = size;
    }

    public LevelTiledMapRenderer (TiledMap map, Batch batch, Vector2 position, Vector2 size) {
        super(map, batch);
        this.position = position;
        this.size = size;
    }

    public LevelTiledMapRenderer (TiledMap map, float unitScale, Vector2 position, Vector2 size) {
        super(map, unitScale);
        this.position = position;
        this.size = size;
    }

    public LevelTiledMapRenderer (TiledMap map, float unitScale, Batch batch, Vector2 position, Vector2 size) {
        super(map, unitScale, batch);
        this.position = position;
        this.size = size;
    }

    public void update(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    public void updatePosition(Vector2 position) {
        this.position = position;
    }

    public void updateSize(Vector2 size) {
        this.size = size;
    }

    @Override
    public void renderTileLayer (TiledMapTileLayer layer) {

        final float layerTileConstant = 0.0625f;

        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * layerTileConstant * unitScale;
        final float layerTileHeight = layer.getTileHeight() * layerTileConstant * unitScale;

        final int col1 = Math.max(0, (int)(viewBounds.x / layerTileWidth - position.x));
        final int col2 = Math.min(layerWidth, (int)((viewBounds.x + viewBounds.width / layerTileWidth) - position.x + 1));

        final int row1 = Math.max(0, (int)((viewBounds.y / layerTileHeight) - position.y));
        final int row2 = Math.min(layerHeight, (int)(((size.y + viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight) + position.y));

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;
        final float[] vertices = this.vertices;

        for (int row = row2; row >= row1; row--) {
            float x = xStart;
            for (int col = col1; col < col2; col++) {
                final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) {
                    x += layerTileWidth;
                    continue;
                }
                final TiledMapTile tile = cell.getTile();

                if (tile != null) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() + position.x * unitScale;
                    float y1 = y + tile.getOffsetY() + position.y - size.y * unitScale;
                    float x2 = x1 + region.getRegionWidth() * layerTileConstant * unitScale;
                    float y2 = y1 + region.getRegionHeight() * layerTileConstant * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
                }
                x += layerTileWidth;
            }
            y -= layerTileHeight;
        }
    }
}