/*
//------------------------------------------------------------//

**************		****		****		**************		
**************		****		****		**************
****		 ****		****		****		****
****		 ****		****		****		****
**************		****		****		****
**************		****		****		****
****					****		****		****
****					****		****		****
****					*************		**************
****					*************		**************		

-------------------------------------------------------------//
Programmer				Date				Modification Reason

Jaime Alvarez			01-31-14			Initial Implementation


//-----------------------------------------------------------//

Description:
This the class that will mainly concentrate on
getting, interpreting, and sending Xbox 360 
controller input to a robot.
*/

//For sockets


import java.io.*;
import java.net.*;

//For controller inputs
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


class ProcessUserInput extends Thread
{      
   //Variables used
   //A description is available in the constructor description
   private String player = "";
   private String theIP = "";
   private int thePort;
   private Controller controller;
   private RobotCoordinates QBposition;
   private RobotCoordinates RCposition;
   
   private DatagramSocket robotSocket;
   
   //This keeps track of the current Axis position from the controller
   private float xAxisPercentage = 0.0f;
   private float yAxisPercentage = 0.0f;
   
   //This keeps track if the robot is turning
   private boolean isTurning = false;
   
   //Constructor
   //Pass in the player's controller reference, the IP and Port number to send the input data to the right Robot,
   //and keep track of the position of the Quarterback and Receiver.
   public ProcessUserInput(String player, Controller controller, String IP, int portNumber, RobotCoordinates QBposition, RobotCoordinates RCposition)
   {
      this.player = player;
      this.controller = controller;
      theIP = IP;
      thePort = portNumber;
      this.QBposition = QBposition;
      this.RCposition = RCposition;
      
      try
      {
    	  robotSocket = new DatagramSocket();
      }
      catch(Exception e)
      {
    	  System.out.println(e.getMessage());
      }
   }
   
   //Implement the run() method from the super class Thread
   public void run()
   {
      while(true)
      {   
         //Poll controller for current data, and break while loop if controllers are disconnected.
         if( !controller.poll() )
         {
             System.out.println(player + " is Disconnected!");
             
             //Tell the corresponding BeagleBone(robot) to stop
             tellOtherSide("Done");             
             break;                            
         }      
                 
         //Go trough all components of the controller.
         Component[] components = controller.getComponents();
         for(int i=0; i < components.length; i++)
         {
             Component component = components[i];
             
             //Identify the component
             Identifier componentIdentifier = component.getIdentifier();
             
             // Buttons
             //For testing purposes ignore these for now
             //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
             if(componentIdentifier.getName().matches("^[0-9]*$"))// If the component identifier name contains only numbers, then this is a button.
             { 
                 // Is button pressed?
                 boolean isItPressed = true;
                 if(component.getPollData() == 0.0f)
                     isItPressed = false;
                 
                 // Button index
                 if(isItPressed)
                 {
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    //sendButtonPressed(buttonIndex);
                  }  
                                     
                 // We know that this component was button so we can skip to next component.
                 continue;
             }
             
             // Hat switch
             //For testing purposes ignore these for now
             if(componentIdentifier == Component.Identifier.Axis.POV){
                 float hatSwitchPosition = component.getPollData();
                                     
                 //We know that this component was a hat switch so we can skip to next component.
                 continue;
             }
             
             //Axes
             //This is what we are testing now
             if(component.isAnalog())
             {
                 //Get any movement the analog stick does or doesn't
                 float axisValueInPercentage = component.getPollData();
                                                      
                 // X axis
                 if(componentIdentifier == Component.Identifier.Axis.X){
                     xAxisPercentage = axisValueInPercentage;
                     //sendMovement(-1*xAxisPercentage, 'X');
                     continue; // Go to next component.
                 }
                 // Y axis
                 if(componentIdentifier == Component.Identifier.Axis.Y){
                     yAxisPercentage = axisValueInPercentage;
                     sendMovement(yAxisPercentage, 'Y');
                     continue; // Go to next component.
                 }                    
                 
                 try
                 {
                  Thread.sleep(30);
                 }
                 catch(Exception ex)
                 {
                  System.out.println(ex.getMessage());
                 }
                 
             }                         
         }
       }   
   }//run()

   //Tell the other side a message i.e. the BeagleBone
   private void tellOtherSide(String message)
   {
      try
      {
         DatagramPacket msg = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(theIP), thePort);         
         robotSocket.send(msg);
      }
      catch(Exception e)
      {
         System.out.println(e.getMessage());
      }
      System.out.println(message);
   }//tellOtherSide()
   
   //Robot Movement!
   private void sendMovement(float percentage, char axis)
   {
      //Inverted apparently
      int movement = (int)(-255*percentage);
      //If value is less than 50 and greater than -50 interpret it as zero movement
      if(movement < 60 && movement > -60)
      {
          movement = 0;
      }      
      //Check if it's a X or Y axis input using the axis parameter
      String commandType = "S";
      if(axis == 'X')
    	  commandType = "T";
      
      boolean canISend = true;
      
      //If the robot is turning, ignore the move forward and backward commands completely
      if(isTurning && axis == 'Y')
      {
    	  canISend = false;
      }
      //If the robot is turning, send the new turning speed
      else if(isTurning && axis == 'X' && (movement > 49 || movement < -49))
      {
    	  //canISend already equals true and keep the isTurning boolean as it is (true)
      }
      //If the robot is turning, but the turning command is zero then stop turning
      else if(isTurning && axis == 'X' && (movement < 49 && movement > -49))
      {
    	  isTurning = false;
    	  //canISend already true, send T0    	  
      }
      //If we want to start turning again
      else if(!isTurning && axis == 'X' && (movement > 49 || movement < -49))
      {
    	  isTurning = true;
    	  //canISend is true leave it as it is
      }
      //If NOT turning at all then don't send the turning zero value
      else if(!isTurning && axis == 'X' && (movement < 49 && movement > -49))
      {
    	  canISend = false;    	  
      }
      //Else just go straight forward or backward
      else
      {
    	  //Send full forward or full backward command, so leave canISend (true)
      }      
      
      if(canISend)
      {
    	  String data = commandType + Integer.toString(movement);
          tellOtherSide(data);
      }      
        
   }//sendMovement()

}//ProcessUserInput