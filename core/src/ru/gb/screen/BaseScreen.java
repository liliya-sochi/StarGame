package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.gb.math.MatrixUtils;
import ru.gb.math.Rect;

public class BaseScreen implements Screen, InputProcessor {
    protected SpriteBatch batch;
    private Rect screenBounds;
    private Rect worldBounds;
    private Rect glBounds;
    private Matrix4 worldToGl;
    private Matrix3 screenToWorld;
    private Vector2 touch;

    @Override
    public void show() {
        System.out.println("[LOG]: Show screen!");
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        screenBounds = new Rect();
        worldBounds = new Rect();
        glBounds = new Rect(0, 0, 1f, 1f);
        worldToGl = new Matrix4();
        screenToWorld = new Matrix3();
        touch = new Vector2();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BROWN);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("[LOG]: Resize width = " + width + " height = " + height + ".");
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);
        float aspect = width / (float) height;
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        resize(worldBounds);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds,worldBounds);
    }

    public void resize(Rect worldBounds) {
        System.out.println("[LOG]: WorldBounds width = " + worldBounds.getWidth() + " height = " + worldBounds.getHeight() + ".");

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
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer, button);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        System.out.println("[LOG]: Touch down touchX = " + touch.x + ", touchY = " + touch.y + ".");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("[LOG]: Touch up screenX = " + screenX + ", screenY = " + screenY + ".");
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer, button);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        System.out.println("[LOG]: Touch up touchX = " + touch.x + ", touchY = " + touch.y + ".");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("[LOG]: Touch dragged screenX = " + screenX + ", screenY = " + screenY + ".");
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        System.out.println("[LOG]: Touch dragged touchX = " + touch.x + ", touchY = " + touch.y + ".");
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