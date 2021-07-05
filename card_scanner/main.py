import json
import time
import traceback

import requests
import serial

from config import CARD_URL, PORT, FREQUENCY, HEADERS


def read_card_number():
    code = get_card_number()
    data = {'card': code}
    requests.post(CARD_URL, data=json.dumps(data), headers=HEADERS)
    time.sleep(5)


def get_card_number() -> str:
    with serial.Serial(PORT, FREQUENCY) as card:
        data = b''
        current_byte = b''
        while current_byte != b'\x03':
            if current_byte != b'\x02':
                data += current_byte
            current_byte = card.read()

    data = ""
    return str(int(data[2:-2], 16))


if __name__ == '__main__':
    while True:
        try:
            read_card_number()
        except Exception:
            traceback.print_exc()
