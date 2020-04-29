package fi.tuni.atomics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

public class MenuButton extends Actor {
    private Texture texture;
    private boolean touched = false;

    MenuButton(float width, float height, float x, float y, Texture texture) {
        this.texture = texture;
        setWidth(width);
        setHeight(height);
        setBounds(x,
                y,
                getWidth(),
                getHeight());
        addListener(new InputListener() {
           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               touched = true;
               ParallelAction parallel = new ParallelAction();
               MenuButton.this.addAction(parallel);
               return true;
           }
        });
    }

    public boolean isTouched() {
        return this.touched;
    }

    public void setTouched(boolean x) {
        this.touched = x;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void dispose() {
        texture.dispose();
    }
}
