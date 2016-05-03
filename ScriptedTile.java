//package Game;

import java.awt.image.BufferedImage;

public class ScriptedTile extends IncidentTile{
  public ScriptedTile(int dx, int dy, int width, int height, boolean pass, Queue<Incident> incidents){
    super(dx, dy, width, height, pass, incidents);
    isScripted = true;
  }
  
  public Cutscene use(Field f, BufferedImage bImg, FieldCharacter c){
    if(in.empty())
      return null;
    return new Cutscene(f, c, in);
  }
  
}