import Adafruit_BBIO.UART as UART
import serial
import time
import socket

theIP = "192.168.1.100"
thePort = 7070

theSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
theSocket.bind((theIP,thePort))
UART.setup("UART1")
UART.setup("UART2")

ser1 = serial.Serial(port = "/dev/ttyO1", baudrate=9600)
ser2 = serial.Serial(port = "/dev/ttyO2", baudrate=9600)

ser1.close()
ser1.open()
ser2.close()
ser2.open()

if ser1.isOpen() and  ser2.isOpen():
	print "All is well"
	while True:
            data, addr = theSocket.recvfrom(1024)
            print "Received: ", data
            ser1.write("S" +  data + "\n\r")
	    ser2.write("S" + str(-1*int(data)) + "\n\r") 
	    if data[0] == "D":
		    print "Done"
		    break
