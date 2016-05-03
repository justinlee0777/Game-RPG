//package Game;

public class Item{
  ItemType item;
  int quantity;
  String name;
  Item left, right, parent;
  
  public Item(ItemType it){
    item = it;
    switch(it){
      case Soda: name = "Soda";
      break;
    }
    quantity = 1;
  }
  
  public Item(ItemType it, int quant){
    item = it;
    switch(it){
      case Soda: name = "Soda";
      break;
    }
    quantity = quant;
  }
  
  public enum ItemType{
    Soda;
  }
  
  public void use(Battler bat){
    switch(item){
      case Soda: bat.useMana(-50);
      break;
    }
    quantity--;
  }
  
  public void addStock(int num){
    quantity+=num;
  }
  
  public void addStock(Item it){
    quantity+=it.quantity;
  }
  
  public boolean noMoreStock(){return quantity == 0;}
}