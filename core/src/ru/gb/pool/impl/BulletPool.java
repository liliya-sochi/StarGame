package ru.gb.pool.impl;

import ru.gb.pool.SpritesPool;
import ru.gb.sprite.impl.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
