package fi.tuni.atomics;

import com.badlogic.gdx.math.Vector2;

class RareItem extends Item {
    RareItem(Vector2 spawnPoint, int itemInt) {
        this.spawnPoint = spawnPoint;
        getRandomTexture(itemInt);
        width = texture.getWidth() / 1000f;
        height = texture.getHeight() / 1000f;
        targetWidth = width * 10;
        targetHeight = height * 10;
    }

    public void dispose() {
        texture.dispose();
        super.dispose();
    }
}
