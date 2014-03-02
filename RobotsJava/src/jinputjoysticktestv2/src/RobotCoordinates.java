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
This class is to contain the coordinates of a robot
The reason we used a class to represent this is so the 
ProcessUserInput thread object will see the position of
the quarterback and the receiver to determine when it's
good to throw while the ProcessRobotPosition thread will
update the positions accordingly. The beauty of pass by 
reference.
*/


class RobotCoordinates
{
   //The current position of the robot
   private float Xcurrent = 0.0f;
   private float Ycurrent = 0.0f;
   private float PHIcurrent = 0.0f;
   
   //Start the robot in a specified coordinate
   public RobotCoordinates(float x, float y, float phi)
   {
      Xcurrent = x;
      Ycurrent = y;
      PHIcurrent = phi;
   }
   
   //Set methods
   public void setX(float x){Xcurrent = x;}
   public void setY(float y){Ycurrent = y;}
   public void setPHI(float phi){PHIcurrent = phi;}
   
   //Get methods
   public float getX(){return Xcurrent;}
   public float getY(){return Ycurrent;}
   public float getPHI(){return PHIcurrent;}
}