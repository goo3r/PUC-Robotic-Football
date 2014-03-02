//Maybe GUI?
//import java.awt.Color;
//import java.awt.Graphics2D;
//import javax.swing.JPanel;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

//For sockets
import java.io.*;
import java.net.*;

//For controller inputs and stuff
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import java.util.ArrayList;


class TestControllerToMotor{
//Main entry of program
public static void main(String args[]) {

   new TestControllerToMotor();
}

//A list to hold controllers
private ArrayList<Controller> foundControllers = new ArrayList<>();

//Boolean to deteremine if any controllers are found
private boolean controllersAreFound;

//Xbox 360 controller index
//To alternate between controllers and read there input
int player = 0;

//booleans to keep track which controllers are connected
boolean playerOneIsConnected = false;
boolean playerTwoIsConnected = false;

// X axis and Y axis from controller
float xAxisPercentage = 0.0f;
float yAxisPercentage = 0.0f;

//IPs and Ports used
//For player one and player two respectively
//These are for the robots
String[] RobotIPs = {"192.168.1.167", "192.168.1.153"};
int[] RobotPorts = {7770, 5050};

//For this application
//It'll retrieve data such as position of the robot
String ServerIP = "192.168.1.153";
int ServerPort = 4469;
byte[] receiveData = new byte[512];
DatagramSocket serverSocket;
float QXposition = 0.0f;
float QYposition = 0.0f;
float QPHIangle = 0.0f;

//Constructor i.e. also the main program
public TestControllerToMotor(){

//Setup the port for receiving
try
{
   serverSocket = new DatagramSocket(ServerPort);      
}
catch(Exception e)
{
   System.out.println(e.getMessage());
}

int counter = 0;
//Next search for controllers, if found, start getting the controller data
searchForControllers();
if(controllersAreFound)
{

   System.out.println("Controller(s) Found!");
   while(true)
   {
         //System.out.println("In loop: " + counter);
         if(playerOneIsConnected && playerTwoIsConnected)
         {
            //Currently select the next controller
            player = nextController(player); 
         }
         
         //Get the selected controller
         Controller controller = foundControllers.get(player);

         //Pull controller for current data, and break while loop if controllers are disconnected.
         //If one is still connected, switch to it and restart loop
         if( !controller.poll() )
         {
             System.out.println("Player " + (player+1) + " Disconnected!");
             if(player == 0)
             {
               playerOneIsConnected = false;
             }
             else
             {
               playerTwoIsConnected = false;
             }
             //Tell the corresponding BeagleBone to stop
             //System.out.println("PlayerOneBool=" + playerOneIsConnected);
             //System.out.println("PlayerTwoBool=" + playerTwoIsConnected);
             tellOtherSide("Done");
             
             //Check whether to switch to other controller or break
             //Both controllers are disconnected
             if(!(playerOneIsConnected || playerTwoIsConnected))
             {
               System.out.println("All controllers are disconnected!");
               break;
             }
             else
             {
               //Switch to connected controller
               player = nextController(player);               
               continue;
             }                
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
                    sendButtonPressed(buttonIndex);
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
             
             // Axes
             //This is what we are testing now
             if(component.isAnalog())
             {
                 //Get any movement the analog stick does or doesn't
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
             //retrieveData();
             //System.out.println("Made it");
             counter++;
             
         }
         
         // We have to give processor some rest.
         try 
         {
             Thread.sleep(25);
         } 
         catch (InterruptedException ex) 
         {
            System.out.println(ex.toString());
         }
   }
}
else
{
   System.out.println("No controller connected!");
   /*while(true)
   {
      retrieveData();
   }*/
}
}//Constructor i.e. main program

//Method to search for any possible controllers currently connected
private void searchForControllers() 
{
       //Apparently ControllerEnvironment.getDefaultEnvironment().getControllers() gets more other devices than it should
       //However, through testing, the Xbox controllers are the first ones found so there indexes are 0 and 1
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
       if(controllersAreFound)
       {
         //Always the first player is connected
         playerOneIsConnected = true;
         System.out.println("Player one is connected!");
         int total = 0;
         for(int i = 0; i < foundControllers.size(); i++)
         {
            total++;
         }
         System.out.println(total);

         
         //Check if the two controllers are connected         
         if(foundControllers.size() == 2)
         {
            //A second controller is connected then
            playerTwoIsConnected = true;
            System.out.println("Player two is connected!");
         } 
       }       
}

//Method to alternate to next controller
//Currently only accounting for two controllers total
private int nextController(int currentController)
{
   if(currentController == 0)
   {
      return 1;
   }
   else
   {
      return 0;
   }
}

//Tell the other side a message i.e. the BeagleBone
private void tellOtherSide(String message)
{
   try
   {
      DatagramPacket msg = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(RobotIPs[player]), RobotPorts[player]);
      DatagramSocket s = new DatagramSocket();
      s.send(msg);
   }
   catch(Exception e)
   {
      System.out.println(e.getMessage());
   }
   System.out.println(message);
}

//Method to retrieve any data from the robots
private void retrieveData()
{
   try
   {
      if(playerOneIsConnected)
      {
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
         serverSocket.receive(receivePacket);
         String msg = new String(receivePacket.getData());
         char[] posData = msg.toCharArray();
         String x = "";
         String y = "";
         String PHI = "";
         int Xcounter = 0;
         int i = 0;
         boolean keepGoing = true;
         while(keepGoing)
         {
            if(posData[i] != 'X')
            {
               if(Xcounter == 0)
                  x += posData[i];
               else if(Xcounter == 1)
                  y += posData[i];
               else if(Xcounter == 2)
                  PHI += posData[i];
               else
                  keepGoing = false;
            }
            else
            {
               Xcounter++;
            }
            i++;              
         }
         QXposition = Float.parseFloat(x);
         QYposition = Float.parseFloat(y);
         QPHIangle = Float.parseFloat(PHI);
         System.out.println("Quar2pac: " +  msg);
         System.out.println(x);
         System.out.println(y);
         System.out.println(PHI);
         System.out.println(QXposition);
         System.out.println(QYposition);
         System.out.println(QPHIangle);
      }
      if(playerTwoIsConnected)
      {
         
      }       
   }
   catch(Exception e)
   {
      System.out.println("Error");
      System.out.println(e.getMessage());
   }
   
}

//Y-Axis Movement
private void sendMovement(float percentage)
{
   //Inverted apparently
   int movement = (int)(-255*percentage);
   //If value is less than 50 and greater than -50 interpret it as zero movement
   if(movement < 50 && movement > -50)
   {
       movement = 0;
   }
   
   String data = "S" + Integer.toString(movement);
   tellOtherSide(data);  
}

//Method to discriminate which button was pressed and send it through the socket
private void sendButtonPressed(String buttonNumber)
{
   String buttonPressed = "";
   if(buttonNumber == "0")
   {
      buttonPressed = "A";
   }
   else if(buttonNumber == "1")
   {
      buttonPressed = "B";
   }
   else if(buttonNumber == "2")
   {
      buttonPressed = "X";
   }
   else if(buttonNumber == "3")
   {
      buttonPressed = "Y";
   }
   else if(buttonNumber == "4")
   {
      buttonPressed = "Left Bumper";
   }
   else if(buttonNumber == "5")
   {
      buttonPressed = "Right Bumper";
   }
   else if(buttonNumber == "6")
   {
      buttonPressed = "Back";
   }
   else if(buttonNumber == "7")
   {
      buttonPressed = "Start";
   }   
   else if(buttonNumber == "8")
   {
      buttonPressed = "Left Thumbstick";
   }
   else if(buttonNumber == "9")
   {
      buttonPressed = "Right Thumbstick";
   }
   else
   {
      //No button pressed
   }
   
   tellOtherSide("B" + buttonPressed);   
}





}//class