package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.control.WorldController;
import com.prologik.tanksgame.utils.Utils;

import java.util.ArrayList;

public class GameWorld implements Disposable {

  static TextureAtlas textureAtlas;
  public static final float PLAYFIELD_MIN_X = -13f;
  public static final float PLAYFIELD_MAX_X = 13f;
  public static final float PLAYFIELD_MIN_Y = -13f;
  public static final float PLAYFIELD_MAX_Y = 13f;

  private Player player;

  private ArrayList<Box> boxes = new ArrayList<>();
  private ArrayList<Box> removedBoxes = new ArrayList<>();

  private Array<Enemy> enemies = new Array<>();
  private Array<Enemy> removedEnemies = new Array<>();

  private WorldController contoller;

  public GameWorld() {
    textureAtlas = new TextureAtlas("MyAtlas.atlas");
    contoller = new WorldController(this);
    loadLevel();
  }

  private void loadLevel() {
    player = new Player("tank1", new Vector2(-4f, -13f),
        2f, 2f, new Vector2(0f, 1f));
    generatePlaylevel();
  }

  private void generatePlaylevel() {
    Integer[][] levelMap = Utils.levelParser("level1.lvl");

    for (int i = (int) PLAYFIELD_MIN_X; i < PLAYFIELD_MAX_X; i++)
      for (int j = (int) PLAYFIELD_MIN_Y; j < PLAYFIELD_MAX_Y; j++) {
        if ((levelMap[i + 13][j + 13]) != 0) {
          boxes.add(new Box(BoxType.typeByNumber(levelMap[i + 13][j + 13]),
              new Vector2(j, i), 1f, 1f));
        }
      }
  }

  public void createEnemy() {
    if (enemies.size < 50) {
      Enemy newEnemy = new Enemy("tank5", new Vector2(-5f, 11f),
          2f, 2f, new Vector2(0f, -1f));
      enemies.add(newEnemy);
    }
  }

  public void draw(SpriteBatch batch) {
    player.draw(batch);
    for (Box box : boxes) box.draw(batch);
    for (Enemy enemy : enemies) enemy.draw(batch);
  }

  public void update(float delta) {
    updateEnemies(delta);
    updateBlocks();
    updateShots(delta, player);
    for (Enemy enemy : enemies) {
      updateShots(delta, enemy);
      updateBullets(enemy);
    }
    updatePlayer(delta);
    contoller.update(delta);
  }

  private void updateBullets(Enemy enemy) {
    enemy.removedBullets.clear();
    player.removedBullets.clear();
    for (Bullet bulletPlayer : player.bullets)
      for (Bullet bulletEnemy : enemy.bullets) {
        if (bulletEnemy.collide(bulletPlayer)) {
          bulletEnemy.setCanMove(false);
        }
        if (!bulletEnemy.isCanMove()) {
          enemy.removedBullets.add(bulletEnemy);
          player.removedBullets.add(bulletPlayer);
        }
      }
    player.bullets.removeAll(player.removedBullets, false);
    enemy.bullets.removeAll(enemy.removedBullets, false);
  }

  private void updateEnemies(float delta) {
    removedEnemies.clear();
    for (Enemy tankEnemy : enemies) {
      tankEnemy.randomMovement();
      tankEnemy.setCanMove(true);
      tankEnemy.move(delta);

      for (Box box : boxes) {
        if (tankEnemy.collide(box)) {
          tankEnemy.randomMovement();
          tankEnemy.lastPosition();
        }
      }
      for (Bullet bullet : player.bullets) {
        if (bullet.collide(tankEnemy)) {
          removedEnemies.add(tankEnemy);
          player.removedBullets.add(bullet);
          player.bullets.removeAll(player.removedBullets, false);
        }
      }
      tankEnemy.update(delta);
    }
    enemies.removeAll(removedEnemies, false);
  }

  private void updateBlocks() {
    boxes.removeAll(removedBoxes);
    removedBoxes.clear();
  }

  private void updatePlayer(float delta) {
    for (Box box : boxes) {
      if (player.collide(box)) {
        player.lastPosition();
      }
    }
    player.update(delta);
  }

  private void updateShots(float delta, Tank tank) {
    tank.removedBullets.clear();
    for (Bullet bullet : tank.bullets) {
      bullet.setCanMove(true);
      bullet.move(delta);

      if (bullet.isCanMove()) {
        for (Box box : boxes) {
          if (bullet.collide(box)) {
            bullet.setCanMove(false);
            if (box.getBoxType().equals(BoxType.BRICS))
            removedBoxes.add(box);
          }
        }
      }
      bullet.update(delta);

      if (!bullet.isCanMove()) tank.removedBullets.add(bullet);
    }
    tank.bullets.removeAll(tank.removedBullets, false);

  }

  @Override
  public void dispose() {
    textureAtlas.dispose();
  }
}
