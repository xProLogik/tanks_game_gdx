package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

  private Sprite object;
  public static int PIXEL_SIZE_SPRITE = 8;

  GameObject(String nameRegion, Vector2 position, float width, float height) {
    object = new Sprite(GameWorld.textureAtlas.findRegion(nameRegion));
    object.setBounds(position.x, position.y, width, height);
    object.setSize(width, height);
    object.setOrigin(width / 2, height / 2);
    object.setPosition(position.x, position.y);
  }

  public void draw(SpriteBatch batch) {
    setFlip();
    object.draw(batch);

  }

  private void setFlip() {
    if (getObject().getRotation() == 90) object.setFlip(true, false);
    else if (getObject().getRotation() == 180) object.setFlip(true, false);
    else object.setFlip(false, false);
  }

  Rectangle getBounds() {
    return object.getBoundingRectangle();
  }

  public Sprite getObject() {
    return object;
  }
}

