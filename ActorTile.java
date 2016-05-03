//package Game;

import java.awt.*;

public class ActorTile extends DialogueTile{
  protected ActorSprite sprite; 
  //for bottom tiles
  
  //Rectangles  
  public Rectangle down, right, up, left;
  
  //for Door...
  public ActorTile(){
    super();
  }
  
  public ActorTile(String file, int destx, int desty, ActorSprite actor, Queue<Incident> incidents){
    super(file, destx, desty, actor.sWidth, actor.sHeight, incidents);
    sprite = actor;
    down = actor.down;
    right = actor.right;
    up = actor.up;
    left = actor.left;
    setBounds(dx, dy, width, height);
  }
  
  public ActorTile(String file, int destx, int desty, ActorSprite actor, boolean pass, Queue<Incident> incidents){
    super(file, destx, desty, actor.sWidth, actor.sHeight, incidents);
    sprite = actor;
    down = actor.down;
    right = actor.right;
    up = actor.up;
    left = actor.left;
    setBounds(dx, dy, width, height);
    isPassable = pass;
  }
  
  public ActorTile(Image img, int destx, int desty, ActorSprite actor, Queue<Incident> incidents){
    super(img, destx, desty, actor.sWidth, actor.sHeight, incidents);
    sprite = actor;
    down = actor.down;
    right = actor.right;
    up = actor.up;
    left = actor.left;
    setBounds(dx, dy, width, height);
  }
  
  public ActorTile(Image img, int destx, int desty, ActorSprite actor, boolean pass, Queue<Incident> incidents){
    super(img, destx, desty, actor.sWidth, actor.sHeight, incidents);
    sprite = actor;
    down = actor.down;
    right = actor.right;
    up = actor.up;
    left = actor.left;
    setBounds(dx, dy, width, height);
    isPassable = pass;
  }
  
  public double calculateDistance(Point a, Point b){
    return sprite.calculateDistance(a, b);
  }
  
  public void setDirections(Rectangle d, Rectangle r, Rectangle u, Rectangle l){
    sprite.setDirections(d, r, u, l);
  }
  
  public void setPose(ActorSprite.Direction dir){
    switch(dir){
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
      case Sleep: x = sprite.sleep.x;
      y = sprite.sleep.y;
      break;
    }
  }
  
  public ActorSprite.Direction determineDirection(Point p){
    return sprite.determineDirection(p);
  }
  
  public Rectangle getWalking(ActorSprite.Direction d){
    return sprite.getWalking(d);
  }
  
  public Queue<Sprite> use(Field field, Tile actor, FieldCharacter c){
    if(c.dx > dx + sWidth/2){
      x = right.x;
      //bounds = right;
      //bounds.width = right.width;
      //bounds.height = right.height;
    }
    else if(c.dx < dx - sWidth/2){
      x = left.x;
    }
    else if(c.dy < dy - sHeight/2){
      x = sprite.up.x;
    }
    else{
      x = down.x;
    }
    return super.use(field, actor, c);
  }
  
  public void paint(Graphics g){
    g.drawImage(img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2), x, y, x+sWidth, y+sHeight, this);
  }
  
}