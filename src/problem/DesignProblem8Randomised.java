/*
 * DesignProblme8Randomised.java
 * created 9 November 2011
 */

package problem;


/**
 * @author Christopher Simons
 * 
 * The randomised design problem.
 * 
 * The real design problem figures are as follows:
 * 
 *      Actions Data    Uses    Number of Classes
 * CBS  15      16      40      5
 * GDP  12      43      107     5
 * SC   29      62      144     15
 * 
 * Taking the average of all three:
 * 
 *      18      40      97      8
 * 
 * Thus the size of the design problem is the same every time,
 * i.e. 18 actions and 40 data partitioned across 8 classes. 
 * 
 * First time around, a randomised use table is created,
 * and is written to file as a use matrix.  
 * However, second (and subsequent times around), the user
 * has the option of EITHER generating a fresh use matrix,
 * OR loading the previous use matrix from file.
 * 
 * 21 Jan 2016
 * Above is nice idea, but for Jan 2016 experiments, 
 * A randomly generated use matrix is hard coded. 
 */

import java.util.*;


public class DesignProblem8Randomised extends DesignProblem
{
    private final static boolean RELOAD = false;
    
    /** for local manipulation in this class */
    private List< CLSAction > actionList;
    private List< CLSDatum > datumList;
    
    /**
     * constructor
     * @param problemController 
     */
    public DesignProblem8Randomised( ProblemController problemController )
    {
        super( problemController );
        
        actionList = new ArrayList< CLSAction >( );
        datumList = new ArrayList< CLSDatum >( );
        
        int counter = 0;
        
        a0 = new CLSAction( Integer.toString( counter ), counter++ ); 
        actionList.add( a0 );
        a1 = new CLSAction( Integer.toString( counter ), counter++ ); 
        actionList.add( a1 );
        a2 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a2 );
        a3 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a3 );
        a4 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a4 );
        a5 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a5 );
        a6 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a6 );
        a7 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a7 );
        a8 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a8 );
        a9 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a9 );
        a10 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a10 );
        a11 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a11 );
        a12 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a12 );
        a13 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a13 );
        a14 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a14 );
        a15 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a15 );
        a16 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a16 );
        a17 = new CLSAction( Integer.toString( counter ), counter++ );
        actionList.add( a17 );
        assert actionList.size( ) == 18;
                
        counter = 0;
        
        d0 = new CLSDatum( Integer.toString( counter ), counter++ );
        d1 = new CLSDatum( Integer.toString( counter ), counter++ );
        d2 = new CLSDatum( Integer.toString( counter ), counter++ );
        d3 = new CLSDatum( Integer.toString( counter ), counter++ );
        d4 = new CLSDatum( Integer.toString( counter ), counter++ );
        d5 = new CLSDatum( Integer.toString( counter ), counter++ );
        d6 = new CLSDatum( Integer.toString( counter ), counter++ );
        d7 = new CLSDatum( Integer.toString( counter ), counter++ );
        d8 = new CLSDatum( Integer.toString( counter ), counter++ );
        d9 = new CLSDatum( Integer.toString( counter ), counter++ );
        d10 = new CLSDatum( Integer.toString( counter ), counter++ );
        d11 = new CLSDatum( Integer.toString( counter ), counter++ );
        d12 = new CLSDatum( Integer.toString( counter ), counter++ );
        d13 = new CLSDatum( Integer.toString( counter ), counter++ );
        d14 = new CLSDatum( Integer.toString( counter ), counter++ );
        d15 = new CLSDatum( Integer.toString( counter ), counter++ );
        d16 = new CLSDatum( Integer.toString( counter ), counter++ );
        d17 = new CLSDatum( Integer.toString( counter ), counter++ );
        d18 = new CLSDatum( Integer.toString( counter ), counter++ );
        d19 = new CLSDatum( Integer.toString( counter ), counter++ );
        d20 = new CLSDatum( Integer.toString( counter ), counter++ );
        d21 = new CLSDatum( Integer.toString( counter ), counter++ );
        d22 = new CLSDatum( Integer.toString( counter ), counter++ );
        d23 = new CLSDatum( Integer.toString( counter ), counter++ );
        d24 = new CLSDatum( Integer.toString( counter ), counter++ );
        d25 = new CLSDatum( Integer.toString( counter ), counter++ );
        d26 = new CLSDatum( Integer.toString( counter ), counter++ );
        d27 = new CLSDatum( Integer.toString( counter ), counter++ );
        d28 = new CLSDatum( Integer.toString( counter ), counter++ );
        d29 = new CLSDatum( Integer.toString( counter ), counter++ );
        d30 = new CLSDatum( Integer.toString( counter ), counter++ );
        d31 = new CLSDatum( Integer.toString( counter ), counter++ );       
        d32 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d33 = new CLSDatum( Integer.toString( counter ), counter++ );       
        d34 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d35 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d36 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d37 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d38 = new CLSDatum( Integer.toString( counter ), counter++ );        
        d39 = new CLSDatum( Integer.toString( counter ), counter++ ); 
        datumList.add( d0 );
        datumList.add( d1 );
        datumList.add( d2 );
        datumList.add( d3 );
        datumList.add( d4 );
        datumList.add( d5 );
        datumList.add( d6 );
        datumList.add( d7 );
        datumList.add( d8 );
        datumList.add( d9 );
        datumList.add( d10 );
        datumList.add( d11 );
        datumList.add( d12 );
        datumList.add( d13 );
        datumList.add( d14 );
        datumList.add( d15 );
        datumList.add( d16 );
        datumList.add( d17 );
        datumList.add( d18 );
        datumList.add( d19 );
        datumList.add( d20 );
        datumList.add( d21 );
        datumList.add( d22 );
        datumList.add( d23 );
        datumList.add( d24 );
        datumList.add( d25 );
        datumList.add( d26 );
        datumList.add( d27 );
        datumList.add( d28 );
        datumList.add( d29 );
        datumList.add( d30 );
        datumList.add( d31 );
        datumList.add( d32 );
        datumList.add( d33 );
        datumList.add( d34 );
        datumList.add( d35 );
        datumList.add( d36 );
        datumList.add( d37 );
        datumList.add( d38 );
        datumList.add( d39 );
        assert datumList.size( ) == 40;
    }
    
    /**
     * assemble design problem 8 - randomised
     */
    public void constructRandomisedDesignProblem8( )
    {
        // add all the actions to the problem space
        for( CLSAction a : this.actionList )
        {
            problemController.addActiontoProblemSpace( a );
        }
        assert problemController.getNumberOfUniqueActions( ) ==
            this.actionList.size( );
                
        // add all the data to the problem space
        problemController.addDatumToProblemSpace( this.datumList );
        assert problemController.getNumberOfUniqueData( ) ==
            this.datumList.size( );
        

        /*
         * for each action
         * 
         *      for a random number of times around 5.388
         * 
         *          get a data at random
         *          add it to a local data list
         * 
         *      add the action / data entries to the use table
         */
        
        for( CLSAction a : this.actionList )
        {
            List< CLSDatum > localDatumList = new ArrayList< CLSDatum >( );
            
            // get a random integer around 5.3888
            long random = getRandomNumberOfUses( );
            
            // make sure no datum is duplicated in the list
            List< Integer > intList = new ArrayList< Integer >( );
            
            
            for( int i = 0; i < random; i++ )
            {
                int randomDatumNumber = myUtils.Utility.getRandomInRange( 
                    this.datumList.size( ) - 1, 0 );
            
                // check for duplication
                while( intList.contains( randomDatumNumber ) )
                {
                    randomDatumNumber = myUtils.Utility.getRandomInRange( 
                        this.datumList.size( ) - 1, 0 );
                }
                
                intList.add( randomDatumNumber );
                
                assert randomDatumNumber > 0;
                assert randomDatumNumber < this.datumList.size( );
                
                CLSDatum datum = getDatum( randomDatumNumber );
                localDatumList.add( datum );
            }
            
            problemController.addEntryToUseTable( a.getName( ), localDatumList );
        }
        
    }

    /**
     * 
     * @return random number of uses
     */
    private long getRandomNumberOfUses( )
    {
        // number of uses is 97
        // number of actions is 18
        // on average, that's 5.3888 uses per action
        final double seed = 5.3888;
        
        Random generator = new Random( );

        // Returns the next pseudorandom, Gaussian
        // ("normally") distributed double value with
        // mean 0.0 and standard deviation 1.0 from
        // this random number generator's sequence.
        // approx 66% lie from -1 to +1 (one st dev)
        // approx 95% lie from -2 to +2 (two st dev)

        double number = generator.nextGaussian( );
        
        double temp = seed + number;
        
        long result = Math.round( temp );
        assert result > 0;
        assert result < 10; // gotta put something reasonable!
        
        return result;
    }
    
    /**
     * get a datum from the datum list based on number
     * @param number 
     * @return datum
     */
    private CLSDatum getDatum( int number )
    {
        assert number >= 0;
        assert number < this.datumList.size( );
        
        CLSDatum result = null;
        boolean found = false;
        
        for( int i = 0; i < this.datumList.size( ) && found == false; i++ )
        {
            if( i == number )
            {
                result = this.datumList.get( i );
                found = true;
            }
        }
        assert found == true;
        assert result != null;
        return result;
    }
   
           
}   // end class

//------- end file ----------------------------------------

