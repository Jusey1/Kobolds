package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.*;
import net.salju.kobolds.compat.Supplementaries;
import net.neoforged.fml.ModList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KoboldEngineer extends AbstractKoboldEntity {
	public KoboldEngineer(EntityType<KoboldEngineer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		if (ModList.get().isLoaded("supplementaries")) {
			Supplementaries.addGoals(this);
		}
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldSpecialRangeGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "gameplay/engineer_loot"));
		this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
	}

	@Override
	public boolean isPreferredWeapon(ItemStack stack) {
		return stack.is(KoboldsTags.ENGI);
	}
}