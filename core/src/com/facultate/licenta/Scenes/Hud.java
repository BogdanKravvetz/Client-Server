package com.facultate.licenta.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.screens.MenuScreen;
import com.facultate.licenta.screens.PlayScreen;
import com.facultate.licenta.tools.Constants;

import java.util.Locale;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private PlayScreen thisPlayScreen;

    private Label timeLabel;
    private Label inGameTimerLabel;

    private Label healthLabel;

    private Label speedLabel;
    private Label endGameLabel;
    private Label fireRateLabel;

    private Label winnerLabel;
    private Image menu;

    public Hud(SpriteBatch spriteBatch, PlayScreen playScreen)
    {
        thisPlayScreen = playScreen;
        viewport = new FitViewport(Constants.WIDTH,Constants.HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,spriteBatch);
        menu = new Image(new Texture("Meniu.png"));
        //scaleaza butonul
        menu.setSize(menu.getImageWidth(),menu.getImageHeight());
        //input events
        menu.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                thisPlayScreen.getConnectionHandler().getSocket().disconnect();
                thisPlayScreen.getMyGame().setScreen(new MenuScreen(thisPlayScreen.getMyGame()));
                super.touchUp(event, x, y, pointer, button);
            }
        });

        healthLabel = new Label("Viata: " + (int)playScreen.getPlayer().getPlayerStats().getCurrentHp()+"/" + (int)playScreen.getPlayer().getPlayerStats().getMaxHp()+"      ",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        speedLabel = new Label("Scor: " + playScreen.getSocketEvents().getScore()+"              ",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        endGameLabel = new Label("Jocul s-a terminat!",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        endGameLabel.setFontScale(2);
        timeLabel = new Label("Timp",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        inGameTimerLabel = new Label(String.format(Locale.US,"%02d:%02d", (int)playScreen.getInGameTimer() / 60, (int)playScreen.getInGameTimer() % 60),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        winnerLabel =  new Label("Castigator: " + playScreen.getSocketEvents().getWinnerName(),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        winnerLabel.setFontScale(2);
        Table table = new Table();
        table.setFillParent(true);
        table.setWidth(Constants.WIDTH);
        table.setHeight(Constants.HEIGHT);
        table.left().top();
        table.padLeft(20);
        table.add(healthLabel).padTop(10);
        table.add(timeLabel).padTop(10).expandX();
        //table.add();
        table.row();
        table.add(speedLabel);
        table.add(inGameTimerLabel);
        //table.add();
        table.row().padTop(150);
        table.add();
        table.add(endGameLabel);
        table.row();
        table.add();
        table.add(winnerLabel);
        table.row();
        table.add();
        table.add(menu).size(100,50);
        //table.add();
        menu.setVisible(false);
        endGameLabel.setVisible(false);
        winnerLabel.setVisible(false);
        timeLabel.setFontScale(2);
        inGameTimerLabel.setFontScale(2);
        healthLabel.setFontScale(2);
        speedLabel.setFontScale(2);
        stage.addActor(table);
    }
    public void update(float dt)
    {
        inGameTimerLabel.setText(String.format(Locale.US,"%02d:%02d", (int)thisPlayScreen.getInGameTimer() / 60, (int)thisPlayScreen.getInGameTimer() % 60));
        healthLabel.setText("Viata: " + (int)thisPlayScreen.getPlayer().getPlayerStats().getCurrentHp()+"/" + (int)thisPlayScreen.getPlayer().getPlayerStats().getMaxHp()+"      ");
        speedLabel.setText("Scor: " + thisPlayScreen.getSocketEvents().getScore()+"              ");
        winnerLabel.setText("Castigator: " + thisPlayScreen.getSocketEvents().getWinnerName());
        if (thisPlayScreen.getSocketEvents().getIsGameOver())
        {
            Gdx.input.setInputProcessor(stage);
            menu.setVisible(true);
            endGameLabel.setVisible(true);
            winnerLabel.setVisible(true);
        }
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

}
