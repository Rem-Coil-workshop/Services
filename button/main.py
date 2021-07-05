import traceback

import RPi.GPIO as GPIO
import requests

from config import BUTTON_PIN, BUTTON_URL


GPIO.setmode(GPIO.BOARD)
GPIO.setup(BUTTON_PIN, GPIO.IN, pull_up_down=GPIO.PUD_UP)


def read_button():
    button_state = GPIO.input(BUTTON_PIN)
    if not button_state:
        r = requests.get(BUTTON_URL)
        print(r.content)


if __name__ == '__main__':
    while True:
        try:
            read_button()
        except Exception:
            traceback.print_exc()
