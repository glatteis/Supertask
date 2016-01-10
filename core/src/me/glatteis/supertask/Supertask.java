package me.glatteis.supertask;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import me.glatteis.supertask.handlers.SupertaskInputProcessor;
import me.glatteis.supertask.screens.GameScreen;

public class Supertask extends Game {

	private GameScreen gameScreen = new GameScreen(this);
    private SupertaskInputProcessor inputProcessor;

    public SupertaskInputProcessor getInputProcessor() { return inputProcessor; }
	
	@Override
	public void create () {
        inputProcessor = new SupertaskInputProcessor();
        this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameScreen.render(Gdx.graphics.getDeltaTime());
	}
}
