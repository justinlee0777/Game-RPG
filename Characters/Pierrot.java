//package Game.Characters;
//import Game.*;

import java.awt.*;

public class Pierrot extends Enemy{
  
  public Pierrot(AttackType[] skills, Rectangle[] skilRects, Rectangle[] proj, int numSkills, Member[] c, Party pp){
    super("Pierrot", skills, numSkills);
    sprite = new PierrotSprite(skilRects, proj, numSkills);
    pcs = c;
    pparty = pp;
    speed = 1.1f;
    setStats();
  }
  

    public void addStats(){}
    public void gradeNextBat(){}
    public void gradeNextCom(){}
    public void gradeNextMag(){}   
  

  public void fightAlgorithm(){
    Member c = choosePlayer();
    if(c == null){
      Member[] pc = new Member[1];
      pc[0] = c;
      setTargets(pc, 0); 
      chooseSkill(AttackType.WAIT.v2);
    }
    else{
      Battler[] b;
      if(chooseSkill.nextBoolean()){
        Battler temp = null;
        float f = 0.7f;
        for(int i = 0; i < numAllies; i++){
          if(allies[i].health() > 0 && (allies[i].percentOfFuncMagic() < f || (allies[i] instanceof Bean && !((Bean)allies[i]).grown))){
            f = allies[i].percentOfMagic();
            temp = allies[i];
          } 
        }  
        
        if(temp != null && availableMana(AttackType.Skill.Drain)){       
          if(c.magic() <= 0){
            for(Party.Iterator itr = pparty.begin(); !itr.end(); itr.increment()){
              if(itr.key().magic() > 0)
                c = (Member)itr.key();
            }
          }
          if(c != null){
            b = new Battler[2];
            b[0] = c;
            b[1] = temp;
            setTargets(b, 2);
            chooseSkill(AttackType.Skill.Drain);
            return;
          }
        }
      }
      b = new Battler[1];
      b[0] = c;
      setTargets(b, 1);
      chooseSkill(AttackType.Skill.NONE);
      return;
    }
  }
  
  public class PierrotSprite extends BattleSprite{
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
    
    public PierrotSprite(Rectangle[] skilRects, Rectangle[] proj, int numSkills){
      super("Game/Pierrot.png", 21, 21, new Rectangle(252, 0, 21, 21), new Rectangle(273, 0, 21, 21), skilRects, proj, numSkills, new Rectangle(278, 26, 16, 16), Pierrot.this,
            new Rectangle(0, 0, 63, 21), new Rectangle(63, 0, 63, 21), new Rectangle(126, 0, 63, 21), new Rectangle(189, 0, 63, 21), new Rectangle(0, 0, 21, 21));
    }
    
    public int fframetime(){
      return fframetime(currSkill);
    }
    public int fframetime(AttackType.Skill skill){
      switch(skill){
        case NONE:
          return (100 * numFrames);
        case Drain: 
          return (100 * numFrames) * 5;
        case Item: return 100;
      }
      return 100 * numFrames;
    }
    
    public void standFrame(){
      super.standFrame();
      movingSprite = null;
    }
    
    public FightTimer getFightTimer(){
      return new PierrotTimer();
    }
    
    public synchronized void attackFrame(){
      AttackType.Skill attack = currSkill;
      int moveX, moveY;
      switch(attack){
        case NONE:
          numFrames = (skilRect[0].width/sWidth);
          x = skilRect[0].x;
          y = skilRect[0].y;
          
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(this, skilRect[0], sWidth, new Dimension(0, 0), -1, false);

          animTime = fframetime();
          setInfo();
          break;
        case Drain:
          numFrames = 1;
          x = skilRect[1].x;
          y = skilRect[1].y;
          animTime = fframetime()*2;
          info = new SpriteInfo[2];
          Sprite s = new Sprite(img.getSubimage(projectile[1].x, projectile[1].y, projectile[1].width, projectile[1].height), dx, dy);
          moveX = (targets[0].sprite.dx-dx)/5;
          moveY = (targets[0].sprite.dy-dy)/5;
          info[0] = new SpriteInfo(s, new Rectangle(0, 0, 10, 10), projectile[1].width, new Dimension(moveX, moveY), 5, false);
          moveX = (targets[1].sprite.dx-targets[0].sprite.dx)/5;
          moveY = (targets[1].sprite.dy-targets[0].sprite.dy)/5;
          info[1] = new SpriteInfo(s, new Rectangle(0, 0, 10, 10), projectile[1].width, new Dimension(moveX, moveY), 5, false);
          setInfo();
          break;
        case Wait:
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, null, sWidth, null, -1, false);
          numFrames = 1;
          animTime = fframetime();
          movingSprite = null;
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
    
    public void paint(Graphics g){
      super.paint(g);
      if(movingSprite != this && movingSprite != null)
        movingSprite.paint(g);
    }
    
    public class PierrotTimer extends FightTimer{
      
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
    stats = new Stats(0, 25, 5, 2, 0);
  }
  
  public boolean availableMana(AttackType.Skill type){
    switch(type){
      case Drain: 
        if(magic() >= 5)
        return true;
    }
    return false;
  }
  
  public void setMana(AttackType.Skill type){
    switch(type){
      case Drain: 
        if(magic() >= 5)
        useMana(5);
    }
    return;
  }
  
  public DamageCalculation getDamageTimer(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
    return new PierrotDamage(battle, q, attack, inv);
  }
  
  public class PierrotDamage extends Battler.DamageCalculation{
    int tCounter = 0;
    int sign = 1;
    public PierrotDamage(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
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
        case Drain:
          if(tCounter >= numTargets)
          tCounter = 0;
          targets[tCounter].useMana((int)atk.damage*sign);
          btl.removePCDead(queue);
          btl.removeEnDead(queue);
          hitCounter++;
          tCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          sign *= -1;
          break;
        case Item:
          inv.use(item, targets[0]);
          break;
      }
    }
    
  } 
}