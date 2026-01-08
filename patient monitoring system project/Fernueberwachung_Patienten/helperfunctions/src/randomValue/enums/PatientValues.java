package randomValue.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class PatientValues {
    public static final ArrayList<String> maleNameList = new ArrayList<>(Arrays.asList(
            "AARON", "ADAM", "AIDEN", "ALEX", "ALEXANDER", "ANDREW", "ANTHONY", "BENJAMIN", "BLAKE", "BRADLEY",
            "BRANDON", "BRIAN", "CALEB", "CAMERON", "CHARLES", "CHRISTIAN", "CHRISTOPHER", "COLIN", "CONNOR",
            "DANIEL", "DAVID", "DYLAN", "EDWARD", "ELIJAH", "ETHAN", "EVAN", "GABRIEL", "GAVIN", "HENRY",
            "HUNTER", "ISAAC", "JACK", "JACOB", "JAMES", "JASON", "JAYDEN", "JOHN", "JONATHAN", "JOSEPH",
            "JOSHUA", "JUSTIN", "KEVIN", "LIAM", "LOGAN", "LUKE", "MATTHEW", "MICHAEL", "NATHAN", "NICHOLAS", "NOAH"
    ));
    public static final ArrayList<String> femaleFirstNameList = new ArrayList<>(java.util.Arrays.asList(
            "ABIGAIL", "ADA", "ADELE", "ADRIANNA", "ALEXANDRA", "ALICE", "ALISON", "AMANDA", "AMELIA", "ANASTASIA",
            "ANDREA", "ANGELA", "ANGIE", "ANNA", "ANNE", "ANNIE", "ARIA", "ARIEL", "ASHLEY", "AUDREY", "AURORA",
            "AVA", "BELLA", "BETHANY", "BIANCA", "BRIDGET", "BRITTANY", "BROOKE", "CAMILA", "CAROLINE", "CHARLOTTE",
            "CHLOE", "CLAIRE", "DANIELLE", "DELILAH", "ELEANOR", "ELENA", "ELIZABETH", "ELLA", "EMILY", "EMMA",
            "EVA", "EVELYN", "FAITH", "FIONA", "GABRIELLA", "GRACE", "HANNAH", "HAILEY", "HARPER"
    ));
    public static final ArrayList<String> lastNameList = new ArrayList<>(java.util.Arrays.asList(
            "ADAMS", "ALLEN", "ANDERSON", "BAKER", "BELL", "BROWN", "CAMPBELL", "CARTER", "CLARK", "COOPER",
            "DAVIS", "EDWARDS", "EVANS", "GARCIA", "GREENE", "HARRIS", "HILL", "JOHNSON", "JONES", "KIM",
            "LEE", "LEWIS", "MARTIN", "MILLER", "MOORE", "MURPHY", "NGUYEN", "PEREZ", "ROBERTS", "RODRIGUEZ",
            "SMITH", "TAYLOR", "THOMPSON"
    ));

    public static ArrayList<Integer> age = new ArrayList<>(IntStream.rangeClosed(0, 100)
            .boxed()
            .toList());


    public static ArrayList<String> gender = new ArrayList<>(Arrays.asList("Male", "Female", "Other"));
}
