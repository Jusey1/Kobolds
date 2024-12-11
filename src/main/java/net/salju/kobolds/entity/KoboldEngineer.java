package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
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
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldSpecialRangeGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "gameplay/engineer_loot"));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new KoboldAttackSelector(this)));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
		if (drop.is(KoboldsTags.RANGED)) {
			if (drop.is(hand.getItem())) {
				return this.canReplaceEqualItem(drop, hand);
			} else {
				return !hand.is(KoboldsTags.RANGED);
			}
		}
		return super.canReplaceCurrentItem(drop, hand, slot);
	}

	@Override
	public boolean isPreferredWeapon(ItemStack stack) {
		return stack.getItem() instanceof CrossbowItem;
	}
}