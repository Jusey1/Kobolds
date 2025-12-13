package net.salju.kobolds.entity;

import net.salju.kobolds.events.KoboldsManager;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.DifficultyInstance;

public class KoboldWarrior extends AbstractKoboldEntity {
	public KoboldWarrior(EntityType<KoboldWarrior> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
        KoboldsManager.addPirateGoals(this);
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
		this.goalSelector.addGoal(1, new KoboldTridentGoal(this, 1.0D, 40, 12.0F));
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal<>(this, 1.2D, false));
		this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
	}

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_AXE.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        super.populateDefaultEquipmentSlots(randy, difficulty);
    }

	@Override
	public boolean isPreferredWeapon(ItemStack stack) {
		return stack.is(KoboldsTags.WAR);
	}
}