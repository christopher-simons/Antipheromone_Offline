/*
 * DesignProblem5CBS.java
 * Created on 04 November 2005, 11:34
 * Greatly refactored 28 September 2011
 */

package problem;

/**
 * Cinema Booking System
 * 
 * see http://www.cems.uwe.ac.uk/~clsimons/CaseStudies/CinemaBookingSystem.htm
 * 
 * @author Christopher Simons
 */

import java.util.List;
import java.util.ArrayList;


public class DesignProblem5CBS extends DesignProblem
{
    /** Creates a new instance of DesignProblem5CBS */
    public DesignProblem5CBS( ProblemController problemController )
    {
        super( problemController );
        
        a0 = new CLSAction( "RetrieveShowingsList", 0 );
        a1 = new CLSAction( "RetrieveFimDetails", 1 );
        a2 = new CLSAction( "CheckSeatAvailability", 2 );
        a3 = new CLSAction( "CalculateCost", 3 );
        a4 = new CLSAction( "CancelBooking", 4 );
        a5 = new CLSAction( "TransactPayment", 5 );
        a6 = new CLSAction( "LookUpPayment", 6 );
        a7 = new CLSAction( "PrintTickets", 7 );
        a8 = new CLSAction( "CreateFilm", 8 );
        a9 = new CLSAction( "RetrieveFilmList", 9 );
        a10 = new CLSAction( "DeleteFilm", 10 );
        a11 = new CLSAction( "CreateScreen", 11 );
        a12 = new CLSAction( "RetrieveScreenList", 12 );
        a13 = new CLSAction( "CreateShowing", 13 );
        a14 = new CLSAction( "CancelShowing", 14 );
        
        d0 = new CLSDatum( "DateTime", 0  );
        d1 = new CLSDatum( "ShowingNumber", 1 );
        d2 = new CLSDatum( "Title", 2 );
        d3 = new CLSDatum( "AgeRating", 3 );
        d4 = new CLSDatum( "Duration", 4 );
        d5 = new CLSDatum( "Trailer", 5 );
        d6 = new CLSDatum( "Seats", 6 );
        d7 = new CLSDatum( "BookingNumber", 7 );
        d8 = new CLSDatum( "Quantity", 8 );
        d9 = new CLSDatum( "TicketType", 9 );
        d10 = new CLSDatum( "Cost", 10 );
        d11 = new CLSDatum( "CardType", 11 );
        d12 = new CLSDatum( "CardNumber", 12 );
        d13 = new CLSDatum( "ExpiryDate", 13 );
        d14 = new CLSDatum( "ScreenNumber", 14 );
        d15 = new CLSDatum( "Capacity", 15 );
    }

    /** assemble design problem number 5 */
    public void assembleDesignProblem5( )
    {
//        System.out.println( "assembling design problem 5" );
        
        assemblePurchaseAdvanceTicketsUseCase( );
        assembleCollectTicketsUseCase( );
        assembleAddFilmUseCase( );
        assembleDeleteFilmUseCase( );
        assembleAddScreenUseCase( );
        assembleAddShowingUseCase( );
        assembleCancelShowingUseCase( );
            
//        pc.showActionsAndData( );
        
    }

    /** assemble the "PurchaseAdvanceTickets" use case */
    private void assemblePurchaseAdvanceTicketsUseCase( )
    {
        //  Actor requests to view Showings for a DateTime
        //  System provides list of Showings, including
        //      DateTime, ShowingNumber, Film Title
        assemblePATStep1( );
        
        //  Actor selects a Film Title
        //  System displays the Film Details, including
        //  Film Title, Age Rating, Duration, Short Description
        assemblePATStep2( );
        
        //  Actor specifies ticket type required (student, adult, child )
        //  Actor supplies the Quantity of Tickets required
        //  If insufficient seats available, System cancels Booking
        //  Otherwise, System calculates Cost 
        assemblePATStep3( );
        
        //  If Actor is not satisfied with Cost, System cancels Booking
        //  Otherwise, Actor supplies Payment Card details, including
        //      CardType, CardNumber, ExpiryDate
        //  System transacts Payment 
        assemblePATStep4( );
        
    }
        
    /** assemble step 1 of "Purchase Advance Tickets" */
    private void assemblePATStep1( )
    {
        //  Actor requests to view Showings for a DateTime
        //  System provides list of showings, including
        //      DateTime, Showing Number, Film Title
       
//        CLSAction a1 = new CLSAction( "RetrieveShowingsList", 0 );
        List< CLSDatum > d = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "DateTime") );
//        temp.add( new CLSDatum(  "ShowingNumber" ) );
//        temp.add( new CLSDatum(  "Title" ) );
        
        d.add( d0 );
        d.add( d1 );
        d.add( d2 );
        problemController.addActiontoProblemSpace( a0 );
        problemController.addDatumToProblemSpace( d );
        problemController.addEntryToUseTable( a0.getName( ), d );
    }
    
    /** assemble step 2 of "Purchase Advance Tickets" */
    private void assemblePATStep2( )
    {
        //  Actor selects a Film Title
        //  System displays the Film Details, including
        //  Film Title, Age Rating, Duration, Short Description
        
//        CLSAction a2 = new CLSAction( "RetrieveFilmDetails" , 0 );
        List< CLSDatum > d = new ArrayList< CLSDatum >( );
        
//        temp.add( new CLSDatum(  "Title" ) );
//        temp.add( new CLSDatum(  "AgeRating" ) );
//        temp.add( new CLSDatum(  "Duration" ) );
//        temp.add( new CLSDatum(  "Trailer" ) );
//        
        d.add( d2 );
        d.add( d3 );
        d.add( d4 );
        d.add( d5 );
        
        problemController.addActiontoProblemSpace( a1 );
        problemController.addDatumToProblemSpace( d );
        problemController.addEntryToUseTable( a1.getName( ), d );
    }
    
    /** assemble step 3 of "Purchase Advance Tickets" */
    private void assemblePATStep3( )
    {
        //  Actor specifies Ticket Type required (student, adult, child )
        //  Actor supplies Quantity of Tickets required
        //  If insufficient seats available, System cancels Booking
        //  Otherwise, System calculates Cost
        
//        CLSAction a3 = new CLSAction( "CheckSeatAvailability", 0 );
        List< CLSDatum > d = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "ShowingNumber" ) );
//        temp.add( new CLSDatum( "Seats" ) );

        d.add( d1 );
        d.add( d6 );

        problemController.addActiontoProblemSpace( a2 );
        problemController.addDatumToProblemSpace( d );
        problemController.addEntryToUseTable( a2.getName( ), d );
        
        
        
//        CLSAction a4 = new CLSAction( "CalculateCost", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "BookingNumber" ) );
//        temp.add( new CLSDatum( "Quantity" ) );
//        temp.add( new CLSDatum( "TicketType" ) );
//        temp.add( new CLSDatum( "Cost" ) );

        temp.add( d7 );
        temp.add( d8 );
        temp.add( d9 );
        temp.add( d10 );

        problemController.addActiontoProblemSpace( a3 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a3.getName( ), temp );
        
    }
    
    /** assemble step 4 of "Purchase Advance Tickets" */
    private void assemblePATStep4( )
    {
        //  If Actor is not satisfied with Cost, System cancels Booking
        //  Otherwise, Actor supplies Payment Card Details, including
        //      Card Type, Card number, Expiry Data
        //  System transacts Payment
        
//        CLSAction a5 = new CLSAction( "CancelBooking", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "BookingNumber" ) );
        temp.add( d7 );
        
        problemController.addActiontoProblemSpace( a4 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a4.getName( ), temp ); 
    
//        CLSAction a6 = new CLSAction( "TransactPayment", 0 );
        List< CLSDatum > temp2 = new ArrayList< CLSDatum >( );
//        temp2.add( new CLSDatum( "CardType" ) );
//        temp2.add( new CLSDatum( "CardNumber" ) );
//        temp2.add( new CLSDatum( "ExpiryDate" ) );
//        
        temp2.add( d11 );
        temp2.add( d12 );
        temp2.add( d13);
        
        problemController.addActiontoProblemSpace( a5 );
        problemController.addDatumToProblemSpace( temp2 );
        problemController.addEntryToUseTable( a5.getName( ), temp2 );
    }
    
    /** assemble the "Collect Tickets" Use Case */ 
    private void assembleCollectTicketsUseCase( )
    {
        //  Actor provides Payment Card Number
        //  System looks up Booking Details
        //  System prints tickets for Booking
        assembleCTStep1( );
        
    }
    
    /** assemble step 1 for "Collect Tickets" use case */
    private void assembleCTStep1( )
    {
        //  Actor provides payment Card Number
        //  System looks up Booking Details
        
//        CLSAction a = new CLSAction( "LookUpPayment", 0  );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "CardNumber" ) );
//        temp.add( new CLSDatum( "BookingNumber" ) );
        temp.add( d12 );
        temp.add( d7 );
        
        problemController.addActiontoProblemSpace( a6 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a6.getName( ), temp );

        //  System prints Tickets for Booking
//        CLSAction a2 = new CLSAction( "PrintTickets", 0 );
        List< CLSDatum > temp2 = new ArrayList< CLSDatum >( );
//        temp2.add( new CLSDatum( "BookingNumber" ) );
//        temp2.add( new CLSDatum( "Quantity" ) );
//        temp2.add( new CLSDatum( "TicketType" ) );
        temp2.add( d7 );
        temp2.add( d8 );
        temp2.add( d9 );
        
        problemController.addActiontoProblemSpace( a7 );
        problemController.addDatumToProblemSpace( temp2 );
        problemController.addEntryToUseTable( a7.getName( ), temp2 );       
    }

    /** assemble the "AddFilm" Use Case */
    private void assembleAddFilmUseCase( )
    {
        // Actor supplies Film Details, including
        //      Title, Age Rating, Duration, Trailer
        // System records the film details
        
//        CLSAction a1 = new CLSAction( "CreateFilm", 0 );
        List< CLSDatum > d1 = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "Title" ) );
//        temp.add( new CLSDatum( "AgeRating" ) );
//        temp.add( new CLSDatum( "Duration" ) );
//        temp.add( new CLSDatum( "Trailer" ) );
        d1.add( d2 );
        d1.add( d3 );
        d1.add( d4 );
        d1.add( d5 );
        
        problemController.addActiontoProblemSpace( a8 );
        problemController.addDatumToProblemSpace( d1 );
        problemController.addEntryToUseTable( a8.getName( ), d1 );
    }
    
    /** assemble the "Delete Film" use case */
    private void assembleDeleteFilmUseCase( )
    {
        //  Actor requests a list of current film titles
        //  System provides a list of all film titles
        assembleDFStep1( );
        
        //  Actor Provides a Film Title
        //  System deletes Film Details
        assembleDFStep2( );
    }
    
    /** assemble step 2 of "Delete Film" */
    private void assembleDFStep1( )
    {
        // Actor requests to delete a film
        // System provides a list of all film titles
        
//        CLSAction a1 = new CLSAction( "RetrieveFilmList", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "Title" ) );
        temp.add( d2 );
        
        problemController.addActiontoProblemSpace( a9 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a9.getName( ), temp );
    }
    
    private void assembleDFStep2( )
    {
        // Actor provides a Film Title
        // System deletes Film Details
        
//        CLSAction a2 = new CLSAction( "DeleteFilm", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "Title" ) );
//        temp.add( new CLSDatum( "AgeRating" ) );
//        temp.add( new CLSDatum( "Duration" ) );
//        temp.add( new CLSDatum( "Trailer" ) );
        temp.add( d2 );
        temp.add( d3 );
        temp.add( d4 );
        temp.add( d5 );
        
        problemController.addActiontoProblemSpace( a10 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a10.getName( ), temp );
    }
    
    
    /** assemble the "Add Screen" use case */
    private void assembleAddScreenUseCase( )
    {
        // Actor provides Screen Details, including
        //      Screen Number, Capacity
        // System creates Screen Details
        
//        CLSAction a1 = new CLSAction( "CreateScreen", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "ScreenNumber" ) );
//        temp.add( new CLSDatum( "Capacity" ) );
        temp.add( d14 );
        temp.add( d15 );
        
        problemController.addActiontoProblemSpace( a11 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a11.getName( ), temp );
    }
    
    /** assemble the "Add Showing" use case */
    private void assembleAddShowingUseCase( )
    {
        //  Actor requests to add a Ahowing
        //  System provides a list of all film Titles
        assembleASStep1( );
        
        //  System provides a list of all screens
        assembleASStep2( );
        
        //  Actor supplies Showing Details, including
        //      Date Time, Title, Screen Number     
        //  System creates Showing Details
        assembleASStep3( );
    }
    
    /** assemble step 1 for "Add Showing" use case */
    private void assembleASStep1( )
    {
        // Actor requests to add a showing of a film
        // System provides a list of all film titles
        
//        CLSAction a1 = new CLSAction( "RetrieveFilmList", 0 );
//        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "Title" ) );
//        
//        pc.addActiontoProblemSpace( a1 );
//        pc.addDatumToProblemSpace( temp );
//        pc.addEntryToUseTable( a1.getName( ), temp );
    }
    
    /** assemble step 2 for "Add Showing" use case */
    private void assembleASStep2( )
    {
        // System provides a list of all screens
        
//        CLSAction a3 = new CLSAction( "RetrieveScreenList", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "ScreenNumber" ) );
//        temp.add( new CLSDatum( "Capacity" ) );
        temp.add( d14 );
        temp.add( d15 );
        
        problemController.addActiontoProblemSpace( a12 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a12.getName( ), temp );
    }
    
    /** assemble step 3 for "Add Showing" use case */
    private void assembleASStep3( )
    {
        // Actor selects a screen
        
//        CLSAction a4 = new CLSAction( "CreateShowing", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "DateTime" ) );
//        temp.add( new CLSDatum( "Title" ) );
//        temp.add( new CLSDatum( "ScreenNumber" ) );
        temp.add( d0 );
        temp.add( d2 );
        temp.add( d14 );
        
        problemController.addActiontoProblemSpace( a13 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a13.getName( ), temp );
    }
    
      
    /** assemble the "Cancel Showing" use case */
    private void assembleCancelShowingUseCase( )
    {
        // Actor requests to cancel a showing
        // System provides a list of all showings
        assembleCSStep1( );
        
        // Actor selects a Showing Number
        // System cancels Showing
        assembleCSStep2( );
    }
   
    /** assemble step 1 of "Cancel Showing" use case */
    private void assembleCSStep1( )
    {
        // Actor requests to cancel a showing
        // System provides a list of all showings where no tickets have been sold
        
//        CLSAction a1 = new CLSAction( "RetrieveShowingsList", 0 );
//        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "ShowingNumber" ) );
//        temp.add( new CLSDatum( "DateTime" ) );
//        
//        pc.addActiontoProblemSpace( a1 );
//        pc.addDatumToProblemSpace( temp );
//        pc.addEntryToUseTable( a1.getName( ), temp );
    }
    
    /** assemble step 2 of "Cancel Showing" use case */
    private void assembleCSStep2( )
    {
        // Actor selects a showing
        // System cancels the showing
        
//        CLSAction a2 = new CLSAction( "CancelShowing", 0 );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( );
//        temp.add( new CLSDatum( "ShowingNumber" ) );
//        temp.add( new CLSDatum( "DateTime" ) );
        temp.add( d1 );
        temp.add( d0 );
        
        problemController.addActiontoProblemSpace( a14 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a14.getName( ), temp );        
    }
    
}   //--- end of class ------------------------------------


//------- end of file -------------------------------------
