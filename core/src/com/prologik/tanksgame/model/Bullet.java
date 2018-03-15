package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.prologik.tanksgame.control.Heading;
import com.prologik.tanksgame.view.GameScreen;

public class Bullet extends GameObject{

  public Bullet(TextureRegion textureRegion,Heading head, float x, float y, float width, float height) {
    super(textureRegion, x, y, width, height);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
