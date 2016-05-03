//package Game;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class NPC extends ActorTile{
  String name;
  Rectangle head;
  Rectangle headshot;
  //BufferedImage portrait;
  
  //for non NPCs...used for FieldEnemy door
  public NPC(){
    super();
  }
  
  public NPC(String file, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle por){
    super(file, destx, desty, actor, false, incidents);
    name = nme;
    headshot = por;
  }
  
  public NPC(Image img, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle por){
    super(img, destx, desty, actor, false, incidents);
    name = nme;
    headshot = por;
  }
  
  public NPC(String file, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle hd, Rectangle por){
    this(file, destx, desty, actor, incidents, nme, por);
    head = hd;
  }
  
  public NPC(Image img, int destx, int desty, ActorSprite actor, Queue<Incident> incidents, String nme, Rectangle hd, Rectangle por){
    this(img, destx, desty, actor, incidents, nme, por);
    head = hd;
  }
  
  public void putSleep(){sprite.putSleep();}
  public void putAwake(){sprite.putAwake();}
  public boolean isSleeping(){return sprite.sleeping;}
  
  /*
  public void paintSleeping(Graphics g){
    if(sprite.sleeping)
      g.drawImage(img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2), sprite.sleep.x, sprite.sleep.y, sprite.sleep.x+sWidth, sprite.sleep.y+sHeight, this);
  }
  */
  public void paint(Graphics g){
    /*
    if(sprite.sleeping)
      return;
    else
    */
    super.paint(g);
  }

  public void drawHead(int destx, int desty, Graphics g){
    g.drawImage(img, destx-8, desty-8, destx+8, desty+8, 
                head.x, head.y, head.x+head.width, head.y+head.height, this); 
  }
  public void drawPortrait(int destx, int desty, Graphics g){
   g.drawImage(img, destx, desty, destx+16, desty+16, 
               headshot.x, headshot.y, headshot.x+headshot.width, headshot.y+headshot.height, this); 
  }
  
}