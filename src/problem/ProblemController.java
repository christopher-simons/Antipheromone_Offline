/*
 * ProblemController.java
 *
 * Created on 11 June 2004, 16:36
 * Refactored greatly 28 September 2011
 */
 

package problem;

/**
 *
 * @author Christopher Simons
 */


import config.Parameters;
import java.io.*;
import java.util.*;


public class ProblemController 
{
    /** file name for use matrix */
    private static final String USE_MATRIX_FILE_NAME = "UseMatrix.dat";
    
    /** List of all Data in the problem space */
    private List< CLSDatum > datumList;
    
    /** List of all actions in the problem space */
    private List< CLSAction > actionList;
    
    /** 
     * Table of all action names and the data that
     * they're associated with since they're 
     * found (used) in the same step within a use case.
     */
    private SortedMap< String, List< CLSDatum > > useTable;
    
    /** matrix if uses - actions x data */
    private int[ ][ ] useMatrix;
    
    /** number of couples in the problem,
     * calculated when the use matrix is generated
     */
    private int numberOfUses;
    
    /** 
     * number of actions in this problem, contained 
     * in the header row of the use matrix file 
     */
    private int numberOfActionsForReload;
    
    /**
     * number of data in the problem, contained
     * in the header row of the use matrix file
     */
    private int numberOfDataForReload;
    
    /** number of classes intended for design solutions */
    private int classNumber;
    
    // 0 is there's a variable number of objects in a chromosome
    // otherwise the number of classes required is held here
    public static final int VARIABLE_CLASS_NUMBER = 0;
    
    // the problem that we're dealing with at the moment
    private int currentProblemInstance = 0; 
    
    // reference to either Berlin52 or ST70
    private TSP tsp; 
        
    /** Creates a new instance of ProblemController */
    public ProblemController() 
    {
        datumList = new ArrayList< CLSDatum >( );
        actionList = new ArrayList< CLSAction >( );
        useTable = new TreeMap< String, List< CLSDatum > >( );
        classNumber = VARIABLE_CLASS_NUMBER;
        
        useMatrix = null;   // must generate use table first!!
        numberOfUses = 0;
        
        numberOfActionsForReload = 0;
        numberOfDataForReload = 0;
        currentProblemInstance = 0;
        
        tsp = null;
    }
    
    /** create design problem number 5 
     *  Cinema Booking system
     */
    public void createDesignProblem5( )
    {
        DesignProblem5CBS dp5 = new DesignProblem5CBS( this );
        dp5.assembleDesignProblem5( );
        this.currentProblemInstance = Parameters.CBS;

//        this.showActionsAndData( );
    }
    
    /** create design problem number 6 
     *  Select Cruises
     */
    public void createDesignProblem6( )
    {
        DesignProblem6SC dp6 = new DesignProblem6SC( this );
        dp6.assembleDesignProblem6( );
        this.currentProblemInstance = Parameters.SC;
    }
    
    /** 
     *  create design problem number 7 
     *  Graduate Development Program (GDP)
     */
    public void createDesignProblem7( )
    {
        DesignProblem7GDP dp7 = new DesignProblem7GDP( this );
        dp7.assembleDesignProblem7( );
        this.currentProblemInstance = Parameters.GDP;
    }
    
    /** 
     *  create design problem number 8 
     *  Randomised
     */
    public void createDesignProblem8( )
    {
        DesignProblem8Randomised dp8 = new DesignProblem8Randomised( this );
        dp8.constructRandomisedDesignProblem8( );
        this.currentProblemInstance = Parameters.RANDOMISED;
    }
    
    // 23 September 2018
    public void createTSPBerlin52Problem( )
    {
        this.tsp = new TSP_Berlin52( );
        this.tsp.configure( );
//        this.tsp.showDistances( );
        this.currentProblemInstance = Parameters.TSP_BERLIN52;
    }
    
    public final TSP getTSP( )
    {
        assert this.tsp != null;
        return tsp;
    }
    
    // 22 November 2018
    public void createTSPST70Problem( )
    {
        this.tsp = new TSP_ST70( );
        this.tsp.configure( );
        this.tsp.showDistances( );
        this.currentProblemInstance = Parameters.TSP_ST70;
    }
    
    // 23 November 2018
    public void createTSPRAT99Problem( )
    {
        this.tsp = new TSP_RAT99( );
        this.tsp.configure( );
        this.tsp.showDistances( );
        this.currentProblemInstance = Parameters.TSP_RAT99;
    }
 
    // 23 November 2018
    public void createTSPRAT195Problem( )
    {
        this.tsp = new TSP_RAT195( );
        this.tsp.configure( );
        this.tsp.showDistances( );
        this.currentProblemInstance = Parameters.TSP_RAT195;
    }
    
    
    /**
     * get a handle on the list of unique actions
     * so a client can iterate over all data
     * Again, very trusting of the client!
     * @return Iterator< CLSDatum >
     */
    public Iterator< CLSAction > getActionList( )
    {
        return actionList.iterator( );
    }
    
    /** get number of unique action items in problem
     *  @return number of datum items as integer
     */
    public int getNumberOfUniqueActions( )
    {
        return actionList.size( );
    }
    
    /** attempt to add an action to the problem space.
     *  Note that action are uniquely identified by 
     *  their names. So if a action is already present
     *  within the problem, then the call to this method
     *  does nothing.
     *
     *  Note also that an instances of a actions may be
     *  present in many steps!
     */
    public void addActiontoProblemSpace( CLSAction action )
    {
        assert( action != null );
        String name = action.getName( );
        boolean found = false;
        
        // Java 1.5 for loop 
        for( CLSAction a : actionList )
        {
            if( a.getName( ).equals( name ) )
            {
                found = true;
                break;
            }
        }
        if( ! found )
        {
            actionList.add( action );
        }
        /*
        else // for testing on 2 August 2004
        {
            System.out.println( 
                "attempt to add action when already known to problem, " +
                "datum: " + action.getName( ) );
        } 
        */       
    }

    /**
     * get a handle on the list of unique datum
     * so a client can iterate over all data
     * Again, very trusting of the client!
     * @return Iterator< CLSDatum >
     */
    public Iterator< CLSDatum > getDatumList( )
    {
        return datumList.iterator( );
    }
    
    /** get number of unique datum items in problem
     *  @return number of datum items as integer
     */
    public int getNumberOfUniqueData( )
    {
        return datumList.size( );
    }
    
    /** attempt to add a datum item to the problem space.
     *  Note that data are uniquely identified by 
     *  their names. So if a datum is already present
     *  within the problem, then the call to this method
     *  does nothing.
     *
     *  Note also that an instance of a datum may be
     *  present across many steps!
     */
    public void addDatumToProblemSpace( List< CLSDatum > newList )
    {
        assert( newList != null );
        for( CLSDatum newDatum : newList )
        {
            String name = newDatum.getName( );
            boolean found = false;
        
            for( CLSDatum d : datumList )
            {
                if( d.getName( ).equals( name ) )
                {
                    found = true;
                    break;
                }
            }
            if( ! found )
            {
                datumList.add( newDatum );
            }
            else // for testing on 30 July 2004, and 30 Nov 2011
            {
//                System.out.println( 
//                    "attempt to add datum when already known to problem, " +
//                    "datum: " + newDatum.getName( ) );
            }
        }
    }

    /** add entry to use table */
    public void addEntryToUseTable( 
        String actionName, List< CLSDatum > datumList )
    {
        assert actionName != null;
        assert datumList != null;
        useTable.put( actionName, datumList );
    }
    
    /** get a reference to the use table 
     *  Immensely trusting of the client!!!
     *  Breaks all encapsulation!!!
     *  Used by the search controller for evaluating
     *  cohesion fitness of objects
     */
    public SortedMap< String, List< CLSDatum > > getUseTable( )
    {
        assert( useTable != null );
        return useTable;
    }
    
    /** 
     * go for a new, fresh problem context 
     * discard whatever problem is current 
     */
    public void newProblem( )
    {
        actionList.clear( );
        assert( actionList.isEmpty( ) );
        datumList.clear( );
        assert( datumList.isEmpty( ) );
    }
    
        
    /**
     * show current actions and data
     */
    public void showActionsAndData( )
    {
        int useCounter = 0;
        
        System.out.println(" ACTIONS are: ");
        assert actionList != null;
        for( CLSAction a : actionList )
        {
            String name = a.getName( );
            int number = a.getNumber( );
            System.out.println( "\t" + number + " " + name );
            
            System.out.println( "\t" + "\t" + "and this action uses: ");
            List< CLSDatum > list = useTable.get( name );
            assert list != null;
            for( CLSDatum d : list )
            {
                System.out.println( "\t" + "\t" + "\t" + 
                    d.getNumber( ) + " " + d.getName( ) );
                useCounter++;
            }
        }    
    
        System.out.println(" DATA are: ");
        for( CLSDatum d : datumList )
        {
            System.out.println( "\t" + d.getNumber() + " " + d.getName( ) );
        }
        
        System.out.println( "SUMMARY" );
        System.out.println( "\t" + "Number of actions is: " + actionList.size( ) );
        System.out.println( "\t" + "Number of data is: " + datumList.size( ) );
        System.out.println( "\t" + "Number of uses is: " + useCounter );
    }

   
    /**
     * get number of classes for design solutions
     * @return number of classes for design solutions 
     */
    public int getNumberOfClasses( ) 
    {
        return classNumber;
    }

    /**
     * set number of classes for design solutions
     * @param number of classes
     */
    public void setNumberOfClasses( int number )
    {
        assert number > 0; 
        classNumber = number;
    }
    
    /**
     * generate the use matrix
     */
    public void generateUseMatrix( )
    {
        assert this.actionList.isEmpty( ) == false;
        assert this.datumList.isEmpty( ) == false;
        
        this.useMatrix = new int[ actionList.size( ) ][ datumList.size( ) ];
        System.out.println("actions: " + actionList.size( ) );
        System.out.println("data: " + datumList.size( ) );
        
        // horrible, but necessary
        final int FALSE = 0;
        final int TRUE = 1;
        
        for( int i = 0; i < actionList.size(); i++ )
        {
            for( int j = 0; j < datumList.size( ); j++ )
            {
                useMatrix[ i ][ j ] = FALSE;
            }
        }
        
        for( CLSAction a : actionList )
        {
            String name = a.getName( );
            int actionNumber = a.getNumber( );
        
//            System.out.println( "\t" + number + " " + name );
//            System.out.println( "\t" + "\t" + "and this action uses: ");

            List< CLSDatum > list = useTable.get( name );
            assert list != null;
            for( CLSDatum d : list )
            {
//                System.out.println( "\t" + "\t" + "\t" + d.getName( ) );
                  int datumNumber = d.getNumber( );
                  
                  assert datumNumber < datumList.size( );
                  assert actionNumber < actionList.size( );
                  useMatrix[ actionNumber ][ datumNumber ] = TRUE;
                  this.numberOfUses++;
            }
        }  
        
        System.out.println("number of uses in use matrix is: " + this.numberOfUses );
    }
    
    
    public void showUseMatrix( )
    {
        for( int i = 0; i < actionList.size(); i++ )
        {
            for( int j = 0; j < datumList.size( ); j++ )
            {
                System.out.print( useMatrix[ i ][ j ] + " " );
            }
            System.out.println("");
        }
    }
    
    public void initialiseWithPreGenerated( )
    {
        assert this.actionList.isEmpty( ) == false;
        assert this.datumList.isEmpty( ) == false;
        
        this.useMatrix = new int[ actionList.size( ) ][ datumList.size( ) ];
        System.out.println( "actions: " + actionList.size( ) );
        System.out.println( "data: " + datumList.size( ) );
        
        for( int i = 0; i < actionList.size(); i++ )
        {
            for( int j = 0; j < datumList.size( ); j++ )
            {
                useMatrix[ i ][ j ] = 0;
            }
        }
        
        // use previously randomly generated use matrix 
        useMatrix[ 0 ][ 20 ] = 1; this.numberOfUses++;
        useMatrix[ 0 ][ 24 ] = 1; this.numberOfUses++; 
        useMatrix[ 0 ][ 28 ] = 1; this.numberOfUses++;
        useMatrix[ 0 ][ 32 ] = 1; this.numberOfUses++;
        useMatrix[ 0 ][ 36 ] = 1; this.numberOfUses++;
        useMatrix[ 0 ][ 38 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 12 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 21 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 23 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 26 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 32 ] = 1; this.numberOfUses++;
        useMatrix[ 1 ][ 33 ] = 1; this.numberOfUses++;
        useMatrix[ 2 ][ 10 ] = 1; this.numberOfUses++;
        useMatrix[ 2 ][ 18 ] = 1; this.numberOfUses++;
        useMatrix[ 2 ][ 21 ] = 1; this.numberOfUses++;
        useMatrix[ 2 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 6  ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 13 ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 23 ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 25 ] = 1; this.numberOfUses++;
        useMatrix[ 3 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 4 ][ 11 ] = 1; this.numberOfUses++;
        useMatrix[ 4 ][ 27 ] = 1; this.numberOfUses++;
        useMatrix[ 4 ][ 32 ] = 1; this.numberOfUses++;
        useMatrix[ 4 ][ 33 ] = 1; this.numberOfUses++;
        useMatrix[ 4 ][ 38 ] = 1; this.numberOfUses++;
        useMatrix[ 5 ][ 5 ] = 1; this.numberOfUses++;
        useMatrix[ 5 ][ 6 ] = 1; this.numberOfUses++;
        useMatrix[ 5 ][ 26 ] = 1; this.numberOfUses++;
        useMatrix[ 5 ][ 28 ] = 1; this.numberOfUses++;
        useMatrix[ 6 ][ 2 ] = 1; this.numberOfUses++;
        useMatrix[ 6 ][ 6 ] = 1; this.numberOfUses++;
        useMatrix[ 6 ][ 22 ] = 1; this.numberOfUses++;
        useMatrix[ 7 ][ 9 ] = 1; this.numberOfUses++;
        useMatrix[ 7 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 7 ][ 29 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 12 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 19 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 20 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 31 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 34 ] = 1; this.numberOfUses++;
        useMatrix[ 8 ][ 36 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 1 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 7 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 11 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 22 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 28 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 33 ] = 1; this.numberOfUses++;
        useMatrix[ 9 ][ 34 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 9 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 15 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 21 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 23 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 10 ][ 25 ] = 1; this.numberOfUses++;
        useMatrix[ 11 ][ 7 ] = 1; this.numberOfUses++;
        useMatrix[ 11 ][ 9 ] = 1; this.numberOfUses++;
        useMatrix[ 11 ][ 19 ] = 1; this.numberOfUses++;
        useMatrix[ 11 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 11 ][ 32 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 2 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 10 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 15 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 25 ] = 1; this.numberOfUses++;
        useMatrix[ 12 ][ 35 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 1 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 3 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 16 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 22 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 25 ] = 1; this.numberOfUses++;
        useMatrix[ 13 ][ 33 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 2 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 8 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 23 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 14 ][ 31 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 2 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 6 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 13 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 16 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 23 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 15 ][ 38 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 10 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 11 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 13 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 16 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 30 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 35 ] = 1; this.numberOfUses++;
        useMatrix[ 16 ][ 37 ] = 1; this.numberOfUses++;
        useMatrix[ 17 ][ 6 ] = 1; this.numberOfUses++;
        useMatrix[ 17 ][ 16 ] = 1; this.numberOfUses++;
        useMatrix[ 17 ][ 22 ] = 1; this.numberOfUses++;
        useMatrix[ 17 ][ 24 ] = 1; this.numberOfUses++;
        useMatrix[ 17 ][ 35 ] = 1; this.numberOfUses++;
    
        System.out.println("The number of uses for Randomised design problem is: " + this.numberOfUses );
    }
    
    /**
     * get a reference to the use matrix
     * @return use matrix as 2D array of doubles
     */
    public int[ ][ ] getUseMatrix( )
    {
        assert useMatrix != null;
        return useMatrix;
    }
    
    /**
     * get number of couples
     * @return number of couples
     */
    public int getNumberOfUses( )
    {
//        assert numberOfUses > 0 : "use matrix not generated yet!!";
        
        return numberOfUses;
    }
    
    
    /**
     * get the size of the action list
     * @return size of action list
     */
    public int getActionListSize( )
    {
        assert this.actionList != null;
        return this.actionList.size( );
    }
    
    /**
     * get the size of the datum list
     * @return size of datum list
     */
    public int getDatumListSize( )
    {
        assert this.datumList != null;
        return this.datumList.size( );
    }
    
    /**
     * write the use matrix to file
     * @param design problem name
     * @param path provided by the File Chooser dialog
     */
    public void writeUseMatrixToFile( String path, String problemName )
    {
        assert path != null;
        assert path.length( ) > 0;
        assert useMatrix != null;
        
        String useMatrixFullName = 
                path + 
                "\\" + 
                problemName + 
                USE_MATRIX_FILE_NAME;

        // set up the output file
        PrintWriter out = null;
        
        try
        {
            out = new PrintWriter( useMatrixFullName );
        }
        catch( FileNotFoundException ex )
        {
            System.err.println( "Can't open file to write use matrix!!!" );
            return;
        }

        // first write out a header line
        out.println( "Actions x Data");
        out.print( actionList.size( ) + " " ); 
        out.println( datumList.size( ) );
        
        // them write use matrix values
        for( int i = 0; i < actionList.size(); i++ )
        {
            for( int j = 0; j < datumList.size( ); j++ )
            {
                out.print( useMatrix[ i ][ j ] );
                out.print( " " );
            }
            out.println( );
        }
        
        // and close up...
        out.close( );
    }
    
    
    /**
     * reload the use matrix from file
     * @param path of where use matrix file located
     */
     public void reloadUseMatrix( String path ) 
     { 
        assert path != null;
        String useMatrixFullFileName = 
               path + 
               "\\" + 
               "Randomised" + 
               USE_MATRIX_FILE_NAME;
         
        Scanner scanner = null;
        try 
        {
            scanner = new Scanner( new FileInputStream( useMatrixFullFileName ) );
        } 
        catch( FileNotFoundException ex) 
        {
            assert true: "reload use matrix file not found!"; 
        }
   
        int lineNumber = 0;
        
        while( scanner.hasNextLine( ) )
        {
            String string = scanner.nextLine( );
//            System.out.println( string );
            
            if( lineNumber == 0 )
            {
                // do nothing, just a header line
            }
            else if( lineNumber == 1 )  // this line gives number of actions, data
            {
                String[ ] strings = string.split( " " ); 
                this.numberOfActionsForReload = Integer.parseInt( strings[ 0 ] );
                this.numberOfDataForReload = Integer.parseInt( strings[ 1 ] );
//                System.out.println(
//                    "action are: " + numberOfActions + " " +
//                    "data are: " + numberOfData );      
                
                useMatrix = new int[ numberOfActionsForReload ][ numberOfDataForReload ];
                
                for( int i = 0; i < numberOfActionsForReload; i++ )
                {
                    for( int j = 0; j < numberOfDataForReload; j++ )
                    {
                        useMatrix[ i ][ j ] = 0;
                    }
                }
            }
            else // line by line, put the payload into the use matrix
            {
                String[ ] strings = string.split( " " ); 
                assert strings.length == numberOfDataForReload;
                
                for( int i = 0; i < numberOfDataForReload; i++ )
                {
                    if( strings[ i ].equals( "1" ) )
                    {
                        useMatrix[ lineNumber - 2 ][ i ] = 1;
                    }
                }
            }
            
            lineNumber++;
        }
      
        assert lineNumber == numberOfActionsForReload + 2;
        
        scanner.close();
        
//        for( int i = 0; i < numberOfActions; i++ )
//        {
//            for( int j = 0; j < numberOfData; j++ )
//            {
//                System.out.print( useMatrix[ i ][ j ] );
//                System.out.print( " " );
//            }
//            System.out.println("");
//        }
     }
      
     /**
      * having reloaded the use matrix from file,
      * transfer the values into the use table
      */
     public void reloadUseTable( ) 
     { 
         useTable = new TreeMap< String, List< CLSDatum > >( );
         
         int actionNumber = 0;
         
         for( CLSAction action : actionList )
         {
             List< CLSDatum > localList = new ArrayList< CLSDatum >( );
             
             for( int i = 0; i < numberOfDataForReload; i++ )
             {
                 if( useMatrix[ actionNumber ][ i ] == 1 )
                 {
                     CLSDatum d = datumList.get( i );
                     localList.add( d );
                 }
             }
             
             this.addEntryToUseTable( action.getName( ), localList );
             
             actionNumber++;
         }
         assert actionNumber == actionList.size( );
     }
      
     public int getCurrentProblemInstance( )
     {
         assert this.currentProblemInstance >= 0;
         assert this.currentProblemInstance <= Parameters.NUMBER_OF_PROBLEMS;
         return this.currentProblemInstance;
     }
     
}   // end of class

//------- end of file -------------------------------------