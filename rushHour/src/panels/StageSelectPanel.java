package rushHour.src.panels;

import rushHour.src.controllers.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StageSelectPanel extends JPanel implements ActionListener{
	private RushHourController ctrl;
	private JButton stageButton[] = new JButton[6];

	/*コンストラクタ*/
	public StageSelectPanel(){
		setLayout(null);
		setBounds(0, 0, 800, 455);
		//ステージ選択ボタンをセットする
		for(int i = 0; i < 6; i++){
			ImageIcon icon = new ImageIcon(getClass().getResource("../../assets/images/stageSelectParts/no" + (i+1) +  ".png")); //ステージ選択ボタン
			stageButton[i] = new JButton(icon);
			add(stageButton[i]);
			int x = 110 + 230 * (i % 3);
			int y = 150 + 150 * (i / 3);
			stageButton[i].setBounds(x, y, 120, 120);
			stageButton[i].setActionCommand(String.valueOf(i+1));
			stageButton[i].addActionListener(this);
		}
		// 背景
		ImageIcon stageSelectBackIcon = new ImageIcon(getClass().getResource("../../assets/images/stageSelectParts/stageSelectBack.png"));
		JLabel stageSelectBackLabel = new JLabel(stageSelectBackIcon);
		add(stageSelectBackLabel);
		stageSelectBackLabel.setBounds(0, 0, stageSelectBackIcon.getIconWidth(), stageSelectBackIcon.getIconHeight());
		checkEnabled(); //未クリアのボタンを押せないようにする
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*未クリアのボタンを選択できないようにする*/
	public void checkEnabled(){
		for(int i = 0; i < 6; i++){
			try{
				InputStream is = getClass().getResourceAsStream("../../assets/stages/.clear.txt");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				int cleared = Integer.parseInt(br.readLine());
				if(i+1 > cleared) stageButton[i].setEnabled(false);
				else stageButton[i].setEnabled(true);
				is.close();
				isr.close();
				br.close();
			} catch(IOException e){
			}
		}
	}

	/*プレイ画面に移動する
	 *押されたボタンによってステージ番号が異なる*/
	public void actionPerformed(ActionEvent ev){
		int num = Integer.parseInt(ev.getActionCommand());
		setVisible(false); //このパネルを不可視化
		ctrl.changePlay(num); //Play画面に移ります
	}
}
