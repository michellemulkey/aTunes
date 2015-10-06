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

package net.sourceforge.atunes.gui.views.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.images.ImageLoader;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class EqualizerPanel extends JPanel {
	
	private static final long serialVersionUID = 8115186046826737885L;

	private JToggleButton eqOnOff;
	
	private JSlider globalSlider;
	private JSlider slider60;
	private JSlider slider170;
	private JSlider slider310;
	private JSlider slider600;
	private JSlider slider1K;
	private JSlider slider3K;
	private JSlider slider6K;
	private JSlider slider12K;
	private JSlider slider14K;
	private JSlider slider16K;
	
	
	public EqualizerPanel() {
		super(new GridBagLayout());
		addContent();
	}
	
	private void addContent() {
		eqOnOff = new JToggleButton(ImageLoader.VOlUME_MAX, false);
		
		globalSlider = new JSlider();
		setSlider(globalSlider);
		
		slider60 = new JSlider();
		setSlider(slider60);
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		JLabel label60 = new JLabel("60");
		label60.setFont(f);
		
		slider170 = new JSlider();
		setSlider(slider170);
		JLabel label170 = new JLabel("170");
		label170.setFont(f);
		slider310 = new JSlider();
		setSlider(slider310);
		JLabel label310 = new JLabel("310");
		label310.setFont(f);
		slider600 = new JSlider();
		setSlider(slider600);
		JLabel label600 = new JLabel("600");
		label600.setFont(f);
		slider1K = new JSlider();
		setSlider(slider1K);
		JLabel label1K = new JLabel("1K");
		label1K.setFont(f);
		slider3K = new JSlider();
		setSlider(slider3K);
		JLabel label3K = new JLabel("3K");
		label3K.setFont(f);
		slider6K = new JSlider();
		setSlider(slider6K);
		JLabel label6K = new JLabel("6K");
		label6K.setFont(f);
		slider12K = new JSlider();
		setSlider(slider12K);
		JLabel label12K = new JLabel("12K");
		label12K.setFont(f);
		slider14K = new JSlider();
		setSlider(slider14K);
		JLabel label14K = new JLabel("14K");
		label14K.setFont(f);
		slider16K = new JSlider();
		setSlider(slider16K);
		JLabel label16K = new JLabel("16K");
		label16K.setFont(f);
		
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0; c.weighty = 0; c.fill = GridBagConstraints.NONE;
		eqOnOff.setPreferredSize(new Dimension(20,20));
		eqOnOff.setSize(eqOnOff.getSize());
		topPanel.add(eqOnOff, c);
		
		
		c.gridx = 0; c.gridy = 0;  c.gridwidth = 2; c.weightx = 1; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(10,10,0,10);
		add(topPanel, c);
		
		c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.weightx = 0.2; c.weighty = 1; c.fill = GridBagConstraints.BOTH; c.insets = new Insets(0,20,0,10);
		add(globalSlider, c);
		
		JPanel sliderPanel = new JPanel(new GridLayout(1,10));
		sliderPanel.setOpaque(false);
		sliderPanel.add(slider60);
		sliderPanel.add(slider170);
		sliderPanel.add(slider310);
		sliderPanel.add(slider600);
		sliderPanel.add(slider1K);
		sliderPanel.add(slider3K);
		sliderPanel.add(slider6K);
		sliderPanel.add(slider12K);
		sliderPanel.add(slider14K);
		sliderPanel.add(slider16K);
		
		c.gridx = 1; c.weightx = 0.8; c.insets = new Insets(0,5,0,0); 
		add(sliderPanel, c);
		
		JPanel labelPanel = new JPanel(new GridLayout(1,10));
		labelPanel.setOpaque(false);
		labelPanel.add(label60, c);
		labelPanel.add(label170);
		labelPanel.add(label310);
		labelPanel.add(label600);
		labelPanel.add(label1K);
		labelPanel.add(label3K);
		labelPanel.add(label6K);
		labelPanel.add(label12K);
		labelPanel.add(label14K);
		labelPanel.add(label16K);
		
		c.gridy = 2; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0,0,5,0); 
		add(labelPanel, c);
	}
	
	private void setSlider(JSlider s) {
		s.setOrientation(SwingConstants.VERTICAL);
		s.setOpaque(false);
	}
	
	public static void main(String[] args) {
		ColorDefinitions.initColors();
		JFrame f = new JFrame();
		f.add(new EqualizerPanel());
		f.setSize(500,200);
		f.setVisible(true);
	}

}
