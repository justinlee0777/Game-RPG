//package Game;

import java.awt.*;

public class DialogueTile extends IncidentTile{
  
  public DialogueTile(){
    super();
    isPassable = false;
    isTalkable = true;
  }
  
  public DialogueTile(int dx, int dy, int width, int height, Queue<Incident> incidents){
    super(dx, dy, width, height, incidents);
    isPassable = false;
    isTalkable = true;
  }
  
  public DialogueTile(String file, int dx, int dy, int width, int height, Queue<Incident> incidents){
    super(file, dx, dy, width, height, incidents);
    isPassable = false;
    isTalkable = true;
  }
  
  public DialogueTile(Image img, int dx, int dy, int width, int height, Queue<Incident> incidents){
    super(img, dx, dy, width, height, incidents);
    isPassable = false;
    isTalkable = true;
  }
  
  public DialogueTile(Tile[][] tiles, int wid, int hei, Queue<Incident> incidents){
    super(tiles, wid, hei, incidents); 
    isPassable = false;
    isTalkable = true;
  }
  
  public DialogueTile(String file, Tile[][] tiles, int wid, int hei, Queue<Incident> incidents){
    super(file, tiles, wid, hei, incidents); 
    isPassable = false;
    isTalkable = true;
  }
  
  public Queue<Sprite> use(Field field, Tile actor, FieldCharacter c){
    Incident i = in.pop();
    in.add(i);
    //System.out.println("Queue size: " + in.size());
    return i.activate(field, actor, c);
  }
}