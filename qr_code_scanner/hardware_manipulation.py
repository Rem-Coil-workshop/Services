import evdev
from config import PORT

device = None


def set_up():
    global device
    device = evdev.InputDevice(PORT)


def listen_changes() -> str:
    global device
    code = []
    data = ""

    for event in device.read_loop():
        if event.type == evdev.ecodes.EV_KEY and event.value == 1:
            e_code = event.code - 1
            if 1 <= e_code <= 10:
                digit = 0
                if e_code != 10:
                    digit = e_code
                code.append(digit)
            elif e_code == 52:
                code.append('/')
            elif e_code == 27:
                data = data.join(code)

    return data
