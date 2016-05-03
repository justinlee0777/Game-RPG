//package Game;
//import Game.Tutorial.*;

//import javax.swing.*;
import java.awt.*;
//import java.lang.Math;
import java.util.TimerTask;
import java.util.Timer;
//import java.lang.Thread;

public class BattleCommand extends Panel{
  //frame
  public Battle battle;
  //for distinguishing who acted
  private Turn actingTurn;
  //Timing
  private Timer time = new Timer();
  private BattleSprite.FightTimer act;
  private Battler.DamageCalculation dmgCal;
  private EndTurnTimer end;
  //Turn-based
  private Queue<Battler> bqueue;
  private TurnThread t;
  private volatile boolean pause = false;
  private volatile boolean skipped;
  private volatile boolean turnWait = false;
  private FieldCharacter fChar;
  private FieldEnemy fEnmy;
  //to Field
  protected volatile boolean finished = false;
 
  public BattleCommand(int destx, int desty, int wid, int hei, Field field, FieldCharacter fCh, FieldEnemy fEn, AnimatedScript[] pos){
    battle = new Battle(destx, desty, wid, hei, field, this, fCh, fEn, pos);
    fChar = fCh;
    fEnmy = fEn;
    bqueue = new Queue<Battler>();
    setFocusable(false);
    setVisible(true);  
  }
  
   
  public BattleCommand(int destx, int desty, int wid, int hei, Field field, FieldCharacter fCh, FieldEnemy fEn, AnimatedScript[] pos, boolean[] locks, int trns){
    battle = new ScriptedBattle(destx, desty, wid, hei, field, this, fCh, fEn, pos, locks, trns);
    fChar = fCh;
    fEnmy = fEn;
    bqueue = new Queue<Battler>();
    setFocusable(false);
    setVisible(true);  
  }
  
  public void beginTurns(){
    t = new TurnThread();
    t.start();
  }
  
  public class Turn extends Object{
    Battler battler;
    int tCounter = 0;
    boolean openFightMenu, wait;
    Attack atk;
    Item it;
    
    public Turn(Battler b){
      battler = b;
      wait = !(battler instanceof Enemy);
      openFightMenu = !(battler instanceof Enemy);
      battle.enableMenu(openFightMenu);
      if(battler instanceof Enemy) battler.sprite.standFrame(new Point(fChar.dx, fChar.dy));
      else battler.sprite.standFrame(new Point(fEnmy.dx, fEnmy.dy));
      if(battler instanceof Enemy)
        enemyAI();
      if(battler instanceof Member)
        battle.setCharacter((Member)b);
      turnWait = true;
      battle.fightMenu.setMenu(battler);
    }
    
    private synchronized void enemyAI(){
      ((Enemy)battler).fightAlgorithm();
    }
    
    public synchronized void startTurn(){
      if(!skipped){
        atk = Attack.getAttack(battler, battler.currSkill);   
        pause = true;
        battle.enableMenu(false);
        act = battler.sprite.getFightTimer();
        dmgCal = battler.getDamageTimer(battle, bqueue, atk, battle.fChar.pparty.inventory());
        end = new EndTurnTimer();
        resume(act, 100);
        resume(dmgCal, battler.sprite.fframetime(), battler.sprite.fframetime());
        resume(end, battler.sprite.animationTime(), battler.sprite.animationTime());
      }
    }
    
    public synchronized void endTurn(){
      if(!skipped){
        pause(act);
        pause(end);
        dmgCal = null;
        act = null;
        end = null;
      }
      battle.handlePCStatus();
      battle.handleEnStatus();
      if(battler instanceof Enemy) battler.sprite.standFrame(new Point(fChar.dx, fChar.dy));
      else battler.sprite.standFrame(new Point(fEnmy.dx, fEnmy.dy));
      battle.checkEnd();
      //if(battle.checkEnd()){matchEnd();}
      battle.fightMenu.pushNextTurn();
      skipped = false;
    }
    
    public synchronized void skipTurn(){
      pause = false;
      wait = false;
      skipped = true;
    }
  }
  
  private void fillQueue(){
    while(bqueue.size() < 8){
      bqueue.add(battle.progressTurn());
    }
    battle.getHeads(bqueue);
  }
  
  private void pause(TimerTask ta){ta.cancel();}
    
  private void resume(TimerTask ta, long delay){time.schedule(ta, delay, 100);}
  
  private void resume(TimerTask ta, long delay, long period){time.schedule(ta, delay, period);}
  
  public void matchEnd(){
    finished = true;
    //pause(at);
    //at = null;
    dmgCal = null;
    act = null;
    time.purge();
    time.cancel();
    time = null;
  }
  
  public void join(){
    //battle.join();
    battle = null;
    try{
      t.join();
    }catch(InterruptedException e){System.out.println("Interrupted Exception");}
  }
  
  public class EndTurnTimer extends TimerTask{
    public synchronized void run(){
      pause = false;
    }
  }
  
  //deadly bug--nullpointerexception on attack object in Turn, meaning (it seems) pause changes too quickly
  public class TurnThread extends Thread{
    public void run(){
      while(!finished){
        fillQueue();
        actingTurn = new Turn(bqueue.pop());
        while(actingTurn.wait){
          actingTurn.wait = battle.commandRetained();
          if(battle.skipped()){
            actingTurn.skipTurn();
          }
        } 
        synchronized(this){
          actingTurn.startTurn();
        }
        while(pause);
        actingTurn.endTurn();
        while(turnWait = battle.fightMenu.goNextTurn);
        if(battle.hasEnded)
          break;
      }
    }
  }
  
}