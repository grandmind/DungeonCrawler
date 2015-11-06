package com.darichey.dungeonCrawler.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darichey.dungeonCrawler.item.stack.ItemStack;
import com.darichey.dungeonCrawler.screens.GameScreen;

/**
 * Used in Inventories to store items. Each slot stores one {@link com.darichey.dungeonCrawler.item.stack.ItemStack}
 */
public class Slot
{
    /**
     * The itemstack in this slot
     */
    private ItemStack itemStack;

    /**
     * The position at which this slot should be displayed in a GUI.
     */
    private Vector2 displayPos;

    public Label amountLabel = new Label("", GameScreen.labelStyle);
    public Image itemImage = new Image();
    public Image slotImage = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("Slot.png"))));

    public Slot(Vector2 displayPos)
    {
        this.setDisplayPos(displayPos);
        this.slotImage.setScale(2);
        this.slotImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                System.out.println("gvfdsgfds");
                return false;
            }
        });
    }

    public void setItemStack(ItemStack newStack)
    {
        this.itemStack = newStack;
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    public void addStack(ItemStack stack)
    {
        if (this.itemStack == null)
        {
            this.itemStack = stack;
        }
        else
        {
            this.itemStack.add(stack);
        }
    }

    public int getItemSpace()
    {
        return itemStack == null ? ItemStack.maxAmount : ItemStack.maxAmount - itemStack.getAmount();
    }

    public Vector2 getDisplayPos()
    {
        return this.displayPos;
    }

    public void setDisplayPos(Vector2 displayPos)
    {
        this.displayPos = displayPos;

        this.amountLabel.setPosition(displayPos.x + 20, displayPos.y + 8);
        this.slotImage.setPosition(displayPos.x, displayPos.y);
        this.itemImage.setPosition(displayPos.x, displayPos.y);
    }

    public void update()
    {
        if (getItemStack() != null)
        {
            this.itemImage.setDrawable(new TextureRegionDrawable(new TextureRegion(getItemStack().getItem().getTexture())));
            this.itemImage.setSize(getItemStack().getItem().getTexture().getWidth(), getItemStack().getItem().getTexture().getHeight());

            this.itemImage.setScale(2);

            this.amountLabel.setText(String.valueOf(getItemStack().getAmount()));
            this.amountLabel.setX(displayPos.x + 30 - (amountLabel.getText().length * 7));
        }
        else
        {
            this.itemImage.setDrawable(null);
            this.amountLabel.setText("");
        }
    }
}
