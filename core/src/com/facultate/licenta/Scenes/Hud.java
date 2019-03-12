package com.facultate.licenta.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

import java.util.Locale;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label timeLabel;
    private Label inGameTimerLabel;

    public Hud(SpriteBatch spriteBatch, PlayScreen playScreen)
    {
        this.playScreen = playScreen;
        viewport = new FitViewport(Constants.WIDTH,Constants.HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label("Time",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        inGameTimerLabel = new Label(String.format(Locale.US,"%02d:%02d", (int)playScreen.getInGameTimer() / 60, (int)playScreen.getInGameTimer() % 60),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(inGameTimerLabel).expandX();
        timeLabel.setFontScale(3);
        inGameTimerLabel.setFontScale(3);
        stage.addActor(table);
    }
    public void update(float dt)
    {
        inGameTimerLabel.setText(String.format(Locale.US,"%02d:%02d", (int)playScreen.getInGameTimer() / 60, (int)playScreen.getInGameTimer() % 60));
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

}
