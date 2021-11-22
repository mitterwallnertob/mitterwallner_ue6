package serial;

import person.model.Person;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


public class Catalog {

  private List<Person> objects;
  

  public Catalog() {
    this.objects = new LinkedList<>();
  }
  

  public void persist() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("catalog.ser"))) {
      oos.writeObject(objects);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  

  public void restore() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("catalog.ser"))) {
      objects = (List<Person>) ois.readObject();
    }
    catch (FileNotFoundException e) {
      System.out.println("No Catalog found! Nothing to restore!");
    }
    catch (InvalidClassException e) {
      System.out.println("Catalog contains class description of different version! Nothing to restore!");
    }
    catch (ClassNotFoundException e) {
      System.out.println("Catalog contains class description of unknown class! Nothing to restore!");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  

  public Person selectBySvnr(String svnr) {
    Person found = null;
    
    int i = 0;
    while (i < objects.size() && !objects.get(i).getSvnr().equals(svnr)) {
      i++;
    }
    
    if (i < objects.size()) {
      found = objects.get(i);
    }
    
    return found;
  }
  

  public void save(Person person) {
    if (objects.contains(person)) {
      objects.remove(person);
    }
    objects.add(person);
  
    System.out.println(objects);
  }
  
  
  @Override
  public String toString() {
    return "Catalog{" +
        "objects=" + objects +
        '}';
  }
}
