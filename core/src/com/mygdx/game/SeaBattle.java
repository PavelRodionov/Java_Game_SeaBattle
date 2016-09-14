package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class SeaBattle extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont fnt;
	BitmapFont winFnt;
	Texture backGround;

	enum GameState {start, humanTurn, AITurn, auto}
	GameState gameState;

	enum Win {humanWin, AIWin, wait}
	Win win;

	TurnState turnState;

	Field fieldAI;
	Field fieldHuman;

	PlayerHuman playerHuman;
	PlayerAI playerAI;

	static Random rnd = new Random();

	@Override
	public void create () {
		batch = new SpriteBatch();

		backGround = new Texture("backGround.png");

		fnt = new BitmapFont(Gdx.files.internal("myfnt.fnt"), Gdx.files.internal("myfnt.png"),false);
		winFnt = new BitmapFont(Gdx.files.internal("win_font.fnt"), Gdx.files.internal("win_font.png"),false);


		fieldHuman = new FieldHuman(26,206);
		fieldAI = new FieldAI(416,206);

		playerHuman = new PlayerHuman(fieldAI);
		playerAI = new PlayerAI(fieldHuman);

		gameState = GameState.auto;
		win = Win.wait;

	}//create()


	@Override
	public void render () {
		controller();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

			batch.draw(backGround,0,0);
		    fnt.draw(batch,"МОРСКОЙ БОЙ", Gdx.graphics.getWidth()/2-120, Gdx.graphics.getHeight()-20);

			fieldHuman.draw(batch);
			fieldAI.draw(batch);

		    if(gameState == GameState.start) {
				winFnt.draw(batch,"АВТО",170,175);
				winFnt.draw(batch, "ИГРАТЬ", Gdx.graphics.getWidth() / 2 - 55, 115);
				winFnt.draw(batch, "ВЫХОД", Gdx.graphics.getWidth() / 2 - 55, 55);
			}//if()

		if(win == Win.humanWin){
			winFnt.draw(batch,"ПОБЕДА !",510,175);
		}//if()

		if(win == Win.AIWin){
			winFnt.draw(batch,"ПОРАЖЕНИЕ",510,175);
		}//if()

		batch.end();

	}//render()


public void controller(){

	switch(gameState){

		case auto:{
			fieldHuman.clear();
			fieldAI.clear();
			fieldHuman.getShips().start();
			fieldAI.getShips().start();
			fieldHuman.deployShips(true);
			fieldAI.deployShips(false);
			gameState = GameState.start;
			break;
		}//case

		case start:{

			if(playerHuman.start()){
				fieldHuman.getShips().start();
				fieldHuman.clearBattle();
				fieldAI.getShips().start();
				fieldAI.clear();
				fieldAI.deployShips(false);
				gameState = GameState.humanTurn;
				win = Win.wait;
			}//if()

			if(playerHuman.exit()){
				Gdx.app.exit();
			}//if()

			if(playerHuman.auto()){
				fieldHuman.getShips().start();
				fieldHuman.clear();
				fieldHuman.deployShips(true);
			}//if()

			break;
		}//case

		case humanTurn:{
			turnState = playerHuman.strike();
			switch(turnState){
				case miss:{
					gameState = GameState.AITurn;
					break;
				}//case
				case gameOver:{
					//вывести сообщение: ПОБЕДИЛ ИГРОК!
					win = Win.humanWin;
					gameState = GameState.start;
					break;
				}//case
			}//switch()
			break;
		}//case

		case AITurn:{
			turnState = playerAI.strike();
			switch(turnState){
				case miss:{
					gameState = GameState.humanTurn;
					break;
				}//case
				case gameOver:{
					//вывести сообщение: ПОБЕДИЛ КОМП!
					win = Win.AIWin;
					gameState = GameState.start;
					break;
				}//case
			}//switch()
			break;
		}//case

	}//switch()

}//controller()

}//class SeaBattle
