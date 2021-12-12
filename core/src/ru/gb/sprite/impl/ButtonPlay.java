package ru.gb.sprite.impl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.math.Rect;
import ru.gb.screen.impl.GameScreen;
import ru.gb.sprite.BaseButton;

public class ButtonPlay extends BaseButton {
    private static final float HEIGHT = 0.25f;
    private static final float MARGIN = 0.03f;
    private final Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setRight(worldBounds.getLeft() + MARGIN * 8);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
