import serial
from config import PORT, FREQUENCY


def listen_changes() -> int:
    with serial.Serial(PORT, FREQUENCY) as card:
        data = b''
        current_byte = b''
        while current_byte != b'\x03':
            if current_byte != b'\x02':
                data += current_byte
            current_byte = card.read()

    data = ""
    return int(data[2:-2], 16)
