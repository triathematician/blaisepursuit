/**
 * LocationGenerator.java
 * Created on Jul 14, 2009
 */

package sim.agent;

import java.awt.geom.Point2D;
import scio.random.Random2DUtils;

/**
 * <p>
 *   <code>LocationGenerator</code> ...
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class LocationGenerator {

    /** Force construction with a string name.
     * @param name the name of the algorithm.
     */
    public LocationGenerator(String name) {
        this.name = name;
    }
    
    /** Stores the positions generated. */
    transient Point2D.Double[] locs = new Point2D.Double[0];

    /** @param n the number of locations to return
     * @return array of locations of specified size. */
    public Point2D.Double[] getLocations(int n) {
        if (locs == null || locs.length != n) {
            locs = new Point2D.Double[n];
        }
        for (int i = 0; i < locs.length; i++) {
            locs[i] = getLocation(i, n);
        }
        return locs;
    }

    /**
     * Returns a specific location.
     * @param i the number of the current position being retrieved.
     * @param n the total number of locations to generate
     */
    public abstract Point2D.Double getLocation(int i, int n);

    /** Name of the algorithm. */
    String name;

    /** @return the name of the algorithm */
    @Override
    public String toString() {
        return "Location Generator - " + name;
    }



    //============================================//
    //                                            //
    //               FACTORY METHODS              //
    //                                            //
    //============================================//

    /** An enum encoding possible sensor values */
    public enum LocEnum { DELEGATE, ORIGIN, RANDOM_BOX, SEGMENT, CIRCLE, ARC };

    /** Retrieves an instance of the sensor given an enum value */
    public static LocationGenerator getInstance(LocEnum value) {
        switch (value) {
            case DELEGATE: return DELEGATE_INSTANCE;
            case ORIGIN: return ORIGIN_INSTANCE;
            case RANDOM_BOX: return new RandomBox();
            case SEGMENT: return new Segment();
            case CIRCLE: return new Circle();
            case ARC: return new Arc();
        }
        return null;
    }



    //============================================//
    //                                            //
    //               INNER CLASSES                //
    //                                            //
    //============================================//

    public static final LocationGenerator DELEGATE_INSTANCE = new LocationGenerator("Delegate") {
        public Point2D.Double getLocation(int i, int n) {
            return null;
        }
    };
    
    /** A location generator that starts out players at the origin. */
    public static final LocationGenerator ORIGIN_INSTANCE = new LocationGenerator("Origin") {
        public Point2D.Double getLocation(int i, int n) {
            return new Point2D.Double();
        }
    };

    /** A location generator that starts out players at evenly spaced points along a line segment. */
    public static class Segment extends LocationGenerator {
        Point2D.Double p1 = new Point2D.Double(-10, 0);
        Point2D.Double p2 = new Point2D.Double(10, 10);

        public Segment() { super("Segment"); }
        /** Sets up points between two specified points.
         * @param p1 the first endpoint
         * @param p2 the last endpoint
         */
        public Segment(Point2D.Double p1, Point2D.Double p2) { this(); this.p1 = p1; this.p2 = p2; }

        public Point2D.Double getPoint1() { return p1; }
        public void setPoint1(Point2D.Double p) { this.p1 = p; }
        public Point2D.Double getPoint2() { return p2; }
        public void setPoint2(Point2D.Double p) { this.p2 = p; }

        public Point2D.Double getLocation(int i, int n) {
            return n == 1 
                    ? new Point2D.Double( (p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
                    : new Point2D.Double( p1.x + (p2.x - p1.x) * i / (n - 1), p1.y + (p2.y - p1.y) * i / (n - 1));
        }
    }

    /** A location generator that starts out players at evenly spaced points around a circle. */
    public static class Circle extends LocationGenerator {
        Point2D.Double center = new Point2D.Double();
        double radius = 10.0;

        public Circle() { super("Circle"); }
        /** Sets up points between two specified points.
         * @param center the first endpoint
         * @param radius the last endpoint
         */
        public Circle(Point2D.Double center, double radius) { this(); this.center = center; this.radius = radius; }

        public Point2D.Double getCenter() { return center; }
        public void setCenter(Point2D.Double p) { this.center = p; }
        public double getRadius() { return radius; }
        public void setRadius(double r) { this.radius = r; }

        public Point2D.Double getLocation(int i, int n) {
            return new Point2D.Double(
                    center.x + radius * Math.cos(2 * Math.PI * i / n),
                    center.y + radius * Math.sin(2 * Math.PI * i / n));
        }
    }

    /** A location generator that starts out players at evenly spaced points along an arc of a circle. */
    public static class Arc extends LocationGenerator {
        Point2D.Double center = new Point2D.Double();
        double radius = 10.0;
        double theta1 = Math.PI / 3;
        double theta2 = 5 * Math.PI / 3;

        public Arc() { super("Arc"); }
        /** Sets up points between two specified points.
         * @param center the first endpoint
         * @param radius the last endpoint
         * @param theta1 first angle of arc
         * @param theta2 second angle of arc
         */
        public Arc(Point2D.Double center, double radius, double theta1, double theta2) {
            this();
            this.center = center; this.radius = radius;
            this.theta1 = theta1; this.theta2 = theta2;
        }

        public Point2D.Double getCenter() { return center; }
        public void setCenter(Point2D.Double p) { this.center = p; }
        public double getRadius() { return radius; }
        public void setRadius(double r) { this.radius = r; }
        public double getTheta1() { return theta1; }
        public void setTheta1(double th) { this.theta1 = th; }
        public double getTheta2() { return theta2; }
        public void setTheta2(double th) { this.theta2 = th; }

        public Point2D.Double getLocation(int i, int n) {
            return n == 1 ? new Point2D.Double(
                    center.x + radius * Math.cos((theta2 - theta1) / 2),
                    center.y + radius * Math.sin((theta2 - theta1) / 2))
                : new Point2D.Double(
                    center.x + radius * Math.cos(theta1 + i * (theta2 - theta1) / (n - 1)),
                    center.y + radius * Math.sin(theta1 + i * (theta2 - theta1) / (n - 1)));
        }
    }

    /** A location generator that starts out players at (uniformly) random positions in a square box. */
    public static class RandomBox extends LocationGenerator {
        double boxSize = 10.0;

        public RandomBox() { super("Random"); }
        /** @param boxRadius the size of the box [-r,-r]->[r,r] */
        public RandomBox(double boxSize) { this(); this.boxSize = boxSize; }

        public double getRadius() { return boxSize; }
        public void setRadius(double radius) { this.boxSize = radius; }

        public Point2D.Double getLocation(int i, int n) {
            return Random2DUtils.uniformRectangle(-boxSize, -boxSize, boxSize, boxSize);
        }
    }



}
