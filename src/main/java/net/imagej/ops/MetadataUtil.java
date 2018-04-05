/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops;

import net.imagej.ImageMetadata;
import net.imagej.ImgPlusMetadata;
import net.imagej.Sourced;
import net.imagej.axis.CalibratedAxis;
import net.imagej.space.CalibratedSpace;
import net.imglib2.Interval;

import org.scijava.Named;

/**
 * Utility class that allows to copy Metadata. It is the responsibility of the
 * user to ensure that the source and target spaces have the correct
 * dimensionality.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Michael Zinsmaier (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 */

// TODO Move to ImgLib2?
public class MetadataUtil {

	private MetadataUtil() {
		// helper class
	}

	/**
	 * copies the source attribute from in to out
	 * 
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static Sourced copySource(final Sourced in, final Sourced out) {
		out.setSource(in.getSource());
		return out;
	}

	/**
	 * copies the name from in to out
	 * 
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static Named copyName(final Named in, final Named out) {
		out.setName(in.getName());
		return out;
	}

	/**
	 * copies the ImageMetadata from in to out
	 * 
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static ImageMetadata copyImageMetadata(final ImageMetadata in,
		final ImageMetadata out)
	{
		out.setValidBits(in.getValidBits());
		out.setCompositeChannelCount(in.getCompositeChannelCount());

		for (int c = 0; c < out.getCompositeChannelCount(); c++) {
			out.setChannelMinimum(c, in.getChannelMinimum(c));
			out.setChannelMaximum(c, in.getChannelMaximum(c));
		}

		out.initializeColorTables(in.getColorTableCount());
		for (int n = 0; n < in.getColorTableCount(); n++) {
			out.setColorTable(in.getColorTable(n), n);
		}

		return out;
	}

	/**
	 * copies the CalibratedSpace attributes from in to out. Both spaces must have
	 * the same dimensionality.
	 * 
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static <C extends CalibratedAxis> CalibratedSpace<C> copyTypedSpace(
		final CalibratedSpace<C> in, final CalibratedSpace<C> out)
	{
		copyTypedSpace(null, in, out);
		return out;
	}

	/**
	 * copies all ImgPlus metadata {@link ImgPlusMetadata} from in to out. The
	 * {@link CalibratedSpace} of in and out must have the same dimensionality.
	 * 
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static ImgPlusMetadata copyImgPlusMetadata(final ImgPlusMetadata in,
		final ImgPlusMetadata out)
	{
		copyName(in, out);
		copySource(in, out);
		copyImageMetadata(in, out);
		copyTypedSpace(in, out);
		return out;
	}

	// with cleaning

	/**
	 * copies the CalibratedAxis attributes from in to out. Attributes for
	 * dimensions of size 1 are removed during copying. The dimensionality of the
	 * out space must be dimensionality(inSpace) - #removed dims
	 * 
	 * @param inInterval provides dimensionality information for the in space
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static <C extends CalibratedAxis> CalibratedSpace<C>
		copyAndCleanTypedSpace(final Interval inInterval,
			final CalibratedSpace<C> in, final CalibratedSpace<C> out)
	{
		copyTypedSpace(inInterval, in, out);
		return out;
	}

	/**
	 * copies all {@link ImgPlusMetadata} from in to out.
	 * CalibratedSpace attributes for dimensions of size 1 are removed during
	 * copying. The dimensionality of the out space must be
	 * dimensionality(inSpace) - #removed dims
	 * 
	 * @param inInterval provides dimensionality information for the in space
	 * @param in
	 * @param out
	 * @return returns out
	 */
	public static ImgPlusMetadata copyAndCleanImgPlusMetadata(
		final Interval inInterval, final ImgPlusMetadata in,
		final ImgPlusMetadata out)
	{
		copyName(in, out);
		copySource(in, out);
		copyImageMetadata(in, out);
		copyAndCleanTypedSpace(inInterval, in, out);
		return out;
	}

	// PRIVATE HELPERS

	// @SuppressWarnings("unchecked")
	// private static < T extends TypedAxis > void copyTypedSpace( Interval
	// inInterval, TypedSpace< T > in, TypedSpace< T > out )
	// {
	//
	// int offset = 0;
	// for (int d = 0; d < in.numDimensions(); d++) {
	// if (inInterval != null && inInterval.dimension(d) == 1) {
	// offset++;
	// } else {
	// out.setAxis((T) in.axis(d).copy(), d - offset);
	// }
	// }
	// }

	@SuppressWarnings("unchecked")
	private static <C extends CalibratedAxis> void copyTypedSpace(
		final Interval inInterval, final CalibratedSpace<C> in,
		final CalibratedSpace<C> out)
	{

		int offset = 0;
		for (int d = 0; d < in.numDimensions(); d++) {
			if (inInterval != null && inInterval.dimension(d) == 1) {
				offset++;
			}
			else {
				out.setAxis((C) in.axis(d).copy(), d - offset);
			}
		}
	}

}
