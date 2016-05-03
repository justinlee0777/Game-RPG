import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Bedroom extends Grid{
  
  public Bedroom(FieldCharacter c){
    super(c);
    c.cNode = createGrid(c, null);
  }
  
  public Bedroom(FieldCharacter c, int ste){
    super(c, ste);
    c.cNode = createGrid(c, null);
  }
  
  public Bedroom(FieldCharacter c, Story story, int ste){
    super(c, ste);
    c.cNode = createGrid(c, story);
  }
  
  public Grid getNorthGrid(FieldCharacter c, Story story){ return null; }
  public Grid getEastGrid(FieldCharacter c, Story story){
    return story.getGrid(c, "Hallway");
  }
  public Grid getSouthGrid(FieldCharacter c, Story story) { return null; } 
  public Grid getWestGrid(FieldCharacter c, Story story) { return null; }
  
  public DrawTree.Node createGrid(FieldCharacter c, Story story){
    int tw, ty;
    BufferedImage tile = null;
    Queue<Incident> incQ; 
    
    tile = imageCreator.makeImage("Sprites/Tiles/Blue Tile.png");
    init(144, 192, tile);
    tw = tileSize.width;
    ty = tileSize.height;
    //northP = new Point(dtot(5), dtot(0));
    camera = new Rectangle(0, 0, tw*9, ty*12);
    s_size = new Size(tw*9, ty*12);
    setCamera(c);
    
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, 0, 1, tHeight);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 8, 0, tWidth, 8);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 8, 9, tWidth, tHeight);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, 0, tWidth, 1);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, 0, 1, tHeight);
    makeImpassableTiles("Sprites/Tiles/Blue Impass.png", 0, 11, tWidth, tHeight);
    addBed(c, 1, 2);
    addObject("Game/Window.png", 6, 0, 2, 1);
    addObject("Game/Radiator.png", 6, 1, 2, 1);
    addObject("Game/Bunk.png", 6, 2, 2, 2);
    Tile door = new Tile(true, "Game/Door.png", tiles[tWidth-1][8], 16, 16);
    //addDoor(door);
    switch(state){
      case 1: {
        Sprite instruction = new Sprite("Sprites/Tutorial Move.png", dtot(3), dtot(tHeight-2), 100);
        addSprite(instruction);
        
        NPC bruiser = ActorConstructor.makeNPC(ActorConstructor.Actor.Bruiser, dtot(tWidth+1), dtot(8), null);
        //bruiser = ActorConstructor.makeNPC(ActorConstructor.Actor.Bruiser, dtot(tWidth-4), dtot(8), null);
        
        DrawTree.Node cNode = drawTree.add(c);
        DrawTree.Node bNode = addFixture(bruiser);
        
        incQ = new Queue<Incident>();
        incQ.add(new Incident("Zzz...", Bedroom.this));
        incQ.add(new Incident(instruction, true));
        incQ.add(new Incident("!!!", Bedroom.this));
        incQ.add(new Incident(true, c, tw*2, 0, 10, c.right)); 
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, -tw*6, 0, bruiser.left, bNode, drawTree), new AnimatedScript(door, new Rectangle(16, 0, 32, 16), 16, 8)}));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, 0, -ty*4, bruiser.up, bNode, drawTree), new AnimatedScript(c, new Point(0, 0))}));
        incQ.add(new Incident(bruiser, "Good Morning!", Bedroom.this));
        incQ.add(new Incident(bruiser, "Ready to go to class?", Bedroom.this));
        incQ.add(new Incident(c, "You won't even let me brush my teeth first?", Bedroom.this));
        incQ.add(new Incident(bruiser, "I don't care! Let's get going.", Bedroom.this));
        incQ.add(new Incident(c, "Well, I'm inclined to agree.", Bedroom.this));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, 0, ty*4, 2, bruiser.down, bNode, drawTree), new AnimatedScript(c, 0, ty*4, 2, c.down, cNode, drawTree)}));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, tw*3, 0, 2, bruiser.right, bNode, drawTree), new AnimatedScript(c, tw*3, 0, 2, c.right, cNode, drawTree)}));
        incQ.add(new Incident(bruiser, "...", Bedroom.this));
        incQ.add(new Incident(bruiser, "Your door won't open.", Bedroom.this));
        incQ.add(new Incident(c, "Let me handle it.", Bedroom.this));
        incQ.add(new Incident(new AnimatedScript(bruiser, -tw*3, 0, 2, bruiser.left)));
        incQ.add(new Incident(new AnimatedScript(bruiser, new Rectangle(80, 0, 20, 20), 2)));
        incQ.add(new Incident(new FieldEnemy(c, Bedroom.this, new Point(dtot(tWidth-1), dtot(8)), new Size(16, 16),
                                             BattlerConstructor.EnemyType.Door, true, new boolean[]{false, true, true, true, true}, 0), true));
        incQ.add(new Incident(new AnimatedScript(door, new Point(32, 0))));
        incQ.add(new Incident(bruiser, "Great! Let's get going.", Bedroom.this));
        incQ.add(new Incident(new AnimatedScript(bruiser, tw*6, 0, 2, new Rectangle(80, 0, 120, 20))));
        incQ.add(new Incident(story));
        //incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(bruiser, tw*6, 0, 2, new Rectangle(80, 0, 120, 20)), new AnimatedScript(door, new Rectangle(32, 0, 16, 16), 2, 8)}));
        addScriptedTile(new ScriptedTile(dtot(1), dtot(1), tw*2, ty*2, true, incQ));
        pre_scene.add(incQ);
        
        putTilesInTree(tiles);
        
        c.putSleep();
        putIntoBed(cNode, drawTree, c);
        
        return cNode;
      }
    }
   
    putTilesInTree(tiles);
    DrawTree.Node cNode = drawTree.add(c);
    c.setCoordinates(cNode, drawTree, tw*4, ty*4);
    return cNode;
  }
  
}
