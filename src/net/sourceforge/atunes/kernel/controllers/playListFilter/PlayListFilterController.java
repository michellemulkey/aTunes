/**
 * aTunes 1.6.0
 * Copyright (C) 2006-2007 Alex Aranda (fleax) alex.aranda@gmail.com
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.kernel.controllers.playListFilter;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.sourceforge.atunes.gui.views.panels.PlayListFilterPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.model.PanelController;

public class PlayListFilterController extends PanelController {

	public PlayListFilterController(PlayListFilterPanel panel) {
		super(panel);
		addBindings();
		addStateBindings();
	}
	
	protected void addStateBindings() {}

	protected void addBindings() {
		final PlayListFilterPanel panel = (PlayListFilterPanel) panelControlled;
		panel.getFilterTextField().addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String text = panel.getFilterTextField().getText();
				if (text.equals(""))
					text = null;
				HandlerProxy.getPlayListHandler().setFilter(text);
			}
		});
		panel.getClearFilterButton().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				panel.getFilterTextField().setText("");
				HandlerProxy.getPlayListHandler().setFilter(null);
			}
		});
	}

	public void emptyFilter() {
		((PlayListFilterPanel) panelControlled).getFilterTextField().setText("");
	}
	
	public void reapplyFilter() {
		if (HandlerProxy.getPlayListHandler().isFiltered())
			HandlerProxy.getPlayListHandler().setFilter(((PlayListFilterPanel) panelControlled).getFilterTextField().getText());
	}
	
	protected void notifyReload() {}
}
