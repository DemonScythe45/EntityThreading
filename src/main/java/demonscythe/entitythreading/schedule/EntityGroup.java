/*
 * Copyright (c) 2020  DemonScythe45
 *
 * This file is part of EntityThreading
 *
 *     EntityThreading is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation; version 3 only
 *
 *     EntityThreading is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with EntityThreading.  If not, see <https://www.gnu.org/licenses/>
 */

package demonscythe.entitythreading.schedule;

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
    private ArrayList<Entity> toTick;

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
        } else if (!isTicking && !toTick.isEmpty()) {
            EntityTickScheduler.groupRunnables.add(tickRunnable);
            ticksUnused = 0;
            isTicking = true;
        } else if (!isTicking) {
            ticksUnused++;
        }
    }

    public boolean isEmpty() {
        return toTick.isEmpty();
    }
}
