package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class KoboldPirate extends AbstractKoboldEntity {
	public KoboldPirate(EntityType<KoboldPirate> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "kobolds:gameplay/trader_loot"));
	}
}