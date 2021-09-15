package net.atlas.SkyblockSandbox.util.NumberTruncation;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumberSuffix {
    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000D, "k");
        suffixes.put(1_000_000D, "M");
        suffixes.put(1_000_000_000D, "B");
        suffixes.put(1_000_000_000_000D, "T");
        suffixes.put(1_000_000_000_000_000D, " Quadrillion");
        suffixes.put(1_000_000_000_000_000_000D, " Quintillion");

    }

    public static String format(double value) {
        DecimalFormat blank = new DecimalFormat("#");
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Double.MIN_VALUE) return format(Double.MIN_VALUE + 1D);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return blank.format(value); //deal with easy case

        Map.Entry<Double, String> e = suffixes.floorEntry(value);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10d);
        return hasDecimal ? blank.format((truncated / 10d)) + suffix : blank.format((truncated / 10)) + suffix;
    }
}
