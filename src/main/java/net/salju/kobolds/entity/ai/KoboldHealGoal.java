package net.salju.kobolds.entity.ai;

import net.salju.kobolds.item.KoboldPotionUtils;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.KoboldRascal;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.KoboldsMod;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;

public class KoboldHealGoal extends Goal {
	public final AbstractKoboldEntity kobold;
	private ItemStack potion = new ItemStack(KoboldsItems.KOBOLD_POTION.get());

	public KoboldHealGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return (kobold.getHealth() < 12) && (kobold.getCD() == 0) && checkHand() && !kobold.isBaby() && !(kobold instanceof KoboldRascal);
	}

	@Override
	public void start() {
		kobold.setItemInHand(InteractionHand.OFF_HAND, KoboldPotionUtils.makePotion(potion, MobEffects.HEAL, MobEffects.REGENERATION, 1, 900));
		kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 32, -4, false, false));
		kobold.playSound(SoundEvents.GENERIC_DRINK, 0.5F, 1.0F);
		KoboldsMod.queueServerWork(32, () -> {
			for (MobEffectInstance effect : KoboldPotionUtils.getEffects(kobold.getOffhandItem())) {
				kobold.addEffect(effect);
			}
			kobold.setCD(900);
			kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		});
	}

	protected boolean checkHand() {
		return (kobold.getOffhandItem().isEmpty()) || (kobold.getOffhandItem().getItem() == KoboldsItems.KOBOLD_POTION.get());
	}
}