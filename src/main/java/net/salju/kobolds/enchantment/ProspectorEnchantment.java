package net.salju.kobolds.enchantment;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;

public class ProspectorEnchantment extends Enchantment {
	public ProspectorEnchantment(Enchantment.Rarity rare, EquipmentSlot... slots) {
		super(rare, EnchantmentCategory.DIGGER, slots);
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return ench == Enchantments.MOB_LOOTING ? false : super.checkCompatibility(ench);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return (stack.getItem() instanceof AxeItem);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public void doPostAttack(LivingEntity source, Entity target, int lvl) {
		if (target instanceof Enemy) {
			double check = source.hasEffect(MobEffects.LUCK) ? 0.25 : 0.05;
			if (!target.isAlive() && !target.level().isClientSide()) {
				if (Math.random() <= check) {
					for (int i = 0; i < 1 * lvl; i++) {
						target.level().addFreshEntity(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), new ItemStack(Items.EMERALD)));
					}
				}
			}
		}
	}
}