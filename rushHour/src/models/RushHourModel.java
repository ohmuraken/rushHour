package rushHour.src.models;

import rushHour.src.controllers.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.awt.Point;

public class RushHourModel{
	private int stageNum = 1;
	private int numberOfCars, numberOfBarriers;
	private RushHourController ctrl;
	private Point carsPosition[] = new Point[11]; //車の場所
	private Point barriersPosition[] = new Point[5]; //障害物の場所
	private int carsSize[] = new int[11];         //車の大きさ（長さ）
	private int carsDirection[] = new int[11];    //車の向き（0が横、1が縦）
	private String carsKind[] = new String[11];
	private int moves, time;


	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}

	/*データを初期値にする*/
	public void resetData(){
		moves = 0;
		time = 0;
	}

	/*車の位置をセットする*/
	private void setCarInfos(String[] carInfos, int num){
		carsPosition[num] = new Point(Integer.parseInt(carInfos[0]), Integer.parseInt(carInfos[1]));
		carsSize[num] = Integer.parseInt(carInfos[2]);
		carsDirection[num] = Integer.parseInt(carInfos[3]);
		carsKind[num] = carInfos[4];
	}

	/*障害物の位置をセットする*/
	private void setBarrierPosition(String[] barriersInfo, int num){
		barriersPosition[num] = new Point(Integer.parseInt(barriersInfo[0]), Integer.parseInt(barriersInfo[1]));
	}

	/*移動回数を増やす*/
	public void addMoveCount(){
		moves++;
		ctrl.repaintMoves();
	}

	/*経過時間を増やす*/
	public void addTime(){
		time++;
	}

	public void readData(){
		try{
			InputStream is = getClass().getResourceAsStream("../../assets/stages/.stage" + stageNum + ".txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			numberOfCars = Integer.parseInt(br.readLine());   //最初の行は車の数
			numberOfBarriers =Integer.parseInt(br.readLine()); //2行目は障害物の数
			for(int i = 0; i < numberOfCars; i++){ //車の数だけさらに読み込み
				String carInfo = br.readLine();
				String carInfos[] = carInfo.split(" "); //車の情報を分ける（初期X 初期Y 長さ 向き 種類）
				setCarInfos(carInfos, i);
			}
			String myCarInfos[] = {"0", "2", "2", "0", "myCar"};
			setCarInfos(myCarInfos, numberOfCars);
			numberOfCars++;
			for(int i = 0; i < numberOfBarriers; i++){
				String barrierInfo = br.readLine();
				String barrierInfos[] = barrierInfo.split(" ");
				setBarrierPosition(barrierInfos, i);
			}
			is.close();
			isr.close();
			br.close();
		} catch(IOException e){
		}
	}

	/*各ゲッター*/
	public Point getCarPosition(int num){ return carsPosition[num]; }
	public Point getBarrierPosition(int num){ return barriersPosition[num]; }
	public int getCarSize(int num){ return carsSize[num]; }
	public int getCarDirection(int num){ return carsDirection[num]; }
	public String getCarKind(int num){ return carsKind[num]; }
	public int getMoves(){ return moves; }
	public int getTime(){ return time; }
	public int getStageNumber(){ return stageNum; }
	public int getNumOfCars(){ return numberOfCars; }
	public int getNumOfBarriers(){ return numberOfBarriers; }

	public void setStageNumber(int stageNum){ this.stageNum = stageNum; }
	public void setCarPosition(int x, int y, int num){ carsPosition[num].setLocation(x, y); }
}
