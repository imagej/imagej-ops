/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.help;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.scijava.Context;
import org.scijava.plugin.Parameter;
import org.scijava.prefs.PrefService;

import net.imagej.ops.Namespace;
import net.imagej.ops.Op;
import net.imagej.ops.OpInfo;
import net.imagej.ops.OpService;
import net.imagej.ops.OpUtils;

/**
 * A scrollable tree view of all discovered {@link Op} implementations. The goal
 * of this class is to make it easy to discover the available {@code Ops}, their
 * associated {@link Namespace}, and specific signatures available for these
 * ops.
 * <p>
 * {@code Ops} are sorted with {@code Namespaces} as the top level, then
 * {@code Op} name, finally with {@code Op} signatures as the leaves.
 * </p>
 *
 * @author Mark Hiner <hinerm@gmail.com>
 */
@SuppressWarnings("serial")
public class OpViewer extends JFrame {

	public static final int DEFAULT_WINDOW_WIDTH = 500;
	public static final int DEFAULT_WINDOW_HEIGHT = 700;
	public static final String WINDOW_HEIGHT = "op.viewer.height";
	public static final String WINDOW_WIDTH = "op.viewer.width";
	public static final String NO_NAMESPACE = "default namespace";

	@Parameter
	private OpService opService;

	@Parameter
	private PrefService prefService;

	public OpViewer(final Context context) {
		super("Op Viewer");
		context.inject(this);

		// Load the frame size
		loadPreferences();

		// Top node of the JTree
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
			"Available Ops");
		createNodes(top);

		final JTree tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Make the JTree scrollable
		final JScrollPane pane = new JScrollPane(tree,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setContentPane(pane);

		try {
			if (SwingUtilities.isEventDispatchThread()) {
				pack();
			}
			else {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						pack();
					}
				});
			}
		}
		catch (final Exception ie) {
			/* ignore */
		}

		setLocationRelativeTo(null); // center on screen
		requestFocus();
	}

	/**
	 * Load any preferences saved via the {@link PrefService}, such as window
	 * width and height.
	 */
	public void loadPreferences() {
		final Dimension dim = getSize();

		// If a dimension is 0 then use the default dimension size
		if (0 == dim.width) {
			dim.width = DEFAULT_WINDOW_WIDTH;
		}
		if (0 == dim.height) {
			dim.height = DEFAULT_WINDOW_HEIGHT;
		}

		setPreferredSize(new Dimension(prefService.getInt(WINDOW_WIDTH, dim.width),
			prefService.getInt(WINDOW_HEIGHT, dim.height)));
	}

	/**
	 * Helper method to populate the {@link Op} nodes. Ops without a valid name
	 * will be skipped. Ops with no namespace will be put in a
	 * {@link #NO_NAMESPACE} category.
	 */
	private void createNodes(final DefaultMutableTreeNode top) {
		// Map namespaces and ops to their parent tree node
		final Map<String, DefaultMutableTreeNode> namespaces =
			new HashMap<String, DefaultMutableTreeNode>();

		final Map<String, DefaultMutableTreeNode> ops =
			new HashMap<String, DefaultMutableTreeNode>();

		// Iterate over all ops
		for (final OpInfo info : opService.infos()) {
			final String namespace = getName(info.getNamespace(), NO_NAMESPACE);

			// Get the namespace node for this Op
			final DefaultMutableTreeNode nsCategory = getCategory(top, namespaces,
				namespace);

			final String opName = getName(info.getSimpleName(), info.getName());

			if (!opName.isEmpty()) {
				// get the general Op node for this Op
				final DefaultMutableTreeNode opCategory = getCategory(nsCategory, ops,
					opName);

				// Create a leaf node for this particular Op's signature
				final DefaultMutableTreeNode opSignature = new DefaultMutableTreeNode(
					OpUtils.opString(info.cInfo()));

				opCategory.add(opSignature);
			}
		}
	}

	/**
	 * Helper method to get a properly formatted name. {@code name} is tried
	 * first, then {@code backupName} if needed (i.e. {@code name} is {@code null}
	 * or empty).
	 * <p>
	 * The resulting string is trimmed and set to lowercase.
	 * </p>
	 */
	private String getName(String name, final String backupName) {
		if (name == null || name.isEmpty()) name = backupName;

		return name == null ? "" : name.toLowerCase().trim();
	}

	/**
	 * Helper method to retrieved a map category with the specified name. If the
	 * category does not exist yet, it's created, added to the map, and added as a
	 * child to the parent tree node.
	 */
	private DefaultMutableTreeNode getCategory(
		final DefaultMutableTreeNode parent,
		final Map<String, DefaultMutableTreeNode> categoryMap,
		final String categoryName)
	{
		DefaultMutableTreeNode nsCategory = categoryMap.get(categoryName);
		if (nsCategory == null) {
			nsCategory = new DefaultMutableTreeNode(categoryName);
			parent.add(nsCategory);
			categoryMap.put(categoryName, nsCategory);
		}

		return nsCategory;
	}
}
