/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Arrays;

/**
 *
 * @author elisha
 */
public class StatUtils {

    /**
     * Computes mean & dev of a list of doubles.
     * Note that dev is the sample standard deviation
     * @param list a list of data
     * @return length-3 array of the form { mean - dev, mean, mean + dev }
     */
    public static double[] stats1(double[] list) {
        double mean = 0.0;
        for (double d : list) mean += d;
        mean /= list.length;

        double sse = 0.0;
        for (double d : list) sse += (d - mean)*(d - mean);
        double dev = Math.sqrt(sse/list.length);
        return new double[] { mean - dev, mean, mean + dev };
    }

    /**
     * Computes mean & dev of a list of doubles.
     * Note that dev is the sample standard deviation
     * @param list a list of data
     * @param i index of element to pull out of list
     * @return array of the form { mean - dev, mean, mean + dev }
     */
    public static double[] stats1(double[][] list, int i) {
        double[] sample = new double[list.length];
        for (int j = 0; j < list.length; j++) sample[j] = list[j][i];
        return stats1(sample);
    }

    /**
     * Computes mean & dev of a list of doubles.
     * Note that dev is the sample standard deviation
     * @param list a list of data
     * @param i index of element to pull out of list
     * @return array of the form { mean - dev, mean, mean + dev }
     */
    public static double[] stats1(double[][][] list, int i, int j) {
        double[] sample = new double[list.length];
        for (int k = 0; k < list.length; k++) sample[k] = list[k][i][j];
        return stats1(sample);
    }

    /**
     * Performs percentile analysis of a sample list of doubles.
     * @param list an array of data
     * @return length-5 array of the form { min, 10%, 50%, 90%, max }
     */
    public static double[] stats2(double[] list) {
        Arrays.sort(list);
        int n = list.length;

        double min   = list[0];
        double tenth = list[(int)(.1*n+.5)-1];
        double med   = (n%2==0) ? .5*(list[n/2]+list[n/2-1]) : list[(n-1)/2];
        double ninth = list[(int)(.9*n+.5)-1];
        double max   = list[n-1];

        return new double[] { min, tenth, med, ninth, max };
    }

    /**
     * Performs percentile analysis of a sample list of doubles.
     * @param list an array of data
     * @param i index of element to pull out of list
     * @return array of the form { min, 10%, 50%, 90%, max }
     */
    public static double[] stats2(double[][] list, int i) {
        double[] sample = new double[list.length];
        for (int j = 0; j < list.length; j++) sample[j] = list[j][i];
        return stats2(sample);
    }

    /**
     * Performs percentile analysis of a sample list of doubles.
     * @param list an array of data
     * @param i index of element to pull out of list
     * @param j second index to pull out
     * @return array of the form { min, 10%, 50%, 90%, max }
     */
    public static double[] stats2(double[][][] list, int i, int j) {
        double[] sample = new double[list.length];
        for (int k = 0; k < list.length; k++) sample[k] = list[k][i][j];
        return stats2(sample);
    }


    /**
     * Computes mean, dev, & percentiles of a list of doubles.
     * Note that dev is the sample standard deviation
     * @param list a list of data
     * @param i index of element to pull out of list
     * @return array of the form { mean - dev, mean, mean + dev, min, 10%, 50%, 90%, max }
     */
    static double[] stats3(double[][][] list, int i, int j) {
        double[] result = new double[8];
        double[] r1 = stats1(list, i, j);
        double[] r2 = stats2(list, i, j);
        System.arraycopy(r1, 0, result, 0, 3);
        System.arraycopy(r2, 0, result, 3, 5);
        return result;
    }


    /** Prints table of values in a tab-delimited manner */
    public static void tabPrint(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(tabPrint(arr[i]));
        }
    }

    /** Prints list of values delimited by tabs */
    public static String tabPrint(double[] arr) {
        String s = Double.toString(arr[0]);
        for (int i = 1; i < arr.length; i++) s += "\t" + Double.toString(arr[i]);
        return s;
    }
}
