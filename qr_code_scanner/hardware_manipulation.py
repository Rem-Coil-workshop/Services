import serial
from config import PORT, FREQUENCY


def listen_changes() -> str:
    with serial.Serial(PORT, FREQUENCY) as qr:
        data = b""
        current_symbol = b""
        while current_symbol != b'\r':
            data += current_symbol
            current_symbol = qr.read()

    return data.decode("utf-8")
