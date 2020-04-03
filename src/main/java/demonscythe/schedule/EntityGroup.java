package demonscythe.schedule;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityGroup {
    GroupTickRunnable tickRunnable;
    private int regionx;
    private int regionz;
    private World tickIn;
    ArrayList<Entity> toTick;

    public EntityGroup(int x, int z, World toTickIn) {
        this.regionx = x;
        this.regionz = z;
        tickIn = toTickIn;
        toTick = new ArrayList<>();
        tickRunnable = new GroupTickRunnable(this);
    }

    public void addEntity(Entity entity) {
        toTick.add(entity);
    }

    public void clear() {
        toTick.clear();
    }

    public boolean isInRegion(World world, Entity entity) {
        return (((entity.chunkCoordX  >> 5) == regionx) && ((entity.chunkCoordZ  >> 5) == regionz) && (tickIn.equals(world)));
    }

    public void runTick() {
        for (Entity ticking : toTick) {
            tickIn.updateEntity(ticking);
        }
    }

    public boolean isEmpty() {
        return toTick.isEmpty();
    }
}
