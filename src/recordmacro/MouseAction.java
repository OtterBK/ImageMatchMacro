package recordmacro;

public class MouseAction extends MacroAction{

	private int buttonCode; //마우스 버튼 코드
	
	public MouseAction(InputType inputType, int buttonCode) {
		setActionType(ActionType.MOUSE); //매크로 유형
		setInputType(inputType); //눌렀는지 뗐는지
		
		this.buttonCode = buttonCode; //어떤 버튼 눌렀는지
	}

	public int getKeycode() { //버튼 코드 반환
		return this.buttonCode;
	}
	
}
