package one;

import java.awt.Color;
import java.util.Random;
import javax.swing.JPanel;

class Robot {
	private int row;//��ǰ�������ߵ��к���
	private int col;
	private boolean myIsPlayingChess=false;//��־�������Ƿ���������
	public void setMyIsPlayingChess(boolean status){//���û�����������������
		this.myIsPlayingChess=status;
	}
	public boolean getMyIsPlayingChess(){//��û������Ƿ���������
		return this.myIsPlayingChess;
	}
	public int getRow(){//��õ�ǰ�������ߵ���
		return this.row;
	}
	public int getCol(){//��õ�ǰ�������ߵ���
		return this.col;
	}
	public void cerebra(Color[][] allChesses, JPanel canvas,Color robotChessColor,Stack stack) {//�����˵Ĵ���
		this.myIsPlayingChess=true;//��������������
		long powerValue=0;//��ʼ������Ȩֵ
		int row=0;//��ʼ������λ���к���
		int col=0;
		long attackPowerValue=0;//��ʼ������Ȩֵ
		int attactRow=0;//��ʼ������λ���к���
		int attactCol=0;
		boolean isFirst=true;//�Ƿ��ǵ�һ�Σ���ʼ��Ϊ��
		for(int i=0;i< allChesses.length;i++){//ͨ������õ����ص����λ��,��Ϊ�Ƿ�������ͳ�ư�ɫ
			for(int j=0;j<allChesses.length;j++){
				if(allChesses[i][j]==null){//�Ի�û���ߵĿ�������ͳ��
					if(isFirst==true){//��һ��Ϊ�յĿո�
						row=i;//�������е�λ��
						col=j;
						powerValue=RowPowerValue(allChesses, i, j,Color.white)+ColPowerValue(allChesses, i, j,Color.white)+RightBiasPowerValue(allChesses, i, j,Color.white)+LeftBiasPowerValue(allChesses, i, j,Color.white);//���㵱ǰλ�õ�Ȩֵ
						isFirst=false;//��ʾ��һ��λ�ü������
					}
					else{
						long nowPowerValue;//���޼�����ѷ���λ��
						nowPowerValue=RowPowerValue(allChesses, i, j,Color.white)+ColPowerValue(allChesses, i, j,Color.white)+RightBiasPowerValue(allChesses, i, j,Color.white)+LeftBiasPowerValue(allChesses, i, j,Color.white);
						if(nowPowerValue>powerValue){
							row=i;
							col=j;
							powerValue=nowPowerValue;
						}
					}
				}
			}
		}
		
		isFirst=true;//��ʼ��isFirstΪtrue		
		for(int i=0;i< allChesses.length;i++){
			for(int j=0;j<allChesses.length;j++){
				if(allChesses[i][j]==null){//���������Ȩֵ
					if(isFirst==true){//��һ�������,ִֻ��һ��
						attactRow=i;//��һ���������к���
						attactCol=j;
						attackPowerValue=RowPowerValue(allChesses, i, j,Color.black)+ColPowerValue(allChesses, i, j,Color.black)+RightBiasPowerValue(allChesses, i, j,Color.black)+LeftBiasPowerValue(allChesses, i, j,Color.black);//�����һ��������Ȩֵ
						isFirst=false;
					}
					else{
						long nowPowerValue;//���޼�����ѽ���λ��
						nowPowerValue=RowPowerValue(allChesses, i, j,Color.black)+ColPowerValue(allChesses, i, j,Color.black)+RightBiasPowerValue(allChesses, i, j,Color.black)+LeftBiasPowerValue(allChesses, i, j,Color.black);
						if(nowPowerValue>powerValue){
							attactRow=i;
							attactCol=j;
							attackPowerValue=nowPowerValue;
						}
					}
				}
			}
		}
		
		if(stack.StackLength()==0){//����������ߵ�һ���壬�Ͳ��ü����������һ����
			Random random=new Random();
			this.row=random.nextInt(allChesses.length);
			this.col=random.nextInt(allChesses.length);
			
		}
		else{//�����ȷ���Ƿ��ػ��ǽ���
			if(attackPowerValue>=powerValue){//�ȽϷ��غͽ���Ȩֵ��ȷ�����ػ��ǽ���
				this.row=attactRow;
				this.col=attactCol;
			}
			else{
				this.row=row;
				this.col=col;
			}
		}
		allChesses[getRow()][getCol()]=robotChessColor;//�޸Ķ�ά����
		stack.Push(getRow(), getCol(), robotChessColor);//��������Ϣѹ��ջ��
		canvas.repaint();
		this.myIsPlayingChess=false;//�������������
	}
	
	public long LeftBiasPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=LeftBiasUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=LeftBiasDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount LeftBiasDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i<allChesses.length-1&&j>0){
			if(allChesses[i+1][j-1]==color){
				count++;
				i=i+1;
				j=j-1;
			}
			else {
				if(allChesses[i+1][j-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount LeftBiasUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i>0&&j<allChesses.length-1){
			if(allChesses[i-1][j+1]==color){
				count++;
				i=i-1;
				j=j+1;
			}
			else {
				if(allChesses[i-1][j+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long RightBiasPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=RightBiasUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=RightBiasDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount RightBiasDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i<allChesses.length-1&&j<allChesses.length-1){
			if(allChesses[i+1][j+1]==color){
				count++;
				i=i+1;
				j=j+1;
			}
			else {
				if(allChesses[i+1][j+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount RightBiasUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i>0&&j>0){
			if(allChesses[i-1][j-1]==color){
				count++;
				i=i-1;
				j=j-1;
			}
			else {
				if(allChesses[i-1][j-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long ColPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=ColUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=ColDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
		
	}
	
	public NullAndCount ColDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=row;i<allChesses.length-1;i++){
			if(allChesses[i+1][col]==color){
				count++;
			}
			else{
				if(allChesses[i+1][col]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount ColUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=row;i>0;i--){
			if(allChesses[i-1][col]==color){
				count++;
			}
			else{
				if(allChesses[i-1][col]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long RowPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount rightRow=RightRowPowerValue(allChesses, row, col, color);
		NullAndCount leftRow=LeftRowPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(rightRow.getChessCount()+leftRow.getChessCount()+1){
			case  1:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount LeftRowPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=col;i>0;i--){
			if(allChesses[row][i-1]==color){
				count++;
			}
			else{
				if(allChesses[row][i-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount RightRowPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=col;i<allChesses.length-1;i++){
			if(allChesses[row][i+1]==color){
				count++;
			}
			else{
				if(allChesses[row][i+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
}