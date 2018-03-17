package com.prologik.tanksgame.model;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tank extends GameObject {

  private float speed = 10f;
  Tank(TextureRegion textureRegion, float x, float y, float width, float height) {
    super(textureRegion, x, y, width, height);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  public void moveUp(float delta) {
    bounds.setRotation(0);
    bounds.setPosition(bounds.getX(), bounds.getY() + speed*delta);
    if (bounds.getY()>10f) bounds.setPosition(bounds.getX(),-10f);
  }

  public void moveLeft(float delta) {
    bounds.setRotation(90);
    bounds.setPosition(bounds.getX() - speed*delta, bounds.getY());
    if (bounds.getX()<-13f) bounds.setPosition(13f,bounds.getY());
  }

  public void moveRight(float delta) {
    bounds.setRotation(-90);
    bounds.setPosition(bounds.getX() + speed*delta, bounds.getY());
    if (bounds.getX()>13f) bounds.setPosition(-13f,bounds.getY());
  }

  public void moveDown(float delta) {
    bounds.setRotation(180);
    bounds.setPosition(bounds.getX(), bounds.getY() - speed*delta);
    if (bounds.getY()<-13f) bounds.setPosition(bounds.getX(),13f);
  }
}
