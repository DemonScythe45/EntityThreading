package demonscythe.entitythreading.schedule;

import java.util.concurrent.Callable;

public class GroupTickRunnable implements Callable<Boolean> {
    private EntityGroup toTick;

    public GroupTickRunnable(EntityGroup ticking) {
        toTick = ticking;
    }

    @Override
    public Boolean call() throws Exception {
        toTick.runTick();
        return false;
    }

    public boolean isGroupEmpty() {
        return toTick.toTick.isEmpty();
    }
}
