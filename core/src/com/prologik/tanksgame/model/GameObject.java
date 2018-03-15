package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.prologik.tanksgame.control.Heading;

abstract class GameObject {

  Polygon bounds;
  private Sprite object;
  private float width;
  private float height;

  GameObject(TextureRegion textureRegion, float x, float y, float width, float height) {
    bounds = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
    bounds.setOrigin(width / 2, height / 2);
    bounds.setPosition(x, y);
    object = new Sprite(textureRegion);
    object.setSize(width, height);
    object.setOrigin(width / 2, height / 2);
    this.width = width;
    this.height = height;
  }

  public void draw(SpriteBatch batch) {
    setFlip();
    object.setPosition(bounds.getX(), bounds.getY());
    object.setRotation(bounds.getRotation());
    object.draw(batch);

  }
  private void setFlip(){
    if (bounds.getRotation() == 90) object.setFlip(true, false);
    else if (bounds.getRotation() == 180) object.setFlip(true, false);
    else object.setFlip(false, false);
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }
}
