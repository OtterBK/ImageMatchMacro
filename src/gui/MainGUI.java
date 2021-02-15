package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;

import addon.BubbleBorder;
import addon.DigitFilter;
import addon.MyColor;
import addon.RoundedButton;
import addon.ToggleSwitch;
import executer.Main;
import util.MyUtility;

public class MainGUI extends JFrame{
	
	JFrame frame;
	JScrollPane sc;
	JTextArea logArea;
	int frameWidth = 400;
	int frameHeight = 550;
	public RoundedButton btn_excute;
	private JLabel lbl_desc;
	private JLabel lbl_desc2;
	public JRadioButton autoAfk;
	public JRadioButton autoFishing;
	public JTextField tf_storeInterval;
	public JRadioButton itemTransfer;
	private JLabel lbl_storeInterval;
	private JPanel pane_afkMining;
	public JTextField tf_rgbSimilar;
	private JPanel pane_main;
	private JLabel lbl_isAfkfishing;
	public ToggleSwitch ts_isStore;
	private ImageIcon uncheckIcon = MyUtility.getEmptyIcon(20, MyColor.NAVY);
	private ImageIcon checkedIcon = MyUtility.getSelectedIcon(20, MyColor.CRIMSON, MyColor.NAVY);
	private ImageIcon pickAxeIcon = new ImageIcon(Main.iconFolder + "\\pickAxeIcon.png");
	private ImageIcon fishRodIcon = new ImageIcon(Main.iconFolder + "\\fishRodIcon.png");
	private ImageIcon transferIcon = new ImageIcon(Main.iconFolder + "\\chestIcon.png");
	private JLabel icon_autoAfk;
	private JLabel icon_fishRod;
	private JLabel icon_transfer;
	
	public MainGUI() {
		frame = this;
		
		AbstractBorder brdr = new BubbleBorder(MyColor.MIDNIGHTBLUE,1,16,0);
		
		pickAxeIcon = MyUtility.resizeImage(pickAxeIcon, 20, 20);
		fishRodIcon = MyUtility.resizeImage(fishRodIcon, 20, 20);
		transferIcon = MyUtility.resizeImage(transferIcon, 20, 20);
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		
		this.setIconImage(new ImageIcon(Main.macroImgFolder + "\\chestIcon.png").getImage()); 
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth/2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		frame.setTitle(Main.title); //프레임 제목 설정
		frame.setResizable(false); //프레임 크기 조정 불가능
		frame.setBackground(MyColor.MIDNIGHTBLUE);
		frame.getContentPane().setBackground(MyColor.DRIMGRAY);
		//this.setUndecorated(true); 윈도우 틀 없애기용, 사용안함
		
		logArea = new JTextArea();
		logArea.setFont(logArea.getFont().deriveFont(11f));
		logArea.setAutoscrolls(true);
		logArea.setEditable(false);
		logArea.setForeground(MyColor.WHITE); //글자색 설정
		logArea.setBackground(MyColor.SLATEGRAY);
		logArea.setLineWrap(true);   //꽉차면 다음줄로 가게 해줌.
		sc = new JScrollPane(logArea);
		sc.setSize(375, 200);
		sc.setBorder(new LineBorder(MyColor.BLACK, 2));
		sc.setLocation(5, 10);
		sc.setAutoscrolls(true);
		frame.getContentPane().add(sc, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 215, 375, 290);
		getContentPane().add(panel);
		panel.setBackground(MyColor.GAINSBORO);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(MyColor.BLACK, 2));
		
		btn_excute = new RoundedButton("매크로 실행");
		btn_excute.setBounds(40, 255, 300, 25);
		btn_excute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!Main.excuteScript()) return;
				//MyUtility.capture();
				btn_excute.setEnabled(false);
				btn_excute.setText("매크로 작동중...");
			}
		});
		btn_excute.setBackground(MyColor.NAVY);
		btn_excute.setForeground(MyColor.WHITE);
		btn_excute.setBorder(new LineBorder(MyColor.MIDNIGHTBLUE, 2));
		btn_excute.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panel.add(btn_excute);
		
		lbl_desc = new JLabel("");
		lbl_desc.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_desc.setText("매크로 실행버튼을 누른 뒤 3초안에 마인크래프트 창을 누르세요.");
		lbl_desc.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_desc.setForeground(MyColor.ORANGERED);
		lbl_desc.setBounds(2, 5, 370, 17);
		panel.add(lbl_desc);
		
		lbl_desc2 = new JLabel("종료하려면 [CAPS_LOCK] 키를 누르세요");
		lbl_desc2.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_desc2.setForeground(MyColor.ORANGERED);
		lbl_desc2.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_desc2.setBounds(50, 25, 300, 17);
		panel.add(lbl_desc2);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JLabel label = new JLabel("매크로 실행중에는 마인크래프트 창을 옮기지 마세요!!!");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(MyColor.ORANGERED);
		label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		label.setBounds(50, 45, 300, 17);
		panel.add(label);
		
		
		JLabel lbl_descRgbSimilar = new JLabel("RGB 인식 오차값");
		lbl_descRgbSimilar.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_descRgbSimilar.setBounds(10, 85, 105, 15);
		panel.add(lbl_descRgbSimilar);
		lbl_descRgbSimilar.setForeground(MyColor.PLUSIANBLUE);
		lbl_descRgbSimilar.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		tf_rgbSimilar = new JTextField();
		tf_rgbSimilar.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_rgbSimilar.setBounds(120, 83, 46, 21);
		panel.add(tf_rgbSimilar);
		tf_rgbSimilar.setColumns(3);
		tf_rgbSimilar.setText("7");
		tf_rgbSimilar.setBackground(MyColor.WHEET);
		tf_rgbSimilar.setForeground(Color.black);
		tf_rgbSimilar.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		tf_rgbSimilar.setBorder(new LineBorder(MyColor.WHITE, 2));	
		
		pane_main = new JPanel();
		pane_main.setBounds(10, 111, 360, 140);
		panel.add(pane_main);
		pane_main.setLayout(null);
		pane_main.setBackground(MyColor.GAINSBORO);
		pane_main.setOpaque(false);
		
		JPanel pane_transfer = new JPanel();
		pane_transfer.setBounds(10, 100, 340, 33);
		pane_main.add(pane_transfer);
		pane_transfer.setBackground(MyColor.TULIPNUWA);
		pane_transfer.setLayout(null);
		pane_transfer.setBorder(brdr);
		itemTransfer = new JRadioButton("운송");
		itemTransfer.setIcon(uncheckIcon);
		itemTransfer.setSelectedIcon(checkedIcon);
		itemTransfer.setForeground(MyColor.WHITE);
		itemTransfer.setHorizontalAlignment(SwingConstants.LEFT);
		itemTransfer.setBounds(8, 6, 60, 23);
		itemTransfer.setOpaque(false);
		itemTransfer.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		buttonGroup.add(itemTransfer);
		pane_transfer.add(itemTransfer);
		
		icon_transfer = new JLabel(transferIcon);
		icon_transfer.setBounds(65, 8, 20, 20);
		icon_transfer.setBorder(new LineBorder(MyColor.BLACK, 2));
		pane_transfer.add(icon_transfer);
		
		pane_afkMining = new JPanel();
		pane_afkMining.setBounds(10, 0, 340, 33);
		pane_main.add(pane_afkMining);
		pane_afkMining.setBackground(MyColor.TULIPNUWA);
		pane_afkMining.setBorder(brdr);
		pane_afkMining.setLayout(null);
		autoAfk = new JRadioButton("잠광");
		autoAfk.setForeground(MyColor.WHITE);
		autoAfk.setHorizontalAlignment(SwingConstants.LEFT);
		autoAfk.setBounds(8, 6, 60, 20);
		autoAfk.setOpaque(false);
		autoAfk.setIcon(uncheckIcon);
		autoAfk.setSelectedIcon(checkedIcon);
		autoAfk.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		pane_afkMining.add(autoAfk);
		autoAfk.setSelected(true);
		
		buttonGroup.add(autoAfk);
		
		icon_autoAfk = new JLabel(pickAxeIcon);
		icon_autoAfk.setBounds(65, 8, 20, 20);
		icon_autoAfk.setBorder(new LineBorder(MyColor.BLACK, 2));
		pane_afkMining.add(icon_autoAfk);
		
		JPanel pane_fishing = new JPanel();
		pane_fishing.setBounds(10, 40, 340, 55);
		pane_main.add(pane_fishing);
		pane_fishing.setLayout(null);
		pane_fishing.setBackground(MyColor.TULIPNUWA);
		pane_fishing.setBorder(brdr);
		autoFishing = new JRadioButton("낚시");
		autoFishing.setIcon(uncheckIcon);
		autoFishing.setSelectedIcon(checkedIcon);
		autoFishing.setForeground(MyColor.WHITE);
		autoFishing.setHorizontalAlignment(SwingConstants.LEFT);
		autoFishing.setBounds(8, 5, 60, 23);
		autoFishing.setOpaque(false);
		
		autoFishing.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		pane_fishing.add(autoFishing);
		buttonGroup.add(autoFishing);
		
		tf_storeInterval = new JTextField();
		tf_storeInterval.setBounds(300, 6, 28, 21);
		pane_fishing.add(tf_storeInterval);
		tf_storeInterval.setColumns(2);
		tf_storeInterval.setHorizontalAlignment(SwingConstants.RIGHT);
		((AbstractDocument) tf_storeInterval.getDocument()).setDocumentFilter(new DigitFilter());
		tf_storeInterval.setText("25");
		tf_storeInterval.setBackground(MyColor.WHEET);
		tf_storeInterval.setForeground(Color.black);
		tf_storeInterval.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		tf_storeInterval.setBorder(new LineBorder(MyColor.WHITE, 2));	
		
		lbl_storeInterval = new JLabel("창고 저장간격");
		lbl_storeInterval.setForeground(MyColor.WHITE);
		lbl_storeInterval.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_storeInterval.setBounds(210, 9, 85, 15);
		lbl_storeInterval.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		pane_fishing.add(lbl_storeInterval);
		//autoAfk.requestFocus();
		
		ts_isStore = new ToggleSwitch();
		ts_isStore.setSize(40, 15);
		ts_isStore.setLocation(15, 32);
		pane_fishing.add(ts_isStore);
		
		lbl_isAfkfishing = new JLabel("잠수 낚시");
		lbl_isAfkfishing.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_isAfkfishing.setBounds(60, 30, 57, 15);
		lbl_isAfkfishing.setForeground(MyColor.WHITE);
		lbl_isAfkfishing.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		pane_fishing.add(lbl_isAfkfishing);
		
		icon_fishRod = new JLabel(fishRodIcon);
		icon_fishRod.setBounds(65, 8, 20, 20);
		icon_fishRod.setBorder(new LineBorder(MyColor.BLACK, 2));
		pane_fishing.add(icon_fishRod);
		
		
		
		frame.setVisible(true); //프레임 표시
	}

	public void printLog(String log) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "[yy년MM월dd일 HH:mm:ss] "); //시간
		String format_time1 = format1.format(System.currentTimeMillis());
		logArea.append(format_time1+log+"\n"); //출력
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
}
