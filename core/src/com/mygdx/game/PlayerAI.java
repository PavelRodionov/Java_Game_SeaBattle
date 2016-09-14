package com.mygdx.game;

public class PlayerAI{
    private int x;
    private int y;
    private int hitX;
    private int hitY;
    private Field field;
    private Ships ships;

    TurnState state = TurnState.wait;

    private enum PlayerState{find,getDirection,destroy}
    private PlayerState playerState;

    private enum GetDir{right,up,left,down}
    private GetDir getDir;


    PlayerAI(Field field){
        this.field = field;
        ships = field.getShips();
        playerState = PlayerState.find;
        getDir = GetDir.right;
    }//constructor


    private void findShip(){
        while (true) {
            x = SeaBattle.rnd.nextInt(CONST.FIELD_SIZE);
            y = SeaBattle.rnd.nextInt(CONST.FIELD_SIZE);
            if ((field.getCellField(y, x) == CellState.empty) || (field.getCellField(y, x) == CellState.ship)) {
                break;
            }//if()
        }//while
        state = field.strike(x,y,true);
        if(state == TurnState.hit) {
            hitX = x;
            hitY = y;
            playerState = PlayerState.getDirection;
        }//if()
        getDir = GetDir.right;
    }//find()


    private void direction( GetDir nextDir){
        state = field.strike(x,y,true);
        if(state == TurnState.miss || state == TurnState.wait) {
            getDir = nextDir;
        }//if()
    }//direction()


    private void getDirection(){
        switch(getDir){

            case right:{
                if(x+1 <= CONST.FIELD_SIZE-1 && (field.getCellField(y, x) != CellState.miss) ) {
                    x += 1;
                }//if()
                direction(GetDir.up);
                break;
            }//case right

            case up:{
                if(y+1 <= CONST.FIELD_SIZE-1 && (field.getCellField(y, x) != CellState.miss) ) {
                    y += 1;
                }//if()
                direction(GetDir.left);
                break;
            }//case up

            case left:{
                if(x-1 >= 0 && (field.getCellField(y, x) != CellState.miss) ) {
                    x -= 1;
                }//if()
                direction(GetDir.down);
                break;
            }//case left

            case down:{
                if(y-1 >= 0 && (field.getCellField(y, x) != CellState.miss) ) {
                    y -= 1;
                }//if()
                direction(GetDir.right);
                break;
            }//case down

        }//switch()

        switch(state){

            case miss:
            case wait:{
                x = hitX;
                y = hitY;
                break;
            }//case miss wait

            case hit:{
                playerState = PlayerState.destroy;
                break;
            }//case hit

            case destroyed:{
                playerState = PlayerState.find;
                break;
            }//case destroyed

        }//switch()

    }//getDirection()
    
    
    private void destroyShip(){
        switch(getDir){

            case right:{
                if(x+1 <= CONST.FIELD_SIZE-1 && (field.getCellField(y, x) != CellState.miss)) {
                    x += 1;
                }//if()
                break;
            }//case right

            case up:{
                if(y+1 <= CONST.FIELD_SIZE-1 && (field.getCellField(y, x) != CellState.miss)) {
                    y += 1;
                }//if()
                break;
            }//case up

            case left:{
                if(x-1 >= 0 && (field.getCellField(y, x) != CellState.miss)) {
                    x -= 1;
                }//if()
                break;
            }//case left

            case down:{
                if(y-1 >= 0 && (field.getCellField(y, x) != CellState.miss)) {
                    y-=1;
                }//if()
                break;
            }//case down

        }//switch()

        state = field.strike(x,y,true);

        if(state == TurnState.miss || state == TurnState.wait){
            x = hitX;
            y = hitY;
            switch(getDir){
                case right:
                    getDir = GetDir.left;
                    break;
                case up:
                    getDir = GetDir.down;
                    break;
                case left:
                    getDir = GetDir.right;
                    break;
                case down:
                    getDir = GetDir.up;
                    break;
            }//switch()
        }//if()

        if(state == TurnState.destroyed){
            playerState = PlayerState.find;
            getDir = GetDir.right;
        }//if()
    }//destroy()
    
    
    public TurnState strike(){
        
        switch(playerState){

            case find:{
               findShip();
               break;
            }//case find

            case getDirection: {
                getDirection();
                break;
            }//case getDirection

            case destroy:{
                destroyShip();
                break;
            }//case destroy

        }//switch()
        
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
    
}//class PlayerAI
