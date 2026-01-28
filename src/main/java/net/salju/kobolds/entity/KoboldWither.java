package net.salju.kobolds.entity;

import net.salju.kobolds.entity.ai.*;
import net.salju.kobolds.events.KoboldsManager;
import net.salju.kobolds.init.KoboldsTags;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import javax.annotation.Nullable;

public class KoboldWither extends AbstractKoboldSkeleton {
	public KoboldWither(EntityType<KoboldWither> type, Level world) {
		super(type, world);
        this.setCanPickUpLoot(true);
		this.setPersistenceRequired();
		this.setPathfindingMalus(PathType.LAVA, 8.0F);
		this.getNavigation().setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new OpenDoorGoal(this, false));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(1, new RangedCrossbowAttackGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new WitherboldAttackSelector(this)));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.isAlive()) {
			if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
				this.playSound(this.getAmbientSound(), 1.0F, 1.2F);
				if (!this.level().isClientSide()) {
					player.addEffect(new MobEffectInstance(MobEffects.WITHER, 200), this);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	@Override
	protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor lvl, RandomSource randy, DifficultyInstance difficulty) {
		this.getMainHandItem().enchant(KoboldsManager.getEnchantment(lvl.registryAccess(), "minecraft", "smite"), 5);
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
		if (drop.is(KoboldsTags.ARMOR)) {
			return hand.isEmpty() || this.canReplaceEqualItem(drop, hand);
		}
		return false;
	}

	@Override
	public boolean canReplaceEqualItem(ItemStack drop, ItemStack hand) {
		if (drop.isEnchanted()) {
			if (hand.isEnchanted()) {
				int id = 0;
				ItemEnchantments.Mutable dropMap = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(drop));
				for (Holder<Enchantment> e : dropMap.keySet()) {
					if (e.is(KoboldsTags.ENCHS)) {
						id = (id + (5 * dropMap.getLevel(e)));
					} else {
						id = (id + dropMap.getLevel(e));
					}
				}
				int ih = 0;
				ItemEnchantments.Mutable handMap = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(hand));
				for (Holder<Enchantment> e : handMap.keySet()) {
					if (e.is(KoboldsTags.ENCHS)) {
						ih = (ih + (5 * handMap.getLevel(e)));
					} else {
						ih = (ih + handMap.getLevel(e));
					}
				}
				if (id > ih) {
					return true;
				}
			}
			return !hand.isEnchanted();
		}
		return super.canReplaceEqualItem(drop, hand);
	}

	@Override
	public boolean doHurtTarget(ServerLevel lvl, Entity entity) {
		if (entity instanceof LivingEntity target) {
			target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200), this);
		}
		return super.doHurtTarget(lvl, entity);
	}

	@Override
	public boolean canBeLeashed() {
		return true;
	}

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.WITHER_SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.WITHER_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.WITHER_SKELETON_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.WITHER_SKELETON_STEP;
	}

	@Override
	protected AbstractArrow getArrow(ItemStack stack, float f, @Nullable ItemStack weapon) {
		AbstractArrow ammo = super.getArrow(stack, f, weapon);
		ammo.igniteForSeconds(100.0F);
		return ammo;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.is(MobEffects.WITHER) ? false : super.canBeAffected(effect);
	}
}