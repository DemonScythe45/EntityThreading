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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EntityTickScheduler {
    public static ArrayList<GroupTickRunnable> groupRunnables = new ArrayList<>();
    private static ArrayList<EntityGroup> groups = new ArrayList<>();
    private static ArrayList<EntityGroup> groupRemoveList = new ArrayList<>();
    private static ExecutorService service = Executors.newFixedThreadPool(12);

    public static void queueEntity(World world, Entity toUpdate) {
        //System.out.println("Entity queued!" + toUpdate.getName() + " From world: " + world);
        for (EntityGroup group : groups) {
            if (group.isInRegion(world, toUpdate)) {
                group.addEntity(toUpdate);
                return;
            }
        }
        EntityGroup group = new EntityGroup(toUpdate.chunkCoordX, toUpdate.chunkCoordZ, world);
        group.addEntity(toUpdate);
        groups.add(group);
    }

    public static void waitForFinish() {
        //System.out.println("Groups Ticking" + groups.size());
        //System.out.println("Runnables: " + groupRunnables.size());

        for (EntityGroup groupVar : groups) {
            groupVar.manageRunnable();
            if (groupVar.removeGroup()) groupRemoveList.add(groupVar);
        }
        if (!groupRemoveList.isEmpty()) {
            groups.removeAll(groupRemoveList);
            //System.out.println("Removing " + groupRemoveList.size() + " unused groups!");
            groupRemoveList.clear();
        }

        try {
            List<Future<Boolean>> futures = service.invokeAll(groupRunnables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
