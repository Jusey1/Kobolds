package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KoboldWarrior extends AbstractKoboldEntity {
	public KoboldWarrior(EntityType<KoboldWarrior> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
		this.goalSelector.addGoal(1, new KoboldTridentGoal(this, 1.0D, 40, 12.0F));
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new KoboldAttackSelector(this)));
	}

	@Override
	public boolean isPreferredWeapon(ItemStack stack) {
		return stack.getItem() instanceof AxeItem;
	}
}