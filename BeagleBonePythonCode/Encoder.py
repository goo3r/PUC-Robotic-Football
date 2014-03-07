#Encoder test

import Adafruit_BBIO.SPI as SPI

spi = SPI(0,0)

spi.open()

while True:
    print spi.readbytes(1)
