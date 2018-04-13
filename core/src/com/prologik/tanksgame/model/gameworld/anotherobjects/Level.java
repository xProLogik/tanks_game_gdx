package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.model.gameworld.movableobjects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Level {

  private List<Box> boxes = new LinkedList<>();
  private List<Box> removedBoxes = new ArrayList<>();

  private Eagle eagle;
  private float shovelTime;
  private boolean shovelArmor = false;

  Level(String nameLevel, LevelManager meneger) {

    int[][] levelMap = meneger.levelParser(nameLevel);

    for (int i = (int) GameWorld.PLAYFIELD_MIN_X; i < GameWorld.PLAYFIELD_MAX_X; i++)
      for (int j = (int) GameWorld.PLAYFIELD_MIN_Y; j < GameWorld.PLAYFIELD_MAX_Y; j++) {
        if (levelMap[i + (int) GameWorld.PLAYFIELD_MAX_X][j +
            (int) GameWorld.PLAYFIELD_MAX_Y] != 0) {
          Box newBox = new Box(BoxType.values()[levelMap[i + (int) GameWorld.PLAYFIELD_MAX_X][j +
              (int) GameWorld.PLAYFIELD_MAX_Y]],
              new Vector2(j, i));
          boxes.add(newBox);
        }
      }

    eagle = new Eagle(new Vector2(-1f * GameWorld.SPRITE_SIZE, GameWorld.PLAYFIELD_MIN_Y));
  }

  public void draw(SpriteBatch batch) {
    for (Box box : boxes)
      if (!box.getBoxType().equals(BoxType.GRASS))
        box.draw(batch);
    eagle.draw(batch);
  }

  public void update(float runTime) {
    for (Box box : boxes)
      if (box.getBoxType().equals(BoxType.WATER))
        box.getObject().setRegion((TextureRegion) box.waterAnimation.getKeyFrame(runTime));
    boxes.removeAll(removedBoxes);
    removedBoxes.clear();
  }

  public void interaction(Tank tank, boolean clock_stop) {
    for (Box box : boxes) {
      if (tank.collide(box) || tank.collide(eagle)) {
        tank.setCanMove(false);
        if (tank.getClass().getSimpleName().equals("Enemy"))
          if (!clock_stop)
            ((Enemy) tank).randomMove();
      }
      if (tank.getClass().getSimpleName().equals("Player"))
        if (((Player) tank).collideIce(box))
          ((Player) tank).setOnIce(true);
    }

  }

  public void interaction(Bullet bullet, GameWorld world) {
    for (Box box : boxes) {
      if (bullet.collide(box)) {
        bullet.setCanMove(false);
        bullet.createExploison(world);
        if (box.getBoxType().equals(BoxType.STONE))
          if (bullet.getGunner().getClass().getSimpleName().equals("Player") &&
              (((Player) bullet.getGunner()).getTankLevel().equals(TankLevel.LEVEL4))) {
            GameWorld.sounds.get("bricks").play(0.3f);
            removedBoxes.add(box);
          } else GameWorld.sounds.get("stone").play(0.3f);
        if (box.getBoxType().equals(BoxType.BRICS)) {
          GameWorld.sounds.get("bricks").play(0.2f);
          if (box.changeBox(bullet.getDirection()))
            removedBoxes.add(box);
        }
      }
    }
    if (bullet.collide(eagle)) {
      bullet.setCanMove(false);
      eagle.gameover(world);
      world.createWindowPause();
    }
  }


  public void createShovel(float runTime) {

    shovelArmor = true;
    shovelTime = runTime;
    removedBoxes.addAll(boxes.parallelStream().filter(box -> eagle.getPositionAround().
        contains(box.position)).collect(Collectors.toList()));
    for (Vector2 position : eagle.getPositionAround())
      boxes.add(new Box(BoxType.STONE, position));
  }

  public void blinkShovel(float runTime) {
    for (Box box : boxes)
      if (eagle.getPositionAround().contains(box.position)) {
        float timer = (float) (Math.floor(runTime * 100) % 100);
        if (box.getBoxType().equals(BoxType.STONE)) {
          if (timer <= 50) box.getObject().setRegion(MainGame.sptriteAtlas.findRegion("brics"));
          else box.getObject().setRegion(MainGame.sptriteAtlas.findRegion("stone"));
        }
      }
  }

  public void destroyShovel() {
    boxes.stream().filter(box -> eagle.getPositionAround().
        contains(box.position)).
        forEach(box -> {
          box.getObject().setRegion(MainGame.sptriteAtlas.findRegion("brics"));
          box.setBoxType(BoxType.BRICS);
        });

    shovelArmor = false;
  }

  public List<Box> getBoxes() {
    return boxes;
  }

  public float getShovelTime() {
    return shovelTime;
  }

  public boolean isShovelArmor() {
    return shovelArmor;
  }

}
