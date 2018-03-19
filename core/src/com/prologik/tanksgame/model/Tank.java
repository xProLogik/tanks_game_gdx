package com.prologik.tanksgame.model;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tank extends MovableObject {

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
    Vector2 direction = new Vector2(this.getDirection());
    if (bullets.size<10) {
      Bullet newBullet = new Bullet("bullet_top", new Vector2(this.getBounds().getX() + 0.5f,
          this.getBounds().getY() + 0.5f), 1f, 1f, direction);
      bullets.add(newBullet);
    }
  }
}
