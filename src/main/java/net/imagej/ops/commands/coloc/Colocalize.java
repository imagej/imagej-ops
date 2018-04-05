package net.imagej.ops.commands.coloc;

import java.util.Arrays;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.table.Column;
import org.scijava.table.DefaultColumn;
import org.scijava.table.DefaultGenericTable;
import org.scijava.table.DoubleColumn;
import org.scijava.table.GenericTable;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.coloc.pValue.PValueResult;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

@Plugin(type=Command.class, menuPath="Analyze>Colocalization>Colocalize...")
public class Colocalize<T extends RealType<T>> implements Command {

	@Parameter
	private OpService ops;
	
	@Parameter(callback="imageChanged")
	private Img<T> image1;
	
	@Parameter(callback="imageChanged")
	private Img<T> image2;
	
	@Parameter(label = "PSF Size", description = "<html>Size of blocks for random shufflings.<br>"
			+ "Specify psf size using comma-separated numbers.<br>"
			+ "Otherwise, leave blank for auto-calculated default values.", required = false)
	private String psfSizeString;
	
	@Parameter(label = "ICQ")
	private boolean icq;
	
	@Parameter(label = "K-Tau")
	private boolean kTau;
	
	@Parameter(label = "Pearsons")
	private boolean pearsons;
	
	@Parameter(label = "MTKT")
	private boolean mtkt;

	@Parameter(label = "SACA")
	private boolean saca;

	@Parameter(type=ItemIO.OUTPUT)
	private GenericTable table;

	@Override
	public void run() {
		
		// step one - check input parameters dimensionality and type-match
		final long[] psfDims;
		if (psfSizeString == null || psfSizeString.trim().isEmpty()) {
			psfDims = null;
		} else {
			psfDims = Arrays.asList(psfSizeString.trim().split(",")).stream()//
					.mapToLong(s -> Long.parseLong(s.trim()))//
					.toArray();
		}
		final FinalDimensions psfSize;
		if (psfDims == null || psfDims.length == 0) {
			psfSize = null;
		} else {
			psfSize = new FinalDimensions(psfDims);
		}
		
		Column<String> algorithmColumn = new DefaultColumn<>(String.class, "Algorithm");
		DoubleColumn pValueColumn = new DoubleColumn("P-Value");
		DoubleColumn colocValueColumn = new DoubleColumn("Coloc-Value");
		Column<double[]> colocArrayColumn = new DefaultColumn<>(double[].class, "All Coloc Values");
		
		table = new DefaultGenericTable();
		table.add(algorithmColumn);
		table.add(pValueColumn);
		table.add(colocValueColumn);
		table.add(colocArrayColumn);
		
		// calculate colocalization metrics
		if (icq) {
			calculateRow(Ops.Coloc.ICQ.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "ICQ", psfSize);
		} 
		if(kTau) {
			calculateRow(Ops.Coloc.KendallTau.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "K-Tau", psfSize);
		} 
		if(pearsons) {
			calculateRow(Ops.Coloc.Pearsons.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "Pearsons", psfSize);
		} 
		if(mtkt) {
			calculateRow(Ops.Coloc.MaxTKendallTau.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "MTKT", psfSize);
		}
		if(saca) {
			// TODO: generate pixel-wise image output
		}
		
		table.setRowCount(algorithmColumn.size());
	}
	
	private void calculateRow(Class<? extends Op> opType, Column<String> algorithmColumn, DoubleColumn pValueColumn,
			DoubleColumn colocValueColumn, Column<double[]> colocArrayColumn, String algorithmName, Dimensions psfSize) {
		BinaryFunctionOp<Iterable<T>, Iterable<T>, Double> colocOp = Functions.binary(ops, opType, Double.class, image1, image2);
		PValueResult result = new PValueResult();
		ops.run(Ops.Coloc.PValue.class, result, image1, image2, colocOp, 100, psfSize);
		algorithmColumn.add(algorithmName);
		pValueColumn.add(result.getPValue());
		colocValueColumn.add(result.getColocValue());
		colocArrayColumn.add(result.getColocValuesArray());
	}

	@SuppressWarnings("unused")
	private void imageChanged() {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int d = 0; d < image1.numDimensions(); d++) {
			final long size = (long) Math.floor(Math.sqrt(image1.dimension(d)));
			if (first) first = false;
			else sb.append(",");
			sb.append(size);
		}
		psfSizeString = sb.toString();
	}
//	/* TODO PIXEL-WISE - to be implemented in second iteration of Colocalize command
//	 * OUTPUTS:
//	 * 1) array of z-scores
//	 * 2) array of SigPixel (0 or 1) - binary mask overlay
//	 */
////	@Parameter(type=ItemIO.OUTPUT)
////	private Img<DoubleType> heatMap; // PIXEL-WISE OUTPUT
//
//	// OPTION: add Parameter for neighborhood size/shape for pixel-wise measure
}
