#@ Integer (value = 256) width
#@ Double (value = 5) scale
#@ Integer (value = 10) nLines
#@output ImagePlus result

import ij.ImagePlus
import ij.gui.NewImage
import ij.gui.PolygonRoi
import ij.gui.Roi
import ij.plugin.ImageCalculator
import ij.process.ImageProcessor

imp = NewImage.createShortImage("Source", width, width, 1, NewImage.FILL_BLACK)
final ImageProcessor ip = imp.getProcessor()
// Add a ramp

ip.setColor(1000)

// Draw structures.
pxs = new int[nLines - 1]
pys1 = new int[nLines - 1]
pys2 = new int[nLines - 1]
for (i = 0; i < nLines - 1; i++) {
	int ox1 = (2 * i + 1) * width / nLines / 2
	int ox2 = (2 * i + 2) * width / nLines / 2
	int oy1 = width / nLines
	int oy2 = 2 * width / nLines
	ip.setLineWidth(1 * i + 1)
	ip.drawLine(ox1, oy1, ox2, oy2)

	if(i < (nLines - 3))
		ip.fillRect((5 * ox1 / 4) as int, 2 * width / nLines + oy1 as int, 4 * (i + 1), 4 * (i + 1))

	px = width / nLines + (int) ((nLines - 2.0) / nLines * width *
		Math.sin(2 * Math.PI * i / nLines))
	pxs[i] = px
	py = (int) (6 * width / nLines + width / nLines *
		Math.cos(Math.PI * i))
	pys1[i] = py
	pys2[i] = (int) (py + 2 * 1.5 * width / nLines)

	ip.setLineWidth(1)
	ip.drawLine(width / nLines * i as int,
		(int) ((nLines - 2.5) * width / nLines),
		width / nLines * i as int,
		width)
	ip.drawLine((int) (width / nLines * (i + 0.5)) as int,
		(int) ((nLines - 2.5) * width / nLines),
		(int) (width / nLines * (i + 0.5)) as int,
		width)
}
ip.setLineWidth((int) scale)
roi1 = new PolygonRoi(pxs, pys1, pxs.length, Roi.POLYLINE)
roi1.fitSpline()
ip.draw(roi1)
roi2 = new PolygonRoi(pxs, pys2, pxs.length, Roi.POLYLINE)
roi2.fitSpline()
ip.draw(roi2)

// Add a ramp.
ramp = NewImage.createShortImage("Source", width, width, 1, NewImage.FILL_RAMP)
ramp.getProcessor().multiply(1.0 / 5.0)
result = new ImageCalculator().run("add create", imp, ramp)

// Corrupt by noise.
result.getProcessor().noise(500)
