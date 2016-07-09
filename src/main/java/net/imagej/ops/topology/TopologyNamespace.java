package net.imagej.ops.topology;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.topology.eulerCharacteristic.EulerCorrection;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import org.scijava.plugin.Plugin;

/**
 * The topology namespace contains operations for calculating topology characteristics
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Namespace.class)
public class TopologyNamespace extends AbstractNamespace {

    @Override
    public String getName() {
        return "topology";
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26N.class)
    public <B extends BooleanType<B>> Double eulerCharacteristic26N(final RandomAccessibleInterval<B> in) {
        return (Double) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26N.class, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26NFloating.class)
    public <B extends BooleanType<B>> Double eulerCharacteristic26NFloating(final RandomAccessibleInterval<B> in) {
        return (Double) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26NFloating.class, in);
    }

    @OpMethod(op = EulerCorrection.class)
    public <B extends BooleanType<B>> Double eulerCorrection(final RandomAccessibleInterval<B> in) {
        return (Double) ops().run(net.imagej.ops.Ops.Topology.EulerCorrection.class, in);
    }
}
