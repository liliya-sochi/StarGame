package ru.gb.screen.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.gb.math.Rect;
import ru.gb.screen.BaseScreen;
import ru.gb.sprite.Logo;
import ru.gb.sprite.impl.Background;

public class MenuScreen extends BaseScreen {
    private Texture img;
    private Texture bg;
    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        img = new Texture("my/plane.png");
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        logo = new Logo(img);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        bg.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        logo.touchDown(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        logo.update(delta);
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        batch.end();
    }
}