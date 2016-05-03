//package Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.Dimension;

public class AnimatedScript extends Sprite{
  private Sprite sprite;
  private AnimatedScript[] sprites;
  private Rectangle iterated;
  private int num, numOfCycles;
  private volatile boolean ceased;
  private Size movement;
  private int counter;
  private int changeCount = 0;
  private Point2D.Float virtualPoint;
  private boolean moreSprites = false;
  //various modes
  private boolean setSprite = false;
  private Point setFrame;
  private boolean jump = false;
  private float jumpOffset = 0f;
  private float shadowdy = 0f;
  private boolean awaken = false;
  //camera
  private boolean camFollow = false;
  private boolean camExtend = false;
  private Grid grid;
  private Rectangle2D.Float virtualCam;
  private float focusX;
  private float focusY;
  protected float camIncX;
  protected float camIncY;
  //DrawTree-related stuff
  protected boolean adjustTree;
  protected DrawTree.Node node;
  protected DrawTree tree;
  
  
  //movement in distance; rate is determined by sprite size
  public AnimatedScript(Sprite s, int disX, int disY, Rectangle rect){
    sprite = s;
    iterated = rect;
    num = 0;
    double distance = s.calculateDistance(new Point(s.dx, s.dy), new Point(s.dx+disX, s.dy+disY));
    float distRatio = 1.0f/(Math.abs(disX)+Math.abs(disY));
    numOfCycles = (int)Math.abs(distance/2);
    movement = new Size( (2*distRatio*disX), (2*distRatio*disY));
    ceased = false;
    moreSprites = false;
    counter = 2;
    init();
  }
  
  public AnimatedScript(Sprite s, int disX, int disY, Rectangle rect, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, disX, disY, rect);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //move to a point
  public AnimatedScript(Sprite s, Point p, Rectangle rect){
    this(s, p.x-s.dx, p.y-s.dy, rect);
  }
  
  public AnimatedScript(Sprite s, Point p, Rectangle rect, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, p, rect);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //move in distance changing sprites after a certain number of cycles (int count)
  public AnimatedScript(Sprite s, int disX, int disY, int count, Rectangle rect){
    /*
    sprite = s;
    iterated = rect;
    num = 0;
    double distance = s.calculateDistance(new Point(s.dx, s.dy), new Point(s.dx+disX, s.dy+disY));
    float distRatio = 1.0f/(Math.abs(disX)+Math.abs(disY));
    numOfCycles = (int)Math.abs(distance/2);
    movement = new Size( (2*distRatio*disX), (2*distRatio*disY));
    ceased = false;
    moreSprites = false;
    */
    this(s, disX, disY, rect);
    counter = count;
    init();
  }
  
  public AnimatedScript(Sprite s, int disX, int disY, int count, Rectangle rect, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, disX, disY, count, rect);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //move to a point changing sprites after a certain number of cycles (int count)
  public AnimatedScript(Sprite s, Point p, int count, Rectangle rect){
    this(s, p, rect);
    counter = count;
    init();
  }
  
  public AnimatedScript(Sprite s, Point p, int count, Rectangle rect, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, p, count, rect);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //move in distance with camera following
  public AnimatedScript(Sprite s, int disX, int disY, int count, Rectangle rect, Grid cam){
    this(s, disX, disY, rect);
    camFollow = true;
    counter = count;
    grid = cam;
    init();
  }

  public AnimatedScript(Sprite s, int disX, int disY, int count, Rectangle rect, Grid cam, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, disX, disY, count, rect, cam);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //move to a point with camera following
  public AnimatedScript(Sprite s, Point p, int count, Rectangle rect, Grid cam){
    this(s, p, rect);
    camFollow = true;
    grid = cam;
    counter = count;
    init();
  }
  
  public AnimatedScript(Sprite s, Point p, int count, Rectangle rect, Grid cam, DrawTree.Node selfNode, DrawTree drawTree){
    this(s, p, count, rect, cam);
    node = selfNode;
    adjustTree = true;
    tree = drawTree;
  }
  
  //change sprite after cycles (no movement)
  public AnimatedScript(Sprite s, Rectangle rect, int cycles){
    sprite = s;
    iterated = rect;
    num = 0;
    double distance = 0;
    float distRatio = 0;
    movement = new Size(0, 0);
    ceased = false;
    moreSprites = false;
    numOfCycles = cycles;
    counter = 1;
    init();
  }
  
  //change sprite after count, repeat based on cycles
  public AnimatedScript(Sprite s, Rectangle rect, int cycles, int count){
    this(s, rect, cycles);
    counter = count;
    init();
  }
  
  //change sprite once
  public AnimatedScript(Sprite s, Point frame){
    sprite = s;
    setFrame = frame;
    setSprite = true;
    ceased = false;
    moreSprites = false;
    init();
  }
  
  //move all ActorSprites to a specific point (used primarily to return characters to FieldCharacter sprite)
  public AnimatedScript(ActorSprite[] s, Point p, int count, Rectangle[] rect){
    sprites = new AnimatedScript[s.length];
    for(int i = 0; i < s.length; i++){
      sprites[i] = new AnimatedScript(s[i], p, count, rect[i]);
      sprites[i].init();
    }
    ceased = false;
    moreSprites = true;
  }
  
  //move all ActorSprites to a specific point without count
  public AnimatedScript(NPC[] s, Point p, Rectangle[] rect){
    sprites = new AnimatedScript[s.length];
    for(int i = 0; i < s.length; i++){
      sprites[i] = new AnimatedScript(s[i], p, rect[i]);
      sprites[i].init();
    }
    ceased = false;
    moreSprites = true;
  }
  
  //move all ActorSprites to a specific point without specific frames
  public AnimatedScript(NPC[] s, Point p){
    sprites = new AnimatedScript[s.length];
    for(int i = 0; i < s.length; i++){
      sprites[i] = new AnimatedScript(s[i], p, s[i].getWalking(ActorSprite.determineDirection(new Point(s[i].dx, s[i].dy), p)));
      sprites[i].init();
    }
    ceased = false;
    moreSprites = true;
  }
  
  //do many AnimatedScripts at once
  public AnimatedScript(AnimatedScript[] scripts){
    sprites = scripts;
    ceased = false;
    moreSprites = true;
  }
  
  //for jumping
  public AnimatedScript(Sprite s, int disX, int disY, int cycles, boolean jmp, Rectangle rect){
    sprite = s;
    iterated = rect;
    num = 0;
    numOfCycles = (cycles*2);
    movement = new Size( (float)disX/(float)numOfCycles, (float)disY/(float)numOfCycles );
    jumpOffset = cycles*.4905f;
    ceased = false;
    moreSprites = false;
    jump = jmp;
    counter = 2;
    init();
  }
  
  //jump to a point
  public AnimatedScript(Sprite s, Point p, int cycles, boolean jmp, Rectangle rect){
    this(s, p.x-s.dx, p.y-s.dy, cycles, jmp, rect);
    counter = 2;
  }
  
  //jump from sleep
  public AnimatedScript(boolean sleep, NPC s, int disX, int disY, int cycles, Rectangle rect){
    this(s, disX, disY, cycles, true, rect);
    awaken = sleep;
    init();
  }
  
  //zoom in camera based on increments
  public AnimatedScript(Grid currGrid, int numCycles, float incX, float incY){
    grid = currGrid;
    numOfCycles = numCycles;
    num = 0;
    camIncX = incX;
    camIncY = incY;
    focusX = camIncX;
    focusY = camIncY;
    virtualCam = new Rectangle2D.Float(currGrid.camera.x, currGrid.camera.y, currGrid.camera.width, currGrid.camera.height);
    camExtend = true;
  }
  
  //zoom in camera to an ideal size
  public AnimatedScript(Grid currGrid, int numCycles, Point on_center, Size cam_size){
    //move camera to center on point
    grid = currGrid;
    numOfCycles = numCycles;
    num = 0;
    camIncX = (currGrid.camera.width-cam_size.width)/numOfCycles;
    camIncY = (currGrid.camera.height-cam_size.height)/numOfCycles;
    focusX = ( (float)(on_center.x-currGrid.camera.x-cam_size.width/2) )/numOfCycles;
    focusY = ( (float)(on_center.y-currGrid.camera.y-cam_size.height/2) )/numOfCycles;
    virtualCam = new Rectangle2D.Float(currGrid.camera.x, currGrid.camera.y, currGrid.camera.width, currGrid.camera.height);
    camExtend = true;
  }
  
  public boolean hasStopped(){return ceased;}
  
  public synchronized void paint(Graphics g){
    if(setSprite){
      sprite.updateSprite = true;
      if(ceased)
        return;
      sprite.x = setFrame.x;
      sprite.y = setFrame.y;
      ceased = true;
      return;
    }
    if(ceased)
      return;
    if(camExtend){
      virtualCam.x+=focusX;
      virtualCam.y+=focusY;
      virtualCam.width-=camIncX;
      virtualCam.height-=camIncY;
      grid.camera.x = (int)virtualCam.x;
      grid.camera.y = (int)virtualCam.y;
      grid.camera.width = (int)virtualCam.width;
      grid.camera.height = (int)virtualCam.height;    
      num++;
      if(num >= numOfCycles){
        ceased = true;
      }
      return;
    }
    if(num == 0 && !moreSprites){
      sprite.updateSprite = true;
      sprite.x = iterated.x;
      sprite.y = iterated.y;
      shadowdy = sprite.dy+sprite.sHeight*3/8;
      virtualPoint = new Point2D.Float(sprite.dx, sprite.dy);
    } 
    if(moreSprites){
      boolean b = true;
      for(int i = 0; i < sprites.length; i++){
        sprites[i].paint(g);
        b = b && sprites[i].hasStopped();
      }
      ceased = b;
    }
    else{
      if(num < numOfCycles){
        //System.out.println(virtualPoint.x + " " + virtualPoint.y);
        if(jump){
          jumpOffset-=.5f;
          shadowdy+=movement.height;
          g.setColor(Color.BLACK);
          ((Graphics2D)g).draw(new Ellipse2D.Double(sprite.dx-sprite.sWidth/3, (int)shadowdy, sprite.sWidth*2/3, sprite.sHeight/2));
          ((Graphics2D)g).fill(new Ellipse2D.Double(sprite.dx-sprite.sWidth/3, (int)shadowdy, sprite.sWidth*2/3, sprite.sHeight/2));
          g.setColor(Color.WHITE);
        }
        changeCount++;
        if(changeCount == counter){
          sprite.x+=sprite.sWidth;
          changeCount = 0;
        }
        virtualPoint.x+=movement.width;
        virtualPoint.y+=(movement.height-jumpOffset);
        if(camFollow){
          grid.setCamera(sprite);
          //grid.setCamera(sprite, (int)virtualPoint.x - sprite.dx, (int)virtualPoint.y - sprite.dy );
        }
        sprite.dx = (int)virtualPoint.x;
        sprite.dy = (int)virtualPoint.y;
        if(sprite.x >= iterated.x+iterated.width){
          sprite.x = iterated.x;
        }   

        num++;  
      }
/*
      if(adjustTree){
        node.requestMove(tree);
      }
*/
      if(num >= numOfCycles){
        ceased = true;
        sprite.x = iterated.x;
        if(awaken)
          ((NPC)sprite).putAwake();
      }
    }
  }
}
