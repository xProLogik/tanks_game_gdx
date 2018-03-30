package com.prologik.tanksgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.control.WorldController;
import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameWorld implements Disposable {


  static TextureAtlas textureAtlas;

  static final float SPRITE_SIZE = WorldRenderer.CAM_HEIGHT / 26f;
  static final float PLAYFIELD_MIN_X = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_X = 13f * SPRITE_SIZE;
  static final float PLAYFIELD_MIN_Y = -13f * SPRITE_SIZE;
  static final float PLAYFIELD_MAX_Y = 13f * SPRITE_SIZE;

  public static Map<String, Sound> sounds = new HashMap<String, Sound>() {{
    put("bigexploison", Gdx.audio.newSound(Gdx.files.internal("sounds/bigexploison.wav")));
    put("bricks", Gdx.audio.newSound(Gdx.files.internal("sounds/bricks.wav")));
    put("clock", Gdx.audio.newSound(Gdx.files.internal("sounds/clock.wav")));
    put("dontmove", Gdx.audio.newSound(Gdx.files.internal("sounds/dontmove.wav")));
    put("enemylevel4", Gdx.audio.newSound(Gdx.files.internal("sounds/enemylevel4.wav")));
    put("extralife", Gdx.audio.newSound(Gdx.files.internal("sounds/extralife.wav")));
    put("bonus", Gdx.audio.newSound(Gdx.files.internal("sounds/bonus.wav")));
    put("move", Gdx.audio.newSound(Gdx.files.internal("sounds/move.wav")));
    put("shot", Gdx.audio.newSound(Gdx.files.internal("sounds/shot.wav")));
    put("shovel", Gdx.audio.newSound(Gdx.files.internal("sounds/shovel.wav")));
    put("smallexploison", Gdx.audio.newSound(Gdx.files.internal("sounds/smallexploison.wav")));
    put("star", Gdx.audio.newSound(Gdx.files.internal("sounds/star.wav")));
    put("stone", Gdx.audio.newSound(Gdx.files.internal("sounds/stone.wav")));
    put("gameover", Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.wav")));
    put("startlevel", Gdx.audio.newSound(Gdx.files.internal("sounds/startlevel.wav")));
  }};

  private Player player;
  private ArrayList<Icon> playerLife = new ArrayList<>();

  private Sprite stateSprite;
  private Eagle eagle;

  private Bonus bonus = null;
  private boolean clock_stop = false;
  private float stopTime;
  private float shovelTime;

  private boolean pause = false;


  private float respawnTime = 0;

  private static List<Exploison> exploisons = new LinkedList<>();
  private List<Exploison> exploisonsRemoved = new ArrayList<>();

  public List<ObjectPause> objectPauses = new ArrayList<>();

  private List<Box> boxes = new LinkedList<>();
  private List<Box> removedBoxes = new ArrayList<>();

  private List<Byte> bufferedEnemies = new LinkedList<>();
  private List<Enemy> enemies = new LinkedList<>();
  private List<Enemy> removedEnemies = new ArrayList<>();

  private List<Vector2> placesRespawnEnemy = new ArrayList<Vector2>() {{
    add(new Vector2(PLAYFIELD_MIN_X, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2((PLAYFIELD_MIN_X + PLAYFIELD_MAX_X) / 2 - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2(PLAYFIELD_MAX_X - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
  }};
  private List<Vector2> currentPlaceRespawnEnemy = new LinkedList<>(placesRespawnEnemy);

  private LinkedList<Icon> iconsEnemy = new LinkedList<>();
  private ArrayList<Icon> staticIcon = new ArrayList<>();

  private WorldController contoller;


  public GameWorld() {
    textureAtlas = new TextureAtlas("MyAtlas.atlas");
    contoller = new WorldController(this);

    loadLevel();
  }

  private void loadLevel() {
    sounds.get("startlevel").play(0.5f);
    player = new Player(TankLevel.LEVEL1, new Vector2(0 - 4 * SPRITE_SIZE, PLAYFIELD_MIN_Y));
    player.getObject().setColor(1, 1, 0.2f, 1);
    eagle = new Eagle();
    generatePlayField();
    generateEnemyForLevel();
    generateSides();
  }

  private void generateSides() {
    sideLeft();
    sideRight();
  }

  private void sideRight() {
    Iterator<Byte> it = bufferedEnemies.iterator();
    int i = (int) (PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE);
    while (it.hasNext()) {
      for (int j = (int) (PLAYFIELD_MAX_X + 1 * SPRITE_SIZE); j <= PLAYFIELD_MAX_X + 2 * SPRITE_SIZE; j++) {
        Icon newIcon = new Icon("tank_ico", 0, new Vector2(j, i), SPRITE_SIZE, SPRITE_SIZE);
        if (i < PLAYFIELD_MAX_Y - 12 * SPRITE_SIZE) newIcon.getObject().setAlpha(0);
        iconsEnemy.add(newIcon);
        it.next();
      }
      i--;
    }

    staticIcon.add(new Icon("1player", 0, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
        0 - SPRITE_SIZE),
        2 * SPRITE_SIZE, SPRITE_SIZE));
    staticIcon.add(new Icon("tank_ico", 1, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
        0 - 2 * SPRITE_SIZE), SPRITE_SIZE, SPRITE_SIZE));

    staticIcon.add(new Icon("flag", 0, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
        0 - 9 * SPRITE_SIZE),
        2 * SPRITE_SIZE, 2 * SPRITE_SIZE));
    staticIcon.add(new Icon("number", 1, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
        0 - 10 * SPRITE_SIZE),
        SPRITE_SIZE, SPRITE_SIZE));

  }


  private void sideLeft() {


  }

  private void generatePlayField() {

    int[][] levelMap = Utils.levelParser("level1.lvl");

    for (int i = (int) PLAYFIELD_MIN_X; i < PLAYFIELD_MAX_X; i++)
      for (int j = (int) PLAYFIELD_MIN_Y; j < PLAYFIELD_MAX_Y; j++) {
        Box newBox = new Box(BoxType.typeByNumber(levelMap[i + (int) PLAYFIELD_MAX_X][j + (int) PLAYFIELD_MAX_Y]),
            new Vector2(j, i));
        boxes.add(newBox);
      }
  }

  private void generateEnemyForLevel() {
    Map<String, Integer> enemyParser = Utils.enemyParser("level1.lvl");
    Pattern p = Pattern.compile("[0-9]");
    Matcher m;
    for (Map.Entry<String, Integer> entry : enemyParser.entrySet()) {
      String typeEnemy = entry.getKey();
      m = p.matcher(typeEnemy);
      byte type = Byte.parseByte(m.find() ? m.group() : "1");
      for (int i = 0; i < entry.getValue(); i++) {
        bufferedEnemies.add(type);
      }
    }
  }

  private void generateEnemy(float delta) {
    if ((enemies.size() < 4) && (bufferedEnemies.size() > 0)) {
      if (respawnTime > 2) {
        if (currentPlaceRespawnEnemy.size() < 1)
          currentPlaceRespawnEnemy = new LinkedList<>(placesRespawnEnemy);
        byte nameRegion = bufferedEnemies.get((int) Math.round(Math.random() * (bufferedEnemies.size() - 1)));
        Vector2 position = currentPlaceRespawnEnemy.get((int) Math.round(Math.random() * (currentPlaceRespawnEnemy.size() - 1)));
        createEnemy(TankLevel.typeByNumber(nameRegion), position);//
        currentPlaceRespawnEnemy.remove(position);
        bufferedEnemies.remove((Byte) nameRegion);
        respawnTime = 0;
        iconsEnemy.removeLast();
      }
      respawnTime += delta;

    }

  }

  private void createEnemy(TankLevel tankLevel, Vector2 position) {
    Enemy newEnemy = new Enemy(tankLevel, position);
    if (Math.random() < .5) {
      newEnemy.setBonusEnemy(true);
      newEnemy.setColorBlink(true);
    }
    for (Enemy enemy : enemies)
      if (newEnemy.collide(enemy)) {
        newEnemy.setSpawnCollide(true);
        enemy.setSpawnCollide(true);
      }
    if (newEnemy.collide(player)) {
      newEnemy.setSpawnCollide(true);
      player.setSpawnCollide(true);
    }
    newEnemy.getObject().setAlpha(1);
    enemies.add(newEnemy);
  }

  private void createBonus() {
    bonus = null;
    GameWorld.sounds.get("bonus").play(0.3f);
    bonus = new Bonus(new Vector2(
        (float) Math.random() * (PLAYFIELD_MAX_X - 2f * SPRITE_SIZE - PLAYFIELD_MIN_X) - PLAYFIELD_MAX_X,
        (float) Math.random() * (PLAYFIELD_MAX_Y - 2f * SPRITE_SIZE - PLAYFIELD_MIN_Y) - PLAYFIELD_MAX_Y));
  }

  static void createSmallExploison(Bullet bullet) {
    Vector2 newPosition = new Vector2(bullet.position.x, bullet.position.y);
    switch (bullet.currentFacing) {
      case -90:
        newPosition.x = bullet.position.x;
        newPosition.y = bullet.position.y - 0.5f;
        break;
      case 90:
        newPosition.x = bullet.position.x - 1f;
        newPosition.y = bullet.position.y - 0.5f;
        break;
      case 0:
        newPosition.x = bullet.position.x - 0.5f;
        newPosition.y = bullet.position.y;
        break;
      case 180:
        newPosition.x = bullet.position.x - 0.5f;
        newPosition.y = bullet.position.y - 1f;
        break;
    }
    exploisons.add(new Exploison(3, newPosition));
  }

  private void createBigExploison(Vector2 position) {
    sounds.get("smallexploison").play(0.3f);
    exploisons.add(new Exploison(5, position));
  }

  public void createWindowPause() {
    ObjectPause eclipse = new ObjectPause("empty", new Vector2(PLAYFIELD_MIN_X, PLAYFIELD_MIN_Y),
        26f,26f);
    eclipse.getObject().setAlpha(0.6f);
    objectPauses.add(eclipse);
    ObjectPause stateGame = null;
    if (pause){
      stateGame = new ObjectPause("pause",new Vector2(-2.5f,0),
          5f,1f);
    }
    if (eagle.isGameover()){
      stateGame = new ObjectPause("gameover",new Vector2(-2f,-15f),
          4f,2f);
    }
    objectPauses.add(stateGame);
  }


  public void draw(SpriteBatch batch) {


    for (Icon icon : iconsEnemy) icon.draw(batch);
    for (Icon icon : staticIcon) icon.draw(batch);
    for (Icon icon : playerLife) icon.draw(batch);

    for (Box box : boxes)
      if (!box.getBoxType().equals(BoxType.GRASS))
        box.draw(batch);
    eagle.draw(batch);
    player.draw(batch);
    for (Enemy enemy : enemies) enemy.draw(batch);
    for (Box box : boxes)
      if (box.getBoxType().equals(BoxType.GRASS))
        box.draw(batch);
    if (bonus != null)
      bonus.draw(batch);
    for (Exploison exploison : exploisons) exploison.draw(batch);
    for (ObjectPause objectPause : objectPauses) objectPause.draw(batch);
  }


  public void update(float delta, float runTime) {
    updateExploisons(delta);
    updateWindowPause(delta);
    contoller.update(delta);
    if (!pause && !eagle.isGameover()) {
      updateIconPlayerLife();
      generateEnemy(delta);
      updateBonus(delta, runTime);
      updateEnemies(delta, runTime);
      updateBoxes(runTime);
      updateShots(delta, player);
      for (Enemy enemy : enemies) {
        updateShots(delta, enemy);
        updateBullets(enemy);
      }
      updatePlayer(delta, runTime);

    }

  }

  private void updateWindowPause(float delta) {
    for (ObjectPause objectPause : objectPauses) {
        objectPause.update(delta);

    }
  }


  private void updateExploisons(float delta) {
    exploisonsRemoved.clear();
    for (Exploison exploison : exploisons) {
      if (exploison.exploisonAnimation.isAnimationFinished(exploison.stateTime))
        exploisonsRemoved.add(exploison);
      exploison.update(delta);
    }
    exploisons.removeAll(exploisonsRemoved);
  }

  private void updateIconPlayerLife() {
    playerLife.clear();
    int life = player.tankLife;
    int x = 0;
    while (life > 0) {
      life /= 10;
      x++;
    }
    x--;
    life = player.tankLife;
    while (life > 0) {
      playerLife.add(new Icon("number", life % 10,
          new Vector2(PLAYFIELD_MAX_X + 2 * SPRITE_SIZE + x,
              0 - 2 * SPRITE_SIZE), SPRITE_SIZE, SPRITE_SIZE));
      life /= 10;
      x--;
    }
  }

  private void updateBonus(float delta, float runTime) {
    if (bonus != null && bonus.getBonusTime() < 0) bonus = null;
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
          if (box.getBoxType().equals(BoxType.ICE))
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
    if (bonus != null) bonus.update(delta, runTime);
  }


  private void updateBullets(Enemy enemy) {
    enemy.removedBullets.clear();
    player.removedBullets.clear();
    for (Bullet bulletPlayer : player.bullets) {
      for (Bullet bulletEnemy : enemy.bullets) {
        if (bulletEnemy.collide(bulletPlayer)) {
          bulletEnemy.setCanMove(false);
          bulletPlayer.setCanMove(false);
          createSmallExploison(bulletPlayer);
        }
        if (!bulletEnemy.isCanMove())
          enemy.removedBullets.add(bulletEnemy);
        if (!bulletPlayer.isCanMove())
          player.removedBullets.add(bulletPlayer);

      }
    }
    player.bullets.removeAll(player.removedBullets);
    enemy.bullets.removeAll(enemy.removedBullets);
  }

  private void updateEnemies(float delta, float runTime) {

    LinkedList<Tank> collideEnemies = new LinkedList<>(enemies);
    collideEnemies.add(player);

    for (Enemy tankEnemy : enemies)
      if (tankEnemy.isSpawnCollide()) {
        int collide = 0;
        for (Tank collideEnemy : collideEnemies)
          if (!collideEnemy.equals(tankEnemy))
            if (tankEnemy.collide(collideEnemy))
              collide++;
        if (collide == 0) tankEnemy.setSpawnCollide(false);
      }

    removedEnemies.clear();
    for (Enemy tankEnemy : enemies) {
      if (tankEnemy.isColorBlink())
        tankEnemy.enemyColorBlink(runTime);
      if (!clock_stop) {
        tankEnemy.randomMovement();
        tankEnemy.move(delta, runTime);
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
          player.removedBullets.add(bullet);
          if (tankEnemy.isBonusEnemy()) {
            createBonus();
            tankEnemy.setBonusEnemy(false);
          }
          tankEnemy.tankLife--;
          if (tankEnemy.tankLife < 1) {
            createBigExploison(tankEnemy.position);
            removedEnemies.add(tankEnemy);
          }
          if (tankEnemy.getTankLevel().equals(TankLevel.LEVEL4)) {
            GameWorld.sounds.get("enemylevel4").play(0.3f);
            tankEnemy.declineForLevel4();
          }
        }
      }
      player.bullets.removeAll(player.removedBullets);

      for (Tank tankCollide : collideEnemies)
        if (!(tankCollide.equals(tankEnemy)))
          if (tankCollide.collide(tankEnemy) || tankEnemy.collide(tankCollide)) {
            if (!tankEnemy.isSpawnCollide())
              tankEnemy.setCanMove(false);
            if (!tankCollide.isSpawnCollide())
              tankCollide.setCanMove(false);
            if (!clock_stop) {
              tankEnemy.randomMove();
              if (!tankCollide.equals(player))
                ((Enemy) tankCollide).randomMove();
            }
          }
      tankEnemy.update(delta);
    }
    enemies.removeAll(removedEnemies);
  }

  private void updateBoxes(float runTime) {
    for (Box box : boxes)
      if (box.getBoxType().equals(BoxType.WATER))
        box.getObject().setRegion((TextureRegion) box.waterAnimation.getKeyFrame(runTime));
    boxes.removeAll(removedBoxes);
    removedBoxes.clear();
  }

  private void updatePlayer(float delta, float runTime) {
    if (player.isSpawnCollide()) {
      int collide = 0;
      for (Enemy enemy : enemies)
        if (player.collide(enemy))
          collide++;
      if (collide == 0) player.setSpawnCollide(false);
    }
    for (Box box : boxes) {
      if (player.collideIce(box))
        player.setOnIce(true);
      if (player.collide(box)) {
        player.setCanMove(false);
      }
    }
    if ((bonus != null) && (player.collide(bonus))) {
      switch (bonus.getBonusType()) {
        case HELMET:
          GameWorld.sounds.get("star").play(0.3f);
          break;
        case EXTRA_LIFE:
          GameWorld.sounds.get("extralife").play(0.3f);
          player.tankLife++;
          break;
        case GRENADE:
          for (Enemy enemy : enemies) {
            createBigExploison(enemy.position);
            if (enemy.isBonusEnemy()) createBonus();
          }
          removedEnemies.addAll(enemies);
          enemies.removeAll(removedEnemies);
          break;
        case SHOVEL:
          GameWorld.sounds.get("shovel").play(0.3f);
          ArrayList<Box> stoneBoxes = new ArrayList<>();
          for (int i = (int) PLAYFIELD_MIN_Y; i < PLAYFIELD_MIN_Y + 3 * SPRITE_SIZE; i++) {
            for (int j = -2 * (int) SPRITE_SIZE; j < 2 * SPRITE_SIZE; j++) {
              if (!((j == -1) && (i == -13) || (j == 0) && (i == -13) || (j == -1) && (i == -12) || (j == 0) && (i == -12))) {
                stoneBoxes.add(new Box(BoxType.STONE, new Vector2(j, i)));
              }
            }
          }
          for (Box box : boxes)
            if ((-2 * SPRITE_SIZE <= box.getObject().getX()) && (1 * SPRITE_SIZE >= box.getObject().getX()) &&
                (-13 * SPRITE_SIZE <= box.getObject().getY()) && (-11 >= box.getObject().getY()) &&
                (box.getBoxType().equals(BoxType.BRICS)))
              box.setBoxType(BoxType.ICE);
          boxes.addAll(stoneBoxes);
          shovelTime = runTime;
          break;
        case STAR:
          GameWorld.sounds.get("star").play(0.3f);
          if (!player.getTankLevel().equals(TankLevel.LEVEL4)) {
            player.setTankLevel(player.getTankLevel().nextLevel());
          }
          break;
        case CLOCK:
          GameWorld.sounds.get("clock").play(0.3f);
          for (Enemy enemy : enemies) {
            enemy.setCanMove(false);
            enemy.lastPosition();
            clock_stop = true;
          }
          stopTime = runTime;
          break;
      }
      bonus = null;
    }
    for (Enemy enemy : enemies) {
      for (Bullet bullet : enemy.bullets) {
        if (bullet.isCanMove())
          if (bullet.collide(player)) {
            bullet.setCanMove(false);
            player.setCanMove(false);
            createBigExploison(player.position);
            player.position = new Vector2(0 - 4 * SPRITE_SIZE, PLAYFIELD_MIN_Y);
            player.tankLife--;
            player.setTankLevel(TankLevel.LEVEL1);

          }
        if (!bullet.isCanMove()) enemy.removedBullets.add(bullet);
      }
      enemy.bullets.removeAll(enemy.removedBullets);
    }
    player.update(delta, runTime);
  }

  private void updateShots(float delta, Tank tank) {
    tank.removedBullets.clear();
    for (Bullet bullet : tank.bullets) {
      bullet.move(delta);
      if (bullet.isCanMove()) {
        for (Box box : boxes) {
          if (bullet.collide(box)) {
            bullet.setCanMove(false);
            createSmallExploison(bullet);
            if (box.getBoxType().equals(BoxType.STONE))
              if ((tank.equals(player)) && ((Player) tank).getTankLevel().equals(TankLevel.LEVEL4)) {
                GameWorld.sounds.get("bricks").play(0.3f);
                removedBoxes.add(box);
              } else GameWorld.sounds.get("stone").play(0.3f);

            if (box.getBoxType().equals(BoxType.BRICS)) {
              GameWorld.sounds.get("bricks").play(0.2f);
              if (box.changeBox(bullet.direction))
                removedBoxes.add(box);
            }
          }
        }
        if (bullet.collide(eagle)) {
          createBigExploison(eagle.position);
          bullet.setCanMove(false);
          eagle.gameover();
          createWindowPause();
        }
      }
      bullet.update(delta);

      if (!bullet.isCanMove()) tank.removedBullets.add(bullet);
    }
    tank.bullets.removeAll(tank.removedBullets);

  }

  public void setPause(boolean pause) {
    this.pause = pause;
  }

  public boolean isPause() {
    return pause;
  }

  @Override
  public void dispose() {
    textureAtlas.dispose();
  }

}
