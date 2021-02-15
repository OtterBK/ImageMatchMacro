package recordmacro;

public class KeyboardAction extends MacroAction{

	private int keyCode;
	
	public KeyboardAction(InputType inputType, int keyCode) {
		setActionType(ActionType.KEYBOARD); //��ũ�� ����
		setInputType(inputType); //�������� �ô���
		
		this.keyCode = keyCode; //� Ű ��������
	}

	public int getKeycode() { //Ű�ڵ� ��ȯ
		return this.keyCode;
	}
	
}
