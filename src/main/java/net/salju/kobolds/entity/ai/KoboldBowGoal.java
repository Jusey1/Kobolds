package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;

public class KoboldBowGoal<T extends AbstractKoboldEntity & RangedAttackMob> extends RangedBowAttackGoal<T> {
	private final T kobold;

	public KoboldBowGoal(T t, double d, int i, float f) {
		super(t, d, i, f);
		this.kobold = t;
	}

	@Override
	public boolean canUse() {
		return kobold.getTarget() != null && kobold.getTarget().isAlive() && this.isHoldingBow() && !kobold.isHoldingPotion();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void stop() {
		super.stop();
		kobold.getMoveControl().strafe(0.0F, 0.0F);
		kobold.getNavigation().stop();
	}

	@Override
	protected boolean isHoldingBow() {
		return kobold.isHolding(stack -> stack.getItem() instanceof BowItem);
	}
}