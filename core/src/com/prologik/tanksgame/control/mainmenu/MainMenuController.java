package com.prologik.tanksgame.control.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.control.InputHandler;
import com.prologik.tanksgame.model.mainmenu.MainMenu;
import com.prologik.tanksgame.utils.gui.Button;

import java.util.Map;

public class MainMenuController {

  private MainMenu menu;
  private InputHandler inputHandler;

  public MainMenuController(MainMenu menu) {
    this.menu = menu;
    inputHandler = new InputHandler(menu.mainGame);
  }

  public void draw(SpriteBatch batch) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    menu.draw(batch);
  }

  public void update(float delta) {
    inputHandler.update(delta);
    int direction = 0;
    if (!menu.isSelectedLevel()) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        direction = 1;
      if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        direction = -1;
      if (direction != 0) menu.changeCursorPosition(direction, -5, -11);

      if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        for (Button button : menu.getButtons())
          if (menu.getCursor().getY() == button.getPosition().y)
            menu.selectMenu(button);

      for (Button button : menu.getButtons())
        if (button.isOverButton(inputHandler.mousePosition())) {
          menu.changeColorButton(button);
          if (inputHandler.isClicked())
            menu.selectMenu(button);
        }
    } else {
      if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        direction = 1;
      if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        direction = -1;
      if (direction != 0) menu.changeCursorPosition(direction,
          menu.getLevelButton().values().stream()
              .max((o1, o2) -> Float.compare(o1.getPosition().y, o2.getPosition().y)).get().getPosition().y,
          menu.getLevelButton().values().stream()
              .min((o1, o2) -> Float.compare(o1.getPosition().y, o2.getPosition().y)).get().getPosition().y
      );

      for (Map.Entry<String, Button> entry : menu.getLevelButton().entrySet())
        if (entry.getValue().isOverButton(new Vector2(inputHandler.mousePosition().x,
            inputHandler.mousePosition().y + menu.mainGame.camera.position.y))) {
          menu.changeColorButton(entry.getValue());
          if (inputHandler.isClicked())
            menu.startGame(entry.getKey());
        }

      if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        for (Map.Entry<String, Button> entry : menu.getLevelButton().entrySet())
          if (menu.getCursor().getY() == entry.getValue().getPosition().y)
            menu.startGame(entry.getKey());
    }
    if (menu.isScreenScroll() && inputHandler.isPressed()) {
      menu.changeCameraPosition(inputHandler.mousePosition(), delta);
    } else {
      menu.getMouseSavePosition().set(inputHandler.mousePosition());
    }
  }
}
