package app;

import gameplay.PlayManager;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListRenderer implements ListCellRenderer
{
	public ListRenderer()
	{
		super();
		return;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Object ret = value;
		
		if(value.getClass().getName().contains("CheckBox"))
		{
			CheckBox Value = (CheckBox)value;
			
			if(isSelected && Value.IsLaging())
			{
				Value.setSelected(!Value.isSelected());
				Value.RemoveLag();
			}
			else
				Value.SetLag();
			
			ret = Value;
		}
		
		list.clearSelection();
		return (Component)value;
	}
}
