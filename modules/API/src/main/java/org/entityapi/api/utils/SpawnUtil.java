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
import org.entityapi.api.entity.ControllableEntity;
import org.entityapi.api.entity.ControllableEntityHandle;
import org.entityapi.reflection.refs.CraftWorldRef;

public class SpawnUtil {

    public static boolean spawnEntity(ControllableEntity controllableEntity, Location spawnLocation) {
        SafeConstructor<ControllableEntityHandle> entityConstructor = new Reflection().reflect(controllableEntity.getEntityType().getHandleClass()).getSafeConstructor(MinecraftReflection.getMinecraftClass("World"), ControllableEntity.class);
        ControllableEntityHandle controllableEntityHandle = entityConstructor.getAccessor().invoke(WorldUtil.toNMSWorld(spawnLocation.getWorld()), controllableEntity);
        controllableEntityHandle.setPositionRotation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());

        if (!spawnLocation.getChunk().isLoaded()) {
            spawnLocation.getChunk().load();
        }

        return CraftWorldRef.addEntity(spawnLocation.getWorld(), controllableEntityHandle);
    }
}