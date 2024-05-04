package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldEnchanterTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.EntityType;

public class KoboldEnchanter extends AbstractKoboldEntity {
	public KoboldEnchanter(EntityType<KoboldEnchanter> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
		this.goalSelector.addGoal(1, new KoboldEnchanterTradeGoal(this, "kobolds:gameplay/enchanter_loot"));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof ArmorItem || drop.getItem() == Items.EMERALD) {
			return super.canReplaceCurrentItem(drop, hand);
		} else {
			return false;
		}
	}
}