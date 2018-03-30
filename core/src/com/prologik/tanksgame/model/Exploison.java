package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Exploison extends GameObject {
  private TextureRegion exploison1, exploison2, exploison3, exploison4, exploison5;
  private TextureRegion[] exploisonFrames;
  Animation exploisonAnimation;
  float stateTime;

  Exploison(int countFrames, Vector2 position) {
    super("exploison", 0, position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE);
    exploison1 = GameWorld.textureAtlas.findRegion("exploison", 0);
    exploison2 = GameWorld.textureAtlas.findRegion("exploison", 1);
    exploison3 = GameWorld.textureAtlas.findRegion("exploison", 2);
    if (countFrames == 5) {
      exploison4 = GameWorld.textureAtlas.findRegion("exploison", 3);
      exploison5 = GameWorld.textureAtlas.findRegion("exploison", 4);
      exploisonFrames = new TextureRegion[]{exploison1, exploison2, exploison3, exploison4, exploison5};
    } else exploisonFrames = new TextureRegion[]{exploison1, exploison2, exploison3};
    exploisonAnimation = new Animation<>(0.025f, exploisonFrames);
    exploisonAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    this.stateTime = 0;
  }


  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  public void update(float delta) {
    stateTime += delta;

    if ((this.exploisonAnimation.getKeyFrameIndex(stateTime)) == 3 ||
        (this.exploisonAnimation.getKeyFrameIndex(stateTime)) == 4) {
      this.setKoeffDrawX(-1f);
      this.setKoeffDrawY(-1f);
      this.getObject().setSize(4 * GameWorld.SPRITE_SIZE, 4 * GameWorld.SPRITE_SIZE);
    }
    this.getObject().setRegion((TextureRegion) this.exploisonAnimation.getKeyFrame(stateTime));

  }
}
