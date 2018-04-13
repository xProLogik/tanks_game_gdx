package com.prologik.tanksgame.model.gameworld.movableobjects;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Exploison;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Tank extends MovableObject {

  private boolean spawnCollide = true;
  float timeAfterSpawn = 0;
  TextureRegion tank1, tank2;
  Animation tankAnimation;
  private LinkedList<Bullet> bullets = new LinkedList<>();
  private List<Bullet> removedBullets = new ArrayList<>();
  private int tankLife;
  private int countBullets = 1;

  Tank(String nameRegion, Vector2 position, Vector2 direction, int tankLife) {
    super(nameRegion, position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE,
        direction, 2.5f);
    this.tankLife = tankLife;
    this.getObject().setSize(1.9f, 1.9f);
    this.setKoeffDrawX(0.1f);
    tank1 = MainGame.sptriteAtlas.findRegion(nameRegion, 0);
    tank2 = MainGame.sptriteAtlas.findRegion(nameRegion, 1);
    TextureRegion[] tankFrames = {tank1, tank2};
    tankAnimation = new Animation<>(0.1f, tankFrames);
    tankAnimation.setPlayMode(Animation.PlayMode.LOOP);

  }


  public void update(float delta, float runTime) {
    super.update(delta);
    timeAfterSpawn += delta;

    this.removedBullets.clear();
    for (Bullet bullet : this.bullets) {
      if (!bullet.isCanMove()) {
        this.removedBullets.add(bullet);
      }
      bullet.update(delta);
    }
    this.bullets.removeAll(this.removedBullets);
  }


  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
    bullets.forEach(bullet -> bullet.draw(batch));
  }


  public void createExploison(GameWorld world) {
    GameWorld.sounds.get("smallexploison").play(0.3f);
    world.getExploisons().add(new Exploison(3, this.position));
  }

  public void move(float delta, float runTime) {
    super.move(delta);
    this.getObject().setRegion((TextureRegion) tankAnimation.getKeyFrame(runTime));
  }

  public void shot() {
    Vector2 direction = new Vector2(this.getDirection());
    if (bullets.size() < countBullets) {
      GameWorld.sounds.get("shot").play(0.3f);
      Bullet newBullet = new Bullet(new Vector2(this.getBounds().getX() + GameWorld.SPRITE_SIZE / 2,
          this.getBounds().getY() + GameWorld.SPRITE_SIZE / 2), direction, this);
      newBullet.setVelocity(bulletVelosity() * newBullet.getVelocity());
      newBullet.setCanMove(true);
      bullets.add(newBullet);
    }
  }

  void setCountBullets(int countBullets) {
    this.countBullets = countBullets;
  }

  protected abstract float bulletVelosity();

  public void setSpawnCollide(boolean spawnCollide) {
    this.spawnCollide = spawnCollide;
  }

  public boolean isSpawnCollide() {
    return spawnCollide;
  }

  int getTankLife() {
    return tankLife;
  }

  void setTankLife(int tankLife) {
    this.tankLife = tankLife;
  }

  public LinkedList<Bullet> getBullets() {
    return bullets;
  }

  public float getTimeAfterSpawn() {
    return timeAfterSpawn;
  }

  public abstract void hitTheTank(Tank gunner, GameWorld gameWorld);
}
