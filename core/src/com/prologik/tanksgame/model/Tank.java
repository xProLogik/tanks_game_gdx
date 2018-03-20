package com.prologik.tanksgame.model;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Tank extends MovableObject {

  Array<Bullet> bullets = new Array<>();
  Array<Bullet> removedBullets = new Array<>();

  Tank(String nameRegion, Vector2 position, float width, float height, Vector2 direction, float velocity) {
    super(nameRegion, position, width, height, direction, velocity);
  }


  @Override
  public void update(float delta) {
    super.update(delta);

  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
    for (Bullet bullet : bullets) bullet.draw(batch);
  }


  public void shot() {
    Vector2 direction = new Vector2(this.direction);
    if (bullets.size<10) {
      Bullet newBullet = new Bullet("bullet_top", new Vector2(this.getBounds().getX() + GameWorld.SPRITE_SIZE/2,
          this.getBounds().getY() + GameWorld.SPRITE_SIZE/2), GameWorld.SPRITE_SIZE, GameWorld.SPRITE_SIZE, direction);
      bullets.add(newBullet);
    }
  }
}
