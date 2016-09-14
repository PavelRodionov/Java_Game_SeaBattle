package com.mygdx.game;

public class FieldAI extends Field{


    FieldAI(int x, int y){
        super(x,y);
        ships = new Ships(this,false);
    }//constructor

    @Override
    public TurnState strike(int mouseX, int mouseY, boolean touch){
        selCellX = (mouseX-fieldX)/ CONST.CELL_SIZE;
        selCellY = (mouseY-fieldY)/ CONST.CELL_SIZE;

        if( (mouseX<fieldX) || (mouseY<fieldY)
                || (mouseX>=fieldX+ CONST.FIELD_SIZE_PIXELS)
                || (mouseY>=fieldY+ CONST.FIELD_SIZE_PIXELS) ){
            selCellX = -1;
            selCellY = -1;
        }//if()

        if((touch && selCellX>-1 && selCellY>-1)){

            if(field[selCellY][selCellX] == CellState.empty) {
                field[selCellY][selCellX] = CellState.miss;
                return TurnState.miss;
            }//if()

        }//if()
        return TurnState.wait;

    }//strike()


}//class Field


