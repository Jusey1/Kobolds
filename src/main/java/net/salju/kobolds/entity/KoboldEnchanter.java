package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldPotionGoal;
import net.salju.kobolds.entity.ai.KoboldTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;

public class KoboldEnchanter extends AbstractKoboldEntity {
	public KoboldEnchanter(EntityType<KoboldEnchanter> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
		this.goalSelector.addGoal(1, new KoboldTradeGoal(this, "gameplay/enchanter_loot"));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
		if (drop.getItem() instanceof ArmorItem || drop.is(Items.EMERALD)) {
			return super.canReplaceCurrentItem(drop, hand, slot);
		} else {
			return false;
		}
	}
}