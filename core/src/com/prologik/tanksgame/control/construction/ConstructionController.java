package com.prologik.tanksgame.control.construction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.control.InputHandler;
import com.prologik.tanksgame.model.constuction.ConstructionWorld;
import com.prologik.tanksgame.utils.gui.Button;
import com.prologik.tanksgame.utils.gui.ButtonPlus;

import java.util.Map;

public class ConstructionController {
  private ConstructionWorld world;
  private InputHandler inputHandler;

  public ConstructionController(ConstructionWorld world) {
    this.world = world;
    inputHandler = new InputHandler(world.mainGame);
  }

  public void draw(SpriteBatch batch) {
    Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    world.draw(batch);

  }

  public void update(float delta) {

    if (!world.isStartSaveLevel()) {
      if (inputHandler.isClicked()) {
        world.selectCostructrBox(inputHandler.mousePosition());
        world.addOnField(inputHandler.mousePosition());
      }
      if (world.getSaveButton().isOverButton(inputHandler.mousePosition())) {
        world.getSaveButton().setColorButton(world.getSaveButton().getOverButton());
        if (inputHandler.isClicked()) world.startSaveLevel();
      } else world.getSaveButton().setColorButton(world.getSaveButton().getOutButton());

      if (world.getBackButton().isOverButton(inputHandler.mousePosition())) {
        world.getBackButton().setColorButton(world.getSaveButton().getOverButton());
        if (inputHandler.isClicked()) world.backScreen();
      } else world.getBackButton().setColorButton(world.getSaveButton().getOutButton());
    } else {
      for (Map.Entry<String, Button> entry : world.buttonsMinus.entrySet())
        if (entry.getValue().isOverButton(inputHandler.mousePosition())) {
          entry.getValue().setColorButton(world.getSaveButton().getOverButton());
          if (inputHandler.isClicked())
            world.minusCountEnemy(entry.getKey());
        } else entry.getValue().setColorButton(world.getSaveButton().getOutButton());

      for (Map.Entry<String, ButtonPlus> entry : world.buttonsPlus.entrySet())
        if (entry.getValue().isOverButton(inputHandler.mousePosition())) {
          entry.getValue().setColorButton(world.getSaveButton().getOverButton());
          if (inputHandler.isClicked())
            world.addCountEnemy(entry.getKey());
        } else entry.getValue().setColorButton(world.getSaveButton().getOutButton());

      if (world.getParseButton().isOverButton(inputHandler.mousePosition())) {
        world.getParseButton().setColorButton(world.getSaveButton().getOverButton());
        if (inputHandler.isClicked())
          world.inputFileName();
      } else world.getParseButton().setColorButton(world.getSaveButton().getOutButton());
    }

    inputHandler.update(delta);
  }


}
