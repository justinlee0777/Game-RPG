//package Game;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

public class FieldCharacter extends NPC{
  private volatile int num;
  private int numFrames;
  private Rectangle currWalk;
  //int dx, dy;
  protected ActorSprite.Direction dir;
  //collision
  //protected Line2D faceSide;
  protected PlayerParty pparty;
  protected Ellipse2D.Double shadow;
  //painting
  protected DrawTree.Node cNode;
  
  //Sprite sheet convention: the first sprite of a rectangle is considered a stand-still sprite
  //and will not be included in the animation
  public FieldCharacter(String file, PlayerParty pp, NPC npc, Rectangle slp){
    super(npc.img, npc.dx, npc.dy, npc.sprite, null, npc.name, npc.head, npc.headshot);
    num = 0;
    numFrames = 4;
    pparty = pp;
    dir = ActorSprite.Direction.Right;
    currWalk= right;
    shadow = new Ellipse2D.Double(dx-sWidth/3, dy+sHeight/4, sWidth*2/3, sHeight/2);
    //faceSide = new Line2D.Float(dx-sWidth/2, dy+sHeight/2, dx+sWidth/2, dy+sHeight/2);
    //faceBox = new Rectangle(dx+sWidth/2, dy-sHeight/2, 2, sHeight);
  }
 
  public void setNode(DrawTree.Node node){ cNode = node;}
  public void requestUpdate(DrawTree tree){ cNode.requestMove(tree); }
  
  public void setCoordinates(DrawTree.Node node, DrawTree tree, int destx, int desty){
    super.setCoordinates(node, tree, destx, desty);
    for(int i = 0; i < pparty.getTeamSize(); i++)
      pparty.getTeam()[i].sprite.setCoordinates(dx, dy);
    //node.requestMove();
  }
  
  public void setCoordinates(DrawTree.Node node, DrawTree tree, Point p){
    super.setCoordinates(node, tree, p);
    for(int i = 0; i < pparty.getTeamSize(); i++)
      pparty.getTeam()[i].sprite.setCoordinates(dx, dy);
    //node.requestMove();
  }
  
  public void init(){
    super.init();
    //setBounds(dx, dy, sWidth, sHeight);
    if(pparty != null)
      for(int i = 0; i < pparty.getTeamSize(); i++)
      pparty.getTeam()[i].sprite.setCoordinates(dx, dy);
  }
  
  /*
  public void updateBox(){  
    switch(dir){
      case Right: //faceBox = new Rectangle(dx+sWidth/2, dy-sHeight/2, 2, sHeight);
        faceSide = new Line2D.Float(dx-sHeight/2, dy+sHeight/2, dx+sHeight/2, dy+sHeight/2);
        break;
      case Left: //faceBox = new Rectangle(dx-sWidth/2-2, dy-sHeight/2, 2, sHeight);
        break;
      case Down: //faceBox = new Rectangle(dx-sWidth/2, dy+sHeight/2, sWidth, 2);
        //faceSide = new Line2D.Float(dx-sWidth/2, dy+sHeight/2, dx+Width/2, dy+sHeight/2);
        break; 
      case Up: //faceBox = new Rectangle(dx-sWidth/2, dy-sHeight/2-2, sWidth, 2);
        faceSide = new Line2D.Float(dx-sWidth/2, dy-sHeight/2, dx+sWidth/2, dy-sHeight/2);
        break; 
    }
    //setBounds(dx-sWidth/2, dy-sHeight/2, sWidth, sHeight);
    init();
  }
  */
  
  public void setDirection(ActorSprite.Direction di){
    dir = di;
    //faceBox = null;
    switch(di){
      case Right: 
        currWalk = right;
        x = right.x;
        y = right.y;
        shadow = new Ellipse2D.Double(dx-sWidth/2, dy-sHeight/2, sWidth/2, sHeight);
        break;
      case Left: 
        currWalk = left;
        x = left.x;
        y = left.y;
        shadow = new Ellipse2D.Double(dx, dy-sHeight/2, sWidth/2, sHeight);
        break;
      case Down:
        currWalk = down;
        x = down.x;
        y = down.y;
        shadow = new Ellipse2D.Double(dx-sWidth/2, dy-sHeight/2, sWidth, sHeight/2);
        break;
      case Up: 
        currWalk = up;
        x = up.x;
        y = up.y;
        shadow = new Ellipse2D.Double(dx-sWidth/2, dy, sWidth, sHeight/2);
        break;
    }
    //updateBox();
    //System.out.println(x + " " + y);
  }
  
  public void forward(){
    if(++num >= numFrames){
      num = 0;
      x+=sWidth;
    }
    if(x >= (currWalk.x + currWalk.width)){
      switch(dir){
        case Up: 
        case Down:
          x = currWalk.x+sWidth; break;
        case Right:
        case Left:
          x = currWalk.x; break;
      }
    }
    bounds = new Rectangle(dx-sWidth/2, dy-sHeight/2, sWidth, sHeight);
    shadow = new Ellipse2D.Double(dx-sWidth/3, dy+sHeight/4, sWidth*2/3, sHeight/2);
    /*switch(dir){
      case Right: shadow = new Ellipse2D.Double(dx-sWidth/2, dy-sHeight/4, sWidth/2, sHeight/2);
      break;
      case Left: shadow = new Ellipse2D.Double(dx, dy-sHeight/4, sWidth/2, sHeight/2);
      break;
      case Down: shadow = new Ellipse2D.Double(dx-sWidth/4, dy-sHeight/2, sWidth/2, sHeight/2);
      break;
      case Up: shadow = new Ellipse2D.Double(dx-sWidth/4, dy, sWidth/2, sHeight/2);
      break;
    }*/
  }
  
  /*
  public int priority(){
    return super.priority() + 4;
  }
  */
  public boolean isDir(ActorSprite.Direction d){
    return dir == d;
  }
  
  public void reset(){ num = 0; x=currWalk.x;}
  
  /*
  public void paint(Graphics g){

    //g.setColor(Color.BLACK);
    //((Graphics2D)g).draw(shadow);
    //((Graphics2D)g).fill(shadow);
    //g.setColor(Color.WHITE);
    //g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    super.paint(g);
    System.out.println(dx + " " + dy);
  }
  */
}
