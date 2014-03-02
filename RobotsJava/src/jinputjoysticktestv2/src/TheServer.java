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
The main server application.
This application will be in charge
of executing threads that will process
Xbox 360 Controller input and position
of the robots.
*/


//For sockets
import java.io.*;
import java.net.*;

//For controller inputs and stuff
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import java.util.ArrayList;

class TheServer
{
   //Entry of program
   public static void main(String[] args)
   {
      new TheServer();
   }
   
   //A list to hold controllers
   private ArrayList<Controller> foundControllers = new ArrayList<>();
   
   //Boolean to determine if any controllers are found
   private boolean controllersAreFound;
         
   //booleans to keep track which controllers are connected
   private boolean playerOneIsConnected = false;
   private boolean playerTwoIsConnected = false;
      
   //IPs and Ports used
   //For player one and player two respectively
   //These are for the robots
   private String[] RobotIPs = {"192.168.1.100", "192.168.1.153"};
   private int[] RobotPorts = {8444, 5050};
   
   //For this application
   //It'll retrieve data such as position of the robot
   private String ServerIP = "192.168.1.9";
   private int ServerPort = 4489;
   
   //The program doing its job
   public TheServer()
   {
      searchForControllers();
      if(controllersAreFound)
      {
         RobotCoordinates QB = new RobotCoordinates(0.0f, 0.0f, 0.0f);
         RobotCoordinates RC = new RobotCoordinates(0.0f, 0.0f, 0.0f);
         
         if(playerOneIsConnected)
         {
            ProcessUserInput QBSend = new ProcessUserInput("Player 1", foundControllers.get(0), RobotIPs[0], RobotPorts[0], QB, RC);
            //ProcessRobotPosition QBReceive = new ProcessRobotPosition("Player 1", foundControllers.get(0), ServerIP, ServerPort, QB);
            QBSend.setPriority(10);
            //QBReceive.setPriority(8);
            QBSend.start();
            //QBReceive.start(); 
         }   
      }
      else
      {
         System.out.println("No controllers are connected!");
      }   
   }//Constructor
   
   //Method to search for any possible controllers currently connected
   private void searchForControllers() 
   {
       //Apparently ControllerEnvironment.getDefaultEnvironment().getControllers() gets more other devices than it should
       //However, through testing, the 2 Xbox controllers are the first ones found so there indexes are 0 and 1 just fyi
       Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

       for(int i = 0; i < controllers.length; i++)
       {
            Controller controller = controllers[i];
            
            if (controller.getType() == Controller.Type.GAMEPAD)
            {
                //Add new controller to the list of all controllers.
                foundControllers.add(controller);                
            }
       }
       
       controllersAreFound = !foundControllers.isEmpty();
       
       //Set booleans to true if corresponding players are connected
       //This logic only concerns itself with up to two controllers connected
       if(controllersAreFound)
       {
         //Always the first player is connected
         playerOneIsConnected = true;
         System.out.println("Player one is connected!");
                  
         //Check if the two controllers are connected         
         if(foundControllers.size() == 2)
         {
            //A second controller is connected then
            playerTwoIsConnected = true;
            System.out.println("Player two is connected!");
         } 
       }       
   }//searchForControllers()

}//TheServer