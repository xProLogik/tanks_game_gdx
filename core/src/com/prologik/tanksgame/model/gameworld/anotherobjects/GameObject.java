package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.model.gameworld.GameWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject {

  private Sprite object;
  private Polygon bounds;

  private float koeffDrawX = 0;
  private float koeffDrawY = 0;

  public Vector2 position;
  int indexRegion;
  private float width;
  private float height;


  static int PIXEL_SIZE_SPRITE = 8;

  public GameObject(String nameRegion, int indexRegion, Vector2 position, float width, float height) {
    bounds = new Polygon(new float[]{0f, 0f, width, 0f, width, height, 0f, height});
    bounds.setOrigin(width / 2, height / 2);
    bounds.setPosition(position.x, position.y);

    object = new Sprite(MainGame.sptriteAtlas.findRegion(nameRegion, indexRegion));
    object.setSize(width, height);
    object.setOrigin(width / 2, height / 2);

    this.position = position;
    this.indexRegion = indexRegion;
    this.width = width;
    this.height = height;
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

  public void blink(float runTime) {
    float timer = (float) (Math.floor(runTime * 100) % 100);
    if (timer <= 50) object.setAlpha(timer / 50);
    else object.setAlpha(1 - timer / 50);
  }

  public ArrayList<Vector2> getPositionAround() {
    Vector2 around;
    List<Vector2> list = new ArrayList<>();
    for (float x = this.position.x - 1; x <= this.position.x + this.width; x++) {
      for (float y = this.position.y - 1; y <= this.position.y + this.height; y++) {
        if (inField(around = new Vector2(x, y)))
          if (around.x < this.position.x ||
              around.y < this.position.y ||
              around.x >= this.position.x + this.width ||
              around.y >= this.position.y + this.height)
            list.add(around);
      }
    }
    return (ArrayList<Vector2>) list;
  }

  private boolean inField(Vector2 position) {
    return position.x >= GameWorld.PLAYFIELD_MIN_X && position.x < GameWorld.PLAYFIELD_MAX_X &&
        position.y >= GameWorld.PLAYFIELD_MIN_Y && position.y < GameWorld.PLAYFIELD_MAX_Y;
  }

  public Polygon getBounds() {
    return bounds;
  }

  public Sprite getObject() {
    return object;
  }

  protected void setKoeffDrawX(float koeffDrawX) {
    this.koeffDrawX = koeffDrawX;
  }

  void setKoeffDrawY(float koeffDrawY) {
    this.koeffDrawY = koeffDrawY;
  }

  public Rectangle getCollisionRect() {
    return this.bounds.getBoundingRectangle();
  }
}

