package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.Difficulty;

public class KoboldRevengeGoal extends HurtByTargetGoal {
	public KoboldRevengeGoal(AbstractKoboldEntity kobold) {
		super(kobold);
		this.setAlertOthers(AbstractKoboldEntity.class);
	}

	@Override
	public void start() {
		super.start();
		for (AbstractKoboldEntity kobolds : this.mob.level().getEntitiesOfClass(AbstractKoboldEntity.class, this.mob.getBoundingBox().inflate(32.0D), kobold -> (kobold.getTarget() == null || !kobold.getTarget().isAlive()))) {
			if (this.mob.level().getDifficulty() != Difficulty.PEACEFUL) {
				kobolds.setTarget(this.mob.getTarget());
			}
		}
	}
}