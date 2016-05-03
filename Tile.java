//package Game;

import java.awt.*;

public class Tile extends Sprite{
  protected boolean isPassable = true;
  public boolean isTalkable = false;
  public boolean isScripted = false;
  //for bottom tiles
  public Tile overlappingTile; 
  public Tile tileOverlaps;
  //for top tiles
  public Tile[][] tilesOverlapped;
  int xNumTiles, yNumTiles;
  //portion of overlap tile to draw
  Rectangle overA;
  boolean spreadOut = false;
  NPC sleeper;
  boolean isBed = false;
  //switch on step
  boolean isDoor;
  boolean changeStep;
  Rectangle stepSprite;
  
  public Tile(){
    super();
    init();
  }
  
  public Tile(int destx, int desty){
    super(destx, desty, 16, 16);
    init();
  }
  
  public Tile(int destx, int desty, int wid, int hei){
    super(destx, desty, wid, hei);
    setBounds(dx, dy, width, height);
    init();
  }
  
  public Tile(String file, int destx, int desty){
    super(file, destx, desty);
    setBounds(dx, dy, width, height);
    init();
  }
  
  public Tile(String file, int destx, int desty, boolean pass){
    this(file, destx, desty);
    isPassable = pass;
  }
  
  public Tile(String file, int destx, int desty, int wid, int hei){
    super(file, destx, desty, wid, hei);
    setBounds(dx, dy, width, height);
    init();
  }
  
  public Tile(String file, int destx, int desty, int wid, int hei, boolean pass){
    super(file, destx, desty, wid, hei);
    setBounds(dx, dy, width, height);
    isPassable = pass;
    //if(!isPassable) priori = 16;
    init();
  }
  
  public Tile(Image img, int destx, int desty){
    super(img, destx, desty);
    setBounds(dx, dy, width, height);
    init();
  }
  
  public Tile(Image img, int destx, int desty, int wid, int hei){
    super(img, destx, desty, wid, hei);
    setBounds(dx, dy, width, height);
    init();
  }
  
  public Tile(Image img, int destx, int desty, int wid, int hei, boolean pass){
    this(img, destx, desty, wid, hei);
    isPassable = pass;
  }
  
  public Tile(Tile[][] tiles, int wid, int hei){
    super();
    spreadOut = true;
    tilesOverlapped = tiles;
    xNumTiles = wid;
    yNumTiles = hei;
    sWidth = 0;
    for(int i = 0; i < xNumTiles; i++){
      sWidth += tilesOverlapped[i][0].sWidth;
      sHeight = 0;
      for(int j = 0; j < yNumTiles; j++){
        sHeight += tilesOverlapped[i][j].sHeight;
        tilesOverlapped[i][j].setOverlap(this);
        tilesOverlapped[i][j].overA = new Rectangle(i*tiles[i][j].sWidth, j*tiles[i][j].sHeight, tiles[i][j].sWidth, tiles[i][j].sHeight);
        tilesOverlapped[i][j].spreadOut = true;
      }
    }
    dx = tiles[0][0].dx-tiles[0][0].sWidth/2 + sWidth/2;
    dy = tiles[0][0].dy-tiles[0][0].sHeight/2 + sHeight/2;
    init();
  }
  
  public Tile(String file, Tile[][] tiles, int wid, int hei){
    super(file);
    spreadOut = true;
    tilesOverlapped = tiles;
    xNumTiles = wid;
    yNumTiles = hei;
    sWidth = 0;
    for(int i = 0; i < xNumTiles; i++){
      sWidth += tilesOverlapped[i][0].sWidth;
      sHeight = 0;
      for(int j = 0; j < yNumTiles; j++){
        sHeight += tilesOverlapped[i][j].sHeight;
        tilesOverlapped[i][j].setOverlap(this);
        tilesOverlapped[i][j].overA = new Rectangle(i*tiles[i][j].sWidth, j*tiles[i][j].sHeight, tiles[i][j].sWidth, tiles[i][j].sHeight);
        tilesOverlapped[i][j].spreadOut = true;
      }
    }
    dx = tiles[0][0].dx-tiles[0][0].sWidth/2 + sWidth/2;
    dy = tiles[0][0].dy-tiles[0][0].sHeight/2 + sHeight/2;
    init();
  }
  
  public Tile(String file, Tile[][] tiles, int wid, int hei, boolean pass){
    this(file, tiles, wid, hei);
    isPassable = pass;
  }
  
  public Tile(Image img, Tile[][] tiles, int wid, int hei, boolean pass){
    super(img);
    spreadOut = true;
    tilesOverlapped = tiles;
    xNumTiles = wid;
    yNumTiles = hei;
    sWidth = 0;
    for(int i = 0; i < xNumTiles; i++){
      sWidth += tilesOverlapped[i][0].sWidth;
      sHeight = 0;
      for(int j = 0; j < yNumTiles; j++){
        sHeight += tilesOverlapped[i][j].sHeight;
        tilesOverlapped[i][j].setOverlap(this);
        tilesOverlapped[i][j].overA = new Rectangle(i*tiles[i][j].sWidth, j*tiles[i][j].sHeight, tiles[i][j].sWidth, tiles[i][j].sHeight);
        tilesOverlapped[i][j].spreadOut = true;
      }
    }
    isPassable = pass;
    dx = tiles[0][0].dx-tiles[0][0].sWidth/2 + sWidth/2;
    dy = tiles[0][0].dy-tiles[0][0].sHeight/2 + sHeight/2;
    init();
  }
  
  public Tile(NPC sleep, Image bed, Tile[][] tiles){
    super(bed);
    isBed = true;
    sleeper = sleep;
    tilesOverlapped = tiles;
    dx = tiles[0][0].dx-tiles[0][0].sWidth/2;
    dy = tiles[0][0].dy-tiles[0][0].sHeight/2;
    xNumTiles = 2;
    yNumTiles = 2;
    sWidth = 32;
    sHeight = 32;
    for(int i = 0; i < xNumTiles; i++)
      for(int j = 0; j < yNumTiles; j++)
      tilesOverlapped[i][j].setOverlap(this);
    isPassable = false;
    //priori = 16;
    init();
  }
  
  public Tile(boolean door, String file, Tile overlap, int wid, int hei){
    super(file);
    sWidth = wid;
    sHeight = hei;
    tileOverlaps = overlap;
    overlap.overlappingTile = this;
    isDoor = door;
    init();
  }
  
  public boolean isPassable(){
    if(overlappingTile != null)
      return overlappingTile.isPassable;
    return isPassable;
  }
  public void setPassable(boolean pass){isPassable = pass;}
  
  
  public Tile tile(){
    if(overlappingTile == null)
      return this;
    return overlappingTile;
  }
  
  public int priority(){
    if(tile().img == null) return 0;
    int bonus = (!tile().isPassable()) ? 24 : 0;
    //if(isBed) bonus += ( (sleeper.sprite.sleeping) ? -24 : 0) ;
    return dy + bonus;//priori + bonus;
  }
  
  public void setOverlap(Tile t){
    overlappingTile = t;
    t.tileOverlaps = this;
    overA = new Rectangle(t.x, t.y, t.sWidth, t.sHeight);
  }
  
  public AnimatedScript returnOverlap(){
    Point p = new Point(tileOverlaps.dx, tileOverlaps.dy);
    if(!(this instanceof ActorTile))
      return null;
    ActorSprite act = ((ActorTile)this).sprite;
    return new AnimatedScript(this, p, Math.abs((int)act.calculateDistance(new Point(dx, dy), p))/(sWidth/2),
                              (p.x-dx > 0) ? act.right : act.left);                                                                                                  
  }
  
  public void paint(Graphics g){
    if(img == null)
      return;
    paintTile(g);
  }
  
  public void paintTile(Graphics g){
    if(isBed){
      g.drawImage(img, dx, dy, (dx+sWidth), (dy+sHeight), 0, 0, sWidth, sHeight, null);
      if(sleeper.isSleeping()) sleeper.paint(g);
      g.drawImage(img, dx, dy, dx+sWidth, (dy+sHeight), 32, 0, 64, sHeight, null);
      return;
    }
    super.paint(g);
    if(spreadOut && overlappingTile != null){
      g.drawImage(overlappingTile.img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2),
                  overA.x, overA.y, overA.x+overA.width, overA.y+overA.height, null);
    }
    else if(overlappingTile != null && overlappingTile.isDoor){
      g.drawImage(overlappingTile.img, dx-sWidth/2, dy-sHeight/2, (dx+sWidth/2), (dy+sHeight/2),
                  overlappingTile.x, overlappingTile.y, 
                  overlappingTile.x+overlappingTile.sWidth, overlappingTile.y+overlappingTile.sHeight, null);
    }
    else if(overlappingTile != null)
      tile().paint(g);
  }
  
}