package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Tile extends GameObject{

  Tile(TextureRegion textureRegion, float x, float y, float width, float height) {
    super(textureRegion, x, y, width, height);
  }
}
