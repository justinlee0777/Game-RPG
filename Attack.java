//package Game;

import java.util.Random;

public class Attack extends Object{
 private static Random rand = new Random();
 public int damage;
 public int numOfHits;
 
 public Attack(int d, int num){
   damage = d;
   numOfHits = num;
 }
 
  public static double getAttack(Battler b){
    return b.stats.funcAtk * (1 + rand.nextDouble());
  }
  
  public static Attack getAttack(Battler b, AttackType.Skill skill){
    switch(skill){
      case Lightning: return new Attack( (int)(b.stats.funcAtk * (1.5 + rand.nextDouble())), 1);
      case MultiSlash: return new Attack((int)(b.stats.funcAtk * (rand.nextDouble()+0.5)/2), 3);
      case Roller: return new Attack((int)(b.stats.funcAtk * 1.5 + rand.nextDouble()), 1);
      case Crusher: return new Attack((int)(b.stats.funcAtk), 3);
      case Laser: return new Attack((int)(b.stats.funcAtk * 1.75), 1);
      case GemGuard: return new Attack(0, 1);
      case Hide: return new Attack(0, 1);
      case Mass_Conceal: return new Attack(0, 1);
      case Drain: return new Attack((int)(b.stats.funcAtk * 0.5 + rand.nextDouble()), 2);
      case Tongue: return new Attack(0, 1);
      case Grow: return new Attack(0, 0);
      case Guardian: return new Attack((rand.nextInt(4))+1, 1);
      case Release: return new Attack(0, 0);
      case Item: return new Attack(0, 0);
      case Wait: return new Attack(0, 0);
      case NONE: 
    }
    return new Attack((int)b.stats.funcAtk, 1);
  }
  
}

