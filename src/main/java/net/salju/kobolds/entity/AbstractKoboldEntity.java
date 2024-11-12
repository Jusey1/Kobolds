package net.salju.kobolds.entity;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsSounds;
import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.init.KoboldsTags;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

public abstract class AbstractKoboldEntity extends PathfinderMob implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_DIAMOND_EYES = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private ItemStack primary = new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get());
	private ItemStack trident = ItemStack.EMPTY;
	private UUID thrownTrident = null;
	private int breed;
	private int cooldown;
	private int potion;

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
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(3, new FloatGoal(this));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (!this.primary.isEmpty()) {
			tag.put("Primary", this.primary.save(this.level().registryAccess(), new CompoundTag()));
		}
		if (!this.trident.isEmpty()) {
			tag.put("Trident", this.trident.save(this.level().registryAccess(), new CompoundTag()));
		}
		if (this.thrownTrident != null) {
			tag.putUUID("TridentUUID", this.thrownTrident);
		}
		tag.putInt("Breed", this.breed);
		tag.putInt("CD", this.cooldown);
		tag.putInt("PCD", this.potion);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (ItemStack.parse(this.level().registryAccess(), tag.getCompound("Primary")).isPresent()) {
			this.primary = ItemStack.parse(this.level().registryAccess(), tag.getCompound("Primary")).get();
		}
		if (ItemStack.parse(this.level().registryAccess(), tag.getCompound("Trident")).isPresent()) {
			this.trident = ItemStack.parse(this.level().registryAccess(), tag.getCompound("Trident")).get();
		}
		if (tag.contains("TridentUUID")) {
			this.thrownTrident = tag.getUUID("TridentUUID");
		}
		this.breed = tag.getInt("Breed");
		this.cooldown = tag.getInt("CD");
		this.potion = tag.getInt("PCD");
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHARGING_STATE, false);
		builder.define(DATA_DIAMOND_EYES, false);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 2.0F);
		} else if (this.getMainHandItem().getItem() instanceof BowItem bow) {
			AbstractArrow arrow = ProjectileUtil.getMobArrow(this, this.getProjectile(this.getMainHandItem()), f, this.getMainHandItem());
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

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	public void setChargingCrossbow(boolean check) {
		this.entityData.set(DATA_CHARGING_STATE, check);
	}

	@Override
	public ItemStack equipItemIfPossible(ServerLevel lvl, ItemStack stack) {
		EquipmentSlot slot = this.getEquipmentSlotForItem(stack);
		ItemStack current = this.getItemBySlot(slot);
		boolean flag = this.canReplaceCurrentItem(stack, current, slot);
		if (stack.getItem() == Items.EMERALD || stack.getItem() instanceof TridentItem) {
			slot = EquipmentSlot.OFFHAND;
			current = this.getItemBySlot(slot);
			flag = this.canReplaceCurrentItem(stack, current, slot);
		}
		if (flag && this.canHoldItem(stack)) {
			double d0 = (double) this.getEquipmentDropChance(slot);
			if (!current.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
				this.spawnAtLocation(lvl, current);
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
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
		if (drop.getItem() instanceof CrossbowItem || drop.getItem() instanceof BowItem) {
			if (hand.getItem() instanceof CrossbowItem || hand.getItem() instanceof BowItem) {
				return this.canReplaceEqualItem(drop, hand);
			} else if (!(hand.getItem() instanceof DiggerItem)) {
				return (hand.isEmpty() ? this.trident.isEmpty() : true);
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
		} else if (drop.getItem() == Items.EMERALD && hand.isEmpty() && this.getType().is(KoboldsTags.TRADERS)) {
			return true;
		} else if (slot == EquipmentSlot.MAINHAND && !this.trident.isEmpty() && this.getMainHandItem().isEmpty()) {
			return false;
		}
		return super.canReplaceCurrentItem(drop, hand, slot);
	}

	public void aiStep() {
		super.aiStep();
		this.updateSwingTime();
	}

	public void setCD(int i) {
		this.cooldown = i;
	}

	public void setBreed(int i) {
		this.breed = i;
	}

	public void setPotionCD(int i) {
		this.potion = i;
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

	public int getPotionCD() {
		return this.potion;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return (this.isBaby() ? KoboldsSounds.KOBOLD_YIP.get() : KoboldsSounds.KOBOLD_IDLE.get());
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return (this.isBlocking() ? SoundEvents.SHIELD_BLOCK : KoboldsSounds.KOBOLD_HURT.get());
	}

	@Override
	public SoundEvent getDeathSound() {
		return KoboldsSounds.KOBOLD_DEATH.get();
	}

	@Override
	public void die(DamageSource source) {
		if (source.getDirectEntity() instanceof Zombie && !this.isBaby()) {
			this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
			if (this.level() instanceof ServerLevel) {
				InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this, stack -> stack == Items.POTION);
				if (this.getItemInHand(hand).is(Items.POTION)) {
					if (this.getOffhandItem().getItem() instanceof ShieldItem) {
						this.setItemInHand(hand, this.getPrimaryWeapon());
					} else {
						this.setItemInHand(hand, ItemStack.EMPTY);
					}
				}
				KoboldZombie zombo = this.convertTo(KoboldsMobs.KOBOLD_ZOMBIE.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
				zombo.setZombo(this);
			}
		}
		super.die(source);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack gem = player.getItemInHand(hand).copy();
		if (this.isAlive()) {
			if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
				this.playSound(KoboldsSounds.KOBOLD_PURR.get(), 1.0F, (this.isBaby() ? 1.2F : 1.0F));
				return InteractionResult.SUCCESS;
			} else if (!this.isBaby()) {
				if (gem.is(KoboldsItems.KOBOLD_SPAWN_EGG.get())) {
					if (this.level() instanceof ServerLevel lvl) {
						KoboldsMobs.KOBOLD_CHILD.get().spawn(lvl, BlockPos.containing(this.getX(), this.getY(), this.getZ()), EntitySpawnReason.BREEDING);
					}
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
					return InteractionResult.SUCCESS;
				} else if (gem.is(Items.AMETHYST_SHARD) && this.breed <= 0) {
					if (this.isEffectiveAi()) {
						this.setBreed(20000);
						if (!player.isCreative()) {
							player.getItemInHand(hand).shrink(1);
						}
					}
					return InteractionResult.SUCCESS;
				} else if (this.getOffhandItem().isEmpty()) {
					if (gem.getItem() instanceof AxeItem && hand != InteractionHand.MAIN_HAND && this instanceof Kobold) {
						if (this.isEffectiveAi()) {
							this.setItemInHand(InteractionHand.OFF_HAND, gem);
							if (!player.isCreative()) {
								player.getItemInHand(hand).shrink(1);
							}
						}
						return InteractionResult.SUCCESS;
					} else if (gem.is(Items.EMERALD) && this.getType().is(KoboldsTags.TRADERS)) {
						if (this.isEffectiveAi()) {
							gem.setCount(1);
							this.setItemInHand(InteractionHand.OFF_HAND, gem);
							if (!player.isCreative()) {
								player.getItemInHand(hand).shrink(1);
							}
						}
						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(world, world.getRandom(), difficulty);
		return super.finalizeSpawn(world, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		if (this.getType() == KoboldsMobs.KOBOLD_WARRIOR.get()) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_AXE.get()));
		} else if (this.getType() == KoboldsMobs.KOBOLD_RASCAL.get() || this.getType() == KoboldsMobs.KOBOLD_CAPTAIN.get()) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
		} else if (this.getType() == KoboldsMobs.KOBOLD_ENGINEER.get()) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
		} else if (this.getType() == KoboldsMobs.KOBOLD.get() || this.getType() == KoboldsMobs.KOBOLD_PIRATE.get()) {
			if (Math.random() >= 0.6) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
			} else {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
			}
		}
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		if (this.getType() == KoboldsMobs.KOBOLD_WARRIOR.get()) {
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		} else if (this.getType() == KoboldsMobs.KOBOLD_PIRATE.get()) {
			if (Math.random() >= 0.75) {
				this.primary = this.getMainHandItem();
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
			ItemStack extra = ProjectileWeaponItem.getHeldProjectile(this, weapon.getSupportedHeldProjectiles(stack));
			return extra.isEmpty() ? new ItemStack(Items.ARROW) : extra;
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
			if (this.potion > 0) {
				--this.potion;
			} else if (this.getHealth() < 12) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), 600);
			}
			if (this.breed > 0) {
				if (this.level() instanceof ServerLevel lvl) {
					if (this.breed > 18000) {
						AbstractKoboldEntity target = lvl.getNearestEntity(AbstractKoboldEntity.class, TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight(), this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(8.0D));
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
									KoboldsMobs.KOBOLD_CHILD.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
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
	public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
		if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi() && this.getPotionCD() <= 0) {
			if ((source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA)) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE), 120);
			} else if (source.is(DamageTypes.DROWN) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING), 120);
			}
		}
		if (this.isBlocking() && source.getEntity() instanceof LivingEntity target && target.canDisableShield()) {
			this.setCD(100);
		}
		return (!(source.getEntity() instanceof AbstractKoboldEntity) && !source.is(DamageTypes.CAMPFIRE) && super.hurtServer(lvl, source, amount));
	}

	public void givePotion(ItemStack stack, int i) {
		if (this.getOffhandItem().getItem() instanceof TridentItem) {
			this.setItemInHand(InteractionHand.MAIN_HAND, stack);
			this.setPotionCD(i);
		} else if (this.getOffhandItem().isEmpty()) {
			this.setItemInHand(InteractionHand.OFF_HAND, stack);
			this.setPotionCD(i);
		}
	}

	public void checkTrident() {
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

	public ItemStack getPrimaryWeapon() {
		return this.primary;
	}

	public static List<ItemStack> getTradeItems(AbstractKoboldEntity kobold, String table) {
		return Objects.requireNonNull(kobold.level().getServer()).reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, table))).getRandomItems((new LootParams.Builder((ServerLevel) kobold.level())).withParameter(LootContextParams.THIS_ENTITY, kobold).create(LootContextParamSets.EMPTY));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 1);
		builder = builder.add(Attributes.MAX_HEALTH, 18);
		builder = builder.add(Attributes.ARMOR, 2);
		return builder;
	}
}