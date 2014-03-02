import Adafruit_BBIO.UART as UART
import serial
import time

UART.setup("UART1")
UART.setup("UART2")

ser1 = serial.Serial(port = "/dev/ttyO1", baudrate=9600)
ser2 = serial.Serial(port = "/dev/ttyO2", baudrate=9600)

a = 0
ser1.close()
ser1.open()
ser2.close()
ser2.open()

if ser1.isOpen():
	if ser2.isOpen():
		print "All is well"
		while True:
			a = raw_input('Input motor speed(0-255), type ramp, or q to quit ')
			if a == 'q':
				break
			elif a == 'ramp':
				s = int( raw_input('Max Speed(0-255)'))
				d = float(raw_input('Transition Delay(s)'))
				t = float(raw_input('Time at max(s)'))
				for x in range(0,s+1):
					ser2.write('S' +str(-1*x) + "\n\r")
					ser1.write('S' +str(x) + "\n\r")
					time.sleep(d)
				time.sleep(t)
				for l in range(0,s):
					ser2.write('S' + str(-1*(x-l)) + "\n\r")
					ser1.write('S' + str(x-l) + "\n\r")
					time.sleep(d)
			else:
				ser2.write('S' + str(-1*int(a)) + "\n\r")
				ser1.write('S' + a + "\n\r")

ser1.write('S0' + "\n\r")
ser1.close()
ser2.close()
print 'Done'
