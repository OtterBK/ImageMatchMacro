package recordmacro;

public class MacroAction {

	private InputType inputType; //��������? ������?
	private ActionType actionType; //Ű����? ���콺?
	
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
	
	public InputType getInputType() {
		return this.inputType;
	}
	
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public InputType getActionType() {
		return this.inputType;
	}
}
