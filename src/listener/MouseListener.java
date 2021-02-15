package listener;


import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import executer.Main;
import util.MyUtility;

public class MouseListener implements NativeMouseInputListener {
	
	public int nowY = 0;
	public boolean isRightClick = false;
	
	public void nativeMouseClicked(NativeMouseEvent e) {
		//System.out.println("\n\n\n\n\n\n---- X: " + e.getX()+" | Y: "+e.getY() +"-------\n\n\n\n\n\n");
		//¤±-CheckGUI cg = new CheckGUI("x:"+e.getX()+"|y:"+e.getY());
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		//MyUtility.printLog(Main.script.center.x + ", " + Main.script.center.y);
		//MyUtility.printLog(arg0.getX() + ", " + arg0.getY());
		
		if(arg0.getButton() == MouseEvent.BUTTON2) {
			isRightClick = true;
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getButton() == MouseEvent.BUTTON2) {
			isRightClick = false;
		}
		
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		nowY = arg0.getY();
	}
}