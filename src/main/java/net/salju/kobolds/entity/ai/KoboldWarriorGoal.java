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
import net.minecraft.world.InteractionHand;
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
			InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> item instanceof AxeItem);
			ItemStack weapon = hand.equals(InteractionHand.MAIN_HAND) ? kobold.getOffhandItem() : kobold.getMainHandItem();
			ItemStack axe = kobold.getItemInHand(hand);
			if (kobold.level() instanceof ServerLevel lvl) {
				ItemEntity drop = new ItemEntity(lvl, kobold.getX(), kobold.getY(), kobold.getZ(), weapon);
				drop.setPickUpDelay(10);
				lvl.addFreshEntity(drop);
				if (hand.equals(InteractionHand.MAIN_HAND)) {
					ItemEntity primary = new ItemEntity(lvl, kobold.getX(), kobold.getY(), kobold.getZ(), kobold.getPrimary());
					drop.setPickUpDelay(10);
					lvl.addFreshEntity(primary);
				}
				kobold.setItemSlot(EquipmentSlot.MAINHAND, axe);
				kobold.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
				kobold.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
				kobold.updateItemData();
				kobold.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), ConversionParams.single(kobold, true, true), newbie -> { EventHooks.onLivingConvert(kobold, newbie); });
			}
		});
	}

	private boolean isHoldingAxe() {
		return kobold.isHolding(stack -> stack.getItem() instanceof AxeItem);
	}
}
