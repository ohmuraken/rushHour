package rushHour.src.controllers;

import rushHour.src.models.*;
import rushHour.src.panels.*;


public class RushHourController{
	private RushHourModel mdl;
	private TitlePanel  titlePnl;
	private StageSelectPanel  selectPnl;
	private PlayPanel  playPnl;
	private ClearPanel clearPnl;
	private QuizPanel  quizPnl;
	private AnswerPanel answerPnl;

	//コンストラクタ
	public RushHourController(RushHourModel mdl, TitlePanel titlePnl, StageSelectPanel selectPnl, PlayPanel playPnl, ClearPanel clearPnl,  QuizPanel quizPnl, AnswerPanel answerPnl){
		this.mdl = mdl;
		this.titlePnl = titlePnl;
		this.selectPnl = selectPnl;
		this.playPnl = playPnl;
		this.clearPnl = clearPnl;
		this.quizPnl = quizPnl;
		this.answerPnl = answerPnl;
	}

	//タイトル画面へ
	public void changeTitle(){
		titlePnl.setVisible(true);
	}

	//セレクト画面へ
	public void changeSelect(){
		selectPnl.setVisible(true);
		selectPnl.checkEnabled();
	}

	//プレイ画面へ
	public void changePlay(int stageNum){
		mdl.setStageNumber(stageNum);
		playPnl.setVisible(true);
		playPnl.removeAll(); //前のステージデータを削除する
		playPnl.repaint();   //再描画
		playPnl.initSet();//指定された番号のステージをセットする
		mdl.resetData();
	}

	/*クリア画面へ*/
	public void changeClear(){
		clearPnl.setClearScore();
		clearPnl.setVisible(true);
	}

	//クイズ画面へ
	public void changeQuiz(){
		quizPnl.setCarType();
		quizPnl.setVisible(true);
	}

	//解答画面へ
	public void changeAnswer() {
		answerPnl.setVisible(true);
	}

	/*移動回数を再描画*/
	public void repaintMoves(){
		playPnl.repaintMoves();
	}

	/*時間を再描画*/
	public void  timerAction(){
		mdl.addTime();
		playPnl.repaintTime();
	}

	/*マウスが離されると呼ばれる*/
	public void mouseReleased(int x, int y, int num){
		mdl.addMoveCount();            //移動回数を増やす
		mdl.setCarPosition(x, y, num); //車の新しい場所を記録
		playPnl.setLimits();           //それぞれの車の新しい移動範囲を設定
		playPnl.checkClear();          //クリアしているかチェック
	}
}
