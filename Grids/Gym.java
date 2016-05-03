import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;


public class Gym extends Grid{
  
  public Gym(FieldCharacter c){
    super(c);
    c.cNode = createGrid(c, null);
  }
  
  public Gym(FieldCharacter c, int ste){
    super(c, ste);
    c.cNode = createGrid(c, null);
  }
  
  public Gym(FieldCharacter c, Story story, int ste){
    super(c, ste);
    c.cNode = createGrid(c, story);
  }
  
  public Grid getNorthGrid(FieldCharacter c, Story story){ return null; }
  public Grid getEastGrid(FieldCharacter c, Story story){ return null; }
  public Grid getSouthGrid(FieldCharacter c, Story story) { return null; } 
  public Grid getWestGrid(FieldCharacter c, Story story) { return null; }
  
  public DrawTree.Node createGrid(FieldCharacter c, Story story){
    int tw, ty;
    BufferedImage tile = null;
    Queue<Incident> incQ; 
    
    tile = imageCreator.makeImage("Sprites/Tiles/Green Tile.png");
    init(320, 320, tile);
    tw = tileSize.width;
    ty = tileSize.height;
    camera = new Rectangle(0, 0, tw*9, ty*12);
    s_size = new Size(tw*9, ty*12);
    setCamera(c);
    
    makeImpassableTiles("Sprites/Tiles/Green Impass.png", 0, 0, 2, 1);
    makeImpassableTiles("Sprites/Tiles/Green Impass.png", tWidth-2, 0, tWidth, 1);
    makeImpassableTiles("Sprites/Tiles/Green Impass.png", 0, tHeight-1, 2, tHeight);
    makeImpassableTiles("Sprites/Tiles/Green Impass.png", tWidth-2, tHeight-1, tWidth, tHeight);
    for(int i = 0; i < tWidth-5; i+=3){
      addObject(imageCreator.makeImage(Segment.Bleachers, 0), 2+i, 0, 5, 1);
      addObject(imageCreator.makeImage(Segment.Bleachers, 0), 2+i, 1, 5, 1);
      addObject(imageCreator.makeImage(Segment.Bleachers, 1), 2+i, 2, 5, 1);
    }
    
    //instantiate all characters here: frog, character, all the misc people
    //c.setPose(ActorSprite.Direction.Right);
    switch(state){
      case 1: {
        camera = new Rectangle(0, 0, 320, 320);
        FieldEnemy frog = new FieldEnemy(c, Gym.this, ActorConstructor.makeNPC(ActorConstructor.Actor.Frog, dtot(8), dtot(11), ActorSprite.Direction.Left, null), 
                                         BattlerConstructor.makeEnemies(new BattlerConstructor.Fighter[]{BattlerConstructor.Fighter.Frog}, 1, c.pparty), "Game/Frog.png", 
                                         new Size(20, 20), true, new boolean[]{false, true, true, true, true}, 9);
        frog.setPose(ActorSprite.Direction.Left);
        addSprite(frog);
        NPC nanna = ActorConstructor.makeNPC(ActorConstructor.Actor.Nanna, dtot(6), dtot(11), null);
        addFixture(nanna);
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Amaury, dtot(5), dtot(1), null));
        NPC loader = ActorConstructor.makeNPC(ActorConstructor.Actor.Loader, dtot(8), dtot(3), null);
        addFixture(loader);
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Meowly, dtot(6), dtot(5), null));
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.MireKnight, dtot(2), dtot(5), null));
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Myrton, dtot(1), dtot(1), null));
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Quetzal, dtot(3), dtot(0), null));
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Schlatt, dtot(8), dtot(1), null));
        NPC terry = ActorConstructor.makeNPC(ActorConstructor.Actor.Terry, dtot(6), dtot(2), null);
        addFixture(terry);
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Uniman, dtot(5), dtot(2), null));
        addFixture(ActorConstructor.makeNPC(ActorConstructor.Actor.Wingo, dtot(7), dtot(4), null));
        NPC wolfrang = ActorConstructor.makeNPC(ActorConstructor.Actor.Wolfrang, dtot(8), dtot(5), null);
        addFixture(wolfrang);
        incQ = new Queue<Incident>();
        incQ.add(new Incident(new AnimatedScript(Gym.this, 100, new Point(dtot(5), dtot(11)), new Size(tw*9, ty*12))));
        incQ.add(new Incident(nanna, "Let's start today's practice rounds.", Gym.this));
        incQ.add(new Incident(new AnimatedScript(nanna, new Point(54, 0))));
        incQ.add(new Incident(nanna, "Zeke, you tie with Geropei in this class.", Gym.this));
        incQ.add(new Incident(c, "Yes...", Gym.this));
        incQ.add(new Incident(new AnimatedScript(nanna, 0, -ty*2, 2, nanna.up))); 
        incQ.add(new Incident(new AnimatedScript(nanna, new Point(0,0))));
        incQ.add(new Incident(nanna, "In this round anything goes: powers, magic items, whatever. These exercises are chiefly to prove one's general prowess in combat.", Gym.this));
        incQ.add(new Incident(new AnimatedScript(c, new Point(90, 0))));
        incQ.add(new Incident(c, "Is it beyond reasoning to say that's the reason why I'm matched with Geropei?", Gym.this));
        incQ.add(new Incident(nanna, "You don't have to be so cynical about it! Now get together.", Gym.this));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(c, tw, 0, c.right), new AnimatedScript(frog, -tw, 0, frog.left)}));
        incQ.add(new Incident(frog, "Gehehe! After this match I'll never be thought as worse anymore.", Gym.this));
        incQ.add(new Incident(c, "Yes.", Gym.this));
        incQ.add(new Incident((FieldEnemy)frog, true));
        incQ.add(new Incident(nanna, "That's about everything I guess.", Gym.this));
        incQ.add(new Incident(new AnimatedScript(c, new Point(36,36))));
        incQ.add(new Incident(c, "That's ridiculous! What was that!", Gym.this));
        incQ.add(new Incident(new AnimatedScript(frog, new Point(0,20))));
        incQ.add(new Incident(frog, "Gehe! In last rank no more!", Gym.this));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(c, new Point(90,0)), new AnimatedScript(frog, new Point(160, 0))}));
        incQ.add(new Incident(nanna, "Zeke, don't you have an ability too?", Gym.this));
        incQ.add(new Incident(c, "Well; yes.", Gym.this));
        incQ.add(new Incident(new AnimatedScript[]{new AnimatedScript(c, new Point(54,0)), new AnimatedScript(frog, new Point(220, 0)), new AnimatedScript(nanna, new Point(0, 0))}));
        incQ.add(new Incident(nanna, "Let's try a redo.", Gym.this));
        incQ.add(new Incident((FieldEnemy)frog, new boolean[]{false, false, true, true, true}));
        incQ.add(new Incident((FieldEnemy)frog, true));
        incQ.add(new Incident(nanna, "Welp, that about settles it. Geropei goes up a rank.", Gym.this));
        incQ.add(new Incident(c, "...", Gym.this));
        incQ.add(new Incident(loader, "...", Gym.this));
        incQ.add(new Incident(terry, "...", Gym.this));
        incQ.add(new Incident(wolfrang, "...Hmph.", Gym.this));
        incQ.add(new Incident(true, BattleSprite.Exp.Combat, 100));
        incQ.add(new Incident(story));
        incQ.add(new Incident(story, Story.Region.Dome, "Classroom", /*new Point(dtot(5), dtot(9)),*/ new NPC[]{c, ActorConstructor.makeNPC(ActorConstructor.Actor.Bruiser, dtot(0), dtot(0), null)}));
        pre_scene.add(incQ);
        
        putTilesInTree(tiles);
        
        DrawTree.Node cNode = drawTree.add(c);
        c.setCoordinates(cNode, drawTree, dtot(4), dtot(11));
        
        return cNode;
      }
    }
    
    putTilesInTree(tiles);
    return drawTree.add(c);
  }

}
