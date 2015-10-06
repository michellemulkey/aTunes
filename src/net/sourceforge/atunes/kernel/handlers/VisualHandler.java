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

package net.sourceforge.atunes.kernel.handlers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.atunes.Constants;
import net.sourceforge.atunes.gui.Frame;
import net.sourceforge.atunes.gui.MultipleFrame;
import net.sourceforge.atunes.gui.StandardFrame;
import net.sourceforge.atunes.gui.ToolBar;
import net.sourceforge.atunes.gui.model.PlayListTableModel;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable;
import net.sourceforge.atunes.gui.views.controls.playList.PlayListTable.PlayState;
import net.sourceforge.atunes.gui.views.dialogs.AboutDialog;
import net.sourceforge.atunes.gui.views.dialogs.CopyProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.EditTagDialog;
import net.sourceforge.atunes.gui.views.dialogs.EditTitlesDialog;
import net.sourceforge.atunes.gui.views.dialogs.ExportOptionsDialog;
import net.sourceforge.atunes.gui.views.dialogs.FileSelectionDialog;
import net.sourceforge.atunes.gui.views.dialogs.ImageDialog;
import net.sourceforge.atunes.gui.views.dialogs.IndeterminateProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.OSDDialog;
import net.sourceforge.atunes.gui.views.dialogs.ProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.RepositorySelectionInfoDialog;
import net.sourceforge.atunes.gui.views.dialogs.RipCdDialog;
import net.sourceforge.atunes.gui.views.dialogs.RipperProgressDialog;
import net.sourceforge.atunes.gui.views.dialogs.SearchDialog;
import net.sourceforge.atunes.gui.views.dialogs.StatsDialog;
import net.sourceforge.atunes.gui.views.dialogs.editPreferences.EditPreferencesDialog;
import net.sourceforge.atunes.gui.views.menu.ApplicationMenuBar;
import net.sourceforge.atunes.gui.views.panels.AudioScrobblerPanel;
import net.sourceforge.atunes.gui.views.panels.FilePropertiesPanel;
import net.sourceforge.atunes.gui.views.panels.NavigationPanel;
import net.sourceforge.atunes.gui.views.panels.PlayListPanel;
import net.sourceforge.atunes.kernel.HandlerProxy;
import net.sourceforge.atunes.kernel.Kernel;
import net.sourceforge.atunes.kernel.controllers.DragAndDropListener;
import net.sourceforge.atunes.kernel.controllers.fileProperties.FilePropertiesController;
import net.sourceforge.atunes.kernel.executors.GuiBackgroundExecutor;
import net.sourceforge.atunes.kernel.modules.repository.audio.AudioFile;
import net.sourceforge.atunes.kernel.utils.AudioFilePictureUtils;
import net.sourceforge.atunes.utils.StringUtils;
import net.sourceforge.atunes.utils.TimeUtils;
import net.sourceforge.atunes.utils.language.LanguageTool;

import org.apache.log4j.Logger;


public class VisualHandler {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(VisualHandler.class);
	
	/**
	 * Reference to the Kernel
	 */
	Kernel kernel;
	
	/**
	 * Frames
	 */
	Frame frame;
	
	/**
	 *  Dialogs
	 */
	OSDDialog osdDialog;
	private ProgressDialog progressDialog;
	private EditTagDialog editTagDialog;
	private ExportOptionsDialog exportDialog;
	private StatsDialog statsDialog;
	private CopyProgressDialog exportProgressDialog;
	private CopyProgressDialog copyProgressDialog;
	private SearchDialog searchDialog;
	RipCdDialog ripCdDialog;
	private RipperProgressDialog ripperProgressDialog;
	private IndeterminateProgressDialog indeterminateProgressDialog;
	private EditTitlesDialog editTitlesDialog;
	private EditPreferencesDialog editPreferencesDialog;
	private AboutDialog aboutDialog;
	
	private WindowStateListener fullFrameStateListener;
	
	public VisualHandler() {
		this.kernel = Kernel.getInstance();
		if (!kernel.state.isMultipleWindow())
			frame = new StandardFrame(this);
		else
			frame = new MultipleFrame(this);
	}
	
	public void startVisualization() {
		logger.debug("Starting visualization");
		
		frame.create();
		
		// Create drag and drop listener
		new DragAndDropListener();
		
		showProgressBar(false);
		showIconOnStatusBar(null);
		
		SwingUtilities.updateComponentTreeUI(frame.getFrame());
		logger.debug("Start visualization done");
	}
	
	public void finish() {
		if (!kernel.state.isShowSystemTray())
			kernel.finish();
	}
	
	public void setFullFrameVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	public void setLeftStatusBarText(String text) {
		frame.setLeftStatusBarText(text);
	}
	
	public void setCenterStatusBarText(String text) {
		frame.setCenterStatusBar(text);
	}
	
	public void setRightStatusBarText(String text) {
		frame.setRightStatusBar(text);
	}
	
	public void showRepositorySongNumber(long size, long sizeInBytes, long duration) {
		setCenterStatusBarText(LanguageTool.getString("REPOSITORY") + ": " + 
				               size + ' ' + LanguageTool.getString("SONGS") + " (" +
				               StringUtils.fromByteToMegaOrGiga(sizeInBytes) + ") (" +
				               StringUtils.fromSecondsToHoursAndDays(duration) + ')');
	}
	
	public void showPlaylistSongNumber(int count) {
		setRightStatusBarText(LanguageTool.getString("PLAYLIST") + ": " + count + ' ' + LanguageTool.getString("SONGS"));
	}
	
	public void showIconOnStatusBar(ImageIcon img) {
		if (img == null)
			frame.showStatusBarImageLabel(false);
		else {
			frame.setStatusBarImageLabelIcon(img);
			frame.showStatusBarImageLabel(true);
		}
	}
	
	public void showProgressBar(boolean show) {
		frame.showProgressBar(show);
	}

	public void setTitleBar(String text) {
	    String fullText = (!text.equals("")) ? text +  " - " + Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER : Constants.APP_NAME + ' ' + Constants.APP_VERSION_NUMBER;
   		frame.setTitle(fullText);
   		HandlerProxy.getSystemTrayHandler().setTrayToolTip(fullText);
	}
	
	public void showOSD(AudioFile nextFile) {
		final String title = nextFile.getTitleOrFileName();
		final String album = nextFile.getAlbum();
		final String artist = nextFile.getArtist();
		final String durationString = TimeUtils.seconds2String(nextFile.getDuration());
		ImageIcon image = AudioFilePictureUtils.getInsidePicture(nextFile, -1, -1);
		if (image == null)
			image = AudioFilePictureUtils.getExternalPicture(nextFile, -1, -1);
		
		final ImageIcon imageToShow = image;
		Runnable runn = new Runnable() {
			public void run() {
				if (osdDialog == null)
					osdDialog = new OSDDialog();
				osdDialog.showDialog(imageToShow, title + " (" + durationString + ')', album, artist, Kernel.getInstance().state.OSD_DURATION * 1000);
			}
		};
		Thread t = new Thread(runn);
		t.start();
	}
	
	public ProgressDialog getProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(frame.getFrame());
			progressDialog.getCancelButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getRepositoryHandler().notifyCancel();
				}
			});
		}
		return progressDialog;
	}
	
	public EditTagDialog getEditTagDialog() {
		if (editTagDialog == null) {
			editTagDialog = new EditTagDialog(frame.getFrame());
		}
		return editTagDialog;
	}
	
	public ExportOptionsDialog getExportDialog() {
		if (exportDialog == null) {
			exportDialog = new ExportOptionsDialog(frame.getFrame());
		}
		return exportDialog;
	}

	public void showSongProperties(boolean show, boolean update) {
		kernel.state.setShowSongProperties(show);
		frame.showSongProperties(show);
		HandlerProxy.getControllerHandler().getMenuController().setShowSongProperties(show);
		HandlerProxy.getControllerHandler().getToolBarController().setShowSongProperties(show);
		if (show && update)
			HandlerProxy.getControllerHandler().getFilePropertiesController().updateValues(HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile());
	}
	
	public void showNavigationTable(boolean show) {
		kernel.state.setShowNavigationTable(show);
		frame.showNavigationTable(show);
	}

	public CopyProgressDialog getExportProgressDialog() {
		if (exportProgressDialog == null) {
			exportProgressDialog = new CopyProgressDialog(frame.getFrame(), LanguageTool.getString("EXPORTING") + "...");
		}
		return exportProgressDialog;
	}

	public StatsDialog getStatsDialog() {
		if (statsDialog == null)
			statsDialog = new StatsDialog();
		return statsDialog;
	}
	
	public CopyProgressDialog getCopyProgressDialog() {
		if (copyProgressDialog == null) {
			copyProgressDialog = new CopyProgressDialog(frame.getFrame(), LanguageTool.getString("COPYING") + "...");
		}
		return copyProgressDialog;
	}

	public Kernel getKernel() {
		return kernel;
	}

	public static void showTitle() {
		if (Kernel.getInstance().state.SHOW_TITLE)
			GuiBackgroundExecutor.showApplicationTitle();
	}
	
	public static void hideTitle() {
		if (Kernel.getInstance().state.SHOW_TITLE)
			GuiBackgroundExecutor.hideApplicationTitle();
	}

	public void showIndeterminateProgressDialog(final String text) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					getProgressDialog().setVisible(true);
					getProgressDialog().getLabel().setText(text);
					getProgressDialog().getProgressBar().setIndeterminate(true);
				}
			});
		} catch (Exception e) {
			logger.debug(e);
		}
	}
	
	public void hideIndeterminateProgressDialog() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					getProgressDialog().setVisible(false);
				}
			});
		} catch (Exception e) {
		}
	}

	public void toggleWindowVisibility() {
		frame.setVisible(!frame.isVisible());
		frame.getFrame().setState(java.awt.Frame.NORMAL);
	}
	
	public void showRepositorySelectionInfoDialog() {
		new RepositorySelectionInfoDialog(frame.getFrame()).setVisible(true);
	}
	
	public Point getWindowLocation() {
		return frame.getLocation();
	}
	
	public Dimension getWindowSize() {
		return frame.getSize();
	}
	
	public boolean isMaximized() {
		 return frame.getExtendedState() == java.awt.Frame.MAXIMIZED_BOTH;
	}
	
	public SearchDialog getSearchDialog() {
		if (searchDialog == null)
			searchDialog = new SearchDialog(frame.getFrame());
		return searchDialog;
	}

	public void showAudioScrobblerPanel(boolean show, boolean changeSize) {
		kernel.state.setUseAudioScrobbler(show);
		frame.showAudioScrobblerPanel(show, changeSize);
		if (show)
			HandlerProxy.getControllerHandler().getAudioScrobblerController().updatePanel(HandlerProxy.getPlayerHandler().getCurrentPlayList().getCurrentFile());
		HandlerProxy.getControllerHandler().getToolBarController().setShowAudioScrobblerPanel(show);
	}
	
	public void showNavigationPanel(boolean show, boolean changeSize) {
		kernel.state.setShowNavigationPanel(show);
		frame.showNavigationPanel(show, changeSize);
		HandlerProxy.getControllerHandler().getMenuController().setShowNavigationPanel(show);
		HandlerProxy.getControllerHandler().getToolBarController().setShowNavigatorPanel(show);
	}

	public RipCdDialog getRipCdDialog() {
		if (ripCdDialog == null) {
			ripCdDialog = new RipCdDialog(frame.getFrame());
			ripCdDialog.getAmazonButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getRipperHandler().fillSongsFromAmazon(ripCdDialog.getArtistTextField().getText(), ripCdDialog.getAlbumTextField().getText());
				}
			});
			KeyListener l = new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					final String artist = ripCdDialog.getArtistTextField().getText();
					final String album = ripCdDialog.getAlbumTextField().getText();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							boolean enabled = !artist.equals("") && !album.equals("");
							ripCdDialog.getAmazonButton().setEnabled(enabled);
							if (enabled)
								ripCdDialog.getFolderName().setText(artist + '/' + album);
							else if (artist.equals(""))
								ripCdDialog.getFolderName().setText(album);
							else
								ripCdDialog.getFolderName().setText(artist);
						}
					});
				}
			};
			ripCdDialog.getArtistTextField().addKeyListener(l);
			ripCdDialog.getAlbumTextField().addKeyListener(l);
		}
		return ripCdDialog;
	}
	
	public RipperProgressDialog getRipperProgressDialog() {
		if (ripperProgressDialog == null) {
			ripperProgressDialog = new RipperProgressDialog();
			ripperProgressDialog.getCancelButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HandlerProxy.getRipperHandler().cancelProcess();
				}
			});
		}
		return ripperProgressDialog;
	}
	
	public IndeterminateProgressDialog getIndeterminateProgressDialog() {
		if (indeterminateProgressDialog == null)
			indeterminateProgressDialog = new IndeterminateProgressDialog(frame.getFrame());
		return indeterminateProgressDialog;
	}

	public WindowStateListener getWindowStateListener() {
		if (fullFrameStateListener == null) {
			fullFrameStateListener = new WindowAdapter() {
				public void windowStateChanged(WindowEvent e) {
					if ((e.getNewState() & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED) {
						if (kernel.state.isShowSystemTray())
							frame.setVisible(false);
					}
				}
//				public void windowClosing(WindowEvent e) {
//					if (kernel.state.isShowSystemTray())
//						frame.setVisible(false);
//				}
			};
		}
		return fullFrameStateListener;
	}
	
	public PlayListTable getPlayListTable() {
		return frame.getPlayListTable();
	}
	
	public PlayListTableModel getPlayListTableModel() {
		return (PlayListTableModel) getPlayListTable().getModel();
	}
	
	public void showAboutDialog() {
		if (aboutDialog == null)
			aboutDialog = new AboutDialog(frame.getFrame());
		aboutDialog.setVisible(true);
	}
	
	public void setFrameDefaultCloseOperation(int op) {
		frame.setDefaultCloseOperation(op);
	}
	
	public int showConfirmationDialog(String message, String title) {
		return JOptionPane.showConfirmDialog(frame.getFrame(), message, title, JOptionPane.YES_NO_OPTION);
	}
	
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(frame.getFrame(), message);
	}
	
	public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(frame.getFrame(), message, LanguageTool.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
	}

	public void setFullFrameExtendedState(int state) {
		frame.setExtendedState(state);
	}
	
	public void setFullFrameLocation(Point location) {
		frame.setLocation(location);
	}

	public void setFullFrameLocationRelativeTo(Component c) {
		frame.setLocationRelativeTo(c);
	}
	
	public FileSelectionDialog getFileSelectionDialog(boolean dirOnly) {
		return new FileSelectionDialog(frame.getFrame(), dirOnly);
	}

	public EditTitlesDialog getEditTitlesDialog() {
		if (editTitlesDialog == null)
			editTitlesDialog = new EditTitlesDialog(frame.getFrame());
		return editTitlesDialog;
	}
	
	public FilePropertiesPanel getPropertiesPanel() {
		return frame.getPropertiesPanel();
	}
	
	public ApplicationMenuBar getMenuBar() {
		return frame.getAppMenuBar();
	}
	
	public ToolBar getToolBar() {
		return frame.getToolBar();
	}
	
	public NavigationPanel getNavigationPanel() {
		return frame.getNavigationPanel();
	}
	
	public PlayListPanel getPlayListPanel() {
		return frame.getPlayListPanel();
	}
	
	public void showImageDialog(AudioFile file) {
		new ImageDialog(frame.getFrame(), file);
	}
	
	public int showSaveDialog(JFileChooser fileChooser, FileFilter filter) {
		fileChooser.setFileFilter(filter);
		return fileChooser.showSaveDialog(frame.getFrame());
	}

	public int showOpenDialog(JFileChooser fileChooser, FileFilter filter) {
		fileChooser.setFileFilter(filter);
		return fileChooser.showOpenDialog(frame.getFrame());
	}
	
	public void updateStatusBar(AudioFile song) {
		String text = "<html>" + LanguageTool.getString("PLAYING") + ": ";
		if (song.getTag() == null)
			text = text + "<b>" + song.getName() + "</b></html>";
		else {
			if (song.getTag().getTitle() == null || song.getTag().getTitle().equals(""))
				text = text + "<b>" + song.getName() + "</b> - ";
			else
				text = text + "<b>" + song.getTag().getTitle() + "</b> - ";
			if (song.getTag().getArtist() == null || song.getTag().getArtist().equals(""))
				text = text + "<b>" + LanguageTool.getString("UNKNOWN_ARTIST") + "</b> ";
			else
				text = text + "<b>" + song.getTag().getArtist() + "</b> ";
			text = text +  '(' + TimeUtils.seconds2String(song.getDuration()) + ")</html>";
		}
		setLeftStatusBarText(text);
	}
	
	public void updateStatusBar(String text) {
		setLeftStatusBarText(text);
	}
	
	public void updateTitleBar(AudioFile song) {
		if (song != null) {
			String text = "";
			if (song.getTag() == null)
				text = text + song.getName();
			else {
				text = text + song.getTitleOrFileName() + " - " + song.getTag().getArtist() + " - ";
				if (song.getTag().getAlbum() == null || song.getTag().getAlbum().equals(""))
					text = text + LanguageTool.getString("UNKNOWN_ALBUM");
				else
					text = text + song.getTag().getAlbum();
			}
			text = text + " (" + TimeUtils.seconds2String(song.getDuration()) + ')';
			updateTitleBar(text);
		}
		else
			updateTitleBar("");
	}

	public void updateTitleBar(String text) {
		HandlerProxy.getVisualHandler().setTitleBar(text);
	}
	
	public void updatePlayState(PlayState state) {
		getPlayListTable().setPlayState(state);
	}
	
	public void showInfo(AudioFile file) {
		FilePropertiesPanel propertiesPanel = new FilePropertiesPanel();
		FilePropertiesController propertiesController = new FilePropertiesController(propertiesPanel);
		JDialog dialog = new JDialog(frame.getFrame(), LanguageTool.getString("INFO_OF_FILE") + ' ' + file.getName(), false);
		dialog.setContentPane(propertiesPanel);
		propertiesController.updateValues(file);
		dialog.setSize(650,200);
		dialog.setLocationRelativeTo((Component)frame);
		dialog.setVisible(true);
	}
	
	public void showInfo() {
		PlayListTable table = getPlayListTable();
		int[] rows = table.getSelectedRows();
		if (rows.length == 1) {
			AudioFile file = ((PlayListTableModel)table.getModel()).getFileAt(rows[0]);
			showInfo(file);
		}
	}

	public AudioScrobblerPanel getAudioScrobblerPanel() {
		return frame.getAudioScrobblerPanel();
	}
	
	public boolean isMultipleWindow() {
		return frame instanceof MultipleFrame;
	}
	
	public void repaint() {
		frame.getFrame().invalidate();
		frame.getFrame().validate();
		frame.getFrame().repaint();
	}

	public ComponentListener getStandardWindowListener() {
		final int minHeight = 410;
		return new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				JFrame f = frame.getFrame();
				
				if (frame.getPlayListPanel().getSize().width < StandardFrame.playListPanelWidth) // Avoid resize
					f.setSize(f.getWidth() + (StandardFrame.playListPanelWidth - frame.getPlayListPanel().getSize().width + 10), f.getHeight());
				
				if (f.getHeight() < minHeight)
					f.setSize(f.getWidth(), minHeight);
			}
		};
	}
	
	public void setPlaying(boolean playing) {
		HandlerProxy.getControllerHandler().getPlayerControlsController().setPlaying(playing);
		HandlerProxy.getSystemTrayHandler().setPlaying(playing);
	}
	
	public EditPreferencesDialog getEditPreferencesDialog() {
		if (editPreferencesDialog == null)
			editPreferencesDialog = new EditPreferencesDialog(frame.getFrame());
		return editPreferencesDialog;
	}
	
	public void showStatusBar(boolean show) {
		Kernel.getInstance().state.setShowStatusBar(show);
		frame.showStatusBar(show);
		repaint();
	}
	
	public void showFilter(boolean show) {
		frame.getPlayListPanel().getPlayListFilter().setVisible(show);
	}
	
	public void setVolume(int volume) {
		frame.getPlayListPanel().getPlayerControls().getVolumeSlider().setValue(volume);
	}
}
