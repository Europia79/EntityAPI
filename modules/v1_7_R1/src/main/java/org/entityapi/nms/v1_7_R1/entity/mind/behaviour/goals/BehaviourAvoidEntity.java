/*
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

package org.entityapi.nms.v1_7_R1.entity.mind.behaviour.goals;

import net.minecraft.server.v1_7_R1.*;
import org.entityapi.api.entity.ControllableEntity;
import org.entityapi.api.entity.mind.behaviour.BehaviourType;
import org.entityapi.api.reflection.refs.NMSEntityClassRef;
import org.entityapi.nms.v1_7_R1.NMSEntityUtil;
import org.entityapi.nms.v1_7_R1.entity.mind.behaviour.BehaviourBase;
import org.entityapi.nms.v1_7_R1.entity.selector.EntitySelectorViewable;

import java.util.List;

/**
 * Makes the entity avoid any other entity of the given class type
 */

public class BehaviourAvoidEntity extends BehaviourBase {

    private EntitySelectorViewable selector = new EntitySelectorViewable(this);
    private Class<? extends Entity> classToAvoid;
    private float f;
    private double speedWhenFar;
    private double speedWhenNear;
    private Entity entityToAvoid;
    private PathEntity path;

    public BehaviourAvoidEntity(ControllableEntity controllableEntity, Class<? extends org.bukkit.entity.Entity> classToAvoid, float minDistance, double speedWhenFar, double speedWhenNear) {
        super(controllableEntity);
        this.classToAvoid = (Class<? extends Entity>) NMSEntityClassRef.getNMSClass(classToAvoid);
        if (this.classToAvoid == null && !(EntityLiving.class.isAssignableFrom(classToAvoid))) {
            throw new IllegalArgumentException("Could not find valid NMS class for " + classToAvoid.getSimpleName());
        }
        this.speedWhenFar = speedWhenFar;
        this.speedWhenNear = speedWhenNear;
        this.f = minDistance;
    }

    @Override
    public BehaviourType getType() {
        return BehaviourType.INSTINCT;
    }

    @Override
    public String getDefaultKey() {
        return "Avoid Entity";
    }

    @Override
    public boolean shouldStart() {
        if (this.classToAvoid == EntityHuman.class) {
            if (this.getHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal) this.getHandle()).isTamed()) {
                return false;
            }

            this.entityToAvoid = this.getHandle().world.findNearbyPlayer(this.getHandle(), (double) this.f);
            if (this.entityToAvoid == null) {
                return false;
            }
        } else {
            List list = this.getHandle().world.a(this.classToAvoid, this.getHandle().boundingBox.grow((double) this.f, 3.0D, (double) this.f), this.selector);

            if (list.isEmpty()) {
                return false;
            }

            this.entityToAvoid = (Entity) list.get(0);
        }

        Vec3D vec3d = org.entityapi.nms.v1_7_R1.RandomPositionGenerator.b(this.getHandle(), 16, 7, this.getHandle().world.getVec3DPool().create(this.entityToAvoid.locX, this.entityToAvoid.locY, this.entityToAvoid.locZ));

        if (vec3d == null) {
            return false;
        } else if (this.entityToAvoid.e(vec3d.c, vec3d.d, vec3d.e) < this.entityToAvoid.e(this.getHandle())) {
            return false;
        } else {
            this.path = NMSEntityUtil.getNavigation(this.getHandle()).a(vec3d.c, vec3d.d, vec3d.e);
            return this.path == null ? false : this.path.b(vec3d);
        }
    }

    @Override
    public boolean shouldContinue() {
        return !NMSEntityUtil.getNavigation(this.getHandle()).g();
    }

    @Override
    public void start() {
        NMSEntityUtil.getNavigation(this.getHandle()).a(this.path, this.speedWhenFar);
    }

    @Override
    public void finish() {
        this.entityToAvoid = null;
    }

    @Override
    public void tick() {
        if (this.getHandle().e(this.entityToAvoid) < 49.0D) {
            NMSEntityUtil.getNavigation(this.getHandle()).a(this.speedWhenNear);
        } else {
            NMSEntityUtil.getNavigation(this.getHandle()).a(this.speedWhenFar);
        }
    }

    public static ControllableEntity getEntityFor(BehaviourAvoidEntity behaviourAvoidEntity) {
        return behaviourAvoidEntity.getControllableEntity();
    }
}