//package Game;

import java.awt.*;
//import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class Sprite extends Canvas{
  protected int width, height, sWidth, sHeight;
  protected int x, y;
  protected BufferedImage img;
  public int dx, dy;
  //priority to be drawn last
  protected int priori = 0;
  protected Rectangle bounds;
  protected static Size zoom = new Size(3, 3);
  protected boolean stopPainting = false;
  //figure this out some time...for certain sprites, stop drawing unless they are to be updated
  protected boolean updateSprite = true;
  
  public Sprite(){
  }
  
  public Sprite(int destx, int desty, int wid, int hei){
    dx = destx;
    dy = desty;
    width = wid;
    height = hei;
    sWidth = width;
    sHeight = height;
    init();
  }
  
  public Sprite(String file){
    try{
      img = ImageIO.read(getClass().getResource(file));
    }catch(IOException i){JOptionPane.showMessageDialog(null, "Sprite class: Cannot open " + file);
      System.exit(0);}
    height = img.getHeight(this);
    width = img.getWidth(this);
    sWidth = width;
    sHeight = height;
    init();
  }
    
  public Sprite(String file, int destx, int desty){
    this(file);
    dx = destx;
    dy = desty;
    init();
  }
  
  public Sprite(String file, int destx, int desty, int priority){
    this(file, destx, desty);
    priori = priority;
    init();
  }
  
  public Sprite(String file, int destx, int desty, int sWid, int sHei){
    this(file, destx, desty);
    sWidth = sWid;
    sHeight = sHei;
    init();
  }
  
  public Sprite(Image im){
    img = (BufferedImage)im;
    width = img.getWidth(this);
    height = img.getHeight(this);
    sWidth = width;
    sHeight = height;
    init();
  }
  
  
  public Sprite(Image im, int destX, int destY){
    this(im);
    dx = destX;
    dy = destY;
    init();
  }
  
  public Sprite(Image im, int destx, int desty, int sWid, int sHei){
    this(im, destx, desty);
    sWidth = sWid;
    sHeight = sHei;
    init();
  }
  
  public int priority(){
    return dy + priori;
  }
  
  public void setCoordinates(DrawTree.Node node, DrawTree tree, int destX, int destY){
    dx = destX;
    dy = destY;
    setBounds(dx, dy, sWidth, sHeight);
    //node.requestMove(tree);
    init();
  }
  
  public void setCoordinates(DrawTree.Node node, DrawTree tree, Point p){
    dx = p.x;
    dy = p.y;
    setBounds(dx, dy, sWidth, sHeight);
    //node.requestMove(tree);
    init();
  }
  
  public void init(){
    setBounds(dx-sWidth/2, dy-sHeight/2, sWidth, sHeight);
    bounds = new Rectangle(dx-sWidth/2, dy-sHeight/2, sWidth, sHeight);
  }
  
  public double calculateDistance(Point a, Point b){
    return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
  }
  
  public void paint(Graphics g){
    if(img == null || stopPainting || !updateSprite)
      return;
    g.drawImage(img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2), x, y, x+sWidth, y+sHeight, this);
  }
  
  //...weird
  public void update(Graphics g){
    paint(g);
    //updateSprite = false;
  }
  /*
  public void repaint(){
    super.repaint();
    //updateSprite = false;
  }
  */
}
