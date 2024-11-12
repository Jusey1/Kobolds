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
		if (kobold instanceof KoboldWarrior) {
			return (target.getType().is(KoboldsTags.TARGETZ));
		}
		return (target.getType().is(KoboldsTags.TARGETS));
	}
}