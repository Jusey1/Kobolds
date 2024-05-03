package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class KoboldMeleeGoal<T extends AbstractKoboldEntity> extends MeleeAttackGoal {
	private final T kobold;

	public KoboldMeleeGoal(T t, double d, boolean check) {
		super(t, d, check);
		this.kobold = t;
	}

	@Override
	public boolean canUse() {
		return (super.canUse() && this.hasRightWeapon());
	}

	@Override
	public boolean canContinueToUse() {
		return (super.canContinueToUse() && this.hasRightWeapon());
	}

	public boolean hasRightWeapon() {
		if (kobold.getMainHandItem().getItem() instanceof CrossbowItem || kobold.getMainHandItem().getItem() instanceof BowItem) {
			return false;
		} else {
			return !kobold.getMainHandItem().isEmpty();
		}
	}
}