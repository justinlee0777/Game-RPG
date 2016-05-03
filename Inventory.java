//package Game;

public class Inventory{
  private Item head = null;
  //private Item[] items;
  private int size;
  
  public class Iterator{
    private Item ptr;
    
    public Iterator(){
      ptr = head;
    }    
    
    public Iterator(Item p){
     ptr = p; 
    }
    
    public Item value(){return ptr;}
    //because of Java's auto-pointers end() is like this
    public boolean end(){return ptr == null;}
    //I have a diagram somewhere to show all this stuff unless I threw it out
    public Iterator increment(){
      if(ptr.right != null){
        Item tmp = ptr.right;
        while(tmp.left != null)
          tmp = tmp.left;
        ptr = tmp;
        return this;
      }
      if(ptr.parent != null){
        if(ptr.parent.name.compareTo(ptr.name) >= 0){
          Item par = ptr.parent;
          ptr = par;
          return this;
        }
        else{
          if(ptr.parent.parent != null && ptr.parent.parent.name.compareTo(ptr.name) >= 0){
            ptr = ptr.parent.parent;
            return this;
          }
        }
      }
      ptr = null;
      return this;
    }
    
  }
  
  public Inventory(){
   size = 0; 
  }
  
  public void insert(Item.ItemType k, int quant){
    Item p = new Item(k, quant);
    if(head == null){
      size++;
      head = p;
      return;
    }
    else if(head == p){
      head.addStock(p);
      return;
    }
    insert(p, head);
  }
  
  private void insert(Item p, Item node){
    if(p.name.compareTo(node.name) > 0){
      if(node.right == null)
        node.right = p;
      else{
        insert(p, node.right);
        return;
      }
      size++;
      p.parent = node;
    }
    else if(p.name.compareTo(node.name) < 0){
      if(node.left == null)
        node.left = p;
      else{
        insert(p, node.left);
        return;
      }
      size++;
      p.parent = node;
    }
    else{
      node.addStock(p);
    }
  }
    
  public void erase(Item k){
    if(head == null){
      System.out.println("NullPointerException");
    }
    if(k.name.equals(head.name)){
      if(head.right == null && head.left == null){
        head = null;
      }
      else if(head.left == null){
        head = head.right;
      }
      else if(head.right == null){
        head = head.left;
      }
      else{
        Item currNode = head;
        while(currNode.left != null && currNode.right != null)
          currNode = currNode.right;
        if(currNode.left != null){
          Item temp = currNode.left;
          //move left node up
          currNode.left.parent = currNode.parent;
          currNode.left.right = currNode;
          //transfer values of currNode to right node of left
          currNode.parent = temp;
          currNode.left.right.item = currNode.item;
          currNode.left.right.parent = currNode.parent;
          currNode = null;
        }
        while(currNode != head){
          currNode.left = currNode.parent.left;
          currNode = currNode.parent;
        }
        head = currNode.right;
        currNode = null;
      }
      size--;
      return;
    }
    if(k.name.compareTo(head.name) > 0)
      erase(k, head.right);
    else
      erase(k, head.left);
  }
  
  private void erase(Item k, Item node){
    if(node == null){
      System.out.println("NullPointerException");
    }
    if(k.name.equals(node.name)){
      if(head.right == null && head.left == null){
        node = null;
      }
      else if(head.left == null){
        node = node.right;
      }
      else if(head.right == null){
        node = node.left;
      }
      else{
        Item currNode = node;
        while(currNode.left != null && currNode.right != null)
          currNode = currNode.right;
        if(currNode.left != null){
          Item temp = currNode.left;
          //move left node up
          currNode.left.parent = currNode.parent;
          currNode.left.right = currNode;
          //transfer values of currNode to right node of left
          currNode.parent = temp;
          currNode.left.right.item = currNode.item;
          currNode.left.right.parent = currNode.parent;
          currNode = null;
        }
        while(currNode != node){
          currNode.left = currNode.parent.left;
          currNode = currNode.parent;
        }
        currNode = null;
      }
      size--;
      return;
    }
    if(k.name.compareTo(node.name) > 0)
      erase(k, node.right);
    else
      erase(k, node.left);
  }
  
  public Item find(Item.ItemType k){
    Item it = new Item(k);
    return find(it, head);
  }
  
  private Item find(Item k, Item node){
    if(node == null){
      System.out.println("NullPointerException");
    }
    if(k.name.equals(node.name))
      return node;
    else if(k.name.compareTo(node.name) > 0)
      return find(k, node.right);
    else
      return find(k, node.left);
  }
  
  public void use(Item it, Battler bat){
    it.use(bat);
    if(it.noMoreStock())
      erase(it);
  }
  
  public int size(){return size;}
  public Item head(){return head;}
  public Iterator begin(){
    Item tmp = head;
    if(tmp == null)  return new Iterator(null);
    while(tmp.left != null)
      tmp = tmp.left;
    return new Iterator(tmp);}
}