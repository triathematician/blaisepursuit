/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D.Double;
import java.util.Arrays;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ae3263
 */
public class PointSetAlgorithmsTest {

    public PointSetAlgorithmsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of convexHull method, of class PointSetAlgorithms.
     */
    @Test
    public void testConvexHull() {
        System.out.println("convexHull");
        Double[] arr = new Double[]{ new Double(0,0), new Double(1,0), new Double(2,0), new Double(2,2), new Double(1,1), new Double(0,2) };
        Double[] hull = PointSetAlgorithms.convexHull(arr);
        assertEquals("[Point2D.Double[0.0, 0.0], Point2D.Double[2.0, 0.0], Point2D.Double[2.0, 2.0], Point2D.Double[0.0, 2.0]]", Arrays.toString(hull));
    }

    /**
     * Test of ccw method, of class PointSetAlgorithms.
     */
    @Test
    public void testCcw() {
        System.out.println("ccw");
        Double p1 = new Double(0,0);
        Double p2 = new Double(1,0);
        assertEquals(1, PointSetAlgorithms.ccw(p1, p2, new Double(0,1)));
        assertEquals(-1, PointSetAlgorithms.ccw(p1, p2, new Double(0,-1)));
    }

    /**
     * Test of voronoiFortune method, of class PointSetAlgorithms.
     */
    @Test
    public void testVoronoiFortune() {
        System.out.println("voronoiFortune");

        int[][] single = new int[][]{{0,1}};
        int[][] triple = new int[][]{{0,1},{0,2},{1,2}};

        testVoronoiFortune(new Double[]{new Double(0,0), new Double(0,1)}, single);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(1,0)}, single);

        testVoronoiFortune(new Double[]{new Double(0,0), new Double(1,0), new Double(0,1)}, triple);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(1,-.04), new Double(0,1)}, triple);
        testVoronoiFortune(new Double[]{new Double(-.25,0), new Double(0,1), new Double(-1,-1)}, triple);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(.5,.5), new Double(0,1)}, triple);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(.25,.5), new Double(0,1)}, triple);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(-.5,.5), new Double(0,1)}, triple);
        testVoronoiFortune(new Double[]{new Double(.25,.25), new Double(0,.5), new Double(.25,.75)}, triple);
        testVoronoiFortune(new Double[]{new Double(.25,.25), new Double(-.05,0.4), new Double(.25,.75)}, triple);
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(0,.3), new Double(0,1)}, new int[][]{{0,1},{1,2}});
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(0,.5), new Double(0,1)}, new int[][]{{0,1},{1,2}});

        testVoronoiFortune(new Double[]{new Double(0,0), new Double(3,-1), new Double(1,2), new Double(4,4)}, new int[][]{{0,1},{0,2},{1,2},{1,3},{2,3}});
        testVoronoiFortune(new Double[]{new Double(0,0), new Double(1,0), new Double(2,.5), new Double(2,1)}, new int[][]{{0,1},{0,3},{1,2},{1,3},{2,3}});
        
        testVoronoiFortune(new Double[]{new Double(.25,.75), new Double(1,.5), new Double(1.2,.75), new Double(.25,.25)}, new int[][]{{0,1},{0,2},{0,3},{1,2},{1,3}});
        testVoronoiFortune(new Double[]{new Double(.25,.75), new Double(.4,.4), new Double(1.2,.75), new Double(.25,.25)}, new int[][]{{0,1},{0,2},{0,3},{1,2},{1,3},{2,3}});
    }

    /** Tests to see if computed adjacencies are as expected. */
    private static void testVoronoiFortune(Double[] points, int[][] expectedIndices) {
        Double[][] expected = new Double[expectedIndices.length][2];
        for (int i = 0; i < expected.length; i++) { for (int j = 0; j < 2; j++) { expected[i][j] = points[expectedIndices[i][j]]; } }
        System.out.println("Points: " + Arrays.toString(points));
        List<Double[]> adj = PointSetAlgorithms.voronoiFortune(points);
        System.out.println("Result: " + Arrays.deepToString(adj.toArray(new Double[][]{})));
        System.out.println("------------------");
        for (int i = 0; i < adj.size(); i++) {
            Double[] tAdj = adj.get(i);
            boolean found = false;
            for (int j = 0; j < expected.length; j++) if (match(tAdj, expected[j])) { found = true; break; }
            assertEquals(true, found);
        }
        for (int i = 0; i < expected.length; i++) {
            boolean found = false;
            for (int j = 0; j < adj.size(); j++) if (match(adj.get(j), expected[i])) { found = true; break; }
            assertEquals(true, found);
        }
    }

    /** Test to see if the two arrays contain all the same elements */
    private static boolean match(Double[] p1, Double[] p2) {
        for (int i = 0; i < p1.length; i++) {
            boolean found = false;
            for (int j = 0; j < p2.length; j++) if (p1[i]==p2[j]) { found = true; break; }
            if (! found) { return false; }
        }
        return true;
    }


    /**
     * Test of addEvent method, of class PointSetAlgorithms.
     */
    @Test
    public void testAddEvent() {
        System.out.println("addEvent... tested in voronoiFortune()");
    }

}