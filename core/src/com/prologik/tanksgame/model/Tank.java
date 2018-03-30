package com.prologik.tanksgame.model;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Tank extends MovableObject {

  private boolean spawnCollide = false;
  TextureRegion tank1, tank2;
  Animation tankAnimation;
  LinkedList<Bullet> bullets = new LinkedList<>();
  List<Bullet> removedBullets = new ArrayList<>();
  int tankLife;

  Tank(String nameRegion, Vector2 position, Vector2 direction, int tankLife) {
    super(nameRegion, position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE,
        direction, 2.5f);
    this.tankLife = tankLife;
    this.getObject().setSize(1.9f, 1.9f);
    this.setKoeffDrawX(0.1f);
    tank1 = GameWorld.textureAtlas.findRegion(nameRegion, 0);
    tank2 = GameWorld.textureAtlas.findRegion(nameRegion, 1);
    TextureRegion[] tankFrames = {tank1,tank2};
    tankAnimation = new Animation<>(0.1f,tankFrames);
    tankAnimation.setPlayMode(Animation.PlayMode.LOOP);
  }


  @Override
  public void update(float delta) {
    super.update(delta);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
    for (Bullet bullet : bullets) bullet.draw(batch);
  }


  void move(float delta,float runTime) {
    super.move(delta);
    this.getObject().setRegion((TextureRegion) tankAnimation.getKeyFrame(runTime));
  }

  public void shot() {
    Vector2 direction = new Vector2(this.direction);
    if (bullets.size() < 0) {
      GameWorld.sounds.get("shot").play(0.3f);
      Bullet newBullet = new Bullet(new Vector2(this.getBounds().getX() + GameWorld.SPRITE_SIZE / 2,
          this.getBounds().getY() + GameWorld.SPRITE_SIZE / 2), direction);
      newBullet.setVelocity(bulletVelosity()*newBullet.getVelocity());
      bullets.add(newBullet);
    }
  }

  protected abstract float bulletVelosity();

  void setSpawnCollide(boolean spawnCollide) {
    this.spawnCollide = spawnCollide;
  }

  boolean isSpawnCollide() {
    return spawnCollide;
  }
}
