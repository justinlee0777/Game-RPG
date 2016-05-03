//package Game;
//import Game.Characters.*;

import java.awt.Image;
import java.util.TimerTask;

public abstract class Battler extends Object{
  public String name;
  public int level = 1;
  public int comLvl = 0, magLvl = 0; 
  private float battle_exp, next_bat_exp, combat_exp, next_com_exp, magic_exp, next_mag_exp; 
  private float incExp, expSlvr, expToAdd;
  private int health, maxHealth;
  public int attack, defense;
  private int magic, maxMagic;
  public float speed = 1.0f;
  public float counter = 0;
  //Stats
  public Stats stats;
  //
  public AttackType.Skill currSkill; 
  public Item item;
  public BattleSprite sprite;
  public AttackType[] skills;
  public int numSkills;
  public Battler[] targets;
  public int numTargets;
  //guard variables
  private int guardCounter = 0;
  //concealed variable
  private int concealCounter = 0;
  private boolean hidden = false;
  private Battler controller;
  //binded variable
  private boolean isBinded = false;
  //covered variable
  private Battler coverer = null; 
  private int coverCounter = 0;
  private boolean isGuardian = false;
  
  public float battle_exp(){return battle_exp;}
  public float next_bat_exp(){return next_bat_exp;}
  public float combat_exp(){return combat_exp;}
  public float next_com_exp(){return next_com_exp;}
  public float magic_exp(){return magic_exp;}
  public float next_mag_exp(){return next_mag_exp;}
  protected void set_BattleExp(int e){battle_exp = e;}
  protected void set_NextBatExp(int e){next_bat_exp = e;}
  protected void set_CombatExp(int e){combat_exp = e;}
  protected void set_NextComExp(int e){next_com_exp = e;}
  protected void set_MagicExp(int e){magic_exp = e;}
  protected void set_NextMagExp(int e){next_mag_exp = e;}
  public int health(){return health;}
  public int maxHealth(){return maxHealth;}
  protected void setHealth(int h){health = h;}
  protected void setMaxHealth(int h){maxHealth = h;}
  protected float percentOfHealth(){return health/maxHealth;}
  protected float percentOfFuncHealth(){return (float)health/stats.funcH;}
  public int magic(){return magic;}
  public int maxMagic(){return maxMagic;}
  protected void setMagic(int m){magic = m;}
  protected void setMaxMagic(int m){maxMagic = m;}
  protected float percentOfMagic(){return (float)magic/maxMagic;}
  protected float percentOfFuncMagic(){return (float)magic/stats.funcM;}
  
  abstract void chooseSkill(AttackType.Skill skill);
  abstract AttackType[] getSkills();
  abstract int numSkills();
  
  protected void set_AddExp(float incE, int e){
    incExp = incE;
    expSlvr = 0.0f;
    expToAdd = e;
  }
  //possibly make it return to boolean, remove loop, and accept increment parameters and such to make certain parts drawable
  protected boolean add_BattleExp(){
    if(battle_exp + incExp >= next_bat_exp){
      battle_exp+=(next_bat_exp-battle_exp);
      expSlvr+=(next_bat_exp-battle_exp);
      level++;
      addStats();
      gradeNextBat();
    }
    else if(expSlvr + incExp > expToAdd){
      battle_exp+=(expToAdd-expSlvr);
      expSlvr+=(expToAdd-expSlvr);
    }
    else{
      battle_exp+=incExp;
      expSlvr+=incExp;
    }
    return expSlvr < expToAdd;
  }
  protected boolean add_CombatExp(){
    if(combat_exp + incExp >= next_com_exp){
      combat_exp+=(next_com_exp-combat_exp);
      expSlvr+=(next_com_exp-combat_exp);
      comLvl++;
      addStats();
      gradeNextCom();
    }
    else if(expSlvr + incExp > expToAdd){
      combat_exp+=(expToAdd-expSlvr);
      expSlvr+=(expToAdd-expSlvr);
    }
    else{
      combat_exp+=incExp;
      expSlvr+=incExp;
    }
    return expSlvr < expToAdd;
  }
  protected boolean add_MagicExp(){
    if(magic_exp + incExp >= next_mag_exp){
      magic_exp+=(next_mag_exp-magic_exp);
      expSlvr+=(next_mag_exp-magic_exp);
      magLvl++;
      addStats();
      gradeNextMag();
    }
    else if(expSlvr + incExp > expToAdd){
      magic_exp+=(expToAdd-expSlvr);
      expSlvr+=(expToAdd-expSlvr);
    }
    else{
      magic_exp+=incExp;
      expSlvr+=incExp;
    }
    return expSlvr < expToAdd;
  }
  
  abstract void addStats();
  abstract void setStats();
  abstract void gradeNextBat();
  abstract void gradeNextCom();
  abstract void gradeNextMag();
  
  public void setAttack(AttackType.Skill s){
    currSkill = s;
  }

  public void setTargets(Battler[] bs, int num){
    targets = bs;
    numTargets = num;
  }
  
  public void handleStatus(){
    if(controller != null){
      if(!((Shadow)controller).isHiding){
        controller = null;
        concealCounter = 0;
        sprite.concealSprite = null;
        hidden  = false;
      }
    }
    if(coverCounter == 0){
      coverer = null;
      isGuardian = false;
    }
    else if(coverer != null && coverer.health() <= 0){
      coverer = null;
      isGuardian = false;
      coverCounter = 0;
    }
  }
  
  public void clearStatus(){
    isBinded = false;
    sprite.concealSprite = null;
    isGuardian = false;
    hidden = false;
  }
  
  public boolean isBinded(){return isBinded;}
  public void bind(){isBinded = true;}
  public void removeBind(){isBinded = false;}
  
  public boolean isAttackable(){return concealCounter == 0;}
  public boolean isHidden(){return hidden;}
    public void addConceal(Battler control, Image i){
    concealCounter++;
    controller = control;
    sprite.concealSprite = i;
    hidden = true;
    sprite.drawingConceal = true;
  }
  public void addConceal(Image i){
    concealCounter++;
    sprite.concealSprite = i;
    hidden = true;
  }
  public void decreaseConceal(){
    if(concealCounter > 0){
      if(--concealCounter == 0){
        controller = null;
        sprite.concealSprite = null;
        hidden = false;
      }
    }
  }
 
  public abstract boolean availableMana(AttackType.Skill type);
  
  public abstract void setMana(AttackType.Skill type);
  
  //make receiveDamage return int one day so that you can calculate excess drain health and so to know how much
  //mana to restore from Pierrot class
  public void receiveDamage(int dam){
    if(coverCounter > 0){
      coverer.receiveDamage(dam, isGuardian);
      coverer.sprite.setCoordinates(sprite.dx, sprite.dy+10);
      coverer.sprite.moveSprite(coverer.sprite.regForm, 10);
      coverCounter--;
      return;
    }
    if(guardCounter > 0){
      --guardCounter;
    }
    else{
      if(health - dam > stats.funcH)
        health = (int)stats.funcH;
      else if(dam < stats.funcDef)
        return;
      else{
        health -= (dam-stats.funcDef);
        sprite.hitFrame();
      }
    }
  }
  
  public void receiveDamage(int dam, boolean isGuard){
    int decrease = 1;
    if(isGuard)
      decrease = dam/Attack.getAttack(this, AttackType.Skill.Guardian).damage;
    if(health - decrease > stats.funcH)
      health = (int)stats.funcH;
    else if(decrease < stats.funcDef)
      return;
    else{
      health -= (decrease-stats.funcDef);
      sprite.hitFrame();
    }
  }
  
  public void useMana(int m){
    if(magic - m > stats.funcM)
      magic = (int)stats.funcM;
    else{
      magic -= m;
    }
  }
  
  //ok! Ideally the hitCounter should only go up 1 per charge. A isTargettable() method
  //should be implemented that collects many isPossible methods together once
  //many of those methods are necessary. Soon!
  public void addGuard(){
    guardCounter++;
  }
  
  public void removeGuard(){
    guardCounter = 0;
  }
  
  public boolean isCovered(){return coverer != null;}
  public void covered(Battler b, boolean guardianSkill){
    coverer = b;
    coverCounter = 1;
    isGuardian = guardianSkill;
  }
  
  public boolean isTargetable(AttackType.Skill skill){
    boolean b = isAttackable();
    if(skill == AttackType.Skill.GemGuard)
      b = b && guardCounter == 0;
    return b;
  }
  
  public class Stats{
    float hBoost, mBoost, atkBoost, defBoost, spdBoost; 
    float funcH, funcM, funcAtk, funcDef, funcSpd;
    
    public Stats(float h, float m, float ak, float df, float sd){
      hBoost = h;
      mBoost = m;
      atkBoost = ak;
      defBoost = df;
      spdBoost = sd;
    }
    
    public void addOntoStat(Stats sts){
      sts.funcH+=hBoost;
      sts.funcM+=mBoost;
      sts.funcAtk+=atkBoost;
      sts.funcDef+=defBoost;
      sts.funcSpd+=spdBoost; 
    }
    
    public void getFuncStats(Party prty){
      funcH = maxHealth;
      funcM = maxMagic;
      funcAtk = attack;
      funcDef = defense;
      funcSpd = speed;
      for(Party.Iterator itr = prty.begin(); !itr.end(); itr.increment())
        if(itr.key() != Battler.this)
        itr.key().stats.addOntoStat(this);
    }
  }
  
  protected synchronized void pause(TimerTask ta){ta.cancel();}
  
  public DamageCalculation getDamageTimer(Battle battle, Queue<Battler> q, Attack attack, Inventory inv){
    return new DamageCalculation(battle, q, attack, inv);
  }
  
  public class DamageCalculation extends TimerTask{
    Battle btl;
    Queue<Battler> queue;
    Attack atk;
    Inventory inv;
    int hitCounter = 0;
    public DamageCalculation(Battle battle, Queue<Battler> q, Attack attack, Inventory inventory){
      btl = battle;
      queue = q;
      atk = attack;
      inv = inventory;
    }
    
    public synchronized void run(){
      switch(currSkill){
        case NONE:
          targets[0].receiveDamage((int)atk.damage);
          targets[0].sprite.hitFrame();
          btl.removePCDead(queue);
          btl.removeEnDead(queue);
          hitCounter++;
          if(hitCounter >= atk.numOfHits){
            hitCounter = 0;
            pause(this);
          }
          break;
      }
      
    }
    
  }
  
}