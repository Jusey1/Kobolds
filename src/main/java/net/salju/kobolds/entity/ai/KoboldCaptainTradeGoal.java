package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.KoboldsMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class KoboldCaptainTradeGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldCaptainTradeGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return (checkHand() && !(this.kobold.hasEffect(MobEffects.MOVEMENT_SPEED)));
	}

	@Override
	public void start() {
		this.kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, -10, false, false));
		ItemStack off = this.kobold.getOffhandItem();
		LevelAccessor world = this.kobold.level();
		if (off.is(KoboldsTags.CAPTAIN_ONE)) {
			KoboldsMod.queueServerWork(100, () -> {
				if (this.kobold.isAlive()) {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_one_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
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
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				}
			});
		} else if (off.is(KoboldsTags.CAPTAIN_TWO)) {
			KoboldsMod.queueServerWork(100, () -> {
				if (this.kobold.isAlive()) {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_two_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
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
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				}
			});
		} else if (off.is(KoboldsTags.CAPTAIN_THREE)) {
			KoboldsMod.queueServerWork(100, () -> {
				if (this.kobold.isAlive()) {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_three_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
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
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				}
			});
		}
	}

	protected boolean checkHand() {
		ItemStack off = this.kobold.getOffhandItem();
		return (off.is(KoboldsTags.CAPTAIN_ONE) || off.is(KoboldsTags.CAPTAIN_TWO) || off.is(KoboldsTags.CAPTAIN_THREE));
	}
}