package net.salju.kobolds.entity;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.*;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractKoboldEntity extends AgeableMob implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_DIAMOND_EYES = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private Optional<EntityReference<Entity>> thrownTrident = Optional.empty();
	private ItemStack primary = new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD);
	private ItemStack secondary = ItemStack.EMPTY;
	private int cooldown;

	protected AbstractKoboldEntity(EntityType<? extends AgeableMob> type, Level world) {
		super(type, world);
		this.setCanPickUpLoot(true);
		this.setPersistenceRequired();
		this.getNavigation().setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(3, new FloatGoal(this));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		if (!this.getPrimary().isEmpty()) {
			tag.store("Primary", ItemStack.CODEC, this.getPrimary());
		}
		if (!this.getSecondary().isEmpty()) {
			tag.store("Trident", ItemStack.CODEC, this.getSecondary());
		}
		if (this.getTridentReference() != null) {
			EntityReference.store(this.getTridentReference(), tag, "ThrownTrident");
		}
		tag.putInt("CD", this.cooldown);
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		if (tag.read("Primary", ItemStack.CODEC).isPresent()) {
			this.primary = tag.read("Primary", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		}
		if (tag.read("Trident", ItemStack.CODEC).isPresent()) {
			this.secondary = tag.read("Trident", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		}
		EntityReference<Entity> target = EntityReference.read(tag, "ThrownTrident");
		if (target != null) {
			this.thrownTrident = Optional.of(target);
		} else {
			this.thrownTrident = Optional.empty();
		}
		this.cooldown = tag.getInt("CD").orElse(0);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHARGING_STATE, false);
		builder.define(DATA_DIAMOND_EYES, false);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this, stack -> (stack instanceof CrossbowItem || stack instanceof  BowItem || stack instanceof TridentItem));
		if (this.getItemInHand(hand).getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 2.0F);
		} else if (this.getItemInHand(hand).getItem() instanceof BowItem) {
			AbstractArrow arrow = ProjectileUtil.getMobArrow(this, this.getProjectile(this.getItemInHand(hand)), f, this.getItemInHand(hand));
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - arrow.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			arrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(arrow);
		} else if (this.getItemInHand(hand).getItem() instanceof TridentItem) {
			this.secondary = this.getItemInHand(hand);
			ThrownTrident proj = new ThrownTrident(this.level(), this, this.getSecondary());
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - proj.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
			proj.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(proj);
			this.setItemInHand(InteractionHand.MAIN_HAND, this.primary);
			this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
			this.setTrident(proj);
			this.setCD(1200);
		}
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void setChargingCrossbow(boolean check) {
		this.entityData.set(DATA_CHARGING_STATE, check);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	@Override
	public ItemStack equipItemIfPossible(ServerLevel lvl, ItemStack stack) {
		EquipmentSlot slot = this.getEquipmentSlotForItem(stack);
		ItemStack current = this.getItemBySlot(slot);
		boolean flag = this.canReplaceCurrentItem(stack, current, slot);
		if ((stack.is(Items.EMERALD) && this.getOffhandItem().isEmpty()) || stack.getItem() instanceof TridentItem) {
			slot = EquipmentSlot.OFFHAND;
			current = this.getItemBySlot(slot);
			flag = this.canReplaceCurrentItem(stack, current, slot);
		}
		if (flag && this.canHoldItem(stack)) {
			if (!current.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < this.getDropChances().byEquipment(slot)) {
				this.spawnAtLocation(lvl, current);
			}
			if (stack.is(Items.EMERALD) && stack.getCount() > 1) {
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
		if (EnchantmentHelper.hasTag(drop, EnchantmentTags.CURSE)) {
			return false;
		} else if (drop.getItem() instanceof TridentItem) {
			if (hand.getItem() instanceof TridentItem && this.canReplaceEqualItem(drop, hand)) {
				this.secondary = drop;
				return true;
			} else if (!this.getSecondary().isEmpty()) {
				return false;
			} else if (this.getOffhandItem().isEmpty()) {
				this.primary = this.getMainHandItem();
				this.secondary = drop;
				this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				return true;
			} else {
				return false;
			}
		} else if (this.isPreferredWeapon(drop)) {
			if (hand.getItem() instanceof TridentItem) {
				return false;
			} else if (this.isPreferredWeapon(hand)) {
				return this.canReplaceEqualItem(drop, hand);
			} else {
				return true;
			}
		} else if (this.isPreferredWeapon(hand)) {
			return false;
		} else if (drop.getItem() instanceof ShieldItem) {
			if (hand.getItem() instanceof ShieldItem) {
				return this.canReplaceEqualItem(drop, hand);
			} else if (hand.isEmpty()) {
				return true;
			}
		} else if (drop.is(Items.EMERALD) && hand.isEmpty() && this.getType().is(KoboldsTags.TRADERS)) {
			return true;
		} else if (drop.is(KoboldsTags.ARMOR)) {
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
				} else if (this.isPreferredWeapon(drop) && id == ih) {
					return drop.getAttributeModifiers().compute(this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), EquipmentSlot.MAINHAND) > hand.getAttributeModifiers().compute(this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), EquipmentSlot.MAINHAND);
				}
			}
			return !hand.isEnchanted();
		} else if (this.isPreferredWeapon(drop) && !hand.isEnchanted()) {
			return drop.getAttributeModifiers().compute(this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), EquipmentSlot.MAINHAND) > hand.getAttributeModifiers().compute(this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), EquipmentSlot.MAINHAND);
		}
		return super.canReplaceEqualItem(drop, hand);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.updateSwingTime();
	}

	@Override
	public SoundEvent getAmbientSound() {
		return (this.isBaby() ? KoboldsSounds.KOBOLD_YIP.get() : KoboldsSounds.KOBOLD_IDLE.get());
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return (this.isBlocking() ? SoundEvents.SHIELD_BLOCK.value() : KoboldsSounds.KOBOLD_HURT.get());
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
				if (this.getOffhandItem().is(Items.POTION)) {
					this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
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
				} else if (gem.is(Items.AMETHYST_SHARD) && this.getAge() <= 0) {
					if (this.isEffectiveAi()) {
						if (this.getHealth() < this.getMaxHealth()) {
							this.setAge(1200);
							this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 0));
						} else {
							this.setAge(20000);
						}
						if (!player.isCreative()) {
							player.getItemInHand(hand).shrink(1);
						}
					}
					return InteractionResult.SUCCESS;
				} else if (this.getMainHandItem().isEmpty() || this.getOffhandItem().isEmpty()) {
					if (gem.getItem() instanceof AxeItem && hand != InteractionHand.MAIN_HAND && this instanceof Kobold) {
						if (this.isEffectiveAi()) {
							this.setItemInHand(this.getMainHandItem().isEmpty() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, gem);
							if (!player.isCreative()) {
								player.getItemInHand(hand).shrink(1);
							}
						}
						return InteractionResult.SUCCESS;
					} else if (gem.is(Items.EMERALD) && this.getType().is(KoboldsTags.TRADERS)) {
						if (this.isEffectiveAi()) {
							gem.setCount(1);
							this.setItemInHand(this.getMainHandItem().isEmpty() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, gem);
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
		SpawnGroupData spawn = super.finalizeSpawn(world, difficulty, reason, data);
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(world, world.getRandom(), difficulty);
		this.setBaby(this.isBaby());
		return spawn;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		this.primary = this.getMainHandItem();
		if (this.getType().equals(KoboldsMobs.KOBOLD_PIRATE.get()) && Math.random() >= 0.75) {
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TRIDENT));
		}
		this.secondary = this.getOffhandItem();
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
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
			if (this.getHealth() < 12 && !this.hasEffect(MobEffects.REGENERATION)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, KoboldsPotions.AMETHYST));
			}
			if (this.getAge() > 0) {
				if (this.level() instanceof ServerLevel lvl) {
					if (this.canBreed()) {
						AbstractKoboldEntity target = lvl.getNearestEntity(AbstractKoboldEntity.class, TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight(), this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(8.0D));
						if (Mth.nextInt(this.random, 1, 10) > 8) {
							double d = this.random.nextGaussian() * 0.02D;
							lvl.sendParticles(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 3, d, d, d, 0);
						}
						if (target != null && !target.is(this)) {
							if (target.canBreed()) {
								if (this.distanceTo(target) >= 1.0D) {
									this.getNavigation().moveTo(target, 1.0F);
								} else if (this.distanceTo(target) < 1.0D) {
									KoboldChild child = this.getBreedOffspring(lvl, target);
									if (child != null) {
										child.setBaby(true);
										child.move(MoverType.SELF, this.getOnPos().getBottomCenter());
										lvl.addFreshEntityWithPassengers(child);
									}
									target.setAge(18000);
									this.setAge(18000);
								}
							}
						}
					}
				}
				if (!this.isDiamond()) {
					this.getEntityData().set(DATA_DIAMOND_EYES, true);
				}
			} else if (this.isDiamond()) {
				this.getEntityData().set(DATA_DIAMOND_EYES, false);
			}
		}
	}

	@Override
	public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
		if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi()) {
			if ((source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA)) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE));
			} else if (source.is(DamageTypes.DROWN) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING));
			}
		}
		if (this.isBlocking() && source.getDirectEntity() instanceof LivingEntity atk) {
			this.setCD((int) atk.getSecondsToDisableBlocking() * 20);
		}
		return (!(source.getEntity() instanceof AbstractKoboldEntity) && !source.is(DamageTypes.CAMPFIRE) && super.hurtServer(lvl, source, amount));
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public boolean canBreed() {
		return this.getAge() > 18000;
	}

	@Override
	public KoboldChild getBreedOffspring(ServerLevel lvl, AgeableMob target) {
		return KoboldsMobs.KOBOLD_CHILD.get().create(lvl, EntitySpawnReason.BREEDING);
	}

	public ItemStack getPrimary() {
		return this.primary;
	}

	public ItemStack getSecondary() {
		return this.secondary;
	}

	public void updateItemData() {
		this.primary = this.getMainHandItem();
		this.secondary = this.getOffhandItem();
	}

	public void givePotion(ItemStack stack) {
		if (this.getOffhandItem().isEmpty()) {
			this.setItemInHand(InteractionHand.OFF_HAND, stack);
		} else if (this.getOffhandItem().getItem() instanceof ShieldItem) {
			this.secondary = this.getOffhandItem();
			this.setItemInHand(InteractionHand.OFF_HAND, stack);
		} else if (this.getOffhandItem().getItem() instanceof TridentItem && !this.isAggressive()) {
			this.setItemInHand(InteractionHand.MAIN_HAND, stack);
		}
	}

	public void checkTrident() {
		if (this.getTridentReference() != null && this.level() instanceof ServerLevel lvl && this.cooldown <= 1180 && this.getOffhandItem().isEmpty()) {
			if (lvl.getEntity(this.getTridentReference().getUUID()) instanceof ThrownTrident proj && proj.getOwner().is(this)) {
				if (this.distanceTo(proj) < 2.0D || this.cooldown <= 1) {
					this.giveTrident(proj);
				} else if (this.distanceTo(proj) < 32.0D && this.getTarget() == null) {
					this.getNavigation().moveTo(proj, 1.0F);
				}
			} else if (lvl.getEntity(this.getTridentReference().getUUID()) == null && this.cooldown <= 1) {
				this.giveTrident(null);
			}
		}
	}

	protected void giveTrident(@Nullable ThrownTrident proj) {
		this.primary = this.getMainHandItem();
		this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.setItemInHand(InteractionHand.OFF_HAND, this.getSecondary());
		this.setTrident(null);
		if (this.cooldown > 1 && proj != null) {
			this.setCD(0);
			proj.discard();
		}
	}

	public void setCD(int i) {
		this.cooldown = i;
	}

	public void setTrident(@Nullable ThrownTrident proj) {
		this.thrownTrident = Optional.ofNullable(proj).map(EntityReference::new);
	}

	public boolean isDiamond() {
		return this.getEntityData().get(DATA_DIAMOND_EYES);
	}

	public int getCD() {
		return this.cooldown;
	}

	public boolean isPreferredWeapon(ItemStack stack) {
		return stack.is(KoboldsTags.BASIC);
	}

	@Nullable
	public EntityReference<Entity> getTridentReference() {
		return this.thrownTrident.orElse(null);
	}

	public static List<ItemStack> getTradeItems(AbstractKoboldEntity kobold, String table) {
		return Objects.requireNonNull(kobold.level().getServer()).reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, table))).getRandomItems((new LootParams.Builder((ServerLevel) kobold.level())).withParameter(LootContextParams.THIS_ENTITY, kobold).create(LootContextParamSets.EMPTY));
	}
}