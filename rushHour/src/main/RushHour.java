package rushHour.src.main;

import rushHour.src.panels.*;
import rushHour.src.controllers.*;
import rushHour.src.models.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RushHour extends JFrame{
	private RushHourModel mdl;
	private TitlePanel titlePnl;
	private StageSelectPanel selectPnl;
	private PlayPanel playPnl;
	private ClearPanel clearPnl;
	private QuizPanel quizPnl;
	private AnswerPanel ansPnl;
	private RushHourController ctrl;

	private void run(){
		//モデルの設定
		mdl = new RushHourModel();

		//各パネルの設定
		titlePnl = new TitlePanel();
		selectPnl = new StageSelectPanel();
		playPnl = new PlayPanel(mdl);
		clearPnl = new ClearPanel(mdl);
		quizPnl = new QuizPanel();
		ansPnl = new AnswerPanel(mdl);

		//コントローラーの設定
		ctrl = new RushHourController(mdl, titlePnl, selectPnl, playPnl, clearPnl, quizPnl, ansPnl);
		titlePnl.setCtrl(ctrl);
		selectPnl.setCtrl(ctrl);
		playPnl.setCtrl(ctrl);
		clearPnl.setCtrl(ctrl);
		quizPnl.setCtrl(ctrl);
		ansPnl.setCtrl(ctrl);
		mdl.setCtrl(ctrl);

		//各パネルをフレームに追加
		//タイトル以外を見えないようにする
		getContentPane().add(titlePnl);
		getContentPane().add(selectPnl);
		getContentPane().add(playPnl);
		getContentPane().add(clearPnl);
		getContentPane().add(quizPnl);
		getContentPane().add(ansPnl);
		selectPnl.setVisible(false);
		playPnl.setVisible(false);
		clearPnl.setVisible(false);
		quizPnl.setVisible(false);
		ansPnl.setVisible(false);

		//フレームの設定
		getContentPane().setLayout(null);
		setBounds(100, 100, 800, 500);
		setResizable(false);
		makeMenuBer(); //メニューバーを作る
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	//メニューバーの設定
	private void makeMenuBer(){
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("ゲーム");

		//メニューに表示するものを設定
		JMenuItem titleItem = new JMenuItem("タイトルへ");
		titleItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				titlePnl.setVisible(false);
				selectPnl.setVisible(false);
				playPnl.setVisible(false);
				clearPnl.setVisible(false);
				quizPnl.setVisible(false);
				ansPnl.setVisible(false);
				ctrl.changeTitle();
			}
		});
		menu.add(titleItem);

		JMenuItem quititem = new JMenuItem("終了");
		quititem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				System.exit(0);
			}
		});
		menu.add(quititem);

		bar.add(menu);
		setJMenuBar(bar);
	}

	public static void main(String argd[]){
		RushHour play = new RushHour();
		play.run();
	}
}
