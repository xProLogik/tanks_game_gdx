package com.prologik.tanksgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.model.GameWorld;


public class GameScreen extends AbstractScreen {


  private final GameWorld world;
  private final WorldRenderer renderer;

  private boolean isDone = false;

  public GameScreen() {
    world = new GameWorld();
    renderer = new WorldRenderer();
  }

  @Override
  public void dispose() {
    renderer.dispose();
    world.dispose();
  }

  @Override
  public void update(float delta) {
    if (Gdx.input.isKeyPressed(Input.Keys.Q))
      isDone = true;

    Vector2 moveDirection = new Vector2();
    boolean moveXKeyPressed = false;
    boolean moveYKeyPressed = false;

    if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
      moveDirection.set(0.0f, 1.0f);
      moveYKeyPressed = !moveYKeyPressed;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      moveDirection.set(0.0f, -1.0f);
      moveYKeyPressed = !moveYKeyPressed;
    }

    if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
      moveDirection.set(-1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
    }
    if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
      moveDirection.set(1.0f, 0.0f);
      moveXKeyPressed = !moveXKeyPressed;
    }

    if (moveXKeyPressed || moveYKeyPressed){
      world.moveTank(delta, moveDirection);
    }


    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      world.shot();
    }

    world.update(delta);
  }

  @Override
  public void draw(float delta) {
    renderer.render(world, delta);
  }

  @Override
  public boolean isDone() {
    return isDone;
  }
}
