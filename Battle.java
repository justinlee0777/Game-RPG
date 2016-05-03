//package Game;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

//it might be necessary for all backgrounds to be the same size...
public class Battle extends Sprite implements KeyListener{
  //Move into position
  AnimatedScript form[];
  FieldCharacter fChar;
  FieldEnemy fEnmy;
  //beginning and ending
  private Field field;
  private BattleCommand turnPanel;
  protected boolean hasBegun, hasEnded = false;
  //
  private BufferedImage alphabet;
  private BufferedImage window;
  //Maybe delete the party object? Because it's a bunch o unnecessary data structures
  private PlayerParty pparty;
  private Party eparty;
  //targetting and enemies
  private Battler[] ep;
  private Battler[] targets;
  private int numTargets;
  private BufferedImage reticule;
  private volatile AttackType attack = AttackType.DEFAULT;
  //to check if pc has won or lost yet; is necessary for ScriptedBattle inherited
  protected boolean pcEnd;
  protected boolean enEnd;
  private boolean victory;
  //stages of command
  protected FightSelectable fightMenu;
  private volatile boolean menuOn, command, skip;
  //queue
  private Queue<Battler> heads;
  //prevent excess keyboard input
  private volatile boolean freePress = true;
  //turbo choice (always chooses default fight)
  private volatile boolean turbo = false;

  public Battle(int destx, int desty, int wid, int hei, Field fld, BattleCommand turn, FieldCharacter fCh, FieldEnemy fEn, AnimatedScript[] pos){
    dx = destx;
    dy = desty;
    width = wid;
    height = hei;
    field = fld;
    turnPanel = turn;
    try{
      alphabet = ImageIO.read(getClass().getResource("Sprites/Alphabet.png"));
      window = ImageIO.read(getClass().getResource("Sprites/Window.png"));
      reticule = ImageIO.read(getClass().getResource("Sprites/Reticule.png"));
    }catch(IOException e){JOptionPane.showMessageDialog(null, "Battle Cannot open file");}
    pparty = fCh.pparty;
    eparty = new Party();
    heads = new Queue<Battler>();
    fChar = fCh;
    fEnmy = fEn;
    ep = new Enemy[fEn.enemies.length];
    for(int i = 0; i < ep.length; i++){
      eparty.insert(fEn.enemies[i], true); 
      ep[i] = fEn.enemies[i];
    }
    form = pos;
    
    pparty.setTeamStats();
    eparty.setTeamStats();
    
    setIgnoreRepaint(false);
    fightMenu = new FightSelectable(0, height*3/4, width, height/4);
    setFocusable(true);
    addHierarchyListener(new RequestFocusListener());
    setBounds(dx, dy, width*(int)zoom.width, height*(int)zoom.height); 
  }
  
  public void keyPressed(KeyEvent e){
    if(freePress)
      fightMenu.keyPressed(e);
    freePress = false;
    e.consume();
  }
  public void keyReleased(KeyEvent e){
    fightMenu.keyReleased(e);
    freePress = true;
  }
  public void keyTyped(KeyEvent e){fightMenu.keyTyped(e);}
  
  
  public Battler[] getTargets(){return targets;}
  public int numOfTargets(){return numTargets;}
  public AttackType.Skill getAttack(){return attack.v2;}
  public PlayerParty getPCParty(){return pparty;}
  public void enableMenu(boolean open){
    menuOn = open;
    command = open;
  }
  public boolean commandRetained(){return command&&menuOn;}
  public boolean skipped(){return skip;}
  public void setCharacter(Member c){fightMenu.setCharacter(c);}
  public void getHeads(Queue<Battler> portraits){heads = portraits;}
  
  public void beginBattle(){
    if(hasBegun)
      return;
    boolean b = true;
    for(int i = 0; i < form.length; i++)
      b = b && form[i].hasStopped();
    if(!hasBegun && b){
      turnPanel.beginTurns();
      for(Party.Iterator pi = pparty.teamBegin(); !pi.end(); pi.increment())
        pi.key().sprite.standFrame(new Point(fEnmy.dx, fEnmy.dy));
      for(Party.Iterator ei = eparty.begin(); !ei.end(); ei.increment())
        ei.key().sprite.standFrame(new Point(fChar.dx, fChar.dy));
      hasBegun = true;
    }
  }
  
  public void endBattle(){
    if(!hasEnded)
      return;
    boolean b = true;
    //System.out.println("Length of form: " + form.length);
    for(int i = 0; i < form.length; i++){
      b = b && form[i].hasStopped();
    }
    if(hasEnded && b)
      turnPanel.matchEnd();
  }
  
  public Battler progressTurn(){
    Battler b = pparty.teamBegin().key();
    double counter = 0;
    for(Party.Iterator pi = pparty.teamBegin(); !pi.end(); pi.increment()){
      pi.key().counter+=pi.key().stats.funcSpd;
      if(pi.key().counter > counter && pi.value() == true){
        counter = pi.key().counter;
        b = pi.key();
      }
    }
    
    for(Party.Iterator ei = eparty.begin(); !ei.end(); ei.increment()){
      ei.key().counter+=ei.key().stats.funcSpd;
      if(ei.key().counter > counter && ei.value() == true){
        counter = ei.key().counter;
        b = ei.key();
      }
    }
    b.counter = 0f;
    return b;
  }
  
  public void handlePCStatus(){handleAllStatus(pparty.team());}
  public void handleEnStatus(){handleAllStatus(eparty);}
  
  private void handleAllStatus(Party p){
    for(Party.Iterator pi = p.begin(); !pi.end(); pi.increment()){
      pi.key().handleStatus();
    }
  }
  
  public void removePCDead(Queue<Battler> q){removeDead(q, pparty.team());}
  public void removeEnDead(Queue<Battler> q){removeDead(q, eparty);}
  
  private void removeDead(Queue<Battler> q, Party p){
    for(Party.Iterator pi = p.begin(); !pi.end(); pi.increment()){
      //pi.key().sprite.setHealth(pi.key().health());
      if(pi.key().health() <= 0){
        p.set(pi.key(), false);
        q.removeInstanceOf(pi.key());
      }
    }
  }
  
  public boolean allDead(Party p){
    boolean b = true;
    for(Party.Iterator iter = p.begin(); !iter.end(); iter.increment())
      b = b && iter.key().health() <= 0;
    return b;
  }
  
   //essentially moves all NPCs back first, then adds onto form the battling characters' returning 
  public void checkEnd(){
    pcEnd = pcEnd || allDead(pparty.team());
    enEnd = enEnd || allDead(eparty);
    
    if(pcEnd)
      victory = false;
    if(enEnd)
      victory = true;
    if(pcEnd || enEnd){
      AnimatedScript[] npcsPos = field.moveNPCsBack();
      form = (fEnmy.e != BattlerConstructor.EnemyType.Door) ? new AnimatedScript[npcsPos.length+2] : new AnimatedScript[npcsPos.length+1];
      for(int i = 0; i < npcsPos.length; i++)
        form[i] = npcsPos[i];
      NPC[] cs = new NPC[pparty.getTeamSize()];
      for(int i = 0; i < pparty.getTeamSize(); i++)
        cs[i] = pparty.getTeam()[i].sprite;
      form[npcsPos.length] = new AnimatedScript(cs, new Point(fChar.dx, fChar.dy));
      if(fEnmy.e != BattlerConstructor.EnemyType.Door){
        NPC[] es = new NPC[ep.length];
        for(int i = 0; i < ep.length; i++)
          es[i] = ep[i].sprite;
        form[npcsPos.length+1] = new AnimatedScript(es, new Point(fEnmy.dx, fEnmy.dy));
        //clear up all (graphical) status effects on battlers
        for(int i = 0; i < pparty.getTeam().length; i++)
          pparty.getTeam()[i].clearStatus();
        for(int i = 0; i < ep.length; i++)
          ep[i].clearStatus();
      }
      hasEnded = true;
    }
  }
  public boolean pcAsVictors(){return victory;}
  
  public void paintReticule(int destx, int desty, int w, int h, Graphics g){
    g.drawImage(reticule, destx, desty, destx+8, desty+8, 0, 0, 8, 8, null);
    g.drawImage(reticule, destx+w-8, desty, destx+w, desty+8, 8, 0, 16, 8, null);
    g.drawImage(reticule, destx, desty+h-8, destx+8, desty+h, 0, 8, 8, 16, null);
    g.drawImage(reticule, destx+w-8, desty+h-8, destx+w, desty+h, 8, 8, 16, 16, null);
  }
  
  public void paintWindow(int destx, int desty, int w, int h, Graphics g){
    //draw insides
    for(int i = 0; i < w; i+=16)
      for(int j = 0; j < h;j+=16)
      g.drawImage(window, destx+i, desty+j, destx+i+16, desty+j+16, 16, 16, 32, 32, null);
    //draw corners
    g.drawImage(window, destx, desty, destx+16, desty+16, 0, 0, 16, 16, null);
    g.drawImage(window, destx, desty+h-16, destx+16, desty+h, 0, 32, 16, 48, null);
    g.drawImage(window, destx+w-16, desty+h-16, destx+w, desty+h, 32, 32, 48, 48, null);
    g.drawImage(window, destx+w-16, desty, destx+w, desty+16, 32, 0, 48, 16, null);
    //draw top and bottom
    for(int i = 16; i < w-16; i+=16){
      g.drawImage(window, destx+i, desty, destx+i+16, desty+16, 16, 0, 32, 16, null);
      g.drawImage(window, destx+i, desty+h-16, destx+i+16, desty+h, 16, 32, 32, 48, null);
    }
    //draw sides
    for(int j = 16; j < h-16; j+=16){
      g.drawImage(window, destx, desty+j, destx+16, desty+j+16, 0, 16, 16, 32, null);
      g.drawImage(window, destx+w-16, desty+j, destx+w, desty+j+16, 32, 16, 48, 32, null);
    }
  }
  
  public void paintLetters(String line, int destx, int desty, int wid,Graphics g){
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      if(space+8 >= destx+wid){
        //index = 14 * 8;
        //draw period
        //g.drawImage(alphabet, space, desty+2, space+8, desty+16, index, 0, index+8, 16, null);
        break;
      }
      //add color at some point
      g.drawImage(alphabet, space, desty-1, space+8, desty+15, index, 0, index+8, 16, null);
      i++;
    }
  }
  
  public void paintLetters(String line, Color color, int destx, int desty, int wid, Graphics g){
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      if(space+8 >= destx+wid){
        //index = 14 * 8;
        //draw period
        //g.drawImage(alphabet, space, (desty+2), (space+8), (desty+16), index, 0, index+8, 16, color, null);
        break;
      }
      g.drawImage(alphabet, space, (desty-1), (space+8), (desty+15), index, 0, index+8, 16, color, null);
      i++;
    }
  }
  
  //rotational, for skill names
  public void paintWholeLetters(String line, Color color, double theta, int destx, int desty, Graphics2D g){
    AffineTransform oldXForm = g.getTransform();
    g.rotate(theta, destx, desty);
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      g.drawImage(alphabet, space, (desty-1), (space+8), (desty+15), 
                  index, 0, index+8, 16, color, null);
      i++;
    }
    g.setTransform(oldXForm);
  }
  
  public void paintWholeLetters(String line, int destx, int desty, Graphics g){
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      g.drawImage(alphabet, space, dy+(desty-1), (space+8), dy+(desty+15), 
                  index, 0, index+8, 16, null);
      i++;
    }
  }
  
  public void paintQueue(int destx, int desty, Graphics g){
    int space = destx;
    for(Queue<Battler>.Iterator itr = heads.begin(); !itr.end(); itr.increment()){
      /*
      Color color;
      if(itr.value() instanceof Character)
        color = Color.GREEN;
      else
        color = Color.RED;*/
      itr.value().sprite.drawPortrait(space, desty, g);
      space+=16;
    }
  }
  
  public void paintFightMenu(Graphics g){
    if(hasBegun)
      fightMenu.paint(g);
  }
  
  public void paint(Graphics g){
    beginBattle();
    endBattle();
    for(int i = 0; i < form.length; i++){
      if(form[i] != null && !form[i].hasStopped())
        form[i].paint(g);
    }
    for(Party.Iterator pi = pparty.teamBegin(); !pi.end(); pi.increment()){   
      pi.key().sprite.paint(g);
    }
    for(Party.Iterator ei = eparty.begin(); !ei.end(); ei.increment()){
      ei.key().sprite.paint(g);
    }
    if(fightMenu.selectingT){
      for(int i = 0; i < numTargets; i++){
        paintReticule(targets[i].sprite.dx-targets[i].sprite.sWidth/2, targets[i].sprite.dy-targets[i].sprite.sHeight/2, 
                       targets[i].sprite.sWidth, targets[i].sprite.sHeight, g);
      }
    }
  }
  
  public class FightSelectable extends Sprite{
    //Defining how the window looks
    private Battler character;
    private Battler nextChar;
    //options, colors to see which option is selected, counters for computer to know
    protected BufferedImage[] options;
    private Point[] d;
    protected int fCounter = 0;
    private AttackType[] cAttacks;
    private int skCounter = 0;
    private int itCounter = 0;
    //different modes
    private int tCounter = 0;
    private boolean selectingT = false; 
    //between parties
    private boolean partySelected = false;
    private Battler[] bp;
    private Party bparty;
    private boolean openSkillMenu = false;
    private boolean openItemMenu = false;
    //between turns
    protected boolean goNextTurn;
    private Point nextCharPoint;
    
    public FightSelectable(int destx, int desty, int w, int h){
      dx = destx;
      dy = desty;
      width = w;
      height = h;
      options = new BufferedImage[5];
      try{
        options[0] = ImageIO.read(getClass().getResource("Sprites/Fight.png"));
        options[1] = ImageIO.read(getClass().getResource("Sprites/Skills.png"));
        options[2] = ImageIO.read(getClass().getResource("Sprites/Tech.png"));
        options[3] = ImageIO.read(getClass().getResource("Sprites/Items.png"));
        options[4] = ImageIO.read(getClass().getResource("Sprites/Flee.png"));
      }catch(IOException e){JOptionPane.showMessageDialog(null, "Fight Menu: Cannot open file");}

      d = new Point[3];
      d[0] = new Point(width*2/3, dy);
      d[1] = new Point(width*2/3, dy+height/3);
      d[2] = new Point(width*2/3, dy+height*2/3);
      nextCharPoint = new Point(dx+width-16, dy+height-16);
    }
    
    public void setCharacter(Member c){
      if(hasEnded)
        return;
      character = c;
      cAttacks = c.getSkills();
      fCounter = 0;
      skCounter = 0;
      bp = ep;
      bparty = eparty;
      targets = null;
      attack = cAttacks[0];
      skip = false;
      if(turbo){
        if(character.isHidden() || character.isBinded()){
          attack = AttackType.WAIT;
          character.chooseSkill(attack.v2);
          skip = true;
          return;
        }
        while(bparty.find(bp[tCounter]).value == false){
          tCounter++;
          if(tCounter > bparty.size()){
            tCounter = 0;
          }
        }
        targets = new Battler[1];
        numTargets = 1;
        targets[0] = bp[tCounter];
        attack = AttackType.DEFAULT;       
        character.setTargets(targets, numTargets);
        character.chooseSkill(attack.v2);
        tCounter = 0;
        menuOn = false;
        openSkillMenu = false;
        selectingT = false;
        command = false;
      }
    }
    
    public void setMenu(Battler b){
      character = b;
      nextChar = heads.peek();
      goNextTurn = false;
      nextCharPoint = new Point(dx+width-16, dy+height-16);
    }
    
    public void keyPressed(KeyEvent e){
      if(!hasBegun||hasEnded)
        return;
      if(e.getKeyCode() == KeyEvent.VK_M){
        turbo = (turbo) ? false : true;
      }
      if(command){
        if(!character.isHidden() && !character.isBinded()){
          if(turbo){
            while(bparty.find(bp[tCounter]).value == false){
              tCounter++;
              if(tCounter > bparty.size()){
                tCounter = 0;
              }
            }
            targets = new Battler[1];
            numTargets = 1;
            targets[0] = bp[tCounter];
            attack = AttackType.DEFAULT;       
            character.setTargets(targets, numTargets);
            character.chooseSkill(attack.v2);
            tCounter = 0;
            menuOn = false;
            openSkillMenu = false;
            selectingT = false;
            command = false;
          }
          /////////////////////////////////////////////////////////////////
          else if(!(selectingT || openSkillMenu || openItemMenu)){
            if(e.getKeyCode() == KeyEvent.VK_W){
              fCounter = (fCounter < 1) ? 4 : fCounter-1;
            } 
            else if(e.getKeyCode() == KeyEvent.VK_S){
              fCounter = (fCounter > 3) ? 0 : fCounter+1;
            }
            else if(e.getKeyCode() == KeyEvent.VK_J){
              if(fCounter == 0){
                while(bparty.find(bp[tCounter]).value == false){
                  tCounter++;
                  if(tCounter > bparty.size()){
                    tCounter = 0;
                  }
                }
                targets = new Battler[1];
                numTargets = 1;
                targets[0] = bp[tCounter];
                attack = AttackType.DEFAULT;
                selectingT = true;
              }
              else if(fCounter == 1){
                if(!character.availableMana(cAttacks[skCounter].v2))
                  character.sprite.drawNoMana();  
                openSkillMenu = true;
              }
              else if(fCounter == 3){
                openItemMenu = true;
              }
            }
          }
          ///////////////////////////////////////////////////////////////////
          else if(selectingT){
            if(e.getKeyCode() == KeyEvent.VK_A && attack.numTargets < 3 && bp[0].isTargetable(attack.v2)){
              if(bparty.size() > 1){
                do{
                  tCounter--;
                  if(tCounter < 0){
                    tCounter = bparty.size()-1;
                  }
                }while(bparty.find(bp[tCounter]).value == false);
                targets[0] = bp[tCounter];
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_D && attack.numTargets < 3 && bp[1].isTargetable(attack.v2)){
              if(bparty.size() > 1){
                tCounter++;
                do{
                  if(tCounter >= bparty.size()){
                    tCounter = 0;
                  }
                }while(bparty.find(bp[tCounter]).value == false);
                targets[0] = bp[tCounter];
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_L){
              selectingT = false;
              targets = null;
              tCounter = 0;
              bp = ep;
              bparty = eparty;
              partySelected = false;
            }
            else if(e.getKeyCode() == KeyEvent.VK_J){
              if(bparty.find(targets[0]).value == true){
                character.setTargets(targets, numTargets);
                character.chooseSkill(attack.v2);
                tCounter = 0;
                menuOn = false;
                openSkillMenu = false;
                selectingT = false;
                command = false;
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_K){
              if(!partySelected){
                bp = pparty.getTeam();
                bparty = pparty.team();
              }
              else{
                bp = ep;
                bparty = eparty;
              }
              partySelected = !partySelected;
              int counter = 0;
              for(int i = 0; i < bparty.size(); i++){
                if(bparty.find(bp[tCounter]).value && bp[i].isTargetable(attack.v2) && counter < attack.numTargets){
                  targets[counter] = bp[i];
                  counter++;
                  tCounter=i;
                }
              }
              numTargets = counter;
            }
            
          }
          //////////////////////////////////////////////////////////////////////////////
          else if(openSkillMenu){
            if(e.getKeyCode() == KeyEvent.VK_W){
              if(skCounter > 1){
                skCounter-=2;
                if(!character.availableMana(cAttacks[skCounter].v2))
                  character.sprite.drawNoMana();  
                else
                  character.sprite.drawHasMana();
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_D){
              //System.out.println("D");
              if(skCounter < character.numSkills()-1){
                skCounter++;
                if(!character.availableMana(cAttacks[skCounter].v2))
                  character.sprite.drawNoMana();   
                else
                  character.sprite.drawHasMana();
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_S){
              if(skCounter < character.numSkills() - 2){
                skCounter+=2;
                if(!character.availableMana(cAttacks[skCounter].v2))
                  character.sprite.drawNoMana();   
                else
                  character.sprite.drawHasMana();
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_A){
              //System.out.println("A");
              if(skCounter > 0){
                skCounter--;
                if(!character.availableMana(cAttacks[skCounter].v2))
                  character.sprite.drawNoMana();   
                else
                  character.sprite.drawHasMana();
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_J && character.numSkills() > 0 && character.availableMana(cAttacks[skCounter].v2)){
              attack = cAttacks[skCounter];
              tCounter = 0;
              int counter = 0;
              targets = new Battler[attack.numTargets];
              selectingT = true;
              if(Attack.getAttack(character, attack.v2).damage <= 0){
                partySelected = true;
                bparty = pparty.team();
                bp = pparty.getTeam();
                for(int i = 0; i < bparty.size(); i++){
                  if(bparty.find(bp[tCounter]).value && bp[i].isTargetable(attack.v2) && counter < attack.numTargets){
                    targets[counter] = bp[i];
                    counter++;
                    tCounter=i;
                  }
                }
                numTargets = counter;
              }
              else{
                for(int i = 0; i < bparty.size(); i++){
                  if(bparty.find(bp[tCounter]).value && bp[i].isTargetable(attack.v2) && counter < attack.numTargets){
                    targets[counter] = bp[i];
                    counter++;
                    tCounter=i;
                  }
                }
                numTargets = counter;   
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_L){
              openSkillMenu = false;
              targets = null;
              character.sprite.drawHasMana();
            }
          }
          ///////////////////////////////////////////////////////////////
          else if(openItemMenu){
            if(e.getKeyCode() == KeyEvent.VK_W){
              if(itCounter > 1)
                itCounter-=2;
            }
            else if(e.getKeyCode() == KeyEvent.VK_D){
              if(itCounter < fChar.pparty.inventorySize() - 1)
                itCounter++;
            }
            else if(e.getKeyCode() == KeyEvent.VK_S){
              if(itCounter < fChar.pparty.inventorySize() - 2)
                itCounter+=2;
            }
            else if(e.getKeyCode() == KeyEvent.VK_A){
              if(itCounter > 0)
                itCounter--;
            }
            else if(e.getKeyCode() == KeyEvent.VK_J){
              int i = 0;
              for(Inventory.Iterator itr = fChar.pparty.getHeadItem(); !itr.end(); itr.increment()){
                if(i == itCounter)
                  character.item = itr.value();
                i++;
              }
              attack = AttackType.ITEM;
              tCounter = 0;
              numTargets = 1;
              targets = new Battler[attack.numTargets];
              selectingT = true;
              if(Attack.getAttack(character, attack.v2).damage <= 0){
                partySelected = true;
                bparty = pparty.team();
                bp = pparty.getTeam();
                int counter = 0;
                for(i = 0; i < bparty.size(); i++){
                  if(bparty.find(bp[tCounter]).value && bp[i].isTargetable(attack.v2) && counter < attack.numTargets){
                    targets[counter] = bp[i];
                    counter++;
                    tCounter=i;
                  }
                }
              }
            }
            else if(e.getKeyCode() == KeyEvent.VK_L)
              openItemMenu = false;
          }
        }
        else{
          if(cAttacks[0] == AttackType.RELEASE){
            //System.out.println(fCounter);
            if(e.getKeyCode() == KeyEvent.VK_W && !selectingT){
              fCounter = (fCounter < 1) ? 1 : 0;
            }
            else if(e.getKeyCode() == KeyEvent.VK_S && !selectingT){
              fCounter = (fCounter > 0) ? 0 : 1;
            }
            else if(e.getKeyCode() == KeyEvent.VK_J){
              targets = null;
              numTargets = 0;
              selectingT = true;
              if(fCounter == 0){
                attack = cAttacks[0];
                character.setTargets(targets, numTargets);
                character.chooseSkill(attack.v2);
              }
              else{
                attack = AttackType.WAIT;
                character.chooseSkill(attack.v2);
                skip = true;
              }
              menuOn = false;
              openSkillMenu = false;
              selectingT = false;
              command = false;
              partySelected = false;
            }
            else if(e.getKeyCode() == KeyEvent.VK_L){
              selectingT = false;   
            }
          }
          else{
            if(e.getKeyCode() == KeyEvent.VK_J){
              attack = AttackType.WAIT;
              character.chooseSkill(attack.v2);
              skip = true;
              menuOn = false;
              openSkillMenu = false;
              selectingT = false;
              command = false;
              partySelected = false;
            }
          }
        }
      }
    }
    
    public void keyReleased(KeyEvent e){
    }
    public void keyTyped(KeyEvent e){}
    
    public void pushNextTurn(){goNextTurn = true;}
    
    public void move(Point p, int mX, int mY){
      p.x += mX;
      p.y += mY;
    }
    
    public void paintCommands(Graphics g){
      if(fCounter == 0 || fCounter == 1){
        g.drawImage(options[0], dx+1, dy+1, dx+1+options[0].getWidth(), dy+1+options[0].getHeight(), 0, 0, options[0].getWidth(), options[0].getHeight(), null);
        g.drawImage(options[1], dx+1, dy+1+height/3, dx+1+options[1].getWidth(), dy+1+height/3+options[1].getHeight(), 0, 0, options[1].getWidth(), options[1].getHeight(), null);
        g.drawImage(options[2], dx+1, dy+1+height*2/3, dx+1+options[2].getWidth(), dy+1+height*2/3+options[2].getHeight(), 0, 0, options[2].getWidth(), options[3].getHeight(), null);
        //paintLetters("TECH", dx, dy+height*2/3, width, g);
        if(!openSkillMenu && !openItemMenu && !selectingT){
          if(fCounter == 0)
            paintReticule(dx, dy, width/3, height/3, g);
          else
            paintReticule(dx, dy+height/3, width/3, height/3, g);
        }
      }
      else if(fCounter == 2){
        g.drawImage(options[1], dx+1, dy+1, dx+1+options[1].getWidth(), dy+1+options[1].getHeight(), 0, 0, options[1].getWidth(), options[1].getHeight(), null);
        g.drawImage(options[2], dx+1, dy+1+height/3, dx+1+options[2].getWidth(), dy+1+height/3+options[2].getHeight(), 0, 0, options[2].getWidth(), options[2].getHeight(), null);
        g.drawImage(options[3], dx+1, dy+1+height*2/3, dx+1+options[3].getWidth(), dy+1+height*2/3+options[3].getHeight(), 0, 0, options[3].getWidth(), options[3].getHeight(), null);
        /*paintLetters("TECH", dx, dy+height/3, width, g);
        paintLetters("ITEMS", dx, dy+height*2/3, width, g);*/
        if(!openSkillMenu && !openItemMenu && !selectingT)
          paintReticule(dx, dy+height/3, width/3, height/3, g);
      }
      else if(fCounter == 3 || fCounter == 4){
        /*paintLetters("TECH", dx, dy, width, g);
        paintLetters("ITEMS", dx, dy+height/3, width, g);*/
        g.drawImage(options[2], dx+1, dy+1, dx+1+options[2].getWidth(), dy+1+options[2].getHeight(), 0, 0, options[2].getWidth(), options[2].getHeight(), null);
        g.drawImage(options[3], dx+1, dy+1+height/3, dx+1+options[3].getWidth(), dy+1+height/3+options[3].getHeight(), 0, 0, options[3].getWidth(), options[2].getHeight(), null);
        g.drawImage(options[4], dx+1, dy+1+height*2/3, dx+1+options[4].getWidth(), dy+1+height*2/3+options[4].getHeight(), 0, 0, options[4].getWidth(), options[4].getHeight(), null);
        if(!openSkillMenu && !openItemMenu && !selectingT){
          if(fCounter == 3)
            paintReticule(dx, dy+height/3, width/3, height/3, g);
          else
            paintReticule(dx, dy+height*2/3, width/3, height/3, g);
        }
      }
    }
    
    public void paintSkills(Graphics g){
      int w = width/3;
      int h = 0;
      if(cAttacks[0] == AttackType.RELEASE){
        paintWindow(dx+width/3, dy, width*2/3, height*2/3, g);
        paintWholeLetters(cAttacks[0].v1, width/3, this.dy+16,g);
        paintReticule(dx+width/3, dy, width*2/3, height*2/3, g);
      }
      else{
        for(int i = 0; i < character.numSkills(); i++){
          paintWindow(dx+w, dy+h, width/3, height/3, g);
          paintLetters(cAttacks[i].v1, dx+w+2, dy+h, width, g);
          if(skCounter == i){
            paintReticule(dx+w, dy+h, width/3, height/3, g);
          }
          w+=width/3;
          if(w >= width){
            w = width/3;
            h+=height/3;
          }
        }  
      } 
    }   
    
    public void paintItems(Graphics g){
      int w = width/3;
      int h = 0;
      int i = 0;
      for(Inventory.Iterator itr = fChar.pparty.getHeadItem(); !itr.end(); itr.increment()){
        paintWindow(dx+w, dy+h, width/3, height/3, g);
        paintLetters(itr.value().name, null, dx+w+2, dy+h, width, g);
        paintLetters(Integer.toString(itr.value().quantity), null, dx+w+width/3-12, dy+h, width, g);
        if(itCounter == i){
          paintReticule(dx+w, dy+h, width/3, height/3, g);
        }
        w+=width/3;
        if(w >= width){
          w = width/3;
          h+=height/3;
        }
        i++;
      }  
    }  
    
    public void paint(Graphics g){
      paintWindow(width/3, dy+height-17, width*2/3, 17, g);
      if(nextChar != null)
        nextChar.sprite.drawPortrait(nextCharPoint.x, nextCharPoint.y, g);
      if(goNextTurn){
        if(nextCharPoint.x <= (dx+width/3+1))
          goNextTurn = false;
        else
          move(nextCharPoint, -(dx+width/3+1)/5, 0);
        return;
      }
      if(character != null){
        character.sprite.drawPortrait(dx+width/3+1, dy+height-16, g);
      }
      if(!command)
        return;
      paintWindow(dx, dy, width/3, height, g);
      if(!character.isHidden() && !character.isBinded()){
        for(int i = 0; i < 3; i++){
          paintWindow(dx, dy+i*height/3, width/3, height/3, g);
        }
        paintCommands(g);
        if(openSkillMenu)
          paintSkills(g);
        if(openItemMenu)
          paintItems(g);
      }
      else{
        if(cAttacks[0] == AttackType.RELEASE){
          paintWindow(dx, dy, width/3, height/3, g);
          paintWindow(dx, dy+height/3, width/3, height/3, g);
          paintLetters(cAttacks[0].v1, dx, dy, width, g);
          paintLetters("WAIT", dx, dy+height/3, width, g);
          if(fCounter == 0)
            paintReticule(dx, dy, width/3, height/3, g);
          else
          paintReticule(dx, dy+height/3, width/3, height/3, g);
        }
        else{
          paintWindow(dx, dy, width/3, height*2/3, g);
          paintLetters("WAIT", dx, dy, width, g);
          paintReticule(dx, dy,  width/3, height*2/3, g);
        }
      }
    }
    
  }
  
}