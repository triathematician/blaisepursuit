/**
 * VoronoiFrontier.java
 * Created on Dec 10, 2009
 */
package org.bm.blaise.scio.algorithm.voronoi;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.bm.blaise.scio.algorithm.PointSetAlgorithms;
import org.bm.blaise.scio.algorithm.Tesselation;
import org.bm.blaise.scio.algorithm.Tesselation.Polygon;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier.FrontierArc;

import scio.coordinate.geometry.PlanarGeometryUtils;
import static org.bm.blaise.scio.algorithm.voronoi.VoronoiUtils.*;

/**
 * <p>
 *    This class represents the frontier structure of a set of points, meaning
 *    the collection of parabolic arcs with a common directrix and foci at the
 *    points to the left of the line. The data structure is maintained as a list
 *    of <code>Point2D.Double</code> objects representing the foci, together with
 *    a list of <code>DoubleParabolaBeachVertex</code> objects representing the
 *    points of intersection.
 * </p>
 * @author Elisha Peterson
 */
public class VoronoiFrontier implements Iterable<FrontierArc> {

    final static boolean VERBOSE = false;
    final static boolean REALLY_VERBOSE = false;

    /** Initial set of points. */
    List<Point2D.Double> points;
    /** Determines the "frontier" of the algorithm. */
    double maxDirectrix = java.lang.Double.MAX_VALUE;

    /** Combinatorial object representing the "frontier" determined by the directrix. */
    FrontierNode frontier;
    /** A tree of events. */
    TreeMap<Point2D.Double, FrontierArc> events;

    /** Computed adjacencies. */
    List<FrontierVertex> adjacencies;
    /** Ordered mapping from nodes to adjacent triple points or infinite points. */
    Map<Point2D.Double, TreeSet<Point2D.Double>> nodeVertexMap;
    /** The resulting tesselation of the plane... stores the adjacencies of polygons. */
    Tesselation tess;
    /** Mapping that associates each input point to the polygon of closest values. */
    Map<Point2D.Double, Polygon> polyMap;

    //
    // CONSTRUCTOR(S)
    //
    
    /** Sets up the algorithm for provided number of points. */
    public VoronoiFrontier(List<Point2D.Double> points) {
        setPoints(points);
        calculate();
    }

    //
    // GETTERS & SETTERS
    //

    public double getMaxDirectrix() {
        return maxDirectrix;
    }

    public void setMaxDirectrix(double maxDirectrix) {
        this.maxDirectrix = maxDirectrix;
    }

    public void setPoints(List<Point2D.Double> points) {
        this.points = points;
        Collections.sort(this.points, PointSetAlgorithms.XCOMPARE);
    }

    /**
     * Returns the list of adjacencies in the resulting tesselation, as a list
     * of pairs of points. This may not always contain bi-directional information;
     * sometimes adjacencies will be given one way but not the other.
     * @return a list of point pairs
     */
    public List<Point2D.Double[]> getAdjacencyList() {
        ArrayList<Point2D.Double[]> result = new ArrayList<Point2D.Double[]>();
        for (FrontierVertex fv : adjacencies) {
            result.add(new Point2D.Double[]{ fv.upperArc.point, fv.lowerArc.point });
        }
        return result;
    }

    public Tesselation getTesselation() {
        return tess;
    }
    

    //
    // ALGORITHM
    //

    transient double currentDirectrix;
    transient int i;

    /** Initializes the variables in the algorithm. */
    public void reset() {
        frontier = new FrontierNode();
        events = new TreeMap<Point2D.Double, FrontierArc>(PointSetAlgorithms.XCOMPARE);
        adjacencies = new ArrayList<FrontierVertex>();
        nodeVertexMap = new HashMap<Point2D.Double, TreeSet<Point2D.Double>>();
        for (Point2D.Double p : points) {
            events.put(p, new FrontierArc(p));
            nodeVertexMap.put(p, new TreeSet<Point2D.Double>(new VertexEdgeComparator(p)));
        }
        currentDirectrix = -java.lang.Double.MAX_VALUE;
        i = 0;
    }

    /** Resets variables in the algorithm and re-calculates. */
    public void calculate() {
        reset();
        while (events.size() > 0 && events.firstKey().x < maxDirectrix) {
            handleNextEvent();
            if (VERBOSE) {
                frontier.print(i + "  ");
            }
            i++;        
        }
        buildTesselation();
    }

    /** Builds tesselation based on current entries in the vertex map, together with points on the convex hull. */
    public void buildTesselation() {
        Point2D.Double[] hull = PointSetAlgorithms.convexHull(points.toArray(new Point2D.Double[]{}));
        for (int i = 0; i < hull.length; i++) {
            Point2D.Double infPt = new Point2D.Double(
                    Double.POSITIVE_INFINITY, 
                    Math.atan2( hull[i].x - hull[(i+1)%hull.length].x , hull[(i+1)%hull.length].y - hull[i].y ) );
            nodeVertexMap.get(hull[i]).add(infPt);
            nodeVertexMap.get(hull[(i+1)%hull.length]).add(infPt);
        }
        tess = new Tesselation();
        polyMap = new HashMap<Point2D.Double, Polygon>();
        for (Entry<Point2D.Double, TreeSet<Point2D.Double>> en : nodeVertexMap.entrySet()) {
            Polygon p = new Polygon(en.getValue().toArray(new Point2D.Double[]{}));
            tess.addPolygon(p);
            polyMap.put(en.getKey(), p);
        }
    }

    public Map<Point2D.Double, Polygon> getPolygonMap() {
        return polyMap;
    }

    /** Retrieves the next event in the queue and handles it appropriately. */
    public void handleNextEvent() {
        currentDirectrix = events.firstKey().x;
        Point2D.Double nextPoint = events.firstKey();
        FrontierArc nextEvent = events.remove(nextPoint);
        if (nextEvent.terminates == null) {
            // indicates that a new point is being added on, so the frontier is being added to
            if (frontier.children.size() == 0) {
                frontier.addNode(nextEvent);
            } else {
                FrontierArc whereToInsert = frontier.findInsertionPoint(nextPoint.y, currentDirectrix);
                FrontierNode newNodeToInsert = null;
                newNodeToInsert = FrontierNode.split(whereToInsert, nextEvent, currentDirectrix);
                if (VERBOSE) System.out.println("          Adding node for " + nextEvent.point);
                whereToInsert.parent.replaceNode(whereToInsert, newNodeToInsert);
                if (nextEvent.lowerVertex != null) {
                    adjacencies.add(nextEvent.lowerVertex);
                    checkForEvent(nextEvent.lowerArc());
                }
                if (nextEvent.upperVertex != null) {
                    adjacencies.add(nextEvent.upperVertex);
                    checkForEvent(nextEvent.upperArc());
                }
            }
        } else {
            // indicates that a part of the frontier is being lost
            FrontierVertex newVert = nextEvent.parent.removeArc(nextEvent, currentDirectrix);
            if (newVert != null) {
                FrontierArc upper = nextEvent.upperArc();
                FrontierArc lower = nextEvent.lowerArc();

                adjacencies.add(newVert);
                nodeVertexMap.get(nextEvent.point).add(newVert.start);
                nodeVertexMap.get(nextEvent.upperArc().point).add(newVert.start);
                nodeVertexMap.get(nextEvent.lowerArc().point).add(newVert.start);

                checkForEvent(upper);
                checkForEvent(lower);
            }
        }
    }

    /** Checks to see if the given arc will disappear because the arcs on either side converge. */
    boolean checkForEvent(FrontierArc arc) {
        if (arc.upperArc() == null || arc.lowerArc() == null) {
            // arc is one-sided; no termination possible
            return false;
        }
        if (arc.upperArc().getPoint().equals(arc.lowerArc().getPoint())) {
            // arc is inside another arc; no termination possible
            return false;
        }
        Point2D.Double termPoint = arc.pointOfTermination(currentDirectrix);
        if (termPoint != null) {
            termPoint.x += termPoint.distance(arc.point);
            if (termPoint.x < currentDirectrix) {
                return false;
            }
            events.put(termPoint, arc);
            return true;
        }
        return false;
    }

    //
    // INTERFACE METHODS FOR Iterable and Iterator
    //

    /** Returns an iterator that traverses the arcs in the frontier from bottom to top. */
    public Iterator<FrontierArc> iterator() {
        return new Iterator<FrontierArc>() {
            FrontierArc curArc = null;
            
            public boolean hasNext() {
                return (curArc == null && frontier.children.size() > 0) || (curArc != null && curArc.upperArc() != null);
            }
            public FrontierArc next() {
                curArc = curArc == null ? frontier.lowermostArc() : curArc.upperArc();
                return curArc;
            }
            public void remove() {
            }
        };
    }

    //
    // INNER CLASSES
    //

    /** Repreesents a node in the frontier tree */
    public static class FrontierNode {

        FrontierNode parent;
        /** List of subnodes; first node has the lowest range of y-values. */
        List<FrontierNode> children;

        // CONSTRUCTORS
        /** Construct with empty list of children. */
        FrontierNode() {
            children = new ArrayList<FrontierNode>();
        }

        // STRUCTURAL
        /** Adds a single node, without updating any connections. */
        void addNode(FrontierNode newNode) {
            children.add(newNode);
            newNode.parent = this;
        }

        /**
         * Returns an instance of a node with 2 or 3 children, corresponding to inserting
         * a new node for a particular point at a particular vertex. If the new node has
         * the same x-value as the old node, there will be 2 children; otherwise there will
         * be 3 children. This is only used when a new node is inserted, and must be placed
         * along an arc representing another point. If the points' x-values are the same,
         * the special case must be handled accordingly.
         * 
         * @param oldArc the arc which will be "split" into more pieces
         * @param newMiddle the new arc which will be placed within the old arc
         * @param directrix the current location of the directrix
         * @return a node which has the new arcs in order as children
         */
        static FrontierNode split(FrontierArc oldArc, FrontierArc newMiddle, double directrix) {
            boolean arcIsUpper = oldArc.point.x < newMiddle.point.x || (oldArc.point.x == newMiddle.point.x && oldArc.point.y > newMiddle.point.y);
            boolean arcIsLower = oldArc.point.x < newMiddle.point.x || (oldArc.point.x == newMiddle.point.x && oldArc.point.y < newMiddle.point.y);
            
            FrontierArc newUpper = null;
            if (arcIsUpper) {
                newUpper = new FrontierArc(oldArc.point);
                newUpper.upperVertex = oldArc.upperVertex;
                if (newUpper.upperVertex != null) {
                    newUpper.upperVertex.lowerArc = newUpper;
                }
                // create vertex at intersection of arcs
                FrontierVertex.atDirectrixInstance(newMiddle, newUpper, directrix);
            }
            FrontierArc newLower = null;
            if (arcIsLower) {
                newLower = new FrontierArc(oldArc.point);
                newLower.lowerVertex = oldArc.lowerVertex;
                if (newLower.lowerVertex != null) {
                    newLower.lowerVertex.upperArc = newLower;
                }
                // create vertex at intersection of arcs
                FrontierVertex.atDirectrixInstance(newLower, newMiddle, directrix);
            }
            // note bidirectionality of vertex
            if (!arcIsUpper) newMiddle.lowerVertex.bidir = true;
            else if (!arcIsLower) newMiddle.upperVertex.bidir = true;

            FrontierNode result = new FrontierNode();
            if (arcIsLower) result.addNode(newLower);
            result.addNode(newMiddle);
            if (arcIsUpper) result.addNode(newUpper);
            
            return result;
        }

        /** Swaps the oldNode out and puts the newNode in. Removes old connections, and inserts new ones. */
        void replaceNode(FrontierNode oldNode, FrontierNode newNode) {
            FrontierArc oldUpper = oldNode.upperArc();
            FrontierArc oldLower = oldNode.lowerArc();

            int i = children.indexOf(oldNode);
            children.set(i, newNode);
            newNode.parent = this;

            if (oldUpper != null) {
                oldUpper.lowerVertex.lowerArc = newNode.uppermostArc();
            }
            if (oldLower != null) {
                oldLower.upperVertex.upperArc = newNode.lowermostArc();
            }
        }

        /** Deletes node from a tree. */
        private boolean removeNode(FrontierNode node, double directrix) {
            if (node instanceof FrontierArc) {
                removeArc((FrontierArc) node, directrix);
            }
            if (children.remove(node)) {
                node.parent = null;
                if (children.size() == 0 && !(this instanceof FrontierArc)) {
                    parent.removeNode(this, directrix);
                }
                return true;
            }
            return false;
        }

        /** 
         * Deletes arc from a tree, and returns the new vertex that results.
         * @return vertex between new adjacent arcs, or <code>null</code> if the arc cannot be deleted
         */
        private FrontierVertex removeArc(FrontierArc arc, double directrix) {
            if (VERBOSE) {
                System.out.println("          Removing arc " + arc);
            }
            if (children.remove(arc)) {
                FrontierArc oldUpper = arc.upperArc();
                FrontierArc oldLower = arc.lowerArc();
                FrontierVertex result = FrontierVertex.joinArcInstance(oldLower, oldUpper, directrix);
                arc.upperVertex.end = result.start;
                arc.lowerVertex.end = result.start;
                if (children.size() == 1) {
                    parent.replaceNode(this, children.get(0));
                } else if (children.size() == 0 && !(this instanceof FrontierArc)) {
                    parent.removeNode(this, directrix);
                }
                return result;
            }
            return null;
        }

        // NAVIGATION
        FrontierNode uppermostChild() {
            return children.size() > 0 ? children.get(children.size() - 1) : null;
        }

        FrontierArc uppermostArc() {
            return (this instanceof FrontierArc) ? (FrontierArc) this : uppermostChild().uppermostArc();
        }

        FrontierNode lowermostChild() {
            return children.size() > 0 ? children.get(0) : null;
        }

        FrontierArc lowermostArc() {
            return (this instanceof FrontierArc) ? (FrontierArc) this : lowermostChild().lowermostArc();
        }

        FrontierArc upperArc() {
            return children.size() > 0 ? uppermostArc().upperArc() : null;
        }

        FrontierArc lowerArc() {
            return children.size() > 0 ? lowermostArc().lowerArc() : null;
        }

        // VALUES
        double upperY(double directrix) {
            return uppermostArc().upperY(directrix);
        }

        double lowerY(double directrix) {
            return lowermostArc().lowerY(directrix);
        }

        /** Returns +1 if the y-value is greater than the intersection y-value, -1 if it is less, or 0 if it is equal to it. */
        int contains(double y, double directrix) {
            double up = upperY(directrix);
            double low = lowerY(directrix);
            int result = y > up ? +1 : y < low ? -1 : 0;
            if (REALLY_VERBOSE) {
                System.out.println("          Node : " + toString() + ", y=" + y + ", directrix = " + directrix + ", lower=" + low + ", upper=" + up + ", contains=" + result);
            }
            return result;
        }

        /** Searches for point of insertion for the given directrix. */
        private FrontierArc findInsertionPoint(double y, double directrix) {
            if (children.size() == 0) {
                return (FrontierArc) this;
            }
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).contains(y, directrix) == 0) {
                    return children.get(i).findInsertionPoint(y, directrix);
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "FrontierNode[" + children.size() + "]";
        }

        public void print(String head) {
            System.out.println(head + this);
            for (int i = 0; i < children.size(); i++) {
                children.get(i).print(head + " .");
            }
        }
    }

    /** Represents a frontier arc */
     public static class FrontierArc extends FrontierNode {

        Point2D.Double point;
        Point2D.Double terminates = null;
        FrontierVertex upperVertex, lowerVertex;

        FrontierArc(Point2D.Double point) {
            super();
            this.point = point;
        }

        @Override
        FrontierArc upperArc() {
            return upperVertex == null ? null : upperVertex.upperArc;
        }

        @Override
        FrontierArc lowerArc() {
            return lowerVertex == null ? null : lowerVertex.lowerArc;
        }

        @Override
        public double lowerY(double directrix) {
            return lowerVertex == null ? Double.NEGATIVE_INFINITY : lowerVertex.value(directrix).y;
        }

        @Override
        public double upperY(double directrix) {
            return upperVertex == null ? Double.POSITIVE_INFINITY : upperVertex.value(directrix).y;
        }

        public Point2D.Double getPoint() {
            return point;
        }

        /** Checks to see if the arc will terminate; if so, sets the point at which termination event should occur.
         * @param directrix
         * @return null if no termination event, otherwise the coordinate of that event
         */
        Point2D.Double pointOfTermination(double directrix) {
            if (VERBOSE) {
                System.out.print("          PointOfTermination: ");
            }
            double xInt, yInt;
            if (upperVertex == null || lowerVertex == null || upperVertex.im == lowerVertex.im) {
                terminates = null;
                if (VERBOSE) {
                    System.out.println("0 or 1 adjoining vertices, so nothing to check here.");
                }
                return null;
            }
            if (VERBOSE) {
                System.out.print("lower=" + lowerVertex.start.x + "," + lowerVertex.start.y + " slope=" + lowerVertex.slope.x + "," + lowerVertex.slope.y + "; upper=" + upperVertex.start.x + "," + upperVertex.start.y + " slope=" + upperVertex.slope.x + "," + upperVertex.slope.y);
            }
            if (upperVertex.slope.y == 0) {
                yInt = upperVertex.start.y;
                xInt = lowerVertex.im * yInt + lowerVertex.ib;
            } else if (lowerVertex.slope.y == 0) {
                yInt = lowerVertex.start.y;
                xInt = upperVertex.im * yInt + upperVertex.ib;
            } else {
                yInt = (upperVertex.ib - lowerVertex.ib) / (lowerVertex.im - upperVertex.im);
                xInt = lowerVertex.im * yInt + lowerVertex.ib;
            }
            double futureU = upperVertex.slope.x == 0
                    ? (yInt - upperVertex.start.y) * (upperVertex.slope.y)    // vertical line
                    : (xInt - upperVertex.start.x) * (upperVertex.slope.x);
            double futureL = lowerVertex.slope.x == 0
                    ? (yInt - lowerVertex.start.y) * (lowerVertex.slope.y)    // vertical line
                    : (xInt - lowerVertex.start.x) * (lowerVertex.slope.x);
            if ( (!upperVertex.bidir && futureU < 0) || (!lowerVertex.bidir && futureL < 0) ) {
                terminates = null;
                if (VERBOSE) System.out.println("... does not terminate!");
            } else if (futureU == 0 && futureL == 0) {
                if (upperVertex.initialDirectrix != lowerVertex.initialDirectrix) {
                    terminates = new Point2D.Double(upperVertex.start.x, upperVertex.start.y);
                    if (VERBOSE) System.out.println("... starting points coincide at " + upperVertex.start.x + "," + upperVertex.start.y);
                } else {
                    terminates = null;
                    if (VERBOSE) System.out.println("... starting points accidently coincide, no termination");
                }
            } else {
                terminates = new Point2D.Double(xInt, yInt);
                if (VERBOSE) System.out.println("... terminates at " + terminates.x + "," + terminates.y);
            }
            return terminates;
        }

        @Override
        public String toString() {
            return "FrontierArc[" + point.x + "," + point.y + " ; terminates @ " + terminates + (lowerVertex == null ? "" : " ; lower = " + lowerVertex) + (upperVertex == null ? "" : " ; upper = " + upperVertex);
        }
    }

    /** Represents a frontier vertex, being a point adjacent to two FrontierArc's. */
    public static class FrontierVertex {

        /** Pointer to upper arc */
        FrontierArc upperArc;
        /** Pointer to lower arc */
        FrontierArc lowerArc;
        /** Starting point of edge */
        Point2D.Double start;
        /** Initial directrix */
        double initialDirectrix;
        /** Slope of edge */
        Point2D.Double slope;
        /** Endpoint of edge (may be null) */
        Point2D.Double end;
        /** Whether vertex is "bi-directional", i.e. intersections count in both directions... user must explicitly set! */
        boolean bidir = false;

        private FrontierVertex(FrontierArc lower, FrontierArc upper) {
            if (upper == null || lower == null) {
                return;
            }
            upperArc = upper;
            lowerArc = lower;
            upper.lowerVertex = this;
            lower.upperVertex = this;
        }

        /** 
         * Return instance of a frontier vertex beginning where the directrix first crosses a vertex.
         */
        static FrontierVertex atDirectrixInstance(FrontierArc lower, FrontierArc upper, double directrix) {
            FrontierVertex result = new FrontierVertex(lower, upper);
            result.initialDirectrix = directrix;
            if (upper.point.x == lower.point.x) {
                result.start = new Point2D.Double(upper.point.x, (upper.point.y + lower.point.y) / 2);
            } else if (upper.point.x < lower.point.x) {
                result.start = new Point2D.Double(getXOnParabolaOfGivenFocusAndDirectrix(lower.point.y, upper.point, directrix), lower.point.y);
            } else {
                result.start = new Point2D.Double(getXOnParabolaOfGivenFocusAndDirectrix(upper.point.y, lower.point, directrix), upper.point.y);
            }
            result.calcParameters();
            return result;
        }

        /**
         * Return instance of frontier vertex beginning where two arcs begin intersecting with respect to the directrix...
         * the initial point of intersection is the point equidistant from the two vertices and the directrix.
         */
        static FrontierVertex joinArcInstance(FrontierArc lower, FrontierArc upper, double directrix) {
            if (upper == null || lower == null) {
                return null;
            }
            FrontierVertex result = new FrontierVertex(lower, upper);
            result.initialDirectrix = directrix;
            result.start = PlanarGeometryUtils.getIntersectionOfParabolasWithCommonDirectrix(lower.point, upper.point, directrix)[0];
            result.calcParameters();
            return result;
        }
        transient double im;
        transient double ib;
        transient double csum;

        /** set constants here to quicken calculations */
        private void calcParameters() {
            slope = perpSlope(upperArc.point, lowerArc.point);
            im = slope.x / slope.y;
            ib = start.x - im * start.y;
            csum = (upperArc.point.x - ib) * (upperArc.point.x - ib) + upperArc.point.y * upperArc.point.y;
        }
        
        /** Finds the value of the vertex for the specified frontier directrix. */
        Point2D.Double value(double directrix) {
            if (initialDirectrix == directrix) {
                return start;
            }
            double yRoot = slope.y == 0 ? start.y
                    : quadraticRoot(1,
                    2 * im * (directrix - upperArc.point.x) - 2 * upperArc.point.y,
                    csum - (directrix - ib) * (directrix - ib),
                    slope.y > 0); // if half-line is going up, look for higher y-value, else look for lower y-value
            Point2D.Double result = new Point2D.Double(getXOnParabolaOfGivenFocusAndDirectrix(yRoot, upperArc.point, directrix), yRoot);
            return result;
        }

        @Override
        public String toString() {
            return "FrontierVertex[upper=" + upperArc.point.x + "," + upperArc.point.y + "; lower=" + lowerArc.point.x + "," + lowerArc.point.y + "; start=" + start.x + "," + start.y + ", slope=" + slope.x + "," + slope.y + ", im=" + im + ", ib=" + ib + ", csum=" + csum + "]";
        }

    }
}
