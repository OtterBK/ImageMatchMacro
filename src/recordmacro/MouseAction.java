package recordmacro;

public class MouseAction extends MacroAction{

	private int buttonCode; //���콺 ��ư �ڵ�
	
	public MouseAction(InputType inputType, int buttonCode) {
		setActionType(ActionType.MOUSE); //��ũ�� ����
		setInputType(inputType); //�������� �ô���
		
		this.buttonCode = buttonCode; //� ��ư ��������
	}

	public int getKeycode() { //��ư �ڵ� ��ȯ
		return this.buttonCode;
	}
	
}
