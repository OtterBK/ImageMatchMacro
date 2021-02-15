package recordmacro;

public class WaitAction extends MacroAction{

	private long waitTime; //��ٸ��� �ð�
	
	public WaitAction(long waitTime) {
		setActionType(ActionType.WAIT); //��ũ�� ����
		setInputType(InputType.WAIT); //��ٸ��� ������
		
		this.waitTime = waitTime; //� ��ư ��������
	}

	public long getTime() { //��ٸ��� �ð� ��ȯ
		return this.waitTime;
	}
	
}
