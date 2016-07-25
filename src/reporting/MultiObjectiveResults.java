/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reporting;

import config.Parameters;
import engine.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author cl-simons
 */

public class MultiObjectiveResults 
{
    double[ ] CBOs;
    double[ ] NACs;
    double[ ] ATMRs;
    final int size; 
    
    DecimalFormat df;
    
    /** constructor */
    public MultiObjectiveResults( List< Path > colony )
    {
        assert colony != null;
        
        size = colony.size( ); 
        CBOs = new double[ size ];
        NACs = new double[ size ];
        ATMRs = new double[ size ];
        
        df = new DecimalFormat( "0.000" );
    }
    
    public void transfer( List< Path > colony )
    {
        assert colony != null;
        
        for( int i = 0; i < size; i++ )
        {
            Path p = colony.get( i );
            CBOs[ i ] = p.getCBO( );
            NACs[ i ] = p.getEleganceNAC( );
            ATMRs[ i ] = p.getEleganceATMR( );
        }
    }
    
    public void writeToFile( )
    {
        String moResultsFullName =  
            Parameters.outputFilePath + "\\" + "moResults.dat";
        boolean append = true;
        
        PrintWriter out = null;
        try
        {
            out = new PrintWriter( new FileWriter( new File( moResultsFullName ), append ) );
            
        }
        catch( IOException ex )
        {
            System.err.println( "Cant open file!" );
            return;
        }   
        
        for( int i = 0; i < size; i++ )
        {
            out.print( df.format( CBOs[ i ] ) );
            out.print( " " );
            out.print( df.format( NACs[ i ] ) );
            out.print( " " );
            out.println( ATMRs[ i ] );
        }
    
        out.close( );
    }
}
