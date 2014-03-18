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
This class will simulate the base of each robot.
It provides functions as driving the motors,
collect encoder position data, etc.
'''
import socket
import math
import Adafruit_BBIO.UART as UART
import serial

class TheBase:
    #The X, Y, and Phi coordinates (X,Y,PHI) initially set to zero.
    Xcurrent = 0.0
    Ycurrent = 0.0
    PHIcurrent = 0.0

    #Length of wheel base (inches)
    lengthOfWheelBase = 15

    #These variabels keep track of the number of pulses received during a time interval
    LWPulses = 0
    RWPulses = 0

    #Set up the base motors
    #Ser1 is assuming to be the left motor
    #Ser2 is assuming to be the right motor
    UART.setup("UART1")
    UART.setup("UART2")
    ser1 = serial.Serial(port = "/dev/ttyO1", baudrate=9600)
    ser2 = serial.Serial(port = "/dev/ttyO2", baudrate=9600)
    ser1.close()
    ser2.close()
    ser1.open()
    ser2.open()

    #Server IP, port, and socket to send position data to
    serverIP = "127.0.0.1"
    serverPort = 4489
    Disconnected = False
    serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    #Socket to listen for commands from the server
    theSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    

    #Constructor
    #If for constructing the Quarterback, create the object without any parameters since it'll be the reference
    #If for any other robot, such as the Receiver, then type in the coordinates it'll start off with respect to the Quarterback
    def __init__(self, x = 0.0, y = 0.0, phi = 0.0):
        self.Xcurrent = x
        self.Ycurrent = y
        self.PHIcurrent = phi

    #Displays the robot's current coordinates
    def displayCoordinates(self):
        print self.Xcurrent
        print self.Ycurrent
        print self.PHIcurrent

    #This method will take a the speed data received from the socket
    #And from it, it will differentiate if it will go straight forward
    #Or backwards. It'll also know when to turn and it which direction
    #For going straight forward or backward, the data will contain a value
    #in the format of 'S255'. While turning, the format will be 'TS255'
    def driveBaseMotors(self, speedData):
        if self.ser1.isOpen() and self.ser2.isOpen():
	    if speedData[0] == "S":
                #Go straight backward or forward
                #Logic to drive motors, one is reversed which is right one
                i = 0
                while True:
                    if speedData[i] == "X":
                        break
                    else:
                        i += 1
                self.ser1.write(speedData[0:i] + "\n\r")
                self.ser2.write("S" + str(-1*int(speedData[i+1:])) + "\n\r" )
            elif speedData[0] == "T":
                #Turn robot according to how much the thumbstick is pushed
                #Logic to turn
                #Logic for turning clockwise
                if int(speedData[1:]) > 0:
                    self.ser1.write("S" + speedData[1:] + "\n\r")
                    self.ser2.write("S0 \n\r")
                else:
                    #Logic for turning counterclockwise
                    self.ser1.write("S0 \n\r")
                    self.ser2.write("S" + speedData[1:] + "\n\r")
        elif self.ser1.isOpen() and not self.ser2.isOpen():
            print "Ser1 is opened but not Ser2!"
        elif not self.ser1.isOpen() and self.ser2.isOpen():
            print "Ser2 is opened but not Ser1!"
        else:
            print "Both Ser1 and Ser2 are not opened!"

    #These next two method checks how many pulses are received at a certain time interval from the encoders
    #and will save the number of pulses to LWPulses and RWPulses variables
    def getLeftWheelPulses(self):
        #Try to get pulses from the Left encoder
        try:
            #Logic to do it, LWPulses = .....
            self.LWPulses = 5
            print "Got Left Wheel Pulse"
        except Exception as inst:
            print type(inst)
            print inst.args
            print inst
        else:
            #Anything else to do with encoder
            doen="Done"

    #For right wheel
    def getRightWheelPulses(self):
        #Try to get pulses from the Right encoder
        try:
            #Logic to do it, RWPulses = .....
            self.RWPulses = 5
            print "Got Right Wheel Pulse"
        except Exception as inst:
            print type(inst)
            print inst.args
            print inst
        else:
            #Anything else to do with encoder
            doen="Done"

    #This method will use the number of pulses received during a time interval (using LWPulses and RWPulses variables)
    #to update the position of the robot
    def updatePosition(self):
        #Delta Distance Right Wheel (dDRW), Delta Distance Left wheel (dDLW), and Delta Distance Center of Robot (dDCR) - (initially zero change)
        dDRW = 0.0
        dDLW = 0.0
        dDCR = 0.0
		
        #Get number of pulses for both left and right encoders and determine distance traveled		
        dDLW = 0.012 * self.LWPulses
        dDRW = 0.012 * self.RWPulses
	
        #Determine the distance traveled with respect to the center of the robot
        dDCR = (dDLW + dDRW)/2
		
        #Update orientation of robot first 
        self.PHIcurrent = self.PHIcurrent + (dDRW - dDLW) / self.lengthOfWheelBase
		
        #Then X and Y coordinates
        self.Xcurrent = self.Xcurrent + dDCR*math.sin(-self.PHIcurrent)
        self.Ycurrent = self.Ycurrent + dDCR*math.cos(self.PHIcurrent)
		
        #Clean up delta changes
        dDRW = dDLW = dDCR = 0

        #Testing
        self.Xcurrent += .0003
        self.Ycurrent += .0001
        self.PHIcurrent += .0002
		
        #Send new coordinates to server
        self.sendCoordinates()


    #This method will send the current (X,Y,PHI) coordinates to the server
    def sendCoordinates(self):
        #Logic to send coordinates
        coordinates = "{0:.4f}".format(self.Xcurrent) + "X" + "{0:.4f}".format(self.Ycurrent) + "X" + "{0:.4f}".format(self.PHIcurrent) + "X"
        self.serverSocket.sendto(coordinates, (self.serverIP, self.serverPort))
        
    #This method will set up the IP and port the robot will be listening from
    def setupListenSocket(self, ip, port):
        self.theSocket.bind((ip, port))

    def setupSendSocket(self, ip, port):
        self.serverIP = ip
        self.serverPort = port

    #This method will retreive the command from the server
    def retreiveCommands(self):
        data, addr = self.theSocket.recvfrom(32)
        if data[0] == "S" or data[0] == "T":
            self.driveBaseMotors(data)
        elif data[0] == "B":
            print "Button: ", data[1:]
        elif data[0] == "D":
            self.Disconnected = True
        else:
            print "Unknown data"

    
        
        

    
    
