//package Game;

public class Queue<T>{
 
  private T elements[];
  private int size, alloc;
  private T top;
  
  public Queue(){
    size = 0;
    alloc = 0;
  }
  
  public class Iterator{
    private T value;
    private int pos;
    
    private Iterator(T val, int position){
      value = val;
      pos = position;
    }
    
    public boolean end(){return pos == size;}
    public void increment(){
      pos++;
      if(pos < size)
        value = elements[pos];
      else
        value = null;
    }
    
    public T value(){return value;}
  }
  
  public boolean add(T t){
    if(size == alloc){
      if(size == 0)
        alloc = 1;
      else
        alloc *= 2;
      T[] temp = (T[]) new Object[alloc];
      for(int i = 0; i < size; i++)
        temp[i] = elements[i];
      elements = temp;
    }
    elements[size] = t;
    size++;
    top = elements[0];
    return true;
  }
  
  public T pop(){
    if(size == 0)
      return null;
    T head = top;
    T[] temp = (T[]) new Object[alloc];
    for(int i = 1; i < size; i++)
      temp[i-1] = elements[i];
    elements = temp;
    size--;
    top = elements[0];
    return head;
  }
  
  public T peek(){
    return top;
  }
  
  //remove instance of an element T
  public void removeInstanceOf(T element){
    for(int i = 0; i < size; i++){
      if(elements[i] == element){
        for(int j = i; j < size-1; j++){
          elements[j] = elements[j+1];
        }
       size--;
      }
    }
    if(size > 0)
      top = elements[0];
  }
  
  public boolean empty(){return size == 0;}
  public int size(){return size;}
  public Iterator begin(){return new Iterator(elements[0], 0);}
}