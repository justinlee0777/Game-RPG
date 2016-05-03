public class Stack<T>{
  private T elements[];
  private int size, alloc;
  private T end;
  
  public Stack(){
    size = 0;
    alloc = 0;
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
    end = elements[size];
    size++;
    
    return true;
  }
  
  public T pop(){
    if(size == 0)
      return null;
    T tail = end;
    size--;
    if(size == 0)
      end = null;
    else
      end = elements[size-1];
    return tail;
  }
  
  public T peek(){
    return end;
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
      end = elements[size-1];
  }
  
  public boolean empty(){return size == 0;}
  
  public int size(){return size;}
}