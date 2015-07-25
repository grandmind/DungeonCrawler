package com.darichey.dungeonCrawler.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.darichey.dungeonCrawler.entity.block.EntityBlock;
import com.darichey.dungeonCrawler.entity.living.EntityPlayer;
import com.darichey.dungeonCrawler.entity.base.GameEntity;
import com.darichey.dungeonCrawler.event.EventManager;
import com.darichey.dungeonCrawler.event.world.block.BlockPlacedEvent;
import com.darichey.dungeonCrawler.init.Entities;
import com.darichey.dungeonCrawler.world.World;

/**
 * Handles mouse and keyboard input
 */
public class InputHandler extends InputAdapter
{
    private World world;
    private OrthographicCamera camera;
    private EntityPlayer player;

    public InputHandler(World world, OrthographicCamera camera)
    {
        this.world = world;
        this.camera = camera;
        this.player = world.player;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        Vector3 unroundedWorldPos = camera.unproject(touchPos);
        Vector2 worldPos = new Vector2((float) Math.floor(unroundedWorldPos.x), (float) Math.floor(unroundedWorldPos.y));
        GameEntity entity = world.getEntityAt(worldPos);
        if (entity == null)
        {
            if (button == 0)
            {
                if (EventManager.post(new BlockPlacedEvent(this.player, (EntityBlock) Entities.block))) return false;
                world.setEntityAt(Entities.block, worldPos);
            }
            else if (button == 1)
            {
                if (EventManager.post(new BlockPlacedEvent(this.player, (EntityBlock) Entities.block2))) return false;
                world.setEntityAt(Entities.block2, worldPos);
            }
        }
        else
        {
            world.setEntityAt(null, worldPos);
        }
        return true;
    }

    public void update()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            player.setVelocityX(player.getMovementSpeed());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            player.setVelocityX(-player.getMovementSpeed());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            player.setVelocityY(player.getMovementSpeed());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            player.setVelocityY(-player.getMovementSpeed());
        }
    }
}