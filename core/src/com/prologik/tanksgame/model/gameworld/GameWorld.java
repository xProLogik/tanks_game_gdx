package com.prologik.tanksgame.model.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.gameworld.WorldController;
import com.prologik.tanksgame.model.gameworld.anotherobjects.*;
import com.prologik.tanksgame.model.gameworld.movableobjects.*;
import com.prologik.tanksgame.utils.gui.Button;
import com.prologik.tanksgame.view.MainMenuScreen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameWorld implements Disposable {

  private LevelManager levelManager;
  public MainGame mainGame;

  public static final float SPRITE_SIZE = 1f;
  public static final float PLAYFIELD_MIN_X = -13f * SPRITE_SIZE;
  public static final float PLAYFIELD_MAX_X = 13f * SPRITE_SIZE;
  public static final float PLAYFIELD_MIN_Y = -13f * SPRITE_SIZE;
  public static final float PLAYFIELD_MAX_Y = 13f * SPRITE_SIZE;

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
    put("star", Gdx.audio.newSound(Gdx.files
        .internal("sounds/star.wav")));
    put("stone", Gdx.audio.newSound(Gdx.files.internal("sounds/stone.wav")));
    put("gameover", Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.wav")));
    put("startlevel", Gdx.audio.newSound(Gdx.files.internal("sounds/startlevel.wav")));
  }};

  private List<Player> players = new ArrayList<>();
  private int countPlayers;
  private List<Sprite> leftSide = new ArrayList<>();
  private List<Vector2> placesRespawnEnemy = new ArrayList<Vector2>() {{
    add(new Vector2(PLAYFIELD_MIN_X, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2((PLAYFIELD_MIN_X + PLAYFIELD_MAX_X) / 2 - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
    add(new Vector2(PLAYFIELD_MAX_X - 2 * SPRITE_SIZE, PLAYFIELD_MAX_Y - 2 * SPRITE_SIZE));
  }};
  private List<Vector2> currentPlaceRespawnEnemy = new LinkedList<>(placesRespawnEnemy);

  private List<Enemy> enemyImages = new ArrayList<Enemy>() {{
    add(new Enemy(TankLevel.LEVEL1, new Vector2(-1, 5)));
    add(new Enemy(TankLevel.LEVEL2, new Vector2(-1, 1)));
    add(new Enemy(TankLevel.LEVEL3, new Vector2(-1, -3)));
    add(new Enemy(TankLevel.LEVEL4, new Vector2(-1, -7)));
  }};
  private List<Icon> numberKillingEnemies = new ArrayList<>();
  private List<Icon> numberPlayerScore = new ArrayList<>();
  private boolean firstEndlevel = true;

  private List<Exploison> exploisons = new ArrayList<>();
  private List<Exploison> exploisonsRemoved = new ArrayList<>();

  private List<Points> points = new ArrayList<>();
  private List<Points> pointsRemoved = new ArrayList<>();
  private int allPoints = 0;

  private Bonus bonus = null;
  private boolean clock_stop = false;
  private float stopTime;

  private boolean pause = false;
  private boolean gameover = false;

  private Button pauseButton;
  private Button menuButton;

  private List<ObjectPause> objectPauses = new ArrayList<>();

  private Level level;
  private int numberLevel;

  private List<Byte> bufferedEnemies = new LinkedList<>();
  private List<Enemy> enemies = new LinkedList<>();
  public List<Enemy> removedEnemies = new ArrayList<>();


  private LinkedList<Icon> iconsEnemy = new LinkedList<>();
  private ArrayList<Icon> staticIcon = new ArrayList<>();

  private WorldController contoller;
  private float respawnTime;
  private boolean endLevel = false;


  public GameWorld(MainGame game, int countPlayer, String nameLevel) {
    this.mainGame = game;
    levelManager = new LevelManager(this);
    contoller = new WorldController(this);

    this.countPlayers = countPlayer;
    for (int i = 0; i < countPlayers; i++) {
      createPlayer(TankLevel.LEVEL1, new Vector2((8 * i - 5) * GameWorld.SPRITE_SIZE, GameWorld.PLAYFIELD_MIN_Y));
    }
    loadLevel(nameLevel);
    pauseButton = new Button(new Vector2(-17.5f, 11), 4, 1.5f, "PAUSE", mainGame);
    menuButton = new Button(new Vector2(10.5f, -10f), 5, 2f, "MENU", mainGame);
  }


  private void drawPlayField() {
    float sideField = (mainGame.HEIGHT - 2) / 2;
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.setColor(0, 0, 0, 1);
    mainGame.shapeRenderer.rect(-sideField, -sideField, sideField * 2f, sideField * 2f);
    mainGame.shapeRenderer.end();
  }

  public void loadLevel(String nameLevel) {
    Pattern p = Pattern.compile("[0-9]{1,2}");
    Matcher m = p.matcher(nameLevel);
    if (m.find()) numberLevel = Integer.parseInt(m.group());
    levelManager.loadLevel(nameLevel);
    generateSides();
  }

  private void createPlayer(TankLevel level, Vector2 position) {
    Player player = new Player(level, position);
    players.add(player);
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
        if (it.hasNext()) it.next();
        else continue;
        Icon newIcon = new Icon("tank_ico", 0, new Vector2(j, i), SPRITE_SIZE, SPRITE_SIZE);
        if (i < PLAYFIELD_MAX_Y - 12 * SPRITE_SIZE) newIcon.getObject().setAlpha(0);
        iconsEnemy.add(newIcon);

      }
      i--;
    }

    for (Player player : players) {
      staticIcon.add(new Icon(player.getNumberPlayer() + "player", 0,
          new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,

              0 - SPRITE_SIZE - 3f * (player.getNumberPlayer() - 1)),
          2 * SPRITE_SIZE, SPRITE_SIZE));
      staticIcon.add(new Icon("tank_ico", 1, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
          0 - 2 * SPRITE_SIZE - 3f * (player.getNumberPlayer() - 1)), SPRITE_SIZE, SPRITE_SIZE));
    }

    staticIcon.add(new Icon("flag", 0, new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE,
        0 - 9 * SPRITE_SIZE),
        2 * SPRITE_SIZE, 2 * SPRITE_SIZE));
    int number = numberLevel;
    int count = 0;
    do {
      count++;
      number /= 10;
    } while (number / 10 != 0);
    number = numberLevel;
    for (int j = count; j > 0; j--) {
      Icon newIcon = new Icon("number", number % 10,
          new Vector2(PLAYFIELD_MAX_X + 1 * SPRITE_SIZE + j - 1, 0 - 10 * SPRITE_SIZE),
          SPRITE_SIZE, SPRITE_SIZE);
      newIcon.getObject().setColor(Color.BLACK);
      staticIcon.add(newIcon);
      number /= 10;
    }
  }

  private void sideLeft() {
    for (Player player : players) {
      Sprite newSprite = new Sprite(MainGame.sptriteAtlas.findRegion("player" + player.getNumberPlayer() + "controll"));
      newSprite.setSize(4, 5.5f + (player.getNumberPlayer() - 1));
      newSprite.setPosition(-17.5f, 4.5f - 8f * (player.getNumberPlayer() - 1));
      leftSide.add(newSprite);
    }
  }


  private void generateEnemy(float delta) {
    if ((enemies.size() < 4) && (bufferedEnemies.size() > 0)) {
      if (respawnTime > 2) {
        if (currentPlaceRespawnEnemy.size() < 1)
          currentPlaceRespawnEnemy = new LinkedList<>(placesRespawnEnemy);
        byte nameRegion = bufferedEnemies.get((int) Math.round(Math.random() * (bufferedEnemies.size() - 1)));
        Vector2 position = currentPlaceRespawnEnemy.get((int) Math.round(Math.random() * (currentPlaceRespawnEnemy.size() - 1)));
        createEnemy(TankLevel.values()[nameRegion - 1], position);
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
    enemies.add(newEnemy);
  }

  public synchronized void createBonus() {
    bonus = null;
    GameWorld.sounds.get("bonus").play(0.3f);
    bonus = new Bonus(new Vector2(
        (float) Math.random() * (PLAYFIELD_MAX_X - 2f * SPRITE_SIZE - PLAYFIELD_MIN_X) - PLAYFIELD_MAX_X,
        (float) Math.random() * (PLAYFIELD_MAX_Y - 2f * SPRITE_SIZE - PLAYFIELD_MIN_Y) - PLAYFIELD_MAX_Y));
  }


  public void createWindowPause() {
    ObjectPause eclipse = new ObjectPause("empty", new Vector2(PLAYFIELD_MIN_X, PLAYFIELD_MIN_Y),
        26f, 26f);
    eclipse.getObject().setAlpha(0.6f);
    objectPauses.add(eclipse);
    ObjectPause stateGame = null;
    if (pause) {
      stateGame = new ObjectPause("pause", new Vector2(-2.5f, 0),
          5f, 1f);
    }
    if (gameover) {
      stateGame = new ObjectPause("gameover", new Vector2(-2f, -15f),
          4f, 2f);
    }
    objectPauses.add(stateGame);
  }


  public void draw(SpriteBatch batch) {
    if (!endLevel) {
      drawPlayField();
      mainGame.bitmapFont.getData().setScale(0.027f);
      pauseButton.draw(batch);
      mainGame.bitmapFont.getData().setScale(0.04f);
      batch.begin();
      leftSide.forEach(sprite -> sprite.draw(batch));
      iconsEnemy.forEach(icon -> icon.draw(batch));
      staticIcon.forEach(icon -> icon.draw(batch));
      players.forEach(player -> player.draw(batch));
      level.draw(batch);
      exploisons.forEach(exploison -> exploison.draw(batch));
      enemies.forEach(enemy -> enemy.draw(batch));
      for (Box box : level.getBoxes())
        if (box.getBoxType().equals(BoxType.GRASS))
          box.draw(batch);
      if (bonus != null)
        bonus.draw(batch);
      objectPauses.forEach(objectPause -> objectPause.draw(batch));
      points.forEach(points -> points.draw(batch));
      batch.end();
      if (pause)
        if (menuButton != null) menuButton.draw(batch);

    } else {
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      batch.begin();
      enemyImages.forEach(enemy -> enemy.draw(batch));

      numberKillingEnemies.forEach(icon -> icon.draw(batch));
      numberPlayerScore.forEach(icon -> icon.draw(batch));
      textEndMenu(batch);
      batch.end();
    }
  }

  private void textEndMenu(SpriteBatch batch) {
    mainGame.bitmapFont.draw(batch, "TOTAL SCORE:", -12, 12);
    int count = 1;
    if (numberKillingEnemies.size() > 7) count = 2;
    for (int i = 0; i < count; i++) {
      mainGame.bitmapFont.draw(batch, (i + 1) + "   PLAYER", -10 + 13 * i, 9);
    }
  }


  public void update(float delta, float runTime) {
    if (bufferedEnemies.size() == 0 && enemies.size() == 0 && bonus == null) {
      endLevel = true;
    }
    contoller.update(delta);
    if (!endLevel) {
      exploisonsUpdate(delta);
      pointsUpdate(delta);
      updateWindowPause(delta);
      if (!pause && !gameover) {
        generateEnemy(delta);
        if (bonus != null)
          updateBonus(delta, runTime);
        level.update(runTime);
        updateShots();
        updateAllTanks(delta, runTime);
        checkForGameover();
      }
    } else {
      endLevel();
    }
  }

  private void checkForGameover() {
    Player removePlayer = null;
    int countDead = 0;
    for (Player player : players) {
      if (player.isDead()) {
        countDead++;
        player.delete();
        removePlayer = player;
        iconsEndLevel(player);
      }

    }
    if (countDead == players.size()) gameover();
    if (removePlayer != null) players.remove(removePlayer);
  }

  private void gameover() {
    setGameover(true);
    GameWorld.sounds.forEach((name, sound) -> sound.stop());
    GameWorld.sounds.get("smallexploison").play();
    GameWorld.sounds.get("gameover").play(0.5f);
    createWindowPause();
  }

  private void endLevel() {
    GameWorld.sounds.forEach((name, sound) -> sound.stop());
    if (firstEndlevel) {
      createEndLevelScreen();
      firstEndlevel = false;
    }
  }

  private void createEndLevelScreen() {
    for (Player player : players) {
      iconsEndLevel(player);
    }
    int number = allPoints;
    int count = 0;
    do {
      count++;
      number /= 10;
    } while (number != 0);
    number = allPoints;
    for (int i = 0; i < count; i++) {
      Icon newIcon = new Icon("number", number % 10,
          new Vector2(5 - i, 11f), 1, 1);
      numberPlayerScore.add(newIcon);
      number /= 10;
    }
  }

  private void iconsEndLevel(Player player) {
    allPoints += player.getPoints();
    int sumKillingEnemy = 0;
    int sumScore = 0;
    for (Enemy enemy : enemyImages) {
      float positionY = enemy.position.y + 0.5f;
      int countKillingEnemy = 0;
      int scoreForKillingEnenmy = 0;
      if (player.getKillingEnemy().get(enemy.getTankLevel()) != null) {
        countKillingEnemy = player.getKillingEnemy().get(enemy.getTankLevel());
        scoreForKillingEnenmy = 100 * (enemy.getTankLevel().ordinal() + 1) * countKillingEnemy;
        sumKillingEnemy += countKillingEnemy;
        sumScore += scoreForKillingEnenmy;
      }
      int number = countKillingEnemy;
      int count = 0;
      do {
        count++;
        number /= 10;
      } while (number != 0);
      number = countKillingEnemy;
      for (int i = 0; i < count; i++) {
        Icon newIcon = new Icon("number", number % 10,
            new Vector2(-4 + 8 * (player.getNumberPlayer() - 1) - i, positionY), 1, 1);
        numberKillingEnemies.add(newIcon);
        number /= 10;
      }
      number = scoreForKillingEnenmy;
      count = 0;
      do {
        count++;
        number /= 10;
      } while (number != 0);
      number = scoreForKillingEnenmy;
      for (int i = 0; i < count; i++) {
        Icon newIcon = new Icon("number", number % 10,
            new Vector2(-7 + 16 * (player.getNumberPlayer() - 1) - i, positionY), 1, 1);
        numberPlayerScore.add(newIcon);
        number /= 10;
      }
    }
    float positionY = enemyImages.stream().
        min((o1, o2) -> Float.compare(o1.position.y, o2.position.y)).get().position.y - 4;
    int number = sumKillingEnemy;
    int count = 0;
    do {
      count++;
      number /= 10;
    } while (number != 0);
    number = sumKillingEnemy;
    for (int i = 0; i < count; i++) {
      Icon newIcon = new Icon("number", number % 10,
          new Vector2(-4 + 8 * (player.getNumberPlayer() - 1) - i, positionY), 1, 1);
      numberKillingEnemies.add(newIcon);
      number /= 10;
    }
    number = sumScore;
    count = 0;
    do {
      count++;
      number /= 10;
    } while (number != 0);
    number = sumScore;
    for (int i = 0; i < count; i++) {
      Icon newIcon = new Icon("number", number % 10,
          new Vector2(-7 + 16 * (player.getNumberPlayer() - 1) - i, positionY), 1, 1);
      numberPlayerScore.add(newIcon);
      number /= 10;
    }
  }

  private void pointsUpdate(float delta) {
    pointsRemoved.clear();
    for (Points point : points) {
      point.update(delta);
      if (point.getPointsTime() > 0.5)
        pointsRemoved.add(point);
    }
    points.removeAll(pointsRemoved);
  }

  private void exploisonsUpdate(float delta) {
    exploisonsRemoved.clear();
    for (Exploison exploison : exploisons) {
      exploison.update(delta);
      if (exploison.getExploisonAnimation().isAnimationFinished(exploison.getStateTime()))
        exploisonsRemoved.add(exploison);
    }
    exploisons.removeAll(exploisonsRemoved);
  }

  private void clearLevel() {
    for (Player player : players) {
      player.setSpawnPosition();
    }
    bufferedEnemies.clear();
    enemies.clear();
    clock_stop = false;
    staticIcon.clear();
    firstEndlevel = true;
  }


  private void updateWindowPause(float delta) {
    for (ObjectPause objectPause : objectPauses) {
      objectPause.update(delta);
    }
  }


  private void updateBonus(float delta, float runTime) {

    if (bonus.getBonusTime() < 0) bonus = null;

    if (runTime - level.getShovelTime() > 6)
      level.blinkShovel(runTime);
    if (runTime - level.getShovelTime() > 10 && level.isShovelArmor())
      level.destroyShovel();

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

    for (Player player : players) {
      if (bonus != null && player.collide(bonus)) {
        createPoints(bonus, 500, player);
        switch (bonus.getBonusType()) {
          case HELMET:
            GameWorld.sounds.get("star").play(0.3f);
            player.setHelmetArmor(true);
            bonus = null;
            break;
          case EXTRA_LIFE:
            player.extraLife();
            bonus = null;
            break;
          case GRENADE:
            bonus = null;
            for (Enemy enemy : enemies) {
              enemy.createExploison(this);
              createPoints(enemy, 100 * (enemy.getTankLevel().ordinal() + 1), player);
              if (enemy.isBonusEnemy()) createBonus();
            }
            removedEnemies.addAll(enemies);
            enemies.removeAll(removedEnemies);
            break;
          case SHOVEL:
            GameWorld.sounds.get("shovel").play(0.3f);
            level.createShovel(runTime);
            bonus = null;
            break;
          case STAR:
            GameWorld.sounds.get("star").play(0.3f);
            player.upLevel();
            bonus = null;
            break;
          case CLOCK:
            GameWorld.sounds.get("clock").play(0.3f);
            for (Enemy enemy : enemies) {
              enemy.setCanMove(false);
              enemy.lastPosition();
              clock_stop = true;
            }
            stopTime = runTime;
            bonus = null;
            break;
        }
      }
    }
  }

  public void createPoints(GameObject obj, int point, Player player) {
    points.add(new Points(point, new Vector2(obj.position.x, obj.position.y + 0.5f)));
    player.addPoints(point);
    if (point != 500) {
      player.addKillingEnemy(((Enemy) obj).getTankLevel());
    }
  }

  private void updateShots() {


    List<Tank> allTanks = new ArrayList<>(enemies);
    allTanks.addAll(players);

    List<Bullet> allBullets = new ArrayList<>();
    for (Enemy enemy : enemies)
      allBullets.addAll(enemy.getBullets());
    for (Player player : players)
      allBullets.addAll(player.getBullets());

    for (Bullet bullet : allBullets) {
      if (bullet.isCanMove()) {
        level.interaction(bullet, this);
        bulletsCollide(allBullets, bullet);
        bulletCollideWithTanks(allTanks, bullet);
        if (bullet.isLeftTheField()) bullet.createExploison(this);
      }
    }

  }

  private void bulletCollideWithTanks(List<Tank> allTanks, Bullet bullet) {
    removedEnemies.clear();
    for (Tank tank : allTanks)
      if ((!bullet.getGunner().equals(tank)) && (bullet.collide(tank)) &&
          ((players.contains(bullet.getGunner()) && enemies.contains(tank)) ||
              (enemies.contains(bullet.getGunner()) && players.contains(tank)))) {
        bullet.setCanMove(false);
        if (tank.getTimeAfterSpawn() > 1)
          tank.hitTheTank(bullet.getGunner(), this);
      }
    enemies.removeAll(removedEnemies);
  }

  private void bulletsCollide(List<Bullet> allBullets, Bullet bullet) {
    for (Bullet anotherBullet : allBullets)
      if (anotherBullet.isCanMove() && bullet.collide(anotherBullet) && !anotherBullet.equals(bullet)) {
        bullet.setCanMove(false);
        anotherBullet.setCanMove(false);
        bullet.createExploison(this);
      }
  }

  private void updateAllTanks(float delta, float runTime) {

    List<Tank> allTanks = new ArrayList<>(enemies);
    allTanks.addAll(players);

    for (Tank tank : allTanks)
      if (tank.isSpawnCollide()) {
        int collide = 0;
        for (Tank anotherTank : allTanks)
          if (!anotherTank.equals(tank))
            if (tank.collide(anotherTank))
              collide++;
        if (collide == 0) tank.setSpawnCollide(false);
      }

    for (Tank tank : allTanks) {
      if (enemies.contains(tank)) {
        if (((Enemy) tank).isColorBlink())
          ((Enemy) tank).enemyColorBlink(runTime);
        if (!clock_stop) {
          ((Enemy) tank).randomMovement();
          tank.move(delta, runTime);
        }
      }

      for (Tank anotherTank : allTanks) {
        if (!tank.equals(anotherTank)) {
          if (tank.collide(anotherTank)) {
            if (!tank.isSpawnCollide()) {
              tank.setCanMove(false);
            }
            if (!anotherTank.isSpawnCollide())
              anotherTank.setCanMove(false);
            if (!clock_stop) {
              if (enemies.contains(tank))
                ((Enemy) tank).randomMove();
              if (enemies.contains(anotherTank))
                ((Enemy) anotherTank).randomMove();
            }
          }

        }
      }
      level.interaction(tank, clock_stop);
      tank.update(delta, runTime);
    }

  }

  private void setPause(boolean pause) {
    this.pause = pause;
  }

  public boolean isPause() {
    return pause;
  }


  public boolean isGameover() {
    return gameover;
  }

  public void setGameover(boolean gameover) {
    this.gameover = gameover;
  }

  @Override
  public void dispose() {
    players.forEach(player -> player.delete());

  }

  public void pause() {
    setPause(true);
    menuButton = new Button(new Vector2(-5, -3), 10, 2, "MAIN MENU", mainGame);
    menuButton.setColorButton(new Color(0, 0, 0, 0.6f));
    createWindowPause();
    GameWorld.sounds.forEach((name, sound) -> sound.pause());
  }

  public void unPause() {
    setPause(false);
    objectPauses.clear();
    GameWorld.sounds.forEach((name, sound) -> sound.resume());
  }

  public void exitInMenu() {
    GameWorld.sounds.forEach((name, sound) -> sound.stop());
    dispose();
    mainGame.setScreen(new MainMenuScreen(mainGame));

  }

  public List<Exploison> getExploisons() {
    return exploisons;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public void setBufferedEnemies(List<Byte> bufferedEnemies) {
    this.bufferedEnemies = bufferedEnemies;
  }

  public boolean isEndLevel() {
    return endLevel;
  }

  public void nextLevel() {
    clearLevel();
    levelManager.changeLevel();
    endLevel = false;
    numberKillingEnemies.clear();
    numberPlayerScore.clear();
    players.forEach(player -> player.getKillingEnemy().clear());
  }

  public void setEndLevel(boolean endLevel) {
    this.endLevel = endLevel;
  }

  public Button getPauseButton() {
    return pauseButton;
  }

  public Button getMenuButton() {
    return menuButton;
  }
}
