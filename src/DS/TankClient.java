package  DS;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.applet.Applet;


import javax.swing.JOptionPane;
//import javax.swing.JRootPane;


public class TankClient extends Frame//frame???????????????????
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TankClient(){
	this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(TankClient.class.getResource("pointer.png")),new Point(0,0), ""));	
	Applet.newAudioClip(TankClient.class.getResource("123.mid")).loop();
	
	}//鼠标手指
	
	public int speedFlag=0;
	public boolean win=false;//??????????????????????????????????????????????????>>>>
	public static final int  WIN_WIDTH=1000;
	public static final int  WIN_HEIGHT=600;
	boolean repaintFlag=true;  //控制重画线程的结束
	Image bkImage=null;  //用于双缓冲的缓存图片
	Point mousePoint=new Point(600,500);
	boolean pause=false;//暂停
	UserTank ut=new UserTank(20,572,Tank.TANK_DIR.U,true,this); //玩家控制的Tank
	//?????????????????????????
	Thread threadRepaint;
	Thread  threadRobot;

	List<Shot> shotsList=Collections.synchronizedList(new ArrayList<Shot>());//存储界面上的子弹
	List<Tank> tanksList=Collections.synchronizedList(new ArrayList<Tank>());//存储界面上的Tank
	List<Wall> wallList=Collections.synchronizedList(new ArrayList<Wall>());//界面上的障碍物
	List<Item> itemList=Collections.synchronizedList(new ArrayList<Item>());//界面上的道具
	
	/*
	public static void main(String[] args)
	{
		TankClient tc=new TankClient();
		tc.lunchFrame();
	}
	*/
	/*public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				TankClient tc=new TankClient();
				tc.lunchFrame();
			}
		});
	}*/
	void lunchFrame()  //界面初始化
	{
		tanksList.add(ut);
		
		
		this.wallList.add(new Wall(160,80,1,30,this,0));  //添加障碍物########
		this.wallList.add(new Wall(80,80,7,1,this,0));
		this.wallList.add(new Wall(96,176,1,9,this,0));
		this.wallList.add(new Wall(207,257,4,1,this,0));
		this.wallList.add(new Wall(47,536,4,1,this,0));
		this.wallList.add(new Wall(766,0,13,1,this,0));
		this.wallList.add(new Wall(624,303,6,1,this,0));
		this.wallList.add(new Wall(640,303,6,8,this,1));//yao shi zai ni mian
		this.wallList.add(new Wall(2,400,2,3,this,1));
		this.wallList.add(new Wall(50,400,2,18,this,0));  //添加障碍物######################
		this.wallList.add(new Wall(450,400,2,20,this,0));  //添加障碍物########################
		//this.wallList.add(new Wall(200,160,Wall.WALLTYPE.HEART,this));
		this.wallList.add(new Wall(306,430,3,2,this,0));
		this.wallList.add(new Wall(306,528,4,2,this,0));
		this.wallList.add(new Wall(450,430,7,2,this,0));
		this.wallList.add(new Wall(936,400,2,4,this,0));
		this.wallList.add(new Wall(401,541,4,5,this,1));
		this.wallList.add(new Wall(324,5,4,8,this,1));
		this.wallList.add(new Wall(401,527,1,3,this,0));
		this.wallList.add(new Wall(321,190,1,8,this,0));//shop
		this.wallList.add(new Wall(321,206,6,1,this,0));
		this.wallList.add(new Wall(433,206,6,1,this,0));
		this.wallList.add(new Wall(337,271,1,1,this,0));
		this.wallList.add(new Wall(417,271,1,1,this,0));
		
		
		this.wallList.add(new Wall(340,400,2,7,this,1));  //添加障碍物########################
		this.wallList.add(new Wall(150,485,this,2));
		this.wallList.add(new Wall(357,96,this,3));
		this.wallList.add(new Wall(860,482,this,4));
		this.wallList.add(new Wall(592,180,this,5));
		this.wallList.add(new Wall(10,250,this,6));
		this.wallList.add(new Wall(561,482,this,7));
		this.wallList.add(new Wall(930,266,this,8));
		this.wallList.add(new Wall(865,94,this,9));

		
		Random r=new Random();//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
		this.itemList.add(new HitPointItem(628+r.nextInt(160),430+r.nextInt(156),10,this));//添加一个涨血的道具
		this.itemList.add(new ShotsItem(370,35,1,this));  //超级炮弹的道具
		this.itemList.add(new ShotsItem(8+r.nextInt(784),15+r.nextInt(570),0,this));  //特殊炮弹的道具
		this.itemList.add(new moneyItem(782+r.nextInt(200),15+r.nextInt(200),200,this));  //特殊炮弹的道具
		this.itemList.add(new speedItem(376,468,0,this));
		//this.itemList.add(new boxItem(8+r.nextInt(784),15+r.nextInt(570),this));
		this.itemList.add(new shopItem(350,206,this));
		this.itemList.add(new keyItem(679,338,this));
		//this.itemList.add(new flagItem(700,338,this));//??????????????????
		
		RobotTank.add(3,false,RobotTank.RTANKTYPE.NOR,this);  //添加机器人Tank
		//RobotTank.add(3,true,RobotTank.RTANKTYPE.BOSS,this);  //添加机器人Tank
		RobotTank.add(2,false,RobotTank.RTANKTYPE.SPE,this);
		RobotTank.add(1,false,RobotTank.RTANKTYPE.BOSS,this);
		
		//this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		//this.setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG); // 设置为简单对话框风格

		this.setLocation(200,100);
		this.setSize(WIN_WIDTH,WIN_HEIGHT);
		this.setTitle("战场");
		this.setResizable(false); //不能改变窗口大小
		this.setBackground(Color.LIGHT_GRAY);
		

		this.addWindowListener(new FrameClose());
		this.addKeyListener(new TankMoveLis());
		this.addMouseMotionListener(new MouseMoveLis());
		this.addMouseListener(new MouseCleckLis()); //Tank开火监听者
		//new Thread(new RepaintThread()).start(); //定时重画的线程
		//new Thread(new RobotTanksThread(this)).start();
		Runnable repaintRunnable=new RepaintThread();
		Runnable robotTankRunnable=new RobotTanksThread(this);
		 threadRepaint=new Thread(repaintRunnable);
	     threadRobot=new Thread(robotTankRunnable);
	     threadRepaint.start();
	     threadRobot.start();
		this.setVisible(true);	
	}

	
	class FrameClose extends WindowAdapter  //窗口关闭事件监听者
	{
		public void windowClosing(WindowEvent e)
		{
			Frame f=(Frame)e.getWindow();/////??????????????????????
			repaintFlag=false;
			f.dispose();
		}
	}
	
	
	public void paint(Graphics g)  //synchronized
	{
		//if(!ut.isLive) { ut.isLive=true; ut.tankHitPoint.number=5; }
		//else
		//initComponents();
		{
			Item tcItems=null;
			for(int i=0;i<itemList.size();i++)
			{
				tcItems=itemList.get(i);
				tcItems.draw(g);
			}
			Wall tcWall=null;
			for(int i=0;i<wallList.size();i++)
			{
				tcWall=wallList.get(i);
				tcWall.draw(g);
			}
			Shot tcShots=null;
			for(int i=0;i<shotsList.size();i++)
			{
				tcShots=shotsList.get(i);
				tcShots.draw(g);
			}
			Tank tcTanks=null;
			for(int i=tanksList.size()-1;i>=0;i--)
			{
				tcTanks=tanksList.get(i);
				tcTanks.draw(g);
			}
		}
	}

	
	public void update(Graphics g)
	{
		if(null==bkImage) { bkImage=this.createImage(WIN_WIDTH,WIN_HEIGHT); }

		g.drawImage(bkImage,0,0,null);  //把虚拟图片的内容画到当前窗口

		Graphics gBkImg=bkImage.getGraphics();
		gBkImg.clearRect(0,0,WIN_WIDTH,WIN_HEIGHT); //先清空图片
		this.paint(gBkImg);  //将图像先画到虚拟图片上
	}

	
	class RepaintThread implements Runnable //画面重画线程
	{
		public void run()
		{
			while(repaintFlag)
			{
				try
				{
					repaint();
					ut.move(); //Tank定时移动

					Shot tcShots=null;
					for(int i=0;i<shotsList.size();i++) //界面上的子弹定时移动
					{
						tcShots=shotsList.get(i);
						tcShots.move();
					}
					if(ut.moneyNum.MoneyNumber>=200){//达到1000金币就出来flag，然后吃掉就可以胜利
						ut.str="☆★右上方胜利大门已经打开，请进 ★☆";
						ut.doorOpen=true;//红色字体，标记
						ut.tc.itemList .add(new flagItem(700,2,ut.tc));
					}
					
					Thread.sleep(33);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}
public void escape(){
	this.dispose();
}
	
	class RobotTanksThread implements Runnable  //机器人Tank的行动线程
	{
		TankClient tc;

		public RobotTanksThread(TankClient tc)
		{
			this.tc=tc;
		}
		
		public void run()
		{
			Tank tcTanks=null;
			int count=-1;

			while(repaintFlag)
			{
				if(2==count&&!win)  //电脑Tank全灭后重新加几辆
				{
					RobotTank.add(1,false,RobotTank.RTANKTYPE.BOSS,tc);
					RobotTank.add(1,false,RobotTank.RTANKTYPE.SPE,tc);
					RobotTank.add(1,false,RobotTank.RTANKTYPE.NOR,tc);
				}
				
				try
				{
					count=0;

					for(int i=0;i<tanksList.size();i++)
					{
						tcTanks=tanksList.get(i);
						if(!tcTanks.camp) { count++; }

						if(!tcTanks.isNPC||!tcTanks.isLive) { continue; } //不控制玩家的Tank
						
						((RobotTank)tcTanks).autoAction();  //自主行动
					}

					Thread.sleep(33);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}
	
	
	class TankMoveLis extends KeyAdapter  //玩家Tank按键监听者
	{
	//	@SuppressWarnings("deprecation")
		@SuppressWarnings({  "deprecation" })
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode()==KeyEvent.VK_G){//    G键进入shop
				threadRobot.suspend();
				threadRepaint.suspend();
				new shop(ut).setVisible(true);
			}
			else if(e.getKeyCode()==KeyEvent.VK_ESCAPE||e.getKeyCode()==KeyEvent.VK_DELETE){
				threadRobot.suspend();
				threadRepaint.suspend();
				int result3 =JOptionPane.showConfirmDialog(null, "确定要退出游戏吗？？", "游戏主界面系统消息", JOptionPane.YES_NO_OPTION );
				if(result3==JOptionPane.YES_OPTION){
					escape();
				}//esc键或者delete键退出游戏
				else if(result3==JOptionPane.NO_OPTION){
					threadRobot.resume();
					threadRepaint.resume();
				}
			}
			else if(e.getKeyCode()==KeyEvent.VK_P)//暂停与恢复，
			{
				if(pause)
				{//threadRepaint.resume();
				threadRobot.resume();
				pause=false;}
				else {
					//threadRepaint.suspend() ;
					threadRobot.suspend();
					pause=true;
				}
			}
			
			if(ut.isLive)
			{   		
				ut.keyDispose(e.getKeyCode(),true);
			}
		}

		public void keyReleased(KeyEvent e)
		{
			if(ut.isLive)
			{
				if(e.getKeyCode()==KeyEvent.VK_F1)
					ut.help();
				else
				ut.keyDispose(e.getKeyCode(),false);
			}
			else
			{
				if(e.getKeyCode()==KeyEvent.VK_F2)
				{
					ut.rebirth();  //复活
				}
				
			}
		}
	}
	//class keyLis
	
	class MouseMoveLis extends MouseMotionAdapter  //鼠标移动监听者
	{
		public void mouseMoved(MouseEvent e)
		{
			if(ut.isLive)
			{
				//System.out.print(e.getX()+" || ");
				//System.out.println(e.getY());
				mousePoint=e.getPoint();
				ut.setTurretDir(mousePoint);  //根据鼠标位置更新炮筒方向
			}
		}
	}

	
	class MouseCleckLis extends MouseAdapter  //点击鼠标,玩家Tank发射炮弹
	{
		@SuppressWarnings("deprecation")
		public void mouseClicked(MouseEvent e)
		{
			if(ut.isLive)
			{
				if(MouseEvent.BUTTON2==e.getButton())//鼠标中键进入shop或者G键
				{
					threadRobot.suspend();
					threadRepaint.suspend();
					new shop(ut).setVisible(true);
				}
				ut.fire(MouseEvent.BUTTON3==e.getButton());  //按鼠标右键会发射特殊子弹
			}
		}
	}
	
}









