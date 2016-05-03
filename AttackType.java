//package Game;

public class AttackType{
  public String v1;
  public Skill v2;
  public int numTargets;
  //public Sprite animation;
  private AttackType(String name, Skill skill, int num){
    v1 = name;
    v2 = skill;
    numTargets = num;
    //animation = anim;
  }
  
  public enum Skill{
    NONE, Lightning, MultiSlash, Roller, Crusher, Laser, GemGuard, Hide, Mass_Conceal, Drain, Release, Tongue, Grow, Guardian, Item, Wait;  
  }
    
  static AttackType DEFAULT = new AttackType("Fight", Skill.NONE, 1);
  static AttackType LIGHTNING = new AttackType("Lightning", Skill.Lightning, 1);
  static AttackType MULTISLASH = new AttackType("Multislash", Skill.MultiSlash, 1);
  static AttackType ROLLER = new AttackType("Roller", Skill.Roller, 1);
  static AttackType CRUSHER = new AttackType("Crusher", Skill.Crusher, 3);
  static AttackType LASER = new AttackType("Laser", Skill.Laser, 1);
  static AttackType GEM_GUARD = new AttackType("Gem Guard", Skill.GemGuard, 1);
  static AttackType HIDE  = new AttackType("Hide", Skill.Hide, 1);
  static AttackType MASS_CONCEAL = new AttackType("Conceal", Skill.Mass_Conceal, 3);
  static AttackType DRAIN = new AttackType("Drain", Skill.Drain, 2);
  static AttackType RELEASE = new AttackType("Release", Skill.Release, 0);
  static AttackType TONGUE = new AttackType("Tongue", Skill.Tongue, 1);
  static AttackType GROW = new AttackType("Grow", Skill.Grow, 0);
  static AttackType GUARDIAN = new AttackType("Guardian", Skill.Guardian, 1);
  static AttackType ITEM = new AttackType("Item", Skill.Item, 1);
  public static AttackType WAIT = new AttackType("Wait", Skill.Wait, 0);
  
  public static String findSkillName(Skill skill){
    switch(skill){
      case NONE: return "Fight";
      case Lightning: return "Lightning";
      case MultiSlash: return "Multislash";
      case Roller: return "Roller";
      case Crusher: return "Crusher";
      case Laser: return "Laser";
      case GemGuard: return "Gem Guard";
      case Hide: return "Hide";
      case Mass_Conceal: return "Mass Conceal";
      case Drain: return "Drain";
      case Release: return "Release";
      case Tongue: return "Tongue";
      case Grow: return "Grow";
      case Guardian: return "Guardian";
      case Item: return "Item";
      case Wait: return "Wait";
    }
    return " ";
  }
}