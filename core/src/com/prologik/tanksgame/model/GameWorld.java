package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.control.WorldController;
import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.utils.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameWorld implements Disposable {


  static TextureAtlas textureAtlas;

  static final float SPRITE_SIZE = WorldRenderer.CAM_HEIGHT / 26f;
  static final float PLAYFIELD_MIN_X = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_X = 13f * SPRITE_SIZE;
  static final float PLAYFIELD_MIN_Y = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_Y = 13f * SPRITE_SIZE;


  private Player player;

  private boolean clock_stop = false;
  private float stopTime;
  private float shovelTime;

  private ArrayList<Box> boxes = new ArrayList<>();
  private ArrayList<Box> removedBoxes = new ArrayList<>();

  private Array<String> bufferedEnemies = new Array<>();
  private Array<Enemy> enemies = new Array<>();
  private Array<Enemy> removedEnemies = new Array<>();

  private Array<Vector2> placesRespawnEnemy = new Array<Vector2>() {{
    add(new Vector2(PLAYFIELD_MIN_X, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2((PLAYFIELD_MIN_X + PLAYFIELD_MAX_X) / 2 - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2(PLAYFIELD_MAX_X - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
  }};

  private Bonus bonus = null;

  private WorldController contoller;

  public GameWorld() {
    textureAtlas = new TextureAtlas("MyAtlas.atlas");
    contoller = new WorldController(this);
    loadLevel();
  }

  private void loadLevel() {

    player = new Player(TankLevel.LEVEL1, new Vector2(0 - 4 * SPRITE_SIZE, PLAYFIELD_MIN_Y),
        2 * SPRITE_SIZE, 2 * SPRITE_SIZE, new Vector2(0f, 1f));
    player.getObject().setColor(Color.GOLD);
    generatePlaylevel();
    generateEnemyForLevel();
  }

  private void generateEnemyForLevel() {
    Map<String, Integer> enemyParser = Utils.enemyParser("level1.lvl");
    Pattern p = Pattern.compile("[0-9]");
    Matcher m;
    for (Map.Entry<String, Integer> entry : enemyParser.entrySet()) {

      m = p.matcher(entry.getKey());
      String typeEnemy = "tank5";
      if (m.find()) typeEnemy = "tankenemy" + m.group();
      for (int i = 0; i < entry.getValue(); i++) {
        bufferedEnemies.add(typeEnemy);
      }
    }
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


  public void generateEnemy() {
    if (enemies.size < 5) {
      Enemy newEnemy = new Enemy("tankenemy1", new Vector2(-5f, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE),
          2 * SPRITE_SIZE, 2 * SPRITE_SIZE, new Vector2(0f, -1f));
      enemies.add(newEnemy);
    }
  }

  public void createEnemy() {
    if (enemies.size < 5) {
      Enemy newEnemy = new Enemy("tankenemy1", new Vector2(-5f, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE),
          2 * SPRITE_SIZE, 2 * SPRITE_SIZE, new Vector2(0f, -1f));
      //if (!clock_stop) newEnemy.setCanMove(true);
      enemies.add(newEnemy);

    }
  }

  private void createBonus() {
    bonus = null;
    int randomBonus = 5;// (int) Math.round(Math.random() * 5);
    bonus = new Bonus("bonus", randomBonus, new Vector2(
        (float) Math.random() * (PLAYFIELD_MAX_X - 2f * SPRITE_SIZE - PLAYFIELD_MIN_X) - PLAYFIELD_MAX_X,
        (float) Math.random() * (PLAYFIELD_MAX_Y - 2f * SPRITE_SIZE - PLAYFIELD_MIN_Y) - PLAYFIELD_MAX_Y),
        2 * SPRITE_SIZE, 2 * SPRITE_SIZE);
  }

  public void draw(SpriteBatch batch) {
    player.draw(batch);
    for (Enemy enemy : enemies) enemy.draw(batch);
    for (Box box : boxes) box.draw(batch);
    if (bonus != null)
      bonus.draw(batch);
  }

  public void update(float delta, float runTime) {
    updateBonus(runTime);
    updateEnemies(delta);
    updateBlocks();
    updateShots(delta, player);
    for (Enemy enemy : enemies) {
      updateShots(delta, enemy);
      updateBullets(enemy);
    }
    updatePlayer(delta, runTime);
    contoller.update(delta);
  }

  private void updateBonus(float runTime) {
    if (runTime - shovelTime > 6) {
      for (Box box : boxes)
        if ((-2 * SPRITE_SIZE <= box.getBounds().getX()) && (1 * SPRITE_SIZE >= box.getBounds().getX()) &&
            (-13 * SPRITE_SIZE <= box.getBounds().getY()) && (-11 >= box.getBounds().getY()) &&
            (box.getBoxType().equals(BoxType.STONE))) box.blink(runTime);
    }
    if (runTime - shovelTime > 10) {
      for (Box box : boxes)
        if ((-2 * SPRITE_SIZE <= box.getBounds().getX()) && (1 * SPRITE_SIZE >= box.getBounds().getX()) &&
            (-13 * SPRITE_SIZE <= box.getBounds().getY()) && (-11 >= box.getBounds().getY())) {
          if (box.getBoxType().equals(BoxType.STONE))
            removedBoxes.add(box);
          if (box.getBoxType().equals(BoxType.WATER))
            box.setBoxType(BoxType.BRICS);
        }
      boxes.removeAll(removedBoxes);
      shovelTime = 0;
    }
    if ((clock_stop) && (runTime - stopTime > 7)) {
      for (Enemy enemy : enemies)
        enemy.blink(runTime);
    }
    if ((clock_stop) && (runTime - stopTime > 10)) {
      clock_stop = false;
      for (Enemy enemy : enemies)
        enemy.getObject().setAlpha(1);
    }
    if (bonus != null) bonus.update(runTime);
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

    Array<Tank> collideEnemies = new Array<>(enemies);
    collideEnemies.add(player);

    removedEnemies.clear();
    for (Enemy tankEnemy : enemies) {
      if (!clock_stop) {
        tankEnemy.randomMovement();
        tankEnemy.move(delta);
      }

      for (Box box : boxes) {
        if (tankEnemy.collide(box)) {
          tankEnemy.setCanMove(false);
          if (!clock_stop)
            tankEnemy.randomMove();
        }
      }
      for (Bullet bullet : player.bullets) {
        if (bullet.collide(tankEnemy)) {
          createBonus();
          removedEnemies.add(tankEnemy);
          player.removedBullets.add(bullet);
          player.bullets.removeAll(player.removedBullets, false);
        }
      }
      for (Tank tankCollide : collideEnemies)
        if (!(tankCollide.equals(tankEnemy)))
          if (tankCollide.collide(tankEnemy) || tankEnemy.collide(tankCollide)) {
            tankEnemy.setCanMove(false);
            tankCollide.setCanMove(false);
            if (!clock_stop) {
              tankEnemy.randomMove();
              if (!tankCollide.equals(player))
                ((Enemy) tankCollide).randomMove();
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

  private void updatePlayer(float delta, float runTime) {
    for (Box box : boxes) {
      if (player.collide(box)) {
        player.setCanMove(false);
      }
    }
    if ((bonus != null) && (player.collide(bonus))) {
      switch (bonus.getBonusType()) {
        case HELMET:
          break;
        case EXTRA_LIFE:
          break;
        case GRENADE:
          for (Enemy enemy : enemies) {
            removedEnemies.add(enemy);
          }
          enemies.removeAll(removedEnemies, false);
          break;
        case SHOVEL:
          ArrayList<Box> stoneBoxes = new ArrayList<>();
          for (Box box : boxes)
            if ((-2 * SPRITE_SIZE <= box.getBounds().getX()) && (1 * SPRITE_SIZE >= box.getBounds().getX()) &&
                (-13 * SPRITE_SIZE <= box.getBounds().getY()) && (-11 >= box.getBounds().getY()) &&
                (box.getBoxType().equals(BoxType.BRICS))) {
              box.setBoxType(BoxType.WATER);
              stoneBoxes.add(new Box(BoxType.STONE, new Vector2(box.getBounds().getX(), box.getBounds().getY()), SPRITE_SIZE, SPRITE_SIZE));
            }
          boxes.addAll(stoneBoxes);
          shovelTime = runTime;
          break;
        case STAR:
          if (!player.getTankLevel().equals(TankLevel.LEVEL4)) {
            player.setTankLevel(player.getTankLevel().nextLevel());
            player.getObject().setRegion(textureAtlas.findRegion(player.getTankLevel().getNameRegion()));
          }
          break;
        case CLOCK:
          for (Enemy enemy : enemies) {
            enemy.setCanMove(false);
            clock_stop = true;
            enemy.update(delta);
          }
          stopTime = runTime;
          break;
      }
      bonus = null;
    }
    player.update(delta);
  }

  private void updateShots(float delta, Tank tank) {
    tank.removedBullets.clear();
    for (Bullet bullet : tank.bullets) {
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
