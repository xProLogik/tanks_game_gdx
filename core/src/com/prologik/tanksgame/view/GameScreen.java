package com.prologik.tanksgame.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.gameworld.WorldController;
import com.prologik.tanksgame.model.gameworld.GameWorld;


public class GameScreen extends AbstractScreen {
  private static GameWorld world;
  private final WorldController controller;
  private float runTime = 0;


  public GameScreen(MainGame game, int countPlayer, String nameLevel) {
    super(game);
    world = new GameWorld(game, countPlayer, nameLevel);
    controller = new WorldController(world);
  }


  @Override
  public void dispose() {
    world.dispose();
  }

  @Override
  public void update(float delta) {
    runTime += delta;
    world.update(delta, runTime);
  }

  @Override
  public void draw(SpriteBatch batch) {
    controller.draw(batch);
  }
}
