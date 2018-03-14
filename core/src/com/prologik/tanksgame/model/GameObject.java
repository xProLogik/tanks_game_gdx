package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

abstract class GameObject {

  Polygon bounds;
  private Sprite object;

  GameObject(TextureRegion textureRegion, float x, float y, float width, float height) {
    bounds = new Polygon(new float[]{0f, 0f, width,0f,width, height,0f,height});
    bounds.setOrigin(width/2,height/2);
    bounds.setPosition(x,y);
    object = new Sprite(textureRegion,0,0,16, 16);
    object.setSize(width, height);
    object.setOrigin(width/2,height/2);
  }

  public void draw(SpriteBatch batch) {
    object.setPosition(bounds.getX(),bounds.getY());
    object.setRotation(bounds.getRotation());
    object.draw(batch);
  }
}
