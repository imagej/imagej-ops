package net.imagej.ops.geom.geom3d;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.DefaultMesh;
import net.imagej.ops.geom.geom3d.mesh.Facet;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.geom.geom3d.mesh.TriangularFacet;
import net.imagej.ops.geom.geom3d.mesh.Vertex;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RealLocalizable;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Mesh smoothing.
 * 
 * @author Kyle Harrington, University of Idaho 
 */
@Plugin(type = Ops.Geometric.MeshSmoothing.class)
public class DefaultMeshSmoothing3D extends AbstractUnaryFunctionOp<Mesh, Mesh>
implements Ops.Geometric.MeshSmoothing
{
	@Parameter(type = ItemIO.INPUT, required = true)
	private int smoothingSteps;

	@Override
	public Mesh compute1(final Mesh input) {
		DefaultMesh output = (DefaultMesh) input.copy();
		smooth( output, smoothingSteps );
		return output;
	}	
	
	static class Edge {
		public Vertex a;
		public Vertex b;
		
		public Edge( Vertex a, Vertex b) {
			this.a = a;
			this.b = b;			
		}				
	}

	/**
	 * Smoothing based on 3D viewer's smoothing, which was based on Blender
	 * 
	 * Comments from: https://github.com/fiji/3D_Viewer/blob/master/src/main/java/isosurface/MeshEditor.java
	 * Implemented Blender-style vertex smoothing. See Blender's file
	 * editmesh_mods.c, at function
	 * "static int smooth_vertex(bContext *C, wmOperator *op)" What it does: 1.
	 * For each unique edge, compute the average of both vertices and store it in
	 * a Point3f. Also increment a counter for each vertex indicating that it has
	 * been part of an averaging operation. If the vertex is again part of an
	 * averaging operation, just add the new average to the existing one. 2. For
	 * each unique vertex, computer a factor as 0.5/count, where count is the
	 * number of times that the vertex has been part of an averaging operation.
	 * Then set the value of the vertex to 0.5 times the original coordinates,
	 * plus the factor times the cumulative average coordinates. The result is
	 * beautifully smoothed meshes that don't shrink noticeably. All kudos to
	 * Blender's authors. Thanks for sharing with GPL license.
	 * 
	 */
	static protected void smooth(final DefaultMesh toSmooth, final int iterations)
	{
		// Vertex list
		//final Set<RealLocalizable> verts = toSmooth.getVertices();
		final List<Vertex> verts = new ArrayList<Vertex>();
		for( Facet f : toSmooth.getFacets() ) 
			for( Vertex v : ((TriangularFacet) f).getVertices() )
				verts.add(v);
		
		// Store all edges within the mesh
		final HashSet<int[]> edges = new HashSet<>(); 
		// Store an average Vertex for each Vertex
		final HashMap<Integer,Vertex> vertAverages = new HashMap<>();
		// Store a count of the number of times an average has been taken
		final HashMap<Integer,Integer> vertAverageCount = new HashMap<>();
		
		System.err.println(  "In vert counts = " + verts.size() );
		
		// Collect unique edges (3D Viewer checked if vertices were unique, we assume that, EDIT: probz bad)
		for( Facet f : toSmooth.getFacets() ) {
			TriangularFacet tf = (TriangularFacet) f;// Ew...
			edges.add( new int[]{ verts.indexOf( tf.getVertex(0) ), verts.indexOf( tf.getVertex(1) ) } );
			edges.add( new int[]{ verts.indexOf( tf.getVertex(1) ), verts.indexOf( tf.getVertex(2) ) } );
			edges.add( new int[]{ verts.indexOf( tf.getVertex(0) ), verts.indexOf( tf.getVertex(2) ) } );
		}		
		
		// Initialize intermediate collections
		//for( Vertex v : verts ) {
		for( int k = 0; k < verts.size(); k++ ) {
			vertAverages.put( k, new Vertex(0,0,0) );
			vertAverageCount.put( k, 0 );
		}				
		
		System.err.println( "Num edges = " + edges.size() );

		System.err.println( "Iteration " + -1 + " num verts = " + verts.size() );		
		
		for (int i = 0; i < iterations; ++i) {
			System.err.println( "Iteration " + i + " num verts = " + verts.size() );

			// Take averages over all neighbors per vert
			for (final int[] e : edges) {
				// Increment average count for both vertices
				vertAverageCount.put(e[0], vertAverageCount.get(e[0])+1 );
				vertAverageCount.put(e[1], vertAverageCount.get(e[1])+1 );
				// Compute average
				Vertex avg = new Vertex( ( verts.get( e[0] ).getDoublePosition(0) + verts.get( e[1] ).getDoublePosition(0) ) / 2,
										 ( verts.get( e[0] ).getDoublePosition(1) + verts.get( e[1] ).getDoublePosition(1) ) / 2,
										 ( verts.get( e[0] ).getDoublePosition(2) + verts.get( e[1] ).getDoublePosition(2) ) / 2 );
				// Accumulate averages
				vertAverages.get(e[0]).add(avg);
				vertAverages.get(e[1]).add(avg);
			}

			// Smooth from average for each vert
			for (final Vertex v : verts ) {
				final float f = 0.5f / vertAverageCount.get(v);
				Vertex tmp = vertAverages.get(v);
				vertAverages.put( verts.indexOf(v), new Vertex( 0.5f * v.getDoublePosition(0) + f * tmp.getX(),
												0.5f * v.getDoublePosition(1) + f * tmp.getY(),
												0.5f * v.getDoublePosition(2) + f * tmp.getZ() ) );
			}

			// Update verts
			if (i + 1 < iterations) {
				for (final RealLocalizable v : verts) {
					Vertex newPos = vertAverages.get(v);
					Vertex vtx = (Vertex) v;
					for( int k=0; k < 3; k++ ) {
						vtx.changeDoublePosition(k, newPos.getDoublePosition(k));
					}					
				}
			}
		}
	}

}
