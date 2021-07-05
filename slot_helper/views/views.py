from app import app
from core.resourse_helper import get_slot_helper


@app.route('/open_slot/<slot_id>')
def boxes(slot_id: int):
    slot_helper = get_slot_helper()
    slot_helper.open_slot(slot_id)
    return 'success'
