//package Game;

import java.awt.*;

public class ActorConstructor{
  public enum Actor{
    Alternado, Amaury, Anubis, Bruiser, Frog, Gem, Kurtle, LGalahad, Loader, Meowly, MireKnight, Myrton, Nanna, Otso, Quetzal, PGirl, Schlatt, Shadow, Terry, Uniman, Wingo, Wolfrang;
  }
  
  public static NPC makeNPC(Actor act, int destx, int desty, Queue<Incident> incidents){
    switch(act){
      case Alternado: return new NPC("Game/Alternado.png", destx, desty, 
                                     new ActorSprite("Game/Alternado.png", 0, 0, 20, 20, new Rectangle(0, 0, 20, 20), 
                                                     new Rectangle(20, 0, 20, 20), new Rectangle(40, 0, 20, 20), new Rectangle(60, 0, 20, 20), new Rectangle(0, 0, 20, 20)),
                                     incidents, "Alternado", new Rectangle(84, 4, 16, 16));
      case Anubis: return new NPC("Game/Anubis.png", destx, desty, 
                                  new ActorSprite("Game/Anubis.png", 0, 0, 20, 20, new Rectangle(0, 0, 20, 20), 
                                                  new Rectangle(20, 0, 20, 20), new Rectangle(40, 0, 20, 20), new Rectangle(60, 0, 20, 20), new Rectangle(0, 0, 20, 20)),
                                  incidents, "Anubis", new Rectangle(84, 4, 16, 16));
      case Amaury: return new NPC("Game/Amaury.png", destx, desty,
                                  new ActorSprite("Game/Amaury.png", 0, 0, 18, 18, new Rectangle(0, 0, 18, 18),
                                                  new Rectangle(18, 0, 18, 18), new Rectangle(36, 0, 18, 18), new Rectangle(54, 0, 18, 18), new Rectangle(0, 0, 18, 18)),
                                  incidents, "Amaury", new Rectangle(74, 2, 16, 16));
      case Bruiser: return new NPC("Game/Bruiser.png", destx, desty, 
                                   new ActorSprite("Game/Bruiser.png", 0, 0, 20, 20, new Rectangle(0, 0, 80, 20), new Rectangle(80, 0, 120, 20), 
                                                   new Rectangle(200, 0, 80, 20), new Rectangle(280, 0, 120, 20), new Rectangle(0, 0, 20, 20)),
                                   incidents, "Bonbon", new Rectangle(424, 28, 16, 16), new Rectangle(424, 44, 16, 16));
      case Frog: return new NPC("Game/Frog.png", destx, desty,
                                new ActorSprite("Game/Frog.png", 0, 0, 20, 20, new Rectangle(0, 0, 80, 20), new Rectangle(80, 0, 60, 20),
                                                new Rectangle(140, 0, 80, 20), new Rectangle(220, 0, 60, 20), new Rectangle(0, 0, 20, 20)),
                                incidents, "Geropei", new Rectangle(304, 24, 16, 16));
      case Gem: return new NPC("Game/Gem.png", destx, desty, 
                               new ActorSprite("Game/Gem.png", 0, 0, 18, 18, new Rectangle(0, 0, 54, 18), new Rectangle(54, 0, 36, 18),
                                               new Rectangle(90, 0, 54, 18), new Rectangle(144, 0, 36, 18), new Rectangle(0, 0, 18, 18)),
                               incidents, "Gem", new Rectangle(200, 38, 16, 16));
      case Kurtle: return new NPC("Game/Kurtle.png", destx, desty, 
                                  new ActorSprite("Game/Kurtle.png", 0, 0, 20, 20, new Rectangle(0, 0, 20, 20), 
                                                  new Rectangle(20, 0, 20, 20), new Rectangle(40, 0, 20, 20), new Rectangle(60, 0, 20, 20), new Rectangle(0, 0, 20, 20)),
                                  incidents, "Kurtle", new Rectangle(84, 4, 16, 16));
      case LGalahad: return new NPC("Game/L. Galahad.png", destx, desty, 
                                    new ActorSprite("Game/L. Galahad.png", 0, 0, 18, 18, new Rectangle(0, 0, 18, 18), 
                                                    new Rectangle(18, 0, 18, 18), new Rectangle(36, 0, 18, 18), new Rectangle(54, 0, 18, 18), new Rectangle(0, 0, 18, 18)),
                                    incidents, "Little Galahad", new Rectangle(74, 2, 16, 16));
      case Loader: return new NPC("Game/Loader.png", destx, desty,
                                  new ActorSprite("Game/Loader.png", 0, 0, 28, 28, new Rectangle(0, 0, 28, 28),
                                                  new Rectangle(28, 0, 28, 28), new Rectangle(56, 0, 28, 28), new Rectangle(84, 0, 28, 28), new Rectangle(0, 0, 28, 28)),
                                  incidents, "Loader", new Rectangle(124, 12, 16, 16));      
      case Meowly: return new NPC("Game/Meowly.png", destx, desty,
                                  new ActorSprite("Game/Meowly.png", 0, 0, 24, 24, new Rectangle(0, 0, 24, 24),
                                                  new Rectangle(24, 0, 24, 24), new Rectangle(48, 0, 24, 24), new Rectangle(72, 0, 24, 24), new Rectangle(0, 0, 24, 24)),
                                  incidents, "Meowly", new Rectangle(104, 8, 16, 16));  
      case MireKnight: return new NPC("Game/Mire Knight.png", destx, desty,
                                      new ActorSprite("Game/Mire Knight.png", 0, 0, 34, 34, new Rectangle(0, 0, 34, 34),
                                                      new Rectangle(34, 0, 34, 34), new Rectangle(68, 0, 34, 34), new Rectangle(102, 0, 34, 34), new Rectangle(0, 0, 34, 34)),
                                      incidents, "Mire Knight", new Rectangle(154, 18, 16, 16));  
      case Myrton: return new NPC("Game/Myrton.png", destx, desty,
                                  new ActorSprite("Game/Myrton.png", 0, 0, 22, 22, new Rectangle(0, 0, 22, 22),
                                                  new Rectangle(22, 0, 22, 22), new Rectangle(44, 0, 22, 22), new Rectangle(66, 0, 22, 22), new Rectangle(0, 0, 22, 22)),
                                  incidents, "Myrton", new Rectangle(94, 6, 16, 16));  
      case Nanna: return new NPC("Game/Nanna.png", destx, desty,
                                 new ActorSprite("Game/Nanna.png", 0, 0, 18, 18, new Rectangle(0, 0, 18, 18),
                                                 new Rectangle(18, 0, 18, 18), new Rectangle(36, 0, 18, 18), new Rectangle(54, 0, 18, 18), new Rectangle(0, 0, 18, 18)),
                                 incidents, "Nanna", new Rectangle(74, 2, 16, 16));  
      case Otso: return new NPC("Game/Otso.png", destx, desty,
                                      new ActorSprite("Game/Otso.png", 0, 0, 32, 32, new Rectangle(0, 0, 32, 32),
                                                      new Rectangle(64, 0, 32, 32), new Rectangle(128, 0, 32, 32), new Rectangle(192, 0, 32, 32), new Rectangle(0, 0, 32, 32)),
                                      incidents, "Otso", new Rectangle(268, 16, 16, 16));  
      case Quetzal: return new NPC("Game/Quetzal.png", destx, desty,
                                   new ActorSprite("Game/Quetzal.png", 0, 0, 24, 24, new Rectangle(0, 0, 24, 24),
                                                   new Rectangle(24, 0, 24, 24), new Rectangle(48, 0, 24, 24), new Rectangle(72, 0, 24, 24), new Rectangle(0, 0, 24, 24)),
                                   incidents, "Quetzal", new Rectangle(104, 8, 16, 16));  
      case PGirl: return new NPC("Game/Pedal G..png", destx, desty, 
                                 new ActorSprite("Game/Pedal G..png", 0, 0, 28, 28, new Rectangle(0, 0, 28, 28), 
                                                 new Rectangle(28, 0, 28, 28), new Rectangle(56, 0, 28, 28), new Rectangle(84, 0, 28, 28), new Rectangle(0, 0, 28, 28)),
                                 incidents, "Pedal Girl", new Rectangle(124, 12, 16, 16));
      case Schlatt: return new NPC("Game/Schlatt.png", destx, desty,
                                   new ActorSprite("Game/Schlatt.png", 0, 0, 40, 40, new Rectangle(0, 0, 40, 40),
                                                   new Rectangle(40, 0, 40, 40), new Rectangle(80, 0, 40, 40), new Rectangle(120, 0, 40, 40), new Rectangle(0, 0, 40, 40)),
                                   incidents, "Schlatt", new Rectangle(184, 24, 16, 16));  
      case Shadow: return new NPC("Game/Shadow.png", destx, desty, 
                                  new ActorSprite("Game/Shadow.png", 0, 0, 18, 18, new Rectangle(0, 0, 54, 18), 
                                                  new Rectangle(54, 0, 36, 18), new Rectangle(90, 0, 54, 18), new Rectangle(144, 0, 36, 18), new Rectangle(0, 0, 18, 18)),
                                  incidents, "Zeke", new Rectangle(200, 22, 16, 16), new Rectangle(200, 38, 16, 16));
      case Terry: return new NPC("Game/Terry.png", destx, desty,
                                 new ActorSprite("Game/Terry.png", 0, 0, 18, 18, new Rectangle(0, 0, 18, 18),
                                                 new Rectangle(18, 0, 18, 18), new Rectangle(36, 0, 18, 18), new Rectangle(54, 0, 18, 18), new Rectangle(0, 0, 18, 18)),
                                 incidents, "Terry", new Rectangle(74, 2, 16, 16)); 
      case Uniman: return new NPC("Game/Uniman.png", destx, desty,
                                  new ActorSprite("Game/Uniman.png", 0, 0, 24, 24, new Rectangle(0, 0, 24, 24),
                                                  new Rectangle(24, 0, 24, 24), new Rectangle(48, 0, 24, 24), new Rectangle(72, 0, 24, 24), new Rectangle(0, 0, 24, 24)),
                                  incidents, "Uniman", new Rectangle(104, 8, 16, 16));
      case Wingo: return new NPC("Game/Wingo.png", destx, desty,
                                  new ActorSprite("Game/Wingo.png", 0, 0, 24, 24, new Rectangle(0, 0, 24, 24),
                                                  new Rectangle(24, 0, 24, 24), new Rectangle(48, 0, 24, 24), new Rectangle(72, 0, 24, 24), new Rectangle(0, 0, 24, 24)),
                                  incidents, "Wingo", new Rectangle(104, 8, 16, 16));
      case Wolfrang: return new NPC("Game/Wolfrang.png", destx, desty, 
                                    new ActorSprite("Game/Wolfrang.png", 0, 0, 28, 28, new Rectangle(0, 0, 28, 28), 
                                                    new Rectangle(28, 0, 28, 28), new Rectangle(56, 0, 28, 28), new Rectangle(84, 0, 28, 28), new Rectangle(0, 0, 28, 28)),
                                    incidents, "Wolfrang", new Rectangle(124, 12, 16, 16));

    }
    return null;
  }
  public static NPC makeNPC(Actor act, int destx, int desty, ActorSprite.Direction dir, Queue<Incident> incidents){
    NPC npc = makeNPC(act, destx, desty, incidents);
    npc.setPose(dir);
    return npc;
  }
}