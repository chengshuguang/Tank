package  DS;

import java.awt.*;
import java.util.ArrayList;

//创造一个可以选择地图类型的游戏，虽然不是随机生成，希望可以弥补？？？？？？？？？？？？？？？？？？？？？？？？
public class Wall
{
	static final int WIDTH=16;
	static final int HEIGHT=16;
	
	int x, y;
	int line, row;
	boolean isLive;
	int type;
	ArrayList<WallNode> wallNodesList=new ArrayList<WallNode>();
	TankClient tc;
	
	public Wall(int x,int y,int line,int row,TankClient tc,int type)
	{
		this.x=x;
		this.y=y;
		this.line=line;
		this.row=row;
		this.isLive=true;
		this.tc=tc;
		this.type=type;

		this.creat(type);
	}
	public Wall(int x,int y,TankClient tc,int type){
		this.x=x;
		this.y=y;
		this.isLive=true;
		this.tc=tc;
		this.type=type;
		this.creat2(type);
	}
	
	private void creat(int type)
	{
		for(int i=0;i<line;i++)
		{
			for(int j=0;j<row;j++)
			{
				
				this.wallNodesList.add(new WallNode(x+j*WallNode.WIDTH,y+i*WallNode.HEIGHT,type));//???????????
			}
		}
	}
	private void creat2(int type){
		this.wallNodesList.add(new WallNode(x,y,type));
	}
	
	public boolean isHits(Shot s) //普通子弹可以破坏墙
	{
		WallNode wn=null;
		for(int i=0;i<wallNodesList.size();i++)
		{
			wn=wallNodesList.get(i);
			if(s.getRect().intersects(wn.getRect()))
			{   if(wn.typeWall==1){//*******************************************
				wn.isLive=false;
				wallNodesList.remove(i);}
			
				if(0==wallNodesList.size())//墙全部被破坏了
				{
					this.isLive=false;
					this.tc.wallList.remove(this);
                    //System.out.println(tc.wallList.size());
				} //墙全部被破坏了
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isHits(Rectangle rect) //碰撞检测
	{
		WallNode wn=null;
		for(int i=0;i<wallNodesList.size();i++)
		{
			wn=wallNodesList.get(i);

			if(rect.intersects(wn.getRect()))
			{
				return true;
			}
		}

		return false;
	}
	
	public void draw(Graphics g)
	{
		for(int i=0;i<wallNodesList.size();i++)
		{
			wallNodesList.get(i).draw(g);
		}
	}
	
}

class WallNode
{
	static final Toolkit TLK=Toolkit.getDefaultToolkit(); //用来将图片读入
	static final Image WallImage[]={TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/1.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/Wall.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house1.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house2.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house3.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house4.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house5.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/BigTree.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house6.png")),
									TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/house7.png"))
									};
	
	
	static final int WIDTH=16;
	static final int HEIGHT=16;
	int x, y;
	int typeWall;
	boolean isLive;
	
	public WallNode(int x,int y,int typeWall)//?????????????????????????????????????????????
	{
		this.x=x;
		this.y=y;
		this.isLive=true;
		this.typeWall=typeWall;
	}
	
	public void draw(Graphics g)//???????????????????????????????????????????????????
	{
		if(isLive)
		{
			
			g.drawImage(WallImage[typeWall],x,y,null);
			
		}
	}
	
	public Rectangle getRect()
	{
		if(typeWall==0||typeWall==1)
		return new Rectangle(this.x,this.y,WIDTH,HEIGHT);
		else if(typeWall==8)
			return new Rectangle(this.x,this.y,64,64);
		else
			return new Rectangle(this.x,this.y,48,48);
	}

}
