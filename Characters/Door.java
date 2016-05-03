//package Game.Characters;
//import Game.*;

import java.awt.*;

public class Door extends Enemy{
    
  public Door(Member[] c, Party pp){
    super("Door", null, 0);
    sprite = new DoorSprite();
    pcs = c;
    pparty = pp;
    setHealth(50);
    speed = 1.0f;
    setStats();
  }
  
  
  public void addStats(){}
  public void gradeNextBat(){}
  public void gradeNextCom(){}
  public void gradeNextMag(){} 

  public void fightAlgorithm(){
    setTargets(null, 0); 
    chooseSkill(AttackType.WAIT.v2);
  }
  
  public class DoorSprite extends BattleSprite{
    //sprite to be moved
    protected SpriteInfo[] info;
    //current set of movement
    protected Sprite movingSprite;
    protected Rectangle movingBounds;
    protected int animMove;
    protected Dimension move;
    protected int numMoves;
    protected boolean repeatSprite;
    //a series of instructions indicating how and when the sprite moves
    protected int motionCounter;
    protected int moveCounter;
    
    public DoorSprite(){
      super("Game/Door.png", 16, 16, new Rectangle(16, 0, 16, 16), new Rectangle(32, 0, 16, 16), null, null, 0, new Rectangle(0, 0, 16, 16), Door.this,
            null, null, null, null, null);
    }
    
    public int fframetime(){
      return fframetime(currSkill);
    }
    public int fframetime(AttackType.Skill skill){
      return 100;
    }
    
    public FightTimer getFightTimer(){
      return new DoorTimer();
    }
    
    public synchronized void attackFrame(){
      AttackType.Skill attack = currSkill;
      switch(attack){
        case Wait:
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, null, sWidth, null, -1, false);
          numFrames = 1;
          animTime = fframetime();
          movingSprite = null;
          setInfo();
          break;
      }
      setMana(attack);
      drawInFight();
    }
  
    public void setInfo(){
      moveCounter = 0;
      motionCounter = 0;
      movingSprite = info[0].movingSprite;
      movingBounds = info[0].movingBounds;
      animMove = info[0].animMove;
      move = info[0].movement;
      numMoves = info[0].numMoves;
      repeatSprite = info[0].repeatSprite;
    }
    
    public void paint(Graphics g){
      if(health() <= 0)
        return;
        //g.drawImage(img, dx-sWidth/2, dy-sHeight/2, dx+sWidth/2, dy+sHeight/2, death.x, death.y, death.x+death.width, death.y+death.height, this);
      super.paint(g);
      if(movingSprite != this && movingSprite != null)
        movingSprite.paint(g);
    }
    
    public class DoorTimer extends FightTimer{
      
      public synchronized void run(){ 
        if(movingSprite == null || motionCounter >= info.length){}
        else{
          if(moveCounter == numMoves){
            motionCounter++;
            if(motionCounter >= info.length)
              return;
            moveCounter = 0;
            movingSprite = info[motionCounter].movingSprite;
            movingBounds = info[motionCounter].movingBounds;
            animMove = info[motionCounter].animMove;
            move = info[motionCounter].movement;
            numMoves = info[motionCounter].numMoves;
            repeatSprite = info[motionCounter].repeatSprite;
            movingSprite.x = movingBounds.x;
            movingSprite.y = movingBounds.y;
          }
          moveCounter++;
          movingSprite.dx+=move.width;
          movingSprite.dy+=move.height;
          movingSprite.x+=animMove;
          if(movingSprite.x >= movingBounds.x + movingBounds.width){
            movingSprite.x = movingBounds.x;
          }
        }  
      }
      
    }
  }
  
  public void setStats(){
    stats = new Stats(0, 0, 0, 0, 0);
  }
  
  public boolean availableMana(AttackType.Skill type){
    switch(type){
      case Wait: break;
    }
    return false;
  }
  
  public void setMana(AttackType.Skill type){
    switch(type){
      case Wait: break;
    }
    return;
  }
  
  public DamageCalculation getDamageTimer(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
    return new DoorDamage(battle, q, attack, inv);
  }
  
  public class DoorDamage extends Battler.DamageCalculation{
    int tCounter = 0;
    int sign = 1;
    public DoorDamage(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
      super(battle, q, attack, inv);
    }
    
    public synchronized void run(){
      switch(currSkill){
        case Wait:
          break;
      }
    }
    
  } 
}