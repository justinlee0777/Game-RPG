//package Game;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;

public class FieldEnemy extends NPC{ 
  protected Rectangle currWalk;
  protected Direction d;
  //protected Rectangle faceBox;
  protected BattlerConstructor.EnemyType e;
  private FieldCharacter c;
  protected Enemy[] enemies;
  protected boolean[] locks; 
  protected int turns = 0;
  private Grid grid;
  private Timer time;
  private CollisionTimer t;
  public boolean isScripted = false;
  public boolean collided = false;

  public FieldEnemy(FieldCharacter character, Grid g, NPC npc, BattlerConstructor.EnemyType en){
    super(npc.img, npc.dx, npc.dy, npc.sprite, null, npc.name, npc.headshot);
    e = en;
    currWalk = down;
    c = character;
    //faceBox = new Rectangle(dx, dy, width, height);
    //faceBox = new Rectangle(dx-width, dy-height, width*3, height*3);
    grid = g;
    time = new Timer();
    t = new CollisionTimer();
  }
  //public NPC(String file, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle por)
  public FieldEnemy(FieldCharacter character, Grid g, NPC npc, BattlerConstructor.EnemyType en, boolean scripted, boolean[] comLocks){
    this(character, g, npc, en);
    isScripted = scripted;
    locks = comLocks;
  }

  public FieldEnemy(FieldCharacter character, Grid g, NPC npc, BattlerConstructor.EnemyType en, boolean scripted, boolean[] comLocks, int trns){
    this(character, g, npc, en, scripted, comLocks);
    turns = trns;
  }
  //should be used for scripted battles exclusively
  public FieldEnemy(FieldCharacter character, Grid g, NPC npc, Enemy[] enmy, String file, Size sSize, boolean scripted, boolean[] comLocks, int trns){
    super(npc.img, npc.dx, npc.dy, npc.sprite, null, npc.name, npc.headshot);
    try{
      img = ImageIO.read(getClass().getResource(file));
    }catch(IOException i){System.out.println( "FieldEnemy class: Cannot open " + file);}
    sWidth = (int)sSize.width;
    sHeight = (int)sSize.height;
    enemies = enmy;
    currWalk = down;
    c = character;
    isScripted = scripted;
    locks = comLocks;
    turns = trns;
    grid = g;
    time = new Timer();
    t = new CollisionTimer();
  }
  
  //should be used for Door exclusively
  public FieldEnemy(FieldCharacter character, Grid g, Point dest, Size sSize,BattlerConstructor.EnemyType en, boolean scripted, boolean[] comLocks, int trns){
    super();
    dx = dest.x;
    dy = dest.y;
    setBounds(dx, dy, width, height);
    sWidth = (int)sSize.width;
    sHeight = (int)sSize.height;
    e = en;
    currWalk = down;
    c = character;
    isScripted = scripted;
    locks = comLocks;
    turns = trns;
    grid = g;
    time = new Timer();
    t = new CollisionTimer();
  }
  
  public enum Direction{
    Stand, Left, Right, Up, Down;
  }
  
  public void setEnemy(BattlerConstructor.EnemyType e){
    /*
    switch(e){
      case Irritants: 
        try{
        img = ImageIO.read(getClass().getResource("Game/Frog.png"));
      }catch(IOException i){System.out.println( "FieldEnemy class: Cannot open Frog");}
      /*
        sWidth = 20;
        sHeight = 20;
        down = new Rectangle(0, 0, 80, 20);
        right = new Rectangle(80, 0, 60, 20);
        up = new Rectangle(140, 0, 80, 20);
        left = new Rectangle(220, 0, 60, 20);
        
        break;
    }*/
  }
  
  public void makeEnemies(PlayerParty pp){
    if(e != null)
      enemies = BattlerConstructor.makeEnemies(e, pp);
    for(int i = 0; i < enemies.length; i++)
      enemies[i].sprite.setCoordinates(dx, dy);
  }
  /*
  public int numEnemies(){
    return BattlerConstructor.numEnemies(e);
  }*/
  
  /*public void updateBox(){
    switch(d){
      case Right: faceBox = new Rectangle(dx+width, dy, 2, height);
      break;
      case Left: faceBox = new Rectangle(dx-2, dy, 2, height);
      break;
      case Down: faceBox = new Rectangle(dx, dy+height, width, 2);
      break; 
      case Up: faceBox = new Rectangle(dx, dy-2, width, 2);
      break; 
    }
    setBounds(dx, dy, width, height);
  }*/
  
  public void walk(){
    Direction temp = d;
    d = grid.findPath(c, this);   
    switch(d){
      case Stand: 
        currWalk = down;
        break;
      case Up: 
        currWalk = up;
        dy-=2;
        forward();
        break;
      case Right:
        currWalk = right;
        dx+=2;
        forward();
        break;
      case Down: 
        currWalk = down;
        dy+=2;
        forward();
        break;
      case Left: 
        currWalk = left;
        dx-=2;
        forward();
        break;
    }
    if(d != temp)
      x = currWalk.x;
    setBounds(dx, dy, width, height);
    //faceBox = new Rectangle(dx-width, dy-height, width*3, height*3);
    //updateBox();
  }
 
  public void forward(){
    x+=sWidth;
    if(x >= (currWalk.x + currWalk.width))
      x = currWalk.x;
  }
  
  public void join(){
    t.cancel();
    time.cancel();
    time = null;
  }
  
  public void resume(){
    t = new CollisionTimer();
    time.schedule(t, 100, 100);
  }
  
  public void pause(){
    t.cancel();
    t = null;
  }
  
  //to prevent incidents from being used
  public Queue<Sprite> use(Field field, Tile actor, FieldCharacter c){
    return null;
  }
  
/*  public void paint(Graphics g){

  }*/
  
  public class CollisionTimer extends TimerTask{
    public void run(){
      if(!getBounds().intersects(c.getBounds())){
        double distance = Math.sqrt( (c.dx-dx)*(c.dx-dx) + (c.dy-dy)*(c.dy-dy) );
        if(distance < sWidth * 5)
          walk();
      }
      else{
        collided = true;
        join();
      }
    }
  }
  
}