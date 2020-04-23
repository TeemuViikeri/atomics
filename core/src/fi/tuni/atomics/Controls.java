package fi.tuni.atomics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static fi.tuni.atomics.PlayScreen.ROOM_HEIGHT_PIXELS;
import static fi.tuni.atomics.PlayScreen.ROOM_WIDTH_PIXELS;
import static fi.tuni.atomics.PlayScreen.scale;

class Controls {
    private static Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Button speedButton;
    static Button shootButton;

    void createButtons(final Player player) {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()), Atomics.HUDBatch);
        touchpad = new Touchpad(10, getTouchpadStyle());
        Table joystickTable = new Table();
        Table speedButtonTable = new Table();
        Table shootButtonTable = new Table();
        Table pauseButtonTable = new Table();
        joystickTable.setFillParent(true);
        speedButtonTable.setFillParent(true);
        shootButtonTable.setFillParent(true);

        joystickTable.add(touchpad).width(Gdx.graphics.getHeight() / 5.0f)
                .height(Gdx.graphics.getHeight() / 5.0f)
                .padLeft(-Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(Gdx.graphics.getHeight() / 3.5f)
                .padRight(Gdx.graphics.getWidth() / 3f)
                .fill();
        touchpadStyle.knob.setMinWidth(Gdx.graphics.getWidth() / 16.0f);
        touchpadStyle.knob.setMinHeight(Gdx.graphics.getWidth() / 16.0f);

        speedButton = new Button(getButtonStyle());
        speedButtonTable.add(speedButton).width(Gdx.graphics.getHeight() / 5.0f)
                .height(Gdx.graphics.getHeight() / 5.0f)
                .padLeft(+Gdx.graphics.getWidth() / 3f)
                .padBottom(-Gdx.graphics.getHeight() / 3.5f)
                .padTop(+Gdx.graphics.getHeight() / 3.5f)
                .padRight(-Gdx.graphics.getWidth() / 3f)
                .fill();

        shootButton = new Button(getShootButtonStyle());
        shootButtonTable.add(shootButton).width((float) Gdx.graphics.getHeight() / 5.0f)
                .height((float) Gdx.graphics.getHeight() / 5.0f)
                .padLeft((float) Gdx.graphics.getWidth() / 3f)
                .padBottom((float) -Gdx.graphics.getHeight() / 3.5f)
                .padTop((float) Gdx.graphics.getHeight() / 3.5f)
                .padRight((float) -Gdx.graphics.getWidth() / 10f)
                .fill();

        stage.addActor(joystickTable);
        stage.addActor(speedButtonTable);
        stage.addActor(shootButtonTable);

        Gdx.input.setInputProcessor(stage);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setDeltaX(((Touchpad) actor).getKnobPercentX());
                player.setDeltaY(((Touchpad) actor).getKnobPercentY());
            }
        });

    }

    private Button.ButtonStyle getButtonStyle() {
        Skin buttonSkin = new Skin();

        Button.ButtonStyle speedButtonStyle = new Button.ButtonStyle();

        buttonSkin.add("down", new Texture("down.png"));
        buttonSkin.add("up", new Texture("up.png"));

        Drawable up = buttonSkin.getDrawable("up");
        Drawable down = buttonSkin.getDrawable("down");

        speedButtonStyle.up = up;
        speedButtonStyle.down = down;

        return speedButtonStyle;
    }

    private Button.ButtonStyle getShootButtonStyle() {
        Skin buttonSkin = new Skin();

        Button.ButtonStyle speedButtonStyle = new Button.ButtonStyle();

        buttonSkin.add("down", new Texture("shootdown.png"));
        buttonSkin.add("up", new Texture("shootup.png"));

        Drawable up = buttonSkin.getDrawable("up");
        Drawable down = buttonSkin.getDrawable("down");

        speedButtonStyle.up = up;
        speedButtonStyle.down = down;

        return speedButtonStyle;
    }

    private Touchpad.TouchpadStyle getTouchpadStyle() {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchpadbg.png"));
        touchpadSkin.add("touchKnob", new Texture("touchpadknob.png"));

        touchpadStyle = new Touchpad.TouchpadStyle();

        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        return touchpadStyle;
    }

    static Stage getStage() {
        return stage;
    }

    Touchpad getTouchpad() {
        return touchpad;
    }

    Button getSpeedButton() {
        return speedButton;
    }

    Button getShootButton() {
        return shootButton;
    }
}
