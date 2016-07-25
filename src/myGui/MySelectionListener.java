/*
 * MySelectionListener.java 
 * Created 5 Jully 2012
 */
package myGui;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;

/**
 *  Listener and handler for selection change events
 * @author cl-simons
 */


public class MySelectionListener implements GraphSelectionListener
{

    @Override
    public void valueChanged( GraphSelectionEvent gse ) 
    {
        System.out.println( "selection changed: " + gse );
    }
    
}   // end class

//----- end of file ---------------------------------
