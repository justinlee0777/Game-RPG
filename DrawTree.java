//package Game;

import java.awt.Rectangle;

public class DrawTree{
  private Node root;
  //static iterating node
  private volatile Node itr;
  private int size;
  
  //private int test_int = 0;
  
  //refresh after every so often changes?
  
  //use "old_priority" for searches
  
  public DrawTree(){ 
    root = null; 
    size = 0;
  }
  
  public Node add(Sprite t){
    if(root == null){
      size++;
      root = new Node(t);
      //System.out.println("root: " + root.sprite.priority());
      return root;
    }
    return add(root, t);
  }
  
  public Node add(Node node, Sprite t){
    if(t.priority() < node.old_priority){
      if(node.left == null){
        //System.out.println(t.priority() + " < " + node.sprite.priority());
        node.left = new Node(t);
        node.left.parent = node;
        size++;
        return node.left;
      }
      return add(node.left, t);
    }
    else if(t.priority() > node.old_priority){
      if(node.right == null){
        //System.out.println(t.priority() + " > " + node.sprite.priority());
        node.right = new Node(t);
        node.right.parent = node;
        size++;
        return node.right;
      }
      return add(node.right, t);
    }
    else if(t.priority() == node.old_priority){ 
      if(node.middle == null){
        //System.out.println(t.priority() + " == " + node.sprite.priority());
        node.middle = new Node(t);
        node.middle.parent = node;
        size++;
        return node.middle;
      }
      return add(node.middle, t);
    }
    else return null;
  }
  
  public void begin(){
    //System.out.close();
    //System.out.println("begin");
    //test_int = 0;
    if(root == null){
      itr = null;
      return;
    }
    itr = root;
    
    while(itr.left != null){ itr = itr.left; }
    while(itr.middle != null) itr = itr.middle;
  }
  
  public Sprite itr_value(){
    if(itr == null) return null;
    return itr.sprite;
  }
  
  public boolean end(){
    return itr == null;
  }
  
  public Node approximate(Node node){
    return approximate(root, node);
  }
  
  public Node approximate(Node currNode, Node node){
    if(node.sprite.priority() < currNode.old_priority){
      if(currNode.left == null){
        return currNode;
      }
      return approximate(currNode.left, node);
    }
    else if(node.sprite.priority() > currNode.old_priority){
      if(currNode.right == null){
        return currNode;
      }
      return approximate(currNode.right, node);
    }
    else if(node.sprite.priority() == currNode.old_priority){ 
      return currNode;
    }
    else return null;
  }
  
  public void DFS(){
    DFS(root);
    /*
    Queue<Node> queue = new Queue();
    queue.add(root);
    while(!queue.empty()){
      Node node = queue.pop();
      if(node.right != null) queue.add(node.right);
      if(node.left != null) queue.add(node.left);
      if(node.middle != null) queue.add(node.middle);
      System.out.println("BFS: " + node.nodeInfo());
    }
    */
  }
  
  public void DFS(Node node){
    //System.out.println(node.nodeInfo());
    if(node.middle != null) DFS(node.middle);
    if(node.left != null) DFS(node.left);
    if(node.right != null) DFS(node.right);
  }
  
  public synchronized boolean traverse(){
    if(itr == null){ 
      //System.out.println("false");
      return false;
    }
    
    //System.out.println("traverse " + itr.nodeInfo() );
    if(itr.right != null){
      itr = itr.right;
      while(itr.left != null) { itr = itr.left; }
      while(itr.middle != null) { itr = itr.middle; }
      return true;
    }
    else if(itr.parent != null){
      //end case; highest possible value
      if(itr.parent.old_priority < itr.old_priority){
        Node temp = itr.parent;
        while(temp.parent != null && temp.parent.old_priority < itr.old_priority){ 
          //System.out.println("parent " + temp.nodeInfo());
          temp = temp.parent; 
        }
        if(temp.parent != null && temp.parent.old_priority > itr.old_priority){
          itr = temp.parent;
          while(itr.middle != null) itr = itr.middle; 
          return true;
        }
      }
      else if(itr.parent.old_priority > itr.old_priority){
        itr = itr.parent;
        while(itr.middle != null) itr = itr.middle; 
        return true;
      }
      else if(itr.parent.old_priority == itr.old_priority){
        itr = itr.parent;
        return true;
      }
    }
    //System.out.println("end");
    itr = null;
    return true;
  }

  public synchronized boolean ex_traverse(Rectangle cam){
    if(itr == null){ 
      //System.out.println("false");
      return false;
    }
    
    //System.out.println("traverse " + itr.nodeInfo() );
    if(itr.right != null){
      itr = itr.right;
      while(itr.left != null) { itr = itr.left; }
      if(itr.sprite.dy < cam.y - 16|| itr.sprite.dy > cam.y + cam.height + 16)
        return ex_traverse(cam);
      while(itr.middle != null) { itr = itr.middle; }
      return true;
    }
    else if(itr.parent != null){
      //end case; highest possible value
      if(itr.parent.old_priority < itr.old_priority){
        Node temp = itr.parent;
        while(temp.parent != null && temp.parent.old_priority < itr.old_priority){ 
          //System.out.println("parent " + temp.nodeInfo(2));
          temp = temp.parent; 
        }
        if(temp.parent != null && temp.parent.old_priority > itr.old_priority){
          itr = temp.parent;
          if(itr.sprite.dy < cam.y - 16 || itr.sprite.dy > cam.y + cam.height + 16)
            return ex_traverse(cam);
          while(itr.middle != null) itr = itr.middle; 
          return true;
        }
      }
      else if(itr.parent.old_priority > itr.old_priority){
        itr = itr.parent;
        if(itr.sprite.dy < cam.y -16 || itr.sprite.dy > cam.y + cam.height + 16)
          return ex_traverse(cam);
        while(itr.middle != null) itr = itr.middle; 
        return true;
      }
      else if(itr.parent.old_priority == itr.old_priority){
        itr = itr.parent;
        return true;
      }
    }
    //System.out.println("end");
    itr = null;
    return true;
  }
  
  //broken; will neglect many sprites
  /*
  public synchronized void refresh_tree(){
    DrawTree newTree = new DrawTree();
    for(this.begin(); !this.end(); this.traverse()){
      newTree.add(this.itr_value());
    }
    root = newTree.root;
  }
  */
  public class Node extends Object{
    private Node left, right, middle;
    /*private*/ Node parent;
    private Sprite sprite;
    /*private*/ int old_priority;
    
    public Node(Sprite spr){
      left = null; right = null; middle = null; parent = null;
      sprite = spr;
      old_priority = sprite.priority();
    }
    
    //debugging tool
    public String nodeInfo(){
      String str = this.sprite.priority() + " ";
      if(this.parent != null) str+="parent ";
      if(this.right != null) str+="right ";
      if(this.middle != null) str+="middle ";
      if(this.left != null) str+="left";
      return str;
    }
    
    public String nodeInfo(int i){
      i--;
      String str = this.nodeInfo();
      str += "\n";
      if(i > 0){
        if(this.parent != null) str += (this.parent.nodeInfo(i));
        if(this.right != null) str += (this.right.nodeInfo(i));
        if(this.middle != null) str += (this.middle.nodeInfo(i));
        if(this.left != null) str += (this.left.nodeInfo(i));
      }
      return str;
    }
    

    //fix: move all of node's children too
    public synchronized void requestMove(DrawTree tree){
      
      if( Math.abs(this.sprite.priority() - this.old_priority) < 8) return;
      
      Node temp = tree.approximate(this);
      //System.out.println(this.sprite.priority() + " >= " + temp.sprite.priority());
      if( temp != null && this.sprite.priority() >= temp.old_priority ){
        //DFS();
        System.out.print("Old: " + this.old_priority + " Node: " + this.nodeInfo(2));
        this.old_priority = this.sprite.priority();
        
        int temp_priority = temp.old_priority;
        
        //System.out.println(this.sprite.priority() + " " + temp.old_priority);
        
        // 'node' has a parent, middle: ? left: ? right: ?
        if(this.parent != null){
          //System.out.print(this.nodeInfo(2) + " has a parent");
          if(this.parent.right == this){
            System.out.println(", is the right");
            // 'this' has a parent, middle, left: ?, right: ?
            if(this.middle != null){
              //System.out.println("this.middle: " + this.middle.nodeInfo());
              this.middle.right = this.right;
              this.middle.left = this.left;
              this.middle.parent = this.parent;
              this.parent.right = this.middle;
              if(this.left != null) this.left.parent = this.middle;
              if(this.right != null) this.right.parent = this.middle;
              //System.out.println("this.parent: " + this.parent.nodeInfo());
            }
            // 'this' has a parent, left, right: ?
            else if(this.left != null){
              this.left.right = this.right;
              this.parent.right = this.left;
              if(this.right != null) this.right.parent = this.middle;
            }
            // 'this' has a parent, right
            else if(this.right != null){
              this.parent.right = this.right;
              this.right.parent = this.parent;
            }
            else
              this.parent.right = null;
          }
          else if(this.parent.middle == this){ 
            System.out.println(", is the middle");
            //System.out.println("Parent: " + this.parent.nodeInfo());
            // 'this' has a parent, middle, left: ?, right: ?
            if(this.middle != null){
              //System.out.println("this.middle: " + this.middle.nodeInfo());
              this.middle.right = this.right;
              this.middle.left = this.left;
              this.middle.parent = this.parent;
              this.parent.middle = this.middle;
              if(this.left != null) this.left.parent = this.middle;
              if(this.right != null) this.right.parent = this.middle;
            }
            // 'this' has a parent, left, right: ?
            else if(this.left != null){
              //should logically have no left
              
              //inefficient sorting?
              Node exchange = this.left;
              while(exchange.right != null){
                exchange = exchange.right;
              }
              exchange.right = this.right;
              //this.parent.middle = this.left;
             
              this.parent.middle = null;
            }
            // 'this' has a parent, right
            else if(this.right != null){
              //should logically have no right
              this.parent.middle = null;
            }
            else
              this.parent.middle = null;
          }
          else if(this.parent.left == this){ 
            System.out.println(", is the left");
            // 'this' has a parent, middle, left: ?, right: ?
            if(this.middle != null){
              //System.out.println("this.middle: " + this.middle.nodeInfo());
              this.middle.right = this.right;
              this.middle.left = this.left;
              this.middle.parent = this.parent;
              this.parent.left = this.middle;
              if(this.left != null) this.left.parent = this.middle;
              if(this.right != null) this.right.parent = this.middle;
            }
            // 'this' has a parent, left, right: ?
            else if(this.left != null){
              //System.out.println(", has a left");
              Node exchange = this.left;
              while(exchange.right != null){
                exchange = exchange.right;
              }
              exchange.right = this.right;
              this.parent.left = this.left;
            }
            else if(this.right != null){
              //System.out.println(", has a right");
              this.parent.left = this.right;
            }
            else{
              //System.out.println(", branchless");
              this.parent.left = null;
            }
          }
          else System.out.println("DrawTree error: Node does not exist as branch");
          
        }
        //'this' has no parent, middle, left: ?, right: ?
        else if( this.middle != null ){
          //System.out.println("no parent, has a middle " + this.nodeInfo());
          this.middle.right = this.right;
          this.middle.left = this.left;
          if( this == root) root = this.middle;
          
        }
        //'this' has no parent, no middle, left, right: ?
        else if( this.left != null ){
          //System.out.println("no parent, has a left " + this.nodeInfo());
          Node exchange = this.left;
          while(exchange.right != null){
            exchange = exchange.right;
          }
          exchange.right = this.right;
          if( this == root) root = this.left;
          
        }
        //'this' has no parent, no middle, no left, a right
        else if( this.right != null ){
          //System.out.println("no parent, has a right " + this.nodeInfo());
          if(this == root) root = this.right;
          
        }
        this.right = null;
        this.middle = null;
        this.left = null;
        this.parent = null;
        if(this.old_priority > temp_priority && temp.right != null){
          //System.out.println("current node has a right: " + temp.right.nodeInfo());
          Node exchange = temp;
          while(exchange.left != null && this.sprite.priority() > exchange.left.sprite.priority()) 
            exchange = exchange.left;
          if(exchange.right != null && this.sprite.priority() > exchange.right.sprite.priority() ){
            //System.out.println("exchange node is less: " + exchange.right.nodeInfo());
            this.right = exchange.right;
            exchange.right.parent = this;
            exchange.right = this;
            this.parent = exchange;
          }
          else if(exchange.right != null && this.old_priority == exchange.right.sprite.priority() ){
            //System.out.println("exchange node is equal: " + exchange.right.nodeInfo());
            this.left = exchange.right.left;
            exchange.right.left = null;
            this.right = exchange.right.right;
            exchange.right.right = null;
            this.middle = exchange.right.middle;
            exchange.right.parent = this;
            exchange.right = this;
            this.parent = temp;
          }
          else{
            exchange.right = this;
            this.parent = temp; 
          }
          
        }
        else if(this.old_priority > temp_priority){
          //System.out.println("current node has no right: " + temp.nodeInfo());
          temp.right = this;
          this.parent = temp;
        }
        else if(temp_priority == this.old_priority && temp != this && temp.middle != null){
          //System.out.println("current node has a middle: " + temp.nodeInfo());
          //System.out.println("temp.middle: " + temp.middle.nodeInfo());
          this.middle = temp.middle;
          temp.middle.parent = this;
          temp.middle = this;
          this.parent = temp;
        }
        else if(temp_priority == this.old_priority && temp != this){
          //System.out.println("current node has no middle: " + temp.nodeInfo());
          temp.middle = this;
          this.parent = temp;
        }
        else if(temp_priority == this.old_priority && temp == this && temp.parent == null){
          System.out.print("Error: no move");
        }
        else if(temp_priority == this.old_priority){
          Node exchange = temp.parent;
          while(exchange.right != null) exchange = exchange.right;
          if(temp.parent.middle == temp) temp.parent.middle = null;
          else if(temp.parent.right == temp) temp.parent.right = null;
          else if(temp.parent.left == temp) temp.parent.left = null;
          exchange.right = this;
        }
        else 
          System.out.println("Error: Node not moved " + temp.nodeInfo());
        //System.out.println("drawtree end " + this.nodeInfo());
        //Node tmp = this;
        //while(tmp.middle != null){ System.out.print("middling: "); tmp.nodeInfo(); tmp = tmp.middle;}
        //DFS();
      }
    }
    
  }
  
}
