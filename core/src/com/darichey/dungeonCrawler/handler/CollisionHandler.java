package com.darichey.dungeonCrawler.handler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.darichey.dungeonCrawler.entity.base.DynamicEntity;
import com.darichey.dungeonCrawler.init.Entities;
import com.darichey.dungeonCrawler.world.World;

/**
 * Detects and handles entity collision
 */
public class CollisionHandler extends HandlerBase
{
    private World world;

    public CollisionHandler(World world)
    {
        this.world = world;
    }

    @Override
    public void update()
    {
        for (DynamicEntity entity : world.getDynamicEntities())
        {
            Rectangle entityBounds = entity.getBounds();

        }
    }
}
