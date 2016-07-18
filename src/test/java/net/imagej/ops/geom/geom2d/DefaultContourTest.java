package net.imagej.ops.geom.geom2d;

import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.*;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import org.junit.Test;
import org.scijava.Context;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by rhaase on 7/18/16.
 */
public class DefaultContourTest  extends AbstractFeatureTest {


    long[][] positions = {
            {2,2},
            {2,3},
            {2,4},
            {3,4},
            {4,4},
            {4,3},
            {4,2},
            {3,2},
            {3,3}
    };

    @Test
    public void testPolygonCreationOnBitTypeImage()
    {
        Img<BitType> img = ArrayImgs.bits(new long[]{10,10});
        RandomAccess ra = img.randomAccess();

        for (int i = 0; i < positions.length; i++)
        {
            ra.setPosition(positions[i]);
            ((BitType)ra.get()).set(true);
        }

        // print test image on console
        System.out.println(ops.image().ascii(img));

        Polygon bitPolygon = ops.geom().contour(img, false, false);

        printPolygon(bitPolygon);
        assertTrue(bitPolygon.getVertices().size() == 8);
    }

    @Test
    public void testPolygonCreationOnBoolTypeImage()
    {
        // create test image
        Img<FloatType> img = ArrayImgs.floats(new long[]{10,10});
        RandomAccess ra = img.randomAccess();

        for (int i = 0; i < positions.length; i++)
        {
            ra.setPosition(positions[i]);
            ((FloatType)ra.get()).set(1);
        }

        // print test image on console
        System.out.println(ops.image().ascii(img));

        // create label from test image
        LabelRegion region1 = createLabelRegion(img, 1, 1);

        printRegion(region1);

        Polygon boolPolygon1 = ops.geom().contour(region1, false, false);

        printPolygon(boolPolygon1);
    }

    private void printRegion(LabelRegion<?> region)
    {
        Cursor<Void> cur = region.cursor();
        System.out.print(cur);

        int count = 0;
        while (cur.hasNext())
        {
            cur.next();

            count++;
        }
        System.out.println("region contains " + count + " pixels");
    }

    private void printPolygon(Polygon polygon)
    {
        System.out.println("no of vertices: " + polygon.getVertices().size());

        for (RealLocalizable vertex : polygon.getVertices())
        {
            System.out.println("polpos: " + vertex.getDoublePosition(0) + "/" + vertex.getDoublePosition(1));
        }
    }
}