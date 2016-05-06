public class Runner{
    // comments
  public static void main(String[] args){
    GameFrame frame = new GameFrame();
  }
}

////
//// fix DrawTree!!!!!
////

//go to BattlerConstructor, connect BattleSprite to FieldEnemy
//stop painting things already painted (ex. NPCs)
//make certain sprites contingent on event
//share one BufferedImage among objects
//alter repaint?
//oh god. Unique collision bounds (for each direction)?
//flesh out locations

//implement memory for individual battler cursors
//gah clipping (with bed)

//smooth out walking? LERP?

//make static regions, then add incidents afterwards?
//global-incidents class?

//appropriate enter-exit places
//variable protection is disgusting (ex.currGrid)

//page-flipping (eliminates tearing)