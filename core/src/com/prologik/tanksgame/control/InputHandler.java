package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;

public class InputHandler {
  private MainGame mainGame;
  private float aspectRatio;

  public InputHandler(MainGame game) {
    this.mainGame = game;
  }


  public boolean isPressed() {
    return Gdx.input.isTouched();
  }

  public boolean isClicked() {
    return Gdx.input.justTouched();
  }

  public Vector2 mousePosition() {
    return new Vector2((Gdx.input.getX() - Gdx.graphics.getWidth() / 2) / aspectRatio,
        (Gdx.graphics.getHeight() / 2 - Gdx.input.getY()) / aspectRatio);
  }


  public void update(float delta) {
    if (mainGame.viewport.getLeftGutterWidth() > 0)
      aspectRatio = Gdx.graphics.getHeight() / mainGame.viewport.getWorldHeight();
    else aspectRatio = Gdx.graphics.getWidth() / mainGame.viewport.getWorldWidth();
  }
}
