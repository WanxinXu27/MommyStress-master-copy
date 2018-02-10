from __future__ import print_function
import subprocess
import time


computer_time = int(round(time.time() * 1000))
phone_time = subprocess.check_output("adb shell echo ${EPOCHREALTIME}", shell=True)

phone_time = int(1000*float(phone_time.strip()))


print("Phone time: {} \nComputer time: {} \nOffset (Phone - Computer): {} \n".format(\
	phone_time, computer_time, phone_time - computer_time))