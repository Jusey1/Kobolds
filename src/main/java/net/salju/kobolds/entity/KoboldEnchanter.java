package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.KoboldEnchanterTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
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
		this.goalSelector.addGoal(1, new KoboldEnchanterTradeGoal(this, "kobolds:gameplay/enchanter_loot"));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof ArmorItem) {
			if (EnchantmentHelper.hasBindingCurse(hand)) {
				return false;
			} else if (hand.isEmpty() || hand.getItem() instanceof BlockItem) {
				return true;
			} else if (hand.getItem() instanceof ArmorItem) {
				ArmorItem newbie = (ArmorItem) drop.getItem();
				ArmorItem worn = (ArmorItem) hand.getItem();
				if (newbie.getDefense() != worn.getDefense()) {
					return newbie.getDefense() > worn.getDefense();
				} else if (newbie.getToughness() != worn.getToughness()) {
					return newbie.getToughness() > worn.getToughness();
				} else {
					return this.canReplaceEqualItem(drop, hand);
				}
			} else {
				return false;
			}
		} else if (drop.getItem() == Items.EMERALD && hand.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}