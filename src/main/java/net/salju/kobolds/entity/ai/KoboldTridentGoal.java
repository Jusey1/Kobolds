package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.InteractionHand;

public class KoboldTridentGoal extends RangedAttackGoal {
	public final AbstractKoboldEntity kobold;

	public KoboldTridentGoal(AbstractKoboldEntity kobold, double d, int i, float f) {
		super(kobold, d, i, f);
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.isHoldingTrident();
	}

	@Override
	public void start() {
		super.start();
		kobold.setAggressive(true);
		kobold.startUsingItem(InteractionHand.MAIN_HAND);
	}

	@Override
	public void stop() {
		super.stop();
		kobold.setAggressive(false);
		kobold.stopUsingItem();
	}

	private boolean isHoldingTrident() {
		return kobold.getMainHandItem().getItem() instanceof TridentItem;
	}
}