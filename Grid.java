//package Game;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Point2D;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public abstract class Grid extends Component{
  Tile[][] tiles;
  
  protected DrawTree drawTree;
  
  //let's explain it yo! So now everything is drawn from the center of the destination coordinates. This is fine so long
  //as these offsets are added to every coordinate
  int oX, oY;
  int width, height, tWidth, tHeight;
  //for zooming in/out
  protected Size s_size;
  private BufferedImage bg;
  private BufferedImage wholeBG;
  private Graphics fieldGraphics;
  protected Dimension tileSize;
  //Cutscenes on immediate entry
  Queue<Queue<Incident>> pre_scene;
  //Incidents--one incident will contain them all, one incident will be for input necessaries, one incident for mere contact 
  //for now stepped is not implemented
  
  Vector<IncidentTile> promptInc;
  Vector<ScriptedTile> scriptInc;
  FieldEnemy[] e;
  //"fixtures" for non-removable sprites, "sprites" for battle-removed sprites

  Vector<Sprite> fixtures;
  Vector<Sprite> sprites;
  //"doors" for collision detection of doors
  Vector<Tile> doors;
  Sprite bed;
  //
  Rectangle camera;
  Point centerOfCam;
  private int amt_dispx, amt_dispy;
  //
  Sprite tempSprite;
  //Grid making
  ImageCreator imageCreator;
  //Connect grids to each other
  private Grid northGrid, eastGrid, southGrid, westGrid;
  //character positions when entering from the opposite direction
  public Point northP, eastP, southP, westP;
  
  //BufferedImage for speech bubble
  private BufferedImage speech;
  private Sprite speechTok; 
  
  //'Grid' states
  protected int state;
  
  public Grid(FieldCharacter c){
    drawTree = new DrawTree();
    try{
      //danger = ImageIO.read(getClass().getResource("Sprites/Danger.png"));
      speech = ImageIO.read(getClass().getResource("Sprites/Speech.png"));
    }catch(IOException i){JOptionPane.showMessageDialog(null, "Cannot open file");}
    imageCreator = new ImageCreator();
    pre_scene = new Queue<Queue<Incident>>();
    promptInc = new Vector<IncidentTile>();
    scriptInc = new Vector<ScriptedTile>();
    e = new FieldEnemy[0];
    amt_dispx = 0;
    amt_dispy = 0;
    sprites = new Vector<Sprite>();
    fixtures = new Vector<Sprite>();
    doors = new Vector<Tile>();
    speechTok = null;
    state = 0;
    c.cNode = null;
  }
  
  public Grid(FieldCharacter c, int ste){
    drawTree = new DrawTree();
    try{
      //danger = ImageIO.read(getClass().getResource("Sprites/Danger.png"));
      speech = ImageIO.read(getClass().getResource("Sprites/Speech.png"));
    }catch(IOException i){JOptionPane.showMessageDialog(null, "Cannot open file");}
    imageCreator = new ImageCreator();
    pre_scene = new Queue<Queue<Incident>>();
    promptInc = new Vector<IncidentTile>();
    scriptInc = new Vector<ScriptedTile>();
    e = new FieldEnemy[0];
    amt_dispx = 0;
    amt_dispy = 0;
    sprites = new Vector<Sprite>();
    fixtures = new Vector<Sprite>();
    doors = new Vector<Tile>();
    speechTok = null;
    state = ste;
    c.cNode = null;
  }
  
  public enum PredefinedGrids{
    Classroom, Bedroom, Hallway, Gym;
  }
  
  protected enum Segment{
    Bleachers;
  }
  
  //public DrawTree.Node makeGrid(PredefinedGrids grid, FieldCharacter c){ return gridMaker.makeGrid(grid, c); }
  
  public abstract Grid getNorthGrid(FieldCharacter c, Story story); //{return northGrid;}
  //public void setNorthGrid(Grid n){northGrid = n;}
  public abstract Grid getEastGrid(FieldCharacter c, Story story); //{return eastGrid;}
  //public void setEastGrid(Grid e){eastGrid = e;}
  public abstract Grid getSouthGrid(FieldCharacter c, Story story); //{return southGrid;}
  //public void setSouthGrid(Grid s){southGrid = s;}
  public abstract Grid getWestGrid(FieldCharacter c, Story story); //{return westGrid;}
  //public void setWestGrid(Grid w){westGrid = w;}
  
  /*public void enter(FieldCharacter c){
    
  }*/
  
  public void setCamera(Sprite c){
    int nGridOff = 0;
    int eGridOff = 0;
    int sGridOff = 0;
    int wGridOff = 0;
    if(northP != null)
      nGridOff = tileSize.height;
    if(eastP != null)
      eGridOff = tileSize.width;
    if(southP != null)
      sGridOff = tileSize.height;
    if(eastP != null)
      wGridOff = tileSize.width;
    
    if(c.dx + camera.width/2 > width + eGridOff)
      camera.x = width + eGridOff  - camera.width;    
    else if(c.dx - camera.width/2 < 0 - wGridOff)
      camera.x = 0 - wGridOff;
    else
      camera.x = c.dx - camera.width/2;
    if(c.dy + camera.height/2 > height + sGridOff)
      camera.y = height + sGridOff - camera.height;
    else if(c.dy - camera.height/2 < 0 - nGridOff)
      camera.y = 0 - nGridOff;
    else
      camera.y = c.dy - camera.height/2;
  }
  /*
  public void setCamera(Sprite c, int displx, int disply){
    amt_dispx += displx;
    amt_dispy += disply;
    
    int shftx = (int)Math.ceil(0.1*amt_dispx);
    int shfty = (int)Math.ceil(0.1*amt_dispy);
    
    System.out.println(shftx + " " + shfty);
    
    int nGridOff = 0;
    int eGridOff = 0;
    int sGridOff = 0;
    int wGridOff = 0;
    if(northP != null)
      nGridOff = tileSize.height;
    if(eastP != null)
      eGridOff = tileSize.width;
    if(southP != null)
      sGridOff = tileSize.height;
    if(eastP != null)
      wGridOff = tileSize.width;
    
    if(c.dx + camera.width/2 + shftx > width + eGridOff)
      camera.x = width + eGridOff  - camera.width;    
    else if(c.dx - camera.width/2 + shftx < 0 - wGridOff)
      camera.x = 0 - wGridOff;
    else
      camera.x = camera.x + shftx;
    if(c.dy + camera.height/2 + shfty > height + sGridOff)
      camera.y = height + sGridOff - camera.height;
    else if(c.dy - camera.height/2 + shfty < 0 - nGridOff)
      camera.y = 0 - nGridOff;
    else
      camera.y = camera.y + shfty;
    
    amt_dispx -= shftx;
    amt_dispy -= shfty;
  }
  */
  //to make battle backgrounds
    
  public void setOverlap(Tile[][] ts, int destx, int desty, int wid, int hei){
    for(int i = destx; i < destx+wid; i++)
      for(int j = desty; j < desty+hei; j++)
      ts[i-destx][j-desty] = tiles[i][j];
  }
  
  public boolean isPassable(FieldCharacter c){
    //System.out.println( "tile[4][0] priority: " + tiles[4][0].priority() );
    boolean pass = true;
    
    int currX = (int)Math.floor(c.dx/16);
    int currY = (int)Math.floor(c.dy/16);
    if(tiles[currX][currY].tile().isDoor){
      tiles[currX][currY].tile().x = 32;
    }
    
    Rectangle rec = new Rectangle(c.dx-c.sWidth/2, c.dy-c.sHeight/2, c.sWidth, c.sHeight);
    
    // criteria for 'up' and 'down' are harsher, as it is fine for sprites to overlap but not on the sides
    switch(c.dir){ 
      case Up:
        currX = (int)Math.floor((c.dx+c.sWidth/2)/16);
        currY = (int)Math.ceil((c.dy+c.sHeight/2)/16);
        pass = pass && tiles[currX][currY-1].isPassable();
        currX = (int)Math.floor((c.dx-c.sWidth/2)/16);
        pass = pass && tiles[currX][currY-1].isPassable();
        currX = (int)Math.floor(c.dx/16);
        pass = pass && tiles[currX][currY-1].isPassable();
        break;
      case Down:
        currX = (int)Math.floor((c.dx+c.sWidth/2)/16);
        currY = (int)Math.ceil((c.dy-c.sHeight/2)/16);
        pass = pass && tiles[currX][currY+1].isPassable();
        currX = (int)Math.floor((c.dx-c.sWidth/2)/16);
        pass = pass && tiles[currX][currY+1].isPassable();
        currX = (int)Math.floor(c.dx/16);
        pass = pass && tiles[currX][currY+1].isPassable();
        //System.out.println("Tile: " + tiles[currX][currY+1].priority());
        //System.out.println("FC: " + c.cNode.old_priority + " True: " + c.priority() + " Tile: " + tiles[currX][currY+1].priority());
      break;
      case Left:
        //currX = (int)Math.ceil((c.dx+c.sWidth/2)/16);
        //pass = pass && tiles[currX-1][currY].isPassable();
        currX = (int)Math.floor((c.dx+c.sWidth/2)/16);
        pass = pass && tiles[currX-1][currY].isPassable();
        //System.out.println("Tile: " + tiles[currX-1][currY].priority());
        break;
      case Right:
        //currX = (int)Math.ceil((c.dx-c.sWidth/2)/16);
        //pass = pass && tiles[currX+1][currY].isPassable();
        currX = (int)Math.floor((c.dx-c.sWidth/2)/16);
        pass = pass && tiles[currX+1][currY].isPassable();
        //System.out.println("Tile: " + tiles[currX+1][currY].priority());
        break;
    }
    
    if(tiles[currX][currY].tile().isDoor){
      tiles[currX][currY].tile().x = 0;
    }
    speechTok = null;
    for( int i = 0; i < promptInc.size(); i++){
      if(rec.intersects(promptInc.valAt(i).bounds)) speechTok = promptInc.valAt(i);
      pass = pass && !rec.intersects(promptInc.valAt(i).bounds);
      //if(rec.intersects(promptInc.valAt(i).bounds)) System.out.println("Prompt: " + promptInc.valAt(i).priority());
    }
    return pass;
  }
  
  public FieldEnemy.Direction findPath(FieldCharacter pc, FieldEnemy en){
    int currX = (int)Math.floor(en.dx/16);
    int currY = (int)Math.floor(en.dy/16);
    //adjusted tile value
    int adj;
    int x, y;
    double[] tDistances = new double[5];
    double value;
    FieldEnemy.Direction currDirection;
   //stand case
    x = tiles[currX][currY].dx;
    y = tiles[currX][currY].dy;
    tDistances[0] = Math.sqrt( (x-pc.dx)*(x-pc.dx) + (y-pc.dy)*(y-pc.dy));
    value = tDistances[0];
    currDirection = FieldEnemy.Direction.Stand; 
    //up case
    adj = 1;
    /*if(currY-adj < 0 || tiles[currX][currY-adj].getBounds().intersects(en.getBounds()))
      adj = 2;*/
    if(currY-adj < 0 || !tiles[currX][currY-adj].tile().isPassable())
      tDistances[1] = Double.POSITIVE_INFINITY;
    else {
      x = tiles[currX][currY-adj].dx;
      y = tiles[currX][currY-adj].dy;
      tDistances[1] = Math.sqrt( (x-pc.dx)*(x-pc.dx) + (y-pc.dy)*(y-pc.dy));
      if(tDistances[1] < value){
        value = tDistances[1];
        currDirection = FieldEnemy.Direction.Up;
      }
    }
    //right case
    adj = 1;
    /*if(currX+adj > tWidth  || tiles[currX+adj][currY].getBounds().intersects(en.getBounds()))
      adj = 2;*/
    if(currX+adj > tWidth  || !tiles[currX+adj][currY].tile().isPassable())
      tDistances[2] = Double.POSITIVE_INFINITY;
    else {
      x = tiles[currX+adj][currY].dx;
      y = tiles[currX+adj][currY].dy;
      tDistances[2] = Math.sqrt( (x-pc.dx)*(x-pc.dx) + (y-pc.dy)*(y-pc.dy));
      if(tDistances[2] < value){
        value = tDistances[2];
        currDirection = FieldEnemy.Direction.Right;
      }
    }
    //down case
    adj = 1;
    /*if(currY+adj >= tHeight || tiles[currX][currY+adj].getBounds().intersects(en.getBounds()))
      adj = 2;*/
    if(currY+adj >= tHeight || !tiles[currX][currY+adj].tile().isPassable())
      tDistances[3] = Double.POSITIVE_INFINITY;
    else {
      x = tiles[currX][currY+adj].dx;
      y = tiles[currX][currY+adj].dy;
      tDistances[3] = Math.sqrt( (x-pc.dx)*(x-pc.dx) + (y-pc.dy)*(y-pc.dy));
      if(tDistances[3] < value){
        value = tDistances[3];
        currDirection = FieldEnemy.Direction.Down;
      }
    }
    //left case
    adj = 1;
    /*if(currX - adj < 0 || tiles[currX-adj][currY].getBounds().intersects(en.getBounds()))
      adj = 2;*/
    if(currX - adj < 0 || !tiles[currX-adj][currY].tile().isPassable())
      tDistances[4] = Double.POSITIVE_INFINITY;
    else {
      x = tiles[currX-adj][currY].dx;
      y = tiles[currX-adj][currY].dy;
      tDistances[4] = Math.sqrt( (x-pc.dx)*(x-pc.dx) + (y-pc.dy)*(y-pc.dy));
      if(tDistances[4] < value){
        value = tDistances[4];
        currDirection = FieldEnemy.Direction.Left;
      }
    }
   
    return currDirection;
  }
  
  public Cutscene getCutscene(Field field, /*BufferedImage bImg,*/ FieldCharacter fChar){
    if(pre_scene.peek() == null || pre_scene.empty())
      return null;
    //while(bImg == null);
    return new Cutscene(field, fChar, /*bImg,*/ pre_scene.pop());
  }
  
  public IncidentTile handleIncidents(FieldCharacter c){
    Rectangle rec = new Rectangle(c.dx-c.sWidth/2, c.dy-c.sHeight/2, c.sWidth, c.sHeight);
    int currX = 0;
    int currY = 0;
    if(c.dir == ActorSprite.Direction.Up){
      rec.y-=2;
      currX = (int)Math.floor((c.dx)/16);
      currY = (int)Math.floor((c.dy-c.sHeight/2)/16);
    }
    if(c.dir == ActorSprite.Direction.Down){
      rec.y+=2;
      currX = (int)Math.floor((c.dx)/16);
      currY = (int)Math.floor((c.dy+c.sHeight/2)/16);      
    }
    if(c.dir == ActorSprite.Direction.Left){
      rec.x-=2;
      currX = (int)Math.floor((c.dx-c.sWidth/2)/16);
      currY = (int)Math.floor((c.dy)/16);      
    }
    if(c.dir == ActorSprite.Direction.Right){
      rec.x+=2;
      currX = (int)Math.floor((c.dx+c.sWidth/2)/16);
      currY = (int)Math.floor((c.dy)/16);      
    }
    for(int i = 0; i < promptInc.size(); i++){
      if(promptInc.valAt(i).hasIncident(rec))
        return promptInc.valAt(i);
    }
    //System.out.println("null case");
    return null;
  }
  
  public ScriptedTile handleScripts(FieldCharacter c){
    for(int i = 0; i < scriptInc.size(); i++){
      if(scriptInc.valAt(i).getBounds().intersects(c.getBounds()))
        return scriptInc.valAt(i);
    }
    return null;
  }
  
  public void deleteEncounter(int enemyNumber){e[enemyNumber] = null;}
    
  public void orderPause(){
    for(int i = 0; i < e.length; i++){
      if(e[i] != null)
        e[i].pause();
    }
  }
  
  public void orderResume(){
    for(int i = 0; i < e.length; i++){
      if(e[i] != null)
        e[i].resume();
    }
  }
  
  //helper; means "Distance to Tile", shortened so it will be useful
  //honestly all the widths and heights are the same...for now I guess
  public int dtot(double numTiles){
    return oX + (int)(tileSize.width * numTiles);
  }
  
  public void addTempSprite(Sprite as){
    tempSprite = as;
  }
  
  public void removeTempSprite(){tempSprite = null;}
  
  public void putIntoBed(DrawTree.Node fChNode, DrawTree tree, FieldCharacter fCh){
    fCh.setCoordinates(fChNode, tree, bed.dx+bed.sWidth/2, bed.dy+bed.sHeight/4);
  }
  
  public boolean inCam(Sprite spr){
    return spr.dx > (camera.x-16) && spr.dx < (camera.x+camera.width+16) && spr.dy > (camera.y-16) && spr.dy < (camera.y+camera.height+16);
  }
  
  public synchronized void paintEntireField(boolean inBattle, Graphics g){
    g.drawImage(wholeBG, 0, 0, width, height, null);

    for(drawTree.begin(); !drawTree.end();  drawTree.traverse()){
      //System.out.println("traversing " + drawTree.itr_value() );
      //if( inCam(drawTree.itr_value()) ){
        if(drawTree.itr_value() instanceof FieldCharacter){
          /*
          int currX = (int)Math.floor(drawTree.itr_value().dx/16);
          int currY = (int)Math.floor(drawTree.itr_value().dy/16);
          g.drawRect(tiles[currX][currY].bounds.x, tiles[currX][currY].bounds.y, tiles[currX][currY].bounds.width, tiles[currX][currY].bounds.height); 
          */
          /*g.drawRect(drawTree.itr_value().dx-drawTree.itr_value().sWidth/2, drawTree.itr_value().dy-drawTree.itr_value().sHeight/2,
                     drawTree.itr_value().sWidth, drawTree.itr_value().sHeight);*/
          if(!inBattle) drawTree.itr_value().paint(g); 
        }
        else if(drawTree.itr_value() instanceof FieldEnemy){
          if(!inBattle) drawTree.itr_value().paint(g);
        }
        else
          drawTree.itr_value().paint(g);
        if(drawTree.itr_value() instanceof DialogueTile && speechTok == drawTree.itr_value())
          g.drawImage(speech, drawTree.itr_value().dx, drawTree.itr_value().dy, 8, 7, null);
      //}
    }
    
    if(bed != null)
      bed.paint(g);
    if(tempSprite != null)
      tempSprite.paint(g);
    
    //Rectangle rec = new Rectangle(c.dx-c.sWidth/2, c.dy-c.sHeight/2, c.sWidth, c.sHeight);
    
    /*
    for(int i = 0; i < tWidth; i++)
      for(int j = 0; j < tHeight; j++)
      if(inCam(tiles[i][j]) && tiles[i][j].spreadOut){
      tiles[i][j].paint(g);
      if(rec.intersects(tiles[i][j].bounds) && rec.y > tiles[i][j].bounds.y && !inBattle)
        c.paint(fieldGraphics);
    }
    */
    /*
    for(int i = 0; i < promptInc.size(); i++){
      if(inCam(promptInc.valAt(i)))
        promptInc.valAt(i).paint(g);
      if(speechTok == i)
        g.drawImage(speech, promptInc.valAt(i).dx, promptInc.valAt(i).dy, 8, 7, null);
    }
    */
  }
  
  //helper function for Cutscene
  public void paintOnto(Sprite s, Graphics g){
    s.paint(fieldGraphics);
  }
  
  public void paint(Graphics g){
    g.drawImage(wholeBG, 0, 0, (int)s_size.width, (int)s_size.height, camera.x, camera.y, (camera.x+camera.width), (camera.y+camera.height), null);
  }
  
  public void paintGrid(boolean inBattle, Graphics g){
    wholeBG = bg.getSubimage(0, 0, width, height);
    fieldGraphics = wholeBG.getGraphics();
    paintEntireField(inBattle, fieldGraphics);

    /*if(inBattle){
      g.drawImage(wholeBG, 0, 0, camera.width, camera.height*4/9, camera.x, camera.y, (camera.x+camera.width), camera.y+camera.height/2, null);
      for(int i = 0; i < camera.width; i += 16)
        for(int j = camera.height/2; j < camera.height; j+=16)
        g.drawImage(danger, i, j, i+16, j+16, 0, 0, 16, 16, null);
      g.drawImage(wholeBG, 0, camera.height*4/9, camera.width, camera.height*4/3, 
                  camera.x, camera.height/2, (camera.x+camera.width), (camera.y+camera.height), null);
    }
    else*/
    paint(g);
  }
  public void update(Graphics g){paint(g);}
  
  public abstract DrawTree.Node createGrid(FieldCharacter c, Story story);
  
  public void makeImpassableTiles(String file, int initX, int initY, int endX, int endY){
    if(initX >= endX || initY >= endY)
      return;
    BufferedImage tile = imageCreator.makeImage(file);
    for(int i = initX; i < endX; i++)
      for(int j = initY; j < endY; j++){
      tiles[i][j] = new Tile(tile, dtot(i), dtot(j));
      tiles[i][j].setPassable(false);
    }
  }
  
  public Queue<Incident> addMessage(String message){
    Queue<Incident> queue = new Queue<Incident>();
    queue.add(new Incident(message, Grid.this));
    return queue;
  }
  
  public void addObject(String file, int destx, int desty, int width, int height){
    Tile[][] ts = new Tile[width][height];
    setOverlap(ts, destx, desty, width, height);
    Tile t1 = new Tile(file, ts, width, height, false); 
  }
  
  public void addObject(BufferedImage image, int destx, int desty, int width, int height){
    Tile[][] ts = new Tile[width][height];
    setOverlap(ts, destx, desty, width, height);
    Tile t1 = new Tile(image, ts, width, height, false); 
  }
  
  public DrawTree.Node addSprite(Sprite s){
    sprites.add(s);
    return drawTree.add(s);
  }
  
  public DrawTree.Node addFixture(Sprite s){
    fixtures.add(s);
    return drawTree.add(s);
  }
  
  public DrawTree.Node addDoor(Tile d){
    doors.add(d);
    return drawTree.add(d);
  }
  
  public DrawTree.Node addScriptedTile(ScriptedTile t){
    scriptInc.add(t);
    return drawTree.add(t);
  }
  
  public DrawTree.Node addDialogueTile(IncidentTile t){
    promptInc.add(t); 
    return drawTree.add(t);
  }
  
  public DrawTree.Node addNPCTile(ActorConstructor.Actor act, int npcX, int npcY, Incident[] incidents, boolean overlap ){
    return addNPCTile(act, npcX, npcY, 0, 0, incidents, overlap);
    /*
     Queue<Incident> q = new Queue<Incident>();
     q.add(new Incident(true, incidents));
     ActorTile tile = ActorConstructor.makeNPC(act, dtot(npcX), dtot(npcY), q);
     //if(overlap)
     //  tiles[npcX][npcY].setOverlap(tile);
     return addNPCTile(act, npcX, npcY, q, overlap);
     */
    
  }
  
  public DrawTree.Node addNPCTile(ActorConstructor.Actor act, int npcX, int npcY, int offx, int offy, Incident[] incidents, boolean overlap ){
    Queue<Incident> q = new Queue<Incident>();
    q.add(new Incident(true, incidents));
    //ActorTile tile = ActorConstructor.makeNPC(act, dtot(npcX)+offx, dtot(npcY)+offy, q);
    //if(overlap)
    //  tiles[npcX][npcY].setOverlap(tile);
    return addNPCTile(act, npcX, npcY, offx, offy, q, overlap);
  }
  
  public DrawTree.Node addNPCTile(NPC npc, /*int npcX, int npcY,*/ Incident[] incidents, boolean overlap ){
    Queue<Incident> q = new Queue<Incident>();
    q.add(new Incident(true, incidents));
    npc.giveIncident(q);
    /*
     if(overlap)
     tiles[npcX][npcY].setOverlap(npc);
     */
    return addDialogueTile(npc);
  }
  
  public DrawTree.Node addNPCTile(ActorConstructor.Actor act, int npcX, int npcY, Queue<Incident> messages, boolean overlap ){
    ActorTile tile = ActorConstructor.makeNPC(act, dtot(npcX), dtot(npcY), messages);
    //if(overlap)
    //  tiles[npcX][npcY].setOverlap(tile);
    return addNPCTile(act, npcX, npcY, 0, 0, messages, overlap);
  }
  
  public DrawTree.Node addNPCTile(ActorConstructor.Actor act, int npcX, int npcY, int offx, int offy, Queue<Incident> messages, boolean overlap ){
    ActorTile tile = ActorConstructor.makeNPC(act, dtot(npcX)+offx, dtot(npcY)+offy, messages);
    //if(overlap)
    //  tiles[npcX][npcY].setOverlap(tile);
    return addDialogueTile(tile);
  }
  
  /*public void addNPCTile(NPC npc, int npcX, int npcY, boolean overlap ){
   if(overlap)
   tiles[npcX][npcY].setOverlap(npc);
   addDialogueTile(npc);
   }*/
  
  public void addBed(NPC sleeper, int initX, int initY){
    Tile[][] bedTiles = new Tile[2][2];
    setOverlap(bedTiles, initX, initY, 2, 2);
    bed = new Tile(sleeper, imageCreator.makeImage("Game/Bed.png"), bedTiles); 
  }
  
  public void init(int wid, int hei, BufferedImage til){
    bg = imageCreator.makeImage("Sprites/Field.png");
    bg = bg.getSubimage(0, 0, wid, hei);
    width = bg.getWidth();
    height = bg.getHeight();
    setBounds(bg.getMinX(), bg.getMinY(), bg.getWidth(), bg.getHeight());
    setVisible(true);
    tWidth = bg.getWidth()/16;
    tHeight = bg.getHeight()/16;
    tiles = new Tile[bg.getWidth()/16][bg.getHeight()/16];
    tileSize = new Dimension(16, 16);
    oX = tileSize.width/2;
    oY = tileSize.height/2;
    for(int i = 0; i < tWidth; i++)
      for(int j = 0; j < tHeight; j++)
      tiles[i][j] = new Tile(til, dtot(i), dtot(j));
  }
  
  public void putTilesInTree(Tile[][] tiles){
    for(int i = 0; i < tiles.length; i++)
      for(int j = 0; j < tiles[i].length; j++)        
      drawTree.add(tiles[i][j]);
  }
  
  protected class ImageCreator{
    
    public ImageCreator(){
    }
        
    public BufferedImage makeImage(String file){
      BufferedImage image = null;
      try{
        image = ImageIO.read(getClass().getResource(file));
      }catch(IOException i){JOptionPane.showMessageDialog(null, "Cannot open file");}
      return image;
    }
    
    public BufferedImage makeImage(Segment seg, int par){
      BufferedImage pic;
      switch(seg){
        case Bleachers:
          pic = imageCreator.makeImage("Sprites/Bleacher.png");
          pic = pic.getSubimage(0, par*16, pic.getWidth(), 16);
          return pic;
      }
      return null;
    }
  }
}


/*int currX = (int)Math.floor((c.dx)/16);
 int currY = (int)Math.floor((c.dy)/16);
 for(int i = - 1; i < 2; i++)
 for(int j = -1; j < 2; j++){
 if(currX+i < 0 || currX+i >= tWidth || currY+j < 0 || currY+j >= tHeight)
 continue;
 Rectangle r = tiles[currX+i][currY+j].getBounds();
 fieldGraphics.drawRect(r.x-r.width/2, r.y-r.height/2, r.width, r.height);
 }*/
