'''//------------------------------------------------------------//

**************		****	 ****		**************		
**************		****	 ****		**************
****	  ****		****	 ****		****
****	  ****		****	 ****		****
**************		****	 ****		****
**************		****	 ****		****
****			****	 ****		****
****			****     ****		****
****			*************		**************
****			*************		**************		

-------------------------------------------------------------//
Programmer	    Date		Modification Reason

Jaime Alvarez	    01-31-14		Initial Implementation


//-----------------------------------------------------------//

Description:
This class will simulate the Quarterback.
It provides functions as driving the flywheel
motors, control the linear actuator, etc.

'''

from TheBase import TheBase

class TheQuarterback(TheBase):
    #The constructor
    def __init__(self, x = 0.0, y = 0.0, phi = 0.0):
        TheBase.__init__(self, x, y, phi)

    #This method will drive the flywheels on the throwing mechanism
    #leftSpeed and rightSpeed will be used to control the speed of the flywheels respectively
    def driveFlyWheels(self, leftSpeed = 0.0, rightSpeed = 0.0):
        #Logic to drive flywheels
        print "throwing mechanism"

    #This method will have the actuator extend until the ball is grabbed by the flywheels
    def startActuator(self):
        #Logic to have the actuator extend
        print "actuator"

    #This method will retract the actuator
    def retractActuator(self):
        #Logic to retract back to original position
        print "retract"
