//package Game;

import java.awt.*;

public class IncidentTile extends Tile{
  protected Queue<Incident> in; 

  public IncidentTile(){
    super();
  }
  
  public IncidentTile(int destx, int desty, int wid, int hei, Queue<Incident> incidents){
    super(destx, desty, wid, hei);
    in = incidents;
  }
  
  public IncidentTile(int destx, int desty, int wid, int hei, boolean pass, Queue<Incident> incidents){
    super(destx, desty, wid, hei);
    isPassable = pass;
    in = incidents;
  }
  
  public IncidentTile(String file, int destx, int desty, int wid, int hei, Queue<Incident> incidents){
    super(file, destx, desty, wid, hei);
    in = incidents;
  }
  
  public IncidentTile(Image img, int destx, int desty, int wid, int hei, Queue<Incident> incidents){
    super(img, destx, desty, wid, hei);
    in = incidents;
  }
  
  public IncidentTile(String file, Tile[][] tiles, int wid, int hei, Queue<Incident> incidents){
    super(file, tiles, wid, hei); 
    in = incidents;
  }
  
  public IncidentTile(Tile[][] tiles, int wid, int hei, Queue<Incident> incidents){
    super(tiles, wid, hei); 
    in = incidents;
  }
  
  public void giveIncident(Queue<Incident> qIn){
    in = qIn;
  }
  
  public boolean hasIncident(Rectangle box){
    if(tilesOverlapped != null){
      boolean b = false;
      for(int i = 0; i < xNumTiles; i++)
        for(int j = 0; j < yNumTiles; j++){
        b = b || tilesOverlapped[i][j].bounds.intersects(box);
      }
      return b;
    }
    return bounds.intersects(box);
  }
  
  public Queue<Sprite> use(Field field, Tile actor, FieldCharacter c){
    if(in.empty())
      return null;
    Incident i = in.pop();
    return i.activate(field, actor, c);
  }
  
  public boolean noMoreUses(){return in.size() == 0;}
  
}