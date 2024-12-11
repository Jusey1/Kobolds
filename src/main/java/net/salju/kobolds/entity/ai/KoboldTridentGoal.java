package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.InteractionHand;

public class KoboldTridentGoal extends RangedAttackGoal {
	public final AbstractKoboldEntity kobold;

	public KoboldTridentGoal(RangedAttackMob kobold, double dub, int inty, float enty) {
		super(kobold, dub, inty, enty);
		this.kobold = (AbstractKoboldEntity) kobold;
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
		return kobold.isHolding(stack -> stack.getItem() instanceof TridentItem);
	}
}