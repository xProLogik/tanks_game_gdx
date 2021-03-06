package com.prologik.tanksgame.model.gameworld.movableobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.GameWorld;

public class Enemy extends Tank {

  private TankLevel tankLevel;
  private int random;
  private boolean colorBlink = false;
  private boolean bonusEnemy = false;


  public Enemy(TankLevel tankLevel, Vector2 position) {
    super(tankLevel.getNameRegion().replace("tank", "tankenemy"), position, new Vector2(0, -1),
        1);
    this.tankLevel = tankLevel;
    if (tankLevel.equals(TankLevel.LEVEL4)) this.setTankLife(3);
    if (tankLevel.equals(TankLevel.LEVEL2)) this.setVelocity(this.getVelocity() * 4);
    else this.setVelocity(this.getVelocity() * 2);
    if (Math.random() < .1) {
      this.setBonusEnemy(true);
      this.setColorBlink(true);
    }
    this.getObject().setAlpha(1);
  }

  public TankLevel getTankLevel() {
    return tankLevel;
  }

  public void randomMove() {
    if (Math.random() * 100 < 10)
      randomAngle();
  }

  private void randomAngle() {
    random = (int) (Math.random() * 100);
    switch (currentFacing) {
      case RIGHT:
        if (random < 15)
          this.setDirection(new Vector2(-1, 0));
        else if (random < 30)
          this.setDirection(new Vector2(0, 1));
        else if (random < 45)
          this.setDirection(new Vector2(0, -1));
        break;
      case LEFT:
        if (random < 15)
          this.setDirection(new Vector2(1, 0));
        else if (random < 30)
          this.setDirection(new Vector2(0, -1));
        else if (random < 45)
          this.setDirection(new Vector2(0, 1));
        break;
      case DOWN:
        if (random < 15)
          this.setDirection(new Vector2(0, 1));
        else if (random < 30)
          this.setDirection(new Vector2(1, 0));
        else if (random < 45)
          this.setDirection(new Vector2(-1, 0));
        break;
      case UP:
        if (random < 15)
          this.setDirection(new Vector2(0, -1));
        else if (random < 30)
          this.setDirection(new Vector2(-1, 0));
        else if (random < 45)
          this.setDirection(new Vector2(1, 0));
        break;
    }
    this.recalculateRotationAngle(this.getDirection());
  }

  public void randomMovement() {
    if (Math.random() < .01) {
      randomAngle();
    }
    if (Math.random() < .01) {
      this.shot();

    }
  }

  @Override
  public void leftTheField() {
    super.leftTheField();
    randomMove();
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  @Override
  protected float bulletVelosity() {
    if (this.tankLevel.equals(TankLevel.LEVEL3)) return 2;
    return 1;
  }

  private void declineForLevel4() {
    this.setColorBlink(true);

  }

  private void setBonusEnemy(boolean bonusEnemy) {
    this.bonusEnemy = bonusEnemy;
  }

  public boolean isBonusEnemy() {
    return bonusEnemy;
  }

  public boolean isColorBlink() {
    return colorBlink;
  }

  private void colorBlink(float runTime, float[] rgb) {
    if (Math.floor(runTime * 10) % 4 >= 2) getObject().setColor(rgb[0], rgb[1], rgb[2], 1);
    else getObject().setColor(1, 1, 1, 1);
  }

  private void setColorBlink(boolean colorBlink) {
    this.colorBlink = colorBlink;
  }

  public void enemyColorBlink(float runTime) {
    float[] color = new float[3];
    if (bonusEnemy) color = new float[]{0.6f, 0, 0.4f};
    if (this.getTankLife() == 2) color = new float[]{1, 1, 0.2f};
    if (this.getTankLife() == 1 && tankLevel.equals(TankLevel.LEVEL4)) color = new float[]{0.1f, 0.7f, 0.1f};
    colorBlink(runTime, color);
  }

  public void hitTheTank(Tank tank, GameWorld world) {
    if (this.isBonusEnemy()) {
      world.createBonus();
      this.setBonusEnemy(false);
    }
    this.setTankLife(this.getTankLife() - 1);
    if (this.getTankLife() < 1) {
      this.createExploison(world);
      world.createPoints(this, 100 * (this.tankLevel.ordinal() + 1), (Player) tank);
      world.removedEnemies.add(this);
    }
    if (this.getTankLevel().equals(TankLevel.LEVEL4)) {
      GameWorld.sounds.get("enemylevel4").play(0.3f);
      this.declineForLevel4();
    }
  }

}
