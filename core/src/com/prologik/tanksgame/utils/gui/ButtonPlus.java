package com.prologik.tanksgame.utils.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;

public class ButtonPlus extends Button {

  public ButtonPlus(Vector2 position, float width, MainGame game) {
    super(position, width, game);

  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
    drawVerticle();
  }

  private void drawVerticle() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.rect(this.position.x + (width - height) / 2, position.y - (width - height) / 2, height, width);
    mainGame.shapeRenderer.end();
  }

  @Override
  public boolean isOverButton(Vector2 position) {
    return super.isOverButton(position) || (position.x >= this.position.x + (width - height) / 2 &&
        position.y >= this.position.y - (width - height) / 2 &&
        position.x <= this.position.x + (width - height) / 2 + this.height &&
        position.y <= this.position.y - (width - height) / 2 + this.width);
  }
}
