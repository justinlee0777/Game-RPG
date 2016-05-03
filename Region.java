/*package Game;

import java.awt.*;

public class Region{
 
  public enum Floor{
    Dome, Tutorial, Tower;
  }
  
  //where the FieldCharacter begins should depend on story
  
  //hmmm. Remember that the 'cNode' is a single-assign, so the grid to put char in should be last.
  //(stupid. fix that)
  
  public static Grid makeFloor(Floor f, FieldCharacter c){
    Grid[] grids;
    switch(f){
      case Dome: 
        grids = new Grid[1];
        grids[0] = new Classroom(c);
        //c.setCoordinates(c.cNode, new Point(grids[0].dtot(5), grids[0].dtot(9)));
        grids[0].setCamera(c);
        return grids[0];
      case Tutorial:
        grids = new Grid[2];
        grids[0] = new Bedroom(c);
        grids[1] = new Hallway(c);
        //c.setCoordinates(c.cNode, grids[0].dtot(5), grids[0].dtot(2));
        grids[0].setEastGrid(grids[1]);
        grids[1].setWestGrid(grids[0]);
        grids[0].setCamera(c);
        //c.putSleep();
        //grids[0].putIntoBed(c.cNode, c);
        return grids[0];
      case Tower:
        grids = new Grid[1];
        grids[0] = new Gym(c);
        //c.setCoordinates(c.cNode, grids[0].dtot(0), grids[0].dtot(0));
        return grids[0];
    }
    return null;
  } 
  
  public static Grid makeFloor(Floor f, FieldCharacter c, int g, Point cPoint){
    Grid[] grids;
    switch(f){
      case Dome: 
        grids = new Grid[1];
        grids[0] = new Classroom(c);
        //c.setCoordinates(c.cNode, cPoint);
        grids[0].setCamera(c);
        return grids[g];
      case Tutorial:
        grids = new Grid[2];
        grids[0] = new Bedroom(c);
        grids[1] = new Hallway(c);
        //c.setCoordinates(c.cNode, cPoint);
        grids[0].setEastGrid(grids[1]);
        grids[1].setWestGrid(grids[0]);
        grids[g].setCamera(c);
        return grids[g];
      case Tower:
        grids = new Grid[1];
        grids[0] = new Gym(c);
        //c.setCoordinates(c.cNode, cPoint);
        grids[g].setCamera(c);
        return grids[g];
    }
    return null;
  }
  
}*/