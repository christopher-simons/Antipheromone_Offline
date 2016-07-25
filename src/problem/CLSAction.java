
/*
 * CLSAction.java
 *
 * Created on 14 June 2004, 16:06
 * Greatly refactored 28 September 2100
 */

package problem;

/**
 *
 * @author Christopher Simons
 */


public class CLSAction 
{
    /** uniquely identifying name */
    private String name;
    
    /** unique number */
    private int number;
    
    /** default constructor */
    public CLSAction() 
    {
        name = "un-named";
        number = -1;
     }
    
    /** constructor with parameters */
    public CLSAction( String n, int number )
    {
        assert( n != null );
        name = n;
        assert number >= 0;
        this.number = number;
    }
    
    /** set the name of the action */
    public void setName( String newName )
    {
        assert( newName != null );
        name = newName;
    }
    
    /** get the name of the action */
    public String getName( )
    {
        assert( name != null );
        return name;
    }

    /**
     * @return the number
     */
    public int getNumber() 
    {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) 
    {
        this.number = number;
    }

}   // end class

//------- end of file -------------------------------------