package github.cephrus.optimizer;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class LoLOptimizer
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.put("nimbusBase", Color.DARK_GRAY);
			
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
		
		JFrame frame = new JFrame("Nimbus");
		frame.setDefaultCloseOperation(3);
		frame.setPreferredSize(new Dimension(600, 800));
		frame.setVisible(true);
		
		frame.pack();
	}
}
