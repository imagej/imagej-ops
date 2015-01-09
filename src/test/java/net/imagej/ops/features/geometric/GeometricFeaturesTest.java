package net.imagej.ops.features.geometric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.FeatureResult;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.sets.GeometricFeatureSet;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.RealPoint;

import org.junit.Before;
import org.junit.Test;

/**
 * To get comparable values with ImageJ I created an image of a polygon, read
 * that image into ImageJ and used the Wand (tracing) tool to select the polygon
 * and used the corners of this polygon here.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
public class GeometricFeaturesTest extends AbstractFeatureTest {

	private Map<String, Double> results = new HashMap<String, Double>();

	@Before
	public void setup() {

		// create a polygon
		Polygon p = new Polygon();
		p.add(new RealPoint(444, 183));
		p.add(new RealPoint(445, 183));
		p.add(new RealPoint(445, 184));
		p.add(new RealPoint(446, 184));
		p.add(new RealPoint(446, 185));
		p.add(new RealPoint(447, 185));
		p.add(new RealPoint(447, 186));
		p.add(new RealPoint(448, 186));
		p.add(new RealPoint(448, 187));
		p.add(new RealPoint(445, 187));
		p.add(new RealPoint(445, 188));
		p.add(new RealPoint(443, 188));
		p.add(new RealPoint(443, 189));
		p.add(new RealPoint(441, 189));
		p.add(new RealPoint(441, 190));
		p.add(new RealPoint(438, 190));
		p.add(new RealPoint(438, 191));
		p.add(new RealPoint(436, 191));
		p.add(new RealPoint(436, 192));
		p.add(new RealPoint(434, 192));
		p.add(new RealPoint(434, 193));
		p.add(new RealPoint(432, 193));
		p.add(new RealPoint(432, 194));
		p.add(new RealPoint(429, 194));
		p.add(new RealPoint(429, 195));
		p.add(new RealPoint(427, 195));
		p.add(new RealPoint(427, 196));
		p.add(new RealPoint(425, 196));
		p.add(new RealPoint(425, 197));
		p.add(new RealPoint(422, 197));
		p.add(new RealPoint(422, 198));
		p.add(new RealPoint(420, 198));
		p.add(new RealPoint(420, 199));
		p.add(new RealPoint(418, 199));
		p.add(new RealPoint(418, 200));
		p.add(new RealPoint(415, 200));
		p.add(new RealPoint(415, 201));
		p.add(new RealPoint(413, 201));
		p.add(new RealPoint(413, 202));
		p.add(new RealPoint(411, 202));
		p.add(new RealPoint(411, 203));
		p.add(new RealPoint(408, 203));
		p.add(new RealPoint(408, 204));
		p.add(new RealPoint(406, 204));
		p.add(new RealPoint(406, 205));
		p.add(new RealPoint(404, 205));
		p.add(new RealPoint(404, 206));
		p.add(new RealPoint(402, 206));
		p.add(new RealPoint(402, 207));
		p.add(new RealPoint(399, 207));
		p.add(new RealPoint(399, 208));
		p.add(new RealPoint(397, 208));
		p.add(new RealPoint(397, 209));
		p.add(new RealPoint(395, 209));
		p.add(new RealPoint(395, 210));
		p.add(new RealPoint(392, 210));
		p.add(new RealPoint(392, 211));
		p.add(new RealPoint(390, 211));
		p.add(new RealPoint(390, 212));
		p.add(new RealPoint(388, 212));
		p.add(new RealPoint(388, 213));
		p.add(new RealPoint(385, 213));
		p.add(new RealPoint(385, 214));
		p.add(new RealPoint(383, 214));
		p.add(new RealPoint(383, 215));
		p.add(new RealPoint(381, 215));
		p.add(new RealPoint(381, 216));
		p.add(new RealPoint(378, 216));
		p.add(new RealPoint(378, 217));
		p.add(new RealPoint(376, 217));
		p.add(new RealPoint(376, 218));
		p.add(new RealPoint(374, 218));
		p.add(new RealPoint(374, 219));
		p.add(new RealPoint(371, 219));
		p.add(new RealPoint(371, 220));
		p.add(new RealPoint(369, 220));
		p.add(new RealPoint(369, 221));
		p.add(new RealPoint(367, 221));
		p.add(new RealPoint(367, 222));
		p.add(new RealPoint(365, 222));
		p.add(new RealPoint(365, 223));
		p.add(new RealPoint(362, 223));
		p.add(new RealPoint(362, 224));
		p.add(new RealPoint(360, 224));
		p.add(new RealPoint(360, 225));
		p.add(new RealPoint(358, 225));
		p.add(new RealPoint(358, 226));
		p.add(new RealPoint(355, 226));
		p.add(new RealPoint(355, 227));
		p.add(new RealPoint(353, 227));
		p.add(new RealPoint(353, 228));
		p.add(new RealPoint(351, 228));
		p.add(new RealPoint(351, 229));
		p.add(new RealPoint(348, 229));
		p.add(new RealPoint(348, 230));
		p.add(new RealPoint(346, 230));
		p.add(new RealPoint(346, 231));
		p.add(new RealPoint(344, 231));
		p.add(new RealPoint(344, 232));
		p.add(new RealPoint(346, 232));
		p.add(new RealPoint(346, 233));
		p.add(new RealPoint(348, 233));
		p.add(new RealPoint(348, 234));
		p.add(new RealPoint(350, 234));
		p.add(new RealPoint(350, 235));
		p.add(new RealPoint(352, 235));
		p.add(new RealPoint(352, 236));
		p.add(new RealPoint(354, 236));
		p.add(new RealPoint(354, 237));
		p.add(new RealPoint(356, 237));
		p.add(new RealPoint(356, 238));
		p.add(new RealPoint(359, 238));
		p.add(new RealPoint(359, 239));
		p.add(new RealPoint(361, 239));
		p.add(new RealPoint(361, 240));
		p.add(new RealPoint(363, 240));
		p.add(new RealPoint(363, 241));
		p.add(new RealPoint(365, 241));
		p.add(new RealPoint(365, 242));
		p.add(new RealPoint(367, 242));
		p.add(new RealPoint(367, 243));
		p.add(new RealPoint(369, 243));
		p.add(new RealPoint(369, 244));
		p.add(new RealPoint(372, 244));
		p.add(new RealPoint(372, 245));
		p.add(new RealPoint(374, 245));
		p.add(new RealPoint(374, 246));
		p.add(new RealPoint(376, 246));
		p.add(new RealPoint(376, 247));
		p.add(new RealPoint(378, 247));
		p.add(new RealPoint(378, 248));
		p.add(new RealPoint(380, 248));
		p.add(new RealPoint(380, 249));
		p.add(new RealPoint(382, 249));
		p.add(new RealPoint(382, 250));
		p.add(new RealPoint(385, 250));
		p.add(new RealPoint(385, 251));
		p.add(new RealPoint(387, 251));
		p.add(new RealPoint(387, 252));
		p.add(new RealPoint(389, 252));
		p.add(new RealPoint(389, 253));
		p.add(new RealPoint(391, 253));
		p.add(new RealPoint(391, 254));
		p.add(new RealPoint(393, 254));
		p.add(new RealPoint(393, 255));
		p.add(new RealPoint(395, 255));
		p.add(new RealPoint(395, 256));
		p.add(new RealPoint(398, 256));
		p.add(new RealPoint(398, 257));
		p.add(new RealPoint(400, 257));
		p.add(new RealPoint(400, 258));
		p.add(new RealPoint(402, 258));
		p.add(new RealPoint(402, 259));
		p.add(new RealPoint(404, 259));
		p.add(new RealPoint(404, 260));
		p.add(new RealPoint(406, 260));
		p.add(new RealPoint(406, 261));
		p.add(new RealPoint(408, 261));
		p.add(new RealPoint(408, 262));
		p.add(new RealPoint(411, 262));
		p.add(new RealPoint(411, 263));
		p.add(new RealPoint(413, 263));
		p.add(new RealPoint(413, 264));
		p.add(new RealPoint(415, 264));
		p.add(new RealPoint(415, 265));
		p.add(new RealPoint(417, 265));
		p.add(new RealPoint(417, 266));
		p.add(new RealPoint(419, 266));
		p.add(new RealPoint(419, 267));
		p.add(new RealPoint(421, 267));
		p.add(new RealPoint(421, 268));
		p.add(new RealPoint(419, 268));
		p.add(new RealPoint(419, 269));
		p.add(new RealPoint(417, 269));
		p.add(new RealPoint(417, 270));
		p.add(new RealPoint(415, 270));
		p.add(new RealPoint(415, 271));
		p.add(new RealPoint(413, 271));
		p.add(new RealPoint(413, 272));
		p.add(new RealPoint(410, 272));
		p.add(new RealPoint(410, 273));
		p.add(new RealPoint(408, 273));
		p.add(new RealPoint(408, 274));
		p.add(new RealPoint(406, 274));
		p.add(new RealPoint(406, 275));
		p.add(new RealPoint(404, 275));
		p.add(new RealPoint(404, 276));
		p.add(new RealPoint(401, 276));
		p.add(new RealPoint(401, 277));
		p.add(new RealPoint(399, 277));
		p.add(new RealPoint(399, 278));
		p.add(new RealPoint(397, 278));
		p.add(new RealPoint(397, 279));
		p.add(new RealPoint(394, 279));
		p.add(new RealPoint(394, 280));
		p.add(new RealPoint(392, 280));
		p.add(new RealPoint(392, 281));
		p.add(new RealPoint(390, 281));
		p.add(new RealPoint(390, 282));
		p.add(new RealPoint(388, 282));
		p.add(new RealPoint(388, 283));
		p.add(new RealPoint(385, 283));
		p.add(new RealPoint(385, 284));
		p.add(new RealPoint(383, 284));
		p.add(new RealPoint(383, 285));
		p.add(new RealPoint(381, 285));
		p.add(new RealPoint(381, 286));
		p.add(new RealPoint(379, 286));
		p.add(new RealPoint(379, 287));
		p.add(new RealPoint(376, 287));
		p.add(new RealPoint(376, 288));
		p.add(new RealPoint(374, 288));
		p.add(new RealPoint(374, 289));
		p.add(new RealPoint(372, 289));
		p.add(new RealPoint(372, 290));
		p.add(new RealPoint(369, 290));
		p.add(new RealPoint(369, 291));
		p.add(new RealPoint(367, 291));
		p.add(new RealPoint(367, 292));
		p.add(new RealPoint(365, 292));
		p.add(new RealPoint(365, 293));
		p.add(new RealPoint(363, 293));
		p.add(new RealPoint(363, 294));
		p.add(new RealPoint(360, 294));
		p.add(new RealPoint(360, 295));
		p.add(new RealPoint(358, 295));
		p.add(new RealPoint(358, 296));
		p.add(new RealPoint(356, 296));
		p.add(new RealPoint(356, 297));
		p.add(new RealPoint(353, 297));
		p.add(new RealPoint(353, 298));
		p.add(new RealPoint(351, 298));
		p.add(new RealPoint(351, 299));
		p.add(new RealPoint(349, 299));
		p.add(new RealPoint(349, 300));
		p.add(new RealPoint(347, 300));
		p.add(new RealPoint(347, 301));
		p.add(new RealPoint(344, 301));
		p.add(new RealPoint(344, 302));
		p.add(new RealPoint(342, 302));
		p.add(new RealPoint(342, 303));
		p.add(new RealPoint(340, 303));
		p.add(new RealPoint(340, 304));
		p.add(new RealPoint(338, 304));
		p.add(new RealPoint(338, 305));
		p.add(new RealPoint(335, 305));
		p.add(new RealPoint(335, 306));
		p.add(new RealPoint(333, 306));
		p.add(new RealPoint(333, 307));
		p.add(new RealPoint(330, 307));
		p.add(new RealPoint(330, 305));
		p.add(new RealPoint(329, 305));
		p.add(new RealPoint(329, 303));
		p.add(new RealPoint(328, 303));
		p.add(new RealPoint(328, 301));
		p.add(new RealPoint(327, 301));
		p.add(new RealPoint(327, 299));
		p.add(new RealPoint(326, 299));
		p.add(new RealPoint(326, 297));
		p.add(new RealPoint(325, 297));
		p.add(new RealPoint(325, 294));
		p.add(new RealPoint(324, 294));
		p.add(new RealPoint(324, 292));
		p.add(new RealPoint(323, 292));
		p.add(new RealPoint(323, 290));
		p.add(new RealPoint(322, 290));
		p.add(new RealPoint(322, 288));
		p.add(new RealPoint(321, 288));
		p.add(new RealPoint(321, 286));
		p.add(new RealPoint(320, 286));
		p.add(new RealPoint(320, 284));
		p.add(new RealPoint(319, 284));
		p.add(new RealPoint(319, 282));
		p.add(new RealPoint(318, 282));
		p.add(new RealPoint(318, 280));
		p.add(new RealPoint(317, 280));
		p.add(new RealPoint(317, 277));
		p.add(new RealPoint(316, 277));
		p.add(new RealPoint(316, 275));
		p.add(new RealPoint(315, 275));
		p.add(new RealPoint(315, 273));
		p.add(new RealPoint(314, 273));
		p.add(new RealPoint(314, 271));
		p.add(new RealPoint(313, 271));
		p.add(new RealPoint(313, 269));
		p.add(new RealPoint(312, 269));
		p.add(new RealPoint(312, 267));
		p.add(new RealPoint(311, 267));
		p.add(new RealPoint(311, 265));
		p.add(new RealPoint(310, 265));
		p.add(new RealPoint(310, 262));
		p.add(new RealPoint(309, 262));
		p.add(new RealPoint(309, 260));
		p.add(new RealPoint(308, 260));
		p.add(new RealPoint(308, 258));
		p.add(new RealPoint(307, 258));
		p.add(new RealPoint(307, 256));
		p.add(new RealPoint(306, 256));
		p.add(new RealPoint(306, 254));
		p.add(new RealPoint(305, 254));
		p.add(new RealPoint(305, 252));
		p.add(new RealPoint(304, 252));
		p.add(new RealPoint(304, 250));
		p.add(new RealPoint(303, 250));
		p.add(new RealPoint(303, 248));
		p.add(new RealPoint(302, 248));
		p.add(new RealPoint(302, 245));
		p.add(new RealPoint(301, 245));
		p.add(new RealPoint(301, 243));
		p.add(new RealPoint(300, 243));
		p.add(new RealPoint(300, 241));
		p.add(new RealPoint(299, 241));
		p.add(new RealPoint(299, 239));
		p.add(new RealPoint(298, 239));
		p.add(new RealPoint(298, 237));
		p.add(new RealPoint(297, 237));
		p.add(new RealPoint(297, 235));
		p.add(new RealPoint(296, 235));
		p.add(new RealPoint(296, 233));
		p.add(new RealPoint(295, 233));
		p.add(new RealPoint(295, 230));
		p.add(new RealPoint(294, 230));
		p.add(new RealPoint(294, 229));
		p.add(new RealPoint(282, 229));
		p.add(new RealPoint(282, 230));
		p.add(new RealPoint(268, 230));
		p.add(new RealPoint(268, 231));
		p.add(new RealPoint(255, 231));
		p.add(new RealPoint(255, 232));
		p.add(new RealPoint(242, 232));
		p.add(new RealPoint(242, 233));
		p.add(new RealPoint(228, 233));
		p.add(new RealPoint(228, 234));
		p.add(new RealPoint(215, 234));
		p.add(new RealPoint(215, 235));
		p.add(new RealPoint(202, 235));
		p.add(new RealPoint(202, 236));
		p.add(new RealPoint(201, 236));
		p.add(new RealPoint(201, 234));
		p.add(new RealPoint(202, 234));
		p.add(new RealPoint(202, 232));
		p.add(new RealPoint(203, 232));
		p.add(new RealPoint(203, 231));
		p.add(new RealPoint(204, 231));
		p.add(new RealPoint(204, 229));
		p.add(new RealPoint(205, 229));
		p.add(new RealPoint(205, 228));
		p.add(new RealPoint(206, 228));
		p.add(new RealPoint(206, 226));
		p.add(new RealPoint(207, 226));
		p.add(new RealPoint(207, 224));
		p.add(new RealPoint(208, 224));
		p.add(new RealPoint(208, 223));
		p.add(new RealPoint(209, 223));
		p.add(new RealPoint(209, 221));
		p.add(new RealPoint(210, 221));
		p.add(new RealPoint(210, 220));
		p.add(new RealPoint(211, 220));
		p.add(new RealPoint(211, 218));
		p.add(new RealPoint(212, 218));
		p.add(new RealPoint(212, 216));
		p.add(new RealPoint(213, 216));
		p.add(new RealPoint(213, 215));
		p.add(new RealPoint(214, 215));
		p.add(new RealPoint(214, 213));
		p.add(new RealPoint(215, 213));
		p.add(new RealPoint(215, 212));
		p.add(new RealPoint(216, 212));
		p.add(new RealPoint(216, 210));
		p.add(new RealPoint(217, 210));
		p.add(new RealPoint(217, 208));
		p.add(new RealPoint(218, 208));
		p.add(new RealPoint(218, 207));
		p.add(new RealPoint(219, 207));
		p.add(new RealPoint(219, 205));
		p.add(new RealPoint(220, 205));
		p.add(new RealPoint(220, 204));
		p.add(new RealPoint(221, 204));
		p.add(new RealPoint(221, 202));
		p.add(new RealPoint(222, 202));
		p.add(new RealPoint(222, 200));
		p.add(new RealPoint(223, 200));
		p.add(new RealPoint(223, 199));
		p.add(new RealPoint(224, 199));
		p.add(new RealPoint(224, 197));
		p.add(new RealPoint(225, 197));
		p.add(new RealPoint(225, 196));
		p.add(new RealPoint(226, 196));
		p.add(new RealPoint(226, 194));
		p.add(new RealPoint(227, 194));
		p.add(new RealPoint(227, 192));
		p.add(new RealPoint(228, 192));
		p.add(new RealPoint(228, 191));
		p.add(new RealPoint(229, 191));
		p.add(new RealPoint(229, 189));
		p.add(new RealPoint(230, 189));
		p.add(new RealPoint(230, 187));
		p.add(new RealPoint(231, 187));
		p.add(new RealPoint(231, 186));
		p.add(new RealPoint(232, 186));
		p.add(new RealPoint(232, 184));
		p.add(new RealPoint(233, 184));
		p.add(new RealPoint(233, 183));
		p.add(new RealPoint(234, 183));
		p.add(new RealPoint(234, 181));
		p.add(new RealPoint(235, 181));
		p.add(new RealPoint(235, 179));
		p.add(new RealPoint(236, 179));
		p.add(new RealPoint(236, 178));
		p.add(new RealPoint(237, 178));
		p.add(new RealPoint(237, 176));
		p.add(new RealPoint(238, 176));
		p.add(new RealPoint(238, 175));
		p.add(new RealPoint(239, 175));
		p.add(new RealPoint(239, 173));
		p.add(new RealPoint(240, 173));
		p.add(new RealPoint(240, 171));
		p.add(new RealPoint(241, 171));
		p.add(new RealPoint(241, 170));
		p.add(new RealPoint(242, 170));
		p.add(new RealPoint(242, 168));
		p.add(new RealPoint(243, 168));
		p.add(new RealPoint(243, 167));
		p.add(new RealPoint(244, 167));
		p.add(new RealPoint(244, 165));
		p.add(new RealPoint(245, 165));
		p.add(new RealPoint(245, 163));
		p.add(new RealPoint(246, 163));
		p.add(new RealPoint(246, 162));
		p.add(new RealPoint(247, 162));
		p.add(new RealPoint(247, 160));
		p.add(new RealPoint(248, 160));
		p.add(new RealPoint(248, 159));
		p.add(new RealPoint(249, 159));
		p.add(new RealPoint(249, 157));
		p.add(new RealPoint(250, 157));
		p.add(new RealPoint(250, 155));
		p.add(new RealPoint(251, 155));
		p.add(new RealPoint(251, 154));
		p.add(new RealPoint(252, 154));
		p.add(new RealPoint(252, 152));
		p.add(new RealPoint(253, 152));
		p.add(new RealPoint(253, 151));
		p.add(new RealPoint(254, 151));
		p.add(new RealPoint(254, 149));
		p.add(new RealPoint(255, 149));
		p.add(new RealPoint(255, 147));
		p.add(new RealPoint(256, 147));
		p.add(new RealPoint(256, 146));
		p.add(new RealPoint(257, 146));
		p.add(new RealPoint(257, 144));
		p.add(new RealPoint(258, 144));
		p.add(new RealPoint(258, 143));
		p.add(new RealPoint(259, 143));
		p.add(new RealPoint(259, 141));
		p.add(new RealPoint(260, 141));
		p.add(new RealPoint(260, 139));
		p.add(new RealPoint(261, 139));
		p.add(new RealPoint(261, 138));
		p.add(new RealPoint(262, 138));
		p.add(new RealPoint(262, 136));
		p.add(new RealPoint(263, 136));
		p.add(new RealPoint(263, 134));
		p.add(new RealPoint(264, 134));
		p.add(new RealPoint(264, 133));
		p.add(new RealPoint(265, 133));
		p.add(new RealPoint(265, 131));
		p.add(new RealPoint(266, 131));
		p.add(new RealPoint(266, 130));
		p.add(new RealPoint(267, 130));
		p.add(new RealPoint(267, 129));
		p.add(new RealPoint(272, 129));
		p.add(new RealPoint(272, 128));
		p.add(new RealPoint(277, 128));
		p.add(new RealPoint(277, 127));
		p.add(new RealPoint(282, 127));
		p.add(new RealPoint(282, 126));
		p.add(new RealPoint(286, 126));
		p.add(new RealPoint(286, 125));
		p.add(new RealPoint(291, 125));
		p.add(new RealPoint(291, 124));
		p.add(new RealPoint(296, 124));
		p.add(new RealPoint(296, 123));
		p.add(new RealPoint(301, 123));
		p.add(new RealPoint(301, 122));
		p.add(new RealPoint(306, 122));
		p.add(new RealPoint(306, 121));
		p.add(new RealPoint(311, 121));
		p.add(new RealPoint(311, 120));
		p.add(new RealPoint(316, 120));
		p.add(new RealPoint(316, 119));
		p.add(new RealPoint(320, 119));
		p.add(new RealPoint(320, 118));
		p.add(new RealPoint(325, 118));
		p.add(new RealPoint(325, 117));
		p.add(new RealPoint(330, 117));
		p.add(new RealPoint(330, 116));
		p.add(new RealPoint(335, 116));
		p.add(new RealPoint(335, 115));
		p.add(new RealPoint(340, 115));
		p.add(new RealPoint(340, 114));
		p.add(new RealPoint(345, 114));
		p.add(new RealPoint(345, 113));
		p.add(new RealPoint(350, 113));
		p.add(new RealPoint(350, 112));
		p.add(new RealPoint(355, 112));
		p.add(new RealPoint(355, 111));
		p.add(new RealPoint(359, 111));
		p.add(new RealPoint(359, 110));
		p.add(new RealPoint(364, 110));
		p.add(new RealPoint(364, 109));
		p.add(new RealPoint(369, 109));
		p.add(new RealPoint(369, 108));
		p.add(new RealPoint(375, 108));
		p.add(new RealPoint(375, 110));
		p.add(new RealPoint(376, 110));
		p.add(new RealPoint(376, 111));
		p.add(new RealPoint(377, 111));
		p.add(new RealPoint(377, 112));
		p.add(new RealPoint(378, 112));
		p.add(new RealPoint(378, 113));
		p.add(new RealPoint(379, 113));
		p.add(new RealPoint(379, 114));
		p.add(new RealPoint(380, 114));
		p.add(new RealPoint(380, 115));
		p.add(new RealPoint(381, 115));
		p.add(new RealPoint(381, 116));
		p.add(new RealPoint(382, 116));
		p.add(new RealPoint(382, 117));
		p.add(new RealPoint(383, 117));
		p.add(new RealPoint(383, 118));
		p.add(new RealPoint(384, 118));
		p.add(new RealPoint(384, 119));
		p.add(new RealPoint(385, 119));
		p.add(new RealPoint(385, 120));
		p.add(new RealPoint(386, 120));
		p.add(new RealPoint(386, 121));
		p.add(new RealPoint(387, 121));
		p.add(new RealPoint(387, 122));
		p.add(new RealPoint(388, 122));
		p.add(new RealPoint(388, 123));
		p.add(new RealPoint(389, 123));
		p.add(new RealPoint(389, 124));
		p.add(new RealPoint(390, 124));
		p.add(new RealPoint(390, 126));
		p.add(new RealPoint(391, 126));
		p.add(new RealPoint(391, 127));
		p.add(new RealPoint(392, 127));
		p.add(new RealPoint(392, 128));
		p.add(new RealPoint(393, 128));
		p.add(new RealPoint(393, 129));
		p.add(new RealPoint(394, 129));
		p.add(new RealPoint(394, 130));
		p.add(new RealPoint(395, 130));
		p.add(new RealPoint(395, 131));
		p.add(new RealPoint(396, 131));
		p.add(new RealPoint(396, 132));
		p.add(new RealPoint(397, 132));
		p.add(new RealPoint(397, 133));
		p.add(new RealPoint(398, 133));
		p.add(new RealPoint(398, 134));
		p.add(new RealPoint(399, 134));
		p.add(new RealPoint(399, 135));
		p.add(new RealPoint(400, 135));
		p.add(new RealPoint(400, 136));
		p.add(new RealPoint(401, 136));
		p.add(new RealPoint(401, 137));
		p.add(new RealPoint(402, 137));
		p.add(new RealPoint(402, 138));
		p.add(new RealPoint(403, 138));
		p.add(new RealPoint(403, 139));
		p.add(new RealPoint(404, 139));
		p.add(new RealPoint(404, 140));
		p.add(new RealPoint(405, 140));
		p.add(new RealPoint(405, 142));
		p.add(new RealPoint(406, 142));
		p.add(new RealPoint(406, 143));
		p.add(new RealPoint(407, 143));
		p.add(new RealPoint(407, 144));
		p.add(new RealPoint(408, 144));
		p.add(new RealPoint(408, 145));
		p.add(new RealPoint(409, 145));
		p.add(new RealPoint(409, 146));
		p.add(new RealPoint(410, 146));
		p.add(new RealPoint(410, 147));
		p.add(new RealPoint(411, 147));
		p.add(new RealPoint(411, 148));
		p.add(new RealPoint(412, 148));
		p.add(new RealPoint(412, 149));
		p.add(new RealPoint(413, 149));
		p.add(new RealPoint(413, 150));
		p.add(new RealPoint(414, 150));
		p.add(new RealPoint(414, 151));
		p.add(new RealPoint(415, 151));
		p.add(new RealPoint(415, 152));
		p.add(new RealPoint(416, 152));
		p.add(new RealPoint(416, 153));
		p.add(new RealPoint(417, 153));
		p.add(new RealPoint(417, 154));
		p.add(new RealPoint(418, 154));
		p.add(new RealPoint(418, 155));
		p.add(new RealPoint(419, 155));
		p.add(new RealPoint(419, 157));
		p.add(new RealPoint(420, 157));
		p.add(new RealPoint(420, 158));
		p.add(new RealPoint(421, 158));
		p.add(new RealPoint(421, 159));
		p.add(new RealPoint(422, 159));
		p.add(new RealPoint(422, 160));
		p.add(new RealPoint(423, 160));
		p.add(new RealPoint(423, 161));
		p.add(new RealPoint(424, 161));
		p.add(new RealPoint(424, 162));
		p.add(new RealPoint(425, 162));
		p.add(new RealPoint(425, 163));
		p.add(new RealPoint(426, 163));
		p.add(new RealPoint(426, 164));
		p.add(new RealPoint(427, 164));
		p.add(new RealPoint(427, 165));
		p.add(new RealPoint(428, 165));
		p.add(new RealPoint(428, 166));
		p.add(new RealPoint(429, 166));
		p.add(new RealPoint(429, 167));
		p.add(new RealPoint(430, 167));
		p.add(new RealPoint(430, 168));
		p.add(new RealPoint(431, 168));
		p.add(new RealPoint(431, 169));
		p.add(new RealPoint(432, 169));
		p.add(new RealPoint(432, 170));
		p.add(new RealPoint(433, 170));
		p.add(new RealPoint(433, 171));
		p.add(new RealPoint(434, 171));
		p.add(new RealPoint(434, 173));
		p.add(new RealPoint(435, 173));
		p.add(new RealPoint(435, 174));
		p.add(new RealPoint(436, 174));
		p.add(new RealPoint(436, 175));
		p.add(new RealPoint(437, 175));
		p.add(new RealPoint(437, 176));
		p.add(new RealPoint(438, 176));
		p.add(new RealPoint(438, 177));
		p.add(new RealPoint(439, 177));
		p.add(new RealPoint(439, 178));
		p.add(new RealPoint(440, 178));
		p.add(new RealPoint(440, 179));
		p.add(new RealPoint(441, 179));
		p.add(new RealPoint(441, 180));
		p.add(new RealPoint(442, 180));
		p.add(new RealPoint(442, 181));
		p.add(new RealPoint(443, 181));
		p.add(new RealPoint(443, 182));
		p.add(new RealPoint(444, 182));

		List<FeatureResult> compute = ops.op(GeometricFeatureSet.class, p)
				.compute(p);
		for (FeatureResult featureResult : compute) {
			results.put(featureResult.getName(), featureResult.getValue());
		}

	}

	/**
	 * Test the {@link AreaFeature} Op.
	 */
	@Test
	public void testArea() {
		// value taken from imagej
		assertEquals(AreaFeature.NAME, 24332, results.get(AreaFeature.NAME),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link PerimeterFeature} Op.
	 */
	@Test
	public void testPerimeter() {
		// value taken from imagej
		assertEquals(PerimeterFeature.NAME, 866.690475583,
				results.get(PerimeterFeature.NAME),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link CircularityFeature} Op.
	 */
	@Test
	public void testCircularity() {
		// value taken from imagej
		assertEquals(CircularityFeature.NAME, 0.407061121,
				results.get(CircularityFeature.NAME),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link SolidityFeature} Op.
	 */
	@Test
	public void testSolidity() {
		// value taken from imagej
		assertEquals(SolidityFeature.NAME, 0.759437569,
				results.get(SolidityFeature.NAME),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link FeretsDiameterFeature} Op.
	 */
	@Test
	public void testFeretDiameter() {
		// value taken from imagej
		assertEquals(FeretsDiameterFeature.NAME, 252.009920440,
				results.get(FeretsDiameterFeature.NAME),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link FeretsAngleFeature} Op.
	 */
	@Test
	public void testFeretAngle() {

		// value taken from imagej, angle could be reversed so check 11.44.. and
		// 11.44.. + 180
		boolean isEquals = false;
		if (Math.abs(11.443696697 - results.get(FeretsAngleFeature.NAME)) < AbstractFeatureTest.SMALL_DELTA
				|| Math.abs(11.443696697 + 180 - results
						.get(FeretsAngleFeature.NAME)) < AbstractFeatureTest.SMALL_DELTA) {
			isEquals = true;
		}

		assertTrue("FeretsAngleFeature.NAME Expected [11.443696697] was ["
				+ results.get(FeretsAngleFeature.NAME) + "]", isEquals);
	}

}
