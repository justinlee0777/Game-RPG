//package Game;

import java.awt.*;

public class Incident extends Object{
  //distinguishing what incident does
  private boolean isMessage = false;
  private boolean isAnimation = false;
  //private boolean isCamera = false;
  protected Grid currGrid;
  //for messages
  private String message;
  private int w, h;
  private NPC npc;
  //for multiple messages
  private boolean isDialogue = false;
  private Incident[] dialogue;
  //for animation
  private Queue<Sprite> sequence;
  //for forced battles
  protected boolean isBattle = false;
  protected FieldEnemy en;
  //to stop paintings
  protected boolean stopPaint = false;
  protected Sprite sprite;
  //move character to places
  protected boolean moveDest = false;
  protected Story.Region region;
  protected Story story;
  protected String floor;
  //protected int gridNum;
  protected Point charPoint;
  protected NPC[] partyNPCs;
  //add experience to characters
  protected boolean addExp = false;
  protected BattleSprite.Exp expType;
  protected int expToAdd;
  //of a non-activating type
  protected boolean nonActivate = false;
  //change FieldEnemy object
  protected boolean[] locks;
  
  public Incident(String mess, int width, int height){
    message = mess;
    w = width;
    h = height;
    isMessage = true;
  }
  
  public Incident(NPC act, String mess, int width, int height){
    this(mess, width, height);
    npc = act;
  }
  
  public Incident(String mess, Grid grid){
    this(mess, 0, 0);
    currGrid = grid;
  }
  
  public Incident(NPC act, String mess, Grid grid){
    this(mess, grid);
    npc = act;
  }
  
  public Incident(boolean isConvo, Incident[] incidents){
    dialogue = incidents;
    isDialogue = isConvo;
    sequence = new Queue<Sprite>();
    isMessage = true;
  }
  
  public Incident(Sprite s, int disX, int disY, Rectangle rect){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(s, disX, disY, rect));
    isAnimation = true;
  }
  
  public Incident(Sprite s, int disX, int disY, int counter, Rectangle rect){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(s, disX, disY, counter, rect));
    isAnimation = true;
  }
  
    
  public Incident(Sprite s, int disX, int disY, int cycles, boolean jump, Rectangle rect){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(s, disX, disY, cycles, jump, rect));
    isAnimation = true;
  }
  
  public Incident(boolean sleep, NPC act, int disX, int disY, int cycles, Rectangle rect){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(sleep, act, disX, disY, cycles, rect));
    isAnimation = true;
  }
  
  public Incident(AnimatedScript script){
    sequence = new Queue<Sprite>();
    sequence.add(script);
    isAnimation = true;
  }
  
  public Incident(AnimatedScript scripts[]){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(scripts));
    isAnimation = true;
  }
  
  public Incident(Queue<Sprite> seq){
    sequence = seq;
    isAnimation = true;
  }
  
  public Incident(Sprite sp, boolean stop){
    sequence = new Queue<Sprite>();
    sequence.add(sp);
    sprite = sp;
    stopPaint = stop;
  }
  
  public Incident(FieldEnemy e, boolean battle){
    isBattle = battle;
    en = e;
  }
  
  //progress the Story
  public Incident(Story stry){
    story = stry;
    //if(story == null) System.out.println("not null");
    sequence = new Queue<Sprite>();
    nonActivate = true;
  }
  
  //alter FieldEnemy object
  public Incident(FieldEnemy e, boolean[] lcks){
    en = e;
    locks = lcks;
    sequence = new Queue<Sprite>();
    nonActivate = true;
  }
  
  public Incident(boolean expAdd, BattleSprite.Exp experience, int exp){
    sequence = new Queue<Sprite>();
    addExp = expAdd; 
    expToAdd = exp;
    expType = experience;
  }
  
  public Incident(Story stry, Story.Region rgn, String flr, /*Point cPoint,*/ NPC[] npcs){
    story = stry;
    region = rgn;
    floor = flr;
    //charPoint = cPoint;
    partyNPCs = npcs;
    sequence = new Queue<Sprite>();
    moveDest = true;
  }
  
  public Incident(Grid grid, int numCycles, float incX, float incY){
    sequence = new Queue<Sprite>();
    sequence.add(new AnimatedScript(grid, numCycles, incX, incY));
    isAnimation = true;
  }
  
  public boolean isMessage(){return isMessage;}
  public boolean isAnimation(){return isAnimation;}
  
  public Queue<Sprite> activate(Field field, Tile actor, FieldCharacter c){
    if(nonActivate){
      if(locks != null) //System.out.println("Incident: progress");
        en.locks = locks; 
      else if(story != null)
        story.progress();
      return sequence;
    }
    else if(isAnimation)
      return sequence;
    else if(stopPaint){
      sprite.stopPainting = stopPaint;
      return sequence;
    }
    else if(addExp){
      field.setExpScreen(expType, expToAdd);
      return sequence;
    }
    else if(moveDest){
      field.gameState = Field.GameState.inMap;
      field.map.setChars(partyNPCs);
      field.map.setDestination(region, floor/*, charPoint*/);
      //field.map.setDestination(story.getGrid(floor), gridNum, charPoint);
      return sequence;
    }
    //currGrid != null
    else if(isMessage){
      if(isDialogue){
        Sprite[] mess;
        for(int i = 0; i < dialogue.length; i++){
          if(dialogue[i].npc == null)
            mess = MessageBox.makeDialogue(0, (dialogue[i].currGrid.camera.height*2/3)-16, dialogue[i].currGrid.camera.width, dialogue[i].currGrid.camera.height/3, field, actor, dialogue[i].message, c);
          else
            mess = MessageBox.makeDialogue(0, (dialogue[i].currGrid.camera.height*2/3)-16, dialogue[i].currGrid.camera.width, dialogue[i].currGrid.camera.height/3, field, dialogue[i].npc, dialogue[i].message, c);
          for(int j = 0; j < mess.length; j++)
            sequence.add(mess[j]);
        }
        return sequence;
      }
      else{
        if(npc == null)
          return MessageBox.makeBoxes(0, (currGrid.camera.height*2/3)-16, currGrid.camera.width, currGrid.camera.height/3, field, actor, message, c);
        else
          return MessageBox.makeBoxes(0, (currGrid.camera.height*2/3)-16, currGrid.camera.width, currGrid.camera.height/3, field, npc, message, c);
      }
    }
    else if(npc == null)
      return MessageBox.makeBoxes(0, (h*2)-16, w, h, field, actor, message, c);
    return MessageBox.makeBoxes(0, (h*2)-16, w, h, field, npc, message, c);
  }

}
