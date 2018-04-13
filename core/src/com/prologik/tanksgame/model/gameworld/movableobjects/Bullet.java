package com.prologik.tanksgame.model.gameworld.movableobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Box;
import com.prologik.tanksgame.model.gameworld.anotherobjects.BoxType;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Exploison;
import com.prologik.tanksgame.model.gameworld.anotherobjects.GameObject;

public class Bullet extends MovableObject {


  private final Tank gunner;

  Bullet(Vector2 position, Vector2 direction, Tank tank) {
    super("bullet", position, GameWorld.SPRITE_SIZE,
        GameWorld.SPRITE_SIZE, direction, 15f);
    this.gunner = tank;
  }


  @Override
  public boolean isLeftTheField() {
    return (getCollisionRect().x < GameWorld.PLAYFIELD_MIN_X ||
        getCollisionRect().y < GameWorld.PLAYFIELD_MIN_Y ||
        getCollisionRect().x + getCollisionRect().width > GameWorld.PLAYFIELD_MAX_X ||
        getCollisionRect().y + getCollisionRect().height > GameWorld.PLAYFIELD_MAX_Y);
  }

  @Override
  void leftTheField() {
    super.leftTheField();

  }


  public void createExploison(GameWorld world) {
    Vector2 newPosition = new Vector2(this.position.x, this.position.y);
    switch (this.currentFacing) {
      case -90:
        newPosition.x = this.position.x;
        newPosition.y = this.position.y - 0.5f;
        break;
      case 90:
        newPosition.x = this.position.x - 1f;
        newPosition.y = this.position.y - 0.5f;
        break;
      case 0:
        newPosition.x = this.position.x - 0.5f;
        newPosition.y = this.position.y;
        break;
      case 180:
        newPosition.x = this.position.x - 0.5f;
        newPosition.y = this.position.y - 1f;
        break;
    }
    world.getExploisons().add(new Exploison(3, newPosition));
  }


  @Override
  public boolean collide(Box box) {
    return (box.getBoxType().equals(BoxType.BRICS) ||
        box.getBoxType().equals(BoxType.STONE)) && this.collide((GameObject) box);
  }


  @Override
  public void update(float delta) {
    super.update(delta);
    if (this.isCanMove()) this.move(delta);
  }


  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  public Tank getGunner() {
    return gunner;
  }
}
