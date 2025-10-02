package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.ai.*;
import net.salju.kobolds.compat.Supplementaries;
import net.neoforged.fml.ModList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.DifficultyInstance;

public class Kobold extends AbstractKoboldEntity {
	public Kobold(EntityType<Kobold> type, Level world) {
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
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "gameplay/trader_loot"));
		this.goalSelector.addGoal(1, new KoboldWarriorGoal(this));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
	}

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
        if (Math.random() >= 0.6) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
        }
        super.populateDefaultEquipmentSlots(randy, souls);
    }
}