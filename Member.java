//package Game;

import java.awt.*;
import java.util.Arrays;

public abstract class Member extends Battler{
    
    public Member(String nm, AttackType[] attacks,int numAtks){
      name = nm;
      setHealth(300);
      setMaxHealth(300);
      attack = 20;
      defense = 0;
      setMagic(50);
      setMaxMagic(50);
      speed = 1.1f;
      skills = attacks;
      numSkills = numAtks;
    }
    
    public AttackType[] getSkills(){return Arrays.copyOfRange(skills, 1, numSkills);}
    public int numSkills(){return numSkills-1;}
    
    public void chooseSkill(AttackType.Skill skill){
      setAttack(skill);
      sprite.attackFrame();
      
    }
    

    
}