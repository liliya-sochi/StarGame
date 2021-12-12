package ru.gb.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.gb.font.Font;
import ru.gb.math.Rect;
import ru.gb.pool.impl.BulletPool;
import ru.gb.pool.impl.EnemyPool;
import ru.gb.pool.impl.ExplosionPool;
import ru.gb.screen.BaseScreen;
import ru.gb.sprite.impl.Background;
import ru.gb.sprite.impl.Bullet;
import ru.gb.sprite.impl.ButtonNewGame;
import ru.gb.sprite.impl.EnemyShip;
import ru.gb.sprite.impl.GameOver;
import ru.gb.sprite.impl.MainShip;
import ru.gb.sprite.impl.Star;
import ru.gb.sprite.impl.TrackingStar;
import ru.gb.util.EnemyEmitter;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;
    private static final float MARGIN = 0.01f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private Texture bg;
    private Background background;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;
    private ExplosionPool explosionPool;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private TextureAtlas atlas;
    private TrackingStar[] stars;
    private MainShip mainShip;
    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private EnemyEmitter enemyEmitter;
    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.02f);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        gameOver = new GameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas, this);
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(explosionPool, bulletPool, bulletSound, worldBounds);
        mainShip = new MainShip(atlas, explosionPool, bulletPool, laserSound);
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new TrackingStar(atlas, mainShip.getV());
        }
        enemyEmitter = new EnemyEmitter(atlas, worldBounds, enemyPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        frags = 0;
    }

    public void startNewGame() {
        frags = 0;
        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        explosionPool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        font.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (mainShip.isDestroyed()) {
            buttonNewGame.touchDown(touch, pointer, button);
        } else {
            mainShip.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (mainShip.isDestroyed()) {
            buttonNewGame.touchUp(touch, pointer, button);
        } else {
            mainShip.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.update(delta);
            bulletPool.updateActiveSprite(delta);
            enemyPool.updateActiveSprite(delta);
            enemyEmitter.generate(delta, frags);
        }
        explosionPool.updateActiveSprite(delta);
    }

    private void checkCollisions() {
        if (mainShip.isDestroyed()) {
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            if (enemyShip.isDestroyed()) {
                continue;
            }
            float minDist = (mainShip.getWidth() + enemyShip.getWidth()) * 0.5f;
            if (mainShip.pos.dst(enemyShip.pos) < minDist) {
                mainShip.damage(enemyShip.getHp() * 2);
                enemyShip.destroy();
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed()) {
                continue;
            }
            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyShip enemyShip : enemyShipList) {
                if (enemyShip.isDestroyed()) {
                    continue;
                }
                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    if (enemyShip.isDestroyed()) {
                        frags++;
                    }
                    bullet.destroy();
                }
            }
        }
    }

    private void freeAllDestroyed() {
        explosionPool.freeAllDestroyed();
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprite(batch);
            enemyPool.drawActiveSprite(batch);
        } else {
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprite(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags),
                worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP). append(mainShip.getHp()),
                worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()),
                worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN, Align.right);
    }
}