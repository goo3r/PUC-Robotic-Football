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

#Setup sending and receiving threads
qbSend = TheQuarterback()
qbReceive = TheQuarterback()

#Setup send thread, by default the thread is set t o "Receive"
qbSend.job = "Send"

#Start the threads
qbReceive.start()
qbSend.start()

while True:
    if !qbReceive.isAlive():
        qbSend.Done = True

if !qbReceive.isAlive() and !qbSend.isAlive():
    print "Both threads terminated successfully"
elif !qbReceive.isAlive() and qbSend.isAlive():
    print "Still sending, will attempt to terminate"
    qbSend.Done = True
elif qbReceive.isAlive() and qbSend.isAlive():
    print "WTF?!"

#quar2pac = TheQuarterback()
#quar2pac.setupListenSocket(recIP, recPort)
#quar2pac.setupSendSocket(send2IP, send2Port)
#quar2pac.displayCoordinates()

#quar2pac.start()

#while True:
    #quar2pac.retreiveCommands()
    #if quar2pac.Disconnected == True:
        #print "Done"
        #break
    #quar2pac.updatePosition()
    #quar2pac.displayCoordinates()
