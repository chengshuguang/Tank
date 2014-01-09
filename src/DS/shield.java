package  DS;

import java.awt.Color;
import java.awt.Graphics;

import DS.UserTank;

public class shield
{
	int topNumber;  //总钱
	int ShieldNumber;  //当前金币
	UserTank ut;

	public shield(int topNum,UserTank ut)
	{
		if(topNum>30||topNum<=0) { this.topNumber=30; }
		topNumber=topNum;
		ShieldNumber=topNum;
		this.ut=ut;
	}
	public void draw(Graphics g)
	{
		Color c=g.getColor();

		g.setColor(Color.white);
		g.drawRect(ut.x-25,ut.y-43,50,5);
		g.setColor(Color.yellow);
		g.fillRect(ut.x-24,ut.y-42,ShieldNumber*50/topNumber-1,4);			
		g.setColor(c);
	}
	
	public void cutsShieldNumber(int cuts)  
	{
		ShieldNumber-=cuts;
		if(ShieldNumber<0) ShieldNumber=0;
	}
	
		
	public void raisesShieldNumber(int raises) //血量增加
	{
		
		ShieldNumber+=raises;
		if(ShieldNumber>30) ShieldNumber=30;
	}
		
	
}
