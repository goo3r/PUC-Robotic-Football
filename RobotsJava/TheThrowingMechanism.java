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
This class represents interface for the throwing mechanism for the quarterback.

*/
interface TheThrowingMechanism
{
	//Methods for the quarterback to implement
	public void driveFlyWheels(int leftSpeed, int rightSpeed);
	
	public void startActuator();
	
	public void retractActuator();


}