package recordmacro;

public class WaitAction extends MacroAction{

	private long waitTime; //기다리는 시간
	
	public WaitAction(long waitTime) {
		setActionType(ActionType.WAIT); //매크로 유형
		setInputType(InputType.WAIT); //기다리는 유형임
		
		this.waitTime = waitTime; //어떤 버튼 눌렀는지
	}

	public long getTime() { //기다리는 시간 반환
		return this.waitTime;
	}
	
}
