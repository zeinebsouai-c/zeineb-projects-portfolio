package randomValue.enums;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class VitalDataValues {

    public static ArrayList<Integer> heartRate = new ArrayList<>(IntStream.rangeClosed(0, 150)
            .boxed()
            .toList());

    public static ArrayList<Integer> respirationRate = new ArrayList<>(IntStream.rangeClosed(0, 50)
            .boxed()
            .toList());

    public static ArrayList<Integer> bodyTemperature = new ArrayList<>(IntStream.rangeClosed(0, 43)
            .boxed()
            .toList());

    public static ArrayList<Integer> pulseOximetry = new ArrayList<>(IntStream.rangeClosed(0, 100)
            .boxed()
            .toList());
}
