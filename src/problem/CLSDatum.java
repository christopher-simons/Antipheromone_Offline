/*
 * CLSDatum.java
 *
 * Created on 14 June 2004, 17:01
 * Greatly refactored 28 September 2011
 */

package problem;

/**
 *
 * @author Christopher Simons
 */

public class CLSDatum 
{
    private String name;
    
    private int number;

    /** default constructor */
    public CLSDatum() 
    {
        name = "un-named";
        number = -1;
    }

    /**
     * set the datum name
     * @param name as string
     */
    public CLSDatum( String s, int number ) 
    {
        assert( s != null );
        name = s;
        assert number >= 0;
        this.number = number;
    }
    
    /** get the name of the Datum
     * @return name as String
     */
    public String getName( ) 
    { 
        assert( name != null ); 
        return name; 
    }
    
    /** set the name of the datum
     * @param name as String
     */
    public void setName( String n )
    {
        assert( n != null );
        name = n;
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

