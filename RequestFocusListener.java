//package Game;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;

public class RequestFocusListener implements HierarchyListener {
  
  public void hierarchyChanged(HierarchyEvent e) {
    final Component c = e.getComponent();
    if (c.isShowing() && (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
      c.requestFocus();
      /*Window toplevel = SwingUtilities.getWindowAncestor(c);
      toplevel.addWindowFocusListener(new WindowAdapter() {
        
        public void windowGainedFocus(WindowEvent e) {
          c.requestFocus();
        }
      });*/
    }
  }
}