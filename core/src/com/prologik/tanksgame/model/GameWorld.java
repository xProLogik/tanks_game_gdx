package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.utils.Utils;

import java.util.ArrayList;

public class GameWorld implements Disposable {

  public static final float PLAYFIELD_MIN_X = -13f;
  public static final float PLAYFIELD_MAX_X = 13f;
  public static final float PLAYFIELD_MIN_Y = -13f;
  public static final float PLAYFIELD_MAX_Y = 13f;
  private TextureAtlas textureAtlas;
  public Tank tank;

  public ArrayList<Box> boxes = new ArrayList<>();
  private ArrayList<Box> removedBoxes = new ArrayList<>();

  public ArrayList<Bullet> bullets = new ArrayList<>();
  private ArrayList<Bullet> removedBullets = new ArrayList<>();

  public GameWorld() {
    textureAtlas = new TextureAtlas("MyAtlas.atlas");
    loadLevel();
  }

  private void loadLevel() {
    tank = new Tank(textureAtlas.findRegion("tank1", 0), new Vector2(-3f, -12f),
        2f, 2f, new Vector2(0f, 1f));
    generatePlaylevel();
  }

  private void generatePlaylevel() {
    Integer[][] levelMap = Utils.levelParser("level1.lvl");

    for (int i = (int) PLAYFIELD_MIN_X; i < PLAYFIELD_MAX_X; i++)
      for (int j = (int) PLAYFIELD_MIN_Y; j < PLAYFIELD_MAX_Y; j++) {
        if ((levelMap[i + 13][j + 13]) != 0) {
          boxes.add(new Box(textureAtlas.findRegion(TileType.nameByNumber(levelMap[i + 13][j + 13])),
              new Vector2(-j, -i), 1f, 1f));
        }
      }
  }

  public void draw(SpriteBatch batch) {
    tank.draw(batch);
    for (Box box : boxes) box.draw(batch);
    for (Bullet bullet : bullets) bullet.draw(batch);
  }

  public void dispose() {
    textureAtlas.dispose();
  }

  public void update(float delta) {
    updateShots(delta);
    updateBlocks(delta);
    updateTank(delta);
  }

  private void updateTank(float delta) {

    for (Box box : boxes) {
      if (tank.collide(box)) {
        //tank.setCanMove(false);
        tank.lastPosition();
      }
    }

    tank.update(delta);
  }

  private void updateShots(float delta) {
    removedBullets.clear();
    for (Bullet bullet : bullets) {
      bullet.setCanMove(true);
      bullet.move(delta);

      if (bullet.isCanMove())
        for (Box box : boxes) {
          if (bullet.collide(box)) {
            bullet.setCanMove(false);
            removedBoxes.add(box);
          }
        }

      bullet.update(delta);

      if (!bullet.isCanMove()) removedBullets.add(bullet);
    }
    for (Bullet bullet : removedBullets) {
      bullets.remove(bullet);
    }
  }

  private void updateBlocks(float delta) {

    for (Box box : removedBoxes) {
      boxes.remove(box);
    }
    removedBoxes.clear();
  }

  public void moveTank(float delta, Vector2 moveDirection) {
    tank.setDirection(moveDirection);
    tank.move(delta);
  }

  public void shot() {

    if (bullets.isEmpty()) {
      Bullet newBullet = new Bullet(textureAtlas.findRegion("bullet_top"), new Vector2(tank.getBounds().getX()+0.5f,tank.getBounds().getY()+0.5f),
          1f, 1f, tank.getDirection());
      bullets.add(newBullet);
    }
  }
}
