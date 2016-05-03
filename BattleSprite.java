//package Game;

import java.awt.*;
import javax.swing.*;
//import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import java.awt.geom.Ellipse2D;

public abstract class BattleSprite extends NPC{
  public int bounds;
  protected int numFrames;
  protected Rectangle hitSprite;
  protected Rectangle[] skilRect;
  protected Rectangle[] projectile;
  protected int numSkills;
  //health; int health is to keep BattleSprite and Battler classes distinct
  //private int health, maxHealth, magic, maxMagic;
  private Battler battler;
  private float hInc, mInc, ebInc, ecInc, emInc;
  //private int numDigits;
  //private int[] hDigits;
  private BufferedImage hImg;
  private Rectangle death;
  //length of animations
  protected int animTime;
  //for battle
  public Rectangle headshot;
  public BufferedImage effects;
  public int outX;
  //for experience
  protected boolean expAdding;
  //draw stats
  public boolean drawNoMana = false;
  public boolean drawInFight = false;
  //various defense sprites
  public Image concealSprite;
  public boolean drawingConceal;
  public int sinkParam = 0;
  
  protected Point regForm;
  private MoveTimer m;
  private Timer t;
  //private int a = 0;
  
  //Sprite convention: First set is the collection of fight sprites, second is the hit sprite,
  //third is miscellany. ALL sheets must have a hit sprite
  //includes specifications for a "hit" animation
  
  //calculation time: the numFrames for "Fight" is the width to hitFrame's end / width
  //numFrames for "Skills" is the width of the Rectangles / width

  //public ActorSprite(String file, int destX, int destY, int sWid, int sHei, Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
  public BattleSprite(String file, int sW, int sH, Rectangle hit, Rectangle dead, Rectangle[] sk, Rectangle[] proj, int numSk, Rectangle pic, Battler bat, 
                      Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
    //super(file, 0, 0, sW, sH, d, r, u, l, slp);
    super(file, 0, 0, new ActorSprite(file, 0, 0, sW, sH, d, r, u, l, slp), null, "", pic); 
    hitSprite = hit;
    death = dead;
    skilRect = sk;
    projectile = proj;
    numSkills = numSk;
    headshot = pic;
    sWidth = sW;
    sHeight = sH;
    bounds = hitSprite.x;
    numFrames = hitSprite.x/sWidth;  
    setSize(sWidth, sHeight);
    //health
    try{
      hImg = ImageIO.read(getClass().getResource("Sprites/Health.png"));
      effects = ImageIO.read(getClass().getResource("Game/Effects.png"));
    }catch(IOException i){JOptionPane.showMessageDialog(null, "BattleSprite: Cannot open file");}
    //outX = 20;
    outX = 0;
    battler = bat;
    hInc = battler.health()/10;
    mInc = battler.magic()/10;
    regForm = new Point(dx, dy);
  }  
  // public NPC(String file, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle por){
  public BattleSprite(Image img, int sW, int sH, Rectangle hit, Rectangle dead, Rectangle[] sk, Rectangle[] proj, int numSk, Rectangle pic, Battler bat, 
                      Rectangle d, Rectangle r, Rectangle u, Rectangle l, Rectangle slp){
    //super(img, 0, 0, sW, sH, d, r, u, l, slp);
    super(img, 0, 0, new ActorSprite(img, 0, 0, sW, sH, d, r, u, l, slp), null, "", pic); 
    hitSprite = hit;
    death = dead;
    skilRect = sk;
    projectile = proj;
    numSkills = numSk;
    headshot = pic;
    sWidth = sW;
    sHeight = sH;
    bounds = hitSprite.x;
    numFrames = hitSprite.x/sWidth;  
    setSize(sWidth, sHeight);
    //health
    try{
      hImg = ImageIO.read(getClass().getResource("Sprites/Health.png"));
      effects = ImageIO.read(getClass().getResource("Game/Effects.png"));
    }catch(IOException i){JOptionPane.showMessageDialog(null, "BattleSprite: Cannot open file");}
    //outX = 20;
    outX = 0;
    battler = bat;
    hInc = battler.health()/10;
    mInc = battler.magic()/10;
    regForm = new Point(dx, dy);
  }  
  
  public BattleSprite(NPC npc, Rectangle hit, Rectangle dead, Rectangle[] sk, Rectangle[] proj, int numSk, Battler bat){
    this(npc.img, npc.sWidth, npc.sHeight, hit, dead, sk, proj, numSk, npc.headshot, bat, npc.down, npc.right, npc.up, npc.left, npc.sprite.sleep);
  }
  
  public void init(){
    setBounds(dx, dy, sWidth, sHeight);
    //createDHealth();
    setVisible(true);
  }
  
  //problematic; check
  public void setCoordinates(int destX, int destY){
    dx = destX;
    dy = destY;
    setBounds(dx, dy, sWidth, sHeight);
    init();
  }
  
  public void setCoordinates(Point p){
    dx = p.x;
    dy = p.y;
    setBounds(dx, dy, sWidth, sHeight);
    init();
  }
  
  public enum Exp{
    Battle, Combat, Magic;
  }
  
  public void set_AddExp(Exp experience, int e){
    switch(experience){
      case Battle:
        ebInc = (float)e/10;
        break;
      case Combat:
        ecInc = (float)e/10;
        break;
      case Magic:
        emInc = (float)e/10;
        break;
    }
  }
  //public void setHealth(int h){health = h;}
  //public void setMagic(int m){magic = m;}
  /*public void createDHealth(){
    hDigits = new int[1];
    numDigits = 1;
    if(health > 9){
      hDigits = new int[2];
      numDigits = 2;
      if(health > 99){
        hDigits = new int[3];
        numDigits = 3;
        hDigits[2] = (int)(Math.floor(health/100));
      }
      hDigits[1] = (int)(Math.floor((health%100)/10));
    }
    hDigits[0] = health%10;
  }*/
  
  public void setRegForm(Point p){
    regForm = p;
  }
  
  public Point calculateTarget(Sprite target){
    return new Point(dx, dy-sWidth/2-target.sHeight/2);
  }
  
  public abstract int fframetime();
  
  public int animationTime(){
    return animTime;
  }
  
  public void standFrame(){
    x = 0;
    y = 0;
    drawNotInFight();
  }
  
  public void standFrame(Point p){
    ActorSprite.Direction dir = ActorSprite.determineDirection(new Point(dx, dy), p);
    if(dir == ActorSprite.Direction.Down && down != null){
      x = down.x;
      y = down.y;
    }
    else if(dir == ActorSprite.Direction.Right && right != null){
      x = right.x;
      y = right.y;
    }
    else if(dir == ActorSprite.Direction.Up && up != null){
      x = up.x;
      y = up.y;
    }
    else if(left != null){
      x = left.x;
      y = left.y;
    }
    else{
      x = 0;
      y = 0;
    }
    drawNotInFight();
  }
  
  public void hitFrame(){
    x = hitSprite.x;
    y = hitSprite.y;
  }
    
  //helper function for certain classes with repeating sprites
  public void determineRepeatNum(Point p, Point[] directions, int[] numOfSprites, Dimension[] differences, Dimension sign){
    int sx = sign.width;
    int sy = sign.height;
    //straight vertical case
    if(sy > 0){
      while(p.y > directions[0].y){
        directions[0].y+=8*sy;
        numOfSprites[0]++;
      }
    }
    else{
      while(p.y < directions[0].y){
        directions[0].y+=8 * sy;
        numOfSprites[0]++;
      } 
    }
    differences[0] = new Dimension(p.x-directions[0].x, p.y-directions[0].y);
    //straight horizontal case
    if(sx > 0){
      while(p.x > directions[1].x){
        directions[1].x+=11 * sx;
        numOfSprites[1]++;
      }
    }
    else{
      while(p.x < directions[1].x){
        directions[1].x+=11 * sx;
        numOfSprites[1]++;
      } 
    }
    differences[1] = new Dimension(p.x-directions[1].x, p.y-directions[1].y);
    //northwest way
    if(sx > 0 && sy > 0){
      while(p.x > directions[2].x && p.y > directions[2].y){
        directions[2].x+=11 * sx;
        directions[2].y+=8 * sy;
        numOfSprites[2]++;
      }
    }
    else{
      while(p.x < directions[2].x && p.y < directions[2].y){
        directions[2].x+=11 * sx;
        directions[2].y+=8 * sy;
        numOfSprites[2]++;
      }
    }
    differences[2] = new Dimension(p.x-directions[2].x, p.y-directions[2].y);
    //northeast way
    if(sx < 0 && sy > 0){
      while(p.x < directions[3].x && p.y > directions[3].y){
        directions[3].x+=11 * sx;
        directions[3].y+=8 * sy;
        numOfSprites[3]++;
      }
    }
    else{
      while(p.x > directions[3].x && p.y < directions[3].y){
        directions[3].x+=11 * sx;
        directions[3].y+=8 * sy;
        numOfSprites[3]++;
      }
    }
    differences[3] = new Dimension(p.x-directions[3].x, p.y-directions[3].y);
  }
  
  public void drawNoMana(){drawNoMana = true;}
  public void drawHasMana(){drawNoMana = false;}
  public void drawInFight(){drawInFight = true;}
  public void drawNotInFight(){drawInFight = false;}
  
  public abstract FightTimer getFightTimer();
  
  public abstract void attackFrame();
  
  //implement skill levels sometime
  public void drawBars(int destx, int desty, Graphics g){
    int i = 0;
    for(float f = 0; f < battler.health(); f+=hInc){
      g.drawImage(hImg, destx+i, desty, destx+i+1, desty+3, 0, 0, 1, 3, this);  
      i++;
    }
    g.drawLine(destx, desty+3, destx+(int)(battler.stats.funcH/hInc), desty+3);  
    i = 0;
    for(float f = 0; f < battler.magic(); f+=mInc){
      g.drawImage(hImg, destx+i+2, desty+5, destx+i+3, desty+8, 10, 0, 11, 3, this);
      i++;
    }
    g.drawLine(destx+2, desty+8, destx+2+(int)(battler.stats.funcM/mInc), desty+8); 
    g.drawLine(destx, desty+3, destx+2, desty+8);
  }
  
  public void drawBars(int destx, int desty, int xLineOffset, int yLineOffset, Graphics g){
    int i = 0;
    for(float f = 0; f < battler.health(); f+=hInc){
      g.drawImage(hImg, destx+i, desty, destx+i+1, desty+3, 0, 0, 1, 3, this);  
      i++;
    }
    g.drawLine(destx, desty+3, destx+(int)(battler.stats.funcH/hInc), desty+3);  
    i = 0;
    for(float f = 0; f < battler.magic(); f+=mInc){
      g.drawImage(hImg, destx+i+xLineOffset, desty+5+xLineOffset, destx+i+3+yLineOffset, desty+8+yLineOffset, 10, 0, 11, 3, this);
      i++;
    }
    g.drawLine(destx+xLineOffset, desty+8+yLineOffset, destx+xLineOffset+(int)(battler.stats.funcM/mInc), desty+8+yLineOffset); 
    g.drawLine(destx, desty+3, destx+xLineOffset, desty+8);
  }
  
  public void drawExperience(Exp experience, int destx, int desty, Graphics g){
    int i = 0;
    float exp = 0.0f; 
    float expInc = 0.0f;
    float nextExp = 0.0f;
    switch(experience){
      case Battle: exp = battler.battle_exp();
      expInc = ebInc;
      nextExp = battler.next_bat_exp();
      break;
      case Combat: exp = battler.combat_exp();
      expInc = ecInc;
      nextExp = battler.next_com_exp();
      break;
      case Magic: exp = battler.magic_exp();
      expInc = emInc;
      nextExp = battler.next_mag_exp();
      break;
    }
    for(float f = 0; f < exp; f+=expInc){
      g.drawImage(hImg, destx+i, desty, destx+i+1, desty+3, 20, 0, 21, 3, this);  
      i++;
    }
    Color color = g.getColor();
    g.setColor(Color.WHITE);
    g.drawLine(destx, desty, destx+(int)(nextExp/expInc), desty);  
    g.drawLine(destx, desty, destx, desty+3);  
    g.drawLine(destx, desty+3, destx+(int)(nextExp/expInc), desty+3);  
    g.setColor(color);
    if(expAdding){
      switch(experience){
        case Battle: expAdding = battler.add_BattleExp();
        break;
        case Combat: expAdding = battler.add_CombatExp();
          break;
        case Magic: expAdding = battler.add_MagicExp();
          break;
      }
    }
    
  }
  
  /*public void drawHealth(int destx, int desty, Graphics g){
    int hDx, hSx;
    createDHealth();
    for(int i = 0; i < numDigits; i++){
      hDx = destx+ ((numDigits-i)*10);
      hSx = (10*hDigits[i])+10;
      g.drawImage(hImg, hDx, desty, hDx+10, desty+10, hSx, 0, hSx+10, 10, this);
    }
  }*/
  
  public void drawOutOfMana(Graphics g){
    g.drawImage(effects, dx-8, dy-sHeight/2-16, dx+8, dy-sHeight/2, outX, 0, outX+16, 16, null);
    outX+=16;
    if(outX >= 48)
      outX = 16;
  }
  
  /*public void drawGuard(Graphics g){
    if(guardSprite != null)
      g.drawImage(guardSprite, dx-sWidth/2, dy-sHeight/2, sWidth, sHeight, null);
  }*/
  
  public void drawConceal(Graphics g){
    if(drawingConceal){
      sinkParam+=sHeight/8;
      g.drawImage(img, dx-sWidth/2, dy-sHeight/2+sinkParam, dx+sWidth/2, dy+sHeight/2, x, y+sinkParam, x+sWidth, y+sHeight, this);
      if(sinkParam >= sHeight){
        sinkParam = 0;
        drawingConceal = false;
      }
    }
  }
  
  public void drawPortrait(int destx, int desty, Graphics g){
   //g.drawImage(portrait, destx, desty, destx+16, desty+16, 0, 0, 16, 16, this);
   g.drawImage(img, destx, desty, destx+16, desty+16, 
               headshot.x, headshot.y, headshot.x+headshot.width, headshot.y+headshot.height, this);
  }
  
  public void paint(Graphics g){
    if(battler.health() <= 0)
      g.drawImage(img, dx-sWidth/2, dy-sHeight/2, dx+sWidth/2, dy+sHeight/2, death.x, death.y, death.x+death.width, death.y+death.height, this);
    else{
      if(concealSprite == null){
        g.drawImage(img, dx-sWidth/2, dy-sHeight/2, dx+sWidth/2, dy+sHeight/2, x, y, x+sWidth, y+sHeight, this);
        //drawGuard(g);
        if(!drawInFight) drawBars(dx, dy+sHeight/2, g);
        if(drawNoMana) drawOutOfMana(g);
      }
      else{
        drawConceal(g);
        g.setColor(Color.BLACK);
        Ellipse2D.Double el = new Ellipse2D.Double(dx-sWidth/2, dy, sWidth, sHeight/2);
        ((Graphics2D)g).draw(el);
        ((Graphics2D)g).fill(el);
        g.setColor(Color.WHITE);
      }
        //drawConceal(g);
    }
  }
  
  public void moveSprite(Point p){
    if(m == null){
      m = new MoveTimer( (p.x-dx)/3, (p.y-dy)/3, 3, 3);
      t = new Timer();
      t.schedule(m, 0, 150);
    }
  }
  
  public void moveSprite(Point p, int periodicity){
    if(m == null){
      m = new MoveTimer( (p.x-dx)/periodicity, (p.y-dy)/periodicity, periodicity, periodicity);
      t = new Timer();
      t.schedule(m, 0, 150);
    }
  }
  
  public void moveSprite(Point p, int periodicity, Rectangle direction){
    if(m == null){
      m = new MoveTimer( (p.x-dx)/periodicity, (p.y-dy)/periodicity, periodicity, periodicity, direction);
      t = new Timer();
      t.schedule(m, 0, 150);
    }
  }
  
  private void killMoveTimer(){
    if(m != null){
      m.cancel();
      t.cancel();
      m = null;
      t = null;
    }
  }
  
  public class SpriteInfo{
    Sprite movingSprite;
    Rectangle movingBounds;
    int animMove;
    Dimension movement;
    int numMoves;
    boolean repeatSprite;
    public SpriteInfo(Sprite movSpr, Rectangle movBounds, int anChange, Dimension mvment, int nMoves, boolean repeat){
      movingSprite = movSpr;
      movingBounds = movBounds;
      animMove = anChange;
      movement = mvment;
      numMoves = nMoves;
      repeatSprite = repeat;
    }
  }
  
  //generalize class by adding Battler parameter?
  public class MoveTimer extends TimerTask{
    int countX, countY, mX, mY, limX, limY;
    Rectangle dir;
    public MoveTimer(int movX, int movY, int cycleX, int cycleY){
      mX = movX;
      mY = movY;
      limX = cycleX;
      limY = cycleY;
      countX = 0;
      countY = 0;
    }
    
    public MoveTimer(int movX, int movY, int cycleX, int cycleY, Rectangle direction){
      mX = movX;
      mY = movY;
      limX = cycleX;
      limY = cycleY;
      countX = 0;
      countY = 0;
      dir = direction;
      x = dir.x;
    }
    
    public synchronized void run(){
      if(countX == limX && countY == limY){
        standFrame();
        killMoveTimer();
      }
      else{
        dx+=mX;
        dy+=mY;
        countX++;
        countY++;
        if(dir != null){
          x+=sWidth;
          if(x >= dir.x+dir.width)
            x = dir.x;
        }
      }   
    }
    
  }
  public class FightTimer extends TimerTask{
    
    public void run(){ 
      x+=sWidth;
      if(x >= bounds){
        x = 0;
      }
    }  
    
  }
  
}
