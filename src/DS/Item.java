package  DS;

import java.awt.*;
import java.util.Random;

//import cn.hnist.Joshua.Bomb;
import DS.Tank.TANK_DIR;
import DS.boxBomb;

abstract class Item
{
	public static Toolkit TLK=Toolkit.getDefaultToolkit();
	
	int x,y;
	int width,height;
	Random ran_item=new Random();
	TankClient tc;
	
	public Item(int x,int y,TankClient tc)
	{
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	abstract void draw(Graphics g);
	
	abstract void eat(Tank tk);
	
	public Rectangle getRect()
	{
		return new Rectangle(x,y,width,height);
	}
}


class HitPointItem extends Item  //涨血的道具
{
	int raisesNum;
	int randomNum;
	static final Image hpimg[]={TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/medicine.png")),
		TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/redcross.png"))};
	
	
	public HitPointItem(int x,int y,int raisesNum,TankClient tc)
	{
		super(x,y,tc);
		this.raisesNum=raisesNum;
		this.width=32;
		this.height=32;
		randomNum=ran_item.nextInt(2);
	}

	void draw(Graphics g)
	{
		g.drawImage(hpimg[randomNum],x,y,null);
		
	}
	
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{tk.tankHitPoint.raisesHitPoint(raisesNum);  //Tank涨血
		tk.changeSpeed();
		tc.itemList.remove(this);}
	}
}

class ShotsItem extends Item  //涨炮弹或者散弹的道具
{
	int type; //涨的炮弹类型
	static final Image shotImage[]={TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/5.png")),
		                            TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/6.png"))};

	public ShotsItem(int x,int y,int type,TankClient tc)
	{
		super(x,y,tc);
		this.type=type;
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		if(type==0)
			g.drawImage(shotImage[0],x,y,null);
		else
			g.drawImage(shotImage[1],x,y,null);
	}

	void eat(Tank tk)
	{
		if(!tk.isNPC)  //玩家才可以吃
		{
			switch(type)
			{
				case 0:{((UserTank)tk).addHydraShots();}break;
				case 1:{((UserTank)tk).addSuperShots();}break;
				default:break;
			}
			tc.itemList.remove(this);
		}
	}
}

class speedItem extends Item  //提速或者减速的道具
{
	int type; //提速或者减速
	static final Image shotImage[]={TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/speedItem.png")),
		                            TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/speedItem2.png"))};

	public speedItem(int x,int y,int type,TankClient tc)
	{
		super(x,y,tc);
		this.type=type;
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		if(type==0)
			g.drawImage(shotImage[0],x,y,null);
		else
			g.drawImage(shotImage[1],x,y,null);
	}

	void eat(Tank tk)
	{
		//if(!tk.isNPC)  //玩家和敌人都可以吃，这个道具比较特殊一点的
		//{
			switch(type)
			{
				case 0:{tk.addSpeed();}break;
				case 1:{tk.cutSpeed();}break;
				default:break;
			}
			tc.itemList.remove(this);
		//}
	}
}



class moneyItem extends Item  //加钱的道具
{
	int raisesNum;
	static final Image moneyImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/coin.png"));
	
	public moneyItem(int x,int y,int raisesNum,TankClient tc)
	{
		super(x,y,tc);
		this.raisesNum=raisesNum;
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		g.drawImage(moneyImage,x,y,null);
	}
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{((UserTank)tk).moneyNum.raisesMoneyNumber(raisesNum);  //加钱
		tc.itemList.remove(this);}
	}
}
class keyItem extends Item  //钥匙，拥有他才能进入商城，购买道具
{
	static final Image keyImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/key.png"));
	
	public keyItem(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		g.drawImage(keyImage,x,y,null);
	}
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{ 
			Item tempItem=null;
		for(int i=0;i<((UserTank)tk).tc.itemList.size();i++)
		{
			tempItem=tc.itemList.get(i);
			if(tempItem instanceof shopItem){
				((shopItem) tempItem).open=true;
			}
			
		}
		tc.itemList.remove(this);}
	}
}

class shopItem extends Item  //道具商城
{
	boolean open;
	static final Image shopImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/shopImage.png"));
	
	public shopItem(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		this.width=64;
		this.height=20;//本来是64，但是考虑到进入时能进入shop内部，故改成20比较好
		open=false;
	}

	void draw(Graphics g)
	{
		g.drawImage(shopImage,x,y,null);
	}
	
	@SuppressWarnings("deprecation")
	void eat(Tank tk)
	{
		if((tk instanceof UserTank) && open)
		{
			open=false;
		new shop(((UserTank)tk)).setVisible(true);
		((UserTank)tk).tc.threadRobot.suspend();
		((UserTank)tk).tc.threadRepaint.suspend();	
		((UserTank)tk).keyList.clear();//一定要清零，不然会自己跑
		tk.setMoveDir(TANK_DIR.STOP);
			
		
		}
	}
}
class shieldItem extends Item  //盾牌道具
{
	static final Image shieldImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/shield_2.png"));
	
	public shieldItem(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		g.drawImage(shieldImage,x,y,null);
	}
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{((UserTank)tk).shieldNum.ShieldNumber=30;  //防护加满
		tc.itemList.remove(this);}
	}
}

class flagItem extends Item  //door道具
{
	static final Image flagImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/door48.png"));
	
	public flagItem(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		this.width=48;
		this.height=30;
	}

	void draw(Graphics g)
	{
		g.drawImage(flagImage,x,y,null);
	}
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{
			//JOptionPane.showMessageDialog(null, "Congratulations, YOU WIN!!!");
			//tc.dispose();
			tc.win=true;
			/*Tank tempTk=null;
			for(int i=0;i<tc.tanksList.size();i++){
				tempTk=tc.tanksList.get(i);
				if(!tempTk.camp)
					tempTk.dead();
			}*/
			tc.tanksList.clear();
			tc.itemList.add(new WinIcon(452,252,tc));
		}
	}
}

class WinIcon extends Item  //win 道具
{
	static final Image winImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/cup.png"));
	
	public WinIcon(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		this.width=48;
		this.height=30;
	}

	void draw(Graphics g)
	{
		g.drawImage(winImage,x,y,null);
	}
	
	void eat(Tank tk)
	{//nothing to do
	}
}


class freezeItem extends Item  //冻结效果的道具
{
	long freezeSec; 
	static final Image freezeImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/freeze.png"));
	
	public freezeItem(int x,int y,long freezeSec,TankClient tc)
	{
		super(x,y,tc);
		this.freezeSec=freezeSec;
		this.width=33;
		this.height=33;
	}

	void draw(Graphics g)
	{
		g.drawImage(freezeImage,x,y,null);
	}
	
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{((UserTank)tk).freeze(freezeSec);  //Tank freezing; 2 seconds;
		tc.itemList.remove(this);}
	}
}

class boxItem extends Item  //
{
	static final Image boxImage=TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/Boxes.png"));
	Random r2=new Random();
	boxBomb box=null;
	//boolean isBombed=false;
	boolean isLived=true;
	//HitPoint hp;
	public boxItem(int x,int y,TankClient tc)
	{
		super(x,y,tc);
		//this.hp=hp;
		this.width=64;
		this.height=64;
		//hp=new HitPoint(10,this);
	}

	void draw(Graphics g)
	{if(isLived){
		
		g.drawImage(boxImage,x,y,null);}
		else
		{
			if(box!=null)
			box.draw(g);		
		}
	}
	/*void dead()
	{
		
		tc.itemList.remove(this);
	}*/
	void dead(){
		isLived=false;
		box=new boxBomb(x-64,y-64,this);
	}
	void eat(Tank tk)
	{
		if(!tk.isNPC)
		{
			
			switch(r2.nextInt(13)){
			case 0:
			tc.itemList.add(new HitPointItem(x,y,10,tc)); break;//Tank涨血
			case 1:
				tc.itemList.add(new ShotsItem(x,y,0,tc)); break;//
			case 2:
				tc.itemList.add(new ShotsItem(x,y,1,tc)); break;//
			case 3:
				tc.itemList.add(new moneyItem(x,y,50,tc)); break;//
			case 4:
				tc.itemList.add(new freezeItem(x,y,4000,tc)); break;
			case 5:
				RobotTank.add(1,false,RobotTank.RTANKTYPE.SPE,tc,x,y);break;//
			case 6:
				RobotTank.add(1,false,RobotTank.RTANKTYPE.BOSS,tc,x,y);break;//
			case 7:
				RobotTank.add(1,false,RobotTank.RTANKTYPE.NOR,tc,x,y);break;//
			case 8:
				RobotTank.add(1,true,RobotTank.RTANKTYPE.SPE,tc,x,y);break;//
			case 9:
				RobotTank.add(1,true,RobotTank.RTANKTYPE.NOR,tc,x,y);break;//
			default:
				break;
			}
			
			
		}
	}
}