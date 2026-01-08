package observerPattern;

import EventSystem.*;

public class CapacityObserver implements EventListener {

    private final double capacityLimit;

    public CapacityObserver(double capacityLimit) {
        this.capacityLimit = capacityLimit;
    }


        @Override
        public void handleEvent(EventType eventType, Object data){
            if (eventType == EventType.CAPACITY_EXCEEDED) {
                int usedCapacity = (int) data;
                System.out.println("Warning: capacity exceeded 90%. Current usage: " + usedCapacity);
            }
        }


}

