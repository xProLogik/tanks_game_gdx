package com.prologik.tanksgame.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.mainmenu.MainMenuController;
import com.prologik.tanksgame.model.mainmenu.MainMenu;


public class MainMenuScreen extends AbstractScreen {

  private final MainMenu menu;
  private final MainMenuController controller;


  public MainMenuScreen(final MainGame game) {
    super(game);
    menu = new MainMenu(mainGame);
    controller = new MainMenuController(menu);
  }


  @Override
  public void draw(SpriteBatch batch) {
    controller.draw(batch);

  }

  @Override
  public void update(float delta) {
    menu.update(delta);
  }


  @Override
  public void dispose() {
    super.dispose();
    menu.dispose();
  }

}
