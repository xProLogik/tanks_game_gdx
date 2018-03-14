package com.prologik.tanksgame.control;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.prologik.tanksgame.view.GameScreen;

public class TankContoller {

  private Polygon tankBounds;

  public TankContoller(Polygon bounds) {
    this.tankBounds = bounds;
  }

  private float speed=0, velocity = 10f, rotationSpeed = 30f;

  public void handle() {
    if (Gdx.input.isKeyPressed(Input.Keys.UP))
      speed += velocity * GameScreen.deltaCff;
    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
      speed -= velocity * GameScreen.deltaCff;
    else downSpeed();
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
      tankBounds.rotate(rotationSpeed * GameScreen.deltaCff);
    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
      tankBounds.rotate(-rotationSpeed * GameScreen.deltaCff);
    tankBounds.setPosition(tankBounds.getX() + MathUtils.cosDeg(tankBounds.getRotation() + 90) * speed * GameScreen.deltaCff,
        tankBounds.getY() + MathUtils.sinDeg(tankBounds.getRotation() + 90) * speed * GameScreen.deltaCff);



  }

  private void downSpeed() {
    if (speed > velocity * GameScreen.deltaCff)
      speed -= velocity * GameScreen.deltaCff;
    else if (speed < -velocity * GameScreen.deltaCff)
      speed += velocity * GameScreen.deltaCff;
    else speed = 0;
  }
}
