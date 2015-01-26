package net.imagej.ops.functionbuilder;

import java.util.List;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.OpRef;

import org.scijava.service.Service;

public interface ComputerBuilder extends Service {

    <I, O> Computer<I, O> build(final OpRef opType, final Class<O> outputType,
            final Class<I> inputType, final OpRef... opPool);

    <I, O> Computer<I, List<O>> build(final Set<OpRef> opType,
            final Class<O> outputType, final Class<I> inputType,
            final OpRef... opPool);
}
