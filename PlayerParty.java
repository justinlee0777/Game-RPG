//package Game;

public class PlayerParty extends Party{
  private Member[] characters, order;
  //private int numChars;
  private Party team;
  private Inventory inventory;
  
  public PlayerParty(Member c[], int cnum){
    super();
    characters = c;
    ///numChars = cnum;
    team = new Party();
    order = new Member[cnum];
    for(int i = 0; i < cnum; i++){
      characters[i] = c[i];
      insert(c[i], true);
    }
    for(int i = 0; i < cnum && i < 3; i++){
      team.insert(c[i], true);
      order[i] = c[i];
    }
    team.setTeamStats();
    inventory = new Inventory();
    inventory.insert(Item.ItemType.Soda, 3);
  }
  
  public Member[] getAll(){return characters;}
  
  public Member[] getTeam(){return order;}
  public int getTeamSize(){return team.size();}
  public Party team(){return team;}
  public Party.Iterator teamBegin(){return team.begin();}
  
  public void setOrder(int initPos, int switchPos){
    Member temp = characters[initPos];
    characters[initPos] = characters[switchPos];
    characters[switchPos] = temp;
    team = new Party();
    for(int i = 0; i < size() && i < 3; i++){
      team.insert(characters[i], true);
      order[i] = characters[i];
    }
    team.setTeamStats();
  }
  
  public Battler getPosBattler(int b){
    return characters[b];
  }
  
  public Inventory inventory(){return inventory;}
  
  public void addItem(Item.ItemType item, int count){
    inventory.insert(item, count);
  } 
  
  public Inventory.Iterator getHeadItem(){
    return inventory.begin();
  }
  
  public int inventorySize(){
    return inventory.size();
  }
  
  public void useItem(Item it, Battler bat){
    inventory.use(it, bat);
  }
}