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

package org.entityapi.api;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.entityapi.api.entity.ControllableEntity;
import org.entityapi.api.entity.ControllableEntityType;
import org.entityapi.api.entity.DespawnReason;

import java.util.Collection;

public interface EntityManager {

    ChunkManager getChunkManager();

    public Plugin getOwningPlugin();

    public boolean willKeepEntitiesInMemory();

    public void setKeepEntitiesInMemory(boolean bool);

    Integer getNextID();

    Integer getNextID(int index);

    public ControllableEntity spawnEntity(ControllableEntityType entityType, Location location);

    public ControllableEntity spawnEntity(ControllableEntityType entityType, Location location, boolean prepare);

    public Collection<ControllableEntity> getEntities();

    public void despawnAll();

    public void despawnAll(DespawnReason despawnReason);

    void despawn(ControllableEntity controllableEntity);

    void despawn(ControllableEntity controllableEntity, DespawnReason despawnReason);

    boolean spawn(ControllableEntity controllableEntity, Location location);

    @Override
    public String toString();

    @Override
    public int hashCode();
}
