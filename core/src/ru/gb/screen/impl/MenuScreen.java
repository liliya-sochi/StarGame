package ru.gb.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.gb.screen.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Texture fon;
    private Texture img;
    private Vector2 touch;
    float x, y;

    @Override
    public void show() {
        super.show();
        fon = new Texture("fon-space.jpg");
        img = new Texture("plane.png");
        touch = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(x < touch.x) x = x + touch.x / 100;
        if(y < touch.y) y = y + touch.y / 100;
        batch.begin();
        batch.draw(fon, 0, 0, 1920, 1080);
        batch.draw(img, x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        fon.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}