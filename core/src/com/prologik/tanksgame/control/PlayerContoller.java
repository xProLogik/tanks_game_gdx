package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.Player;

public class PlayerContoller{

  private Player player;

  public PlayerContoller(Player player) {
    this.player = player;
  }



  public void update(float delta, float runTime) {

    Vector2 moveDirection = new Vector2();
    boolean moveXKeyPressed = false;
    boolean moveYKeyPressed = false;

    if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
      moveDirection.set(0.0f, 1.0f);
      moveYKeyPressed = !moveYKeyPressed;
      configForMove();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      moveDirection.set(0.0f, -1.0f);
      moveYKeyPressed = !moveYKeyPressed;
      configForMove();
    }

    if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
      moveDirection.set(-1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
      configForMove();
    }
    if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
      moveDirection.set(1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
      configForMove();
    }


    if (moveXKeyPressed || moveYKeyPressed) {
      player.move(delta,runTime, moveDirection);
    }
    else {
      player.moveOnIce(delta);
      player.setPlaySound(false);
      player.setTimeMoveSound(0);
    }



    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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

