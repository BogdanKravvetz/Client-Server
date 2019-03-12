package com.facultate.licenta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.tools.Constants;

public class MenuScreen implements Screen {

    private Game myGame;

    private int buttonWidth = 900;
    private int buttonHeight = 300;

    private OrthographicCamera menuCamera;
    private Viewport menuPort;
    private Stage stage;
    private Texture background;
    public MenuScreen(Game game)
    {
        myGame = game;
        background = new Texture("bg.png");
        //camera pentru hud(controller)
        menuCamera = new OrthographicCamera();
        //hud-ul are nevoie de propriul viewport
        menuPort = new FitViewport(Constants.WIDTH,Constants.HEIGHT,menuCamera);
        //setup stage, stage este si input listener.
        stage = new Stage(menuPort,myGame.batch);
        Gdx.input.setInputProcessor(stage);
        //table e ca o masa intr=o camera(stage) pe care asezi etichete(lable)

        menuCamera.position.set(Constants.WIDTH/2f, Constants.HEIGHT/2f, 0);
        //creaza o imagine cu textura butonului
        Image play = new Image(new Texture("Play.png"));
        //scaleaza butonul
        play.setSize(buttonWidth,buttonHeight);
        //input events
        play.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                myGame.setScreen(new LobbyScreen(myGame));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Image exit = new Image(new Texture("Exit.png"));
        //scaleaza butonul
        exit.setSize(buttonWidth,buttonHeight);
        //input events
        exit.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Table table = new Table();
        table.setWidth(Constants.WIDTH);
        table.setHeight(Constants.HEIGHT);
        table.center();//aliniaza
        table.add(play).size(play.getWidth(),play.getHeight());
        table.row().pad(5,5,5,5);
        table.add(exit).size(exit.getWidth(),exit.getHeight());
        table.row().pad(5,5,5,5);
        stage.addActor(table);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        myGame.batch.begin();
        myGame.batch.draw(background,0,0,Constants.WIDTH,Constants.HEIGHT);
        myGame.batch.end();
        stage.draw();
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        menuPort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
