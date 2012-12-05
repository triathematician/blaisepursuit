/**
 * ComparisonType.java
 * Created on Jul 23, 2009
 */

package sim.metrics;

/**
 * <p>
 *   The <code>ComparisonType</code> enum encodes various ways to compare two
 *   double values, allowing the user to programmatically set them.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum ComparisonType {
    /** Returns true when first value is less than second. */
    LESS("<") {
        public boolean compare(double value1, double value2) {
            return value1 < value2;
        }
    },
    /** Returns true when first value is less than or equal to second. */
    LESS_EQUAL("<=") {
        public boolean compare(double value1, double value2) {
            return value1 <= value2;
        }
    },
    /** Returns true when first value is equal to second. */
    EQUAL("==") {
        public boolean compare(double value1, double value2) {
            return value1 == value2;
        }
    },
    /** Returns true when first value is greater than or equal to second. */
    GREATER_EQUAL(">=") {
        public boolean compare(double value1, double value2) {
            return value1 >= value2;
        }
    },
    /** Returns true when first value is greater than second. */
    GREATER(">") {
        public boolean compare(double value1, double value2) {
            return value1 > value2;
        }
    },
    /** Returns true when first value is not equal to second. */
    NOT_EQUAL("!=") {
        public boolean compare(double value1, double value2) {
            return value1 != value2;
        }
    };

    String str;
    @Override public String toString() { return str; }
    ComparisonType(String str) { this.str = str; }
    
    /** Compares the first value to the second. */
    abstract public boolean compare(double value1, double value2);
}
