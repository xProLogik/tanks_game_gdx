package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

class Box extends GameObject {


  private BoxType boxType;

  Box(BoxType boxType, Vector2 position) {
    super(boxType.name().toLowerCase(), 0, position, GameWorld.SPRITE_SIZE,GameWorld.SPRITE_SIZE);
    this.boxType = boxType;
  }

  BoxType getBoxType() {
    return boxType;
  }

  void setBoxType(BoxType boxType) {
    this.boxType = boxType;
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  boolean changeBox(Vector2 direction) {

    boolean result = false;
    int x = 0, y = 0, width = this.getObject().getRegionWidth(), height = this.getObject().getRegionHeight();
    float changeWidth = 0, changeHeight = 0;

    if (direction.x > 0) {
      x = PIXEL_SIZE_SPRITE / 2;
      width /= 2;
      this.setKoeffDrawX(GameWorld.SPRITE_SIZE / 2);
      changeWidth = GameWorld.SPRITE_SIZE / 2f;
    }
    if (direction.x < 0) {
      width /= 2;
      changeWidth = GameWorld.SPRITE_SIZE / 2f;
    }
    if (direction.y > 0) {
      height /= 2;
      this.setKoeffDrawY(GameWorld.SPRITE_SIZE / 2);
      changeHeight = GameWorld.SPRITE_SIZE / 2f;
    }
    if (direction.y < 0) {
      y = PIXEL_SIZE_SPRITE / 2;
      height /= 2;
      changeHeight = GameWorld.SPRITE_SIZE / 2f;
    }
    this.getObject().setRegion(this.getObject(), x, y, width, height);
    this.getObject().setSize(this.getObject().getWidth() - changeWidth,this.getObject().getHeight() - changeHeight);
    if (width < PIXEL_SIZE_SPRITE / 2 || height < PIXEL_SIZE_SPRITE / 2) result = true;


    return result;
  }
}
