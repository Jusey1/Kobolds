package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.KoboldsMod;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class KoboldCaptain extends AbstractKoboldEntity {
	public KoboldCaptain(PlayMessages.SpawnEntity packet, Level world) {
		this(KoboldsMobs.KOBOLD_CAPTAIN.get(), world);
	}

	public KoboldCaptain(EntityType<KoboldCaptain> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldCaptain.KoboldTradeGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		super.mobInteract(player, hand);
		ItemStack gem = (player.getItemInHand(hand).copy());
		ItemStack off = this.getOffhandItem();
		LevelAccessor world = this.level();
		if (!world.isClientSide() && this.isAlive()) {
			if (off.getItem() == (ItemStack.EMPTY).getItem()) {
				if (gem.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_one"))) || gem.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_two")))
						|| gem.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_three")))) {
					this.setItemInHand(InteractionHand.OFF_HAND, gem);
					if (!player.getAbilities().instabuild) {
						(player.getItemInHand(hand)).shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.FAIL;
	}

	class KoboldTradeGoal extends Goal {
		public final AbstractKoboldEntity kobold;

		public KoboldTradeGoal(AbstractKoboldEntity kobold) {
			this.kobold = kobold;
		}

		@Override
		public boolean canUse() {
			return (checkHand() && !(this.kobold.hasEffect(MobEffects.MOVEMENT_SPEED)));
		}

		@Override
		public void start() {
			this.kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, -10, (false), (false)));
			ItemStack off = this.kobold.getOffhandItem();
			LevelAccessor world = this.kobold.level();
			double x = this.kobold.getX();
			double y = this.kobold.getY();
			double z = this.kobold.getZ();
			if (off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_one")))) {
				KoboldsMod.queueServerWork(100, () -> {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_one_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
						Player target = lvl.getNearestPlayer(this.kobold, 7);
						if (target != null) {
							pos = target.position();
						}
						for (ItemStack stack : list) {
							BehaviorUtils.throwItem(this.kobold, stack, pos);
						}
					}
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				});
			} else if (off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_two")))) {
				KoboldsMod.queueServerWork(100, () -> {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_two_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
						Player target = lvl.getNearestPlayer(this.kobold, 7);
						if (target != null) {
							pos = target.position();
						}
						for (ItemStack stack : list) {
							BehaviorUtils.throwItem(this.kobold, stack, pos);
						}
					}
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				});
			} else if (off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_three")))) {
				KoboldsMod.queueServerWork(100, () -> {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/captain_three_loot");
						Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 1);
						Player target = lvl.getNearestPlayer(this.kobold, 7);
						if (target != null) {
							pos = target.position();
						}
						for (ItemStack stack : list) {
							BehaviorUtils.throwItem(this.kobold, stack, pos);
						}
					}
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				});
			}
		}

		protected boolean checkHand() {
			ItemStack off = this.kobold.getOffhandItem();
			return (off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_one"))) || off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_two")))
					|| off.is(ItemTags.create(new ResourceLocation("kobolds:captain_tier_three"))));
		}
	}
}