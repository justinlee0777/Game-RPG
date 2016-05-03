import java.awt.*;
//import java.awt.image.BufferedImage;
import java.awt.event.*;
//import java.lang.Thread;

public class GameFrame extends Frame{
  private FieldPanel fPanel;
  PlayerParty pp;
  //FieldEnemy e;
  //Image fieldImage, battleImage;
  //Graphics fieldg, battleg;
  //boolean flipField = true;
  
  public GameFrame(){
    addWindowListener(new WindowAdapter(){
      public void windowClosed(WindowEvent e){dispose(); System.exit(1);}
      public void windowClosing(WindowEvent e){dispose(); System.exit(1);}
    });
    Member[] c = new Member[1];
    c[0] = BattlerConstructor.makeAGoodBattler(BattlerConstructor.Fighter.Shadow);
    //c[1] = BattlerConstructor.makeAGoodBattler(BattlerConstructor.Fighter.Bruiser);
    //c[2] = BattlerConstructor.makeAGoodBattler(BattlerConstructor.Fighter.Gem);
    pp = new PlayerParty(c, 1);
    setBounds(new Rectangle(800, 800));
    //addFieldPanel();
    setResizable(false);
    setVisible(true);
    //SwitchThread t = new SwitchThread();
    //t.start();
    toFront();
    addHierarchyListener(new RequestFocusListener());
    fPanel = new FieldPanel(pp);
    fPanel.setBounds(new Rectangle(getX(), getY(), getWidth(), getHeight()));
    fPanel.setVisible(true);
    add(fPanel);
    fPanel.init();
    pack();
  }
  
  public void update(Graphics g){repaint();}
  
}