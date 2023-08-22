package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.KoboldsMod;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class KoboldPirate extends AbstractKoboldEntity {
	public KoboldPirate(PlayMessages.SpawnEntity packet, Level world) {
		this(KoboldsMobs.KOBOLD_PIRATE.get(), world);
	}

	public KoboldPirate(EntityType<KoboldPirate> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldPirate.KoboldTradeGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
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
			KoboldsMod.queueServerWork(100, () -> {
				this.kobold.swing(InteractionHand.MAIN_HAND, true);
				this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
				LevelAccessor world = this.kobold.level();
				double x = this.kobold.getX();
				double y = this.kobold.getY();
				double z = this.kobold.getZ();
				if (world instanceof ServerLevel lvl) {
					List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/trader_loot");
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

		protected boolean checkHand() {
			return (this.kobold.getOffhandItem().getItem() == (Items.EMERALD));
		}
	}
}