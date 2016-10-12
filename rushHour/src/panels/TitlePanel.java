package rushHour.src.panels;

import rushHour.src.controllers.*;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URISyntaxException;

public class TitlePanel extends JPanel{
	private RushHourController ctrl;

	/*コンストラクタ*/
	public TitlePanel(){
		setLayout(null);
		setBounds(0, 0, 800, 455);

		// データのリセットを行うボタン
		ImageIcon reset_data_icon = new ImageIcon(getClass().getResource("../../assets/images/titleParts/reset_data_btn.png"));
		JButton reset =  new JButton(reset_data_icon);
		add(reset);
		reset.setBounds(261, 318, 270, 53);
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				int option = JOptionPane.showConfirmDialog(null, "クリア情報をリセットしますか？", "確認ダイアログ", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){ //ダイアログでyesを押した場合
					int option2 = JOptionPane.showConfirmDialog(null, "元に戻せませんが本当にリセットしますか？", "再確認", JOptionPane.YES_NO_OPTION);
					if (option2 == JOptionPane.YES_OPTION){ //2重チェック
						try{ //クリア情報をリセットする
							URL url = getClass().getResource("../../assets/stages/.clear.txt");
							File file = new File(url.toURI());
							FileWriter filewriter = new FileWriter(file);
							filewriter.write("1");
							filewriter.close();
						}catch(IOException e){
						}catch(URISyntaxException e){
						}
					}
				}
			}
		});
		//ステージセレクトへのボタン
		ImageIcon stage_select_icon = new ImageIcon(getClass().getResource("../../assets/images/titleParts/stage_select_btn.png"));
		JButton goSelect = new JButton(stage_select_icon);
		add(goSelect);
		goSelect.setBounds(261, 216, 267, 53);
		goSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				setVisible(false); //このパネルを不可視化
				ctrl.changeSelect();
			}
		});
		// 背景
		ImageIcon titleBackIcon = new ImageIcon(getClass().getResource("../../assets/images/titleParts/titleBack.png"));
		JLabel titleBackLabel = new JLabel(titleBackIcon);
		add(titleBackLabel);
		titleBackLabel.setBounds(0, 0, titleBackIcon.getIconWidth(), titleBackIcon.getIconHeight());
	}

	public void setCtrl(RushHourController ctrl){
		this.ctrl = ctrl;
	}
}
