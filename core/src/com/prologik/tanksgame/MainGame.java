package com.prologik.tanksgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.prologik.tanksgame.utils.Assets;
import com.prologik.tanksgame.view.GameScreen;

public class MainGame extends Game {

  private Assets assets;
  private Screen gameScreen;

  @Override
  public void create() {
    assets = new Assets();
    gameScreen = new GameScreen();
    setScreen(gameScreen);
  }

  @Override
  public void dispose() {
    super.dispose();
    gameScreen.dispose();
    assets.dispose();
  }

}
