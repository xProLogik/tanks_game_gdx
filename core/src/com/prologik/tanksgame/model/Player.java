package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.control.PlayerContoller;

public class Player extends Tank {

  private TankLevel tankLevel;
  private PlayerContoller contoller;
  private float timeOnIce;
  private boolean onIce;
  private boolean playSound = false;
  private float timeMoveSound = 0;
  private float timeDontMoveSound = 0;


  Player(TankLevel tankLevel, Vector2 position) {
    super(tankLevel.getNameRegion(), position, new Vector2(0, 1), 132);
    contoller = new PlayerContoller(this);
    this.tankLevel = tankLevel;
    this.setVelocity(this.getVelocity() * 3);
  }


  public void move(float delta, float runTime, Vector2 moveDirection) {
    if (playSound && timeMoveSound == 0) {
      timeMoveSound += delta;
      GameWorld.sounds.get("move").loop(0.3f);
      GameWorld.sounds.get("dontmove").stop();
    }
    this.setDirection(moveDirection);
    super.move(delta, runTime);
  }

  TankLevel getTankLevel() {
    return tankLevel;
  }

  void setTankLevel(TankLevel tankLevel) {
    this.tankLevel = tankLevel;
  }

  private void dontMove(float delta,float runtime) {
    if (!playSound && timeDontMoveSound == 0) {
      timeDontMoveSound+=delta;
      GameWorld.sounds.get("move").stop();
      if (runtime>5)
      GameWorld.sounds.get("dontmove").loop(0.1f);
    }

  }

  public void update(float delta, float runTime) {
    super.update(delta);
    dontMove(delta,runTime);
    tank1 = GameWorld.textureAtlas.findRegion(this.getTankLevel().getNameRegion(), 0);
    tank2 = GameWorld.textureAtlas.findRegion(this.getTankLevel().getNameRegion(), 1);
    TextureRegion[] tankFrames = {tank1, tank2};
    tankAnimation = new Animation<>(0.1f, tankFrames);
    tankAnimation.setPlayMode(Animation.PlayMode.LOOP);
    this.contoller.update(delta, runTime);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  @Override
  protected float bulletVelosity() {
    if (this.tankLevel.equals(TankLevel.LEVEL1)) return 1;
    return 2;
  }

  boolean collideIce(Box box) {
    return box.getBoxType().equals(BoxType.ICE) && this.collide((GameObject) box);
  }

  public void moveOnIce(float delta) {
    if (onIce && timeOnIce < 0.2f) {
      move(delta);
      timeOnIce += delta;
    } else this.onIce = false;
  }

  public void setTimeOnIce(float timeOnIce) {
    this.timeOnIce = timeOnIce;
  }

  public void setOnIce(boolean onIce) {
    this.onIce = onIce;
  }

  public void setPlaySound(boolean playSound) {
    this.playSound = playSound;
  }

  public void setTimeMoveSound(float timeMoveSound) {
    this.timeMoveSound = timeMoveSound;
  }

  public void setTimeDontMoveSound(float timeDontMoveSound) {
    this.timeDontMoveSound = timeDontMoveSound;
  }

  @Override
  public void shot() {
    Vector2 direction = new Vector2(this.direction);
    if (bullets.size() < 1) {
      GameWorld.sounds.get("shot").play(0.3f);
      Bullet newBullet = new Bullet(new Vector2(this.getBounds().getX() + GameWorld.SPRITE_SIZE / 2,
          this.getBounds().getY() + GameWorld.SPRITE_SIZE / 2), direction);
      newBullet.setVelocity(bulletVelosity() * newBullet.getVelocity());
      bullets.add(newBullet);
    }
  }
}
