/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bm.blaise.scio.algorithm;

import scio.coordinate.utils.PlanarMathUtils;
import java.awt.geom.Point2D.Double;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ae3263
 */
public class PolygonUtilsTest {

    public static final double TOL = 1e-6;
    public static void assertEqualsPt(Double p1, Double p2, double tolerance) { assertEqualsPt(p1.x, p1.y, p2, tolerance); }
    public static void assertEqualsPt(double x1, double y1, Double p2, double tolerance) { assertEquals(x1, p2.x, tolerance); assertEquals(y1, p2.y, tolerance); }
    //public static void assertArrayEquals(double[] arr1, double[] arr2, double tolerance) { assertEquals(arr1.length, arr2.length); for (int i = 0; i < arr1.length; i++) { assertEquals(arr1[i], arr2[i], tolerance); } }

    /**
     * Test of areaOfPolygon method, of class MetricUtils.
     */
    @Test
    public void testAreaOfPolygon() {
        System.out.println("area");
        assertEquals(0.0, PolygonUtils.areaOf(new Double[]{new Double(0,0), new Double(1,0)}), TOL);
        assertEquals(0.5, PolygonUtils.areaOf(new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}), TOL);
        assertEquals(1.0, PolygonUtils.areaOf(new Double[]{new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1)}), TOL);
    }

    /**
     * Test of inPolygon method, of class ClipUtils.
     */
    @Test
    public void testInPolygon() {
        System.out.println("inPolygon");
        assertFalse(PolygonUtils.inPolygon(new Double(.5,.5), new Double[]{new Double(0,0), new Double(1,0)}));
        assertFalse(PolygonUtils.inPolygon(new Double(-.1,-.1), new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}));
        assertTrue(PolygonUtils.inPolygon(new Double(.5,.5), new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}));
        assertFalse(PolygonUtils.inPolygon(new Double(.51,.51), new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}));
        assertFalse(PolygonUtils.inPolygon(new Double(1,1), new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}));
        assertTrue(PolygonUtils.inPolygon(new Double(1,1), new Double[]{new Double(0,0), new Double(1,2), new Double(2,0)}));

        assertTrue(PolygonUtils.inPolygon(new Double(0,0), new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}));
        assertTrue(PolygonUtils.inPolygon(new Double(.5,.5), new Double[]{new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1)}));
    }


    /**
     * Test of intersectionOfSegments method, of class ClipUtils.
     */
    @Test
    public void testIntersectSegments() {
        System.out.println("intersectSegments");
        Double px = new Double();
        PolygonUtils.intersectSegments(px, new Double(0,0), new Double(2,0), new Double(1,1), new Double(3,1));
        assertEquals(new Double(), px);
        PolygonUtils.intersectSegments(px, new Double(0,0), new Double(3,1), new Double(1,1), new Double(2,0));
        assertEqualsPt(new Double(1.5,.5), px, TOL);
        PolygonUtils.intersectSegments(px, new Double(-1,0), new Double(1,0), new Double(0,1), new Double(0,-1));
        assertEqualsPt(new Double(), px, TOL);

        // special cases
        PolygonUtils.intersectSegments(px, new Double(0,0), new Double(2,0), new Double(1,0), new Double(3,0));
        assertEqualsPt(new Double(1,0), px, TOL);
        PolygonUtils.intersectSegments(px, new Double(0,0), new Double(2,0), new Double(2,0), new Double(3,0));
        assertEqualsPt(new Double(2,0), px, TOL);
    }

    /**
     * Test of clipPolygon method, of class ClipUtils.
     */
    @Test
    public void testIntersectionOfConvexPolygons() {
        System.out.println("intersectionOfConvexPolygons");

        testIntersection(
                new Double[]{new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1)},
                new Double[]{new Double(-.25,.5), new Double(.5,-.25), new Double(.5,1.25) } );
        testIntersection(
                new Double[]{ new Double(.5,.5), new Double(-.1,.3), new Double(1.5,.2) },
                new Double[]{ new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1) } );        
        testIntersection(
                new Double[]{ new Double(.725,.5), PlanarMathUtils.polarPointAtInfinity(Math.PI/2), PlanarMathUtils.polarPointAtInfinity(Math.PI) },
                new Double[]{ new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1) } );
        testIntersection(
                new Double[]{ new Double(0.7249999999999968, 0.5163775285245027), new Double(0.7212618045637035, 0.5000000000000093), PlanarMathUtils.polarPointAtInfinity(-1.3074618452021987), PlanarMathUtils.polarPointAtInfinity(0.23268863178971982) },
                new Double[]{ new Double(0,0), new Double(1,0), new Double(1,1), new Double(0,1) } );
    }

    public void testIntersection(Double[] poly1, Double[] poly2) {
        System.out.println("Testing intersection of " + print(poly1) + " and " + print(poly2));
        Double[] result = PolygonIntersectionUtils.intersectionOfConvexPolygons(poly1, poly2);
        System.out.println("  result = " + print(result));
    }

    public String print(Double p) {
        return String.format("(%.3f,%.3f)", p.x, p.y);
    }

    public String print(Double[] poly) {
        if (poly == null) return "null";
        StringBuilder result = new StringBuilder("Polygon[");
        if (poly.length > 0) {
            result.append(print(poly[0]));
            for (int i = 1; i < poly.length; i++)
                result.append(", ").append(print(poly[i]));
        }
        result.append("]");
        return result.toString();
    }

}