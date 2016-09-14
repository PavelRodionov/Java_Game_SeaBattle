package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


abstract public class Field {

    protected static Texture pictureField = new Texture("field.png");
    protected static Texture missCell = new Texture("point.png");

    protected int pictureFieldX;
    protected int pictureFieldY;
    protected int fieldX;
    protected int fieldY;

    protected int selCellX;
    protected int selCellY;

    protected Ships ships;

    protected CellState field[][] = new CellState[CONST.FIELD_SIZE][CONST.FIELD_SIZE];


    public Field(int x, int y){
        pictureFieldX = x;
        pictureFieldY = y;
        fieldX = x+33;
        fieldY = y+3;
    }//constructor

    public void clearBattle(){
        for (int i = 0; i < CONST.FIELD_SIZE ; i++) {
            for (int j = 0; j < CONST.FIELD_SIZE; j++) {
                if(field[i][j] == CellState.miss) {
                    field[i][j] = CellState.empty;
                }//if()
                if(field[i][j] == CellState.blast){
                    field[i][j] = CellState.ship;
                }//if()
            }//for()
        }//for()
    }//start()

    public void clear(){
        for (int i = 0; i < CONST.FIELD_SIZE ; i++) {
            for (int j = 0; j < CONST.FIELD_SIZE; j++) {

                    field[i][j] = CellState.empty;

            }//for()
        }//for()
    }//clear()

    public TurnState strike(int mouseX, int mouseY, boolean touch){return TurnState.wait;}

    public Ships getShips(){
        return ships;
    }//getShips()


    public void makeBorder(int mouseX, int mouseY, Direction direction, int l){
        int x = (mouseX-fieldX)/ CONST.CELL_SIZE;
        int y = (mouseY-fieldY)/ CONST.CELL_SIZE;

        if(direction == Direction.vertical){
            x-=1;
        }//if()

        int x1 = x;
        int y1 = y;

        int vx=1;
        int vy=0;
        switch(direction){
            case horizontal:
                vx = 1;
                vy = 0;
                break;
            case vertical:
                vx = 0;
                vy = 1;
                break;
        }//switch()

        int x2 = x + vx*(l-1);
        int y2 = y + vy*(l-1);

        for (int i = y1-1; i <= y2+1 ; i++) {
            for (int j = x1-1; j <= x2+1 ; j++) {
                if(i>-1 && j>-1 && i< CONST.FIELD_SIZE && j< CONST.FIELD_SIZE && field[i][j] == CellState.empty){
                    field[i][j] = CellState.miss;
                }//if()
            }//for()
        }//for()

    }//makeBorder()


    public void draw(SpriteBatch batch){

        batch.draw(pictureField, pictureFieldX, pictureFieldY);

        for (int i = 0; i < CONST.FIELD_SIZE; i++) {
            for (int j = 0; j < CONST.FIELD_SIZE; j++) {

                if(field[i][j] == CellState.miss){
                    batch.draw(missCell,fieldX+j* CONST.CELL_SIZE,fieldY+i* CONST.CELL_SIZE);
                }//if()

            }//for(j)
        }//for(i)
        ships.draw(batch);
    }//draw()



    public CellState getCellField(int i, int j){
        return field[i][j];
    }


    public void deployShips(boolean visible){
        int[] shipLength = {4,3,3,2,2,2,1,1,1,1};
        for (int i = 0; i <shipLength.length ; i++) {
            int vx;
            int vy;

            do{
                vx = SeaBattle.rnd.nextInt(2);
                vy = SeaBattle.rnd.nextInt(2);
            }while(Math.abs(vx)+Math.abs(vy)!=1);

            int x;
            int y;

            while(true){
                x = SeaBattle.rnd.nextInt(CONST.FIELD_SIZE);
                y = SeaBattle.rnd.nextInt(CONST.FIELD_SIZE);
                if(setOneShip( x,y,vx,vy,shipLength[i])){
                    break;
                }//if()
            }//while()

           ships.getShip(i).setCoords(fieldX+x*30-1,fieldY+y*30-1);
           ships.getShip(i).setPosition(vx,vy);
           ships.setVisibleShips(visible);
        }//for()
    }//putShips()

    public boolean setOneShip(int x, int y, int vx, int vy, int l){
        int x1 = x;
        int y1 = y;
        int x2 = x + vx*(l-1);
        int y2 = y + vy*(l-1);

        if(x1<0 || y1<0 || x2> CONST.FIELD_SIZE-1 || y2> CONST.FIELD_SIZE-1) return false;
        for (int i = y1-1; i <= y2+1 ; i++) {
            for (int j = x1-1; j <= x2+1 ; j++) {
                if(i>-1 && j>-1 && i< CONST.FIELD_SIZE && j< CONST.FIELD_SIZE && field[i][j] != CellState.empty) return false;
            }//for()
        }//for()

        for (int i = 0; i < l; i++) {
            field[y+vy*i][x+vx*i] = CellState.ship;
        }//for()

        return true;
    }//setOneShip()

}//class Field
