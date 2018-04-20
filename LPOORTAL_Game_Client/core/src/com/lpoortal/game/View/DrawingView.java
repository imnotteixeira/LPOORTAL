package com.lpoortal.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.lpoortal.game.LPOORTAL_Game;

public class DrawingView extends ScreenView{

    private ButtonView jumpBtn;

    public DrawingView(TextureManager textureManager){

        super(textureManager);

        createUI();
    }

    private void createUI(){
        portraitMode();

        Image logo = new Image(new TextureRegion(textureManager.getTexture(TextureManager.Object_Texture.LOGO)));
        logo.setSize(256, 144);
        logo.setPosition(192, 300);
        stage.addActor(logo);

        Image tutorial = new Image(new TextureRegion(textureManager.getTexture(TextureManager.Object_Texture.MOVEMENT_TUTORIAL)));
        tutorial.setSize(144, 256);
        tutorial.setPosition(248, 0);
        stage.addActor(tutorial);
    }

}
