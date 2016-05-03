//package Game.Characters;
//import Game.*;

import java.awt.*;

public class Shadow extends Member{
  public volatile boolean isHiding = false;
  private Battler[] hiders;
  private int numHiders;
    
  public Shadow(AttackType[] skills, Rectangle[] skilRects, Rectangle[] proj, int numSkills){
    super("Shadow", skills, numSkills);
    sprite = new ShadowSprite(skilRects, proj, numSkills);
    speed = 1.0f;
    set_NextBatExp(200);
    set_NextComExp(200);
    set_NextMagExp(200);
    setStats();
  }

  public class ShadowSprite extends BattleSprite{
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
    
    public ShadowSprite(Rectangle[] skilRects, Rectangle[] proj, int numSkills){
      super("Game/Shadow.png", 18, 18, new Rectangle(180, 0, 18, 18), new Rectangle(198, 0, 18, 18), skilRects, proj, numSkills, new Rectangle(200, 38, 16, 16), Shadow.this,
            new Rectangle(0, 0, 54, 18), new Rectangle(54, 0, 36, 18), new Rectangle(90, 0, 54, 18), new Rectangle(144, 0, 36, 18), new Rectangle(0, 0, 18, 18));
    }
    
    public int fframetime(){
      return fframetime(currSkill);
    }
    
    public int fframetime(AttackType.Skill skill){
      switch(skill){
        case NONE: return 100*Math.abs((int)calculateDistance(new Point(dx, dy), new Point(targets[0].sprite.dx, targets[0].sprite.dy))/(sWidth/2));
        case Item: return 100;
      }
      return 100*numFrames;
    }
    
    public FightTimer getFightTimer(){
      return new ShadowTimer();
    }
    
    public void attackFrame(){
      AttackType.Skill attack = currSkill;  
      int moveX, moveY;
      switch(attack){
        case NONE:
          numFrames = (skilRect[0].width/sWidth);
          info = new SpriteInfo[2];
          int incMoves = Math.abs((int)calculateDistance(new Point(dx, dy), new Point(targets[0].sprite.dx, targets[0].sprite.dy))/(sWidth/2));
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
            //to clarify: these are not using directional sprites, only a single "kicking" motion sprite
            info[0] = new SpriteInfo(this, new Rectangle(18, 0, 18, 18), sWidth, new Dimension(moveX, moveY), incMoves, false);
            info[1] = new SpriteInfo(this, up, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
            break;
            case Right: x = 18;
            info[0] = new SpriteInfo(this, new Rectangle(72, 0, 18, 18), sWidth, new Dimension(moveX, moveY), incMoves, false);
            info[1] = new SpriteInfo(this, left, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
            break;
            case Up: x = 36;
            info[0] = new SpriteInfo(this, new Rectangle(108, 0, 18, 18), sWidth, new Dimension(moveX, moveY), incMoves, false);
            info[1] = new SpriteInfo(this, down, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
            break;
            case Left: x = 54;
            info[0] = new SpriteInfo(this, new Rectangle(162, 0, 18, 18), sWidth, new Dimension(moveX, moveY), incMoves, false);
            info[1] = new SpriteInfo(this, right, sWidth, new Dimension(-moveX, -moveY), incMoves, false);
            break;
          }
          y = 18;
          animTime = fframetime()*2;
          motionCounter = 0;
          moveCounter = 0;
          setInfo();
          break;
        case Hide: 
          isHiding = true;
          numFrames = 3;
          switch(determineDirection(new Point(targets[0].sprite.dx, targets[0].sprite.dy))){
            case Down: x = 0;
            break;
            case Right: x = 36;
            break;
            case Up: x = 72;
            break;
            case Left: x = 108;
            break;
          }
          y = 36;
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, new Rectangle(x, y, 36, 18), sWidth, new Dimension(0, 0), -1, false);
          animTime = fframetime();
          
          hiders = targets;
          numHiders = 1;
          setInfo();
          break;
        case Mass_Conceal: 
          isHiding = true;
          numFrames = 3;
          x = 72;
          y = 36;
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, new Rectangle(72, 18, 36, 18), sWidth, new Dimension(0, 0), -1, false);
          animTime = fframetime();
          
          hiders = targets;
          numHiders = numTargets;
          setInfo();
          break;
        case Release:
          numFrames = 1;
          x = 72;
          y = 36;
          info = new SpriteInfo[1];
          info[0] = new SpriteInfo(null, null, sWidth, new Dimension(0, 0), -1, false);
          animTime = fframetime()*3/2;
          motionCounter = 0;
          moveCounter = 0;
          isHiding = false;
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
      drawInFight();
      setMana(attack);
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
      if(isHiding)
        drawBars(dx, dy+sHeight/2, g);
    }
  
    public class ShadowTimer extends FightTimer{
      
      public void run(){ 
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
  
  public AttackType[] getSkills(){
    if(isHiding){
      AttackType[] skill = new AttackType[1];
      skill[0] = AttackType.RELEASE;
      return skill;
    }
    return super.getSkills();
  }
  
  public int numSkills(){
    return (isHiding) ? 1 : super.numSkills();
  }
    
  public void addStats(){
    stats.addOntoStat(new Stats(100, 100, 1, 1, 0.5f));
  }
  
  public void setStats(){
    stats = new Stats(100, 0, 0, 0, 0);
  }
  
  public void gradeNextBat(){
    set_NextBatExp( (int)(next_bat_exp()+100) ); 
  }
  public void gradeNextCom(){
    set_NextComExp( (int)(next_com_exp()+100) );
  }
  public void gradeNextMag(){
    set_NextMagExp( (int)(next_mag_exp()+100) );   
  }
  
  public boolean availableMana(AttackType.Skill type){
    if(isHiding && type != AttackType.Skill.Release)
      return false;
    switch(type){
      case Hide:
        if(magic() >= 5)
        return true;
      case Mass_Conceal: 
        if(magic() >= 15)
        return true;
      case Release:
        return true;
    }
    return false;
  }
  
  public void setMana(AttackType.Skill type){
    switch(type){
      case Hide:
        if(magic() >= 5)
        useMana(5);
        break;
      case Mass_Conceal:
        if(magic() >= 15)
        useMana(15);
        break;
    }
    if(isHiding){
      if(magic() <= 5 * numHiders){
        isHiding = false;
        useMana(5 * numHiders);
        for(int i = 0; i < numHiders; i++){
          hiders[i].decreaseConceal();
        }
      }
      else
        useMana(5 * numHiders);
    }
    return;
  }
  
  public DamageCalculation getDamageTimer(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
    return new ShadowDamage(battle, q, attack, inv);
  }
  
  public class ShadowDamage extends Battler.DamageCalculation{
    int tCounter = 0;
    public ShadowDamage(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
      super(battle, q, attack, inv);
    }
    
    public synchronized void run(){
      switch(currSkill){
        case NONE:
          targets[0].receiveDamage((int)atk.damage);
          btl.removePCDead(queue);
          btl.removeEnDead(queue);
          hitCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          break;
        case Hide: 
        case Mass_Conceal:
          Rectangle r = sprite.projectile[1];
          for(int i = 0; i < numHiders; i++)
            targets[i].addConceal(Shadow.this, sprite.img.getSubimage(r.x, r.y, r.width, r.height));
          hitCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          break;
        case Release: 
          for(int i = 0; i < numHiders; i++)
          hiders[i].decreaseConceal();
          break;
        case Item:
          inv.use(item, targets[0]);
          break;
      }
    }
    
  }
  
}
