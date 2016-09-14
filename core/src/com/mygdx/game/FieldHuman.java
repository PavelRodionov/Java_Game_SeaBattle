package com.mygdx.game;

public class FieldHuman extends Field {

    FieldHuman(int x, int y){
        super(x,y);
        ships = new Ships(this,true);
    }//constructor


   @Override
    public TurnState strike(int mouseX, int mouseY, boolean touch){
        TurnState state;
        if(field[mouseY][mouseX] == CellState.empty) {
            field[mouseY][mouseX] = CellState.miss;
            return TurnState.miss;
        }//if()

        if(field[mouseY][mouseX] == CellState.ship){
            field[mouseY][mouseX] = CellState.blast;
            state = ships.strike(mouseX* CONST.CELL_SIZE+fieldX,mouseY* CONST.CELL_SIZE+fieldY,true);

            if(state != TurnState.wait){
                return state;
            }//if()
        }//if()

        return TurnState.wait;
    }//strike()

}
