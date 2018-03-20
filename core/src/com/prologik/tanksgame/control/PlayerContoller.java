package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.Player;
import com.prologik.tanksgame.model.Tank;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class PlayerContoller {

  private Player player;

  public PlayerContoller(Player player) {
    this.player = player;
  }


  public void update(float delta) {

    Vector2 moveDirection = new Vector2();
    boolean moveXKeyPressed = false;
    boolean moveYKeyPressed = false;

    if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
      moveDirection.set(0.0f, 1.0f);
      moveYKeyPressed = !moveYKeyPressed;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      moveDirection.set(0.0f, -1.0f);
      moveYKeyPressed = !moveYKeyPressed;
    }

    if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
      moveDirection.set(-1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
    }
    if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
      moveDirection.set(1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
    }

    if (moveXKeyPressed || moveYKeyPressed) {
      player.move(delta, moveDirection);
    }


    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      player.shot();
    }
  }
}

