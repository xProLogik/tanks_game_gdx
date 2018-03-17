package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Polygon;
import com.prologik.tanksgame.model.Bullet;
import com.prologik.tanksgame.model.Tank;
import com.prologik.tanksgame.view.GameScreen;

public class PlayerContoller {

  private Tank tank;

  public PlayerContoller( Tank tank) {
    this.tank = tank;
  }

  public void handle(float delta) {
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) tank.moveUp(delta);
    else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) tank.moveLeft(delta);
    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) tank.moveRight(delta);
    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) tank.moveDown(delta);
  }





}
