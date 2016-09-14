package com.mygdx.game;

public class PlayerHuman{
    private int x;
    private int y;
    private boolean touch;
    private Field field;
    private Ships ships;

    PlayerHuman(Field field){
        this.field = field;
        ships = field.getShips();
    }//constructor

    private void getMouse(){
       x = InputHandler.getMouseX();
       y = InputHandler.getMouseY();
       touch = InputHandler.turn();
    }//getMouse()


    public TurnState strike(){
        TurnState state;

        getMouse();

        if(field.strike(x,y,touch) == TurnState.miss){
            return TurnState.miss;
        }//if()

        state = ships.strike(x,y,touch);
        if(state != TurnState.wait){
            if(state == TurnState.destroyed){
                if( ships.blastAllShips() == TurnState.gameOver){
                    return TurnState.gameOver;
                }//if()
            }//if()
            return state;
        }//if()

        return TurnState.wait;
    }//strike()


    public boolean start(){
        getMouse();
        if(touch && x>330 && x<480 && y>90 && y<120){
                return true;
        }//if()
        return false;
    }//start()


    public boolean exit(){
        getMouse();
        if(touch && x>330 && x<480 && y>30 && y<60){
                return true;
        }//if()
        return false;
    }//exit()


    public boolean auto(){
        getMouse();
        if(touch && x>150 && x<270 && y>150 && y<180){
                return true;
        }//if()
        return false;
    }//auto()


}//class PlayerHuman
