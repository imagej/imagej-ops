package net.imagej.ops.features.geometric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.EccentricityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.sets.GeometricFeatureSet;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

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

		p.add(new RealPoint(444.0d, 183.0d));
		p.add(new RealPoint(445.0d, 183.0d));
		p.add(new RealPoint(445.0d, 184.0d));
		p.add(new RealPoint(446.0d, 184.0d));
		p.add(new RealPoint(446.0d, 185.0d));
		p.add(new RealPoint(447.0d, 185.0d));
		p.add(new RealPoint(447.0d, 186.0d));
		p.add(new RealPoint(448.0d, 186.0d));
		p.add(new RealPoint(448.0d, 187.0d));
		p.add(new RealPoint(445.0d, 187.0d));
		p.add(new RealPoint(445.0d, 188.0d));
		p.add(new RealPoint(443.0d, 188.0d));
		p.add(new RealPoint(443.0d, 189.0d));
		p.add(new RealPoint(441.0d, 189.0d));
		p.add(new RealPoint(441.0d, 190.0d));
		p.add(new RealPoint(438.0d, 190.0d));
		p.add(new RealPoint(438.0d, 191.0d));
		p.add(new RealPoint(436.0d, 191.0d));
		p.add(new RealPoint(436.0d, 192.0d));
		p.add(new RealPoint(434.0d, 192.0d));
		p.add(new RealPoint(434.0d, 193.0d));
		p.add(new RealPoint(432.0d, 193.0d));
		p.add(new RealPoint(432.0d, 194.0d));
		p.add(new RealPoint(429.0d, 194.0d));
		p.add(new RealPoint(429.0d, 195.0d));
		p.add(new RealPoint(427.0d, 195.0d));
		p.add(new RealPoint(427.0d, 196.0d));
		p.add(new RealPoint(425.0d, 196.0d));
		p.add(new RealPoint(425.0d, 197.0d));
		p.add(new RealPoint(422.0d, 197.0d));
		p.add(new RealPoint(422.0d, 198.0d));
		p.add(new RealPoint(420.0d, 198.0d));
		p.add(new RealPoint(420.0d, 199.0d));
		p.add(new RealPoint(418.0d, 199.0d));
		p.add(new RealPoint(418.0d, 200.0d));
		p.add(new RealPoint(415.0d, 200.0d));
		p.add(new RealPoint(415.0d, 201.0d));
		p.add(new RealPoint(413.0d, 201.0d));
		p.add(new RealPoint(413.0d, 202.0d));
		p.add(new RealPoint(411.0d, 202.0d));
		p.add(new RealPoint(411.0d, 203.0d));
		p.add(new RealPoint(408.0d, 203.0d));
		p.add(new RealPoint(408.0d, 204.0d));
		p.add(new RealPoint(406.0d, 204.0d));
		p.add(new RealPoint(406.0d, 205.0d));
		p.add(new RealPoint(404.0d, 205.0d));
		p.add(new RealPoint(404.0d, 206.0d));
		p.add(new RealPoint(402.0d, 206.0d));
		p.add(new RealPoint(402.0d, 207.0d));
		p.add(new RealPoint(399.0d, 207.0d));
		p.add(new RealPoint(399.0d, 208.0d));
		p.add(new RealPoint(397.0d, 208.0d));
		p.add(new RealPoint(397.0d, 209.0d));
		p.add(new RealPoint(395.0d, 209.0d));
		p.add(new RealPoint(395.0d, 210.0d));
		p.add(new RealPoint(392.0d, 210.0d));
		p.add(new RealPoint(392.0d, 211.0d));
		p.add(new RealPoint(390.0d, 211.0d));
		p.add(new RealPoint(390.0d, 212.0d));
		p.add(new RealPoint(388.0d, 212.0d));
		p.add(new RealPoint(388.0d, 213.0d));
		p.add(new RealPoint(385.0d, 213.0d));
		p.add(new RealPoint(385.0d, 214.0d));
		p.add(new RealPoint(383.0d, 214.0d));
		p.add(new RealPoint(383.0d, 215.0d));
		p.add(new RealPoint(381.0d, 215.0d));
		p.add(new RealPoint(381.0d, 216.0d));
		p.add(new RealPoint(378.0d, 216.0d));
		p.add(new RealPoint(378.0d, 217.0d));
		p.add(new RealPoint(376.0d, 217.0d));
		p.add(new RealPoint(376.0d, 218.0d));
		p.add(new RealPoint(374.0d, 218.0d));
		p.add(new RealPoint(374.0d, 219.0d));
		p.add(new RealPoint(371.0d, 219.0d));
		p.add(new RealPoint(371.0d, 220.0d));
		p.add(new RealPoint(369.0d, 220.0d));
		p.add(new RealPoint(369.0d, 221.0d));
		p.add(new RealPoint(367.0d, 221.0d));
		p.add(new RealPoint(367.0d, 222.0d));
		p.add(new RealPoint(365.0d, 222.0d));
		p.add(new RealPoint(365.0d, 223.0d));
		p.add(new RealPoint(362.0d, 223.0d));
		p.add(new RealPoint(362.0d, 224.0d));
		p.add(new RealPoint(360.0d, 224.0d));
		p.add(new RealPoint(360.0d, 225.0d));
		p.add(new RealPoint(358.0d, 225.0d));
		p.add(new RealPoint(358.0d, 226.0d));
		p.add(new RealPoint(355.0d, 226.0d));
		p.add(new RealPoint(355.0d, 227.0d));
		p.add(new RealPoint(353.0d, 227.0d));
		p.add(new RealPoint(353.0d, 228.0d));
		p.add(new RealPoint(351.0d, 228.0d));
		p.add(new RealPoint(351.0d, 229.0d));
		p.add(new RealPoint(348.0d, 229.0d));
		p.add(new RealPoint(348.0d, 230.0d));
		p.add(new RealPoint(346.0d, 230.0d));
		p.add(new RealPoint(346.0d, 231.0d));
		p.add(new RealPoint(344.0d, 231.0d));
		p.add(new RealPoint(344.0d, 232.0d));
		p.add(new RealPoint(346.0d, 232.0d));
		p.add(new RealPoint(346.0d, 233.0d));
		p.add(new RealPoint(348.0d, 233.0d));
		p.add(new RealPoint(348.0d, 234.0d));
		p.add(new RealPoint(350.0d, 234.0d));
		p.add(new RealPoint(350.0d, 235.0d));
		p.add(new RealPoint(352.0d, 235.0d));
		p.add(new RealPoint(352.0d, 236.0d));
		p.add(new RealPoint(354.0d, 236.0d));
		p.add(new RealPoint(354.0d, 237.0d));
		p.add(new RealPoint(356.0d, 237.0d));
		p.add(new RealPoint(356.0d, 238.0d));
		p.add(new RealPoint(359.0d, 238.0d));
		p.add(new RealPoint(359.0d, 239.0d));
		p.add(new RealPoint(361.0d, 239.0d));
		p.add(new RealPoint(361.0d, 240.0d));
		p.add(new RealPoint(363.0d, 240.0d));
		p.add(new RealPoint(363.0d, 241.0d));
		p.add(new RealPoint(365.0d, 241.0d));
		p.add(new RealPoint(365.0d, 242.0d));
		p.add(new RealPoint(367.0d, 242.0d));
		p.add(new RealPoint(367.0d, 243.0d));
		p.add(new RealPoint(369.0d, 243.0d));
		p.add(new RealPoint(369.0d, 244.0d));
		p.add(new RealPoint(372.0d, 244.0d));
		p.add(new RealPoint(372.0d, 245.0d));
		p.add(new RealPoint(374.0d, 245.0d));
		p.add(new RealPoint(374.0d, 246.0d));
		p.add(new RealPoint(376.0d, 246.0d));
		p.add(new RealPoint(376.0d, 247.0d));
		p.add(new RealPoint(378.0d, 247.0d));
		p.add(new RealPoint(378.0d, 248.0d));
		p.add(new RealPoint(380.0d, 248.0d));
		p.add(new RealPoint(380.0d, 249.0d));
		p.add(new RealPoint(382.0d, 249.0d));
		p.add(new RealPoint(382.0d, 250.0d));
		p.add(new RealPoint(385.0d, 250.0d));
		p.add(new RealPoint(385.0d, 251.0d));
		p.add(new RealPoint(387.0d, 251.0d));
		p.add(new RealPoint(387.0d, 252.0d));
		p.add(new RealPoint(389.0d, 252.0d));
		p.add(new RealPoint(389.0d, 253.0d));
		p.add(new RealPoint(391.0d, 253.0d));
		p.add(new RealPoint(391.0d, 254.0d));
		p.add(new RealPoint(393.0d, 254.0d));
		p.add(new RealPoint(393.0d, 255.0d));
		p.add(new RealPoint(395.0d, 255.0d));
		p.add(new RealPoint(395.0d, 256.0d));
		p.add(new RealPoint(398.0d, 256.0d));
		p.add(new RealPoint(398.0d, 257.0d));
		p.add(new RealPoint(400.0d, 257.0d));
		p.add(new RealPoint(400.0d, 258.0d));
		p.add(new RealPoint(402.0d, 258.0d));
		p.add(new RealPoint(402.0d, 259.0d));
		p.add(new RealPoint(404.0d, 259.0d));
		p.add(new RealPoint(404.0d, 260.0d));
		p.add(new RealPoint(406.0d, 260.0d));
		p.add(new RealPoint(406.0d, 261.0d));
		p.add(new RealPoint(408.0d, 261.0d));
		p.add(new RealPoint(408.0d, 262.0d));
		p.add(new RealPoint(411.0d, 262.0d));
		p.add(new RealPoint(411.0d, 263.0d));
		p.add(new RealPoint(413.0d, 263.0d));
		p.add(new RealPoint(413.0d, 264.0d));
		p.add(new RealPoint(415.0d, 264.0d));
		p.add(new RealPoint(415.0d, 265.0d));
		p.add(new RealPoint(417.0d, 265.0d));
		p.add(new RealPoint(417.0d, 266.0d));
		p.add(new RealPoint(419.0d, 266.0d));
		p.add(new RealPoint(419.0d, 267.0d));
		p.add(new RealPoint(421.0d, 267.0d));
		p.add(new RealPoint(421.0d, 268.0d));
		p.add(new RealPoint(419.0d, 268.0d));
		p.add(new RealPoint(419.0d, 269.0d));
		p.add(new RealPoint(417.0d, 269.0d));
		p.add(new RealPoint(417.0d, 270.0d));
		p.add(new RealPoint(415.0d, 270.0d));
		p.add(new RealPoint(415.0d, 271.0d));
		p.add(new RealPoint(413.0d, 271.0d));
		p.add(new RealPoint(413.0d, 272.0d));
		p.add(new RealPoint(410.0d, 272.0d));
		p.add(new RealPoint(410.0d, 273.0d));
		p.add(new RealPoint(408.0d, 273.0d));
		p.add(new RealPoint(408.0d, 274.0d));
		p.add(new RealPoint(406.0d, 274.0d));
		p.add(new RealPoint(406.0d, 275.0d));
		p.add(new RealPoint(404.0d, 275.0d));
		p.add(new RealPoint(404.0d, 276.0d));
		p.add(new RealPoint(401.0d, 276.0d));
		p.add(new RealPoint(401.0d, 277.0d));
		p.add(new RealPoint(399.0d, 277.0d));
		p.add(new RealPoint(399.0d, 278.0d));
		p.add(new RealPoint(397.0d, 278.0d));
		p.add(new RealPoint(397.0d, 279.0d));
		p.add(new RealPoint(394.0d, 279.0d));
		p.add(new RealPoint(394.0d, 280.0d));
		p.add(new RealPoint(392.0d, 280.0d));
		p.add(new RealPoint(392.0d, 281.0d));
		p.add(new RealPoint(390.0d, 281.0d));
		p.add(new RealPoint(390.0d, 282.0d));
		p.add(new RealPoint(388.0d, 282.0d));
		p.add(new RealPoint(388.0d, 283.0d));
		p.add(new RealPoint(385.0d, 283.0d));
		p.add(new RealPoint(385.0d, 284.0d));
		p.add(new RealPoint(383.0d, 284.0d));
		p.add(new RealPoint(383.0d, 285.0d));
		p.add(new RealPoint(381.0d, 285.0d));
		p.add(new RealPoint(381.0d, 286.0d));
		p.add(new RealPoint(379.0d, 286.0d));
		p.add(new RealPoint(379.0d, 287.0d));
		p.add(new RealPoint(376.0d, 287.0d));
		p.add(new RealPoint(376.0d, 288.0d));
		p.add(new RealPoint(374.0d, 288.0d));
		p.add(new RealPoint(374.0d, 289.0d));
		p.add(new RealPoint(372.0d, 289.0d));
		p.add(new RealPoint(372.0d, 290.0d));
		p.add(new RealPoint(369.0d, 290.0d));
		p.add(new RealPoint(369.0d, 291.0d));
		p.add(new RealPoint(367.0d, 291.0d));
		p.add(new RealPoint(367.0d, 292.0d));
		p.add(new RealPoint(365.0d, 292.0d));
		p.add(new RealPoint(365.0d, 293.0d));
		p.add(new RealPoint(363.0d, 293.0d));
		p.add(new RealPoint(363.0d, 294.0d));
		p.add(new RealPoint(360.0d, 294.0d));
		p.add(new RealPoint(360.0d, 295.0d));
		p.add(new RealPoint(358.0d, 295.0d));
		p.add(new RealPoint(358.0d, 296.0d));
		p.add(new RealPoint(356.0d, 296.0d));
		p.add(new RealPoint(356.0d, 297.0d));
		p.add(new RealPoint(353.0d, 297.0d));
		p.add(new RealPoint(353.0d, 298.0d));
		p.add(new RealPoint(351.0d, 298.0d));
		p.add(new RealPoint(351.0d, 299.0d));
		p.add(new RealPoint(349.0d, 299.0d));
		p.add(new RealPoint(349.0d, 300.0d));
		p.add(new RealPoint(347.0d, 300.0d));
		p.add(new RealPoint(347.0d, 301.0d));
		p.add(new RealPoint(344.0d, 301.0d));
		p.add(new RealPoint(344.0d, 302.0d));
		p.add(new RealPoint(342.0d, 302.0d));
		p.add(new RealPoint(342.0d, 303.0d));
		p.add(new RealPoint(340.0d, 303.0d));
		p.add(new RealPoint(340.0d, 304.0d));
		p.add(new RealPoint(338.0d, 304.0d));
		p.add(new RealPoint(338.0d, 305.0d));
		p.add(new RealPoint(335.0d, 305.0d));
		p.add(new RealPoint(335.0d, 306.0d));
		p.add(new RealPoint(333.0d, 306.0d));
		p.add(new RealPoint(333.0d, 307.0d));
		p.add(new RealPoint(330.0d, 307.0d));
		p.add(new RealPoint(330.0d, 305.0d));
		p.add(new RealPoint(329.0d, 305.0d));
		p.add(new RealPoint(329.0d, 303.0d));
		p.add(new RealPoint(328.0d, 303.0d));
		p.add(new RealPoint(328.0d, 301.0d));
		p.add(new RealPoint(327.0d, 301.0d));
		p.add(new RealPoint(327.0d, 299.0d));
		p.add(new RealPoint(326.0d, 299.0d));
		p.add(new RealPoint(326.0d, 297.0d));
		p.add(new RealPoint(325.0d, 297.0d));
		p.add(new RealPoint(325.0d, 294.0d));
		p.add(new RealPoint(324.0d, 294.0d));
		p.add(new RealPoint(324.0d, 292.0d));
		p.add(new RealPoint(323.0d, 292.0d));
		p.add(new RealPoint(323.0d, 290.0d));
		p.add(new RealPoint(322.0d, 290.0d));
		p.add(new RealPoint(322.0d, 288.0d));
		p.add(new RealPoint(321.0d, 288.0d));
		p.add(new RealPoint(321.0d, 286.0d));
		p.add(new RealPoint(320.0d, 286.0d));
		p.add(new RealPoint(320.0d, 284.0d));
		p.add(new RealPoint(319.0d, 284.0d));
		p.add(new RealPoint(319.0d, 282.0d));
		p.add(new RealPoint(318.0d, 282.0d));
		p.add(new RealPoint(318.0d, 280.0d));
		p.add(new RealPoint(317.0d, 280.0d));
		p.add(new RealPoint(317.0d, 277.0d));
		p.add(new RealPoint(316.0d, 277.0d));
		p.add(new RealPoint(316.0d, 275.0d));
		p.add(new RealPoint(315.0d, 275.0d));
		p.add(new RealPoint(315.0d, 273.0d));
		p.add(new RealPoint(314.0d, 273.0d));
		p.add(new RealPoint(314.0d, 271.0d));
		p.add(new RealPoint(313.0d, 271.0d));
		p.add(new RealPoint(313.0d, 269.0d));
		p.add(new RealPoint(312.0d, 269.0d));
		p.add(new RealPoint(312.0d, 267.0d));
		p.add(new RealPoint(311.0d, 267.0d));
		p.add(new RealPoint(311.0d, 265.0d));
		p.add(new RealPoint(310.0d, 265.0d));
		p.add(new RealPoint(310.0d, 262.0d));
		p.add(new RealPoint(309.0d, 262.0d));
		p.add(new RealPoint(309.0d, 260.0d));
		p.add(new RealPoint(308.0d, 260.0d));
		p.add(new RealPoint(308.0d, 258.0d));
		p.add(new RealPoint(307.0d, 258.0d));
		p.add(new RealPoint(307.0d, 256.0d));
		p.add(new RealPoint(306.0d, 256.0d));
		p.add(new RealPoint(306.0d, 254.0d));
		p.add(new RealPoint(305.0d, 254.0d));
		p.add(new RealPoint(305.0d, 252.0d));
		p.add(new RealPoint(304.0d, 252.0d));
		p.add(new RealPoint(304.0d, 250.0d));
		p.add(new RealPoint(303.0d, 250.0d));
		p.add(new RealPoint(303.0d, 248.0d));
		p.add(new RealPoint(302.0d, 248.0d));
		p.add(new RealPoint(302.0d, 245.0d));
		p.add(new RealPoint(301.0d, 245.0d));
		p.add(new RealPoint(301.0d, 243.0d));
		p.add(new RealPoint(300.0d, 243.0d));
		p.add(new RealPoint(300.0d, 241.0d));
		p.add(new RealPoint(299.0d, 241.0d));
		p.add(new RealPoint(299.0d, 239.0d));
		p.add(new RealPoint(298.0d, 239.0d));
		p.add(new RealPoint(298.0d, 237.0d));
		p.add(new RealPoint(297.0d, 237.0d));
		p.add(new RealPoint(297.0d, 235.0d));
		p.add(new RealPoint(296.0d, 235.0d));
		p.add(new RealPoint(296.0d, 233.0d));
		p.add(new RealPoint(295.0d, 233.0d));
		p.add(new RealPoint(295.0d, 230.0d));
		p.add(new RealPoint(294.0d, 230.0d));
		p.add(new RealPoint(294.0d, 229.0d));
		p.add(new RealPoint(282.0d, 229.0d));
		p.add(new RealPoint(282.0d, 230.0d));
		p.add(new RealPoint(268.0d, 230.0d));
		p.add(new RealPoint(268.0d, 231.0d));
		p.add(new RealPoint(255.0d, 231.0d));
		p.add(new RealPoint(255.0d, 232.0d));
		p.add(new RealPoint(242.0d, 232.0d));
		p.add(new RealPoint(242.0d, 233.0d));
		p.add(new RealPoint(228.0d, 233.0d));
		p.add(new RealPoint(228.0d, 234.0d));
		p.add(new RealPoint(215.0d, 234.0d));
		p.add(new RealPoint(215.0d, 235.0d));
		p.add(new RealPoint(202.0d, 235.0d));
		p.add(new RealPoint(202.0d, 236.0d));
		p.add(new RealPoint(201.0d, 236.0d));
		p.add(new RealPoint(201.0d, 234.0d));
		p.add(new RealPoint(202.0d, 234.0d));
		p.add(new RealPoint(202.0d, 232.0d));
		p.add(new RealPoint(203.0d, 232.0d));
		p.add(new RealPoint(203.0d, 231.0d));
		p.add(new RealPoint(204.0d, 231.0d));
		p.add(new RealPoint(204.0d, 229.0d));
		p.add(new RealPoint(205.0d, 229.0d));
		p.add(new RealPoint(205.0d, 228.0d));
		p.add(new RealPoint(206.0d, 228.0d));
		p.add(new RealPoint(206.0d, 226.0d));
		p.add(new RealPoint(207.0d, 226.0d));
		p.add(new RealPoint(207.0d, 224.0d));
		p.add(new RealPoint(208.0d, 224.0d));
		p.add(new RealPoint(208.0d, 223.0d));
		p.add(new RealPoint(209.0d, 223.0d));
		p.add(new RealPoint(209.0d, 221.0d));
		p.add(new RealPoint(210.0d, 221.0d));
		p.add(new RealPoint(210.0d, 220.0d));
		p.add(new RealPoint(211.0d, 220.0d));
		p.add(new RealPoint(211.0d, 218.0d));
		p.add(new RealPoint(212.0d, 218.0d));
		p.add(new RealPoint(212.0d, 216.0d));
		p.add(new RealPoint(213.0d, 216.0d));
		p.add(new RealPoint(213.0d, 215.0d));
		p.add(new RealPoint(214.0d, 215.0d));
		p.add(new RealPoint(214.0d, 213.0d));
		p.add(new RealPoint(215.0d, 213.0d));
		p.add(new RealPoint(215.0d, 212.0d));
		p.add(new RealPoint(216.0d, 212.0d));
		p.add(new RealPoint(216.0d, 210.0d));
		p.add(new RealPoint(217.0d, 210.0d));
		p.add(new RealPoint(217.0d, 208.0d));
		p.add(new RealPoint(218.0d, 208.0d));
		p.add(new RealPoint(218.0d, 207.0d));
		p.add(new RealPoint(219.0d, 207.0d));
		p.add(new RealPoint(219.0d, 205.0d));
		p.add(new RealPoint(220.0d, 205.0d));
		p.add(new RealPoint(220.0d, 204.0d));
		p.add(new RealPoint(221.0d, 204.0d));
		p.add(new RealPoint(221.0d, 202.0d));
		p.add(new RealPoint(222.0d, 202.0d));
		p.add(new RealPoint(222.0d, 200.0d));
		p.add(new RealPoint(223.0d, 200.0d));
		p.add(new RealPoint(223.0d, 199.0d));
		p.add(new RealPoint(224.0d, 199.0d));
		p.add(new RealPoint(224.0d, 197.0d));
		p.add(new RealPoint(225.0d, 197.0d));
		p.add(new RealPoint(225.0d, 196.0d));
		p.add(new RealPoint(226.0d, 196.0d));
		p.add(new RealPoint(226.0d, 194.0d));
		p.add(new RealPoint(227.0d, 194.0d));
		p.add(new RealPoint(227.0d, 192.0d));
		p.add(new RealPoint(228.0d, 192.0d));
		p.add(new RealPoint(228.0d, 191.0d));
		p.add(new RealPoint(229.0d, 191.0d));
		p.add(new RealPoint(229.0d, 189.0d));
		p.add(new RealPoint(230.0d, 189.0d));
		p.add(new RealPoint(230.0d, 187.0d));
		p.add(new RealPoint(231.0d, 187.0d));
		p.add(new RealPoint(231.0d, 186.0d));
		p.add(new RealPoint(232.0d, 186.0d));
		p.add(new RealPoint(232.0d, 184.0d));
		p.add(new RealPoint(233.0d, 184.0d));
		p.add(new RealPoint(233.0d, 183.0d));
		p.add(new RealPoint(234.0d, 183.0d));
		p.add(new RealPoint(234.0d, 181.0d));
		p.add(new RealPoint(235.0d, 181.0d));
		p.add(new RealPoint(235.0d, 179.0d));
		p.add(new RealPoint(236.0d, 179.0d));
		p.add(new RealPoint(236.0d, 178.0d));
		p.add(new RealPoint(237.0d, 178.0d));
		p.add(new RealPoint(237.0d, 176.0d));
		p.add(new RealPoint(238.0d, 176.0d));
		p.add(new RealPoint(238.0d, 175.0d));
		p.add(new RealPoint(239.0d, 175.0d));
		p.add(new RealPoint(239.0d, 173.0d));
		p.add(new RealPoint(240.0d, 173.0d));
		p.add(new RealPoint(240.0d, 171.0d));
		p.add(new RealPoint(241.0d, 171.0d));
		p.add(new RealPoint(241.0d, 170.0d));
		p.add(new RealPoint(242.0d, 170.0d));
		p.add(new RealPoint(242.0d, 168.0d));
		p.add(new RealPoint(243.0d, 168.0d));
		p.add(new RealPoint(243.0d, 167.0d));
		p.add(new RealPoint(244.0d, 167.0d));
		p.add(new RealPoint(244.0d, 165.0d));
		p.add(new RealPoint(245.0d, 165.0d));
		p.add(new RealPoint(245.0d, 163.0d));
		p.add(new RealPoint(246.0d, 163.0d));
		p.add(new RealPoint(246.0d, 162.0d));
		p.add(new RealPoint(247.0d, 162.0d));
		p.add(new RealPoint(247.0d, 160.0d));
		p.add(new RealPoint(248.0d, 160.0d));
		p.add(new RealPoint(248.0d, 159.0d));
		p.add(new RealPoint(249.0d, 159.0d));
		p.add(new RealPoint(249.0d, 157.0d));
		p.add(new RealPoint(250.0d, 157.0d));
		p.add(new RealPoint(250.0d, 155.0d));
		p.add(new RealPoint(251.0d, 155.0d));
		p.add(new RealPoint(251.0d, 154.0d));
		p.add(new RealPoint(252.0d, 154.0d));
		p.add(new RealPoint(252.0d, 152.0d));
		p.add(new RealPoint(253.0d, 152.0d));
		p.add(new RealPoint(253.0d, 151.0d));
		p.add(new RealPoint(254.0d, 151.0d));
		p.add(new RealPoint(254.0d, 149.0d));
		p.add(new RealPoint(255.0d, 149.0d));
		p.add(new RealPoint(255.0d, 147.0d));
		p.add(new RealPoint(256.0d, 147.0d));
		p.add(new RealPoint(256.0d, 146.0d));
		p.add(new RealPoint(257.0d, 146.0d));
		p.add(new RealPoint(257.0d, 144.0d));
		p.add(new RealPoint(258.0d, 144.0d));
		p.add(new RealPoint(258.0d, 143.0d));
		p.add(new RealPoint(259.0d, 143.0d));
		p.add(new RealPoint(259.0d, 141.0d));
		p.add(new RealPoint(260.0d, 141.0d));
		p.add(new RealPoint(260.0d, 139.0d));
		p.add(new RealPoint(261.0d, 139.0d));
		p.add(new RealPoint(261.0d, 138.0d));
		p.add(new RealPoint(262.0d, 138.0d));
		p.add(new RealPoint(262.0d, 136.0d));
		p.add(new RealPoint(263.0d, 136.0d));
		p.add(new RealPoint(263.0d, 134.0d));
		p.add(new RealPoint(264.0d, 134.0d));
		p.add(new RealPoint(264.0d, 133.0d));
		p.add(new RealPoint(265.0d, 133.0d));
		p.add(new RealPoint(265.0d, 131.0d));
		p.add(new RealPoint(266.0d, 131.0d));
		p.add(new RealPoint(266.0d, 130.0d));
		p.add(new RealPoint(267.0d, 130.0d));
		p.add(new RealPoint(267.0d, 129.0d));
		p.add(new RealPoint(272.0d, 129.0d));
		p.add(new RealPoint(272.0d, 128.0d));
		p.add(new RealPoint(277.0d, 128.0d));
		p.add(new RealPoint(277.0d, 127.0d));
		p.add(new RealPoint(282.0d, 127.0d));
		p.add(new RealPoint(282.0d, 126.0d));
		p.add(new RealPoint(286.0d, 126.0d));
		p.add(new RealPoint(286.0d, 125.0d));
		p.add(new RealPoint(291.0d, 125.0d));
		p.add(new RealPoint(291.0d, 124.0d));
		p.add(new RealPoint(296.0d, 124.0d));
		p.add(new RealPoint(296.0d, 123.0d));
		p.add(new RealPoint(301.0d, 123.0d));
		p.add(new RealPoint(301.0d, 122.0d));
		p.add(new RealPoint(306.0d, 122.0d));
		p.add(new RealPoint(306.0d, 121.0d));
		p.add(new RealPoint(311.0d, 121.0d));
		p.add(new RealPoint(311.0d, 120.0d));
		p.add(new RealPoint(316.0d, 120.0d));
		p.add(new RealPoint(316.0d, 119.0d));
		p.add(new RealPoint(320.0d, 119.0d));
		p.add(new RealPoint(320.0d, 118.0d));
		p.add(new RealPoint(325.0d, 118.0d));
		p.add(new RealPoint(325.0d, 117.0d));
		p.add(new RealPoint(330.0d, 117.0d));
		p.add(new RealPoint(330.0d, 116.0d));
		p.add(new RealPoint(335.0d, 116.0d));
		p.add(new RealPoint(335.0d, 115.0d));
		p.add(new RealPoint(340.0d, 115.0d));
		p.add(new RealPoint(340.0d, 114.0d));
		p.add(new RealPoint(345.0d, 114.0d));
		p.add(new RealPoint(345.0d, 113.0d));
		p.add(new RealPoint(350.0d, 113.0d));
		p.add(new RealPoint(350.0d, 112.0d));
		p.add(new RealPoint(355.0d, 112.0d));
		p.add(new RealPoint(355.0d, 111.0d));
		p.add(new RealPoint(359.0d, 111.0d));
		p.add(new RealPoint(359.0d, 110.0d));
		p.add(new RealPoint(364.0d, 110.0d));
		p.add(new RealPoint(364.0d, 109.0d));
		p.add(new RealPoint(369.0d, 109.0d));
		p.add(new RealPoint(369.0d, 108.0d));
		p.add(new RealPoint(375.0d, 108.0d));
		p.add(new RealPoint(375.0d, 110.0d));
		p.add(new RealPoint(376.0d, 110.0d));
		p.add(new RealPoint(376.0d, 111.0d));
		p.add(new RealPoint(377.0d, 111.0d));
		p.add(new RealPoint(377.0d, 112.0d));
		p.add(new RealPoint(378.0d, 112.0d));
		p.add(new RealPoint(378.0d, 113.0d));
		p.add(new RealPoint(379.0d, 113.0d));
		p.add(new RealPoint(379.0d, 114.0d));
		p.add(new RealPoint(380.0d, 114.0d));
		p.add(new RealPoint(380.0d, 115.0d));
		p.add(new RealPoint(381.0d, 115.0d));
		p.add(new RealPoint(381.0d, 116.0d));
		p.add(new RealPoint(382.0d, 116.0d));
		p.add(new RealPoint(382.0d, 117.0d));
		p.add(new RealPoint(383.0d, 117.0d));
		p.add(new RealPoint(383.0d, 118.0d));
		p.add(new RealPoint(384.0d, 118.0d));
		p.add(new RealPoint(384.0d, 119.0d));
		p.add(new RealPoint(385.0d, 119.0d));
		p.add(new RealPoint(385.0d, 120.0d));
		p.add(new RealPoint(386.0d, 120.0d));
		p.add(new RealPoint(386.0d, 121.0d));
		p.add(new RealPoint(387.0d, 121.0d));
		p.add(new RealPoint(387.0d, 122.0d));
		p.add(new RealPoint(388.0d, 122.0d));
		p.add(new RealPoint(388.0d, 123.0d));
		p.add(new RealPoint(389.0d, 123.0d));
		p.add(new RealPoint(389.0d, 124.0d));
		p.add(new RealPoint(390.0d, 124.0d));
		p.add(new RealPoint(390.0d, 126.0d));
		p.add(new RealPoint(391.0d, 126.0d));
		p.add(new RealPoint(391.0d, 127.0d));
		p.add(new RealPoint(392.0d, 127.0d));
		p.add(new RealPoint(392.0d, 128.0d));
		p.add(new RealPoint(393.0d, 128.0d));
		p.add(new RealPoint(393.0d, 129.0d));
		p.add(new RealPoint(394.0d, 129.0d));
		p.add(new RealPoint(394.0d, 130.0d));
		p.add(new RealPoint(395.0d, 130.0d));
		p.add(new RealPoint(395.0d, 131.0d));
		p.add(new RealPoint(396.0d, 131.0d));
		p.add(new RealPoint(396.0d, 132.0d));
		p.add(new RealPoint(397.0d, 132.0d));
		p.add(new RealPoint(397.0d, 133.0d));
		p.add(new RealPoint(398.0d, 133.0d));
		p.add(new RealPoint(398.0d, 134.0d));
		p.add(new RealPoint(399.0d, 134.0d));
		p.add(new RealPoint(399.0d, 135.0d));
		p.add(new RealPoint(400.0d, 135.0d));
		p.add(new RealPoint(400.0d, 136.0d));
		p.add(new RealPoint(401.0d, 136.0d));
		p.add(new RealPoint(401.0d, 137.0d));
		p.add(new RealPoint(402.0d, 137.0d));
		p.add(new RealPoint(402.0d, 138.0d));
		p.add(new RealPoint(403.0d, 138.0d));
		p.add(new RealPoint(403.0d, 139.0d));
		p.add(new RealPoint(404.0d, 139.0d));
		p.add(new RealPoint(404.0d, 140.0d));
		p.add(new RealPoint(405.0d, 140.0d));
		p.add(new RealPoint(405.0d, 142.0d));
		p.add(new RealPoint(406.0d, 142.0d));
		p.add(new RealPoint(406.0d, 143.0d));
		p.add(new RealPoint(407.0d, 143.0d));
		p.add(new RealPoint(407.0d, 144.0d));
		p.add(new RealPoint(408.0d, 144.0d));
		p.add(new RealPoint(408.0d, 145.0d));
		p.add(new RealPoint(409.0d, 145.0d));
		p.add(new RealPoint(409.0d, 146.0d));
		p.add(new RealPoint(410.0d, 146.0d));
		p.add(new RealPoint(410.0d, 147.0d));
		p.add(new RealPoint(411.0d, 147.0d));
		p.add(new RealPoint(411.0d, 148.0d));
		p.add(new RealPoint(412.0d, 148.0d));
		p.add(new RealPoint(412.0d, 149.0d));
		p.add(new RealPoint(413.0d, 149.0d));
		p.add(new RealPoint(413.0d, 150.0d));
		p.add(new RealPoint(414.0d, 150.0d));
		p.add(new RealPoint(414.0d, 151.0d));
		p.add(new RealPoint(415.0d, 151.0d));
		p.add(new RealPoint(415.0d, 152.0d));
		p.add(new RealPoint(416.0d, 152.0d));
		p.add(new RealPoint(416.0d, 153.0d));
		p.add(new RealPoint(417.0d, 153.0d));
		p.add(new RealPoint(417.0d, 154.0d));
		p.add(new RealPoint(418.0d, 154.0d));
		p.add(new RealPoint(418.0d, 155.0d));
		p.add(new RealPoint(419.0d, 155.0d));
		p.add(new RealPoint(419.0d, 157.0d));
		p.add(new RealPoint(420.0d, 157.0d));
		p.add(new RealPoint(420.0d, 158.0d));
		p.add(new RealPoint(421.0d, 158.0d));
		p.add(new RealPoint(421.0d, 159.0d));
		p.add(new RealPoint(422.0d, 159.0d));
		p.add(new RealPoint(422.0d, 160.0d));
		p.add(new RealPoint(423.0d, 160.0d));
		p.add(new RealPoint(423.0d, 161.0d));
		p.add(new RealPoint(424.0d, 161.0d));
		p.add(new RealPoint(424.0d, 162.0d));
		p.add(new RealPoint(425.0d, 162.0d));
		p.add(new RealPoint(425.0d, 163.0d));
		p.add(new RealPoint(426.0d, 163.0d));
		p.add(new RealPoint(426.0d, 164.0d));
		p.add(new RealPoint(427.0d, 164.0d));
		p.add(new RealPoint(427.0d, 165.0d));
		p.add(new RealPoint(428.0d, 165.0d));
		p.add(new RealPoint(428.0d, 166.0d));
		p.add(new RealPoint(429.0d, 166.0d));
		p.add(new RealPoint(429.0d, 167.0d));
		p.add(new RealPoint(430.0d, 167.0d));
		p.add(new RealPoint(430.0d, 168.0d));
		p.add(new RealPoint(431.0d, 168.0d));
		p.add(new RealPoint(431.0d, 169.0d));
		p.add(new RealPoint(432.0d, 169.0d));
		p.add(new RealPoint(432.0d, 170.0d));
		p.add(new RealPoint(433.0d, 170.0d));
		p.add(new RealPoint(433.0d, 171.0d));
		p.add(new RealPoint(434.0d, 171.0d));
		p.add(new RealPoint(434.0d, 173.0d));
		p.add(new RealPoint(435.0d, 173.0d));
		p.add(new RealPoint(435.0d, 174.0d));
		p.add(new RealPoint(436.0d, 174.0d));
		p.add(new RealPoint(436.0d, 175.0d));
		p.add(new RealPoint(437.0d, 175.0d));
		p.add(new RealPoint(437.0d, 176.0d));
		p.add(new RealPoint(438.0d, 176.0d));
		p.add(new RealPoint(438.0d, 177.0d));
		p.add(new RealPoint(439.0d, 177.0d));
		p.add(new RealPoint(439.0d, 178.0d));
		p.add(new RealPoint(440.0d, 178.0d));
		p.add(new RealPoint(440.0d, 179.0d));
		p.add(new RealPoint(441.0d, 179.0d));
		p.add(new RealPoint(441.0d, 180.0d));
		p.add(new RealPoint(442.0d, 180.0d));
		p.add(new RealPoint(442.0d, 181.0d));
		p.add(new RealPoint(443.0d, 181.0d));
		p.add(new RealPoint(443.0d, 182.0d));
		p.add(new RealPoint(444.0d, 182.0d));

		List<Pair<String, DoubleType>> compute = ops.op(
				GeometricFeatureSet.class, p).getFeatures(p);
		for (Pair<String, DoubleType> featureResult : compute) {
			results.put(featureResult.getA(), featureResult.getB()
					.getRealDouble());
		}
	}

	/**
	 * Test the {@link AreaFeature} Op.
	 */
	@Test
	public void testArea() {
		// value taken from imagej
		assertEquals(AreaFeature.NAME, 24332, results.get("area"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link PerimeterFeature} Op.
	 */
	@Test
	public void testPerimeter() {
		// value taken from imagej
		assertEquals(PerimeterFeature.NAME, 866.690475583,
				results.get("perimeter"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link CircularityFeature} Op.
	 */
	@Test
	public void testCircularity() {
		// value taken from imagej
		assertEquals(CircularityFeature.NAME, 0.407061121,
				results.get("circularity"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link MinorAxisFeature} Op.
	 */
	@Test
	public void testMinorAxis() {
		// value taken from imagej
		assertEquals(MinorAxisFeature.NAME, 166.288157325,
				results.get("minoraxis"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link MajorAxisFeature} Op.
	 */
	@Test
	public void testMajorAxis() {
		// value taken from imagej
		assertEquals(MajorAxisFeature.NAME, 186.305898754,
				results.get("majoraxis"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link FeretsDiameterFeature} Op.
	 */
	@Test
	public void testFeretDiameter() {
		// value taken from imagej
		assertEquals(FeretsDiameterFeature.NAME, 252.009920440,
				results.get("feretsdiameter"),
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
		if (Math.abs(11.443696697 - results.get("feretsangle")) < AbstractFeatureTest.SMALL_DELTA
				|| Math.abs(11.443696697 + 180 - results
						.get("feretsangle")) < AbstractFeatureTest.SMALL_DELTA) {
			isEquals = true;
		}

		assertTrue("FeretsAngleFeature.NAME Expected [11.443696697] was ["
				+ results.get("feretsangle") + "]", isEquals);
	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testEccentricity() {
		assertEquals(EccentricityFeature.NAME, 1.120379838,
				results.get("eccentricity"),
				AbstractFeatureTest.SMALL_DELTA);

	}

	/**
	 * Test the {@link EccentricityFeature} Op.
	 */
	@Test
	public void testRoundness() {
		assertEquals(RoundnessFeature.NAME, 0.892554441,
				results.get("roundness"),
				AbstractFeatureTest.SMALL_DELTA);
	}

	/**
	 * Test the {@link SolidityFeature} Op.
	 */
	@Test
	public void testSolidity() {
		// value taken from imagej
		assertEquals(SolidityFeature.NAME, 0.759437569,
				results.get("solidity"), AbstractFeatureTest.SMALL_DELTA);
	}
}
