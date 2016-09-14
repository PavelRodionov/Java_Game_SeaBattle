package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Ships {
   private static Texture picShips = new Texture("ship_g.png");
   private static Texture blastCell = new Texture("blast.png");
   private Field field;

     class Ship{
       private int length;
       private Direction direction;
       private Texture pictureShip;
       private int x;
       private int y;
       private int selCellX;
       private int selCellY;
     private boolean visible;

        private int FIELD_SIZE_PIXELS;
        private CellState[] shipMass;

        private int start;

       public Ship(int length){
           this.length = length;
           shipMass = new CellState[length];

           direction = Direction.horizontal;
           pictureShip = picShips;
           FIELD_SIZE_PIXELS = length* CONST.CELL_SIZE;
       }//constructor

      public int getX() {
          return x;
      }

      public int getY() {
          return y;
      }

      public int getLength() {
          return length;
      }

      public Direction getDirection() {
          return direction;
      }


      public void start(){
          for (int i = 0; i < shipMass.length; i++) {
              shipMass[i] = CellState.ship;
          }//for()
      }//start()


       public void setCoords(int x, int y){
           this.x = x;
           this.y = y;
       }//setCoords()


       public void setPosition(int vx, int vy) {
           if (vx == 1) {
               direction = Direction.horizontal;
           }//if()
           if(vy == 1){
               x+= CONST.CELL_SIZE;
               direction = Direction.vertical;
           }//if()
       }//setPosition()


       public void rotate(){
           if(direction == Direction.horizontal){
               x+= CONST.CELL_SIZE;
               direction = Direction.vertical;
           }else{
               x-= CONST.CELL_SIZE;
               direction = Direction.horizontal;
           }//else
       }//rotate()


       public boolean blastShip(){
           for (int i = 0; i <shipMass.length ; i++) {
               if(shipMass[i] == CellState.ship){
                   return false;
               }//if()
           }//for()
           return true;
       }//BlastAllShip()


       public void draw(SpriteBatch batch){
           //int start=0;
           switch(length){
               case 1:
                   start = 0;
                   break;
               case 2:
                   start = 1;
                   break;
               case 3:
                   start = 3;
                   break;
               case 4:
                   start = 6;
                   break;
           }//switch()

           int px,py,xnew;
           for (int i = 0; i < shipMass.length; i++) {
               if(shipMass[i] == CellState.blast){
                   if(direction == Direction.horizontal){
                       px = 1;
                       py = 0;
                       xnew = x;
                   }else{
                       px = 0;
                       py = 1;
                       xnew = x- CONST.CELL_SIZE;
                   }
                   batch.draw(blastCell,xnew+i*px* CONST.CELL_SIZE,y+i*py* CONST.CELL_SIZE);
               }
           }

           int position=0;
           switch(direction){
               case horizontal:
                   position = 0;
                   break;
               case vertical:
                   position = 90;
                   break;
           }//switch()

           if(visible) {
               batch.draw(pictureShip, x, y+1,1,1,length*30+2,32,1,1,position,start*30+(length-1)*2,0,length*30+2,32,false,false);
           }//if()

       }//draw()



      public TurnState strike(int mouseX, int mouseY, boolean touch){
          selCellX = (mouseX-x)/ CONST.CELL_SIZE;
          selCellY = (mouseY-y)/ CONST.CELL_SIZE;
          switch(direction){
              case horizontal:{
                  if( (mouseX<x) || (mouseX>=x+FIELD_SIZE_PIXELS) ||
                          (mouseY<y) || (mouseY>=y+ CONST.CELL_SIZE) ){
                      selCellX = -1;
                      selCellY = -1;
                  }
                  break;
              }
              case vertical:{
                  if( (mouseX<x- CONST.CELL_SIZE) || (mouseX>=x) ||
                          (mouseY<y) || (mouseY>=y+FIELD_SIZE_PIXELS) ){
                      selCellX = -1;
                      selCellY = -1;
                  }
                  break;
              }
          }

          if((touch && selCellX>-1 && selCellY>-1)){

              switch(direction){
                  case horizontal:{
                      if(shipMass[selCellX] == CellState.ship){
                          shipMass[selCellX] = CellState.blast;
                      }//if()
                      if(blastShip()){
                          field.makeBorder(x+ CONST.CELL_SIZE/2,y+ CONST.CELL_SIZE/2,direction,length);
                          visible = true;
                          return TurnState.destroyed;
                      }//if()
                      return TurnState.hit;

                  }//case
                  case vertical:{
                      if(shipMass[selCellY] == CellState.ship){
                          shipMass[selCellY] = CellState.blast;
                      }//if()
                      if(blastShip()){
                          field.makeBorder(x+ CONST.CELL_SIZE/2,y+ CONST.CELL_SIZE/2,direction,length);
                          visible = true;
                          return TurnState.destroyed;
                      }//if()
                      return TurnState.hit;

                  }//case
              }//switch()

          }//if()
          return TurnState.wait;
      }

   }//class Ship

    private Ship[] ships;

    Ships(Field field,boolean visible){
        ships = new Ship[]{new Ship(4), new Ship(3), new Ship(3),
                new Ship(2), new Ship(2), new Ship(2),
                new Ship(1), new Ship(1), new Ship(1), new Ship(1)};
        for (int i = 0; i < ships.length; i++) {
            ships[i].visible = visible;
        }//for()
        this.field = field;
    }//constructor


   public TurnState strike(int mouseX, int mouseY, boolean touch){
       TurnState state;
       for (int i = 0; i <ships.length ; i++) {
           state = ships[i].strike(mouseX, mouseY, touch);
           if(state != TurnState.wait){
               return state;
           }//if()
       }//for()
       return TurnState.wait;
   }//strike()

   public void draw(SpriteBatch batch){
       for (int i = 0; i < ships.length; i++) {
           if(ships[i].x != 0 && ships[i].y !=0)
           ships[i].draw(batch);
       }//for()
   }//draw()

   public TurnState blastAllShips(){
       for (int i = 0; i < ships.length; i++) {
           if(!ships[i].blastShip()) return TurnState.wait;
       }//for()
       return TurnState.gameOver;
   }//blastAllShips()

   public Ship getShip(int i){
       return ships[i];
   }

   public void start(){
       for (int i = 0; i < ships.length; i++) {
           ships[i].start();
       }//for()
   }//start()

   public void setVisibleShips(boolean visible){
       for (int i = 0; i < ships.length; i++) {
           ships[i].visible = visible;
       }
   }

}//class Ships
