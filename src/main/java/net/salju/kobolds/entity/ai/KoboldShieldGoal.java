package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.InteractionHand;

public class KoboldShieldGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldShieldGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return this.isHoldingShield() && this.raiseShield();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		super.start();
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> item instanceof ShieldItem);
		kobold.startUsingItem(hand);
	}

	@Override
	public void stop() {
		super.stop();
		kobold.stopUsingItem();
	}

	private boolean raiseShield() {
		if (kobold.getTarget() != null && kobold.getTarget().isAlive() && this.kobold.getCD() <= 0) {
			LivingEntity target = kobold.getTarget();
            return (target instanceof RangedAttackMob && kobold.distanceTo(target) >= 0.2D) || (kobold.distanceTo(target) >= 0.2D && kobold.distanceTo(target) <= 5.2D);
		}
		return false;
	}

	private boolean isHoldingShield() {
		return kobold.isHolding(stack -> stack.getItem() instanceof ShieldItem);
	}
}