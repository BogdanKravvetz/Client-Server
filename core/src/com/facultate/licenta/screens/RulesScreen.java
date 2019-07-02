package com.facultate.licenta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.facultate.licenta.Game;
import com.facultate.licenta.tools.Constants;

public class RulesScreen implements Screen {
    private Game myGame;

    private OrthographicCamera menuCamera;
    private Viewport menuPort;
    private Stage stage;
    private Texture background;

    public RulesScreen(Game game)
    {

        myGame = game;
        background = new Texture("bg3.png");
        menuCamera = new OrthographicCamera();
        menuPort = new FitViewport(Constants.WIDTH,Constants.HEIGHT,menuCamera);
        stage = new Stage(menuPort,myGame.batch);
        Gdx.input.setInputProcessor(stage);

        Image back = new Image(new Texture("Back.png"));
        back.setSize(900,300);
        back.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                myGame.setScreen(new MenuScreen(myGame));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Image rules = new Image(new Texture("Rules.png"));
        rules.setSize(rules.getWidth(),rules.getHeight());
        Table table = new Table();
        table.setWidth(Constants.WIDTH);
        table.setHeight(Constants.HEIGHT);
        table.top().pad(20);
        table.add(rules).size(1100,600);
        table.row().pad(0,0,0,0);
        table.add(back).size(400,100);
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

    }
}
