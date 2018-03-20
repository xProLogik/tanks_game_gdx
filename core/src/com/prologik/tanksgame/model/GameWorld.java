package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.control.WorldController;
import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.utils.Utils;
import com.prologik.tanksgame.view.GameScreen;

import java.util.ArrayList;

public class GameWorld implements Disposable {


  static TextureAtlas textureAtlas;

  static final float SPRITE_SIZE = WorldRenderer.CAM_HEIGHT / 26;
  static final float PLAYFIELD_MIN_X = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_X = 13f * SPRITE_SIZE;
  static final float PLAYFIELD_MIN_Y = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_Y = 13f * SPRITE_SIZE;


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

    player = new Player("tank1", new Vector2(0 - 4 * SPRITE_SIZE, PLAYFIELD_MIN_Y),
        2 * SPRITE_SIZE, 2 * SPRITE_SIZE, new Vector2(0f, 1f));
    generatePlaylevel();
  }

  private void generatePlaylevel() {
    int[][] levelMap = Utils.levelParser("level1.lvl");

    for (int i = (int) PLAYFIELD_MIN_X; i < PLAYFIELD_MAX_X; i++)
      for (int j = (int) PLAYFIELD_MIN_Y; j < PLAYFIELD_MAX_Y; j++) {
        if ((levelMap[i + (int) PLAYFIELD_MAX_X][j + (int) PLAYFIELD_MAX_Y]) != 0) {
          Box newBox = new Box(BoxType.typeByNumber(levelMap[i + (int) PLAYFIELD_MAX_X][j + (int) PLAYFIELD_MAX_Y]),
              new Vector2(j, i), SPRITE_SIZE, SPRITE_SIZE);
          boxes.add(newBox);
        }
      }
  }

  public void createEnemy() {
    if (enemies.size < 50) {
      Enemy newEnemy = new Enemy("tank5", new Vector2(-5f, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE),
          2 * SPRITE_SIZE, 2 * SPRITE_SIZE, new Vector2(0f, -1f));
      enemies.add(newEnemy);
    }
  }

  public void draw(SpriteBatch batch) {
    player.draw(batch);
    for (Enemy enemy : enemies) enemy.draw(batch);
    for (Box box : boxes) box.draw(batch);
  }

  public void update(float delta) {
    updateTanks();
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

  private void updateTanks() {

    Array<Tank> collideEnemies = new Array<>(enemies);
    collideEnemies.add(player);

    for (Tank tank : enemies) {
      for (Tank tankCollide : collideEnemies)
        if (!(tankCollide.equals(tank)))
          if (tankCollide.collide(tank)) {
            tankCollide.lastPosition();
            tank.lastPosition();
            if (!tank.equals(player))
              ((Enemy) tank).randomMove();
            if (!tankCollide.equals(player))
              ((Enemy) tankCollide).randomMove();
          }
    }
  }

  private void updateBullets(Enemy enemy) {
    enemy.removedBullets.clear();
    player.removedBullets.clear();
    for (Bullet bulletPlayer : player.bullets) {
      for (Bullet bulletEnemy : enemy.bullets) {
        if (bulletEnemy.collide(bulletPlayer)) {
          bulletEnemy.setCanMove(false);
          bulletPlayer.setCanMove(false);
        }
        if (!bulletEnemy.isCanMove())
          enemy.removedBullets.add(bulletEnemy);
        if (!bulletPlayer.isCanMove())
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
          tankEnemy.randomMove();
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
            if (box.getBoxType().equals(BoxType.BRICS)) {
              if (box.changeBox(bullet.direction))
                removedBoxes.add(box);
            }
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
