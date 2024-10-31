package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;

public class KoboldWarriorGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldWarriorGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return (checkHand() && !(this.kobold.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)));
	}

	@Override
	public void start() {
		this.kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 10, false, false));
		Kobolds.queueServerWork(600, () -> {
			ItemStack weapon = this.kobold.getMainHandItem();
			ItemStack off = this.kobold.getOffhandItem();
			LevelAccessor world = this.kobold.level();
			double x = this.kobold.getX();
			double y = this.kobold.getY();
			double z = this.kobold.getZ();
			if (world instanceof ServerLevel lvl) {
				ItemEntity drop = new ItemEntity(lvl, x, y, z, weapon);
				drop.setPickUpDelay(10);
				world.addFreshEntity(drop);
				this.kobold.setItemSlot(EquipmentSlot.MAINHAND, off);
				this.kobold.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
				this.kobold.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
				this.kobold.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), ConversionParams.single(this.kobold, true, true), newbie -> { EventHooks.onLivingConvert(this.kobold, newbie); });
			}
		});
	}

	protected boolean checkHand() {
		return (this.kobold.getOffhandItem().getItem() instanceof AxeItem);
	}
}