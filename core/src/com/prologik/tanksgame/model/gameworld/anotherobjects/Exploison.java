package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.model.gameworld.GameWorld;

public class Exploison extends GameObject {
  private TextureRegion exploison1, exploison2, exploison3, exploison4, exploison5;
  private TextureRegion[] exploisonFrames;
  private Animation exploisonAnimation;
  private float stateTime = 0;

  public Exploison(int countFrames, Vector2 position) {
    super("exploison", 0, position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE);
    exploison1 = MainGame.sptriteAtlas.findRegion("exploison", 0);
    exploison2 = MainGame.sptriteAtlas.findRegion("exploison", 1);
    exploison3 = MainGame.sptriteAtlas.findRegion("exploison", 2);
    if (countFrames == 5) {
      exploison4 = MainGame.sptriteAtlas.findRegion("exploison", 3);
      exploison5 = MainGame.sptriteAtlas.findRegion("exploison", 4);
      exploisonFrames = new TextureRegion[]{exploison1, exploison2, exploison3, exploison4, exploison5};
    } else exploisonFrames = new TextureRegion[]{exploison1, exploison2, exploison3};
    exploisonAnimation = new Animation<>(0.025f, exploisonFrames);
    exploisonAnimation.setPlayMode(Animation.PlayMode.NORMAL);
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

  public Animation getExploisonAnimation() {
    return exploisonAnimation;
  }

  public float getStateTime() {
    return stateTime;
  }
}
