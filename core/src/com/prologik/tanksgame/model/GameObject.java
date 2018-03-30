package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

  private Sprite object;
  private Polygon bounds;

  private float koeffDrawX = 0;
  private float koeffDrawY = 0;

  Vector2 position;
  int indexRegion;

  static int PIXEL_SIZE_SPRITE = 8;

  GameObject(String nameRegion, int indexRegion, Vector2 position, float width, float height) {
    bounds = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
    bounds.setOrigin(width / 2, height / 2);
    bounds.setPosition(position.x, position.y);

    object = new Sprite(GameWorld.textureAtlas.findRegion(nameRegion, indexRegion));
    object.setSize(width, height);
    object.setOrigin(width / 2, height / 2);
    this.position = position;
    this.indexRegion = indexRegion;
  }


  public void draw(SpriteBatch batch) {
    setFlip();
    object.setPosition(bounds.getX() + koeffDrawX, bounds.getY() + koeffDrawY);
    object.setRotation(bounds.getRotation());
    object.draw(batch);
  }


  private void setFlip() {
    if (bounds.getRotation() == 90) object.setFlip(true, false);
    else if (bounds.getRotation() == 180) object.setFlip(true, false);
    else object.setFlip(false, false);
  }

  void blink(float runTime) {
    float timer = (float) (Math.floor(runTime * 100) % 100);
    if (timer <= 50) object.setAlpha(timer / 50);
    else object.setAlpha(1 - timer / 50);
  }

  Polygon getBounds() {
    return bounds;
  }

  Sprite getObject() {
    return object;
  }

  void setKoeffDrawX(float koeffDrawX) {
    this.koeffDrawX = koeffDrawX;
  }

  void setKoeffDrawY(float koeffDrawY) {
    this.koeffDrawY = koeffDrawY;
  }

  Rectangle getCollisionRect() {
    return this.bounds.getBoundingRectangle();
  }
}

