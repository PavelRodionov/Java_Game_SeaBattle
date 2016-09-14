package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class InputHandler {
    public static int getMouseX(){return Gdx.input.getX();}
    public static int getMouseY(){return Gdx.graphics.getHeight() - Gdx.input.getY();}
    public static boolean turn(){return Gdx.input.justTouched();}
}
