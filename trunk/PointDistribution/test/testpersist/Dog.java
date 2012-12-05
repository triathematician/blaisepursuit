/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testpersist;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ae3263
 */
public class Dog {
    String name;
    float weight;
    public Dog() { this("Spot", 20); }
    public Dog(String name, float weight) { this.name = name; this.weight = weight; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setWeight(float weight) { this.weight = weight; }
    public float getWeight() { return weight; }
    @Override public String toString() { return "Dog["+name+","+weight+"]"; }
    
public static class DogPack {
    Vector<Dog> dogs;
    public DogPack() { dogs = new Vector<Dog>(); }
    public Vector<Dog> getDogs() { return dogs; }
    public void setDogs(Vector<Dog> dogs) { this.dogs = dogs; }
    @Override public String toString() { return dogs.toString(); }
}

public static class DogPack2 {
    Dog[] dogs;
    public DogPack2() { dogs = new Dog[3]; }
    public Dog[] getDogs() { return dogs; }
    public void setDogs(Dog[] dogs) { this.dogs = dogs; }
    @Override public String toString() { return Arrays.toString(dogs); }
}
    
    public static void main(String[] args) {
        XMLEncoder e;
        XMLDecoder ed;
        Dog dog = new Dog(); dog.name = "Spotty";
        try {
            e = new XMLEncoder(new FileOutputStream("test1.xml"));
            ed = new XMLDecoder(new FileInputStream("test1.xml"));
            System.out.println(dog);
            e.writeObject(dog); e.close();
            dog = (Dog) ed.readObject(); ed.close();
            System.out.println(dog);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dog.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            DogPack pack = new DogPack(); pack.dogs.add(new Dog("Curly", 15)); pack.dogs.add(dog);
            e = new XMLEncoder(new FileOutputStream("test2.xml"));
            ed = new XMLDecoder(new FileInputStream("test2.xml"));
            System.out.println(pack);
            e.writeObject(pack); e.close();
            pack = (DogPack) ed.readObject(); ed.close();
            System.out.println(pack);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dog.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            DogPack2 pack2 = new DogPack2(); pack2.dogs[1] = dog;
            e = new XMLEncoder(new FileOutputStream("test3.xml"));
            ed = new XMLDecoder(new FileInputStream("test3.xml"));
            System.out.println(pack2);
            e.writeObject(pack2); e.close();
            pack2 = (DogPack2) ed.readObject(); ed.close();
            System.out.println(pack2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
