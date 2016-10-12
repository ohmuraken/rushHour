package rushHour.src.panels;

import rushHour.src.controllers.*;
import rushHour.src.models.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AnswerPanel extends JPanel implements ActionListener{
	private RushHourModel mdl;
	private RushHourController ctrl;

	//コンストラクタ
	public AnswerPanel(RushHourModel mdl){
		this.mdl = mdl;
		setLayout(null);
		setBounds(0, 0, 800, 455);
		goNextButton(); //タイトルボタン
		goSelectButton(); //ステージセレクトボタン

		// 背景
		ImageIcon correctBackIcon = new ImageIcon(getClass().getResource("../../assets/images/answerParts/correctBack.png"));
		JLabel correctBackLabel = new JLabel(correctBackIcon);
		add(correctBackLabel);
		correctBackLabel.setBounds(0, 0, correctBackIcon.getIconWidth(), correctBackIcon.getIconHeight());
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*タイトルへ戻るボタン*/
	public void goSelectButton(){
		ImageIcon correctTitleIcn = new ImageIcon(getClass().getResource("../../assets/images/answerParts/clearedSelect.png"));
		JButton goTitle = new JButton(correctTitleIcn);
		add(goTitle);
		goTitle.setBounds(420, 338, 178, 64);
		goTitle.setActionCommand("0");
		goTitle.addActionListener(this);
	}

	/*ステージセレクトへ戻るボタン*/
	public void goNextButton(){
		ImageIcon correctNextIcn = new ImageIcon(getClass().getResource("../../assets/images/answerParts/clearedNext.png"));
		JButton goSelect = new JButton(correctNextIcn);
		add(goSelect);

		goSelect.setBounds(202, 338, 178, 64);
		goSelect.setActionCommand("1");
		goSelect.addActionListener(this);
	}

	/*AnswerPanelの処理*/
	public void actionPerformed(ActionEvent e) {
		int command = Integer.parseInt(e.getActionCommand()); //数値型への変換

		if(command == 0){
			setVisible(false);
			ctrl.changeSelect(); //タイトル画面へ
		} else if(command == 1){
			setVisible(false);
			ctrl.changePlay(mdl.getStageNumber()+1); //次のステージへ
		}
	}
}
