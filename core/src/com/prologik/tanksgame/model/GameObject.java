package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject{

  private Polygon bounds;
  private Sprite object;


  GameObject(String nameRegion, Vector2 position, float width, float height) {


    bounds = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
    bounds.setOrigin(width / 2, height / 2);
    bounds.setPosition(position.x, position.y);

    object = new Sprite(GameWorld.textureAtlas.findRegion(nameRegion));
    object.setSize(width, height);
    object.setOrigin(width / 2, height / 2);
  }

  public void draw(SpriteBatch batch) {
    setFlip();
    object.setPosition(bounds.getX(), bounds.getY());
    object.setRotation(bounds.getRotation());
    object.draw(batch);
  }

  private void setFlip() {
    if (bounds.getRotation() == 90) object.setFlip(true, false);
    else if (bounds.getRotation() == 180) object.setFlip(true, false);
    else object.setFlip(false, false);
  }

  Polygon getBounds() {
    return bounds;
  }
}

