
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
This class represents the quarterback robot.
*/

class Quarterback extends TheBase implements TheThrowingMechanism
{

	//Constructors
	public Quarterback()
	{
		super();
	}
	
	public Quarterback(double x, double y, double phi)
	{
		super(x, y, phi);
	}
	
	//TheThrowingMechanism's methods implememented
	
	//This method will drive the flywheels on the throwing mechanism.
	//leftSpeed and rightSpeed will be used to control the speed of the flywheels respectively.
	public void driveFlyWheels(int leftSpeed, int rightSpeed)
	{
		//Logic to drive flywheels
	}
	
	//This method will have the actuator extend until the ball is grabbed by the flywheels
	public void startActuator()
	{
		//Logic to have actuator push the ball into the flywheels ( extend )
	}
	
	//This method will retract the actuator
	public void retractActuator()
	{
		//Logic to retract back to original position
	}	

}