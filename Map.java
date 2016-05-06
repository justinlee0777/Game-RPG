//package Game;

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map extends Sprite{
  Field field;
  FieldCharacter fChar;
  Point[] locationPoints;
  Story.Region[] regions;
  NPC[] partyNPCs;
  Sprite[] walkNPCs;
  Rectangle[] walkBounds;
  //moving from dest to dest
  Point currPoint;
  int dest;
  Point destPoint;
  //int gridNum;
  String floor;
  //Point fCharPoint;
  //
  int slct;
  BufferedImage location;
  int selected = 0;
  protected boolean reachedDest = true;
  
  public Map(Field fld, FieldCharacter c){
    super(72, 96, 144, 192);
    field = fld;
    fChar = c;
    try{
      img = ImageIO.read(getClass().getResource("Sprites/Map.png"));
      location = ImageIO.read(getClass().getResource("Sprites/Location Icon.png"));
    }catch(IOException e){JOptionPane.showMessageDialog(null, "Map: Cannot open file");}
    locationPoints = new Point[4];
    locationPoints[0] = new Point(37, 142);
    locationPoints[1] = new Point(114, 129);
    locationPoints[2] = new Point(92, 59);
    locationPoints[3] = new Point(79, 40);
    regions = new Story.Region[4];
    regions[0] = Story.Region.Tutorial;
    regions[2] = Story.Region.Tower;
    regions[3] = Story.Region.Dome;
    partyNPCs = new NPC[1];
    partyNPCs[0] = c;
    currPoint = new Point(37, 142);
  }
 
  public void setChars(NPC[] npcs){
    partyNPCs = npcs;
    walkNPCs = new Sprite[partyNPCs.length];
    walkBounds = new Rectangle[partyNPCs.length];
    for(int i = 0; i < partyNPCs.length; i++){
      walkNPCs[i] = new Sprite(partyNPCs[i].img, 60+(i*20), 192-partyNPCs[i].sHeight/2, partyNPCs[i].sWidth, partyNPCs[i].sHeight);
      walkBounds[i] = partyNPCs[i].right;
      walkNPCs[i].x = walkBounds[i].x;
      walkNPCs[i].y = walkBounds[i].y;
    }
  }
  
  int signX, signY;
  public void setDestination(Story.Region toFloor, String flr/*, Point fPoint*/){
    for(int i = 0; i < locationPoints.length; i++){
      if(regions[i] == toFloor){
        if(i == slct)
          return;
        dest = i;
        destPoint = new Point(locationPoints[i].x, locationPoints[i].y);
        reachedDest = false;
        signX = (currPoint.x < destPoint.x) ? 1 : -1;
        signY = (currPoint.y < destPoint.y) ? 1 : -1;
      }
    }
    floor = flr;
    //gridNum = g;
    //fCharPoint = fPoint;
  }
  
  private int counter = 0;
  public void paint(Graphics g){
    if(reachedDest){
      field.currGrid = field.getGrid(fChar, floor); //Region.makeFloor(regions[dest], fChar, gridNum, fCharPoint);
      field.setCutscene(field.currGrid/*.getCutscene(field, field.backbuffer, fChar)*/);
      slct = dest;
      //field.drawFrame = new Rectangle(field.width/2*(int)zoom.width, field.height/2*(int)zoom.height, 0, 0);
      field.gameState = Field.GameState.inField;
    }
    super.paint(g);
    for(int i = 0; i < locationPoints.length; i++){
      if(selected > 2)
        selected = 0;
      if(i == slct)
        g.drawImage(location, locationPoints[i].x-8, locationPoints[i].y-8, locationPoints[i].x+8, locationPoints[i].y+8, 16*selected, 0, 16*(selected+1), 16, null);
      else
        g.drawImage(location, locationPoints[i].x-8, locationPoints[i].y-8, locationPoints[i].x+8, locationPoints[i].y+8, 0, 0, 16, 16, null);
    }
    if(++counter%3 == 0){
      //walking sprites
      for(int i = 0; i < partyNPCs.length; i++){
        walkNPCs[i].x+=walkNPCs[i].sWidth;
        if(walkNPCs[i].x >= walkBounds[i].x+walkBounds[i].width)
          walkNPCs[i].x = walkBounds[i].x;
      }
      //
      counter = 0;
      //move sprite
      int addX, addY;
      if(signX < 0)
        addX = (currPoint.x+(signX*4) < destPoint.x) ? destPoint.x - currPoint.x : 4;
      else
        addX = (currPoint.x+(signX*4) > destPoint.x) ? destPoint.x - currPoint.x : 4;
      if(signY < 0)
        addY = (currPoint.y+(signY*4) < destPoint.y) ? destPoint.y - currPoint.y: 4;
      else
        addY = (currPoint.y+(signY*4) > destPoint.y) ? destPoint.y - currPoint.y: 4;
      currPoint.x+=(signX*addX);
      currPoint.y+=(signY*addY);
      reachedDest = (currPoint.x == destPoint.x && currPoint.y == destPoint.y);
      //change current location icon
      selected++;
    }
    //drawing NPC sprites
    if(partyNPCs.length > 1)
      partyNPCs[1].drawHead(currPoint.x+4, currPoint.y, g);
    if(partyNPCs.length > 2)
      partyNPCs[2].drawHead(currPoint.x-4, currPoint.y, g);
    partyNPCs[0].drawHead(currPoint.x, currPoint.y, g);
    for(int i = 0; i < partyNPCs.length; i++)
      walkNPCs[i].paint(g);
  }
  
}
