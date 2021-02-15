package recordmacro;

public class KeyboardAction extends MacroAction{

	private int keyCode;
	
	public KeyboardAction(InputType inputType, int keyCode) {
		setActionType(ActionType.KEYBOARD); //매크로 유형
		setInputType(inputType); //눌렀는지 뗐는지
		
		this.keyCode = keyCode; //어떤 키 눌렀는지
	}

	public int getKeycode() { //키코드 반환
		return this.keyCode;
	}
	
}
