package team.software.irbl.core.test;

/**
 * This class provides access to a small number of SWT system-wide
 * methods, and in addition defines the public constants provided
 * by SWT.
 * <p>
 * By defining constants like UP and DOWN in a single class, SWT
 * can share common names and concepts at the same time minimizing
 * the number of classes, names and constants for the application
 * programmer.
 * </p><p>
 * Note that some of the constants provided by this class represent
 * optional, appearance related aspects of widgets which are available
 * either only on some window systems, or for a differing set of
 * widgets on each window system. These constants are marked
 * as <em>HINT</em>s. The set of widgets which support a particular
 * <em>HINT</em> may change from release to release, although we typically
 * will not withdraw support for a <em>HINT</em> once it is made available.
 * </p>
 */
public class Test1 {
    /**
     * This is field doc
     */
    private int field1;

    /**
     * This is a method doc
     */
    public void methodJava(int arg1){
        /**
        * This is a inner field doc
         */
        int arg2 = arg1 * 2;
        System.out.println(arg2);
    }

    /**
     * This is a inner class doc
     */
    class innerTest{

        /**
         * This is a inner class field doc
         */
        private int innerField1;

        /**
         * This is a inner class method doc
         */
        public void innerMethod1(int arg1){
            int arg2 = arg1 * 2;
            System.out.println(arg2);
        }

    }
}

/**
 * This is a outer class doc
 */
class outerTest{

    /**
     * This is a outer class field doc
     */
    private int outerField1;

    /**
     * This is a outer class method doc
     */
    public void outerMethod1(int arg1){
        int arg2 = arg1 * 2;
        System.out.println(arg2);
    }

}