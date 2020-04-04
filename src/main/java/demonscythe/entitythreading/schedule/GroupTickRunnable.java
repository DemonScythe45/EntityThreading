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
        return toTick.isEmpty();
    }
}
