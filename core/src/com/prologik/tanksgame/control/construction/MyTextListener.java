package com.prologik.tanksgame.control.construction;

import com.badlogic.gdx.Input;

public class MyTextListener implements Input.TextInputListener {
  private String newText;

  @Override
  public void input(String text) {
    newText = text;
  }

  @Override
  public void canceled() {
  }

  public String getText() {
    return newText;
  }

  public void setTextNull() {
    this.newText = null;
  }
}
