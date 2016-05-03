import java.util.HashMap;

public class Story{
  
  private Queue<Change> env;
  private int story_set;
  
  private HashMap<String, Integer> all_states;
  
  /*a Map between Strings and Integers, which are the states of the game?*/
  /*incidents should petition for Grid and Story should return them
   */
  /*ok, make a set of grids corresponding to each Region
   * then update them whenever a change happens?
   * make a nested class, StoryChanges?
   */
  /*make all setEast, setWest, set...Grids abstract? to ensure petitioning?
   * will this not make everything intractable, making separate grids for every single room 
   */
  public enum Region{
    Dome, Tutorial, Tower;
  }
  
  public Story(FieldCharacter c){
    env = new Queue<Change>();
    story_set = 0;
    all_states = new HashMap<String, Integer>();
    setStory(c);
    init();
  }
  
  public Story(FieldCharacter c, int set){
    env = new Queue<Change>();
    story_set = set;
    all_states = new HashMap<String, Integer>();
    setStory(c);
    init();
  }
  
  public void init(){
    all_states.put("Bedroom", 1);
    all_states.put("Hallway", 0);
    all_states.put("Gym", 0);
    all_states.put("Classroom", 0);
    
    for(int i = 0; i < story_set; i++)
      progress();
  }
  
  public Grid getCurrGrid(FieldCharacter c){
    switch(story_set){
      case 3: return getGrid(c, "Classroom");
    }
    return getGrid(c, "Bedroom");
    //return new Bedroom(c, this, 1);
  }
  
  public Grid getGrid(FieldCharacter c, String str){
    Integer ste;
    if( (ste = all_states.get(str) ) == null) return null;
    switch(str){
      case "Bedroom": return new Bedroom(c, this, ste);
      case "Hallway": return new Hallway(c, this, ste);
      case "Gym": return new Gym(c, this, ste);
      case "Classroom": return new Classroom(c, this, ste);
    }
    return null;
  }
  
  public void progress(){
    (env.pop() ).activate();
  }
  
  public void setStory(FieldCharacter c){
    //env.add(new Change( new String[]{"Bedroom"}, new Integer[]{1} ));
    env.add(new Change( new String[]{"Hallway"}, new Integer[]{1} ));
    env.add(new Change( new String[]{"Gym"}, new Integer[]{1} ));
    env.add(new Change( new String[]{"Classroom"}, new Integer[]{1} ));
  }
 
  /*
  public Grid getRegion(Region region){
    return null;
  }
  */
  
  private class Change{
    /* Change keeps a list of Grids and a list of Integer changes;
     * these lists MUST correspond with each other.
     * When activated, the event will change these grids' states when created
     */
    String[] grids;
    Integer[] states;
    public Change(String[] grds, Integer[] stes){
      grids = grds;
      states = stes;
    }
    
    public void activate(){
      for(int i = 0; i < grids.length; i++){
        all_states.remove(grids[i]);
        all_states.put(grids[i], states[i]);
      }
    }
  }
  
}