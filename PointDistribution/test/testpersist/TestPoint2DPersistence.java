/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testpersist;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ae3263
 */
public class TestPoint2DPersistence {

    public static void main(String[] args) {
        XMLEncoder e = new XMLEncoder(System.out);
        e.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
        e.setPersistenceDelegate(P2D.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
        e.setPersistenceDelegate(P2D2.class, new DefaultPersistenceDelegate(new String[]{"x","y"}));
//        testColors(e);
//        testColorArray(e);
//        testPoint2DDouble(e);
//        testPoint2DDoubleArray1(e);
        testPoint2DDoubleArray2(e);
//        testDoubleArray(e);
//        testNewPoint(e);
//        testNewPointArray1(e);
//        testNewPointArray2(e);
        e.close();
    }

    
    //
    // test colors
    //

    public static void testColors(XMLEncoder def) {
        System.out.println("--testColors--");
        XMLEncoder e;
        XMLDecoder ed;
        Color c = Color.RED;
        try {
            e = new XMLEncoder(new FileOutputStream("test1.xml"));
            ed = new XMLDecoder(new FileInputStream("test1.xml"));
            System.out.println(c);
            e.writeObject(c); e.close();
            c = (Color) ed.readObject(); ed.close();
            System.out.println(c);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestPoint2DPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class ColorArrayTestObject implements Serializable {
        Color[] cArr = new Color[2];
        public ColorArrayTestObject() { cArr[0] = new Color(1,2,3); cArr[1] = new Color(4,5,6); }
        public Color[] getColorArr() { return cArr; }
        public void setColorArr(Color[] p) { this.cArr = p; }
    }

    public static void testColorArray(XMLEncoder def) {
        System.out.println("--testColorArray--");
        ColorArrayTestObject p = new ColorArrayTestObject();
        p.cArr[0] = new Color(7,8,9);
        def.writeObject(p);
    }

    //
    // test points
    //

    public static void testPoint2DDouble(XMLEncoder def) {
        System.out.println("--testPoint2DDouble--");
        XMLEncoder e;
        XMLDecoder ed;
        Point2D.Double p = new Point2D.Double(3,2);
        try {
            e = new XMLEncoder(new FileOutputStream("test2.xml"));
            ed = new XMLDecoder(new FileInputStream("test2.xml"));
            System.out.println(p);
            e.writeObject(p); e.close();
            p = (Point2D.Double) ed.readObject(); ed.close();
            System.out.println(p);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    // test arrays of points
    //

    public static class P2D2 extends Point2D.Double {
        public P2D2() { this(0,0); }
        public P2D2(double x, double y) { super(x,y); }
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final P2D2 other = (P2D2) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }
    }

    public static void testPoint2DDoubleArray1(XMLEncoder def) {
        System.out.println("--testPoint2DDoubleArray1--");
        Point2D.Double[] arr = new Point2D.Double[4];
        arr[2] = new Point2D.Double(1,1);
        arr[1] = new Point2D.Double(0,5);
        def.writeObject(arr);
    }

    public static class PointArrayTestObject {
        Point2D.Double[] pArr;
        public PointArrayTestObject() { pArr = new Point2D.Double[]{new Point2D.Double(3,4),new Point2D.Double(5,6)};}
        public Point2D.Double[] getPArr() { return pArr; }
        public void setPArr(Point2D.Double[] p) { this.pArr = p; }
       // public Point2D.Double getPArr(int i) { return pArr[i]; }
       // public void setPArr(int i, Point2D.Double p) { pArr[i] = p; }
    }

    public static void testPoint2DDoubleArray2(XMLEncoder def) {
        System.out.println("--testPoint2DDoubleArray2--");
        PointArrayTestObject p = new PointArrayTestObject();
        p.pArr[0]=new Point2D.Double(1,1);
        def.writeObject(p);
    }

    //
    // test arrays of doubles
    //

    public static class DoubleArrayTestObject {
        double[][] pArr = new double[2][2];
        public DoubleArrayTestObject() { pArr[0][0] = 1; pArr[1][1] = 2; }
        public double[][] getPArr() { return pArr; }
        public void setPArr(double[][] p) { this.pArr = p; }
    }

    public static void testDoubleArray(XMLEncoder def) {
        System.out.println("--testDoubleArray--");
        DoubleArrayTestObject p = new DoubleArrayTestObject();
        p.pArr[0][1] = 5;
        def.writeObject(p);
    }

    //
    // test new point class
    //

    public static class P2D implements Serializable {
        public P2D() { this(0,0); }
        public P2D(double x, double y) { this.x=x; this.y=y; }
        double x,y;        
        public double getX() { return x; }
        public double getY() { return y; }
        @Override public String toString() { return "("+x+","+y+")"; }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final P2D other = (P2D) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }

//        @Override
//        public int hashCode() {
//            int hash = 7;
//            hash = 83 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
//            hash = 83 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
//            return hash;
//        }
        
    }

    public static void testNewPoint(XMLEncoder def) {
        System.out.println("--testNewPoint--");
        XMLEncoder e;
        XMLDecoder ed;
        P2D p = new P2D(3,2);
        try {
            e = new XMLEncoder(new FileOutputStream("test3.xml"));
            ed = new XMLDecoder(new FileInputStream("test3.xml"));
            System.out.println(p);
            e.writeObject(p); e.close();
            p = (P2D) ed.readObject(); ed.close();
            System.out.println(p);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testNewPointArray1(XMLEncoder def) {
        System.out.println("--testNewPointArray1--");
        P2D[] arr = new P2D[4];
        arr[2] = new P2D(1,1);
        arr[1] = new P2D(0,5);
        def.writeObject(arr);
    }

    public static class NewPointArrayTestObject implements Serializable {
        P2D[] pArr = new P2D[2];
        public NewPointArrayTestObject() { pArr[0] = new P2D(); pArr[1] = new P2D(1,5); }
        public P2D[] getPointArr() { return pArr; }
        public void setPointArr(P2D[] p) { this.pArr = p; }
//        public P2D getPArr(int i) { return pArr[i]; }
//        public void setPArr(int i, P2D p) { pArr[i] = p; }
    }

    public static void testNewPointArray2(XMLEncoder def) {
        System.out.println("--testNewPointArray2--");
        NewPointArrayTestObject p = new NewPointArrayTestObject();
        p.pArr[0] = new P2D(1,5);
        def.writeObject(p);
        new Point2D.Double().equals(null);
    }
}
