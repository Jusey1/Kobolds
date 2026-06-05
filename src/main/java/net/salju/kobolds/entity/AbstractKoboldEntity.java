package net.salju.kobolds.entity;

import net.salju.kobolds.init.*;
import net.salju.kobolds.events.*;
import net.salju.kobolds.entity.ai.KoboldBreedGoal;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractKoboldEntity extends AgeableMob implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_DIAMOND_EYES = SynchedEntityData.defineId(AbstractKoboldEntity.class, EntityDataSerializers.BOOLEAN);

	protected AbstractKoboldEntity(EntityType<? extends AgeableMob> type, Level world) {
		super(type, world);
		this.setCanPickUpLoot(true);
		this.setPersistenceRequired();
		this.getNavigation().setCanOpenDoors(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHARGING_STATE, false);
		builder.define(DATA_DIAMOND_EYES, false);
	}

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(4, new KoboldBreedGoal(this, 1.2));
    }

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		if (this.isHolding((stack) -> stack.getItem() instanceof CrossbowItem)) {
			this.performCrossbowAttack(this, 2.0F);
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
    public boolean canPickUpLoot() {
        return this.isBaby() ? false : super.canPickUpLoot();
    }

    @Override
    public ItemStack equipItemIfPossible(ServerLevel lvl, ItemStack stack) {
        EquipmentSlot slot = this.getEquipmentSlotForItem(stack);
        ItemStack current = this.getItemBySlot(slot);
        boolean flag = this.canReplaceCurrentItem(stack, current, slot);
        if (this.canTrade(stack)) {
            slot = EquipmentSlot.OFFHAND;
            current = this.getItemBySlot(slot);
            flag = this.canReplaceCurrentItem(stack, current, slot);
        }
        if (flag && this.canHoldItem(stack)) {
            if (!current.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < this.getDropChances().byEquipment(slot)) {
                this.spawnAtLocation(lvl, current);
            }
            if (this.canTrade(stack) && stack.getCount() > 1) {
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
        } else if (this.isSpear(drop)) {
            if (this.isSpear(hand)) {
                return this.canReplaceEqualItem(drop, hand);
            }
            return true;
        } else if (this.isPreferredWeapon(drop)) {
            if (this.isSpear(hand)) {
                return false;
            } else if (this.isPreferredWeapon(hand)) {
                return this.canReplaceEqualItem(drop, hand);
            }
            return true;
        } else if (this.canBlock(drop)) {
            if (this.canBlock(hand)) {
                return this.canReplaceEqualItem(drop, hand);
            }
            return hand.isEmpty() && !(this.getMainHandItem().getItem() instanceof ProjectileWeaponItem);
        } else if (this.canTrade(drop)) {
            return hand.isEmpty();
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
					Holder<Attribute> a = Attributes.ATTACK_DAMAGE;
					double d = this.getAttributes().hasAttribute(a) ? this.getAttributeBaseValue(a) : 0.0;
					return drop.getAttributeModifiers().compute(a, d, EquipmentSlot.MAINHAND) > hand.getAttributeModifiers().compute(a, d, EquipmentSlot.MAINHAND);
				}
			}
			return !hand.isEnchanted();
		} else if (this.isPreferredWeapon(drop) && !hand.isEnchanted()) {
			Holder<Attribute> a = Attributes.ATTACK_DAMAGE;
			double d = this.getAttributes().hasAttribute(a) ? this.getAttributeBaseValue(a) : 0.0;
			return drop.getAttributeModifiers().compute(a, d, EquipmentSlot.MAINHAND) > hand.getAttributeModifiers().compute(a, d, EquipmentSlot.MAINHAND);
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
	public SoundEvent getHurtSound(DamageSource source) {
		return (this.isBlocking() ? SoundEvents.SHIELD_BLOCK.value() : KoboldsSounds.KOBOLD_HURT.get());
	}

	@Override
	public SoundEvent getDeathSound() {
		return KoboldsSounds.KOBOLD_DEATH.get();
	}

	@Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack gem = player.getItemInHand(hand).copy();
        if (this.isAlive()) {
            if (player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
                this.playSound(KoboldsSounds.KOBOLD_PURR.get(), 1.0F, (this.isBaby() ? 1.2F : 1.0F));
                return InteractionResult.SUCCESS;
            } else if (this.isBreedItem(gem)) {
                int i = this.getAge();
                if (this.isEffectiveAi() && i <= 0) {
                    if (this.level() instanceof ServerLevel lvl) {
                        double d = this.getRandom().nextGaussian() * 0.02D;
                        lvl.sendParticles(ParticleTypes.WAX_OFF, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 3, d, d, d, 5);
                    }
                    if (this.isBaby()) {
                        this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                    } else {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.setAge(1200);
                            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 0));
                        } else {
                            this.setAge(20000);
                        }
                    }
                    if (!player.isCreative()) {
                        player.getItemInHand(hand).shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            } else if (this.getOffhandItem().isEmpty() && !this.isBaby()) {
                if (this.canTrade(gem)) {
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
        return super.mobInteract(player, hand);
    }

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        if (reason == EntitySpawnReason.BREEDING) {
            this.setBaby(true);
        }
        if (reason != EntitySpawnReason.CONVERSION) {
            this.setType(this.getSpawnType(reason));
            this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
        }
		return super.finalizeSpawn(world, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        if (!this.isBaby()) {
            this.giveNewWeapon(Math.random());
        }
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
        if (this.level() instanceof ServerLevel lvl && this.isAlive() && this.isEffectiveAi()) {
            if (this.getHealth() < 12 && !this.hasEffect(MobEffects.REGENERATION)) {
                this.givePotion(PotionContents.createItemStack(Items.POTION, KoboldsPotions.AMETHYST));
            }
            if (this.canBreed()) {
                if (Mth.nextInt(this.getRandom(), 1, 10) > 8) {
                    double d = this.getRandom().nextGaussian() * 0.02D;
                    lvl.sendParticles(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 3, d, d, d, 0);
                }
            }
        }
	}

	@Override
	public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
		if (this.isAlive() && this.isEffectiveAi()) {
			if ((source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA)) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE));
			} else if (source.is(DamageTypes.DROWN) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
				this.givePotion(PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING));
			}
		}
		return (!(source.getEntity() instanceof AbstractKoboldEntity) && !source.is(DamageTypes.CAMPFIRE) && super.hurtServer(lvl, source, amount));
	}

    @Override
    public boolean doHurtTarget(ServerLevel lvl, Entity target) {
        boolean check = super.doHurtTarget(lvl, target);
        if (check && !target.isAlive() && this.isSpear(this.getMainHandItem())) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0));
        }
        return check;
    }

    @Override
    public void setAge(int i) {
        super.setAge(i);
        if (!this.isDiamond() && i > 0) {
            this.getEntityData().set(DATA_DIAMOND_EYES, true);
        } else if (this.isDiamond() && i <= 0) {
            this.getEntityData().set(DATA_DIAMOND_EYES, false);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby()) {
            this.giveNewWeapon(Math.random());
        }
    }

	@Override
	public boolean canBreed() {
		return this.getAge() > 18000;
	}

	public void givePotion(ItemStack stack) {
		if (this.getOffhandItem().isEmpty() && !this.isBaby()) {
			this.setItemInHand(InteractionHand.OFF_HAND, stack);
		}
	}

    public void giveNewWeapon(double d) {
        if (d >= 0.7) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.enchantWeapon(new ItemStack(Items.CROSSBOW), false));
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else if (d <= 0.45) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.enchantWeapon(new ItemStack(KoboldsItems.KOBOLD_IRON_SPEAR.get()), false));
            if (d <= 0.15) {
                this.setItemSlot(EquipmentSlot.OFFHAND, this.enchantWeapon(new ItemStack(Items.SHIELD), false));
            }
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.enchantWeapon(new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()), false));
        }
    }

    public ItemStack enchantWeapon(ItemStack stack, boolean check) {
        if (Math.random() >= 0.85 || check) {
            return EnchantmentHelper.enchantItem(this.getRandom(), stack, 21, this.registryAccess(), Optional.empty());
        }
        return stack;
    }

	public boolean isDiamond() {
		return this.getEntityData().get(DATA_DIAMOND_EYES);
	}

    public boolean isHoldingPotion() {
        return this.isHolding(stack -> stack.is(Items.POTION)) || this.isHolding(stack -> stack.is(Items.GLASS_BOTTLE));
    }

    public abstract String getTradeType();

    public abstract void setType(int i);

    public abstract void spawnChild(ServerLevel lvl, AbstractKoboldEntity partner);

    public abstract boolean isSpear(ItemStack stack);

    public abstract boolean isBreedItem(ItemStack stack);

    public abstract boolean canBlock(ItemStack stack);

    public abstract boolean canTrade(ItemStack stack);

    public abstract boolean isPreferredWeapon(ItemStack stack);

    public abstract int getSpawnType(EntitySpawnReason reason);
}