package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.InteractionHand;

public class KoboldTridentGoal extends RangedAttackGoal {
	public final AbstractKoboldEntity kobold;

	public KoboldTridentGoal(RangedAttackMob kobold, double dub, int inty, float enty) {
			super(kobold, dub, inty, enty);
			this.kobold = (AbstractKoboldEntity) kobold;
		}

	public boolean canUse() {
		return (super.canUse() && kobold.getMainHandItem().getItem() instanceof TridentItem);
	}

	public void start() {
		super.start();
		kobold.setAggressive(true);
		kobold.startUsingItem(InteractionHand.MAIN_HAND);
	}

	public void stop() {
		super.stop();
		kobold.setAggressive(false);
		kobold.stopUsingItem();
	}
}