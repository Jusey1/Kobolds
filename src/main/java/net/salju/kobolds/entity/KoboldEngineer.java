package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EntityType;

public class KoboldEngineer extends AbstractKoboldEntity {
	public KoboldEngineer(EntityType<KoboldEngineer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "kobolds:gameplay/engineer_loot"));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof ArmorItem || drop.getItem() instanceof CrossbowItem || drop.getItem() == Items.EMERALD) {
			return super.canReplaceCurrentItem(drop, hand);
		} else {
			return false;
		}
	}
}