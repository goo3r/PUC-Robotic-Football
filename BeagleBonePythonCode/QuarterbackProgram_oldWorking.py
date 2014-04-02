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

Jaime Alvarez	    02-01-14		Initial Implementation


//-----------------------------------------------------------//

Description:
The main program for the quarterback.
'''

from TheBase import TheBase
from TheQuarterback import TheQuarterback

#The main program to run on the robots

#The IP and Port to receive from
recIP = "192.168.1.107"
recPort = 5555

#The IP and Port to send to position data
send2IP = "127.0.0.1"
send2Port = 5551

quar2pac = TheQuarterback()
quar2pac.setupListenSocket(recIP, recPort)
quar2pac.setupSendSocket(send2IP, send2Port)
quar2pac.displayCoordinates()

while True:
    quar2pac.retreiveCommands()
    if quar2pac.Disconnected == True:
        print "Done"
        break
    #quar2pac.updatePosition()
    #quar2pac.displayCoordinates()
