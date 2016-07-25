/*
 * Couple.java
 * Created on 15 February 2007, 14:58
 * 
 */
 
package softwareDesign;

/**
 * @author clsimons
 */

public class Couple
{
    public int source;
    
    public int target;
    
    public int counter;
    
    /**
     * Creates a new instance of Couple
     */
    public Couple()
    {
        // be unkind and force the attribute values to be set
        source = -1;
        target = -1;
        // set counter to zero
        counter = 0;
    }
    
}

//------- end of file -------------------------------------