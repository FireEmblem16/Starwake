package app;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import client.ClientApp;

public class ScrollPane extends JScrollPane
{
	public ScrollPane(ClientApp app)
	{
		super();
		
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		setPreferredSize(new Dimension(750,500));	
	
		Rules = new SettingsList(app);
			
		add(Rules);
		setViewportView(Rules);
	
		return;
	}
	
	protected SettingsList Rules;
}