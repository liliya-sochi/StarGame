package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BaseScreen implements Screen, InputProcessor {
    protected SpriteBatch batch;

    @Override
    public void show() {
        System.out.println("[LOG]: Show screen!");
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BROWN);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("[LOG]: Resize width = " + width + " height = " + height + ".");
    }

    @Override
    public void pause() {
        System.out.println("[LOG]: Pause screen!");
    }

    @Override
    public void resume() {
        System.out.println("[LOG]: Resume screen!");
    }

    @Override
    public void hide() {
        System.out.println("[LOG]: Hide screen!");
        dispose();
    }

    @Override
    public void dispose() {
        System.out.println("[LOG]: Dispose screen!");
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("[LOG]: ey down keycode = " + keycode + ".");
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("[LOG]: Key up keycode = " + keycode + ".");
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("[LOG]: Key typed character = " + character + ".");
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("[LOG]: Touch down screenX = " + screenX + ", screenY = " + screenY + ".");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("[LOG]: Touch up screenX = " + screenX + ", screenY = " + screenY + ".");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("[LOG]: Touch dragged screenX = " + screenX + ", screenY = " + screenY + ".");
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("[LOG]: Scrolled amountX = " + amountX + ", amountY = " + amountY + ".");
        return false;
    }
}