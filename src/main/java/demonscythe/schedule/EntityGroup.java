package demonscythe.schedule;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityGroup {
    GroupTickRunnable tickRunnable;
    private int regionx;
    private int regionz;
    private int ticksUnused;
    private World tickIn;
    private boolean isTicking;
    ArrayList<Entity> toTick;

    public EntityGroup(int x, int z, World toTickIn) {
        regionx = x;
        regionz = z;
        ticksUnused = 0;
        tickIn = toTickIn;
        toTick = new ArrayList<>();
        tickRunnable = new GroupTickRunnable(this);
        isTicking = false;
    }

    public void addEntity(Entity entity) {
        toTick.add(entity);
    }

    public boolean isInRegion(World world, Entity entity) {
        return ((entity.chunkCoordX == regionx) && (entity.chunkCoordZ == regionz) && (tickIn.equals(world)));
    }

    public void runTick() {
        for (Entity ticking : toTick) {
            tickIn.updateEntity(ticking);
        }
        toTick.clear();
    }

    public boolean removeGroup() {
        return (ticksUnused > 1200);
    }

    public void manageRunnable() {
        if (isTicking && toTick.isEmpty()) {
            EntityTickScheduler.groupRunnables.remove(tickRunnable);
            isTicking = false;
        }else if (!isTicking && !toTick.isEmpty()) {
            EntityTickScheduler.groupRunnables.add(tickRunnable);
            ticksUnused = 0;
            isTicking = true;
        }else if (!isTicking) {
            ticksUnused++;
        }
    }

    public boolean isEmpty() {
        return toTick.isEmpty();
    }
}
