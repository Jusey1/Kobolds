package net.salju.kobolds.entity.ai;

import net.salju.kobolds.item.KoboldPotionUtils;
import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.KoboldsMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class KoboldEnchanterTradeGoal extends Goal {
	public final AbstractKoboldEntity kobold;
	public final String loc;

	public KoboldEnchanterTradeGoal(AbstractKoboldEntity kobold, String str) {
		this.kobold = kobold;
		this.loc = str;
	}

	@Override
	public boolean canUse() {
		return (checkHand() && !(this.kobold.hasEffect(MobEffects.MOVEMENT_SPEED)));
	}

	@Override
	public void start() {
		this.kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, -10, false, false));
		ItemStack potion = KoboldPotionUtils.sellPotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), Mth.nextInt(RandomSource.create(), 0, 100));
		KoboldsMod.queueServerWork(100, () -> {
			if (this.kobold.isAlive()) {
				this.kobold.swing(InteractionHand.MAIN_HAND, true);
				this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
				LevelAccessor world = this.kobold.level();
				if (world instanceof ServerLevel lvl) {
					if (Math.random() >= 0.1) {
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 0);
						Player target = lvl.getNearestPlayer(this.kobold, 7);
						if (target != null) {
							pos = target.position();
						} else if (pos == null) {
							pos = this.kobold.position();
						}
						BehaviorUtils.throwItem(this.kobold, potion, pos);
					} else {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, this.loc);
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 0);
						Player target = lvl.getNearestPlayer(this.kobold, 7);
						if (target != null) {
							pos = target.position();
						} else if (pos == null) {
							pos = this.kobold.position();
						}
						for (ItemStack stack : list) {
							BehaviorUtils.throwItem(this.kobold, stack, pos);
						}
					}
				}
				KoboldsMod.queueServerWork(20, () -> {
					this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
				});
			}
		});
	}

	protected boolean checkHand() {
		return (this.kobold.getOffhandItem().getItem() == (Items.EMERALD));
	}
}