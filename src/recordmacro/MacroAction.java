package recordmacro;

public class MacroAction {

	private InputType inputType; //누른거임? 뗀거임?
	private ActionType actionType; //키보드? 마우스?
	
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
