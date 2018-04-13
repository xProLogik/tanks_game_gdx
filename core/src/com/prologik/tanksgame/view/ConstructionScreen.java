package com.prologik.tanksgame.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.construction.ConstructionController;
import com.prologik.tanksgame.model.constuction.ConstructionWorld;

public class ConstructionScreen extends AbstractScreen {

  private final ConstructionWorld world;
  private final ConstructionController controller;

  public ConstructionScreen(MainGame game) {
    super(game);
    world = new ConstructionWorld(mainGame);
    controller = new ConstructionController(world);
  }

  @Override
  public void draw(SpriteBatch batch) {
    controller.draw(batch);
  }


  @Override
  public void update(float delta) {
    world.update(delta);
  }

  @Override
  public void dispose() {
    super.dispose();
    world.dispose();
  }
}
