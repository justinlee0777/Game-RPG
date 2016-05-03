//package Game;

import java.awt.*;
import java.util.*;

public abstract class Enemy extends Battler{
  protected static Random choosePlayer = new Random();
  protected static Random chooseSkill = new Random();
  protected Member[] pcs;
  protected Party pparty;
  protected Enemy[] allies;
  protected int numAllies;
  
  public Enemy(String nm, AttackType[] attacks,int numAtks){
    name = nm;
    setHealth(200);
    setMaxHealth(300);
    setMaxMagic(50);
    attack = 20;
    defense = 0;
    setMagic(50);
    setMaxMagic(50);
    speed = 1.0f;
    skills = attacks;
    numSkills = numAtks;    
  }
  
  public Enemy(String nm, int lvl, AttackType[] attacks,int numAtks){
    level = lvl;
    name = nm;
    setHealth(200);
    setMaxHealth(300);
    setMaxMagic(50);
    attack = 20;
    defense = 0;
    setMagic(50);
    setMaxMagic(50);
    speed = 1.0f;
    skills = attacks;
    numSkills = numAtks;    
  }
   
  public void fightAlgorithm(){
    Member c = choosePlayer();
    Member[] pc = new Member[1];
    pc[0] = c;
    if(c == null){
      setTargets(pc, 0); 
      chooseSkill(AttackType.WAIT.v2);
    }
    else{
      setTargets(pc, 1);
      chooseSkill(AttackType.Skill.NONE);
    }
  }
  
  public AttackType[] getSkills(){return Arrays.copyOfRange(skills, 1, numSkills);}
  public int numSkills(){return numSkills-1;}
  
  public void chooseSkill(AttackType.Skill skill){
    setAttack(skill);
    sprite.attackFrame();
  }
  
  public void getAllies(Enemy[] e, int eNum){
    allies = e;
    numAllies = eNum;
  }
  
  public Member choosePlayer(){
    boolean concealed = true;
    for(int i = 0; i < pparty.size(); i++)
      concealed = pcs[i].isHidden() && concealed;
    if(concealed)
      return null;
    Member pc = pcs[choosePlayer.nextInt(pparty.size())];
    if(pc.health() <= 0 || !pc.isAttackable())
      return choosePlayer();
    return pc;
  }
}