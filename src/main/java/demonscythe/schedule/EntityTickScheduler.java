package demonscythe.schedule;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EntityTickScheduler {
    private static ArrayList<EntityGroup> groups = new ArrayList<>();
    private static ArrayList<EntityGroup> groupRemoveList = new ArrayList<>();
    public static ArrayList<GroupTickRunnable> groupRunnables = new ArrayList<>();
    private static ExecutorService service = Executors.newFixedThreadPool(12);

    public static void queueEntity(World world, Entity toUpdate) {
        //System.out.println("Entity queued!" + toUpdate.getName() + " From world: " + world);
        boolean added = false;
        for (EntityGroup group : groups) {
            if (group.isInRegion(world, toUpdate)) {
                group.addEntity(toUpdate);
                added = true;
                break;
            }
        }
        if (!added) {
            EntityGroup group = new EntityGroup(toUpdate.chunkCoordX, toUpdate.chunkCoordZ, world);
            group.addEntity(toUpdate);
            groups.add(group);
        }
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
