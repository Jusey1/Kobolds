package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;

public class KoboldShieldGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldShieldGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return kobold.getOffhandItem().getItem() instanceof ShieldItem && raiseShield() && (kobold.getCD() == 0);
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		super.start();
		kobold.startUsingItem(InteractionHand.OFF_HAND);
	}

	@Override
	public void stop() {
		super.stop();
		kobold.stopUsingItem();
	}

	protected boolean raiseShield() {
		if (kobold.getTarget() != null && kobold.getTarget().isAlive()) {
			LivingEntity target = kobold.getTarget();
            return (target instanceof RangedAttackMob && kobold.distanceTo(target) >= 0.2D) || (kobold.distanceTo(target) >= 0.2D && kobold.distanceTo(target) <= 5.2D);
		}
		return false;
	}
}