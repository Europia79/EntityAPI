package io.snw.entityapi;

import com.google.common.collect.Maps;
import io.snw.entityapi.api.ControllableEntity;
import io.snw.entityapi.api.ControllableEntityType;
import io.snw.entityapi.exceptions.NameRequiredException;
import io.snw.entityapi.reflection.SafeConstructor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;

public class EntityManager {

    private final Plugin owningPlugin;
    private boolean keepEntitiesInMemory;

    private final Map<Integer, ControllableEntity> entities = Maps.newConcurrentMap();

    public EntityManager(Plugin plugin, boolean keepEntitiesInMemory) {
        this.owningPlugin = plugin;
        this.keepEntitiesInMemory = keepEntitiesInMemory;
    }

    public Plugin getOwningPlugin() {
        return this.owningPlugin;
    }

    public boolean isKeepEntitiesInMemory() {
        return this.keepEntitiesInMemory;
    }

    public void setKeepEntitiesInMemory(boolean bool) {
        this.keepEntitiesInMemory = bool;
    }

    protected Integer getNextID() {
        return getNextID(Integer.MIN_VALUE);
    }

    protected Integer getNextID(int index) {
        Set<Integer> ids = this.entities.keySet();
        while (ids.contains(index)) {
            index++;
        }
        return index;
    }

    public ControllableEntity spawnEntity(ControllableEntityType entityType, Location location) {
        return spawnEntity(entityType, location, true);
    }

    public ControllableEntity spawnEntity(ControllableEntityType entityType, Location location, boolean prepare) {
        if (entityType.isNameRequired())
            throw new NameRequiredException();

        Integer id = getNextID();
        ControllableEntity entity = createEntity(entityType, id);

        if (entity == null)
            return null;

        // TODO: take care of actual spawning

        return entity;
    }

    protected ControllableEntity createEntity(ControllableEntityType entityType, int id) {
        SafeConstructor<? extends ControllableEntity> constructor = new SafeConstructor<>(entityType.getControllableClass());
        ControllableEntity entity = constructor.newInstance(id, this);
        this.entities.put(id, entity);
        return entity;
    }
}