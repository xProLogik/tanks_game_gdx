package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Polygon;
import com.prologik.tanksgame.model.Bullet;
import com.prologik.tanksgame.view.GameScreen;

public class TankContoller {

  private Bullet bullet;
  private Polygon tankBounds;

  public TankContoller(Polygon bounds) {
    this.tankBounds = bounds;
  }

  private float speed = 10f;

  public void handle() {
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveUp();
    else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft();
    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight();
    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveDown();
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) shot();
  }

  private void shot() {
    bullet = new Bullet(GameScreen.textureAtlas.findRegion("bullet_top"), Heading.UP, -1f, 3f, 1f, 1f);
    bullet.draw(GameScreen.batch);
  }

  private void moveUp() {
    tankBounds.setRotation(0);
    tankBounds.setPosition(tankBounds.getX(), tankBounds.getY() + speed * GameScreen.deltaCff);
  }

  private void moveLeft() {
    tankBounds.setRotation(90);

    tankBounds.setPosition(tankBounds.getX() - speed * GameScreen.deltaCff, tankBounds.getY());
  }

  private void moveRight() {
    tankBounds.setRotation(-90);
    tankBounds.setPosition(tankBounds.getX() + speed * GameScreen.deltaCff, tankBounds.getY());
  }

  private void moveDown() {
    tankBounds.setRotation(180);
    tankBounds.setPosition(tankBounds.getX(), tankBounds.getY() - speed * GameScreen.deltaCff);
  }

}
