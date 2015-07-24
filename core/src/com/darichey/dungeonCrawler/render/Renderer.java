package com.darichey.dungeonCrawler.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.darichey.dungeonCrawler.entity.base.DynamicEntity;
import com.darichey.dungeonCrawler.entity.base.GameEntity;
import com.darichey.dungeonCrawler.world.World;

/**
 * Renders all game objects within the world
 */
public class Renderer
{
    private SpriteBatch batch;
    private World world;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    public OrthographicCamera camera;
    private boolean debugRender = true;

    private int VIEWPORT_WIDTH = 60;
    private int VIEWPORT_HEIGHT = 32;

    public Renderer(World world)
    {
        this.world = world;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public void render()
    {
        Gdx.gl.glClearColor(0f, 10f, 200f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.x = world.player.getPos().x;
        camera.position.y = world.player.getPos().y;
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderWalls();
        renderDynamics();
        batch.end();

        if (debugRender) renderDebug();
    }

    public void renderDebug()
    {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(1, 1, 0, 1);
        renderWallDebug();
        renderDynamicsDebug();
        debugRenderer.end();
    }

    private void renderWalls()
    {
        for (int y = 0; y < World.height; y++)
        {
            for (int x = 0; x < World.width; x++)
            {
                Vector2 pos = new Vector2(x, y);
                GameEntity entity = world.getEntityAt(pos);
                if (entity != null)
                {
                    if (entityCanBeeSeenAt(entity, pos))
                    {
                        batch.draw(entity.getTexture(), x, y, entity.width, entity.height);
                    }
                }
            }
        }
    }

    private void renderDynamics()
    {
        for (DynamicEntity entity : world.getDynamicEntities())
        {
            if (entityCanBeeSeenAt(entity, entity.getPos()))
                batch.draw(entity.getTexture(), entity.getPos().x, entity.getPos().y, entity.width, entity.height);
        }
    }

    private void renderWallDebug()
    {
        for (int y = 0; y < World.height; y++)
        {
            for (int x = 0; x < World.width; x++)
            {
                Vector2 pos = new Vector2(x, y);
                GameEntity entity = world.getEntityAt(pos);
                if (entity != null)
                {
                    if (entityCanBeeSeenAt(entity, pos))
                    {
                        debugRenderer.rect(x, y, entity.width, entity.height);
                    }
                }
            }
        }
    }

    private void renderDynamicsDebug()
    {
        for (DynamicEntity entity : world.getDynamicEntities())
        {
            if (entityCanBeeSeenAt(entity, entity.getPos()))
            {
                debugRenderer.rect(entity.getPos().x, entity.getPos().y, entity.width, entity.height);
            }
        }
    }

    private boolean entityCanBeeSeenAt(GameEntity entity, Vector2 worldPos)
    {
        Vector3 bottomLeft = camera.project(new Vector3(worldPos.x, worldPos.y, 0));
        Vector3 topLeft = camera.project(new Vector3(worldPos.x, worldPos.y + entity.height, 0));
        Vector3 bottomRight = camera.project(new Vector3(worldPos.x + entity.width, worldPos.y, 0));
        Vector3 topRight = camera.project(new Vector3(worldPos.x + entity.width, worldPos.y + entity.height, 0));

        return screenContainsPos(bottomLeft) || screenContainsPos(topLeft) || screenContainsPos(bottomRight) || screenContainsPos(topRight);
    }

    private boolean screenContainsPos(Vector3 pos)
    {
        if (pos.x > 0 && pos.x < Gdx.graphics.getWidth())
        {
            if (pos.y > 0 && pos.y < Gdx.graphics.getHeight())
            {
                return true;
            }
        }
        return false;
    }
}
