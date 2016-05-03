//package Game;
//import Game.Characters.*;

import java.awt.*;
import java.util.*;

public class BattlerConstructor{
  static Random makeBattler = new Random();
  
  public enum Fighter{
    Gem, Shadow, Bruiser, Door, Frog, Bean, Pierrot;
  }
  
  public enum EnemyType{
    Door, Irritants;
  }
  
  public static Member makeAGoodBattler(Fighter f){
    AttackType[] skills;
    Rectangle[] skilRects, proj;
    switch(f){
      case Gem: 
        skills = new AttackType[3];
        skills[0] = AttackType.DEFAULT;
        skills[1] = AttackType.LASER;
        skills[2] = AttackType.GEM_GUARD;
        skilRects = new Rectangle[3];
        skilRects[0] = new Rectangle(0, 0, 20, 20);
        skilRects[1] = new Rectangle(0, 0, 20, 20);
        skilRects[2] = new Rectangle(0, 0, 20, 20);
        proj = new Rectangle[3];
        proj[0] = new Rectangle(0, 38, 35, 9);
        proj[1] = new Rectangle(36, 38, 30, 14);
        proj[2] = new Rectangle(0, 37, 35, 4);
        return null;
        //return new Gem(skills,skilRects, proj, 3);
      case Shadow: 
        skills = new AttackType[3];
        skills[0] = AttackType.DEFAULT;
        skills[1] = AttackType.HIDE;
        skills[2] = AttackType.MASS_CONCEAL;
        skilRects = new Rectangle[3];
        skilRects[0] = new Rectangle(0, 18, 144, 18);
        //skilRects[1] = new Rectangle(0, 32, 32, 32);
        //skilRects[2] = new Rectangle(0, 32, 32, 32);
        proj = new Rectangle[3];
        proj[0] = null;
        proj[1] = new Rectangle(144, 36, 18, 18);
        proj[2] = new Rectangle(144, 36, 18, 18);
        return new Shadow(skills, skilRects, proj, 3);
      case Bruiser: 
        skills = new AttackType[3];
        skills[0] = AttackType.DEFAULT;
        skills[1] = AttackType.ROLLER;
        skills[2] = AttackType.CRUSHER;
        skilRects = new Rectangle[3];
        skilRects[0] = new Rectangle(0, 20, 160, 20);
        skilRects[1] = new Rectangle(0, 40, 100, 20);
        skilRects[2] = new Rectangle(0, 40, 100, 20);
        proj = new Rectangle[3];
        proj[0] = null;
        proj[1] = null;
        proj[2] = null;
        return new Bruiser(skills, skilRects, proj, 3);
    }
    return null;
  }
  
  public static Enemy makeABadBattler(Fighter f, Member[] c, Party pp){
    return makeABadBattler(f, 1, c, pp);
  }
  
  public static Enemy makeABadBattler(Fighter f, int level, Member[] c, Party pp){
    AttackType[] skills;
    Rectangle[] skilRects, proj;
    switch(f){
      case Door: 
        return new Door(c, pp);
      case Frog: 
        skills = new AttackType[2];
        skills[0] = AttackType.DEFAULT;
        skills[1] = AttackType.TONGUE;
        skilRects = new Rectangle[2];
        skilRects[0] = new Rectangle(0, 0, 20, 20);
        skilRects[1] = new Rectangle(0, 0, 20, 20);
        proj = new Rectangle[2];
        proj[0] = new Rectangle(160, 20, 20, 20);
        proj[1] = new Rectangle(180, 24, 33, 16);
        return new Frog(level, skills, skilRects, proj, 2, c, pp);
      case Bean:
        skills = new AttackType[4];
        skills[0] = AttackType.DEFAULT;
        skills[1] = AttackType.DEFAULT;
        skills[2] = AttackType.GROW;
        skills[3] = AttackType.GUARDIAN;
        skilRects = new Rectangle[4];
        skilRects[0] = new Rectangle(0, 0, 20, 20);
        skilRects[1] = new Rectangle(0, 80, 60, 40);
        skilRects[2] = new Rectangle(0, 40, 80, 40);
        skilRects[3] = null;
        proj = new Rectangle[4];
        proj[0] = null;
        proj[1] = new Rectangle(62, 85, 16, 30);
        proj[2] = null;
        proj[3] = new Rectangle(0, 120, 20, 40);
        return new Bean(skills, skilRects, proj, 4, c, pp);
      case Pierrot: 
        skills = new AttackType[2];
        skills[0] = AttackType.DEFAULT;
        skills[0] = AttackType.DRAIN;
        skilRects = new Rectangle[2];
        skilRects[0] = new Rectangle(0, 21, 84, 21);
        skilRects[1] = new Rectangle(0, 21, 21, 21);
        proj = new Rectangle[2];
        proj[0] = null;
        proj[1] = new Rectangle(85, 21, 10, 10);
        return new Pierrot(skills, skilRects, proj, 2, c, pp);
    }
    return null;
  }
  
  public static Enemy[] makeEnemies(BattlerConstructor.Fighter[] enmies, PlayerParty pp){
    Enemy[] battlers = new Enemy[enmies.length];
    if(enmies.length > 3)
      return null;
    for(int i = 0; i < enmies.length; i++)
      battlers[i] = BattlerConstructor.makeABadBattler(enmies[i], pp.getTeam(), pp.team());
    return battlers;
  }

  public static Enemy[] makeEnemies(BattlerConstructor.Fighter[] enmies, int level, PlayerParty pp){
    Enemy[] battlers = new Enemy[enmies.length];
    if(enmies.length > 3)
      return null;
    for(int i = 0; i < enmies.length; i++)
      battlers[i] = BattlerConstructor.makeABadBattler(enmies[i], level, pp.getTeam(), pp.team());
    return battlers;
  }
  
  public static Enemy[] makeEnemies(EnemyType e, PlayerParty pp){
    Enemy[] battlers;
    switch(e){
      case Door:
        battlers = new Enemy[1];
        battlers[0] = BattlerConstructor.makeABadBattler(BattlerConstructor.Fighter.Door, pp.getTeam(), pp.team());
        return battlers;
      case Irritants:
        battlers = new Enemy[3];
        battlers[0] = BattlerConstructor.makeABadBattler(BattlerConstructor.Fighter.Frog, pp.getTeam(), pp.team());
        battlers[1] = BattlerConstructor.makeABadBattler(BattlerConstructor.Fighter.Pierrot, pp.getTeam(), pp.team());
        battlers[2] = BattlerConstructor.makeABadBattler(BattlerConstructor.Fighter.Bean, pp.getTeam(), pp.team());
        for(int i = 0; i < 3; i++)
          battlers[i].getAllies(battlers, 3);
        return battlers;
    }
    return null;
  }
  /*
  public static int numEnemies(EnemyType e){
    if(e == null)
      System.out.println("null");
    switch(e){
      case Door: return 1;
      case Irritants: return 3;
    }
    return 0;
  }
  */
  /*public static Character makeRandomCharacter(){
   int r = makeBattler.nextInt(4);
   if(r == 0)
   return makeAGoodBattler(Fighter.Gyro);
   else if(r == 1)
   return makeAGoodBattler(Fighter.Wind);
   else if(r == 2)
   return makeAGoodBattler(Fighter.Flame);
   else if(r == 3)
   return makeAGoodBattler(Fighter.Knight);
   return null;
   }
   
   public static Enemy makeRandomEnemy(){
   int r = makeBattler.nextInt(4);
   if(r == 0)
   return makeABadBattler(Fighter.Gyro);
   else if(r == 1)
   return makeABadBattler(Fighter.Wind);
   else if(r == 2)
   return makeABadBattler(Fighter.Flame);
   else if(r == 3)
   return makeABadBattler(Fighter.Knight);
   return null;
   }*/
}
