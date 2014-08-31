/**
 * 
 */
package com.wwidesigner.gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jidesoft.app.framework.DataModel;
import com.jidesoft.app.framework.event.EventSubscriber;
import com.jidesoft.app.framework.event.SubscriberEvent;
import com.jidesoft.app.framework.file.FileDataModel;
import com.jidesoft.app.framework.gui.DataViewPane;
import com.jidesoft.app.framework.gui.filebased.FileBasedApplication;
import com.jidesoft.tree.TreeUtils;
import com.wwidesigner.geometry.Instrument;
import com.wwidesigner.modelling.SketchInstrument;

/**
 * @author kort
 * 
 */
public class StudyView extends DataViewPane implements EventSubscriber
{
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private StudyModel study;

	@Override
	protected void initializeComponents()
	{
		// create file tree
		tree = new JTree();
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if (path != null)
				{
					if (path.getPathCount() == 3) // it is a leaf
					{
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
								.getLastPathComponent();
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
								.getParent();
						study.setCategorySelection(
								(String) parentNode.getUserObject(),
								(String) node.getUserObject());
					}
					updateView();
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(225, 100));
		add(scrollPane);

		System.out.println("NafOptimizationRunner");
		System.out.println("Copyright (C) 2014, Edward Kort, Antoine Lefebvre, Burton Patkau.");
		System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
		System.out.println("This is free software, and you are welcome to redistribute it");
		System.out.println("under the terms of the GNU General Public License, version 3.0.");
		System.out.println();

		Preferences myPreferences = getApplication().getPreferences();
		String modelName = myPreferences.get(OptimizationPreferences.STUDY_MODEL_OPT,
				OptimizationPreferences.NAF_STUDY_NAME);
		setStudyModel(modelName);
		study.setPreferences(myPreferences);

		getApplication().getEventManager().subscribe(
				NafOptimizationRunner.FILE_OPENED_EVENT_ID, this);
		getApplication().getEventManager().subscribe(
				NafOptimizationRunner.FILE_CLOSED_EVENT_ID, this);
		getApplication().getEventManager().subscribe(
				NafOptimizationRunner.FILE_SAVED_EVENT_ID, this);
		getApplication().getEventManager().subscribe(
				NafOptimizationRunner.WINDOW_RENAMED_EVENT_ID, this);
	}

	protected void updateView()
	{
		// Build the selection tree.

		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		List<TreePath> selectionPaths = new ArrayList<TreePath>();
		
		// Add all static categories and selection options to the tree.

		for (String category : study.getCategoryNames())
		{
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
			if (node != null)
			{
				node.setAllowsChildren(true);
				root.add(node);
			}
			String selectedSub = study.getSelectedSub(category);
			for (String name : study.getSubcategories(category))
			{
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
						name);
				if (childNode != null)
				{
					node.add(childNode);
					if (name.equals(selectedSub))
					{
						selectionPaths.add(new TreePath(childNode.getPath()));
					}
				}
			}
		}
		
		TreeModel model = new DefaultTreeModel(root);
		tree.setModel(model);
		TreeUtils.expandAll(tree);
		tree.setSelectionPaths(selectionPaths.toArray(new TreePath[0]));
		updateActions();
	}

	protected void updateActions()
	{
		boolean canDo = study.canTune();
		getApplication().getEventManager().publish(
				NafOptimizationRunner.TUNING_ACTIVE_EVENT_ID, canDo);

		canDo = study.canOptimize();
		getApplication().getEventManager().publish(
				NafOptimizationRunner.OPTIMIZATION_ACTIVE_EVENT_ID, canDo);
	}

	@Override
	public void doEvent(SubscriberEvent event)
	{
		String eventId = event.getEvent();
		Object eventSource = event.getSource();
		if (eventSource instanceof FileDataModel)
		{
			FileDataModel source = (FileDataModel) eventSource;
			switch (eventId)
			{
				case NafOptimizationRunner.FILE_OPENED_EVENT_ID:
					if (! study.addDataModel(source))
					{
						System.out.print("\nError: Data in editor tab, ");
						System.out.print(source.getName());
						System.out.println(", is not valid Instrument or Tuning XML.");
						System.out.println("Fix and close the file, then re-open it.");
					}
					break;
				case NafOptimizationRunner.FILE_CLOSED_EVENT_ID:
					study.removeDataModel(source);
					break;
				case NafOptimizationRunner.FILE_SAVED_EVENT_ID:
				case NafOptimizationRunner.WINDOW_RENAMED_EVENT_ID:
					if (! study.replaceDataModel(source))
					{
						System.out.print("\nError: Data in editor tab, ");
						System.out.print(source.getName());
						System.out.println(", is not valid Instrument or Tuning XML.");
						System.out.println("Fix and close the file, then re-open it.");
					}
					break;
			}
			updateView();
		}
	}

	public void getTuning()
	{
		try
		{
			study.calculateTuning("Tuning"); // This a title, not a constant
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void graphTuning()
	{
		try
		{
			study.graphTuning("Tuning"); // This a title, not a constant
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void optimizeInstrument()
	{
		try
		{
			String xmlInstrument = study.optimizeInstrument();
			if (xmlInstrument != null && ! xmlInstrument.isEmpty())
			{
				FileBasedApplication app = (FileBasedApplication) getApplication();
				FileDataModel data = (FileDataModel) app.newData("xml");
				data.setData(xmlInstrument);
				study.addDataModel(data);
				updateView();
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void sketchInstrument()
	{
		try
		{
			SketchInstrument sketch = new SketchInstrument();
			sketch.draw(study.getInstrument(), false);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void compareInstrument()
	{
		try
		{
			FileBasedApplication app = (FileBasedApplication) getApplication();
			DataModel data = app.getFocusedModel();
			if (data != null)
			{
				CodeEditorView view = (CodeEditorView) app.getDataView(data);
				if (view != null)
				{
					String xmlInstrument2 = view.getText();
					Instrument  instrument2 = StudyModel.getInstrument(xmlInstrument2);
					if (instrument2 == null)
					{
						System.out.print("\nError: Current editor tab, ");
						System.out.print(data.getName());
						System.out.println(", is not a valid instrument.");
						System.out.println("Select the edit tab for a valid instrument.");
						return;
					}
					study.compareInstrument(data.getName(), instrument2 );
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public StudyModel getStudyModel()
	{
		return study;
	}

	/**
	 * Set the study model given a study class name.
	 */
	public void setStudyModel(StudyModel study)
	{
		this.study = study;

		DataModel[] models = getApplication().getDataModels();
		for ( DataModel model : models )
		{
			if (model instanceof FileDataModel)
			{
				if (! study.addDataModel((FileDataModel) model))
				{
					System.out.print("\nError: Data in editor tab, ");
					System.out.print(model.getName());
					System.out.println(", is not valid Instrument or Tuning XML.");
				}
			}
		}
		updateView();
	}

	/**
	 * Set the study model to a specified object.
	 */
	public void setStudyModel(String studyClassName)
	{
		if (studyClassName.contentEquals(OptimizationPreferences.NAF_STUDY_NAME))
		{
			setStudyModel( new NafStudyModel() );
		}
		else if (studyClassName.contentEquals(OptimizationPreferences.WHISTLE_STUDY_NAME))
		{
			setStudyModel( new WhistleStudyModel() );
		}
		else
		{
			// Default study model.
			setStudyModel( new WhistleStudyModel() );
		}
	}

}
