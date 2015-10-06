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

package net.sourceforge.atunes.kernel.controllers.stats;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.atunes.gui.ColorDefinitions;
import net.sourceforge.atunes.gui.views.dialogs.StatsDialog;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.controllers.model.FrameController;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;
import org.jvnet.substance.SubstanceDefaultTableCellRenderer;


public class StatsDialogController extends FrameController {

	public StatsDialogController(JFrame frame) {
		super(frame);
	}
	
	protected void addStateBindings() {}
	protected void addBindings() {}
	protected void notifyReload() {}
	
	public void showStats() {
		updateStats();
		StatsDialog frame = (StatsDialog) frameControlled;
		frame.setVisible(true);
	}
	
	public void updateStats() {
		setArtistsTable();
		setAlbumsTable();
		setSongsTable();
		setGeneralTable();
		
		setArtistsChart();
		setAlbumsChart();
		setSongsChart();
		setGeneralChart();
	}
	
	private void setGeneralTable() {
		int different = HandlerProxy.getRepositoryHandler().getDifferentSongsPlayed();
		int total = HandlerProxy.getRepositoryHandler().getSongs().size();
		String[] headers = new String[] {" ", LanguageTool.getString("COUNT"), "%"};
		Object[][] content = new Object[2][3];
		content[0] = new Object[3];
		content[0][0] = LanguageTool.getString("SONGS_PLAYED");
		content[0][1] = different;
		content[0][2] = StringUtils.toString((float) different / (float) total * 100, 2);
		content[1] = new Object[3];
		content[1][0] = LanguageTool.getString("SONGS_NEVER_PLAYED");
		content[1][1] = total - different;
		content[1][2] = StringUtils.toString((float) (total - different) / (float) total * 100, 2);
		setTable(((StatsDialog)frameControlled).getGeneralTable(), headers, content);
	}
	
	private void setSongsTable() {
		ArrayList songs = HandlerProxy.getRepositoryHandler().getMostPlayedSongs(-1);
		String[] headers = new String[] {LanguageTool.getString("SONG"), LanguageTool.getString("TIMES_PLAYED"), "%"};
		Object[][] content = new Object[songs.size()][3];
		for (int i = 0; i < songs.size(); i++) {
			content[i][0] = ((Object[]) songs.get(i))[0];
			content[i][1] = ((Object[]) songs.get(i))[1];
			if (HandlerProxy.getRepositoryHandler().getTotalSongsPlayed() != -1)
				content[i][2] = StringUtils.toString(100.0 * (float)((Integer)((Object[]) songs.get(i))[1]) / HandlerProxy.getRepositoryHandler().getTotalSongsPlayed(), 2);
			else 
				content[i][2] = 0;
		}
		setTable(((StatsDialog)frameControlled).getSongsTable(), headers, content);
	}

	private void setAlbumsTable() {
		ArrayList albums = HandlerProxy.getRepositoryHandler().getMostPlayedAlbums(-1);
		String[] headers = new String[] {LanguageTool.getString("ALBUM"), LanguageTool.getString("TIMES_PLAYED"), "%"};
		Object[][] content = new Object[albums.size()][3];
		for (int i = 0; i < albums.size(); i++) {
			content[i][0] = ((Object[]) albums.get(i))[0];
			content[i][1] = ((Object[]) albums.get(i))[1];
			if (HandlerProxy.getRepositoryHandler().getTotalSongsPlayed() != -1)
				content[i][2] = StringUtils.toString(100.0 * (float)((Integer)((Object[]) albums.get(i))[1]) / HandlerProxy.getRepositoryHandler().getTotalSongsPlayed(), 2);
			else 
				content[i][2] = 0;
		}
		setTable(((StatsDialog)frameControlled).getAlbumsTable(), headers, content);
	}
	
	private void setArtistsTable() {
		ArrayList artists = HandlerProxy.getRepositoryHandler().getMostPlayedArtists(-1);
		String[] headers = new String[] {LanguageTool.getString("ARTIST"), LanguageTool.getString("TIMES_PLAYED"), "%"};
		Object[][] content = new Object[artists.size()][3];
		for (int i = 0; i < artists.size(); i++) {
			content[i][0] = ((Object[]) artists.get(i))[0];
			content[i][1] = ((Object[]) artists.get(i))[1];
			if (HandlerProxy.getRepositoryHandler().getTotalSongsPlayed() != -1)
				content[i][2] = StringUtils.toString(100.0 * (float)((Integer)((Object[]) artists.get(i))[1]) / HandlerProxy.getRepositoryHandler().getTotalSongsPlayed(), 2);
			else 
				content[i][2] = 0;
		}
		setTable(((StatsDialog)frameControlled).getArtistsTable(), headers, content);
	}

	private void setTable(JTable table, Object[] headers, Object[][] content) {
		table.setModel(new DefaultTableModel(content, headers));
		table.getColumnModel().getColumn(0).setPreferredWidth(420);
		table.getColumnModel().getColumn(0).setWidth(table.getColumnModel().getColumn(0).getWidth());
		table.getColumnModel().getColumn(2).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setWidth(table.getColumnModel().getColumn(2).getWidth());
		table.getColumnModel().getColumn(1).setCellRenderer(new SubstanceDefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				l.setHorizontalAlignment(SwingConstants.RIGHT);
				return l;
			}
		});
		table.getColumnModel().getColumn(2).setCellRenderer(new SubstanceDefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				l.setHorizontalAlignment(SwingConstants.RIGHT);
				return l;
			}
		});
	}
	
	private void setGeneralChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		int different = HandlerProxy.getRepositoryHandler().getDifferentSongsPlayed();
		int total = HandlerProxy.getRepositoryHandler().getSongs().size();
		dataset.setValue(LanguageTool.getString("SONGS_PLAYED"), different);
		dataset.setValue(LanguageTool.getString("SONGS_NEVER_PLAYED"), total - different);
		JFreeChart chart = ChartFactory.createPieChart3D(LanguageTool.getString("SONGS_PLAYED"), dataset, false, false, false);
		chart.getTitle().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		chart.setBackgroundPaint(new GradientPaint(0, 0, ColorDefinitions.GENERAL_NON_PANEL_TOP_GRADIENT_COLOR, 0, 200, ColorDefinitions.GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR));
		chart.setPadding(new RectangleInsets(5,0,0,0));
		chart.getPlot().setBackgroundPaint(new GradientPaint(0, 0, ColorDefinitions.GENERAL_NON_PANEL_TOP_GRADIENT_COLOR, 0, 200, ColorDefinitions.GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR));
		DefaultDrawingSupplier drawingSupplier = new DefaultDrawingSupplier(new Paint[] {new Color(0,1,0,0.6f), new Color(1,0,0,0.6f)}, new Paint[] {new Color(0,1,0,0.4f), new Color(1,0,0,0.4f)}, 
				DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
		chart.getPlot().setDrawingSupplier(drawingSupplier);
		((StatsDialog)frameControlled).getGeneralChart().setIcon(new ImageIcon(chart.createBufferedImage(710, 250)));
	}
	
	private void setArtistsChart() {
		DefaultCategoryDataset dataset = getDataSet(HandlerProxy.getRepositoryHandler().getMostPlayedArtists(10));
		JFreeChart chart = ChartFactory.createStackedBarChart3D(LanguageTool.getString("ARTIST_MOST_PLAYED"), null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
		chart.getTitle().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		chart.setBackgroundPaint(new GradientPaint(0, 0, ColorDefinitions.GENERAL_NON_PANEL_TOP_GRADIENT_COLOR, 0, 200, ColorDefinitions.GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR));
		chart.setPadding(new RectangleInsets(5,0,0,0));
		NumberAxis axis = new NumberAxis();
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		chart.getCategoryPlot().setRangeAxis(axis);
		chart.getCategoryPlot().setForegroundAlpha(0.6f);
		chart.getCategoryPlot().getRenderer().setPaint(Color.GREEN);

		((StatsDialog)frameControlled).getArtistsChart().setIcon(new ImageIcon(chart.createBufferedImage(710, 250)));
	}
	
	private void setAlbumsChart() {
		DefaultCategoryDataset dataset = getDataSet(HandlerProxy.getRepositoryHandler().getMostPlayedAlbums(10));
		JFreeChart chart = ChartFactory.createStackedBarChart3D(LanguageTool.getString("ALBUM_MOST_PLAYED"), null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
		chart.getTitle().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		chart.setBackgroundPaint(new GradientPaint(0, 0, ColorDefinitions.GENERAL_NON_PANEL_TOP_GRADIENT_COLOR, 0, 200, ColorDefinitions.GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR));
		chart.setPadding(new RectangleInsets(5,0,0,0));
		NumberAxis axis = new NumberAxis();
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		chart.getCategoryPlot().setRangeAxis(axis);
		chart.getCategoryPlot().setForegroundAlpha(0.6f);
		chart.getCategoryPlot().getRenderer().setPaint(Color.GREEN);

		((StatsDialog)frameControlled).getAlbumsChart().setIcon(new ImageIcon(chart.createBufferedImage(710, 250)));
	}

	private void setSongsChart() {
		DefaultCategoryDataset dataset = getDataSet(HandlerProxy.getRepositoryHandler().getMostPlayedSongs(10));
		JFreeChart chart = ChartFactory.createStackedBarChart3D(LanguageTool.getString("SONG_MOST_PLAYED"), null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
		chart.getTitle().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		chart.setBackgroundPaint(new GradientPaint(0, 0, ColorDefinitions.GENERAL_NON_PANEL_TOP_GRADIENT_COLOR, 0, 200, ColorDefinitions.GENERAL_NON_PANEL_BOTTOM_GRADIENT_COLOR));
		chart.setPadding(new RectangleInsets(5,0,0,0));
		NumberAxis axis = new NumberAxis();
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		chart.getCategoryPlot().setRangeAxis(axis);
		chart.getCategoryPlot().setForegroundAlpha(0.6f);
		chart.getCategoryPlot().getRenderer().setPaint(Color.GREEN);

		((StatsDialog)frameControlled).getSongsChart().setIcon(new ImageIcon(chart.createBufferedImage(710, 250)));
	}

	private DefaultCategoryDataset getDataSet(ArrayList list) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				result.setValue((Integer)obj[1], "", (String)obj[0]);
			}
		}
		return result;
	}

}
