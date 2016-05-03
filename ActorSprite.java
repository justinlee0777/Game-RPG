//package Game;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ActorSprite extends Sprite{
  protected Rectangle down, right, up, left;
  protected Rectangle sleep;
  protected boolean sleeping = false;
   public ActorSprite(){
  }
  
  public ActorSprite(int destx, int desty, int wid, int hei, Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
    super(destx, desty, wid, hei);
    setDirections(d, r, u, l);
    sleep = slp;
  }
  
  public ActorSprite(String file, int destX, int destY, int sWid, int sHei, Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
    super(file, destX, destY, sWid, sHei);
    setDirections(d, r, u, l);
    sleep = slp;
  }
  
  public ActorSprite(Image im, int destX, int destY, int sWid, int sHei, Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
    super(im, destX, destY, sWid, sHei);
    setDirections(d, r, u, l);
    sleep = slp;
  }
  
  public enum Direction{
    Left, Right, Up, Down, Sleep;
  }
  
  public void putSleep(){ setPose(Direction.Sleep); sleeping = true;}
  public void putAwake(){sleeping = false;}
  
  public void setDirections(Rectangle d, Rectangle r, Rectangle u, Rectangle l){
    down = d;
    right = r;
    up = u;
    left = l;
  }
  
  public void setPose(Direction d){
    switch(d){
      case Down: x = down.x;
      y = down.y;
      break;
      case Right: x = right.x;
      y = right.y;
      break;
      case Up: x = up.x;
      y = up.y;
      break;
      case Left: x = left.x;
      y = left.y;
      break;
      case Sleep: x = sleep.x;
      y = sleep.y;
      break;
    }
  }
  
  public Direction determineDirection(Point p){
    int offsetX = p.x - dx;
    int offsetY = p.y - dy;
    //in order: down, right, up, left
    if(offsetY > 0){
      return Direction.Down;
    }
    else if(offsetX > 0)
      return Direction.Right;
    else if(offsetY < 0)
      return Direction.Up;
    return Direction.Left;
  }

  public static Direction determineDirection(Point p1, Point p2){
    int offsetX = p2.x - p1.x;
    int offsetY = p2.y - p2.y;
    //in order: down, right, up, left
    if(offsetY > 0){
      return Direction.Down;
    }
    else if(offsetX > 0)
      return Direction.Right;
    else if(offsetY < 0)
      return Direction.Up;
    return Direction.Left;
  }
  
  public static Direction reverseDirection(Direction dir){
    if(dir == Direction.Down)
      return Direction.Up;
    else if(dir == Direction.Right)
      return Direction.Left;
    else if(dir == Direction.Up)
      return Direction.Down;
    return Direction.Right;
  }
  
  public static Dimension dirCoefficients(Direction dir){
    int signX = 0; 
    int signY = 0;
    if(dir == Direction.Down)
      signY = 1;
    if(dir == Direction.Right)
      signX = 1;
    if(dir == Direction.Up)
      signY = -1;
    if(dir == Direction.Left)
      signX = -1;
    return new Dimension(signX, signY);
  }
  
  public Rectangle getWalking(Direction d){
    switch(d){
      case Down: return down;
      case Right: return right;
      case Up: return up;
    }
    return left;
  }
  /*
  public void paintSleeping(Graphics g){
    if(sleeping)
      g.drawImage(img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2), sleep.x, sleep.y, sleep.x+sWidth, sleep.y+sHeight, this);
  }
  */
  public void paint(Graphics g){
    /*if(sleeping)
      return;
      else*/
    super.paint(g);
  }
  
}