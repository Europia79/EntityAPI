package io.snw.entityapi.entity;

import io.snw.entityapi.api.ControllableEntityType;
import io.snw.entityapi.api.EntitySound;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;

//TODO: finish this
public class ControllableEnderDragon extends ControllableBaseEntity<EnderDragon> {

    public ControllableEnderDragon(ControllableEnderDragonEntity entityHandle) {
        super(ControllableEntityType.ENDERDRAGON);
        this.handle = entityHandle;
        this.loot = entityHandle.getDefaultMaterialLoot();
    }
    
    @Override
    public void initSounds() {
        this.setSound(EntitySound.GROWL, Sound.ENDERDRAGON_GROWL);
        this.setSound(EntitySound.HIT, Sound.ENDERDRAGON_HIT);
        this.setSound(EntitySound.DEATH, Sound.ENDERDRAGON_DEATH);
        this.setSound(EntitySound.WINGS, Sound.ENDERDRAGON_WINGS);
    }
}