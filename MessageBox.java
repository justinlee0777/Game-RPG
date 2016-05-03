//package Game;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class MessageBox extends Sprite{
  public BufferedImage window, alphabet;
  private String message;
  private Tile actor;
  //array to contain ascii numbers and place to draw
  int[] ascii_letters;
  Point[] d_coord;
  
  public MessageBox(int destX, int destY, int wid, int hei, Tile act, String mess){
    dx = destX;
    dy = destY;
    width = wid;
    height = hei;
    try{
      window = ImageIO.read(getClass().getResource("Sprites/Window.png"));
    }catch(IOException wi){JOptionPane.showMessageDialog(null, "MessageBox Cannot open file");}
    
    try{
      alphabet = ImageIO.read(getClass().getResource("Sprites/Alphabet.png"));
    }catch(IOException ai){JOptionPane.showMessageDialog(null, "MessageBox Cannot open file");}; 
    setBounds(dx, dy, wid, hei);
    actor = act;
    message = mess;
    ascii_letters = new int[mess.length()];
    //algorithm
    int i = 0;
    int j = 0;
    int letSpce = 0;
    int h = 0;
    int space;
    int index;
    String[] words = message.split(" ");
    while(i < words.length){
      space = (dx + 2) + (8 * letSpce);
      if(space + (words[i].length() * 8) < dx+width && (containsPunctuation(words[i]) || !acceptableLength(space, words[i]))){
        while(j < words[i].length()){
          space = (dx + 2) + (8 * letSpce);
          index = (words[i].codePointAt(j)-32)*8;
          addLetter(index, new Point(space, h));
          letSpce++;
          j++;
        }
        space = (dx + 2) + (8 * letSpce);
        index = ((int)' ' - 32)*8;
        addLetter(index, new Point(space, h));
        letSpce++;
        i++;
      }
      else if(space + 16 < dx+width && !containsPunctuation(words[i]) && acceptableLength(space, words[i])){
        while(j < words[i].length()){
          space = (dx + 2) + (8 * letSpce);
          if(space + 16 > dx+width){
            index = ((int)'-' - 32)*8;
            addLetter(index, new Point(space, h));
            letSpce = 0;
            h+=16;
            space = (dx + 2) + (8 * letSpce);
          }
          index = (words[i].codePointAt(j)-32)*8;
          letSpce++;
          j++;
        }
        space = (dx + 2) + (8 * letSpce);
        index = ((int)' ' - 32)*8;
        addLetter(index, new Point(space, h));
        letSpce++;
        i++;
      }
      else{
        letSpce = 0;
        h+=16;
      }
      j = 0;
    }
  }
  
  public void addLetter(int ascii, Point coord){
    if(ascii_letters == null || d_coord == null){
        ascii_letters = new int[1];
        d_coord = new Point[1];
        ascii_letters[0] = ascii;
        d_coord[0] = coord;
        return;
    }
    int j = ascii_letters.length;
    int[] temp1 = new int[ascii_letters.length+1];
    Point[] temp2 = new Point[ascii_letters.length+1];
    for(int i = 0; i < ascii_letters.length; i++){
      temp1[i] = ascii_letters[i];
      temp2[i] = d_coord[i];
    }
    temp1[j] = ascii;
    temp2[j] = coord;
    ascii_letters = temp1;
    d_coord = temp2;
  }
//using ASCII number convention to find sprite sheet characters
  //16 and 12 differences are to make up for - inclusions
  public static Queue<Sprite> makeBoxes(int destx, int desty, int wid, int hei, Field field, Tile act, String mess, FieldCharacter c){
    if(new Rectangle(field.currGrid.camera.x+destx, field.currGrid.camera.y+desty, wid, hei+16).contains(new Point(c.dx, c.dy)))
      desty = 0; 
    Queue<Sprite> queue = new Queue<Sprite>();
    int i = 0;
    int end = 0;
    String[] words = mess.split(" ");
    while(end < mess.length()){
      if((end - i) * 8 >= (wid * (hei-16))/16){
        boolean b = (end+1 < mess.length() && mess.charAt(end+1) != ' ');
        queue.add(MessageBox.makeBox(destx, desty, wid, hei, act, (b) ? mess.substring(i, end) : mess.substring(i, end)));
        i = end;
      }
      end++;
    }
    if(end - i > 0)
      queue.add(MessageBox.makeBox(destx, desty, wid, hei, act, mess.substring(i, end)));
    return queue;
  }
  
  //for character exchange in NPC tiles
  public static Sprite[] makeDialogue(int destx, int desty, int wid, int hei, Field field, Tile act, String mess, FieldCharacter c){
    if(new Rectangle(field.currGrid.camera.x+destx, field.currGrid.camera.y+desty, wid, hei+16).contains(new Point(c.dx, c.dy)))
      desty = 0; 
    Queue<Sprite> queue = new Queue<Sprite>();
    int i = 0;
    int end = 0;
    String[] words = mess.split(" ");
    while(end < mess.length()){
      if((end - i) * 8 >= (wid * (hei-16))/16){
        boolean b = (end+1 < mess.length() && mess.charAt(end+1) != ' ');
        queue.add(MessageBox.makeBox(destx, desty, wid, hei, act, (b) ? mess.substring(i, end) : mess.substring(i, end)));
        i = end;
      }
      end++;
    }
    if(end - i > 0)
      queue.add(MessageBox.makeBox(destx, desty, wid, hei, act, mess.substring(i, end)));
    Sprite[] mBoxes = new Sprite[queue.size()];
    int qSize = queue.size();
    for( i = 0; i < qSize; i++)
      mBoxes[i] = queue.pop();
    return mBoxes;
  }
  
  public static MessageBox makeBox(int destx, int desty, int wid, int hei, Tile act, String mess){
    MessageBox m = new MessageBox(destx, desty, wid, hei+16, act, mess);
    return m;
  }
  
  public void paintWindow(int destx, int desty, int w, int h, Graphics g){
    //draw insides
    for(int i = 0; i < w; i+=16)
      for(int j = 0; j < h; j+=16)
      g.drawImage(window, destx+i, desty+j, destx+i+16, desty+j+16, 16, 16, 32, 32, null);
    //draw corners
    g.drawImage(window, destx, desty, destx+16, desty+16, 0, 0, 16, 16, null);
    g.drawImage(window, destx, desty+h-16, destx+16, desty+h, 0, 32, 16, 48, null);
    g.drawImage(window, destx+w-16, desty+h-16, destx+w, desty+h, 32, 32, 48, 48, null);
    g.drawImage(window, destx+w-16, desty, destx+w, desty+16, 32, 0, 48, 16, null);
    //draw top and bottom
    for(int i = 16; i < w-16; i+=16){
      g.drawImage(window, destx+i, desty, destx+i+16, desty+16, 16, 0, 32, 16, null);
      g.drawImage(window, destx+i, desty+h-16, destx+i+16, desty+h, 16, 32, 32, 48, null);
    }
    //draw sides
    for(int j = 16; j < h-16; j+=16){
      g.drawImage(window, destx, desty+j, destx+16, desty+j+16, 0, 16, 16, 32, null);
      g.drawImage(window, destx+w-16, desty+j, destx+w, desty+j+16, 32, 16, 48, 32, null);
    }
  }
  
  public void paintLetters(String line, int destx, int desty, int wid, Graphics g){
    int i = 0;
    int space, index;
    while(i < line.length()){ 
      space = (destx + 2) + (8 * i);
      index = (line.codePointAt(i)-32)*8;
      if(space+8 >= destx+wid){
        break;
      }
      g.drawImage(alphabet, space, (desty-1), (space+8), (desty+15), index, 0, index+8, 16, null);
      i++;
    }
  }
  
  public boolean containsPunctuation(String s){
    return s.contains("'") || s.contains(".") || s.contains("-");
  }
  
  public boolean acceptableLength(int spce, String mess){
    return (spce + mess.length() - width) > 32 && mess.length() > 4;
  }
  
  public void paint(Graphics g){
    int offset = 0;
    paintWindow(dx, dy+offset, width, height, g);
    for(int i = 0; i < ascii_letters.length; i++)
      g.drawImage(alphabet, d_coord[i].x, dy+d_coord[i].y+offset, d_coord[i].x+8, dy+d_coord[i].y+offset+16, 
                  ascii_letters[i], 0, ascii_letters[i]+8, 16, null);
    /*if(actor instanceof NPC){
      if(dy > 0){
        offset = 0;
        paintWindow(0, dy-16, width, 16, g);
        ((NPC)actor).drawPortrait(dx, dy, g);
        paintLetters(((NPC)actor).name, dx+16, dy, width, g);
      }
      else{
        offset = 16;
        paintWindow(0, 0, width, 16, g);
        ((NPC)actor).drawPortrait(0, 0, g);
        paintLetters(((NPC)actor).name, 16, 0, width, g);
      }
    }*/

    if(actor instanceof NPC){
      paintWindow(0, dy+height-16, width, 16, g);
      ((NPC)actor).drawPortrait(dx, dy+height-16, g);
      paintLetters(((NPC)actor).name, dx+16, dy+height-16, width, g);
    }
  }
}