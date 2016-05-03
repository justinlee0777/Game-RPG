//package Game;

//designed like a map data structure except can't because Java
//especially since there will be the same name for the same object

//maps Battlers and whether Battlers are alive
//implement an iterator-like nested class?
public class Party{
  private Pair head;
  private int size;
  
  public class Pair{
    public Battler key;
    public boolean value;
    private Pair left, right, parent;
    
    public Pair(Battler k, boolean val){
      key = k;
      value = val;
    }
  }
  
  public class Iterator{
    private Pair ptr;
      
    public Iterator(){
      ptr = head;
    }
    
    public Iterator(Pair p){
     ptr = p; 
    }
    
    public Battler key(){return ptr.key;}
    public boolean value(){return ptr.value;}
    //because of Java's auto-pointers end() is like this
    public boolean end(){return ptr == null;}
    //I have a diagram somewhere to show all this stuff unless I threw it out
    public Iterator increment(){
      if(ptr.right != null){
        Pair tmp = ptr.right;
        while(tmp.left != null)
          tmp = tmp.left;
        ptr = tmp;
        return this;
      }
      if(ptr.parent != null){
        if(ptr.parent.key.name.compareTo(ptr.key.name) >= 0){
          Pair par = ptr.parent;
          ptr = par;
          return this;
        }
        else{
          if(ptr.parent.parent != null && ptr.parent.parent.key.name.compareTo(ptr.key.name) >= 0){
            ptr = ptr.parent.parent;
            return this;
          }
        }
      }
      ptr = null;
      return this;
    }
    
  }
  
  public Party(){
   size = 0; 
  }
  
  //unncessary since the revised names need to be returned
  //,maybe make inserts, erases return boolean like normal map does
  public void insert(Battler k, boolean val){
    Pair p = new Pair(k, val);
    if(head == null){
      size++;
      head = p;
      return;
    }
    insert(p, head);
  }
  
  private void insert(Pair p, Pair node){
    if(p.key.name.compareTo(node.key.name) > 0){
      if(node.right == null)
        node.right = p;
      else{
        insert(p, node.right);
        return;
      }
      size++;
      p.parent = node;
    }
    else{
      if(node.left == null)
        node.left = p;
      else{
        insert(p, node.left);
        return;
      }
      size++;
      p.parent = node;
    }
  }
    
  public void erase(Battler k){
    if(head == null){
      System.out.println("NullPointerException");
    }
    if(k.name.equals(head.key.name)){
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
        Pair currNode = head;
        //Pair<U> leftNode = currNode.left;
        while(currNode.left != null && currNode.right != null)
          currNode = currNode.right;
        if(currNode.left != null){
          Pair temp = currNode.left;
          //move left node up
          currNode.left.parent = currNode.parent;
          currNode.left.right = currNode;
          //transfer values of currNode to right node of left
          currNode.parent = temp;
          currNode.left.right.key = currNode.key;
          currNode.left.right.value = currNode.value;
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
    if(k.name.compareTo(head.key.name) > 0)
      erase(k, head.right);
    else
      erase(k, head.left);
  }
  
  private void erase(Battler k, Pair node){
    if(node == null){
      System.out.println("NullPointerException");
      //System.exit(1);
    }
    if(k.equals(node.key.name)){
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
        Pair currNode = node;
        while(currNode.left != null && currNode.right != null)
          currNode = currNode.right;
        if(currNode.left != null){
          Pair temp = currNode.left;
          //move left node up
          currNode.left.parent = currNode.parent;
          currNode.left.right = currNode;
          //transfer values of currNode to right node of left
          currNode.parent = temp;
          currNode.left.right.key = currNode.key;
          currNode.left.right.value = currNode.value;
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
    if(k.name.compareTo(node.key.name) > 0)
      erase(k, node.right);
    else
      erase(k, node.left);
  }
  
  public Pair find(Battler k){
    return find(k, head);
  }
  
  private Pair find(Battler k, Pair node){
    if(node == null){
      System.out.println("NullPointerException");
    }
    if(k == node.key)
      return node;
    else if(k.name.compareTo(node.key.name) > 0)
      return find(k, node.right);
    else
      return find(k, node.left);
  }
  
  public void set(Battler k, boolean val){
    set(k, head, val);
  }
  
  private void set(Battler k, Pair node, boolean val){
    if(node == null){
      System.out.println("NullPointerException");
    }
    if(k == node.key)
      node.value = val;
    else if(k.name.compareTo(node.key.name) > 0)
      set(k, node.right, val);
    else
      set(k, node.left, val);
  }
  
  public int size(){return size;}
  public Pair head(){return head;}
  public Iterator begin(){
    Pair tmp = head;
    if(tmp == null)  return new Iterator(null);
    while(tmp.left != null)
      tmp = tmp.left;
    return new Iterator(tmp);}
  
  public void setTeamStats(){
    for(Iterator itr = begin(); !itr.end(); itr.increment())
      itr.key().stats.getFuncStats(this);
  }
  
}