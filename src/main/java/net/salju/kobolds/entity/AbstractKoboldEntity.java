package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.ai.KoboldTridentGoal;
import net.salju.kobolds.entity.ai.KoboldShieldGoal;
import net.salju.kobolds.entity.ai.KoboldRevengeGoal;
import net.salju.kobolds.entity.ai.KoboldMeleeGoal;
import net.salju.kobolds.entity.ai.KoboldHealGoal;
import net.salju.kobolds.entity.ai.KoboldCrossbowGoal;
import net.salju.kobolds.entity.ai.KoboldBowGoal;
import net.salju.kobolds.entity.ai.KoboldAttackSelector;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.List;

public abstract class AbstractKoboldEntity extends PathfinderMob implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_DIAMOND_EYES = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private ItemStack primary = new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get());
	private ItemStack trident = ItemStack.EMPTY;
	private UUID thrownTrident = null;
	private boolean partyKobold;
	private int breed;
	private int cooldown;
	@Nullable
	private BlockPos jukebox;

	protected AbstractKoboldEntity(EntityType<? extends PathfinderMob> type, Level world) {
		super(type, world);
		this.setCanPickUpLoot(true);
		this.setPersistenceRequired();
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
		this.goalSelector.addGoal(1, new KoboldHealGoal(this));
		this.goalSelector.addGoal(1, new KoboldTridentGoal(this, 1.0D, 40, 12.0F));
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new KoboldAttackSelector(this)));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(3, new FloatGoal(this));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Primary", this.primary.save(new CompoundTag()));
		tag.put("Trident", this.trident.save(new CompoundTag()));
		if (this.thrownTrident != null) {
			tag.putUUID("TridentUUID", this.thrownTrident);
		}
		tag.putInt("Breed", this.breed);
		tag.putInt("CD", this.cooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Primary")) {
			ItemStack stack = ItemStack.of(tag.getCompound("Primary"));
			this.primary = stack;
		}
		if (tag.contains("Trident")) {
			ItemStack stack = ItemStack.of(tag.getCompound("Trident"));
			this.trident = stack;
		}
		if (tag.contains("TridentUUID")) {
			UUID saved = tag.getUUID("TridentUUID");
			this.thrownTrident = saved;
		}
		if (tag.contains("Breed")) {
			int saved = tag.getInt("Breed");
			this.breed = saved;
		}
		if (tag.contains("CD")) {
			int saved = tag.getInt("CD");
			this.cooldown = saved;
		}
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.225D;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !(this.isPersistenceRequired());
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 2.0F);
		} else if (this.getMainHandItem().getItem() instanceof BowItem bow) {
			AbstractArrow arrow = ProjectileUtil.getMobArrow(this, this.getMainHandItem(), f);
			arrow = bow.customArrow(arrow);
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - arrow.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			arrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(arrow);
		} else if (this.getOffhandItem().getItem() instanceof TridentItem) {
			this.trident = this.getOffhandItem();
			ThrownTrident proj = new ThrownTrident(this.level(), this, this.trident);
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - proj.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
			proj.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(proj);
			this.setItemInHand(InteractionHand.MAIN_HAND, this.primary);
			this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
			this.thrownTrident = proj.getUUID();
			this.setCD(1200);
		}
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void shootCrossbowProjectile(LivingEntity arg0, ItemStack arg1, Projectile arg2, float arg3) {
		this.shootCrossbowProjectile(this, arg0, arg2, arg3, 1.6F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CHARGING_STATE, false);
		this.entityData.define(DATA_DIAMOND_EYES, false);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	public void setChargingCrossbow(boolean charging) {
		this.entityData.set(DATA_CHARGING_STATE, charging);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public ItemStack equipItemIfPossible(ItemStack stack) {
		EquipmentSlot slot = getEquipmentSlotForItem(stack);
		ItemStack current = this.getItemBySlot(slot);
		boolean flag = this.canReplaceCurrentItem(stack, current);
		if (stack.getItem() == Items.EMERALD || stack.getItem() instanceof TridentItem) {
			slot = EquipmentSlot.OFFHAND;
			current = this.getItemBySlot(slot);
			flag = this.canReplaceCurrentItem(stack, current);
		}
		if (flag && this.canHoldItem(stack)) {
			double d0 = (double) this.getEquipmentDropChance(slot);
			if (!current.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
				this.spawnAtLocation(current);
			}
			if (stack.getItem() == Items.EMERALD && (stack.getCount() > 1)) {
				stack = stack.split(1);
			}
			if (slot.isArmor() && stack.getCount() > 1) {
				ItemStack copy = stack.copyWithCount(1);
				this.setItemSlotAndDropWhenKilled(slot, copy);
				return copy;
			} else {
				this.setItemSlotAndDropWhenKilled(slot, stack);
				return stack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof SwordItem && !(this instanceof KoboldWarrior)) {
			if (hand.getItem() instanceof SwordItem) {
				SwordItem newbie = (SwordItem) drop.getItem();
				SwordItem weapon = (SwordItem) hand.getItem();
				if (newbie.getDamage() != weapon.getDamage()) {
					return newbie.getDamage() > weapon.getDamage();
				} else {
					return this.canReplaceEqualItem(drop, hand);
				}
			} else if (!(hand.getItem() instanceof BowItem || hand.getItem() instanceof CrossbowItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
			}
		} else if (drop.getItem() instanceof AxeItem && this instanceof KoboldWarrior) {
			if (hand.getItem() instanceof AxeItem) {
				AxeItem newbie = (AxeItem) drop.getItem();
				AxeItem weapon = (AxeItem) hand.getItem();
				if (newbie.getAttackDamage() != weapon.getAttackDamage()) {
					return newbie.getAttackDamage() > weapon.getAttackDamage();
				} else {
					return this.canReplaceEqualItem(drop, hand);
				}
			} else if (!(hand.getItem() instanceof BowItem || hand.getItem() instanceof CrossbowItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
			}
		} else if (drop.getItem() instanceof TieredItem) {
			if (!(hand.getItem() instanceof BowItem || hand.getItem() instanceof CrossbowItem || hand.getItem() instanceof TieredItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
			}
		} else if (drop.getItem() instanceof CrossbowItem) {
			if (hand.getItem() instanceof CrossbowItem) {
				return this.canReplaceEqualItem(drop, hand);
			} else if (hand.getItem() instanceof BowItem && !hand.isEnchanted() && drop.isEnchanted()) {
				return true;
			} else if (!(hand.getItem() instanceof BowItem || hand.getItem() instanceof TieredItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
			}
		} else if (drop.getItem() instanceof BowItem) {
			if (hand.getItem() instanceof BowItem) {
				return this.canReplaceEqualItem(drop, hand);
			} else if (hand.getItem() instanceof CrossbowItem && !hand.isEnchanted() && drop.isEnchanted()) {
				return true;
			} else if (!(hand.getItem() instanceof CrossbowItem || hand.getItem() instanceof TieredItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
			}
		} else if (drop.getItem() instanceof ArmorItem) {
			if (EnchantmentHelper.hasBindingCurse(hand)) {
				return false;
			} else if (hand.isEmpty() || hand.getItem() instanceof BlockItem) {
				return true;
			} else if (hand.getItem() instanceof ArmorItem) {
				ArmorItem newbie = (ArmorItem) drop.getItem();
				ArmorItem worn = (ArmorItem) hand.getItem();
				if (newbie.getDefense() != worn.getDefense()) {
					return newbie.getDefense() > worn.getDefense();
				} else if (newbie.getToughness() != worn.getToughness()) {
					return newbie.getToughness() > worn.getToughness();
				} else {
					return this.canReplaceEqualItem(drop, hand);
				}
			}
		} else if (drop.getItem() instanceof ShieldItem) {
			if (hand.getItem() instanceof ShieldItem) {
				return this.canReplaceEqualItem(drop, hand);
			} else if (hand.isEmpty() && this.trident.isEmpty()) {
				return true;
			}
		} else if (drop.getItem() instanceof TridentItem) {
			if (hand.getItem() instanceof TridentItem && this.canReplaceEqualItem(drop, hand)) {
				this.trident = drop;
				return true;
			} else if (hand.isEmpty() && this.trident.isEmpty()) {
				this.primary = this.getMainHandItem();
				this.trident = drop;
				this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				return true;
			}
		} else if (drop.getItem() == Items.EMERALD && hand.isEmpty() && !(this instanceof KoboldCaptain || this instanceof KoboldWarrior)) {
			return true;
		}
		return false;
	}

	public void aiStep() {
		this.updateSwingTime();
		this.updateNoActionTime();
		if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 32.76D) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
			this.partyKobold = false;
			this.jukebox = null;
		}
		super.aiStep();
	}

	public void updateNoActionTime() {
		float f = this.getLightLevelDependentMagicValue();
		if (f > 0.5F) {
			this.noActionTime += 2;
		}
	}

	public void setRecordPlayingNearby(BlockPos pos, boolean boop) {
		this.jukebox = pos;
		this.partyKobold = boop;
	}

	public void setCD(int i) {
		this.cooldown = i;
	}

	public void setBreed(int i) {
		this.breed = i;
	}

	public boolean isPartyKobold() {
		return this.partyKobold;
	}

	public boolean isDiamond() {
		return this.getEntityData().get(DATA_DIAMOND_EYES);
	}

	public int getCD() {
		return this.cooldown;
	}

	public int getBreed() {
		return this.breed;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return KoboldsModSounds.KOBOLD_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return (this.isBlocking() ? SoundEvents.SHIELD_BLOCK : KoboldsModSounds.KOBOLD_HURT.get());
	}

	@Override
	public SoundEvent getDeathSound() {
		return KoboldsModSounds.KOBOLD_DEATH.get();
	}

	@Override
	public void die(DamageSource source) {
		if (source.getDirectEntity() instanceof Zombie) {
			if (!this.isBaby() && this.level().getDifficulty() == Difficulty.HARD || this.level().getDifficulty() == Difficulty.NORMAL) {
				this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
				if (this.level() instanceof ServerLevel lvl) {
					KoboldZombie zombo = this.convertTo(KoboldsMobs.KOBOLD_ZOMBIE.get(), true);
					ForgeEventFactory.onLivingConvert(this, zombo);
				}
			}
		}
		super.die(source);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack gem = player.getItemInHand(hand).copy();
		if (this.isAlive()) {
			if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
				this.playSound(KoboldsModSounds.KOBOLD_PURR.get(), 1.0F, (this.isBaby() ? 1.2F : 1.0F));
				return InteractionResult.SUCCESS;
			} else if (gem.getItem() == KoboldsItems.KOBOLD_SPAWN_EGG.get() && !(this instanceof KoboldChild)) {
				if (this.level() instanceof ServerLevel lvl) {
					BlockPos spawn = BlockPos.containing(this.getX(), this.getY(), this.getZ());
					KoboldChild baby = KoboldsMobs.KOBOLD_CHILD.get().spawn(lvl, spawn, MobSpawnType.BREEDING);
				}
				if (!player.isCreative()) {
					(player.getItemInHand(hand)).shrink(1);
				}
				return InteractionResult.SUCCESS;
			} else if (this.isEffectiveAi() && !this.level().isClientSide()) {
				if (gem.is(Items.AMETHYST_SHARD)) {
					AbstractKoboldEntity target = this.level().getNearestEntity(AbstractKoboldEntity.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(64.0D));
					if (!this.isBaby() && target != null && !target.is(this) && this.breed <= 0) {
						this.setBreed(20000);
						if (!player.isCreative()) {
							(player.getItemInHand(hand)).shrink(1);
						}
						return InteractionResult.SUCCESS;
					}
				} else if (this.getOffhandItem().isEmpty()) {
					if (gem.getItem() instanceof AxeItem && hand != InteractionHand.MAIN_HAND) {
						if (this instanceof Kobold) {
							this.setItemInHand(InteractionHand.OFF_HAND, gem);
							if (!player.isCreative()) {
								(player.getItemInHand(hand)).shrink(1);
							}
							return InteractionResult.SUCCESS;
						}
					} else if (gem.is(Items.EMERALD)) {
						if (this instanceof Kobold || this instanceof KoboldPirate || this instanceof KoboldEnchanter || this instanceof KoboldEngineer) {
							gem.setCount(1);
							this.setItemInHand(InteractionHand.OFF_HAND, gem);
							if (!player.isCreative()) {
								(player.getItemInHand(hand)).shrink(1);
							}
							return InteractionResult.SUCCESS;
						}
					}
				}
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		if (this instanceof KoboldRascal rascal) {
			rascal.setDespawnDelay(24000);
			rascal.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, rascal.getDespawnDelay(), 0));
		}
		return super.finalizeSpawn(world, difficulty, reason, data, tag);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		ItemStack sword = (EnchantmentHelper.enchantItem(randy, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()), 21, false));
		ItemStack crossbow = (EnchantmentHelper.enchantItem(randy, new ItemStack(Items.CROSSBOW), 21, false));
		if (this instanceof KoboldWarrior) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_AXE.get()));
		} else if (this instanceof KoboldRascal || this instanceof KoboldCaptain) {
			this.setItemSlot(EquipmentSlot.MAINHAND, sword);
		} else if (this instanceof KoboldEngineer) {
			this.setItemSlot(EquipmentSlot.MAINHAND, crossbow);
		} else if (this instanceof Kobold || this instanceof KoboldPirate) {
			if (Math.random() >= 0.6) {
				if (Math.random() >= 0.15) {
					this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
				} else {
					this.setItemSlot(EquipmentSlot.MAINHAND, crossbow);
				}
			} else {
				if (Math.random() >= 0.15) {
					this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
				} else {
					this.setItemSlot(EquipmentSlot.MAINHAND, sword);
				}
			}
		}
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		if (this instanceof KoboldWarrior) {
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		} else if (this instanceof KoboldPirate) {
			if (Math.random() >= 0.75) {
				this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TRIDENT));
				this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
				this.trident = this.getOffhandItem();
			}
		}
		this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
	}

	@Override
	public ItemStack getProjectile(ItemStack stack) {
		if (stack.getItem() instanceof ProjectileWeaponItem weapon) {
			ItemStack extra = ProjectileWeaponItem.getHeldProjectile(this, weapon.getSupportedHeldProjectiles());
			return net.minecraftforge.common.ForgeHooks.getProjectile(this, stack, extra.isEmpty() ? new ItemStack(Items.ARROW) : extra);
		} else {
			return super.getProjectile(stack);
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi()) {
			this.checkTrident();
			if (this.cooldown > 0) {
				--this.cooldown;
			}
			if (this.breed > 0) {
				if (this.level() instanceof ServerLevel lvl) {
					if (this.breed > 18000) {
						AbstractKoboldEntity target = this.level().getNearestEntity(AbstractKoboldEntity.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(64.0D));
						if (Mth.nextInt(this.random, 1, 10) > 8) {
							double d = this.random.nextGaussian() * 0.02D;
							lvl.sendParticles(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 3, d, d, d, 0);
						}
						if (target != null && !target.is(this)) {
							if (target.getBreed() > 18000) {
								if (this.distanceTo(target) >= 1.0D) {
									this.getNavigation().moveTo(target, 1.0F);
								} else if (this.distanceTo(target) < 1.0D) {
									BlockPos pos = BlockPos.containing(this.getX(), this.getY(), this.getZ());
									KoboldChild baby = KoboldsMobs.KOBOLD_CHILD.get().spawn(lvl, pos, MobSpawnType.BREEDING);
									target.setBreed(18000);
									this.setBreed(18000);
								}
							}
						}
					}
				}
				if (!this.isDiamond()) {
					this.getEntityData().set(DATA_DIAMOND_EYES, true);
				}
				--this.breed;
				if (this.breed <= 0 && this.isDiamond()) {
					this.getEntityData().set(DATA_DIAMOND_EYES, false);
				}
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity target && target.canDisableShield() && this.isBlocking()) {
			this.setCD(100);
		}
		return ((source.getEntity() instanceof AbstractKoboldEntity || source.is(DamageTypes.IN_FIRE)) ? false : super.hurt(source, amount));
	}

	protected void checkTrident() {
		if (this.thrownTrident != null && this.level() instanceof ServerLevel lvl && this.getOffhandItem().isEmpty() && this.cooldown <= 1180) {
			if (lvl.getEntity(this.thrownTrident) instanceof ThrownTrident proj && proj.getOwner().is(this)) {
				if (this.distanceTo(proj) < 2.0D || this.cooldown == 1) {
					this.giveTrident(proj);
				} else if (this.distanceTo(proj) < 32.0D && this.getTarget() == null) {
					this.getNavigation().moveTo(proj, 1.0F);
				}
			} else if (lvl.getEntity(this.thrownTrident) == null && this.cooldown <= 1) {
				this.giveTrident(null);
			}
		}
	}

	protected void giveTrident(@Nullable ThrownTrident proj) {
		this.primary = this.getMainHandItem();
		this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.setItemInHand(InteractionHand.OFF_HAND, this.trident);
		this.thrownTrident = null;
		if (this.cooldown > 1 && proj != null) {
			this.setCD(0);
			proj.discard();
		}
	}

	public static List<ItemStack> getTradeItems(AbstractKoboldEntity kobold, String table) {
		return kobold.level().getServer().getLootData().getLootTable(new ResourceLocation(table))
				.getRandomItems((new LootParams.Builder((ServerLevel) kobold.level())).withParameter(LootContextParams.THIS_ENTITY, kobold).create(LootContextParamSets.EMPTY));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
		builder = builder.add(Attributes.MAX_HEALTH, 18);
		builder = builder.add(Attributes.ARMOR, 2);
		return builder;
	}
}