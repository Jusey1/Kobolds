package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.server.level.ServerLevel;

public class KoboldWarriorGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldWarriorGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return this.isHoldingAxe();
	}

	@Override
	public void start() {
		kobold.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600, 10, false, false));
		Kobolds.queueServerWork(600, () -> {
			if (kobold.level() instanceof ServerLevel lvl) {
				ItemEntity primary = new ItemEntity(lvl, kobold.getX(), kobold.getY(), kobold.getZ(), kobold.getPrimary());
				primary.setPickUpDelay(10);
				lvl.addFreshEntity(primary);
				if (!kobold.getSecondary().isEmpty()) {
					ItemEntity secondary = new ItemEntity(lvl, kobold.getX(), kobold.getY(), kobold.getZ(), kobold.getSecondary());
					secondary.setPickUpDelay(10);
					lvl.addFreshEntity(secondary);
				}
				kobold.setItemSlot(EquipmentSlot.MAINHAND, kobold.getItemInHand(ProjectileUtil.getWeaponHoldingHand(kobold, item -> item instanceof AxeItem)));
				kobold.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
				kobold.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
				kobold.setPrimary(kobold.getMainHandItem());
                kobold.setSecondary(kobold.getOffhandItem());
				kobold.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), ConversionParams.single(kobold, true, true), newbie -> { EventHooks.onLivingConvert(kobold, newbie); });
			}
		});
	}

	private boolean isHoldingAxe() {
		return kobold.isHolding(stack -> stack.getItem() instanceof AxeItem);
	}
}
