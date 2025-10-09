package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.KoboldWarrior;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;

public class KoboldAttackSelector implements TargetingConditions.Selector {
	private final AbstractKoboldEntity kobold;

	public KoboldAttackSelector(AbstractKoboldEntity source) {
		this.kobold = source;
	}

	@Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
        if (target != null) {
            if (kobold.getDragonReference() != null) {
                if (lvl.getEntity(kobold.getDragonReference().getUUID()) instanceof LivingEntity dragon) {
                    if (kobold.distanceTo(dragon) <= 32.0F) {
                        if (dragon.getLastHurtByMob() != null && dragon.getLastHurtByMob().isAlive() && kobold.hasLineOfSight(dragon.getLastHurtByMob())) {
                            return target.is(dragon.getLastHurtByMob());
                        } else if (dragon.getLastHurtMob() != null && dragon.getLastHurtMob().isAlive() && kobold.hasLineOfSight(dragon.getLastHurtMob())) {
                            return target.is(dragon.getLastHurtMob());
                        }
                    }
                }
            }
            if (kobold instanceof KoboldWarrior) {
                return (target.getType().is(KoboldsTags.TARGETZ));
            }
            return (target.getType().is(KoboldsTags.TARGETS));
        }
        return false;
    }
}