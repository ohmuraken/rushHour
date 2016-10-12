package rushHour.src.others;

import rushHour.src.controllers.*;
import rushHour.src.models.*;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

public class Draggable extends JLabel{
	private static RushHourModel mdl;
	private static RushHourController ctrl;
	private int maxX, minX, maxY, minY; //このラベルの最大可動域（自分の大きさを加味した分）
	private int carNumber;
	private boolean soundFlag;
	/* * * * * * マス目の大きさと駐車場の上下左右の限界値 * * * * * */
	private static final int BLOCK_WIDTH = 50, BLOCK_HEIGHT = 50;
	private static final int LIMIT_OF_LEFT = 180, LIMIT_OF_RIGHT = 470;
	private static final int LIMIT_OF_TOP = 80, LIMIT_OF_BOTTOM = 370;

	/*最大可動域の各ゲッター*/
	public int getMaxX(){return maxX;}
	public int getMinX(){return minX;}
	public int getMaxy(){return maxY;}
	public int getMinY(){return minY;}

	/*コンストラクタ*/
	public Draggable(ImageIcon icn, int initX, int initY, int carNumber){
		super(icn); //画像で表示
		this.carNumber = carNumber;
		setBounds(LIMIT_OF_LEFT + (initX * BLOCK_WIDTH), LIMIT_OF_TOP + (initY * BLOCK_HEIGHT), icn.getIconWidth(), icn.getIconHeight()); //初期位置
		//マウスリスナーの設定
		DragLitener dragLister = new DragLitener(this);
		addMouseListener(dragLister);
		addMouseMotionListener(dragLister);
	}

	public void setInfos(RushHourController ctrl, RushHourModel mdl){
		this.ctrl = ctrl;
		this.mdl = mdl;
	}

	/*車の動ける範囲を設定する*/
	public void setMoveLimits(){
		//可動域の設定(仮)
		int myCarSize = mdl.getCarSize(carNumber);
		int myCarDirection = mdl.getCarDirection(carNumber);
		Point myCarPosition = mdl.getCarPosition(carNumber);

		if(myCarDirection == 0){ //動かす車が横長
			maxX = LIMIT_OF_RIGHT - getWidth();         //駐車場の右端
			minX = LIMIT_OF_LEFT;                       //駐車場の左端
			maxY = minY = LIMIT_OF_TOP + (myCarPosition.y * BLOCK_HEIGHT); //Y軸方向は固定
			for(int i = 0; i < mdl.getNumOfCars(); i++){ //全車に対して当たり判定を行う
				if(i == carNumber) continue;         //自分は飛ばす
				int otherCarSize = mdl.getCarSize(i);
				int otherCarDirection = mdl.getCarDirection(i);
				Point otherCarPosition = mdl.getCarPosition(i);
				if(otherCarDirection == 1){ //当たり判定を行う車が縦長(この車と異なる向き)
					//この車の高さと同じ高さに当たり判定対象の車が被っている
					if(otherCarPosition.y <= myCarPosition.y && myCarPosition.y < (otherCarPosition.y + otherCarSize)){
						if(myCarPosition.x < otherCarPosition.x){ //この車が当たり判定対象の車より左にある場合
							int newLimit = LIMIT_OF_LEFT + ((otherCarPosition.x - myCarSize) * BLOCK_WIDTH);
							maxX = maxX >= newLimit ? newLimit : maxX; //現在の上限より小さい場合はそこを上限にする
						}
						else{                                     //この車が当たり判定対象の車より右にある場合
							int newLimit = LIMIT_OF_LEFT + ((otherCarPosition.x + 1) * BLOCK_WIDTH);
							minX = minX <= newLimit ? newLimit : minX; //現在の下限より大きい場合はそこを下限にする
						}
					}
				} else{                     //当たり判定を行う車が横長(この車と同じ向き)
					if(myCarPosition.y == otherCarPosition.y){ //同じ高さに存在する場合(壁□□  □□壁　こういう状況)
						if(myCarPosition.x < otherCarPosition.x){ //この車が当たり判定対象の車より左にある場合
							int newLimit = LIMIT_OF_LEFT + ((otherCarPosition.x - myCarSize) * BLOCK_WIDTH);
							maxX = maxX >= newLimit ? newLimit : maxX; //現在の上限より小さい場合はそこを上限にする
						}
						else{                                     //この車が当たり判定対象の車より右にある場合
							int newLimit  = LIMIT_OF_LEFT + ((otherCarPosition.x + otherCarSize) * BLOCK_WIDTH);
							minX = minX <= newLimit ? newLimit : minX; //現在の下限より大きい場合はそこを下限にする
						}
					}
				}
			}
			for(int i = 0; i < mdl.getNumOfBarriers(); i++){
				Point barrierPosition = mdl.getBarrierPosition(i);
				if(myCarPosition.y == barrierPosition.y){
					if(myCarPosition.x < barrierPosition.x){
						int newLimit = LIMIT_OF_LEFT + ((barrierPosition.x - myCarSize) * BLOCK_WIDTH);
						maxX = maxX >= newLimit ? newLimit : maxX;
					}
					else{
						int newLimit  = LIMIT_OF_LEFT + ((barrierPosition.x + 1) * BLOCK_WIDTH);
						minX = minX <= newLimit ? newLimit : minX;
					}
				}
			}
		} else{ //動かす車が縦長
			maxX = minX = LIMIT_OF_LEFT + (myCarPosition.x * BLOCK_WIDTH); //X軸方向は固定
			maxY = LIMIT_OF_BOTTOM - getHeight();         //駐車場の一番下
			minY = LIMIT_OF_TOP;                          //駐車場の一番上
			for(int i = 0; i < mdl.getNumOfCars(); i++){ //全車に対して当たり判定を行う 以下は上記の構造とほぼ同じ
				if(i == carNumber) continue;
				int otherCarSize = mdl.getCarSize(i);
				int otherCarDirection = mdl.getCarDirection(i);
				Point otherCarPosition = mdl.getCarPosition(i);
				if(otherCarDirection == 0){
					if(otherCarPosition.x <= myCarPosition.x && myCarPosition.x < (otherCarPosition.x + otherCarSize)){
						if(myCarPosition.y < otherCarPosition.y){
							int newLimit = LIMIT_OF_TOP + ((otherCarPosition.y - myCarSize) * BLOCK_HEIGHT);
							maxY = maxY >= newLimit ? newLimit : maxY;
						}
						else{
							int newLimit = LIMIT_OF_TOP + ((otherCarPosition.y + 1) * BLOCK_HEIGHT);
							minY = minY <= newLimit ? newLimit : minY;
						}
					}
				} else{
					if(myCarPosition.x == otherCarPosition.x){
						if(myCarPosition.y < otherCarPosition.y){
							int newLimit = LIMIT_OF_TOP + ((otherCarPosition.y - myCarSize) * BLOCK_HEIGHT);
							maxY = maxY >= newLimit ? newLimit : maxY;
						}
						else{
							int newLimit = LIMIT_OF_TOP + ((otherCarPosition.y + otherCarSize) * BLOCK_HEIGHT);
							minY = minY <= newLimit ? newLimit : minY;
						}
					}
				}
			}
			for(int i = 0; i < mdl.getNumOfBarriers(); i++){
				Point barrierPosition = mdl.getBarrierPosition(i);
				if(myCarPosition.x == barrierPosition.x){
					if(myCarPosition.y < barrierPosition.y){
						int newLimit = LIMIT_OF_TOP + ((barrierPosition.y - myCarSize) * BLOCK_HEIGHT);
						maxY = maxY >= newLimit ? newLimit : maxY;
					}
					else{
						int newLimit  = LIMIT_OF_TOP + ((barrierPosition.y + 1) * BLOCK_HEIGHT);
						minY = minY <= newLimit ? newLimit : minY;
					}
				}
			}
		}
		if(carNumber == (mdl.getNumOfCars() - 1))
			if(maxX == LIMIT_OF_RIGHT - getWidth())
				maxX = LIMIT_OF_RIGHT;
	}

	/*マウスのアクションを受け取るクラス*/
	private class DragLitener extends MouseAdapter{
		int initX, initY;
		private int dx, dy;
		private Draggable drag;

		DragLitener(Draggable drag){
			this.drag = drag;
		}

		/*ドラッグされた場合の処理*/
		public void mouseDragged(MouseEvent e) {
			if(soundFlag){
				try {
					AudioInputStream sound = AudioSystem.getAudioInputStream(getClass().getResource("../../assets/sounds/" + mdl.getCarKind(carNumber) +  ".wav"));
					DataLine.Info di = new DataLine.Info(Clip.class, sound.getFormat());
					Clip clip = (Clip) AudioSystem.getLine(di);
					clip.open(sound);
					clip.start();
					soundFlag = false;
				} catch(Exception ex) {
				}
			}
			int leftTopX = e.getXOnScreen() - dx; //車の左上のX座標
			int leftTopY = e.getYOnScreen() - dy; //車の左上のY座標
			newPoint(leftTopX, leftTopY);
		}

		/*クリックした部分の座標と部品の左上座標の差*/
		public void mousePressed(MouseEvent e) {
			soundFlag = true;
			initX = drag.getX(); //車の初期x
			initY = drag.getY(); //車の初期y
			dx = e.getXOnScreen() - drag.getX();
			dy = e.getYOnScreen() - drag.getY();
		}

		/*マウスが離された場合の処理*/
		public void mouseReleased(MouseEvent e){
			if(initX != drag.getX() || initY != drag.getY()){ //元の位置から動いている場合
				ctrl.mouseReleased(((drag.getX() - LIMIT_OF_LEFT) / BLOCK_WIDTH), ((drag.getY() - LIMIT_OF_TOP) / BLOCK_HEIGHT), carNumber);
			}
		}

		/*ドラッグされた座標を受け取って、座標を修正する*/
		private void newPoint(int leftTopX, int leftTopY){
			if(drag.getHeight() < drag.getWidth()){ //横長
				leftTopX = LIMIT_OF_LEFT + ((leftTopX - 155) / BLOCK_WIDTH) * BLOCK_WIDTH; //きっちり収まるように修正
				if(leftTopX < minX) leftTopX = minX; //最大可動域を超えないようにする
				else if(maxX < leftTopX) leftTopX = maxX;
				leftTopY = maxY;
			}

			if(drag.getWidth() < drag.getHeight()){ //縦長
				leftTopX = maxX;
				leftTopY = LIMIT_OF_TOP + ((leftTopY - 55) / BLOCK_HEIGHT) * BLOCK_HEIGHT; //きっちり収まるように修正
				if(leftTopY < minY) leftTopY = minY; //最大可動域を超えないようにする
				else if(leftTopY > maxY) leftTopY = maxY;
			}
			drag.setLocation(leftTopX, leftTopY);
		}
	}
}
