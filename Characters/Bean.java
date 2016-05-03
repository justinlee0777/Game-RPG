//package Game;

import java.awt.*;
import java.util.*;

public class Bean extends Enemy{
  protected boolean grown = false;
  
  public Bean(AttackType[] skills, Rectangle[] skilRects, Rectangle[] proj, int numSkills, Member[] c, Party pp){
    super("Bean", skills, numSkills);
    sprite = new BeanSprite(skilRects, proj, numSkills);
    pcs = c;
    pparty = pp;
    setMaxMagic(magic()*2);
    //setMagic(90);
    speed = 1.0f;
    setStats();
  }
  
  public void addStats(){}
  public void gradeNextBat(){}
  public void gradeNextCom(){}
  public void gradeNextMag(){}   
    
  public void fightAlgorithm(){
    if(grown)
      useMana(-10);
    Member c = choosePlayer();
    if(c == null){
      Member[] pc = new Member[1];
      pc[0] = c;
      setTargets(pc, 0); 
      chooseSkill(AttackType.WAIT.v2);
      return;
    }
    else{  
      if(!grown && availableMana(AttackType.Skill.Grow)){
        attack = 50;
        speed = 2.0f;
        chooseSkill(AttackType.Skill.Grow);
        return;
      }
      else if(chooseSkill.nextBoolean() && grown){
        Battler temp = null;
        for(int i = 0; i < numAllies; i++)
          if(!allies[i].isCovered() && allies[i] != this)
          temp = allies[i];
        
        if(temp != null && temp != this){
          Battler[] b = new Battler[1];
          b[0] = temp;
          setTargets(b, 1);
          chooseSkill(AttackType.Skill.Guardian);
          return;
        }
      }
      
      Battler[] b = new Battler[1];
      b[0] = c;
      setTargets(b, 1);
      chooseSkill(AttackType.Skill.NONE);
    }

  }
  
  public class BeanSprite extends BattleSprite{
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
    public BeanSprite(Rectangle[] skilRects, Rectangle[] proj, int numSkills){
      super("Game/Bean.png", 20, 20, new Rectangle(240, 0, 20, 20), new Rectangle(260, 0, 20, 20), skilRects, proj, numSkills, new Rectangle(264, 144, 16, 16), Bean.this,
            new Rectangle(0, 0, 60, 20), new Rectangle(60, 0, 60, 20), new Rectangle(120, 0, 60, 20), new Rectangle(180, 0, 60, 20), new Rectangle(0, 0, 20, 20));
    }
    
    public int fframetime(){
      return fframetime(currSkill);
    }
    
    public int fframetime(AttackType.Skill skill){
      switch(skill){
        case NONE:
          if(grown){return (100 * 8);}
          else{return 100*Math.abs((int)calculateDistance(new Point(dx, dy), new Point(targets[0].sprite.dx, targets[0].sprite.dy))/(sWidth/2));}
        case Grow:
          return (100 * numFrames);
        case Guardian:
          return 100;
        case Item: return 100;
      }
      return 100 * numFrames;
    }
    
    public void standFrame(){
      if(grown){
        x = 0;
        y = 80;
      }
      else{
        x = 0;
        y = 0;
      }
      drawNotInFight();
      movingSprite = null;
    }
    
    public void hitFrame(){
      if(grown){
        x = 80;
        y = 80;
      }
      else{
        x = hitSprite.x;
        y = hitSprite.y;
      }
      movingSprite = null;
    }
    
    public FightTimer getFightTimer(){
      return new BeanTimer();
    }
    
    public synchronized void attackFrame(){
      AttackType.Skill attack = currSkill;
      int moveX, moveY;
      int incMoves;
      switch(attack){
        case NONE:
          if(grown){
          numFrames = (skilRect[1].width/sWidth);
          x = skilRect[1].x;
          y = skilRect[1].y;
          
          info = new SpriteInfo[2];
          info[0] = new SpriteInfo(this, skilRect[1], sWidth, new Dimension(0, 0), 3, false);
          moveX = (targets[0].sprite.dx-dx)/5;
          moveY = (targets[0].sprite.dy-dy)/5;
          info[1] = new SpriteInfo(getAngledSprite(targets[0]), new Rectangle(0, 0, 8, 10), 8, new Dimension(moveX, moveY), 5, false);
          
          animTime = fframetime();     
        }
          else{
            numFrames = (skilRect[0].width/sWidth);
            x = skilRect[0].x;
            y = skilRect[0].y;
            
            info = new SpriteInfo[2];
            incMoves = Math.abs((int)calculateDistance(new Point(dx, dy), new Point(targets[0].sprite.dx, targets[0].sprite.dy))/(sWidth/2));
            if(incMoves > 0){
              moveX = (targets[0].sprite.dx-dx)/incMoves;
              moveY = (targets[0].sprite.dy-dy)/incMoves;
            }
            else{
              moveX = 1;
              moveY = 1;
            }
            switch(determineDirection(new Point(targets[0].sprite.dx, targets[0].sprite.dy))){
              case Down: x = 0;
              info[0] = new SpriteInfo(this, new Rectangle(0, 20, 20, 20), sWidth, new Dimension(moveX, moveY), incMoves, false);
              info[1] = new SpriteInfo(this, up, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
              break;
              case Right: x = 20;
              info[0] = new SpriteInfo(this, new Rectangle(20, 20, 20, 20), sWidth, new Dimension(moveX, moveY), incMoves, false);
              info[1] = new SpriteInfo(this, left, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
              break;
              case Up: x = 40;
              info[0] = new SpriteInfo(this, new Rectangle(40, 20, 20, 20), sWidth, new Dimension(moveX, moveY), incMoves, false);
              info[1] = new SpriteInfo(this, down, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
              break;
              case Left: x = 60;
              info[0] = new SpriteInfo(this, new Rectangle(60, 20, 20, 20), sWidth, new Dimension(moveX, moveY), incMoves, false);
              info[1] = new SpriteInfo(this, right, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
              break;
            }
            y = 20;      
            
            animTime = fframetime()*2;
          }
          setInfo();
          break;
        case Grow:
          grown = true;
          numFrames = (skilRect[2].width/sWidth);
          stats.funcAtk+=20;
          stats.funcSpd+=1.0f;
          sHeight = 40;
          x = skilRect[2].x;
          y = skilRect[2].y;
          
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(this, skilRect[2], sWidth, new Dimension(0, 0), -1, false);
          
          animTime = fframetime();
          setInfo();
          break;
        case Guardian:
          numFrames = 1;
          x = projectile[3].x;
          y = projectile[3].y;
          
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(this, projectile[3], sWidth, new Dimension(0, 0), -1, false);
                                   
          animTime = fframetime();
          setInfo();
          break;
        case Wait:
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, null, sWidth, null, -1, false);
          numFrames = 1;
          animTime = fframetime();
          setInfo();
          break;
        case Item:
          info = new SpriteInfo[1];
          switch(determineDirection(new Point(targets[0].sprite.dx, targets[0].sprite.dy))){
            case Down: x = 0;
            info[0] = new SpriteInfo(this, new Rectangle(0, 18, 18, 18), sWidth, new Dimension(0, 0), 1, false);
            break;
            case Right: x = 0;
            info[0] = new SpriteInfo(this, new Rectangle(18, 18, 18, 18), sWidth, new Dimension(0, 0), 1, false);
            break;
            case Up: x = 0;
            info[0] = new SpriteInfo(this, new Rectangle(36, 18, 18, 18), sWidth, new Dimension(0, 0), 1, false);
            break;
            case Left: x = 0;
            info[0] = new SpriteInfo(this, new Rectangle(54, 18, 18, 18), sWidth, new Dimension(0, 0), 1, false);
            break;
          }
          y = 0;
          animTime = fframetime()*2;
          motionCounter = 0;
          moveCounter = 0;
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
    
    public Sprite getAngledSprite(Battler b){
      if(b.sprite.dx  < dx && b.sprite.dy < dy)  return new Sprite(img.getSubimage(projectile[1].x, projectile[1].y, 8, 10), dx, dy);
      else if(b.sprite.dx > dx && b.sprite.dy < b.sprite.dy) return new Sprite(img.getSubimage(projectile[1].x+8, projectile[1].y, 8, 10), dx, dy);
      else if(b.sprite.dx < dx && b.sprite.dy > dy) return new Sprite(img.getSubimage(projectile[1].x, projectile[1].y+10, 8, 10), dx, dy);
      else if(b.sprite.dx > dx && b.sprite.dy > dy) return new Sprite(img.getSubimage(projectile[1].x+8, projectile[1].y+10, 8, 10), dx, dy);
      else if(b.sprite.dy < dy) return new Sprite(img.getSubimage(projectile[1].x, projectile[1].y+20, 8, 10), dx, dy);
      return new Sprite(img.getSubimage(projectile[1].x+8, projectile[1].y+20, 8, 10), dx, dy);
        
    }
    
    public void paint(Graphics g){
      super.paint(g);
      if(movingSprite != null && movingSprite != this)
        movingSprite.paint(g);
    }
    
    public class BeanTimer extends FightTimer{
      
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
  
  public AttackType[] getSkills(){return Arrays.copyOfRange(skills, 2, numSkills);}
  
  public int numSkills(){return super.numSkills()-1;}
  
  public void setStats(){
    stats = new Stats(50, 50, 0, 0, 0);
  }
  
  public void receiveDamage(int dam){
    super.receiveDamage(dam);
    if(health() <= 0)
      sprite.sHeight = 20;
  }
  
  public boolean availableMana(AttackType.Skill type){
    switch(type){
      case Grow:
        if(magic() >= 90)
        return true;
    }
    return false;
  }
  
  public void setMana(AttackType.Skill type){
    switch(type){
      
    }
    if(grown){
      if(magic() <= 10){
        useMana(10);
        grown = false;
        sprite.sHeight = 20;
        stats.funcAtk-=20;
        stats.funcSpd-=1.0f;
      }
      else
        useMana(10);
    }
    return;
  }
  
  public DamageCalculation getDamageTimer(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
    return new BeanDamage(battle, q, attack, inv);
  }
  
  public class BeanDamage extends Battler.DamageCalculation{
    int tCounter = 0;
    int sign = 1;
    public BeanDamage(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
      super(battle, q, attack, inv);
    }
    
    public synchronized void run(){
      switch(currSkill){
        case NONE:
          targets[tCounter].receiveDamage((int)atk.damage);
          btl.removePCDead(queue);
          btl.removeEnDead(queue);
          hitCounter++;
          tCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          break;
        case Guardian:
          targets[0].covered(Bean.this, true);
          hitCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          break;
        case Item:
          inv.use(item, targets[0]);
          break;
      }
    }
    
  } 
}