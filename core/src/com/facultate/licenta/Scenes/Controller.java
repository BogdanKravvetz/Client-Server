package com.facultate.licenta.Scenes;

import com.badlogic.gdx.Gdx;
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

public class Controller {
    private Viewport viewport;
    private Stage stage;

    private boolean upPressed,downPressed,leftPressed,rightPressed;
    private OrthographicCamera controllerCamera;
    private int buttonSize ;

    public Controller(Game game)
    {
        buttonSize = 100 ;
        //camera pentru hud(controller)
        controllerCamera = new OrthographicCamera();
        //hud-ul are nevoie de propriul viewport
        viewport = new FitViewport(Constants.WIDTH,Constants.HEIGHT,controllerCamera);
        //setup stage, stage este si input listener.
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        //table e ca o masa intr=o camera(stage) pe care asezi etichete(lable)

        //creaza o imagine cu textura butonului
        Image up = new Image(new Texture("up.png"));
        //scaleaza butonul
        up.setSize(buttonSize,buttonSize);
        //input events
        up.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Image down = new Image(new Texture("down.png"));
        down.setSize(buttonSize,buttonSize);
        down.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Image left = new Image(new Texture("left.png"));
        left.setSize(buttonSize,buttonSize);
        left.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Image right = new Image(new Texture("right.png"));
        right.setSize(buttonSize,buttonSize);
        right.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true; //returneaza true pentru a intra in touch up
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Table table = new Table();
        table.left().bottom();//aliniaza elementele la stanga jos
        //table este inpartit intr-un grid 3x3 (gen crafting table)
        table.add();
        table.add(up).size(up.getWidth(),up.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(left).size(left.getWidth(),left.getHeight());
        table.add();
        table.add(right).size(right.getWidth(),right.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(down).size(down.getWidth(),down.getHeight());
        table.add();
        //PUNE MASA IN CAMERA
        stage.addActor(table);
    }
    //deseneaza lol xD
    public void draw()
    {
        stage.draw();
    }
    public void resize(int width,int height)
    {
        viewport.update(width,height);
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}
