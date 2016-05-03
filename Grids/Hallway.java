import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Hallway extends Grid{
  public Hallway(FieldCharacter c){
    super(c);
    c.cNode = createGrid(c, null);
  }
  
  public Hallway(FieldCharacter c, int ste){
    super(c, ste);
    c.cNode = createGrid(c, null);
  }
  
  public Hallway(FieldCharacter c, Story story, int ste){
    super(c, ste);
    c.cNode = createGrid(c, story);
  }
  
  public Grid getNorthGrid(FieldCharacter c, Story story){ return null; }
  public Grid getEastGrid(FieldCharacter c, Story story){ return null; }
  public Grid getSouthGrid(FieldCharacter c, Story story) { return null; } 
  public Grid getWestGrid(FieldCharacter c, Story story) { return story.getGrid(c, "Bedroom"); }
  
  public DrawTree.Node createGrid(FieldCharacter c, Story story){
    int tw, ty;
    BufferedImage tile = null;
    Queue<Incident> incQ;
    
    tile = imageCreator.makeImage("Sprites/Tiles/Blue Tile.png");
    init(480, 640, tile);
    tw = tileSize.width;
    ty = tileSize.height;
    westP = new Point(dtot(13), dtot(3));
    camera = new Rectangle(0, 0, tw*9, ty*12);
    s_size = new Size(tw*9, ty*12);
    setCamera(c);
    
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, 0, tWidth, 2);
    for(int j = 2; j < tHeight-8; j+=3){
      //against doors
      makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, j, 13, j+2);
      makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 17, j, tWidth, j+2);
      //against rooms
      makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, j+2, 5, j+3);
      makeImpassableTiles("Sprites/Tiles/Blue Impass.png", tWidth-7, j+2, tWidth, j+3);
      //rooms
      makeImpassableTiles("Sprites/Tiles/Blue Residual.png", 7, j+2, 12, j+3);
      makeImpassableTiles("Sprites/Tiles/Blue Residual.png", 18, j+2, tWidth-7, j+3);
      makeImpassableTiles("Sprites/Tiles/Blue Residual.png", 7, j+1, 11, j+3);
      makeImpassableTiles("Sprites/Tiles/Blue Residual.png", 19, j+1, tWidth-7, j+3);
      addDoor(new Tile(true, "Game/Door.png", tiles[12][j+2], 16, 16));
      addDoor(new Tile(true, "Game/Door.png", tiles[17][j+2], 16, 16));   
    }
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, tHeight-8, 13, tHeight-5);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 17, tHeight-8, tWidth, tHeight-5);
    
    switch(state){
      case 1: {
        NPC bruiser = ActorConstructor.makeNPC(ActorConstructor.Actor.Bruiser, dtot(14), dtot(3), null);
        NPC shadow = ActorConstructor.makeNPC(ActorConstructor.Actor.Shadow, dtot(0), dtot(0), null);
        
        putTilesInTree(tiles);
        DrawTree.Node cNode = drawTree.add(c);
        DrawTree.Node bNode = addFixture(bruiser);
        
        incQ = new Queue<Incident>();
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, tw*1, 0, 2, bruiser.right, bNode, drawTree), new AnimatedScript(c, tw*1, 0, 2, c.right, Hallway.this, cNode, drawTree)}));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, 0, ty*34, 2, bruiser.down, bNode, drawTree), new AnimatedScript(c, 0, ty*34, 2, c.down, Hallway.this, cNode, drawTree)}));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, tw*17, 0, 2, bruiser.right, bNode, drawTree), new AnimatedScript(c, tw*17, 0, 2, c.right, Hallway.this, cNode, drawTree)}));
        incQ.add(new Incident(story));
        incQ.add(new Incident(story, Story.Region.Tower, "Gym", /*new Point(dtot(4), dtot(11)),*/ new NPC[]{shadow, bruiser}));
        addScriptedTile(new ScriptedTile(dtot(13), dtot(3), tw*2, ty*2, true, incQ));
        
        //return drawTree.add(c);
        return cNode;
      }
    }
    
    putTilesInTree(tiles);
    return drawTree.add(c);
  }
    
}