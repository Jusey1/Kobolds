package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.KoboldWither;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class WitherboldAttackSelector implements TargetingConditions.Selector {
	private final KoboldWither kobold;

	public WitherboldAttackSelector(KoboldWither source) {
		this.kobold = source;
	}

	@Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (target instanceof KoboldWither) {
			return false;
		}
		return target.getType().is(EntityTypeTags.UNDEAD);
	}
}