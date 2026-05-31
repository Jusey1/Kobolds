package net.salju.kobolds.entity.ai;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class KoboldTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	public KoboldTargetGoal(Mob kobold, Class<T> target, TargetingConditions.Selector predicate) {
		super(kobold, target, 12, true, false, predicate);
	}

	@Override
	protected double getFollowDistance() {
		if (this.mob.getVehicle() != null) {
			return super.getFollowDistance() * 3;
		}
		return super.getFollowDistance();
	}
}