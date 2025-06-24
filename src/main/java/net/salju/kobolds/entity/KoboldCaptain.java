package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.*;
import net.salju.kobolds.compat.Supplementaries;
import net.neoforged.fml.ModList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class KoboldCaptain extends AbstractKoboldEntity {
	public KoboldCaptain(EntityType<KoboldCaptain> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
		if (ModList.get().isLoaded("supplementaries")) {
			Supplementaries.addGoals(this);
		}
		this.goalSelector.addGoal(1, new KoboldTridentGoal(this, 1.0D, 40, 12.0F));
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "gameplay/captain_loot"));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
	}
}