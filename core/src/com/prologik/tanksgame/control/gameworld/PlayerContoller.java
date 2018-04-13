package com.prologik.tanksgame.control.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.movableobjects.Player;

public class PlayerContoller {

  private Player player;

  public PlayerContoller(Player player) {
    this.player = player;
  }


  public void update(float delta, float runTime) {

    Vector2 moveDirection = new Vector2();
    boolean moveXKeyPressed = false;
    boolean moveYKeyPressed = false;

    if ((Gdx.input.isKeyPressed(Input.Keys.W) && player.getNumberPlayer() == 1) ||
        (Gdx.input.isKeyPressed(Input.Keys.UP) && player.getNumberPlayer() == 2)) {
      moveDirection.set(0.0f, 1.0f);
      moveYKeyPressed = !moveYKeyPressed;
      configForMove();
    }
    if ((Gdx.input.isKeyPressed(Input.Keys.S) && player.getNumberPlayer() == 1) ||
        (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.getNumberPlayer() == 2)) {
      moveDirection.set(0.0f, -1.0f);
      moveYKeyPressed = !moveYKeyPressed;
      configForMove();
    }

    if ((Gdx.input.isKeyPressed(Input.Keys.A) && player.getNumberPlayer() == 1) ||
        (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getNumberPlayer() == 2)) {
      moveDirection.set(-1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
      configForMove();
    }
    if ((Gdx.input.isKeyPressed(Input.Keys.D) && player.getNumberPlayer() == 1) ||
        (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getNumberPlayer() == 2)) {
      moveDirection.set(1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
      configForMove();
    }


    if (moveXKeyPressed || moveYKeyPressed) {
      player.move(delta, runTime, moveDirection);
    } else {
      player.moveOnIce(delta);
      player.setPlaySound(false);
      player.setTimeMoveSound(0);
    }


    if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.getNumberPlayer() == 1) ||
        (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && player.getNumberPlayer() == 2)) {
      player.shot();
    }
  }

  private void configForMove() {
    player.setTimeOnIce(0);
    player.setTimeDontMoveSound(0);
    player.setOnIce(false);
    player.setPlaySound(true);
  }
}

