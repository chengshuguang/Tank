package  DS;

import  DS.UserTank;

public class money
{
	int topNumber;  //总钱
	int MoneyNumber;  //当前金币
	UserTank ut;

	public money(int topNum,UserTank ut)
	{
		if(topNum>9999||topNum<=0) { this.topNumber=9999; }
		topNumber=topNum;
		MoneyNumber=topNum;
		this.ut=ut;
	}

	
	public void cutsMoneyNumber(int cuts)  
	{
		MoneyNumber-=cuts;
	}
	
		
	public void raisesMoneyNumber(int raises) //血量增加
	{
		MoneyNumber+=raises;
		//if(MoneyNumber>topNumber) { MoneyNumber=topNumber; }
	}
		
	
}
