package com.darichey.dungeonCrawler.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.darichey.dungeonCrawler.entity.base.GameEntity;
import com.darichey.dungeonCrawler.entity.block.BlockBase;
import com.darichey.dungeonCrawler.entity.living.EntityPlayer;
import com.darichey.dungeonCrawler.inventory.Slot;
import com.darichey.dungeonCrawler.item.placeable.ItemPlaceableBase;
import com.darichey.dungeonCrawler.item.stack.ItemStack;
import com.darichey.dungeonCrawler.world.World;

/**
 * Handles mouse and keyboard input
 */
public class InputHandler implements InputProcessor {
	private World world;
	private OrthographicCamera camera;
	private EntityPlayer player;

	public InputHandler(World world, OrthographicCamera camera) {
		this.world = world;
		this.camera = camera;
		this.player = world.player;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touchPos = new Vector3(screenX, screenY, 0);
		Vector3 unroundedWorldPos = camera.unproject(touchPos);
		Vector2 worldPos = new Vector2((float) Math.floor(unroundedWorldPos.x), (float) Math.floor(unroundedWorldPos.y));
		GameEntity entity = world.getEntityAt(worldPos);
		if (button == 0) {
			// Break block
			if (entity != null && entity instanceof BlockBase) {
				ItemStack stack = new ItemStack(entity.getPlaceable(), 1);
				Slot slot = player.getInventory().getNextValidSlotFor(stack);
				slot.addStack(stack);
				world.setEntityAt(null, worldPos);
			}
		} else if (button == 1) {
			// Place entity
			if (entity == null) {
				ItemStack stackInHand = player.getSelectedStack();
				if (stackInHand != null) {
					if (stackInHand.getItem().isPlaceable()) {
						GameEntity placeEntity = ((ItemPlaceableBase) stackInHand.getItem()).getEntity();
						if (stackInHand.amount != -1) {
							stackInHand.amount--;
							if (stackInHand.amount == 0) {
								player.getInventory().setStackInSlot(player.getSelectedSlotIndex(), null);
							}
						}
						world.setEntityAt(placeEntity, worldPos);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		player.setSelectedSlot(player.getSelectedSlotIndex() + amount);
		if (player.getSelectedSlotIndex() < 0)
			player.setSelectedSlot(9);
		if (player.getSelectedSlotIndex() > 9)
			player.setSelectedSlot(0);
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9) {
			player.setSelectedSlot(keycode == Input.Keys.NUM_0 ? 9 : keycode - 8);
			return true;
		}
		return false;
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			player.setVelocityX(player.getMovementSpeed());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			player.setVelocityX(-player.getMovementSpeed());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			player.setVelocityY(player.getMovementSpeed());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			player.setVelocityY(-player.getMovementSpeed());
		}
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
}