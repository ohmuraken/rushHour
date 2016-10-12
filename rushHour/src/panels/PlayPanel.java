package rushHour.src.panels;

import rushHour.src.controllers.*;
import rushHour.src.models.*;
import rushHour.src.others.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class PlayPanel extends JPanel implements ActionListener{
	private RushHourModel mdl;
	private RushHourController ctrl;
	private JButton goTitleButton, goSelectButton, restartButton;
	private JLabel playFieldImg;
	private JLabel timeLabel, minNum[] = new JLabel[2], minLabel, secNum[] = new JLabel[2], secLabel;
	private JLabel movesLabel, movesNum[] = new JLabel[3];
	private Draggable myCarImg;
	private Draggable carsImg[] = new Draggable[10];
	private JLabel barrierImg[] = new JLabel[5];
	private Timer timer;

	//コンストラクタ
	public PlayPanel(RushHourModel mdl){
		this.mdl = mdl;
		setLayout(null);
		setBounds(0, 0, 800, 455);
		timer = new Timer(1000, this);
		initSet();
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*各部品のセット*/
	public void initSet(){
		timer.start();
		mdl.readData();
		initGoTitle();
		initGoSelect();
		initRestart();
		initTimeAndMove();
		initBarriers();
		initCars();
		initMyCar();
		initPlayField();
	}

	/*タイトル画面へ行くボタンのセット*/
	private void initGoTitle(){
		ImageIcon titleIcn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playtotitle.png"));
		goTitleButton = new JButton(titleIcn);
		add(goTitleButton);
		goTitleButton.setBounds(580, 100, titleIcn.getIconWidth(), titleIcn.getIconHeight());
		goTitleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				int option = JOptionPane.showConfirmDialog(null, "<html>本当にタイトルに戻りますか？<br>戻ると現在のゲームは破棄されます。</html>", "選択", JOptionPane.YES_NO_OPTION); //選択ダイアログ
				if (option == JOptionPane.YES_OPTION){ //ダイアログでyesを押した場合
					timer.stop();
					setVisible(false); //このパネルを不可視化
					ctrl.changeTitle();
				}
			}
		});
	}

	/*セレクト画面へ行くボタンのセット*/
	private void initGoSelect(){
		ImageIcon selectIcn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playtoselect.png"));
		goSelectButton = new JButton(selectIcn);
		add(goSelectButton);
		goSelectButton.setBounds(580, 180, selectIcn.getIconWidth(), selectIcn.getIconHeight());
		goSelectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				int option = JOptionPane.showConfirmDialog(null, "<html>セレクト画面に戻りますか？<br>戻ると現在のゲームは破棄されます。</html>", "選択", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					timer.stop();
					setVisible(false);
					ctrl.changeSelect();
				}
			}
		});
	}

	/*再挑戦するボタン*/
	private void initRestart(){
		ImageIcon restartIcn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playtorestart.png"));
		restartButton = new JButton(restartIcn);
		add(restartButton);
		restartButton.setBounds(580, 260, restartIcn.getIconWidth(), restartIcn.getIconHeight());
		restartButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				int option = JOptionPane.showConfirmDialog(null, "<html>このステージを再挑戦しますか？</html>", "選択", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					timer.stop();
					setVisible(false);
					ctrl.changePlay(mdl.getStageNumber());
				}
			}
		});
	}

	/*駐車場のセット*/
	private void initPlayField(){
		ImageIcon icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/field.png"));
		playFieldImg = new JLabel(icn);
		playFieldImg.setBounds(0, 0, icn.getIconWidth(), icn.getIconHeight());
		add(playFieldImg);
	}

	/*障害物をセット*/
	private void initBarriers(){
		ImageIcon icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/barrier.gif"));;
		for(int i = 0; i < mdl.getNumOfBarriers(); i++){
			barrierImg[i] = new JLabel(icn);
			barrierImg[i].setBounds((180 + (50 * mdl.getBarrierPosition(i).x)), (80 + (50 * mdl.getBarrierPosition(i).y)), icn.getIconWidth(), icn.getIconHeight());
			add(barrierImg[i]);
		}
	}

	/*車のセット*/
	private void initCars(){
		ImageIcon icn;
		for(int i = 0; i < mdl.getNumOfCars() - 1; i++){
			icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/" + mdl.getCarKind(i) + mdl.getCarSize(i) + mdl.getCarDirection(i) + ".gif"));
			carsImg[i] = new Draggable(icn, mdl.getCarPosition(i).x, mdl.getCarPosition(i).y, i); //画像と初期位置を指定する
			add(carsImg[i]);
		}
	}

	/*駐車場から出す車のセット*/
	private void initMyCar(){
		ImageIcon icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/myCar.gif")); //この車の画像は決め打ち
		myCarImg = new Draggable(icn, 0, 2, mdl.getNumOfCars() - 1); //この車の位置は決め打ちでよい
		myCarImg.setInfos(ctrl, mdl);
		add(myCarImg);
		setLimits();
	}

	/*経過時間と移動回数を描画*/
	private void initTimeAndMove(){
		ImageIcon icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/time.png"));
		timeLabel = new JLabel(icn);
		timeLabel.setBounds(20, 30, icn.getIconWidth(), icn.getIconHeight());
		icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/move.png"));
		movesLabel = new JLabel(icn);
		movesLabel.setBounds(20, 50, icn.getIconWidth(), icn.getIconHeight());

		int x = 20 + movesLabel.getWidth();
		icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/0.png"));
		for(int i = 0; i < 3; i++){
			movesNum[i] = new JLabel(icn);
			movesNum[i].setBounds(x, 50, icn.getIconWidth(), icn.getIconHeight());
			add(movesNum[i]);
			x += icn.getIconWidth();
		}
		add(timeLabel);
		add(movesLabel);

		x = 20 + timeLabel.getWidth();
		for(int i = 0; i < 2; i++){
			minNum[i] = new JLabel(icn);
			minNum[i].setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
			add(minNum[i]);
			x += icn.getIconWidth();
		}
		icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/min.png"));
		minLabel = new JLabel(icn);
		minLabel.setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
		add(minLabel);
		x += icn.getIconWidth();
		icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/0.png"));
		for(int i = 0; i < 2; i++){
			secNum[i] = new JLabel(icn);
			secNum[i].setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
			add(secNum[i]);
			x += icn.getIconWidth();
		}
		icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/sec.png"));
		secLabel = new JLabel(icn);
		secLabel.setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
		add(secLabel);
	}

	/*移動回数を表示*/
	public void repaintMoves(){
		int x = 20 + movesLabel.getWidth();
		int num;
		ImageIcon icn;

		for(int i = 0; i < 3; i++){
			if(i == 0) num = mdl.getMoves() / 100 % 10;
			else if(i == 1) num = mdl.getMoves() / 10 % 10;
			else num = mdl.getMoves() % 10;
			icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/" + num + ".png"));
			movesNum[i].setIcon(icn);
			movesNum[i].setBounds(x, 50, icn.getIconWidth(), icn.getIconHeight());
			x += icn.getIconWidth();
		}
	}

	/*時間を再描画（1秒ごとに呼ばれる）*/
	public void repaintTime(){
		int time = mdl.getTime();
		int mm = time / 60;
		int ss = time % 60;
		int x = 20 + timeLabel.getWidth();
		int num;
		ImageIcon icn;

		for(int i = 0; i < 2; i++){
			if(i == 0) num = mm / 10 % 10;
			else num = mm % 10;
			icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/" + num + ".png"));
			minNum[i].setIcon(icn);
			minNum[i].setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
			x += icn.getIconWidth();
		}
		minLabel.setBounds(x, 30, minLabel.getWidth(), minLabel.getHeight());
		x += minLabel.getWidth();
		for(int i = 0; i < 2; i++){
			if(i == 0) num = ss / 10;
			else num = ss % 10;
			icn = new ImageIcon(getClass().getResource("../../assets/images/playParts/playNumbers/" + num + ".png"));
			secNum[i].setIcon(icn);
			secNum[i].setBounds(x, 30, icn.getIconWidth(), icn.getIconHeight());
			x += icn.getIconWidth();
		}
		secLabel.setBounds(x, 30, secLabel.getWidth(), secLabel.getHeight());
	}

	/*車の動ける範囲を設定する*/
	public void setLimits(){
	for(int i = 0; i < mdl.getNumOfCars() - 1; i++) carsImg[i].setMoveLimits();
		myCarImg.setMoveLimits();
	}

	/*クリア判定*/
	public void checkClear(){
		if(mdl.getCarPosition(mdl.getNumOfCars() - 1).x == 5){
			timer.stop();
			setVisible(false);
			ctrl.changeClear(); //クリアならクリアパネルを表示
		}
	}

	public void actionPerformed(ActionEvent e){
		ctrl.timerAction();
	}
}
