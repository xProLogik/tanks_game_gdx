package com.prologik.tanksgame.utils.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;

public class Button {

  private Color colorText = Color.WHITE;
  private Color overButton = new Color(0.1f, 0.1f, 0.1f, 1);
  private Color outButton = new Color(0.2f, 0.2f, 0.2f, 1);
  private Color colorButton = outButton;
  MainGame mainGame;
  Vector2 position;
  float width;
  float height;
  private String textButton;

  public Button(Vector2 position, float width, MainGame game) {
    this.position = position;
    this.width = width;
    this.height = width / 2.5f;
    this.mainGame = game;

  }

  public Button(Vector2 position, float width, float height, String textButton, MainGame game) {
    this(position, width, game);
    this.textButton = textButton;
    this.height = height;
  }


  public void draw(SpriteBatch batch) {
    drawHorizontal();
    if (textButton != null) {
      batch.begin();
      mainGame.bitmapFont.setColor(colorText);
      mainGame.bitmapFont.draw(batch, textButton, position.x + height / 4, position.y + height * 0.75f);
      batch.end();
    }
  }

  private void drawHorizontal() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.setColor(colorButton);
    mainGame.shapeRenderer.rect(position.x, position.y, width, height);
    mainGame.shapeRenderer.end();
  }


  public boolean isOverButton(Vector2 position) {
    return position.x >= this.position.x && position.y >= this.position.y &&
        position.x <= this.position.x + this.width &&
        position.y <= this.position.y + this.height;
  }

  public void setOverButton(Color overButton) {
    this.overButton = overButton;
  }

  public void setOutButton(Color outButton) {
    this.outButton = outButton;
  }

  public void setColorButton(Color colorButton) {
    this.colorButton = colorButton;
  }

  public Color getOverButton() {
    return overButton;
  }

  public Color getOutButton() {
    return outButton;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }

  public void setColorText(Color colorText) {
    this.colorText = colorText;
  }

  public String getTextButton() {
    return textButton;
  }
}
