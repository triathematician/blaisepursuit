/**
 * Tesselation.java
 * Created on Dec 7, 2009
 */

package org.bm.blaise.scio.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *    This class describes the structure of a tesselation of the plane. A tesselation is a combinatorial object
 *    consisting of a set of vertices, a set of edges joining these vertices, and a set of polygons whose boundaries
 *    are the given edges.
 * </p>
 * <p>
 *    The edges may be finite or infinite. If finite, they consist of two points.
 *    If infinite, they are basically rays and consist of a point and a point at infinity.
 *    By convention, points at infinity are given in polar coordinates, where the first term
 *    is the (infinite radius) and the second is the angle.
 * </p>
 * @author Elisha Peterson
 */
public class Tesselation {

    /** Vertices in the tesselation. */
    List<Point2D.Double> vertices;
    /** Edges in the tesselation. */
    List<Edge> edges;
    /** Polygons in the tesselation. */
    List<Polygon> polygons;

    /**
     * Construct without any polygons.
     */
    public Tesselation() {
        vertices = new ArrayList<Point2D.Double>();
        edges = new ArrayList<Edge>();
        polygons = new ArrayList<Polygon>();
    }

    /** Adds a vertex to the tesselation. */
    public void addVertex(Point2D.Double v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
        }
    }

    /**
     * Adds an edge to the tesselation, along with its vertices if necessary.
     * @param v1 first vertex of edge
     * @param v2 second vertex of edge
     */
    public void addEdge(Point2D.Double v1, Point2D.Double v2) {
        addEdge(new Edge(v1, v2));
    }

    /**
     * Adds an edge to the tesselation, along with its vertices if necessary.
     * @param edge the edge
     */
    public void addEdge(Edge edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
            addVertex(edge.v1);
            addVertex(edge.v2);
        }
    }

    /**
     * Adds a polygon to the tesselation. If the vertices or edges are
     * already in the tesselation, uses the stored pointers corresponding to
     * those edges or vertices. Otherwise creates new ones and adds them to
     * the tesselation's set of edges and vertices.
     * @param vPolygon the vertices of the polygon
     */
    public void addPolygon(Point2D.Double... vPolygon) {
        addPolygon(new Polygon(vPolygon));
    }

    /**
     * Adds a polygon to the tesselation. If the vertices or edges are
     * already in the tesselation, uses the stored pointers corresponding to
     * those edges or vertices. Otherwise creates new ones and adds them to
     * the tesselation's set of edges and vertices.
     * @param p the new polygon
     */
    public void addPolygon(Polygon p) {
        if (!polygons.contains(p)) {
            polygons.add(p);
            for (Edge e : p.edges) {
                addEdge(e);
            }
        }
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public List<Point2D.Double> getVertices() {
        return vertices;
    }

    /**
     * Represents a polygon comprised of a list of edges and vertices.
     * The i-edge includes the i-vertex and the (i+1)-vertex.
     */
    public static class Polygon {
        Point2D.Double[] vertices;
        List<Edge> edges;
        public Polygon(Point2D.Double... vertices) {
            this.vertices = vertices;
            edges = new ArrayList<Edge>();
            for (int i = 0; i < vertices.length; i++) {
                Edge newEdge = new Edge(vertices[i], vertices[(i+1)%vertices.length]);
                newEdge.p1 = this;
                edges.add(newEdge);
            }
        }
        public Point2D.Double[] getVertices() {
            return vertices;
        }
        @Override public String toString() {
            return "Polygon[ " + Arrays.toString(vertices) + " ]";
        }
    }

    /**
     * This represents an edge with two vertices. The edge is essentially a segment from v1 to v2.
     * Because we are also interested in adjacent polygons, the edge has pointers for the "first"
     * and "second" adjacent polygons. The first polygon is to the left of the edge, when traversed from
     * v1 to v2. The second is to the right.
     */
    public static class Edge {

        /** First vertex */
        public Point2D.Double v1;
        /** Second vertex */
        public Point2D.Double v2;

        /** First adjacent polygon */
        public Polygon p1;
        /** Second adjacent polygon */
        public Polygon p2;

        /** Construct with 2 vertex locations. */
        public Edge(Point2D.Double v1, Point2D.Double v2) { this.v1 = v1; this.v2 = v2; }

        /** @return common vertex with another edge, or null if there is none */
        Point2D.Double commonVertex(Edge e2) {
            if (v1.equals(e2.v1) || v1.equals(e2.v2))
                return v1;
            else if (v2.equals(e2.v1) || v2.equals(e2.v2))
                return v2;
            else
                return null;
        }

        @Override public String toString() {
            return "Edge[ " + v1 + ", " + v2 + "]";
        }

        /** Checks edge equality, including orientation. */
        public boolean equalsDirected(Object obj) {
            if (! (obj instanceof Edge) ) { return false; }
            Edge e2 = (Edge) obj;
            return (e2.v1.equals(v1)&&e2.v2.equals(v2));
        }
        
        /** Checks edge equality, independent of orientation. */
        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Edge) ) { return false; }
            Edge e2 = (Edge) obj;
            return (e2.v1.equals(v1)&&e2.v2.equals(v2)) || (e2.v1.equals(v2)&&e2.v2.equals(v1));
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + (this.v1 != null ? this.v1.hashCode() : 0);
            hash = 23 * hash + (this.v2 != null ? this.v2.hashCode() : 0);
            return hash;
        }
    }
}
