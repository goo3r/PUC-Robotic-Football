package jinputjoysticktestv2;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


class TestControllerToMotor extends javax.swing.JFrame{
public static void main(String args[]) {

   new TestControllerToMotor();
}



//private javax.swing.JPanel jPanelXYAxes;
private ArrayList<Controller> foundControllers = new ArrayList<>();
private boolean controllersAreFound;

// X axis and Y axis
float xAxisPercentage = 0.0f;
float yAxisPercentage = 0.0f;


public TestControllerToMotor(){

searchForControllers();
if(controllersAreFound)
{

while(true)
        {
            // Currently selected controller.
            //int selectedControllerIndex = foundControllers.getSelectedControllerName();
            Controller controller = foundControllers.get(0);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if( !controller.poll() ){
                System.out.println("Controller Disconnected!");
                break;
            }           
            
            
                    
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                Identifier componentIdentifier = component.getIdentifier();
                
                // Buttons
                //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    // Button index
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    
                    // Create and add new button to panel.
                    //JToggleButton aToggleButton = new JToggleButton(buttonIndex, isItPressed);
                    //aToggleButton.setPreferredSize(new Dimension(48, 25));
                    //aToggleButton.setEnabled(false);
                    //buttonsPanel.add(aToggleButton);
                    
                    // We know that this component was button so we can skip to next component.
                    continue;
                }
                
                // Hat switch
                if(componentIdentifier == Component.Identifier.Axis.POV){
                    float hatSwitchPosition = component.getPollData();
                    //window.setHatSwitch(hatSwitchPosition);
                    
                    // We know that this component was hat switch so we can skip to next component.
                    continue;
                }
                
                // Axes
                if(component.isAnalog()){
                    float axisValueInPercentage = component.getPollData();
                                        
                    // X axis
                    if(componentIdentifier == Component.Identifier.Axis.X){
                        xAxisPercentage = axisValueInPercentage;
                        continue; // Go to next component.
                    }
                    // Y axis
                    if(componentIdentifier == Component.Identifier.Axis.Y){
                        yAxisPercentage = axisValueInPercentage;
                        sendMovement(yAxisPercentage);
                        continue; // Go to next component.
                    }                    
                    
                }
            }
            
            // We have to give processor some rest.
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }



      }

}
else
{
   System.out.println("No controller connected!");
}
}

private void searchForControllers() 
{
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++)
        {
            Controller controller = controllers[i];
            
            if (
                    controller.getType() == Controller.Type.STICK || 
                    controller.getType() == Controller.Type.GAMEPAD || 
                    controller.getType() == Controller.Type.WHEEL ||
                    controller.getType() == Controller.Type.FINGERSTICK
               )
               {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                
               }
        }
        controllersAreFound = !foundControllers.isEmpty();
}

private void sendMovement(float percentage)
{
   //Inverted apparently
   System.out.println(-255*percentage);
}



}