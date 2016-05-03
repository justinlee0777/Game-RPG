import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

public class FieldPanel extends Panel{
  private FieldCharacter sprite;
  private Field field;
  //private Cutscene cutscene;
  private Timer time;
  private ArtTimer at;
  
  public FieldPanel(PlayerParty p){

    sprite = new FieldCharacter("Game/Shadow.png", p, ActorConstructor.makeNPC(ActorConstructor.Actor.Shadow, 0, 0, null), new Rectangle(0, 0, 0, 0));
    field = new Field(sprite, p);

    add(field);
    setFocusable(false);
    setVisible(true);
    time = new Timer();    
    setPreferredSize(new Dimension(144*(int)Sprite.zoom.width, 192*(int)Sprite.zoom.height));
  }
  
  public Field field(){return field;}

  public void init(){
    at = new ArtTimer();
    resume(at);
    field.orderResume();
  }
  
  public void stop(){pause(at);}
  
  private void pause(TimerTask ta){
    ta.cancel();
    ta = null;
  }
  
  private void resume(TimerTask ta){time.schedule(ta, 0, 50);}
    
  //private void resume(TimerTask ta, long milliseconds){time.schedule(ta, milliseconds, 50);}  
  
  public class ArtTimer extends TimerTask{ 
    public void run(){
      field.repaint();
    }
  }
 
}
