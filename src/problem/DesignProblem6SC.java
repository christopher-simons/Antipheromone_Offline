/*
 * DesignProblem6SC.java
 *
 * Created on 09 May 2006, 14:38
 * Greatly refactored 28 September 2011
 */

package problem;

/** 
 * The Select Cruises design problem
 * 
 * see http://www.cems.uwe.ac.uk/~clsimons/CaseStudies/SelectCruises.htm
 * 
 * @author Christopher Simons
 */

import java.util.List;
import java.util.ArrayList;


public class DesignProblem6SC extends DesignProblem
{
    
            
    /** Creates a new instance of DesignProblem6SC */
    public DesignProblem6SC( ProblemController problemController )
    {
        super( problemController );
        
        a0 = new CLSAction( "ConfirmBooking", 0 );
        a1 = new CLSAction( "RequestMailing", 1 );
        a2 = new CLSAction( "RetrieveMailingList", 2 );
        a3 = new CLSAction( "PrintLetter", 3 );
        a4 = new CLSAction( "RecordMailingDate", 4 );
        a5 = new CLSAction( "LocateQuote", 5 );
        a6 = new CLSAction( "VerifyReservation", 6 );
        a7 = new CLSAction( "VerifyCreditCardDetails", 7 );
        a8 = new CLSAction( "TransactOnline", 8 );
        a9 = new CLSAction( "RetrieveAuthorisation", 9 );
        a10 = new CLSAction( "RetrieveCruiseList", 10 );
        a11 = new CLSAction( "RetrievePassageList", 11 );
        a12 = new CLSAction( "RetrieveBerthDetails", 12 );
        a13 = new CLSAction( "AllocateBerthToCustomer", 13 );
        a14 = new CLSAction( "RetrieveCruiseCost", 14 );
        a15 = new CLSAction( "RecordQuote", 15 );
        a16 = new CLSAction( "RequestQuoteMailing", 16 );
        a17 = new CLSAction( "LocateCustomerRecord", 17 );
        a18 = new CLSAction( "RecordPersonalDetails", 18 );
        a19 = new CLSAction( "RecordCruisePreferences", 19 );       
        a20 = new CLSAction( "SendBatchMailing", 20 );
        a21 = new CLSAction( "RecordShipDetails", 21 );
        a22 = new CLSAction( "RemoveShipDetails", 22 );
        a23 = new CLSAction( "RecordBerthDetails", 23 );
        a24 = new CLSAction( "RemoveBerthDetails", 24 );
        a25 = new CLSAction( "RecordCruiseDetails", 25 );
        a26 = new CLSAction( "RemoveCruiseDetails", 26 );
        a27 = new CLSAction( "RecordPassageDetails", 27 );
        a28 = new CLSAction( "RemovePassageDetails", 28 );
        
        d0 = new CLSDatum( "BookingNumber", 0 );
        d1 = new CLSDatum( "VoyageDate", 1 );
        d2 = new CLSDatum( "Cost", 2 );
        d3 = new CLSDatum( "MailRequestNumber", 3 );
        d4 = new CLSDatum( "DateMailRequenested", 4 );
        d5 = new CLSDatum( "QuoteNumber", 5 );
        d6 = new CLSDatum( "NumberOfBerths", 6 );
        d7 = new CLSDatum( "TotalCost", 7 );
        d8 = new CLSDatum( "CruiseNumber", 8 );
        d9 = new CLSDatum( "CardType", 9 );
        d10 = new CLSDatum( "CardHolderName", 10 );
        d11 = new CLSDatum( "IssuingOrganisation", 11 );
        d12 = new CLSDatum( "CardNumber", 12 );
        d13 = new CLSDatum( "ValidFrom", 13 );
        d14 = new CLSDatum( "ExpiresEnd", 14 );
        d15 = new CLSDatum( "DateTransacted", 15 );
        d16 = new CLSDatum( "InvoiceNumber", 16 );
        d17 = new CLSDatum( "DateBalancePaid", 17 );
        d18 = new CLSDatum( "ccAuthorisationCode", 18 );
        d19 = new CLSDatum( "Ocean", 19 );
        d20 = new CLSDatum( "Season", 20 );        
        d21 = new CLSDatum( "FromPortCruise", 21 );        
        d22 = new CLSDatum( "ToPortCruise", 22 );        
        d23 = new CLSDatum( "CruiseDescription", 23 );        
        d24 = new CLSDatum( "PassageNumber", 24 );        
        d25 = new CLSDatum( "FromPortPassage", 25 );        
        d26 = new CLSDatum( "ToPortPassage", 26 );        
        d27 = new CLSDatum( "NumberNights", 27 );        
        d28 = new CLSDatum( "JoinDate", 28 );       
        d29 = new CLSDatum( "FinishDate", 29 );        
        d30 = new CLSDatum( "PassageCost", 30 );        
        d31 = new CLSDatum( "PassageDescription", 31 );        
        d32 = new CLSDatum( "BerthNumber", 32 );        
        d33 = new CLSDatum( "BerthType", 33 );        
        d34 = new CLSDatum( "BerthDescription", 34 );        
        d35 = new CLSDatum( "MembershipNumber", 35 );        
        d36 = new CLSDatum( "DateBerthReserved", 36 );        
        d37 = new CLSDatum( "DateBerthBooked", 37 );        
        d38 = new CLSDatum( "BookingDate", 38 );        
        d39 = new CLSDatum( "Name", 39 );
        d40 = new CLSDatum( "Address", 40 );
        d41 = new CLSDatum( "DayTelephone", 41 );
        d42 = new CLSDatum( "EveningTelephone", 42 );
        d43 = new CLSDatum( "Nationality", 43 );
        d44 = new CLSDatum( "DateOfBirth", 44 );
        d45 = new CLSDatum( "Sex", 45 );
        d46 = new CLSDatum( "Height", 46 );
        d47 = new CLSDatum( "Weight", 47 );
        d48 = new CLSDatum( "DietaryRequirements", 48 );
        d49 = new CLSDatum( "SailingAbility", 49 );
        d50 = new CLSDatum( "PreferredOcean", 50 );
        d51 = new CLSDatum( "PreferredPassageNumber", 51 );
        d52 = new CLSDatum( "PreferredSeason", 52 );
        d53 = new CLSDatum( "PreferredShipNumber", 53 );
        d54 = new CLSDatum( "PreferredBerthType", 54 );
        d55 = new CLSDatum( "NumberOfRequests", 55 );
        d56 = new CLSDatum( "currentRequest", 56 );
        d57 = new CLSDatum( "DateBatchMailingRequested", 57 );
        d58 = new CLSDatum( "DateBatchMailingPrinted", 58 );
        d59 = new CLSDatum( "ShipNumber", 59 );
        d60 = new CLSDatum( "ShipName", 60 );
        d61 = new CLSDatum( "ShipDescription", 61 );
    }
    
    public void assembleDesignProblem6( )
    {
//        System.out.println( "Assembling design problem 6" );
        
        assembleMakeBooking( );
        assembleSendMailing( );
        assembleFindQuote( );
        assembleMakePayment( );
        assembleFindCruise( );
        assembleMakeInquiry( );
        assembleFindCustomerDetails( );
        assembleSendAllMailings( );
        assembleMaintainFleetInformation( );
        assembleMaintainCruiseInformation( );
        
//        problemController.showActionsAndData( );
    }

    private void assembleMakeBooking( ) 
    { 
//      INCLUDE "Find Quote"
//    
//      Actor confirms reservation
//    
//      INCLUDE "Make Payment"
//    
//      Actor confirms Bookings are paid
//      System records Bookings, including
//          BookingNumber, VoyageDate, Cost
//      System requests confirmation mailing
//    
//      Alternate courses:
//      - Payment refused
//      - Quote not found or invalid
//    
        //  Step 1
        //  Actor confirms Bookings are paid
        //  System records Bookings
        assembleMBStep1( );
        
        
        //  Step 2
        //  System requests confirmation mailing
        assembleMBStep2( );
    }
    
    private void assembleMBStep1( ) 
    { 
        //  Actor confirms Bookings are paid
        //  System records Bookings
        
//        CLSAction a1 = new CLSAction( "confirmBooking", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "VoyageDate", TEMP ) );
//        temp.add( new CLSDatum(  "Cost", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d1 );
        temp.add( this.d2 );
        
        problemController.addActiontoProblemSpace( this.a0 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a0.getName( ), temp );
    }
    
    private void assembleMBStep2( ) 
    { 
        //  System requests confirmation mailing
        
//        CLSAction a1 = new CLSAction( "requestMailing", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "MailRequestNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateMailRequested", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d3 );
        temp.add( this.d4 );
        
        problemController.addActiontoProblemSpace( a1 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a1.getName( ), temp );
    }
    
    
    private void assembleSendMailing( ) 
    { 
//      Actor requests list of Mailings
//      System presents list of Mailings, including
//          MailRequest, DateRequested
//    
//      Actor selects mailing(s) to send
//      System prints appropriate letter(s) to customer
//      System records Date of mailing

        //  step 1
        //  Actor requests list of Mailings
        //  System presents list of Mailings, including
        //      MailRequest, DateRequested
        assembleSMStep1( );
        
        //  Step 2
        //  System prints appropriate letter(s) to customer
        assembleSMStep2( );
        
        //  System records Date of mailing
        assembleSMStep3( );
    }
    
    public void assembleSMStep1( ) 
    { 
        //  Actor requests list of Mailings
        //  System presents list of Mailings, including
        //      MailRequest, DateRequested
       
//        CLSAction a1 = new CLSAction( "retrieveMailingList", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MailRequestNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateMailRequested", TEMP ) );
        temp.add( this.d3 );
        temp.add( this.d4 );
        
        problemController.addActiontoProblemSpace( this.a2 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a2.getName( ), temp );
    }
    
    public void assembleSMStep2( ) 
    { 
        //  System prints appropriate letter(s) to customer
        
//        CLSAction a1 = new CLSAction( "printLetter", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MailRequestNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateMailRequested", TEMP ) );
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
        temp.add( this.d3 );
        temp.add( this.d4 );
        temp.add( this.d0 );
        
        problemController.addActiontoProblemSpace( this.a3 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a3.getName( ), temp );
    }
    
    public void assembleSMStep3 ( ) 
    { 
        //  System records Date of mailing
        
//        CLSAction a1 = new CLSAction( "recordMailingDate", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MailRequestNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateMailRequested", TEMP ) );
        temp.add( this.d3 );
        temp.add( this.d4 );
        
        problemController.addActiontoProblemSpace( this.a4 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a4.getName( ), temp );
    }
    
    
    private void assembleFindQuote( )  
    { 
//      Actor enters Quote Reference
//      System locates Quote Record
//    
//      Actor confirms Quote
//      System verifies reservations still available
//    
//      Alternate Course:
//      - Quote not found
    
        //  Step 1
        //  Actor enters Quote Reference
        //  System locates Quote Record
        assembleFQStep1( );
        
        //  Step 2
        //  Actor confirms Quote
        //  System verifies reservations still available
        assembleFQStep2( );
        
    }
    
    private void assembleFQStep1( ) 
    { 
        //  Actor enters Quote Reference
        //  System locates Quote Record
        
//        CLSAction a1 = new CLSAction( "locateQuote", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "QuoteNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "NumberOfBerths", TEMP ) );
//        temp.add( new CLSDatum(  "TotalCost", TEMP ) );
        temp.add( this.d5 );
        temp.add( this.d0 );
        temp.add( this.d6 );
        temp.add( this.d7 );
        
        problemController.addActiontoProblemSpace( this.a5 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a5.getName( ), temp );
    }
    
    private void assembleFQStep2( ) 
    { 
        //  Actor confirms Quote
        //  System verifies reservations still available
        
//        CLSAction a1 = new CLSAction( "verifyReservation", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum(  "NumberOfBerths", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d8 );
        temp.add( this.d6 );
        
        problemController.addActiontoProblemSpace( this.a6 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a6.getName( ), temp );
    }
    
       
    private void assembleMakePayment( ) 
    { 
//         Actor enters Credit Card Details, including
//              CardType, CardHolderName, CardNumber, ExpiresEnd
//         System verifies entered Credit Card Details, including
//              CardType, CardHolderName, IssuingOrganisation,
//              CardNumber, ValidFrom, ExpiresEnd
//        
//         Actor confirms Total Cost of Payment Amount
//         System uses On-Line Authorisation Service, recording
//              TotalCost, DateTransacted, InvoiceNumber,
//              BookingNumber, DateBalancePaid, ccAuthorisationCode
//        
//         System displays result of authorisation for the Invoice Number
//         including TotalCost, DateTransacted, InvoiceNumber,
//                   BookingNumber, DateBalancePaid, ccAuthorisationCode
//         Actor confirms result

        assembleMPStep1( );
        assembleMPStep2( );
        assembleMPStep3( );
    }
    
    private void assembleMPStep1( ) 
    { 
        // Actor enters Credit Card Details, including
        //      CardType, CardHolderName, CardNumber, ExpiresEnd
        // System verifies entered Credit Card Details, including
        //      CardType, CardHolderName, IssuingOrganisation,
        //      CardNumber, ValidFrom, ExpiresEnd
        
//        CLSAction a1 = new CLSAction( "verifyCreditCardDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "CardType", TEMP ) );
//        temp.add( new CLSDatum(  "CardHolderName", TEMP ) );
//        temp.add( new CLSDatum(  "IssuingOrganisation", TEMP ) );
//        temp.add( new CLSDatum(  "CardNumber", TEMP  ) );
//        temp.add( new CLSDatum(  "ValidFrom", TEMP ) );
//        temp.add( new CLSDatum(  "ExpiresEnd", TEMP ) );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        temp.add( this.d13 );
        temp.add( this.d14 );
        
        problemController.addActiontoProblemSpace( this.a7 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a7.getName( ), temp );
    }
    
    private void assembleMPStep2( ) 
    { 
        // Actor confirms Total Cost of Payment Amount
        // System uses On-Line Authorisation Service, recording
        //      TotalCost, DateTransacted, InvoiceNumber,
        //      BookingNumber, DateBalancePaid, ccAuthorisationCode
        
//        CLSAction a1 = new CLSAction( "transactOnLine", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "TotalCost", TEMP ) );
//        temp.add( new CLSDatum(  "DateTransacted", TEMP ) );
//        temp.add( new CLSDatum(  "InvoiceNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateBalancePaid", TEMP ) );
//        temp.add( new CLSDatum(  "ccAuthorisationCode", TEMP ) );
        temp.add( this.d7 );
        temp.add( this.d15 );
        temp.add( this.d16 );
        temp.add( this.d0 );
        temp.add( this.d17 );
        temp.add( this.d18 );
        
        problemController.addActiontoProblemSpace( this.a8 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a8.getName( ), temp );
    }
    
    private void assembleMPStep3( ) 
    { 
        // System displays result of authorisation for the Invoice Number
        // including TotalCost, DateTransacted, InvoiceNumber,
        //           BookingNumber, DateBalancePaid, ccAuthorisationCode
        // Actor confirms result
        
//        CLSAction a1 = new CLSAction( "retrieveAuthorisation", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "TotalCost", TEMP ) );
//        temp.add( new CLSDatum(  "DateTransacted", TEMP ) );
//        temp.add( new CLSDatum(  "InvoiceNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateBalancePaid", TEMP ) );
//        temp.add( new CLSDatum(  "ccAuthorisationCode", TEMP  ) );
        temp.add( this.d7 );
        temp.add( this.d15 );
        temp.add( this.d16 );
        temp.add( this.d0 );
        temp.add( this.d17 );
        temp.add( this.d18 );
        
        problemController.addActiontoProblemSpace( this.a9 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a9.getName( ), temp );
    }
    
    
    private void assembleFindCruise( ) 
    { 
//         Actor requests to find a Cruise
//         System presents choices of Cruise Details, including
//              CruiseNumber, Ocean, Season, 
//              fromPort, toPort, Description
//        
//         Actor selects a CruiseNumber
//         System presents Passage Details for selected Cruise, including
//              PassageNumber, FromPort, toPort, 
//              NumberNights, JoinDate, FinishDate, 
//              Cost, Description
//        
//         Actor selects PassageNumber
//         System presents Berth Details on Passage, including
//              BerthNumber, BerthType, BerthDescription
//        
//         Actor selects BerthNumber
//         System allocates Berth to Customer MembershipNumber, recording
//              MembershipNumber, DateReserved, DateBooked
//        
//        System displays Costs of Cruise for BookingNumber, including
//              BookingNumber, BookingDate, CruiseNumber,
//              VoyageDate, Cost
//        Actor confirms selection    
        
        assembleFCStep1( );
        assembleFCStep2( );
        assembleFCStep3( );
        assembleFCStep4( );
        assembleFCStep5( );
    }
    
    private void assembleFCStep1( )
    {
        // Actor request to find a Cruise
        // System presents choices of Cruise Details, including
        //      Ocean, Season, FromPort, ToPort, Description
        
//        CLSAction a1 = new CLSAction( "retrieveCruiseList", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Ocean", TEMP ) );
//        temp.add( new CLSDatum(  "Season", TEMP ) );
//        temp.add( new CLSDatum(  "FromPortCruise", TEMP ) );
//        temp.add( new CLSDatum(  "ToPortCruise", TEMP ) );
//        temp.add( new CLSDatum(  "CruiseDescription", TEMP ) );
        temp.add( this.d8 );
        temp.add( this.d19 );
        temp.add( this.d20 );
        temp.add( this.d21 );
        temp.add( this.d22 );
        temp.add( this.d23 );
        
        problemController.addActiontoProblemSpace( this.a10 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a10.getName( ), temp );
    }
    
    private void assembleFCStep2( )
    {
        // Actor selects a CruiseNumber
        // System presents Passage Details for selected Cruise, including
        //      PassageNumber, FromPort, FoPort, 
        //      NumberNights, JoinDate, FinishDate, 
        //      Cost, Description
        
//        CLSAction a1 = new CLSAction( "retrievePassageList", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "PassageNumber", TEMP ) );
//        temp.add( new CLSDatum(  "FromPortPassage", TEMP ) );
//        temp.add( new CLSDatum(  "ToPortPassage", TEMP ) );
//        temp.add( new CLSDatum(  "NumberNights", TEMP ) );
//        temp.add( new CLSDatum(  "JoinDate", TEMP ) );
//        temp.add( new CLSDatum(  "FinishDate", TEMP ) );
//        temp.add( new CLSDatum(  "PassageCost", TEMP ) );
//        temp.add( new CLSDatum(  "PassageDescription", TEMP ) );
        temp.add( this.d24 );
        temp.add( this.d25 );
        temp.add( this.d26 );
        temp.add( this.d27 );
        temp.add( this.d28 );
        temp.add( this.d29 );
        temp.add( this.d30 );
        temp.add( this.d31 );
        
        problemController.addActiontoProblemSpace( this.a11 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a11.getName( ), temp );
    }
    
    private void assembleFCStep3( )
    {
        // Actor selects PassageNumber
        // System presents Berth Details on Passage, including
        //      BerthNumber, BerthType, BerthDescription
        
//        CLSAction a1 = new CLSAction( "retrieveBerthDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BerthNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BerthType", TEMP ) );
//        temp.add( new CLSDatum(  "BerthDescription", TEMP ) );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        
        problemController.addActiontoProblemSpace( this.a12 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a12.getName( ), temp );
    }
    
    private void assembleFCStep4( )
    {
        // Actor selects BerthNumber
        // System allocates Berth to Customer MembershipNumber, recording
        //      MembershipNumber, DateReserved, DateBooked
        
//        CLSAction a1 = new CLSAction( "allocateBerthToCustomer", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MembershipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BerthNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateBerthReserved", TEMP ) );
//        temp.add( new CLSDatum(  "DateBerthBooked", TEMP ) );
        temp.add( this.d35 );
        temp.add( this.d32 );
        temp.add( this.d36 );
        temp.add( this.d37 );
        
        problemController.addActiontoProblemSpace( this.a13 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a13.getName( ), temp );
    }
    
    private void assembleFCStep5( ) 
    {
        // System displays Costs of Cruise for BookingNumber, including
        //      BookingNumber, BookingDate, CruiseNumber,
        //      VoyageDate, Cost
        // Actor confirms selection    
        
//        CLSAction a1 = new CLSAction( "retrieveCruiseCost", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BookingDate", TEMP ) );
//        temp.add( new CLSDatum(  "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum(  "VoyageDate", TEMP ) );
//        temp.add( new CLSDatum(  "Cost", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d38 );
        temp.add( this.d8 );
        temp.add( this.d1 );
        temp.add( this.d2 );
        
        problemController.addActiontoProblemSpace( this.a14 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a14.getName( ), temp );
    }
    
    
    private void assembleMakeInquiry( ) 
    { 
//         INCLUDE "Find Customer Details"
//        
//         INCLUDE "Find Cruise"
//        
//         Actor requests a Quote for BookingNumber
//         System records Quote and displays 
//              QuoteNumber, BookingNumber, NumberOfBerths,
//              TotalCost
//        
//         Actor confirms Quote Number
//         System requests Quote Mailing, recording
//              DateMailingRequested
        
        assembleMIStep1( );
        assembleMIStep2( );
    }
    
    private void assembleMIStep1( ) 
    {
        // Actor requests a Quote for BookingNumber
        // System records Quote and displays 
        //      QuoteNumber, BookingNumber, NumberOfBerths,
        //      TotalCost
        
//        CLSAction a1 = new CLSAction( "recordQuote", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "QuoteNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BookingNumber", TEMP ) );
//        temp.add( new CLSDatum(  "NumberOfBerths", TEMP ) );
//        temp.add( new CLSDatum(  "TotalCost", TEMP ) );
        temp.add( this.d5 );
        temp.add( this.d0 );
        temp.add( this.d6 );
        temp.add( this.d7 );
        
        
        problemController.addActiontoProblemSpace( this.a15 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a15.getName( ), temp );
    }
    
    private void assembleMIStep2  ( ) 
    {
        // Actor confirms Quote Number
        // System requests Quote Mailing, recording
        //      DateMailingRequested
        
//        CLSAction a1 = new CLSAction( "requestQuoteMailing", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MailRequestNumber", TEMP ) );
//        temp.add( new CLSDatum(  "DateMailRequested", TEMP ) );
        temp.add( this.d3 );
        temp.add( this.d4 );
        
        problemController.addActiontoProblemSpace( this.a16 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a16.getName( ), temp );
    }
    
    
    private void assembleFindCustomerDetails( ) 
    { 
//        Actor chooses new or existing Customer options
//        
//        If the Customer is existing
//          Actor enters Membership Number
//          System locates Customer Record and displays details, including
//              Name, Address, DayTelephone,
//              EveningTelephone, Nationality, DateOfBirth,
//              Sex, Height, Weight, 
//              DietaryRequirements, SailingAbility
//          Actor confirms the Details
//        
//        If the Customer is new
//          Actor enters Personal Details (Name, Address etc.)
//          System displays Cruise Choices
//          Actor selects Cruise Preferences, including
//              Ocean, PassageNumber, season, 
//              ShipNumber, BerthType
//          Actor confirms Details
        
        assembleFCustStep1( );
        assembleFCustStep2( );
        assembleFCustStep3( );
        assembleFCustStep4( );
    }
    
    private void assembleFCustStep1( )
    {
        //If the Customer is existing
        //  Actor enters Membership Number
        //  System locates Customer Record and displays details, including
        //      Name, Address, DayTelephone,
        //      EveningTelephone, Nationality, DateOfBirth,
        //      Sex, Height, Weight, 
        //      DietaryRequirements, SailingAbility
        //  Actor confirms the Details
        
//        CLSAction a1 = new CLSAction( "locateCustomerRecord", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MembershipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Name", TEMP ) );
//        temp.add( new CLSDatum(  "Address", TEMP ) );
//        temp.add( new CLSDatum(  "DayTelephone", TEMP ) );
//        temp.add( new CLSDatum(  "EveningTelephone", TEMP ) );
//        temp.add( new CLSDatum(  "Nationality", TEMP ) );
//        temp.add( new CLSDatum(  "DateOfBirth", TEMP ) );
//        temp.add( new CLSDatum(  "Sex", TEMP ) );
//        temp.add( new CLSDatum(  "Height", TEMP ) );
//        temp.add( new CLSDatum(  "Weight", TEMP ) );
//        temp.add( new CLSDatum(  "DietaryRequirements", TEMP ) );
//        temp.add( new CLSDatum(  "SailingAbility", TEMP ) );
        temp.add( this.d35 );
        temp.add( this.d39 );
        temp.add( this.d40 );
        temp.add( this.d41 );
        temp.add( this.d42 );
        temp.add( this.d43 );
        temp.add( this.d44 );
        temp.add( this.d45 );
        temp.add( this.d46 );
        temp.add( this.d47 );
        temp.add( this.d48 );
        temp.add( this.d49 );
        
        problemController.addActiontoProblemSpace( this.a17 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a17.getName( ), temp );
    }
     
    private void assembleFCustStep2( )
    {
        //If the Customer is new
        //  Actor enters Personal Details, including
        //      Name, Address, DayTelephone,
        //      EveningTelephone, Nationality, DateOfBirth,
        //      Sex, Height, Weight, 
        //      DietaryRequirements, SailingAbility
        //  System allocates Membership Number 
        //  System records Personal Details
        
//        CLSAction a1 = new CLSAction( "recordPersonalDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MembershipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Name", TEMP ) );
//        temp.add( new CLSDatum(  "Address", TEMP ) );
//        temp.add( new CLSDatum(  "DayTelephone", TEMP ) );
//        temp.add( new CLSDatum(  "EveningTelephone", TEMP ) );
//        temp.add( new CLSDatum(  "Nationality", TEMP ) );
//        temp.add( new CLSDatum(  "DateOfBirth", TEMP ) );
//        temp.add( new CLSDatum(  "Sex", TEMP ) );
//        temp.add( new CLSDatum(  "Height", TEMP ) );
//        temp.add( new CLSDatum(  "Weight", TEMP ) );
//        temp.add( new CLSDatum(  "DietaryRequirements", TEMP ) );
//        temp.add( new CLSDatum(  "SailingAbility" , TEMP) );
        temp.add( this.d35 );
        temp.add( this.d39 );
        temp.add( this.d40 );
        temp.add( this.d41 );
        temp.add( this.d42 );
        temp.add( this.d43 );
        temp.add( this.d44 );
        temp.add( this.d45 );
        temp.add( this.d46 );
        temp.add( this.d47 );
        temp.add( this.d48 );
        temp.add( this.d49 );
        
        problemController.addActiontoProblemSpace( this.a18 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a18.getName( ), temp );
    }
     
    private void assembleFCustStep3( )
    {
        //  System displays Cruise Choices
        // see FC step 2
    }
        
    private void assembleFCustStep4( )
    {
        //  Actor selects Cruise Preferences, including
        //      Ocean, PassageNumber, season, 
        //      ShipNumber, BerthType
        //  Actor confirms Details
        
//        CLSAction a1 = new CLSAction( "recordCruisePreferences", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "MembershipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "PreferredOcean", TEMP ) );
//        temp.add( new CLSDatum(  "PreferredPassageNumber", TEMP )  );
//        temp.add( new CLSDatum(  "PreferredSeason", TEMP ) );
//        temp.add( new CLSDatum(  "PreferredShipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "PreferredBerthType", TEMP ) );
        temp.add( this.d35 );
        temp.add( this.d50 );
        temp.add( this.d51 );
        temp.add( this.d52 );
        temp.add( this.d53 );
        temp.add( this.d54 );
        
        problemController.addActiontoProblemSpace( this.a19 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a19.getName( ), temp );
    }
   
    
    private void assembleSendAllMailings( ) 
    { 
//        Actor requests to send all Mailings
//        System retrieves list of Mailings
//        System prints appropriate letter to each Customer
//        System records date of Mailing for each Letter Printed  
//        
        assembleSAStep1( );
    }
        
    private void assembleSAStep1( ) 
    {
        // Actor requests to send all Mailings
        // System retrieves list of Mailings 
        //System prints appropriate letter to each Customer
        //System records date of Mailing for each Letter Printed  
        
//        CLSAction a1 = new CLSAction( "sendBatchMailing", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "NumberOfRequests", TEMP) );
//        temp.add( new CLSDatum(  "CurrentRequest", TEMP ) );
//        temp.add( new CLSDatum(  "DateBatchMailingRequested", TEMP ) );
//        temp.add( new CLSDatum(  "DateBatchMailingPrinted", TEMP ) );
        temp.add( this.d55 );
        temp.add( this.d56 );
        temp.add( this.d57 );
        temp.add( this.d58 );
        
        problemController.addActiontoProblemSpace( this.a20 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a20.getName( ), temp );
    }
     
    private void assembleMaintainFleetInformation( )
    {
//        Actor chooses a Fleet maintenance option:
//        
//        If the option is create ship
//          Actor supplies Ship details, including
//            ShipName, ShipDescription, NumberOfBerths
//          System allocates Ship Number
//          System records Ship Details
//        
//        If the option is delete ship
//          Actor supplies Ship Number
//          System removes record of Ship Details
//        
//        If the option is add berth to ship
//          Actor supplies Berth details, including
//            ShipNumber, BerthType, BerthDescription
//          System allocates Berth Number
//          System records Berth Details
//        
//        If the option is delete berth from ship
//          Actor supplies Berth Number
//          System removes record of Berth Details       
        
        assembleMFStep1( );
        assembleMFStep2( );
        assembleMFStep3( );
        assembleMFStep4( );
    }
    
    private void assembleMFStep1( )
    {
        //If the option is create ship
        //  Actor supplies Ship details, including
        //    ShipName, ShipDescription, NumberOfBerths
        //  System allocates Ship Number
        //  System records Ship Details
        
//        CLSAction a1 = new CLSAction( "recordShipDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "ShipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "ShipName", TEMP ) );
//        temp.add( new CLSDatum(  "ShipDescription", TEMP ) );
//        temp.add( new CLSDatum(  "NumberOfBerths", TEMP ) );
        temp.add( this.d59 );
        temp.add( this.d60 );
        temp.add( this.d61 );
        temp.add( this.d6 );
        
        problemController.addActiontoProblemSpace( this.a21 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a21.getName( ), temp ); 
    }
    
    private void assembleMFStep2( )
    {
        //If the option is delete ship
        //  Actor supplies Ship Number
        //  System removes record of Ship Details
        
//        CLSAction a1 = new CLSAction( "removeShipDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "ShipNumber", TEMP ) );
//        temp.add( new CLSDatum(  "ShipName", TEMP ) );
//        temp.add( new CLSDatum(  "NumberOfBerths", TEMP ) );
//        temp.add( new CLSDatum(  "ShipDescription", TEMP ) );
        temp.add( this.d59 );
        temp.add( this.d60 );
        temp.add( this.d6 );
        temp.add( this.d61 );
        
        problemController.addActiontoProblemSpace( this.a22 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a22.getName( ), temp );
    }
    
    private void assembleMFStep3( )
    {
        //If the option is add berth to ship
        //  Actor supplies Berth details, including
        //    ShipNumber, BerthType, BerthDescription
        //  System allocates Berth Number
        //  System records Berth Details
        
//        CLSAction a1 = new CLSAction( "recordBerthDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "BerthNumber", TEMP ) );
//        temp.add( new CLSDatum(  "BerthType", TEMP ) );
//        temp.add( new CLSDatum(  "BerthDescription", TEMP ) );
//        temp.add( new CLSDatum(  "ShipNumber", TEMP ) );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        temp.add( this.d59 );
        
        problemController.addActiontoProblemSpace( this.a23 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a23.getName( ), temp ); 
    }
    
    private void assembleMFStep4( )
    {
       //If the option is delete berth from ship
        //  Actor supplies Berth Number
        //  System removes record of Berth Details       
        
//        CLSAction a1 = new CLSAction( "removeBerthDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "BerthNumber", TEMP ) );
//        temp.add( new CLSDatum( "BerthType", TEMP ) );
//        temp.add( new CLSDatum( "BerthDescription", TEMP ) );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        
        problemController.addActiontoProblemSpace( this.a24 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a24.getName( ), temp ); 
    }
    
    
    private void assembleMaintainCruiseInformation( )
    {
//        Actor chooses a Cruise maintenance option:
//        
//        If the option is create cruise
//          Actor supplies Cruise details, including
//            Ocean, Season, FromPortCruise, 
//            ToPortCruise, CruiseDescription
//          System allocates CruiseNumber
//          System records Cruise Details
//        
//        If the option is delete cruise
//          Actor supplies Cruise Number
//          System removes record of Cruise Details
//        
//        If the option is add passage to cruise
//          Actor supplies Passage details, including
//            CruiseNumber, FromPortPassage, ToPortPassage,
//            NumberNights, JoinDate, Cost, Description
//          System allocates Passage Number
//          System records Passage Details
//        
//        If the option is delete passage from cruise
//          Actor supplies Passage Number
//          System removes record of Passage Details        
        
        assembleMCStep1( );
        assembleMCStep2( );
        assembleMCStep3( );
        assembleMCStep4( );
    }

    private void assembleMCStep1( )
    {
        //If the option is create cruise
        //  Actor supplies Cruise details, including
        //    Ocean, Season, FromPortCruise, 
        //    ToPortCruise, CruiseDescription
        //  System allocates CruiseNumber
        //  System records Cruise Details
        
//        CLSAction a1 = new CLSAction( "recordCruiseDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum( "Ocean", TEMP ) );
//        temp.add( new CLSDatum( "FromPortCruise", TEMP ) );
//        temp.add( new CLSDatum( "ToPortCruise", TEMP ) );
//        temp.add( new CLSDatum( "CruiseDescription", TEMP ) );
        temp.add( this.d8 );
        temp.add( this.d19 );
        temp.add( this.d21 );
        temp.add( this.d22 );
        temp.add( this.d23 );
        
        problemController.addActiontoProblemSpace( this.a25 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a25.getName( ), temp ); 
    }
    
    private void assembleMCStep2( )
    {
        //If the option is delete cruise
        //  Actor supplies Cruise Number
        //  System removes record of Cruise Details
        
//        CLSAction a1 = new CLSAction( "removeCruiseDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum( "Ocean", TEMP ) );
//        temp.add( new CLSDatum( "FromPortCruise", TEMP ) );
//        temp.add( new CLSDatum( "ToPortCruise", TEMP ) );
//        temp.add( new CLSDatum( "CruiseDescription", TEMP ) );
        temp.add( this.d8 );
        temp.add( this.d19 );
        temp.add( this.d25 );
        temp.add( this.d22 );
        temp.add( this.d23 );
        
        problemController.addActiontoProblemSpace( this.a26 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a26.getName( ), temp );
    }
    
    private void assembleMCStep3( )
    {
        //If the option is add passage to cruise
        //  Actor supplies Passage details, including
        //    CruiseNumber, FromPortPassage, ToPortPassage,
        //    NumberNights, JoinDate, Cost, Description
        //  System allocates Passage Number
        //  System records Passage Details
        
//        CLSAction a1 = new CLSAction( "recordPassageDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "CruiseNumber", TEMP ) );
//        temp.add( new CLSDatum( "PassageNumber", TEMP ) );
//        temp.add( new CLSDatum( "FromPortPassage", TEMP ) );
//        temp.add( new CLSDatum( "ToPortPassage", TEMP ) );
//        temp.add( new CLSDatum( "NumberNights", TEMP ) );
//        temp.add( new CLSDatum( "JoinDate", TEMP ) );
//        temp.add( new CLSDatum( "PassageCost", TEMP ) );
//        temp.add( new CLSDatum( "PassageDescription", TEMP ) );
        temp.add( this.d8 );
        temp.add( this.d24 );
        temp.add( this.d25 );
        temp.add( this.d22 );
        temp.add( this.d27 );
        temp.add( this.d28 );
        temp.add( this.d30 );
        temp.add( this.d31 );
        
        problemController.addActiontoProblemSpace( this.a27 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a27.getName( ), temp ); 
    }
    
    private void assembleMCStep4( )
    {
        //If the option is delete passage from cruise
        //  Actor supplies Passage Number
        //  System removes record of Passage Details        
        
//        CLSAction a1 = new CLSAction( "removePassageDetails", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "PassageNumber", TEMP ) );
//        temp.add( new CLSDatum(  "FromPortPassage", TEMP ) );
//        temp.add( new CLSDatum(  "ToPortPassage", TEMP ) );
//        temp.add( new CLSDatum(  "NumberNights", TEMP ) );
//        temp.add( new CLSDatum(  "JoinDate", TEMP ) );
//        temp.add( new CLSDatum(  "Cost", TEMP ) );
//        temp.add( new CLSDatum(  "PassageDescription", TEMP ) );
        temp.add( this.d24 );
        temp.add( this.d25 );
        temp.add( this.d26 );
        temp.add( this.d27 );
        temp.add( this.d28 );
        temp.add( this.d2 );
        temp.add( this.d31 );
        
        problemController.addActiontoProblemSpace( this.a28 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a28.getName( ), temp );
    }
    
}   // end class

//------- end of file -------------------------------------
