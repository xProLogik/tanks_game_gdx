package com.prologik.tanksgame.model.gameworld.movableobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.gameworld.PlayerContoller;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Box;
import com.prologik.tanksgame.model.gameworld.anotherobjects.BoxType;
import com.prologik.tanksgame.model.gameworld.anotherobjects.GameObject;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Tank {

  private TankLevel tankLevel;
  private PlayerContoller contoller;

  private float timeOnIce;
  private boolean onIce;

  private Vector2 spawnPosition;
  private boolean playSound = false;
  private float timeMoveSound = 0;
  private float timeDontMoveSound = 0;
  private int points = 0;
  public static int count = 0;
  private int numberPlayer = 0;
  private Map<TankLevel, Integer> killingEnemy = new HashMap<>();
  private List<Icon> playerLife = new ArrayList<>();

  private TextureRegion outline1, outline2;
  private Animation helmetAnimation;
  private Sprite helmet;
  private boolean helmetArmor = true;
  private float timeHelmetArmor = 0;
  private boolean dead = false;


  public Player(TankLevel tankLevel, Vector2 position) {
    super(tankLevel.getNameRegion(), position, new Vector2(0, 1), 3);
    contoller = new PlayerContoller(this);
    this.tankLevel = tankLevel;
    this.setVelocity(this.getVelocity() * 3);
    this.numberPlayer = ++count;
    if (this.numberPlayer == 1) {
      this.getObject().setColor(1, 1, 0.2f, 1);
    }
    if (this.numberPlayer == 2) {
      this.getObject().setColor(0.1f, 0.7f, 0.1f, 1);
    }

    outline1 = MainGame.sptriteAtlas.findRegion("outline", 0);
    outline2 = MainGame.sptriteAtlas.findRegion("outline", 1);
    TextureRegion[] outlineFrames = {outline1, outline2};
    helmetAnimation = new Animation<>(0.05f, outlineFrames);
    helmetAnimation.setPlayMode(Animation.PlayMode.LOOP);
    spawnPosition = position;
  }


  public void move(float delta, float runTime, Vector2 moveDirection) {
    if (playSound && timeMoveSound == 0) {
      timeMoveSound += delta;
      GameWorld.sounds.get("move").stop();
      GameWorld.sounds.get("move").loop(0.3f);
      GameWorld.sounds.get("dontmove").stop();
    }
    this.setDirection(moveDirection);
    super.move(delta, runTime);
  }

  public TankLevel getTankLevel() {
    return tankLevel;
  }

  private void setTankLevel(TankLevel tankLevel) {
    this.tankLevel = tankLevel;
  }

  private void dontMove(float delta, float runtime) {
    if (!playSound && timeDontMoveSound == 0) {
      timeDontMoveSound += delta;
      GameWorld.sounds.get("move").stop();
      if (runtime > 5)
        GameWorld.sounds.get("dontmove").stop();
      GameWorld.sounds.get("dontmove").loop(0.1f);
    }

  }

  public void update(float delta, float runTime) {

    super.update(delta, runTime);
    dontMove(delta, runTime);
    tank1 = MainGame.sptriteAtlas.findRegion(this.getTankLevel().getNameRegion(), 0);
    tank2 = MainGame.sptriteAtlas.findRegion(this.getTankLevel().getNameRegion(), 1);
    TextureRegion[] tankFrames = {tank1, tank2};
    tankAnimation = new Animation<>(0.1f, tankFrames);
    tankAnimation.setPlayMode(Animation.PlayMode.LOOP);
    this.contoller.update(delta, runTime);
    if (timeAfterSpawn < 5 || (helmetArmor && timeHelmetArmor > 0)) {
      helmet = new Sprite((TextureRegion) helmetAnimation.getKeyFrame(runTime));
      helmet.setSize(this.getObject().getWidth(), this.getObject().getHeight());
      helmet.setPosition(this.getObject().getX(), this.getObject().getY());
      timeHelmetArmor -= delta;
    } else {
      helmet = null;
      helmetArmor = false;
    }
    if (this.tankLevel.equals(TankLevel.LEVEL3)) this.setCountBullets(2);
    else this.setCountBullets(1);
    updateIconPlayerLife();
  }

  @Override
  public void draw(SpriteBatch batch) {

      super.draw(batch);
      if (helmet != null)
        helmet.draw(batch);
    playerLife.forEach(icon -> icon.draw(batch));
  }

  @Override
  protected float bulletVelosity() {
    if (this.tankLevel.equals(TankLevel.LEVEL1)) return 1;
    return 2;
  }

  public boolean collideIce(Box box) {
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

  public int getNumberPlayer() {
    return numberPlayer;
  }

  private void updateIconPlayerLife() {
    this.getPlayerLife().clear();
    int life = this.getTankLife();
    int x = 0;
    do {
      life /= 10;
      x++;
    } while (life != 0);
    x--;
    life = this.getTankLife();
    while (life > 0) {
      Icon newIcon = new Icon("number", life % 10,
          new Vector2(GameWorld.PLAYFIELD_MAX_X + 2 * GameWorld.SPRITE_SIZE + x,
              0 - 2 * GameWorld.SPRITE_SIZE - 3f * (this.getNumberPlayer() - 1)), GameWorld.SPRITE_SIZE, GameWorld.SPRITE_SIZE);
      newIcon.getObject().setColor(Color.BLACK);
      this.getPlayerLife().add(newIcon);
      life /= 10;
      x--;
    }
  }

  public void extraLife() {
    GameWorld.sounds.get("extralife").play(0.3f);
    this.setTankLife(this.getTankLife() + 1);
  }

  private void lostLife() {
    if (this.getTankLife() > 1) {
      this.setSpawnPosition();
      this.setTankLife(this.getTankLife() - 1);
      this.setTankLevel(TankLevel.LEVEL1);
    } else {
      this.dead = true;
    }
  }

  public void setHelmetArmor(boolean helmetArmor) {
    this.timeHelmetArmor = 10;
    this.helmetArmor = helmetArmor;
  }

  private boolean isHelmetArmor() {
    return helmetArmor;
  }

  public void upLevel() {
    if (!this.getTankLevel().equals(TankLevel.LEVEL4)) {
      this.setTankLevel(this.getTankLevel().nextLevel());
    }
  }

  public void addPoints(int add) {
    this.points += add;
  }

  public void addKillingEnemy(TankLevel tankLevel) {
    int count = 0;
    for (Map.Entry<TankLevel, Integer> entry : killingEnemy.entrySet())
      if (entry.getKey().equals(tankLevel)) count = entry.getValue();
    killingEnemy.put(tankLevel, count + 1);
  }

  public void hitTheTank(Tank tank, GameWorld world) {
    if (!this.isHelmetArmor()) {
      this.createExploison(world);
      this.lostLife();
    }
  }

  public void delete() {
    count--;
  }

  private List<Icon> getPlayerLife() {
    return playerLife;
  }

  public void setSpawnPosition() {
    this.setCanMove(false);
    this.position = this.spawnPosition;
    this.setNewPosition(this.position);
    this.setDirection(new Vector2(0, 1));
    this.timeAfterSpawn = 0;
    this.helmetArmor = true;
  }

  public int getPoints() {
    return points;
  }

  public Map<TankLevel, Integer> getKillingEnemy() {
    return killingEnemy;
  }

  public boolean isDead() {
    return dead;
  }

}
