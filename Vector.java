//package Game;

public class Vector<T>{
  
  private T elements[];
  private int size, alloc;
  
  public Vector(){
    size = 0;
    alloc = 0;
  }
  
  public void add(T t){
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
  }
  
  public void delete(T t){
    for(int i = 0; i < elements.length; i++)
      if(t == elements[i]){
      elements[i] = null;
      size--;
    }
    T[] temp = (T[])new Object[size];
    int j = 0;
    for(int i = 0; i < elements.length; i++)
      if(elements[i] != null){
      temp[j] = elements[i];
      j++;
    }
    elements = temp;
  }
  
  public T valAt(int i){
    return elements[i];
  }
  
  public boolean empty(){return size == 0;}
  public int size(){return size;}
  
}