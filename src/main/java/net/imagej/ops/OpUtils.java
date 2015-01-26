package net.imagej.ops;

public class OpUtils {

    @SuppressWarnings("unchecked")
    public static <O> O cast(Object src) {
        return (O) src;
    }

}
