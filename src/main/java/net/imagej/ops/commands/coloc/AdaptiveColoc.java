package net.imagej.ops.commands.coloc;

import java.util.Arrays;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.coloc.pValue.PValueResult;
import net.imagej.ops.coloc.saca.QNorm;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import org.scijava.ItemIO;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.table.Column;
import org.scijava.table.DefaultColumn;
import org.scijava.table.DefaultGenericTable;
import org.scijava.table.DoubleColumn;
import org.scijava.table.GenericTable;

@Plugin(type=Command.class, menuPath="Analyze>Colocalization>AdaptiveColoc...")
public class AdaptiveColoc<T extends RealType<T>> implements Command {

	@Parameter
	private OpService ops;
	
	@Parameter
	private StatusService status;
	
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
	
	@Parameter(label = "SACA alpha cutoff")
	private double alpha = 0.05;

	@Parameter(type=ItemIO.OUTPUT)
	private GenericTable globalResults;
	
	@Parameter(type=ItemIO.OUTPUT, label="Heat Map of Z-Scores")
	private Img<DoubleType> heatmap;
	
	@Parameter(type=ItemIO.OUTPUT, label="Significant Z-Scores Only")
	private Img<BitType> sigBinary;

	@Override
	public void run() {
		
		int chunks = 0;
		int progress = 0;
		if(icq) chunks++;
		if(kTau) chunks++;
		if(pearsons) chunks++;
		if(mtkt) chunks++;
		if(saca) chunks+=2;
		status.showStatus(0, chunks, "Starting AdaptiveColoc...");
		
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
		
		globalResults = new DefaultGenericTable();
		globalResults.add(algorithmColumn);
		globalResults.add(pValueColumn);
		globalResults.add(colocValueColumn);
		globalResults.add(colocArrayColumn);
		
		// calculate colocalization metrics
		if (icq) {
			updateStatus(++progress, chunks, "Calculating ICQ...");
			calculateRow(Ops.Coloc.ICQ.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "ICQ", psfSize);
		} 
		if(kTau) {
			updateStatus(++progress, chunks, "Calculating kTau...");
			calculateRow(Ops.Coloc.KendallTau.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "K-Tau", psfSize);
		} 
		if(pearsons) {
			updateStatus(++progress, chunks, "Calculating Pearsons...");
			calculateRow(Ops.Coloc.Pearsons.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "Pearsons", psfSize);
		} 
		if(mtkt) {
			updateStatus(++progress, chunks, "Calculating MTKT...");
			calculateRow(Ops.Coloc.MaxTKendallTau.class, algorithmColumn, pValueColumn, colocValueColumn, colocArrayColumn, "MTKT", psfSize);
		}
		if(saca) {
			updateStatus(++progress, chunks, "Calculating SACA heatmap...");
			heatmap = Util.getSuitableImgFactory(image1, new DoubleType()).create(image1);
			ops.coloc().saca(heatmap, image1, image2);
			updateStatus(++progress, chunks, "Calculating SACA significant pixels...");
			sigBinary = Util.getSuitableImgFactory(image1, new BitType()).create(image1);
			double thres = QNorm.compute(alpha/Intervals.numElements(heatmap), 0, 1, false, false);
			ops.threshold().apply(sigBinary, heatmap, new DoubleType(thres));
		}
		
		globalResults.setRowCount(algorithmColumn.size());
		status.clearStatus();
	}
	
	private void updateStatus(int i, int chunks, String string) {
		// TODO Auto-generated method stub
		status.showStatus(i, chunks, string);
		System.out.println(i + " / " + chunks + " / " + string);
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
	
	
}
