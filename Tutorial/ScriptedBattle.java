//package Game.Tutorial;
//import Game.*;

import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;

public class ScriptedBattle extends Battle{
  boolean[] lockedCommand;
  int turns = 0;
  int numTurns = 0;
  public ScriptedBattle(int destx, int desty, int wid, int hei, Field fld, BattleCommand turn, FieldCharacter fCh, FieldEnemy fEn, AnimatedScript[] pos, boolean[] locks){
    super(destx, desty, wid, hei, fld, turn, fCh, fEn, pos);
    lockedCommand = locks;
    fightMenu = new ScriptedSelectable(0, height*3/4, width, height/4);
  }

  public ScriptedBattle(int destx, int desty, int wid, int hei, Field fld, BattleCommand turn, FieldCharacter fCh, FieldEnemy fEn, AnimatedScript[] pos, boolean[] locks, int trns){
    super(destx, desty, wid, hei, fld, turn, fCh, fEn, pos);
    lockedCommand = locks;
    turns = trns;
    fightMenu = new ScriptedSelectable(0, height*3/4, width, height/4);
  }
  
  public class ScriptedSelectable extends Battle.FightSelectable{
    public ScriptedSelectable(int destx, int desty, int w, int h){
      super(destx, desty, w, h);
      for(int i = 0; i < lockedCommand.length; i++){
        if(lockedCommand[i]){
          try{
            options[i] = ImageIO.read(getClass().getResource("Sprites/No Skill.png"));
          }catch(IOException e){JOptionPane.showMessageDialog(null, "ScriptedSelectable: Cannot open file");}
          
        }
      }
    }
    
    public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_M)
        return;
      if(e.getKeyCode() == KeyEvent.VK_J && lockedCommand[fCounter])
        return;
      super.keyPressed(e);
    }
  }
  
  public void checkEnd(){
    if(++numTurns == turns)
      pcEnd = true;
    //System.out.println(pcEnd);
    super.checkEnd();
  }
  
}