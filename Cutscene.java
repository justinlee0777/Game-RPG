//package Game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Cutscene extends Sprite implements KeyListener{
  private Field field;
  private FieldCharacter fChar;
  private Queue<Incident> in;
  //private Incident currInc;
  private IncidentHandler inHandle;
  //private Graphics2D fieldG;
  private volatile boolean messageOver = false;
  public volatile boolean isFinished = false;
  
  private volatile boolean forceBattle = false;
  public volatile FieldEnemy fEn;
  
  //place Canvas over Field, drawing Field onto Canvas continually
  //make Queue at last instance...hmm
  public Cutscene(Field f, FieldCharacter fc, /*BufferedImage bImg,*/ Queue<Incident> incQ){
    //super(bImg);
    field = f;
    fChar = fc;
    in = incQ;
    addKeyListener(this);
    setBounds(0, 0, field.width*(int)zoom.width, field.height*(int)zoom.height);
    Progress p = new Progress();
    p.start();
  }
  
  public class IncidentHandler{
    Queue<Sprite> sequence;
    Sprite currS;
    public IncidentHandler(Queue<Sprite> seq){
      sequence = seq;
      forward();
      sequence = seq;
    }
    
    public boolean hasStopped(){
      if(currS == null) return true;
      if(currS instanceof AnimatedScript)
        return ((AnimatedScript)currS).hasStopped();
      if(currS instanceof MessageBox){
        return messageOver;
      }
      return true;
    }
    
    public void forward(){
      if(hasStopped()){
        currS = sequence.pop();
        if(currS instanceof AnimatedScript)
          field.addTempSprite((AnimatedScript)currS);
        messageOver = false;
      }
    }
    
    public boolean isFinished(){return sequence.empty() && hasStopped();}
  }
  
  public boolean forceBattle(){
    return forceBattle;
  }
  public void outBattle(){forceBattle = false;}
  
  public void keyPressed(KeyEvent e){
    if(e.getKeyCode() == KeyEvent.VK_J){
      messageOver = true;
    }
  }
  public void keyReleased(KeyEvent e){}
  public void keyTyped(KeyEvent e){}
  
  
  public void paint(Graphics g){
    if(inHandle != null && inHandle.currS instanceof MessageBox)
      inHandle.currS.paint(g);
  }
  
  public class Progress extends Thread{
    
    public void run(){
      while(!in.empty()){
        Incident i = in.pop();
        if(i.isBattle){
          fEn = i.en;
          forceBattle = true;
          while(forceBattle){};
        }
        else{
          inHandle = new IncidentHandler(i.activate(field, null, fChar));
          do{
            inHandle.forward();
          }while(!inHandle.isFinished());
        }
      }
      isFinished = true;
    }
  }
  
}
