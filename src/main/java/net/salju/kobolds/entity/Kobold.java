package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldWarriorGoal;
import net.salju.kobolds.entity.ai.KoboldTradeGoal;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.EntityType;

public class Kobold extends AbstractKoboldEntity {
	public Kobold(EntityType<Kobold> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "kobolds:gameplay/trader_loot"));
		this.goalSelector.addGoal(1, new KoboldWarriorGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
	}
}