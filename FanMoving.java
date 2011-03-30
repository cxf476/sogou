import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.*;

public class FanMoving
{
	public static void main(String[] args)
	{
		MyFrame frame=new MyFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class MyFrame extends JFrame
{
	public MyFrame()
	{
		setTitle(" Fan ");
		setSize(W,H);
		MyPanel panel=new MyPanel();
		add(panel);
	}
	private static int W=400;
	private static int H=400;
}

class MyPanel extends JPanel implements Runnable
{
	private Thread []thread=new Thread[3];
	private JButton []button=new JButton[4];
	private int []Status=new int[3];
	private int []Start=new int[3];
	private int radius=40;
	private int rangeAngle=30;
	private int x,y;
	private int speed=25;
	private int number=100000000;
	
	public MyPanel()
	{	
		for(int i=0;i<3;i++)
		{
			Status[i]=0;//
			Start[i]=i*30;//风 扇 起 始 角 度
			button[i]=new JButton("风扇"+(i+1));
			button[i].addActionListener(new ButtonAction(i));
			add(button[i]);		
		}	
		button[3]=new JButton("总开关");
		button[3].addActionListener(new ButtonAction(3));
		add(button[3]);
		
		for(int i=0;i<3;i++)
		{
			thread[i]=new Thread(this);
			thread[i].start();
			thread[i].suspend();
		}	
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		setBackground(Color.white);
		PaintFan(g2);
		    
	}
	public void run()
	{				
		try
		{
			for(int i=0;i<number;i++)
			{
				int cur=0;
				for(int j=0;j<3;j++)	
					if(Thread.currentThread()==thread[j])
					{
						cur=j;
						break;
					}
				Start[cur]+=30;//
				repaint();
				if(Start[cur]>=360)	
					Start[cur]-=360;
				Thread.sleep(speed);					
				
			}
		}	
		catch(Exception e)
		{
			System.out.println(e);
		}             
	}
	public synchronized void PaintFan(Graphics2D g2)
	{
		x=50;y=50;
		for(int i=0;i<3;i++)
		{
			int startAngle=Start[i];
			g2.clearRect(x,y,2*radius,2*radius);
			g2.setColor(Color.green);
			g2.fillArc(x,y,2*radius,2*radius,startAngle,rangeAngle);		
			g2.fillArc(x,y,2*radius,2*radius,startAngle+120,rangeAngle);
			g2.fillArc(x,y,2*radius,2*radius,startAngle+240,rangeAngle);
			x+=100;
		}
	}
	private class ButtonAction implements ActionListener
	{
		private int n=0;//
		
		public ButtonAction(int n)
		{
			this.n=n;
		}
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				if(n==3)
				{
					boolean All=true;
					for(int j=0;j<3;j++)
						if(Status[j]==1)
							All=false;
					if(!All)
					{
						for(int j=0;j<3;j++)
						{
							thread[j].suspend();
							Status[j]=0;
						}
					}
					else
					{
						for(int j=0;j<3;j++)
						{
							thread[j].resume();
							Status[j]=1;
						}
					}
				}
				else if(Status[n]==1)
				{
					thread[n].suspend();
					Status[n]=0;
					System.out.println("Stop Thread"+n);
				}
				else 
				{
					thread[n].resume();
					Status[n]=1;
					System.out.println("Start Thread"+n);
				}
			}	
			catch(Exception ex)
			{
				System.out.println(ex);	
			}
			
		}
	}
}
