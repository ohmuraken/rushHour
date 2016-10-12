package rushHour.src.panels;

import rushHour.src.controllers.*;
import rushHour.src.models.*;

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
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URISyntaxException;

public class ClearPanel extends JPanel implements ActionListener{
	private RushHourModel mdl;
	private RushHourController ctrl;
	private JButton clearGoTitleButton, clearGoSelectButton, clearRestartButton, clearGoQuizButton, clearGoNextStageButton;
	private JLabel scoreNum[] = new JLabel[5];
	private JLabel stageSelectBackLabel;

	public ClearPanel(RushHourModel mdl){
		this.mdl = mdl;
		setLayout(null);
		setBounds(0, 0, 800, 455);
		initClearSet();
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*固定部品のセット*/
	private void initClearSet(){
		ImageIcon clearedTitleIcn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearedTitle.png"));
		clearGoTitleButton = new JButton(clearedTitleIcn);
		clearGoTitleButton.setBounds(414, 338+25, 180, 65);
		add(clearGoTitleButton);
		clearGoTitleButton.setActionCommand("0");
		clearGoTitleButton.addActionListener(this);

		ImageIcon clearedRetryIcn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearedRetry.png"));
		clearRestartButton = new JButton(clearedRetryIcn);
		clearRestartButton.setBounds(311, 247+25, 180, 65);
		add(clearRestartButton);
		clearRestartButton.setActionCommand("1");
		clearRestartButton.addActionListener(this);

		ImageIcon clearedNextIcn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearedNext.png"));
		clearGoNextStageButton = new JButton(clearedNextIcn);
		clearGoNextStageButton.setBounds(518, 246+25, 180, 65);
		add(clearGoNextStageButton);
		clearGoNextStageButton.setActionCommand("2");
		clearGoNextStageButton.addActionListener(this);

		ImageIcon clearedSelectIcn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearedSelect.png"));
		clearGoSelectButton = new JButton(clearedSelectIcn);
		clearGoSelectButton.setBounds(207, 338+25, 180, 65);
		add(clearGoSelectButton);
		clearGoSelectButton.setActionCommand("3");
		clearGoSelectButton.addActionListener(this);

		ImageIcon clearedQuizIcn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearedQuiz.png"));
		clearGoQuizButton = new JButton(clearedQuizIcn);
		clearGoQuizButton.setBounds(104, 246+25, 180, 65);
		add(clearGoQuizButton);
		clearGoQuizButton.setActionCommand("4");
		clearGoQuizButton.addActionListener(this);

		// 背景
		ImageIcon stageSelectBackIcon = new ImageIcon(getClass().getResource("../../assets/images/clearParts/clearBack.png"));
		stageSelectBackLabel = new JLabel(stageSelectBackIcon);
		stageSelectBackLabel.setBounds(0, 0, stageSelectBackIcon.getIconWidth(), stageSelectBackIcon.getIconHeight());
	}

	/*スコアのセット*/
	public void setClearScore(){
		int times = mdl.getTime();  //クリア時の経過時間を取得する
		int score = 25000 + (5077 * mdl.getStageNumber()); //スコアの基礎点
		score = score - ((mdl.getMoves() * 1013 / mdl.getStageNumber()) + (times * 1979 / mdl.getStageNumber()));  //スコアの計算（減点方式）
		if(score < 0) score = 0;
		int num;
		ImageIcon icn;
		int x = 200;
		for(int i = 0; i < 5; i++){
			if(i == 0) num = score / 10000;
			else if(i == 1) num = score / 1000 % 10;
			else if(i == 2) num = score / 100 % 10;
			else if(i == 3) num = score / 10 % 10;
			else num = score % 10;
			icn = new ImageIcon(getClass().getResource("../../assets/images/clearParts/scoreNumbers/" + num + ".png"));
			scoreNum[i] = new JLabel(icn);
			scoreNum[i].setBounds(x, 150, icn.getIconWidth(), icn.getIconHeight());
			add(scoreNum[i]);
			x += icn.getIconWidth();
		}
		add(stageSelectBackLabel);
		try{
			InputStream is = getClass().getResourceAsStream("../../assets/stages/.clear.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			int cleared = Integer.parseInt(br.readLine());
			if(cleared == mdl.getStageNumber() && mdl.getStageNumber() != 6){
				try{
					URL url = getClass().getResource("../../assets/stages/.clear.txt");
					File file = new File(url.toURI());
					FileWriter filewriter = new FileWriter(file);
					filewriter.write(String.valueOf(mdl.getStageNumber() + 1));
					filewriter.close();
				}catch(IOException e){
				}catch(URISyntaxException e){
				}
			}
			is.close();
			isr.close();
			br.close();
		}catch(IOException e){
		}
	}

	public void actionPerformed(ActionEvent e){
		setVisible(false);
		int command = Integer.parseInt(e.getActionCommand());

		for(int i = 0; i < 5; i++) remove(scoreNum[i]);
		remove(stageSelectBackLabel);

		if(command == 0) ctrl.changeTitle();
		else if(command == 1) ctrl.changePlay(mdl.getStageNumber());
		else if(command == 2){
			if(mdl.getStageNumber() < 6) ctrl.changePlay(mdl.getStageNumber()+1);
			else ctrl.changeSelect();
		}
		else if(command == 3) ctrl.changeSelect();
		else ctrl.changeQuiz();
	}
}
