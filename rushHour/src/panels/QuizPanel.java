package rushHour.src.panels;

import rushHour.src.controllers.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.util.Random;
import java.io.File;

public class QuizPanel extends JPanel implements ActionListener{
	private String car[] = {"patrol", "ambulance", "fireengine"};
	private RushHourController ctrl;
	private int sec = 3;
	private int carType;

	//コンストラクタ
	public QuizPanel(){
		setLayout(null);
		setBounds(0, 0, 800, 455);
		showQuetion();
		chooseButton();
		soundButton();
		// 背景
		ImageIcon quizBackIcon = new ImageIcon(getClass().getResource("../../assets/images/quizParts/quizBack.png"));
		JLabel quizBackLabel = new JLabel(quizBackIcon);
		add(quizBackLabel);
		quizBackLabel.setBounds(0, 0, quizBackIcon.getIconWidth(), quizBackIcon.getIconHeight());
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*問題の提示*/
	private void showQuetion(){
		JLabel showQuiz = new JLabel("この車の音は何ですか？");
		add(showQuiz);
		showQuiz.setBounds(320, 125, 150, 50);
	}

	/*選択肢ボタンの表示*/
	private void chooseButton(){
		ImageIcon quizPatrolIcon = new ImageIcon(getClass().getResource("../../assets/images/quizParts/quizPatrol.png"));
		JButton button = new JButton(quizPatrolIcon);
		add(button);
		button.setBounds(63, 302, 197, 80);
		button.setActionCommand("0");
		button.addActionListener(this);

		ImageIcon quizRescueIcon = new ImageIcon(getClass().getResource("../../assets/images/quizParts/quizRescue.png"));
		JButton button2 = new JButton(quizRescueIcon);
		add(button2);
		button2.setBounds(301, 302, 197, 80);
		button2.setActionCommand("1");
		button2.addActionListener(this);

		ImageIcon quizFireIcon = new ImageIcon(getClass().getResource("../../assets/images/quizParts/quizFire.png"));
		JButton button3 = new JButton(quizFireIcon);
		add(button3);
		button3.setBounds(539, 302, 197, 80);
		button3.setActionCommand("2");
		button3.addActionListener(this);
	}

	/*問題の音を鳴らす*/
	private void soundButton(){
		ImageIcon quizSpeakerIcon = new ImageIcon(getClass().getResource("../../assets/images/quizParts/quizSpeaker.png"));
		JButton sound = new JButton(quizSpeakerIcon);
		add(sound);
		sound.setBounds(302, 122, 180, 135);
		sound.setActionCommand("7");
		sound.addActionListener(this);
	}

	//車の音の種類をランダムに決定
	public void setCarType() {
		Random ran = new Random();
		carType = ran.nextInt(3); //0 = パトカー, 1 = 救急車, 2 = 消防車
	}

	//正解か判断する
	private boolean checkAnswer(int command){
		if(command == carType) return true;
		else return false;
	}

	/*QuizPanelの処理*/
	public void actionPerformed(ActionEvent e) {
		int command = Integer.parseInt(e.getActionCommand()); //数値型への変換
		if(command == 0 || command == 1 || command == 2){
			if(checkAnswer(command)){
				try {
					AudioInputStream yes = AudioSystem.getAudioInputStream(getClass().getResource("../../assets/sounds/yes.wav")); //正解の音
					DataLine.Info di = new DataLine.Info(Clip.class, yes.getFormat());
					Clip clip = (Clip) AudioSystem.getLine(di);
					clip.open(yes);
					clip.start();
				} catch(Exception ex) {
				}
				setVisible(false);  //正解ならAnswerパネルに移る
				ctrl.changeAnswer();
			} else{
				try {
					AudioInputStream no = AudioSystem.getAudioInputStream(getClass().getResource("../../assets/sounds/no.wav")); //不正解の音
					DataLine.Info di = new DataLine.Info(Clip.class, no.getFormat());
					Clip clip = (Clip) AudioSystem.getLine(di);
					clip.open(no);
					clip.start();
				} catch(Exception ex) {
				}
			}
		} else if(command == 7){
			try {
				AudioInputStream carSound = AudioSystem.getAudioInputStream(getClass().getResource("../../assets/sounds/" + car[carType] + ".wav")); //乱数で決まった音
				DataLine.Info di = new DataLine.Info(Clip.class, carSound.getFormat());
				Clip clip = (Clip) AudioSystem.getLine(di);
				clip.open(carSound);
				clip.start();
			} catch(Exception ex) {
			}
		}
	}
}
