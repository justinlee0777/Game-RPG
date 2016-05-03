//package Game;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.image.BufferedImage;
  
//convention: ...may have to draw complete fields with per unit tile sizes
//complicated, innit?
public class Field extends Sprite implements KeyListener{
  protected FieldCharacter c; 
  private int enemyNumber;
  private int mveSpd;
  protected BufferedImage backbuffer;
  private BufferedImage black;
  private Graphics backg;
  
  protected Story story;
  protected Grid currGrid;
  protected Rectangle drawFrame;
  
  //Battle
  private BattleCommand gPanel;
  private AnimatedScript[] pos;
  
  //Cutscene
  private Sprite currentScript; 
  private Queue<Sprite> script;
  public Cutscene cutscene;
  
  //Map
  protected Map map;
  
  //Experience
  private BattleSprite.Exp expType;
  private boolean passExpScreen = true;
  private BufferedImage gradeCard;
  
  //party menu
  private BufferedImage window, alphabet, reticule;
  private PlayerParty pp;
  private boolean partyMenuOn = false;
  private int partyCounter = 0;
  private boolean selectMenuOn = false;
  private int selCounter = 0;
  private boolean chooseMenuOn = false;
  private int porCounter = 0;
  
  //formation menu          
  private boolean orderMenuOn = false; 
  private boolean switchChoose = false;
  private int switchPor;
  
  //item menu
  private boolean itemMenuOn = false;
  private int itCounter = 0;
  
  
  //
  private boolean inBattle = false;
  private boolean inField = true;
  private boolean inCutscene = false;
  protected boolean inMap = false;
  private boolean inExp = false;

  
  private GameState gameState;
  
  public Field(FieldCharacter sprite, PlayerParty pparty){
    c = sprite;
    pp = pparty;
    mveSpd = 1;
    try{
      window = ImageIO.read(getClass().getResource("Sprites/Window.png"));
      alphabet = ImageIO.read(getClass().getResource("Sprites/Alphabet.png"));
      reticule = ImageIO.read(getClass().getResource("Sprites/Reticule.png"));
      black = ImageIO.read(getClass().getResource("Sprites/Black.png"));
      gradeCard = ImageIO.read(getClass().getResource("Sprites/Card.png"));
    }catch(IOException e){JOptionPane.showMessageDialog(null, "Cannot open file");}
    addKeyListener(this);
    setFocusable(true);
    addHierarchyListener(new RequestFocusListener());
    story = new Story(sprite, 0);
    setCurrGrid(story.getCurrGrid(sprite));
    //currGrid = story.getCurrGrid(sprite);

    width = 144;
    height = 192;
    //setIgnoreRepaint(false);
    //menus
    setBounds(0, 0, 144*(int)zoom.width, 192*(int)zoom.height);
    drawFrame = new Rectangle(0, 0, 144*(int)zoom.width, 192*(int)zoom.height);
    SwitchThread t = new SwitchThread();
    t.start();
    gameState = GameState.inField;
    map = new Map(this, sprite);

  }
  
  public enum GameState { 
    inBattle, inField, inCutscene, inMap, inExp;
  }
  
  public synchronized void keyPressed(KeyEvent e){
    if( gameState.equals(GameState.inExp) ){
      if(e.getKeyCode() == KeyEvent.VK_J){
        boolean b = false;
        for(int i = 0; i < c.pparty.getTeam().length; i++)
          b = b || c.pparty.getTeam()[i].sprite.expAdding;
        passExpScreen = !b;
      }
      return;
    }
    if( gameState.equals(GameState.inBattle) ){
      gPanel.battle.keyPressed(e);
      return;
    }
    if( gameState.equals( GameState.inCutscene) ){
      cutscene.keyPressed(e);
      return;
    }
    if(currentScript != null){
      if(e.getKeyCode() == KeyEvent.VK_J  && currentScript instanceof MessageBox){
        currentScript = script.pop();
      }
    }
    else{
      ///////////////////////////////////////////////////////////
      if(!partyMenuOn){
        //int cam_shftx = 0;
        //int cam_shfty = 0;
        if(c.isSleeping()){}
        else if(e.getKeyCode() == KeyEvent.VK_I) mveSpd = (mveSpd > 1) ? 1 : 2;
        else if(e.getKeyCode() == KeyEvent.VK_D){
          if(c.isDir(ActorSprite.Direction.Right) == false)
            c.setDirection(ActorSprite.Direction.Right); 
          c.dx+=mveSpd;
          //System.out.println(c.dx + " " + (currGrid.width+currGrid.oX - c.sWidth));
          if(c.dx >= currGrid.width+currGrid.oX - c.sWidth && currGrid.getEastGrid(c, story) != null){
            currGrid.orderResume();
            setCurrGrid(currGrid.getEastGrid(c, story));
            //currGrid = currGrid.getEastGrid(c, story);
            c.setCoordinates(c.cNode, currGrid.drawTree, currGrid.westP);
            currGrid.orderResume();
            //setCutscene(currGrid);
            //System.out.println(c.dx + " " + c.dy);
          }
          else if(c.dx > currGrid.width+currGrid.oX - c.sWidth || !currGrid.isPassable(c)){
            c.dx-=mveSpd;
          }
          else{
            //c.requestUpdate(currGrid.drawTree);
            //cam_shftx = 2;
          }
       
          c.forward();
        }
        else if(e.getKeyCode() == KeyEvent.VK_A){
          if(c.isDir(ActorSprite.Direction.Left) == false)
            c.setDirection(ActorSprite.Direction.Left);
          c.dx-=mveSpd;
          if(c.dx - c.sWidth/2 <= 0 && currGrid.getWestGrid(c, story) != null){
            currGrid.orderResume();
            setCurrGrid(currGrid.getWestGrid(c, story));
            //currGrid = currGrid.getWestGrid(c, story);
            c.setCoordinates(c.cNode, currGrid.drawTree, currGrid.eastP);
            currGrid.orderResume();
            //setCutscene(currGrid);
          }
          else if(c.dx - c.sWidth/2 < 0 || !currGrid.isPassable(c)){
            c.dx+=mveSpd;
          }
          else{
            //c.requestUpdate(currGrid.drawTree);
            //cam_shftx = -2;
          }
          
          c.forward();
        }
        else if(e.getKeyCode() == KeyEvent.VK_W){
          if(c.isDir(ActorSprite.Direction.Up) == false)
            c.setDirection(ActorSprite.Direction.Up);
          c.dy-=mveSpd;
          if(c.dy - c.sHeight/2 <= 0 && currGrid.getNorthGrid(c, story) != null){
            currGrid.orderResume();
            setCurrGrid(currGrid.getNorthGrid(c, story));
            c.setCoordinates(c.cNode, currGrid.drawTree, currGrid.southP);
            currGrid.orderResume();
            //setCutscene(currGrid);
          }
          else if(c.dy - c.sHeight/2 < 0 || !currGrid.isPassable(c)){
            c.dy+=mveSpd;
          }
          else{
            //c.requestUpdate(currGrid.drawTree);
            //cam_shfty = -2;
          }
          
          c.forward();
        } 
        else if(e.getKeyCode() == KeyEvent.VK_S){
          if(c.isDir(ActorSprite.Direction.Down) == false)
            c.setDirection(ActorSprite.Direction.Down);
          c.dy+=mveSpd;
          if(c.dy >= currGrid.height+currGrid.oY - c.sHeight && currGrid.getSouthGrid(c, story) != null){
            currGrid.orderResume();
            setCurrGrid(currGrid.getSouthGrid(c, story));
            //currGrid = currGrid.getSouthGrid(c, story);
            c.setCoordinates(c.cNode, currGrid.drawTree, currGrid.northP);
            currGrid.orderResume();
            //setCutscene(currGrid);
          }
          else if(c.dy > currGrid.height+currGrid.oY - c.sHeight || !currGrid.isPassable(c)){
            c.dy-=mveSpd;
          }
          else{
            //c.requestUpdate(currGrid.drawTree);
            //cam_shfty = 2;
          }
          
          c.forward(); 
        }
        else if(e.getKeyCode() == KeyEvent.VK_J){
          IncidentTile t = currGrid.handleIncidents(c);
          if(t == null) return;
          script = t.use(this, t, c);
          if(script == null) return;
          currentScript = script.pop();
          return;
        }
        else if(e.getKeyCode() == KeyEvent.VK_K){
          partyMenuOn = true;
        }
        //c.updateBox();
        currGrid.setCamera(c);
        //handles scripted tiles
        ScriptedTile t = currGrid.handleScripts(c);
        if(t == null)
          return;
        setCutscene(t);
      }
      /////////////////////////////////////////////////////////////////////
      else if(partyMenuOn && !(orderMenuOn || itemMenuOn)){
        if(e.getKeyCode() == KeyEvent.VK_W){
          partyCounter = (partyCounter < 1) ? 2 : partyCounter-1;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_S){
          partyCounter = (partyCounter > 1) ? 0 : partyCounter+1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_J){
          if(partyCounter == 0){
            orderMenuOn = true;
          }
          else if(partyCounter == 2){
            itemMenuOn = true;
          }
        }
        else if(e.getKeyCode() == KeyEvent.VK_L){
          partyMenuOn = false;
        }
      }
      /////////////////////////////////////////////////////////////////
      else if(orderMenuOn && !selectMenuOn){
        if(e.getKeyCode() == KeyEvent.VK_A){
          porCounter = (porCounter < 1) ? pp.size() - 1 : porCounter-1;
          if(switchChoose && porCounter == switchPor)
            porCounter = (porCounter < 1) ? pp.size() - 1 : porCounter-1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
          porCounter = (porCounter >= pp.size()-1) ? 0 : porCounter+1;
          if(switchChoose && porCounter == switchPor)
            porCounter = (porCounter >= pp.size()-1) ? 0 : porCounter+1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_J){
          if(switchChoose && !selectMenuOn){
            selectMenuOn = true;
          }
          else if(!selectMenuOn){
            switchChoose = true;
            switchPor = porCounter;
            porCounter = (porCounter >= pp.size()-1) ? 0 : porCounter+1;
          }
        }
        else if(e.getKeyCode() == KeyEvent.VK_L){
          if(switchChoose){
            switchChoose = false;
            selCounter = 0;
          }
          else{
            orderMenuOn = false;
            partyCounter = 0;
            porCounter = 0;
          }
        }
      }
      else if(itemMenuOn && !(selectMenuOn || chooseMenuOn)){
        if(e.getKeyCode() == KeyEvent.VK_W){
          itCounter = (itCounter > 1) ? itCounter-2 : itCounter;
        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
          itCounter = (itCounter < c.pparty.inventorySize()-1) ? itCounter+1 : itCounter;
        }
        else if(e.getKeyCode() == KeyEvent.VK_S){
          itCounter = (itCounter < c.pparty.inventorySize()-2) ? itCounter+2 : itCounter;
        }
        else if(e.getKeyCode() == KeyEvent.VK_A){
          itCounter = (itCounter > 0) ? itCounter-1 : itCounter;
        }
        else if(e.getKeyCode() == KeyEvent.VK_J){
          chooseMenuOn = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_L){
          itemMenuOn = false;
          porCounter = 0;
        }
      }
      /////////////////////////////////////////////////////////////////////////
      else if(chooseMenuOn && !selectMenuOn){
        if(e.getKeyCode() == KeyEvent.VK_A){
          porCounter = (porCounter < 1) ? pp.size() - 1 : porCounter-1;
          if(switchChoose && porCounter == switchPor)
            porCounter = (porCounter < 1) ? pp.size() - 1 : porCounter-1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
          porCounter = (porCounter >= pp.size()-1) ? 0 : porCounter+1;
          if(switchChoose && porCounter == switchPor)
            porCounter = (porCounter >= pp.size()-1) ? 0 : porCounter+1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_J){
          if(itemMenuOn && !selectMenuOn){
            selectMenuOn = true;
          }
        }
        else if(e.getKeyCode() == KeyEvent.VK_L){
          if(itemMenuOn && !selectMenuOn){
            chooseMenuOn = false;
            porCounter = 0;
          }
        }
      }
      /////////////////////////////////////////////////////////////////////////
      else if(selectMenuOn){
        if(e.getKeyCode() == KeyEvent.VK_W){
          selCounter = (selCounter < 1) ? 1 : 0;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_S){
          selCounter = (selCounter > 0) ? 0 : 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_J && selCounter == 0){
          if(switchChoose){
            pp.setOrder(switchPor, porCounter);
            switchChoose = false;
            selectMenuOn = false;
            selCounter = 0;
          }
          if(itemMenuOn){
            Item it = null;
            int i = 0;
            for(Inventory.Iterator itr = c.pparty.getHeadItem(); !itr.end(); itr.increment()){
              if(i == itCounter)
                it = itr.value();
              i++;
            }
            c.pparty.useItem(it, c.pparty.getPosBattler(porCounter));
            selectMenuOn = false;
            chooseMenuOn = false;
            porCounter = 0;
            selCounter = 0;
          }
        }
        else if(e.getKeyCode() == KeyEvent.VK_L || (e.getKeyCode() == KeyEvent.VK_J && selCounter == 1)){
          if(switchChoose){
            switchChoose = false;
            selectMenuOn = false;
            selCounter = 0;
          }
          if(itemMenuOn){
            selectMenuOn = false;
            selCounter = 0;
          }
        }
      }
    }
  }
  
  public void keyReleased(KeyEvent e){
    if( gameState.equals(GameState.inBattle) ){    
      gPanel.battle.keyReleased(e);
      return;
    }
    else if( !gameState.equals(GameState.inCutscene) )
      c.reset();
    
  }
  
  public void keyTyped(KeyEvent e){
    if( gameState.equals(GameState.inBattle) ){    
      gPanel.battle.keyTyped(e);
      return;
    }
  }
  
  public void setCurrGrid(Grid grid){
    currGrid = grid;
    //backbuffer = (BufferedImage)createImage(width, height);
    //repaint();
    setCutscene(currGrid);
  }
  
  public void setCutscene(Cutscene scene){
    if(scene != null){
      cutscene = scene;
      gameState = GameState.inCutscene;
    }
  }
  
  public void setCutscene(Grid g){
    setCutscene(g.getCutscene(this, /*backbuffer,*/ c));
  }
  
  public void setCutscene(ScriptedTile t){
    setCutscene(t.use(this, backbuffer, c));
  }
  
  public void handleCutscene(){
    if(cutscene.isFinished){
      cutscene = null;
      currGrid.removeTempSprite();
      gameState = GameState.inField;
    }
  }
  
  public Grid getGrid(FieldCharacter c, String grd){ return story.getGrid(c, grd); }
  
  public FieldEnemy getEnemies(){
    if(checkEnemyHit())
      return currGrid.e[enemyNumber];
    else
      return cutscene.fEn;
  }
  
  public void removeEncounter(){
    if(checkEnemyHit())
      deleteEncounter();
    else
      cutscene.outBattle();
  }
  
  public void deleteEncounter(){
    currGrid.deleteEncounter(enemyNumber);
  }
  
  public boolean checkEnemyHit(){
    for(int i = 0; i < currGrid.e.length; i++){
      if(currGrid.e[i] != null && currGrid.e[i].collided){
        enemyNumber = i;
        return true;
      }
    }
    return false;
  }
  
  public void setExpScreen(BattleSprite.Exp experience, int expToAdd){
    for(int i = 0; i < c.pparty.getTeam().length; i++){
      c.pparty.getTeam()[i].set_AddExp(expToAdd/10.0f, expToAdd);
      c.pparty.getTeam()[i].sprite.set_AddExp(experience, expToAdd);
      c.pparty.getTeam()[i].sprite.expAdding = true;
    }
    passExpScreen = false;
    expType = experience;
    gameState = GameState.inExp;
  }
  
  public boolean forceBattle(){
    if(cutscene == null) 
      return false; 
    return cutscene.forceBattle();
  }

  public void makeBattle(FieldEnemy fEn){
    if(!fEn.isScripted)
      gPanel = new BattleCommand((int)currGrid.camera.x, (int)currGrid.camera.y, (int)width, (int)height, this, c, fEn, pos);
    else
      gPanel = new BattleCommand((int)currGrid.camera.x, (int)currGrid.camera.y, (int)width, (int)height, this, c, fEn, pos, fEn.locks, fEn.turns);
  }
  
  public AnimatedScript[] moveNPCsBack(){
    int size = 0;
    for(int i = 0; i < currGrid.promptInc.size(); i++)
      if(currGrid.promptInc.valAt(i) instanceof ActorTile)
      size++;
    AnimatedScript[] animScr = new AnimatedScript[size];
    for(int i = 0; i < currGrid.promptInc.size(); i++){
      if(!(currGrid.promptInc.valAt(i) instanceof ActorTile))
        continue;
      animScr[i] = currGrid.promptInc.valAt(i).returnOverlap();
    }
    return animScr;
  }
  
  public AnimatedScript[] moveNPCs(){
    Grid cg = currGrid;
    int size = 0;
    for(int i = 0; i < currGrid.promptInc.size(); i++)
      if(currGrid.promptInc.valAt(i) instanceof ActorTile)
      size++;
    int j = 0;
    AnimatedScript[] animScr = new AnimatedScript[size];
    for(int i = 0; i < currGrid.promptInc.size(); i++){
      if(!(currGrid.promptInc.valAt(i) instanceof ActorTile))
        continue;
      ActorSprite act = ((ActorTile)cg.promptInc.valAt(i)).sprite;
      int distToCover = (Math.abs(cg.camera.x - cg.promptInc.valAt(i).dx) < Math.abs(cg.camera.x + cg.camera.width - cg.promptInc.valAt(i).dx)) 
        ? (int)(cg.camera.x - cg.promptInc.valAt(i).dx - act.sWidth) : (int)(cg.camera.x + cg.camera.width - cg.promptInc.valAt(i).dx + act.sWidth);
      animScr[j] = new AnimatedScript(cg.promptInc.valAt(i), distToCover, 0, 
                                      (distToCover > 0) ? act.getWalking(ActorSprite.Direction.Right) : act.getWalking(ActorSprite.Direction.Left));
      j++;
    }
    return animScr;
  }
  
  public void getBattlePositions(FieldEnemy en){
    Tile[][] tiles = currGrid.tiles;
    AnimatedScript[] cPos = new AnimatedScript[c.pparty.getTeamSize()];
    AnimatedScript[] ePos = (en.e != BattlerConstructor.EnemyType.Door) ? new AnimatedScript[en.enemies.length] : new AnimatedScript[0];
    
    int tempX;
    int tempY;
    c.init();
    en.init();
    en.makeEnemies(c.pparty);

    Dimension signs = ActorSprite.dirCoefficients(ActorSprite.determineDirection(new Point(en.dx, en.dy), new Point(c.dx, c.dy)));
    int sx = signs.width;
    int sy = signs.height;
    //8 refers to half the size of the tiles (16-width, 16-height)
    int counter = 1;
    Rectangle r = new Rectangle(c.dx, c.dy, c.sWidth, c.sHeight);   
    while(cPos[0] == null){
      tempX = (int)Math.floor(r.x/16);
      tempY = (int)Math.floor(r.y/16);
      if(counter == 3){
        cPos[0] = new AnimatedScript(c.pparty.getTeam()[0].sprite, new Point(r.x+(sx*8), r.y+(sy*8)), 
                                     c.pparty.getTeam()[0].sprite.getWalking(ActorSprite.determineDirection(new Point(c.dx, c.dy), new Point(r.x+(sx*8), r.y+(sy*8)))));
        c.pparty.getTeam()[0].sprite.setRegForm(new Point(r.x+(sx*8), r.y+(sy*8) ));
      }
      else{
        if(!tiles[tempX][tempY].isPassable()){}
        else{
          if(r.x >= currGrid.camera.x+20 && r.x <= currGrid.camera.x+currGrid.camera.width-40)
            r.x+=(sx*8);
          if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
            r.y+=(sy*4);
        }
        counter++;
      }
    }
    
    if(c.pparty.getTeamSize() > 1){
      counter = 1;
      r = new Rectangle(c.dx, c.dy, c.sWidth, c.sHeight);
      while(cPos[1] == null){
        tempX = (int)Math.floor(r.x/16);
        tempY = (int)Math.floor(r.y/16);
        if(counter == 3){
          cPos[1] = new AnimatedScript(c.pparty.getTeam()[1].sprite, new Point(r.x, r.y+(sy*8) ), 
                                       c.pparty.getTeam()[1].sprite.getWalking(ActorSprite.determineDirection(new Point(c.dx, c.dy), new Point(r.x, r.y+(sy*8)))));
          c.pparty.getTeam()[1].sprite.setRegForm(new Point(r.x, r.y+(sy*8) ));
      }
        else{
          if(!tiles[tempX][tempY].isPassable()){}
          else{
            if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
              r.y+=(sy*8);
          }
          counter++;
        }
      }
    }
    
    if(c.pparty.getTeamSize() > 2){
      counter = 1;
      r = new Rectangle(c.dx, c.dy, c.sWidth, c.sHeight);
      while(cPos[2] == null){
        tempX = (int)Math.floor(r.x/16);
        tempY = (int)Math.floor(r.y/16);
        if(counter == 3){
          cPos[2] = new AnimatedScript(c.pparty.getTeam()[2].sprite, new Point(r.x+(sx*8), r.y+(sy*8) ), 
                                       c.pparty.getTeam()[2].sprite.getWalking(ActorSprite.determineDirection(new Point(c.dx, c.dy), new Point(r.x+(sx*8), r.y+(sy*8)))));
          c.pparty.getTeam()[2].sprite.setRegForm(new Point(r.x+(sx*16), r.y+(sy*8) ));
        }
        else{
          if(!tiles[tempX][tempY].isPassable()){}
          else{
            if(r.x >= currGrid.camera.x+20 && r.x <= currGrid.camera.x+currGrid.camera.width-40)
              r.x+=(sx*8);
            if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
              r.y+=(sy*4);
          }
          counter++;
        }
      }
    }

    signs = ActorSprite.dirCoefficients(ActorSprite.reverseDirection(ActorSprite.determineDirection(new Point(en.dx, en.dy), new Point(c.dx, c.dy))));
    sx = signs.width;
    sy = signs.height;
    
    if(en.e != BattlerConstructor.EnemyType.Door){
      counter = 1;
      r = new Rectangle(en.dx, en.dy, en.sWidth, en.sHeight);
      while(ePos[0] == null){
        tempX = (int)Math.floor(r.x/16);
        tempY = (int)Math.floor(r.y/16);
        if(counter == 3){
          ePos[0] = new AnimatedScript(en.enemies[0].sprite, new Point(r.x+(sx*8), r.y+(sy*8) ), 
                                       en.enemies[0].sprite.getWalking(ActorSprite.determineDirection(new Point(en.dx, en.dy), new Point(r.x+(sx*8), r.y+(sy*8)))));   
          en.enemies[0].sprite.setRegForm(new Point(r.x+(sx*8), r.y+(sy*8) ));
        }
        else{
          if(!tiles[tempX][tempY].isPassable()){}
          else{
            if(r.x >= currGrid.camera.x+20 && r.x <= currGrid.camera.x+currGrid.camera.width-40)
              r.x+=(sx*8);
            if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
              r.y+=(sy*4);
          }
          counter++;
        }
      }
      
      if(en.enemies.length > 1){
        counter = 1;
        r = new Rectangle(en.dx, en.dy, en.sWidth, en.sHeight);
        while(ePos[1] == null){
          tempX = (int)Math.floor(r.x/16);
          tempY = (int)Math.floor(r.y/16);
          if(counter == 3){
            ePos[1] = new AnimatedScript(en.enemies[1].sprite, new Point(r.x, r.y+(sy*8) ), 
                                         en.enemies[1].sprite.getWalking(ActorSprite.determineDirection(new Point(en.dx, en.dy), new Point(r.x, r.y+(sy*8)))));
            en.enemies[1].sprite.setRegForm(new Point(r.x, r.y+(sy*8) ));
          }
          else{
            if(!tiles[tempX][tempY].isPassable()){}
            else{
              if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
                r.y+=(sy*8);
            }
            counter++;
          }
        } 
      }
      
      if(en.enemies.length > 2){
        counter = 1; 
        r = new Rectangle(en.dx, en.dy, en.sWidth, en.sHeight);
        while(ePos[2] == null){
          tempX = (int)Math.floor(r.x/16);
          tempY = (int)Math.floor(r.y/16);
          if(counter == 3){
            ePos[2] = new AnimatedScript(en.enemies[2].sprite, new Point(r.x+(sx*8), r.y+(sy*8) ), 
                                         en.enemies[2].sprite.getWalking(ActorSprite.determineDirection(new Point(en.dx, en.dy), new Point(r.x+(sx*8), r.y+(sy*8)))));
            en.enemies[2].sprite.setRegForm(new Point(r.x+(sx*16), r.y+(sy*16) ));
          }
          else{
            if(!tiles[tempX][tempY].isPassable()){}
            else{
              if(r.x >= currGrid.camera.x+20 && r.x <= currGrid.camera.x+currGrid.camera.width-40)
                r.x+=(sx*8);
              if(r.y >= currGrid.camera.y + 60 && r.y <= currGrid.camera.y + currGrid.camera.height*3/4)
                r.y+=(sy*4);
            }
            counter++;
          }
        }
      }
    }
    AnimatedScript[] npcPos = moveNPCs();
    pos = new AnimatedScript[cPos.length + ePos.length + npcPos.length];
    int j = 0;
    for(int i = 0; i < c.pparty.getTeamSize(); i++){
      if(cPos[i] != null){
        pos[j] = cPos[i];
        j++;
      }
    }
    for(int i = 0; (en.e != BattlerConstructor.EnemyType.Door) && i < en.enemies.length; i++){
      if(ePos[i] != null){
        pos[j] = ePos[i];
        j++;
      }
    }
    for(int i = 0; i < npcPos.length; i++){
      pos[j] = npcPos[i];
      j++;
    }
  }
  
  public void orderPause(){
    currGrid.orderPause();
  }
  
  public void orderResume(){    
    currGrid.orderResume();
  }
  
  public void paintWindow(int destx, int desty, int w, int h, Graphics g){
    //draw insides
    for(int i = 0; i < w; i+=16)
      for(int j = 0; j < h; j+=16)
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
  
  public void paintLetters(String line, Color color, int destx, int desty, int wid, Graphics g){
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      if(space+8 >= destx+wid){
        break;
      }
      g.drawImage(alphabet, space, (desty-1), (space+8), (desty+15), index, 0, index+8, 16, color, null);
      i++;
    }
  }
  
  public void paintReticule(int destx, int desty, int w, int h, Graphics g){
    g.drawImage(reticule, destx, desty, destx+8, desty+8, 0, 0, 8, 8, null);
    g.drawImage(reticule, destx+w-8, desty, destx+w, desty+8, 8, 0, 16, 8, null);
    g.drawImage(reticule, destx, desty+h-8, destx+8, desty+h, 0, 8, 8, 16, null);
    g.drawImage(reticule, destx+w-8, desty+h-8, destx+w, desty+h, 8, 8, 16, 16, null);
  }
  
  public void paintExp(Graphics g){
    int m = 0;
    switch(expType){
      case Battle: m = 0;
      break;
      case Combat: m = 1;
      break;
      case Magic: m = 2;
      break;
    }
    g.drawImage(black, 0, 0, width, height, 0, 0, black.getWidth(), black.getHeight(), null);
    paintWindow(16, -16, width-32, 64, g);
    paintLetters("Grade Report", null, 24, 14, width-32, g);
    g.drawImage(gradeCard, width/2-23, 0, width/2+23, 14, m*46, 0, (m+1)*46, 14, null);
    c.pparty.getTeam()[0].sprite.drawPortrait(width/2-16, height/3-8, g);
    c.pparty.getTeam()[0].sprite.drawExperience(expType, width/2, height/3, g);
    if(c.pparty.getTeam().length > 1){
      c.pparty.getTeam()[1].sprite.drawPortrait(width/3-16, height/2-8, g);
      c.pparty.getTeam()[1].sprite.drawExperience(expType, width/3, height/2, g);
    }
    if(c.pparty.getTeam().length > 2){
      c.pparty.getTeam()[2].sprite.drawPortrait(width*2/3-16, height/2-8, g);
      c.pparty.getTeam()[2].sprite.drawExperience(expType, width*2/3, height/2, g);
    }
  }
  
  public void paintStatusScreen(int desty, Graphics g){
    paintWindow(0, desty, width*2/3, height*1/4, g);
    Member[] team = pp.getTeam();
    for(int i = 0; i < pp.getTeamSize(); i++){
      Member p = team[i];
      p.sprite.drawPortrait(0, desty + height*i/12, g);
      p.sprite.drawBars(20, desty+ height*i/12 + height/36, g);
    } 
  }
  
  public void paintPartyMenu(int desty, Graphics g){
    //all three option windows
    paintWindow(width*2/3, desty, width/3, height/12, g);
    paintWindow(width*2/3, desty+height/12, width/3, height/12, g);
    paintWindow(width*2/3, desty+height/6, width*1/3, height/12, g);
    paintLetters("Formation", null, width*2/3, desty, width/3, g);
    paintLetters("Characters", null, width*2/3, desty+height/12, width/3, g);
    paintLetters("Items", null, width*2/3, desty+height/6, width/3, g);
    paintReticule(width*2/3, desty+partyCounter*height/12, width/3, height/12, g);
  }
  
  public void paintItems(int desty, Graphics g){
    paintWindow(0, desty, width*2/3, height*1/4, g);
    int w = 0;
    int h = desty;
    int i = 0;
    for(Inventory.Iterator itr = c.pparty.getHeadItem(); !itr.end(); itr.increment()){
      paintWindow((dx+w), (dy+h), width/3, height/12, g);
      paintLetters(itr.value().name, null, dx+w+2, dy+h, width, g);
      paintLetters(Integer.toString(itr.value().quantity), null, dx+w+width/3-12, dy+h, width, g);
      if(itCounter == i){
        paintReticule(dx+w, dy+h, width/3, height/12, g);
      }
      w+=width/3;
      if(w >= width*2/3){
        w = 0;
        h+=height/12;
      }
      i++;
    }   
  }  
  
  public void paintCharacterChoice(boolean menuType, int desty, Graphics g){
    paintWindow(0, desty, width*2/3, height*1/4, g);
    paintWindow(5, desty+height/16, width/2, height/6, g);
    Member[] all = pp.getAll(); 
    for(int i = 0; i < pp.size(); i++){
      Member p = all[i];
      if(menuType == orderMenuOn && switchChoose && i == switchPor){
        paintWindow((i*width*2/9), desty-10, width*2/9, height/4, g);
        p.sprite.drawPortrait((i*width*2/9)+width/9-10, desty-6, g);
        p.sprite.drawBars((i*width*2/9)+2, desty+14, 0, 0, g);
      }
      else{
        paintWindow((i*width*2/9), desty, width*2/9, height/4, g);
        p.sprite.drawPortrait((i*width*2/9)+width/9-10, desty+4, g);
        p.sprite.drawBars((i*width*2/9)+2, desty+24, 0, 0, g);
      }
      if(i == porCounter)
        paintReticule((i*width*2/9)+width/9-10, desty+4, 16, 16, g);
    }
  }
  
  public void paintBuffer(Graphics g){
    //System.out.println("painting");
    if(drawFrame.x > 0)
      drawFrame.x-=8;
    if(drawFrame.y > 0)
      drawFrame.y-=8;
    if(drawFrame.width < width*(int)zoom.width)
      drawFrame.width+=16;
    if(drawFrame.height < height*(int)zoom.height)
      drawFrame.height+=16;
    if( gameState.equals(GameState.inExp) ){
      paintExp(g);
      if( !passExpScreen ) gameState.equals(GameState.inExp);
      return;
    }
    if( gameState.equals(GameState.inMap) ){
      map.paint(g);
      return;
    }
    if( gameState.equals(GameState.inBattle) ){
      currGrid.paintGrid(true, g);
      if(gPanel != null){
        currGrid.addTempSprite(gPanel.battle);
        gPanel.battle.paintFightMenu(g);
      }
    }
    else if( gameState.equals(GameState.inCutscene) ){
      currGrid.paintGrid(false, g);
      if(cutscene != null)
        cutscene.paint(g);
    }
    else{
      currGrid.paintGrid(false, g);
      if(currentScript!= null)
        currentScript.paint(g);
      
      int hei = (c.dy > currGrid.height - currGrid.camera.y) ?  0 : height*3/4;
      //party menu
      if(partyMenuOn && !(orderMenuOn || itemMenuOn))
        paintStatusScreen(hei, g);
      if(partyMenuOn)
        paintPartyMenu(hei, g);
      if(orderMenuOn){
        paintCharacterChoice(orderMenuOn, hei, g);
      }
      if(itemMenuOn)
        paintItems(hei, g);
      if(selectMenuOn){
        paintWindow(width-32, hei, 32, 32, g);
        paintLetters("Yes", null, width-32, hei, 32, g);
        paintLetters("No", null, width-32, hei+16, 32, g);
        if(selCounter == 0)
          paintReticule(width-32, hei, 32, 16, g);
        else
          paintReticule(width-32, hei+16, 32, 16, g);
      }
      if(chooseMenuOn){
        if(itemMenuOn){
          paintCharacterChoice(itemMenuOn, hei, g);
        }
      }
    }
  }
  
  //helper function for Cutscene class
  public void addTempSprite(AnimatedScript as){
    currGrid.addTempSprite(as);
  }
  
  public void paint(Graphics g){
    backbuffer = (BufferedImage)createImage(width, height);
    //System.out.println("Ought to be non-null");
    backg = backbuffer.getGraphics();
    paintBuffer(backg);
    g.drawImage(backbuffer, drawFrame.x, drawFrame.y, drawFrame.width, drawFrame.height, 0, 0, width, height,this);

    /*0, 0, (int)(width*zoom.width), (int)(height*zoom.height),*/
  }
  
  public class SwitchThread extends Thread{
    
    public void run(){
      while(true){
        if( gameState.equals(GameState.inBattle) && gPanel.finished){
          if(cutscene != null){
            gameState.equals(GameState.inCutscene);
          }
          else{
            gameState.equals(GameState.inField);
          }
          currGrid.removeTempSprite();
          gPanel = null;
          removeEncounter();
          
        }
        else if( !gameState.equals(GameState.inBattle) && (checkEnemyHit() || forceBattle()) ){
          getBattlePositions(getEnemies());
          makeBattle(getEnemies());
          gameState.equals(GameState.inBattle);
          //inMap = false;
          //preBattle = true;
          //preBattle do some method
        }
        if( gameState.equals(GameState.inCutscene) ){
          handleCutscene();
        }
      }      
    }
    
  }
  
}

