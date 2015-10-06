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

package net.sourceforge.atunes.gui.views.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sourceforge.atunes.gui.FontSingleton;
import net.sourceforge.atunes.gui.views.controls.CustomButton;
import net.sourceforge.atunes.gui.views.controls.CustomDialog;
import net.sourceforge.atunes.utils.language.LanguageTool;


public class FileSelectionDialog extends CustomDialog {

	private static final long serialVersionUID = -1612490779910952274L;
	JTree fileSystemTree;
	JList fileSystemList;
	private JLabel selection;
	private JButton okButton;
	private JButton cancelButton;
	
	static FileSystemView fsView = FileSystemView.getFileSystemView();
	
	boolean dirOnly;
	
	boolean canceled = true;
	File selectedDir;
	File[] selectedFiles;
	
	public FileSelectionDialog(JFrame owner, boolean dirOnly) {
		super(owner, 650, 400, true);
		this.dirOnly = dirOnly;
		setContent(getContent());
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel(null);
		
		fileSystemTree = new JTree();
		fileSystemTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane scroll1 = new JScrollPane(fileSystemTree);
		
		fileSystemList = new JList();
		setListRenderer();
		JScrollPane scroll2 = new JScrollPane(fileSystemList);
		
		selection = new JLabel();
		selection.setFont(FontSingleton.GENERAL_FONT);
		
		okButton = new CustomButton(null, LanguageTool.getString("OK"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDir = null;
				selectedFiles = null;
				if (dirOnly) {
					if (fileSystemList.getSelectedValue() != null)
						selectedDir = new File(((File)fileSystemList.getSelectedValue()).getAbsolutePath());
					else {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileSystemTree.getSelectionPath().getLastPathComponent();
						selectedDir = ((Directory)node.getUserObject()).file;
					}
				}
				else {
					if (fileSystemList.getSelectedValues().length > 0) {
						Object [] files = fileSystemList.getSelectedValues();
						selectedFiles = new File[files.length];
						System.arraycopy(files, 0, selectedFiles, 0, files.length);
					}
					else {
						selectedFiles = new File[1];
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileSystemTree.getSelectionPath().getLastPathComponent();
						selectedFiles[0] = ((Directory)node.getUserObject()).file;
					}
				}
				canceled = false;
				setVisible(false);
			}
		});
		cancelButton = new CustomButton(null, LanguageTool.getString("CANCEL"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		scroll1.setSize(220, 320);
		scroll1.setLocation(10,10);
		panel.add(scroll1);
		
		scroll2.setSize(400, 320);
		scroll2.setLocation(240, 10);
		panel.add(scroll2);
		
		selection.setSize(600, 20);
		selection.setLocation(10, 335);
		panel.add(selection);

		okButton.setSize(100, 25);
		okButton.setLocation(430, 360);
		panel.add(okButton);

		cancelButton.setSize(100, 25);
		cancelButton.setLocation(540, 360);
		panel.add(cancelButton);
		
		return panel;
	}
	
	public void startDialog() {
		canceled = true;
		setTree();
		setVisible(true);
	}
	
	private void setTree() {
		File[] roots = fsView.getRoots();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		
		for (int i = 0; i < roots.length; i++) {
			File f = roots[i];
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new Directory(f));
			root.add(treeNode);
			File[] files = fsView.getFiles(f,true);
			Arrays.sort(files);
			for (int j = 0; j < files.length; j++) {
				File f2 = files[j];
				if (fsView.isTraversable(f2)) {
					DefaultMutableTreeNode treeNode2 = new DefaultMutableTreeNode(new Directory(f2));
					treeNode.add(treeNode2);
					treeNode2.add(new DefaultMutableTreeNode("Dummy node"));
				}
			}
		}
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		fileSystemTree.setModel(model);
		fileSystemTree.setRootVisible(false);
		fileSystemTree.expandRow(0);
		fileSystemTree.setSelectionRow(0);
		fileSystemList.setListData(getFiles(roots[0]));
		setSelectionText(roots[0]);
		setTreeRenderer();
		fileSystemTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				DefaultMutableTreeNode nodeSelected = (DefaultMutableTreeNode) fileSystemTree.getSelectionPath().getLastPathComponent();
				nodeSelected.removeAllChildren();
				Directory dir = (Directory) nodeSelected.getUserObject();
				File[] files = fsView.getFiles(dir.file, true);
				Arrays.sort(files);
				for (int j = 0; j < files.length; j++) {
					File f = files[j];
					if (fsView.isTraversable(f)) {
						DefaultMutableTreeNode treeNode2 = new DefaultMutableTreeNode(new Directory(f));
						nodeSelected.add(treeNode2);
						treeNode2.add(new DefaultMutableTreeNode("Dummy node"));
					}
				}
			}
			
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
			}
		});
		fileSystemTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				Directory dir = (Directory) node.getUserObject();
				setSelectionText(dir.file);
				File[] files = getFiles(dir.file);
				ArrayList<File> dirsList = new ArrayList<File>();
				ArrayList<File> filesList = new ArrayList<File>();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory())
						dirsList.add(files[i]);
					else
						filesList.add(files[i]);
				}
				Collections.sort(dirsList);
				Collections.sort(filesList);
				dirsList.addAll(filesList);
				fileSystemList.setListData(dirsList.toArray());
			}
		});
		fileSystemList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				File f = (File) fileSystemList.getSelectedValue();
				setSelectionText(f);
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) fileSystemTree.getSelectionPath().getLastPathComponent();
					TreePath path = new TreePath(parentNode.getPath());
					fileSystemTree.expandPath(path);
					int i = 0;
					DefaultMutableTreeNode childToSelect = null;
					while (i < parentNode.getChildCount() || childToSelect == null) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
						if (((Directory)child.getUserObject()).file.equals(f))
							childToSelect = child;
						i++;
					}
					TreeNode[] newPath = new TreeNode[parentNode.getPath().length+1];
					for (int j = 0; j < parentNode.getPath().length; j++)
						newPath[j] = parentNode.getPath()[j];
					newPath[parentNode.getPath().length] = childToSelect;
					
					fileSystemTree.setSelectionPath(new TreePath(newPath));
				}
			}
		});
	}
	
	void setSelectionText(File f) {
		String displayName;
		if (!fsView.isFileSystem(f))
			displayName = fsView.getSystemDisplayName(f);
		else
			displayName = f.getAbsolutePath();
		selection.setText(displayName);
	}
	
	private void setTreeRenderer() {
		fileSystemTree.setCellRenderer(new TreeCellRenderer() {
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				
				if (node.getUserObject() instanceof String)
					return new JLabel();
				
				Directory content = (Directory) node.getUserObject();
				JLabel icon = new JLabel(value.toString());
				icon.setIcon(fsView.getSystemIcon(content.file));
				
				return icon;
			}
		});
	}
	
	private void setListRenderer() {
		fileSystemList.setCellRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				File f = (File) value;
				
				JLabel icon = new JLabel(fsView.getSystemDisplayName(f));
				icon.setHorizontalAlignment(SwingConstants.LEFT);

				icon.setIcon(fsView.getSystemIcon(f));
				return icon;
			}
		});
	}

	private class Directory {
		public File file;
		
		Directory(File file) {
			this.file = file;
		}
		
		public String toString() {
			return fsView.getSystemDisplayName(file);
		}
	}
	
	File[] getFiles(File f) {
		File[] files = fsView.getFiles(f, true);
		ArrayList<File> list = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			if (!dirOnly)
				list.add(files[i]);
			else if (files[i].isDirectory())
				list.add(files[i]);
		}
		return list.toArray(new File[list.size()]);
	}
	
	public boolean isCanceled() {
		return canceled;
	}

	public File getSelectedDir() {
		return selectedDir;
	}

	public File[] getSelectedFiles() {
		return selectedFiles;
	}

	public static void main(String[] args) {
		new FileSelectionDialog(null, true).startDialog();
	}
}
