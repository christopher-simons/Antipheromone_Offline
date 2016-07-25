/*
 * DesignProblem7GDP.java
 * created 3 July 2008
 * major refactor 17 June 2009
 * greatly refactored 28 September 2011
 */

package problem;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class holds the specification of design problem 7,
 *  Graduate Development Programme (GDP)
 * 
 * see http://www.cems.uwe.ac.uk/~clsimons/CaseStudies/GraduateDevelopmentProgram.htm
 * 
 *  @author Christopher Simons
 */

public class DesignProblem7GDP extends DesignProblem
{
    /** 
     * constructor 
     * @param reference to problem controller
     */
    public DesignProblem7GDP( ProblemController pc )
    {
        super( pc );
        
        a0 = new CLSAction( "RetrieveStudentList", 0 );
        a1 = new CLSAction( "RetrieveGDP", 1 );
        a2 = new CLSAction( "SetGDP", 2 );
        a3 = new CLSAction( "ApplyRules", 3 );
        a4 = new CLSAction( "SaveGDP", 4 );
        a5 = new CLSAction( "RetrieveGroupOfStudents", 5 );
        a6 = new CLSAction( "SetGDPForGroup", 6 );
        a7 = new CLSAction( "SaveGDPForGroup", 7 );
        a8 = new CLSAction( "RetrieveReportTemplate", 8 );
        a9 = new CLSAction( "SetOptionalReportParameters", 9 );
        a10 = new CLSAction( "SaveReportParameters", 10 );
        a11 = new CLSAction( "GenerateReport", 11 );
        
        
        d0 = new CLSDatum( "StudentNumber", 0 );
        d1 = new CLSDatum( "Surname", 1 );
        d2 = new CLSDatum( "Forename", 2 );
        d3 = new CLSDatum( "DateOfBirth", 3 );
        d4 = new CLSDatum( "PersonDetails", 4 );
        d5 = new CLSDatum( "AwardRegistrations", 5 );
        d6 = new CLSDatum( "AddressDetails", 6 );
        d7 = new CLSDatum( "ContactDetails", 7 );
        d8 = new CLSDatum( "UserIDDetails", 8 );
        d9 = new CLSDatum( "Attendance", 9 );
        d10 = new CLSDatum( "Statement", 10 );
        d11 = new CLSDatum( "Review", 11 );
        d12 = new CLSDatum( "RPE", 12 );
        d13 = new CLSDatum( "Completed", 13 );
        d14 = new CLSDatum( "UserID", 14 );
        d15 = new CLSDatum( "Date", 15 );
        d16 = new CLSDatum( "Rule1", 16 );
        d17 = new CLSDatum( "Rule2", 17 );
        d18 = new CLSDatum( "Rule3", 18 );
        d19 = new CLSDatum( "Rule4", 19 );
        d20 = new CLSDatum( "Rule5", 20 );        
        d21 = new CLSDatum( "School", 21 );        
        d22 = new CLSDatum( "PrimaryAward", 22 );        
        d23 = new CLSDatum( "PrimaryTarget", 23 );        
        d24 = new CLSDatum( "RAG", 24 );        
        d25 = new CLSDatum( "RASP", 25 );        
        d26 = new CLSDatum( "GDPLevel", 26 );        
        d27 = new CLSDatum( "PACode", 27 );        
        d28 = new CLSDatum( "FacultyTemplate", 28 );       
        d29 = new CLSDatum( "PrimaryAwardTemplate", 29 );        
        d30 = new CLSDatum( "FacultyParameter", 30 );        
        d31 = new CLSDatum( "StudentNumberParameter", 31 );        
        d32 = new CLSDatum( "PACodeParameter", 32 );        
        d33 = new CLSDatum( "PARegStatusParameter", 33 );        
        d34 = new CLSDatum( "RAGParameter", 34 );        
        d35 = new CLSDatum( "RASPParameter", 35 );        
        d36 = new CLSDatum( "AcademicYearParameter", 36 );        
        d37 = new CLSDatum( "LevelParameter", 37 );        
        d38 = new CLSDatum( "AttendanceParameter", 38 );        
        d39 = new CLSDatum( "StatementParameter", 39 );
        d40 = new CLSDatum( "ReviewParameter", 40 );
        d41 = new CLSDatum( "RPEParameter", 41 );
        d42 = new CLSDatum( "CompletedParameter", 42 );
    }
    
    public void assembleDesignProblem7( )
    {
        assembleRecordGDPOutcomeForAnIndividualStudent( );
        assembleRecordGDPOutcomesForABatchGroupOfStudents( );
        assembleGenerateReportsOnGDPOutcomes( );
        
//        problemController.showActionsAndData( );
    }
    
    private void assembleRecordGDPOutcomeForAnIndividualStudent( ) 
    {
        assembleUC1Step1( );
        assembleUC1Step2( );
        assembleUC1Step3( );
        assembleUC1Step4( );
        assembleUC1Step5( );
    }
    
    private void assembleUC1Step1( ) 
    {
//        CLSAction a1 = new CLSAction( "RetrieveStudentList", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Surname", TEMP ) );
//        temp.add( new CLSDatum(  "Forename", TEMP ) );
//        temp.add( new CLSDatum(  "DateOfBirth", TEMP ) );
//        temp.add( new CLSDatum(  "PersonDetails", TEMP ) );
//        temp.add( new CLSDatum(  "AwardRegistrations", TEMP ) );
//        temp.add( new CLSDatum(  "AddressDetails", TEMP ) );
//        temp.add( new CLSDatum(  "ContactDetails", TEMP ) );
//        temp.add( new CLSDatum(  "UserIDDetails", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d1 );
        temp.add( this.d2 );
        temp.add( this.d3 );
        temp.add( this.d4);
        temp.add( this.d5 );
        temp.add( this.d6 );
        temp.add( this.d7 );
        temp.add( this.d8 );
        problemController.addActiontoProblemSpace( a0 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a0.getName( ), temp );
    }
    
    private void assembleUC1Step2( ) 
    { 
//        CLSAction a1 = new CLSAction( "RetrieveGDP", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Attendance", TEMP ) );
//        temp.add( new CLSDatum(  "Statement", TEMP ) );
//        temp.add( new CLSDatum(  "Review", TEMP ) );
//        temp.add( new CLSDatum(  "RPE", TEMP ) );
//        temp.add( new CLSDatum(  "Completed", TEMP ) );
//        temp.add( new CLSDatum(  "UserID", TEMP ) );
//        temp.add( new CLSDatum(  "Date", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        temp.add( this.d13 );
        temp.add( this.d14 );
        temp.add( this.d15 );
        
        problemController.addActiontoProblemSpace( a1 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a1.getName( ), temp );
    }
    
    private void assembleUC1Step3( ) 
    { 
//        CLSAction a1 = new CLSAction( "SetGDP", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Attendance", TEMP ) );
//        temp.add( new CLSDatum(  "Statement", TEMP ) );
//        temp.add( new CLSDatum(  "Review", TEMP ) );
//        temp.add( new CLSDatum(  "RPE", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        
        problemController.addActiontoProblemSpace( a2 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a2.getName( ), temp );
    }
    
    private void assembleUC1Step4( ) 
    { 
//        CLSAction a1 = new CLSAction( "ApplyRules", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "Rule1", TEMP ) );
//        temp.add( new CLSDatum(  "Rule2", TEMP ) );
//        temp.add( new CLSDatum(  "Rule3", TEMP ) );
//        temp.add( new CLSDatum(  "Rule4", TEMP ) );
//        temp.add( new CLSDatum(  "Rule5", TEMP ) );
//        temp.add( new CLSDatum(  "Attendance", TEMP ) );
//        temp.add( new CLSDatum(  "Statement", TEMP ) );
//        temp.add( new CLSDatum(  "Review", TEMP ) );
//        temp.add( new CLSDatum(  "RPE", TEMP ) );
//        temp.add( new CLSDatum(  "Completed", TEMP ) );
//        temp.add( new CLSDatum(  "UserID", TEMP ) );
//        temp.add( new CLSDatum(  "Date", TEMP ) );
        temp.add( this.d16 );
        temp.add( this.d17 );
        temp.add( this.d18 );
        temp.add( this.d19 );
        temp.add( this.d20 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        temp.add( this.d13 );
        temp.add( this.d14 );
        temp.add( this.d15);
        
        problemController.addActiontoProblemSpace( a3 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a3.getName( ), temp );
    }
    
    private void assembleUC1Step5( )
    {
//        CLSAction a1 = new CLSAction( "SaveGDP", actionNumber++ );
        
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum( "Attendance", TEMP ) );
//        temp.add( new CLSDatum( "Statement", TEMP ) );
//        temp.add( new CLSDatum( "Review", TEMP ) );
//        temp.add( new CLSDatum( "RPE", TEMP ) );
//        temp.add( new CLSDatum( "Completed", TEMP ) );
//        temp.add( new CLSDatum( "UserID", TEMP ) );
//        temp.add( new CLSDatum( "Date", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        temp.add( this.d13 );
        temp.add( this.d14 );
        temp.add( this.d15 );
        
        problemController.addActiontoProblemSpace( a4 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a4.getName( ), temp );
    }
    
    private void assembleRecordGDPOutcomesForABatchGroupOfStudents( ) 
    {
        assembleUC2Step6( );
        assembleUC2Step7( );
        assembleUC2Step8( );
    }

    void assembleUC2Step6( ) 
    { 
//        CLSAction a1 = new CLSAction( "RetrieveGroupOfStudents", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "School", TEMP ) );
//        temp.add( new CLSDatum( "PrimaryAward", TEMP ) );
//        temp.add( new CLSDatum( "PrimaryTarget", TEMP ) );
//        temp.add( new CLSDatum( "RAG", TEMP ) );
//        temp.add( new CLSDatum( "RASP", TEMP ) );
//        temp.add( new CLSDatum( "GDPLevel", TEMP ) );
//        temp.add( new CLSDatum( "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum( "PACode", TEMP ) );
        temp.add( this.d21 );
        temp.add( this.d22 );
        temp.add( this.d23 );
        temp.add( this.d24 );
        temp.add( this.d25 );
        temp.add( this.d26 );
        temp.add( this.d0 );
        temp.add( this.d27 );
        
        problemController.addActiontoProblemSpace( a5 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a5.getName( ), temp );
    }
    
    void assembleUC2Step7( ) 
    { 
//        CLSAction a1 = new CLSAction( "SetGDPForGroup", actionNumber++);
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum(  "Attendance", TEMP ) );
//        temp.add( new CLSDatum(  "Statement", TEMP ) );
//        temp.add( new CLSDatum(  "Review", TEMP ) );
//        temp.add( new CLSDatum(  "RPE", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        
        problemController.addActiontoProblemSpace( a6 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a6.getName( ), temp );
    }
    
    void assembleUC2Step8( ) 
    { 
//        CLSAction a1 = new CLSAction( "SaveGDPForGroup", actionNumber++ );
        
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "StudentNumber", TEMP ) );
//        temp.add( new CLSDatum( "Attendance", TEMP ) );
//        temp.add( new CLSDatum( "Statement", TEMP ) );
//        temp.add( new CLSDatum( "Review", TEMP ) );
//        temp.add( new CLSDatum( "RPE", TEMP ) );
//        temp.add( new CLSDatum( "Completed", TEMP ) );
//        temp.add( new CLSDatum( "UserID", TEMP ) );
//        temp.add( new CLSDatum( "Date", TEMP ) );
        temp.add( this.d0 );
        temp.add( this.d9 );
        temp.add( this.d10 );
        temp.add( this.d11 );
        temp.add( this.d12 );
        temp.add( this.d13 );
        temp.add( this.d14 );
        temp.add( this.d15 );
        
        problemController.addActiontoProblemSpace( a7 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a7.getName( ), temp );
    }

    private void assembleGenerateReportsOnGDPOutcomes( ) 
    {
        assembleUC3Step9( );
        assembleUC3Step10( );
        assembleUC3Step11( );  
        assembleUC3Step12( );  
    }
    
    private void assembleUC3Step9( ) 
    { 
//        CLSAction a1 = new CLSAction( "RetrieveReportTemplate", actionNumber++ );
        
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "FacultyTemplate", TEMP ) );
//        temp.add( new CLSDatum(  "PrimaryAwardTemplate", TEMP ) );
//        temp.add( new CLSDatum(  "FacultyParameter", TEMP ) );
//        temp.add( new CLSDatum(  "StudentNumberParameter", TEMP ) );
//        temp.add( new CLSDatum(  "PACodeParameter", TEMP ) );
//        temp.add( new CLSDatum(  "PARegStatusParameter", TEMP ) );
//        temp.add( new CLSDatum(  "RAGParameter", TEMP ) );
//        temp.add( new CLSDatum(  "RASPParameter", TEMP ) );
//        temp.add( new CLSDatum(  "AcademicYearParameter", TEMP ) );
        temp.add( this.d28 );
        temp.add( this.d29 );
        temp.add( this.d30 );
        temp.add( this.d31 );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        temp.add( this.d35 );
        temp.add( this.d36 );
        
        problemController.addActiontoProblemSpace( a8 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a8.getName( ), temp );
    }
    
    private void assembleUC3Step10( ) 
    { 
//        CLSAction a1 = new CLSAction( "SetOptionalReportParameters", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "LevelParameter", TEMP ) );
//        temp.add( new CLSDatum( "AttendanceParameter", TEMP ) );
//        temp.add( new CLSDatum( "StatementParameter", TEMP ) );
//        temp.add( new CLSDatum( "ReviewParameter", TEMP ) );
//        temp.add( new CLSDatum( "RPEParameter", TEMP ) );
//        temp.add( new CLSDatum( "CompletedParameter", TEMP ) );
        temp.add( this.d37 );
        temp.add( this.d38 );
        temp.add( this.d39 );
        temp.add( this.d40 );
        temp.add( this.d41 );
        temp.add( this.d42 );
        
        problemController.addActiontoProblemSpace( a9 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a9.getName( ), temp );    
    }
    
    private void assembleUC3Step11( ) 
    { 
//        CLSAction a1 = new CLSAction( "SaveReportParameters", actionNumber++ );
        
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum( "FacultyTemplate", TEMP ) );
//        temp.add( new CLSDatum( "PrimaryAwardTemplate", TEMP ) );
//        temp.add( new CLSDatum( "StudentNumberParameter", TEMP ) );
//        temp.add( new CLSDatum( "PACodeParameter", TEMP ) );
//        temp.add( new CLSDatum( "PARegStatusParameter", TEMP ) );
//        temp.add( new CLSDatum( "RAGParameter", TEMP ) );
//        temp.add( new CLSDatum( "RASPParameter", TEMP ) );
//        temp.add( new CLSDatum( "AcademicYearParameter", TEMP ) );
//        temp.add( new CLSDatum( "LevelParameter", TEMP ) );
//        temp.add( new CLSDatum( "AttendanceParameter", TEMP ) );
//        temp.add( new CLSDatum( "StatementParameter", TEMP ) );
//        temp.add( new CLSDatum( "ReviewParameter", TEMP ) );
//        temp.add( new CLSDatum( "RPEParameter", TEMP ) );
//        temp.add( new CLSDatum( "CompletedParameter", TEMP ) );
        temp.add( this.d28 );
        temp.add( this.d29 );
        temp.add( this.d31 );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        temp.add( this.d35 );
        temp.add( this.d36 );
        temp.add( this.d37 );
        temp.add( this.d38 );
        temp.add( this.d39 );
        temp.add( this.d40 );
        temp.add( this.d41 );
        temp.add( this.d42 );
        
        problemController.addActiontoProblemSpace( a10 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a10.getName( ), temp );   
    }
    
    private void assembleUC3Step12( ) 
    { 
//        CLSAction a1 = new CLSAction( "GenerateReport", actionNumber++ );
        List< CLSDatum > temp = new ArrayList< CLSDatum >( ); 
//        temp.add( new CLSDatum(  "FacultyTemplate", TEMP ) );
//        temp.add( new CLSDatum(  "PrimaryAwardTemplate", TEMP ) );
//        temp.add( new CLSDatum(  "StudentNumberParameter", TEMP ) );
//        temp.add( new CLSDatum(  "PACodeParameter", TEMP ) );
//        temp.add( new CLSDatum(  "PARegStatusParameter", TEMP ) );
//        temp.add( new CLSDatum(  "RAGParameter", TEMP ) );
//        temp.add( new CLSDatum(  "RASPParameter", TEMP ) );
//        temp.add( new CLSDatum(  "AcademicYearParameter", TEMP ) );
//        temp.add( new CLSDatum(  "LevelParameter", TEMP ) );
//        temp.add( new CLSDatum(  "AttendanceParameter", TEMP ) );
//        temp.add( new CLSDatum(  "StatementParameter", TEMP ) );
//        temp.add( new CLSDatum(  "ReviewParameter", TEMP ) );
//        temp.add( new CLSDatum(  "RPEParameter", TEMP ) );
//        temp.add( new CLSDatum(  "CompletedParameter", TEMP ) );
//        temp.add( new CLSDatum(  "StudentNumber", TEMP ) );
        temp.add( this.d28 );
        temp.add( this.d29 );
        temp.add( this.d31 );
        temp.add( this.d32 );
        temp.add( this.d33 );
        temp.add( this.d34 );
        temp.add( this.d35 );
        temp.add( this.d36 );
        temp.add( this.d37 );
        temp.add( this.d38 );
        temp.add( this.d39 );
        temp.add( this.d40 );
        temp.add( this.d41 );
        temp.add( this.d42 );
        temp.add( this.d0 );
        
        problemController.addActiontoProblemSpace( a11 );
        problemController.addDatumToProblemSpace( temp );
        problemController.addEntryToUseTable( a11.getName( ), temp );           
    }
    
}   // end class

//------- end of file -------------------------------------
