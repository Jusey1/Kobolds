package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;

public class KoboldCrossbowGoal<T extends AbstractKoboldEntity & CrossbowAttackMob> extends RangedCrossbowAttackGoal<T> {
	private final T kobold;

	public KoboldCrossbowGoal(T t, double d, float f) {
		super(t, d, f);
		this.kobold = t;
	}

	@Override
	public void start() {
		super.start();
		this.kobold.setAggressive(true);
	}
}