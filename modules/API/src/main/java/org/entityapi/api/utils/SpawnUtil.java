/*
 * Copyright (C) EntityAPI Team
 *
 * This file is part of EntityAPI.
 *
 * EntityAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EntityAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EntityAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.entityapi.api.utils;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeConstructor;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.entityapi.api.entity.ControllableEntity;
import org.entityapi.api.entity.ControllableEntityHandle;
import org.entityapi.api.events.ControllableEntityPreSpawnEvent;
import org.entityapi.api.plugin.EntityAPI;
import org.entityapi.game.EntityRegistrationEntry;
import org.entityapi.game.GameRegistry;
import org.entityapi.game.IEntityRegistry;

public class SpawnUtil {

    private SpawnUtil() {
    }

    public static boolean spawnEntity(ControllableEntity controllableEntity, Location spawnLocation) {
        ControllableEntityPreSpawnEvent spawnEvent = new ControllableEntityPreSpawnEvent(controllableEntity, spawnLocation);
        EntityAPI.getCore().getServer().getPluginManager().callEvent(spawnEvent);
        return !spawnEvent.isCancelled() && addEntity(controllableEntity, spawnEvent.getSpawnLocation());
    }

    private static boolean addEntity(ControllableEntity controllableEntity, Location spawnLocation) {
        SafeConstructor<ControllableEntityHandle> entityConstructor = new Reflection().reflect(controllableEntity.getEntityType().getHandleClass()).getSafeConstructor(MinecraftReflection.getMinecraftClass("World"), controllableEntity.getEntityType().getControllableInterface());
        ControllableEntityHandle controllableEntityHandle = entityConstructor.getAccessor().invoke(WorldUtil.toNMSWorld(spawnLocation.getWorld()), controllableEntity);
        controllableEntityHandle.setPositionRotation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());

        if (!spawnLocation.getChunk().isLoaded()) {
            spawnLocation.getChunk().load();
        }

        EntityRegistrationEntry oldEntry = GameRegistry.get(IEntityRegistry.class).getDefaultEntryFor(controllableEntity.getEntityType());
        GameRegistry.get(IEntityRegistry.class).register(new EntityRegistrationEntry(
                controllableEntity.getEntityType().getName(),
                controllableEntity.getEntityType().getId(),
                controllableEntity.getEntityType().getHandleClass()
        ));

        boolean spawned = WorldUtil.addEntity(spawnLocation.getWorld(), controllableEntityHandle, CreatureSpawnEvent.SpawnReason.CUSTOM);

        GameRegistry.get(IEntityRegistry.class).register(oldEntry);

        return spawned;
    }
}
