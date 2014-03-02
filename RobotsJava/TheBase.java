
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

Jaime Alvarez			12-21-13			Initial Implementation


//-----------------------------------------------------------//
Description:
This class represents the base of all robots. This class will drive the base motors,
receive encoder data, and send data to the server. 




*/
import java.io.*;
import java.net.*;

class TheBase
{
	//The X, Y, and Phi coordinates (X,Y,PHI) initially set to zero.
	double Xcurrent = 0;
	double Ycurrent = 0;
	double PHIcurrent = 0;
	
	//Length of wheel base (inches)
	double lengthOfWheelBase = 15;
	
	//These variabels keep track of the number of pulses received during a time interval
	int LWPulses = 0;
	int RWPulses = 0;
	
	//The IP address of the server
	String serverAddress = "172.0000000";
	
	//Constructors
	//This constructor should be called for creating the robot that will be centered at the origin i.e. Quarterback
	public TheBase()
	{
	}
	
	//This constructor should be called for creating a robot in reference to the origin i.e. Receiver
	public TheBase(double x, double y, double phi)
	{
		Xcurrent = x;
		Ycurrent = y;
		PHIcurrent = phi;
	}
	
	//Method to setup network connection
	public boolean connectWithServer()
	{
		//Set up socket with server ip address and port number.
		//Also I/O streams
		try(
				Socket s = new Socket(serverAddress, 4465);
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				)
				{
					//logic
					
					//connection successful so return true
					return true;
				} 		
		catch(IOException e)
		{
			System.err.println("Couldn't get I/O for the connection to the server " + e.getMessage());
			return false;
			
		}
		catch(Exception e1)
		{
			System.err.println(e1.getMessage());
			return false;
		}
		
	}	
	
	
	//This method will take the data received from the server and determine in what direction the robot
	//will move.  It will also use the given speeds for the left and right motors determined by the server.
	//For example: User pushes the thumbstick left up, the server will determine how much should the robot will turn
	//and move forward appropriately.
	public void driveBaseMotors(String direction, int leftMotorSpeed, int rightMotorSpeed)
	{
		if(direction == "Full Forward")
		{
			//Drive both motors in forward direction (same speed)
		}
		else if(direction == "Full Backward")
		{
			//Drive both motors in backward direction (same speed)
		}
		else if(direction == "Rotate Left")
		{
			//Have right motor drive forward and the left motor drive backward
		}
		else if(direction == "Rotate Right")
		{
			//Have right motor drive backward and the left motor drive forward 
		}
		else if(direction == "Forward Left")
		{
			//Have right motor drive faster forward than the left motor (also going forward)
		}
		else if(direction == "Forward Right")
		{
			//Have left motor drive faster forward than the right motor (also going forward)
		}
		else if(direction == "Backward Left")
		{
			//Have right motor drive faster backward than the left motor (also going backward)
		}
		else if(direction == "Backward Right")
		{
			//Have left motor drive faster backward than the right motor (also going backward)
		}
		else
		{
			//Do nothing
		}		
	}
	
	//These next two method checks how many pulses are received at a certain time interval from the encoders
	//and will save the number of pulses to LWPulses and RWPulses variables.
	public void getLeftWheelPulses()
	{
		try
		{
			//Logic to get pulses from left encoder
			//LWPulses = ......
		}
		catch(Exception ex)
		{
			//Handle any errors
		}
		
	}
	
	public void getRightWheelPulses()
	{
		try
		{
			//Logic to get pulses from right encoder
			//RWPulses = ......
		}
		catch(Exception ex)
		{
			//Handle any errors
		}
		
	}
	
	//This method will use the number of pulses received during a time interval (using LWPulses and RWPulses variables)
	//to update the position of the robot.
	public void updatePosition()
	{
		//Delta Distance Right Wheel (dDRW), Delta Distance Left wheel (dDLW), and Delta Distance Center of Robot (dDCR) - (initially zero change)
		double dDRW = 0;
		double dDLW = 0;
		double dDCR = 0;
		
		//Get number of pulses for both left and right encoders and determine distance traveled		
		dDLW = 0.012 * LWPulses;
		dDRW = 0.012 * RWPulses;
		
		//Determine the distance traveled with respect to the center of the robot
		dDCR = (dDLW + dDRW)/2;
		
		//Update orientation of robot first 
		PHIcurrent = PHIcurrent + (dDRW - dDLW) / lengthOfWheelBase;
		
		//Then X and Y coordinates
		Xcurrent = Xcurrent + dDCR*Math.sin(-PHIcurrent);
		Ycurrent = Ycurrent + dDCR*Math.cos(PHIcurrent);
		
		//Clean up delta changes
		dDRW = dDLW = dDCR = 0;
		
		//Send new coordinates to server
		sendCoordinates();		
		
	}
	
	//This method will send the current (X,Y,PHI) coordinates to the server
	public void sendCoordinates()
	{
		//Logic to send data
	}

}