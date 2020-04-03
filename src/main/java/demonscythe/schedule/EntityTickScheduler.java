package demonscythe.schedule;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EntityTickScheduler {
    private static ArrayList<EntityGroup> groups = new ArrayList<>();
    private static ArrayList<Callable<Boolean>> groupRunnables = new ArrayList<>();
    private static ExecutorService service = Executors.newFixedThreadPool(6);

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
            EntityGroup group = new EntityGroup((toUpdate.chunkCoordX  >> 5), (toUpdate.chunkCoordZ  >> 5), world);
            group.addEntity(toUpdate);
            groups.add(group);
            groupRunnables.add(group.tickRunnable);
        }
    }

    public static void waitForFinish() {
        System.out.println("Groups Ticking" + groups.size());
        System.out.println("Runnables: " + groupRunnables.size());
        for (EntityGroup groupVar : groups) {
            if (groupVar.isEmpty()) groupRunnables.remove(groupVar.tickRunnable);
        }
        groups.removeIf(EntityGroup::isEmpty);

        try {
            List<Future<Boolean>> futures = service.invokeAll(groupRunnables);
            for (Future<Boolean> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
