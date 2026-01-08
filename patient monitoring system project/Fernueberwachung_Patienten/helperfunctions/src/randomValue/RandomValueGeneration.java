package randomValue;

import coreLogic.Patient;
import coreLogic.VitalData;
import exeptions.ExceptionHelper;
import randomValue.enums.PatientValues;
import randomValue.enums.VitalDataValues;
import util.FormateUtil;

import java.util.*;

public class RandomValueGeneration {

    public RandomValueGeneration() {
        // Constructor can be used for initialization if needed
    }

   public Patient buildNewPatient(int patientId, int roomId) {

       try {

           Collections.shuffle(PatientValues.gender);
           Collections.shuffle(PatientValues.age);
           Collections.shuffle(PatientValues.lastNameList);
           VitalData vitalData = generateVitalData();

           switch(PatientValues.gender.get(0)) {

               case "Male" -> {
                   Collections.shuffle(PatientValues.maleNameList);
                   return new Patient(
                           patientId,
                           PatientValues.maleNameList.get(0),
                           PatientValues.lastNameList.get(0),
                           PatientValues.age.get(0),
                           PatientValues.gender.get(0),
                           vitalData,
                           null,
                           roomId);
               }

               case "Female" -> {
                   Collections.shuffle(PatientValues.femaleFirstNameList);
                   return new Patient(
                           patientId,
                           PatientValues.femaleFirstNameList.get(0),
                           PatientValues.lastNameList.get(0),
                           PatientValues.age.get(0),
                           PatientValues.gender.get(0),
                           vitalData,
                           null,
                           roomId);
               }

               case "Other" -> {
                   ArrayList<String> otherFirstNames = (ArrayList<String>) chooseRandomList(PatientValues.maleNameList, PatientValues.femaleFirstNameList);
                   Collections.shuffle(otherFirstNames);
                   return new Patient(
                           patientId,
                           otherFirstNames.get(0),
                           PatientValues.lastNameList.get(0),
                           PatientValues.age.get(0),
                           PatientValues.gender.get(0),
                           vitalData,
                           null,
                           roomId);
               }

               default -> {
                   throw new IllegalStateException("I dont know how we got here, im pretty sure this should not be possible. But congrats");
               }
           }

       } catch (Exception e) {
          ExceptionHelper.handleException(e);
           return null;
       }
   }

   public VitalData generateVitalData() {
       try {
           return new VitalData(
                getBiasedRandomValue(VitalDataValues.heartRate, 60, 100, 0.8, 60),
                getBiasedRandomValue(VitalDataValues.respirationRate, 12, 20, 0.8, 20),
                getBiasedRandomValue(VitalDataValues.bodyTemperature, 36,37, 0.8, 37),
                getBiasedRandomValue(VitalDataValues.pulseOximetry, 95, 100, 0.8, 98),
                FormateUtil.getEuropeanDateFormat(new Date())
           );
       } catch (Exception e) {
           ExceptionHelper.handleException(e);
           return null;
       }
   }

    private int getBiasedRandomValue(List<Integer> values, int normalStartIndex, int normalEndIndex, double normalWeight, int fallback) {
        if (values == null || values.isEmpty()) return fallback;

        Random rand = new Random();
        double r = rand.nextDouble();

        if (r <= normalWeight) {
            // Ziehe aus dem Normalbereich
            int index = rand.nextInt(normalEndIndex - normalStartIndex + 1) + normalStartIndex;
            return values.get(index);
        } else {
            // Ziehe aus dem Rest
            List<Integer> rest = new ArrayList<>(values);
            rest.subList(normalStartIndex, normalEndIndex + 1).clear(); // Entferne normalen Bereich
            if (rest.isEmpty()) return values.get(normalStartIndex); // Fallback, falls nichts übrig
            return rest.get(rand.nextInt(rest.size()));
        }
    }



    private List<?> chooseRandomList(List<?> list1 , List<?> list2) {
       return Math.random() < 0.5 ? list1 : list2;
   }
}
