package  DS;

import java.awt.*;

public class HitPoint
{
	public static final int HITPOINTSIZE=50; //血条的长度
	int topNumber;  //总血量
	int number;  //当前血量
	Tank tk;
	//boxItem bi;

	public HitPoint(int topNum,Tank tk)
	{
		if(topNum>50||topNum<=0) { this.topNumber=50; }
		topNumber=topNum;
		number=topNum;
		this.tk=tk;
	}
	/*public HitPoint(int num,boxItem bi){
		this.number=num;
		this.bi=bi;
	}*/

	public void draw(Graphics g)
	{
		Color c=g.getColor();

		g.setColor(Color.white);
		g.drawRect(tk.x-25,tk.y-35,HITPOINTSIZE,5);
		if(!tk.camp)
		g.setColor(Color.black);
		else g.setColor(Color.red);//一点修改而已，红黑两种血嘛
		g.fillRect(tk.x-24,tk.y-34,number*HITPOINTSIZE/topNumber-1,4);			
		g.setColor(c);
	}

	
	public void cutsHitPoint(int cuts)  //发生碰撞时的减血
	{
		number-=cuts;
		
		if(isEmpty()) { tk.dead(); } //若血空了,Tank死掉 
	}
	
	public void cutsHitPoint(int cuts,Shot s)  //被子弹击中时的减血
	{
		number-=cuts;
		if(tk.isLive&&tk instanceof RobotTank) { ((RobotTank) tk).avoidThrust(s); }//?????????????%%%%%%%%%%%%%%%%%%%%%%
		 
		if(isEmpty()) { tk.dead(); } //若血空了,Tank死掉 
	}
	
	public void raisesHitPoint(int raises) //血量增加
	{
		number+=raises;
		if(number>topNumber) { number=topNumber; }
	}
		
	public boolean isEmpty()
	{
		if(number<=0) { return true; }
		else { return false; }
	}
}
