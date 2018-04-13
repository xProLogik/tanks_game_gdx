package com.prologik.tanksgame.model.constuction;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Box;
import com.prologik.tanksgame.model.gameworld.anotherobjects.BoxType;

import java.util.ArrayList;
import java.util.List;

public class ConstructorBox {

  final List<Box> constrBoxes = new ArrayList<>();
  Rectangle bound;
  final BoxType type;
  String state;
  Vector2 position;

  ConstructorBox(BoxType boxType, String state, Vector2 position) {
    if (state.equals("full")) {
      constrBoxes.add(new Box(boxType, position));
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y)));
      constrBoxes.add(new Box(boxType, new Vector2(position.x, position.y + 1)));
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y + 1)));
    }
    if (state.equals("left")) {
      constrBoxes.add(new Box(boxType, position));
      constrBoxes.add(new Box(boxType, new Vector2(position.x, position.y + 1)));
    }
    if (state.equals("right")) {
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y)));
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y + 1)));
    }
    if (state.equals("top")) {
      constrBoxes.add(new Box(boxType, new Vector2(position.x, position.y + 1)));
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y + 1)));
    }
    if (state.equals("bottom")) {
      constrBoxes.add(new Box(boxType, position));
      constrBoxes.add(new Box(boxType, new Vector2(position.x + 1, position.y)));
    }
    bound = new Rectangle(position.x, position.y, 2, 2);
    this.type = boxType;
    this.state = state;
    this.position = position;
  }

  public void setPosition(Vector2 newPosition) {
    Vector2 subVector = new Vector2(position.x - newPosition.x, position.y - newPosition.y);
    constrBoxes.forEach(box -> box.getBounds().setPosition(box.getBounds().getX() - subVector.x,
        box.getBounds().getY() - subVector.y));
    this.position = newPosition;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void draw(SpriteBatch batch) {
    constrBoxes.forEach(box -> box.draw(batch));
  }

}
