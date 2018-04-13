package com.prologik.tanksgame.control.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.control.InputHandler;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.view.MainMenuScreen;

public class WorldController {
  private InputHandler inputHandler;
  private GameWorld world;

  public WorldController(GameWorld world) {
    this.world = world;
    inputHandler = new InputHandler(world.mainGame);
  }

  public void update(float delta) {
    if (!world.isGameover() && !world.isEndLevel())
      if (Gdx.input.isKeyJustPressed(Input.Keys.P))
        if (world.isPause())
          world.unPause();
        else world.pause();

    if (!world.isGameover() && !world.isEndLevel())
      if (world.getPauseButton().isOverButton(inputHandler.mousePosition())) {
        world.getPauseButton().setColorButton(world.getPauseButton().getOverButton());
        if (inputHandler.isClicked())
          if (!world.isPause())
            world.pause();
          else world.unPause();
      } else world.getPauseButton().setColorButton(world.getPauseButton().getOutButton());

    if (world.getMenuButton().isOverButton(inputHandler.mousePosition())) {
      world.getMenuButton().setColorText(Color.YELLOW);
      if (inputHandler.isClicked())
        world.exitInMenu();
    } else world.getMenuButton().setColorText(Color.WHITE);

    if (world.isEndLevel())
      if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
          Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        if (!world.isGameover()) world.nextLevel();
        else world.exitInMenu();

    if (world.isGameover())
      if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
          Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        world.setEndLevel(true);
    inputHandler.update(delta);
  }

  public void draw(SpriteBatch batch) {
    Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    world.draw(batch);
  }
}
