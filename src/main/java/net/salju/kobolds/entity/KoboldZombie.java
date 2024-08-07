package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;

public class KoboldZombie extends Zombie {
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING = SynchedEntityData.defineId(KoboldZombie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(KoboldZombie.class, EntityDataSerializers.STRING);
	private int zomboType;
	private int convert;

	public KoboldZombie(EntityType<KoboldZombie> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		this.getEyePosition(0.5F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CONVERTING, false);
		this.entityData.define(DATA_TYPE, "base");
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ZomboType", this.zomboType);
		tag.putInt("Convert", this.convert);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setZombo(tag.getInt("ZomboType"));
		this.convert = tag.getInt("Convert");
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.225D;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return this.isBaby() ? 0.66F : 1.26F;
	}

	@Override
	protected boolean convertsInWater() {
		return false;
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	public boolean isConvert() {
		return this.getEntityData().get(DATA_CONVERTING);
	}

	public String getZomboType() {
		return this.getEntityData().get(DATA_TYPE);
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ZOMBIE_VILLAGER_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_VILLAGER_DEATH;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide() && this.isAlive() && !this.isNoAi()) {
			if (this.convert > 1) {
				--this.convert;
				if (!this.isConvert()) {
					this.getEntityData().set(DATA_CONVERTING, true);
				}
			} else if (this.convert == 1) {
				if (this.isAlive()) {
					this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
					if (this.level() instanceof ServerLevel lvl) {
						if (this.zomboType <= 1) {
							KoboldCaptain capty = this.convertTo(KoboldsMobs.KOBOLD_CAPTAIN.get(), true);
							capty.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, capty);
						} else if (this.zomboType == 2) {
							Kobold pirate = this.convertTo(KoboldsMobs.KOBOLD_PIRATE.get(), true);
							pirate.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, pirate);
						} else if (this.zomboType == 3) {
							KoboldWarrior war = this.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), true);
							war.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, war);
						} else if (this.zomboType == 4) {
							KoboldEngineer engi = this.convertTo(KoboldsMobs.KOBOLD_ENGINEER.get(), true);
							engi.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, engi);
						} else if (this.zomboType == 5) {
							KoboldEnchanter magic = this.convertTo(KoboldsMobs.KOBOLD_ENCHANTER.get(), true);
							magic.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, magic);
						} else {
							Kobold basic = this.convertTo(KoboldsMobs.KOBOLD.get(), true);
							basic.setCanPickUpLoot(true);
							ForgeEventFactory.onLivingConvert(this, basic);
						}
					}
				}
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack apple = player.getItemInHand(hand);
		int waitTicks = 0;
		int potionLevel = 0;
		if (!this.level().isClientSide() && apple.getItem() == Items.GOLDEN_APPLE && this.hasEffect(MobEffects.WEAKNESS)) {
			if (this.level().getDifficulty() == Difficulty.EASY) {
				potionLevel = 0;
				waitTicks = 1200;
			} else if (this.level().getDifficulty() == Difficulty.NORMAL) {
				potionLevel = 0;
				waitTicks = 2400;
			} else if (this.level().getDifficulty() == Difficulty.HARD) {
				potionLevel = 1;
				waitTicks = 4800;
			}
			if (!player.isCreative()) {
				apple.shrink(1);
			}
			player.swing(hand, true);
			this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
			this.removeEffect(MobEffects.WEAKNESS);
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, waitTicks, potionLevel));
			this.convert = waitTicks;
			this.getEntityData().set(DATA_CONVERTING, true);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (reason != MobSpawnType.CONVERSION) {
			this.setZombo(Mth.nextInt(world.getRandom(), 1, 6));
			this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
			this.populateDefaultEquipmentEnchantments(world.getRandom(), difficulty);
		}
		return super.finalizeSpawn(world, difficulty, reason, data, tag);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		if (this.getZomboType() == "warrior") {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_AXE.get()));
		} else if (this.getZomboType() == "pirate_captain") {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
		} else if (this.getZomboType() == "engineer") {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
		} else if (this.getZomboType() == "base" || this.getZomboType() == "pirate") {
			if (Math.random() >= 0.6) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
			} else {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
			}
		}
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		if (this.getZomboType() == "warrior") {
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		}
		this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
	}

	public void setZombo(AbstractKoboldEntity kobold) {
		if (kobold.getType() == KoboldsMobs.KOBOLD_CAPTAIN.get()) {
			this.getEntityData().set(DATA_TYPE, "pirate_captain");
			this.zomboType = 1;
		} else if (kobold.getType() == KoboldsMobs.KOBOLD_PIRATE.get()) {
			this.getEntityData().set(DATA_TYPE, "pirate");
			this.zomboType = 2;
		} else if (kobold.getType() == KoboldsMobs.KOBOLD_WARRIOR.get()) {
			this.getEntityData().set(DATA_TYPE, "warrior");
			this.zomboType = 3;
		} else if (kobold.getType() == KoboldsMobs.KOBOLD_ENGINEER.get()) {
			this.getEntityData().set(DATA_TYPE, "engineer");
			this.zomboType = 4;
		} else if (kobold.getType() == KoboldsMobs.KOBOLD_ENCHANTER.get()) {
			this.getEntityData().set(DATA_TYPE, "enchanter");
			this.zomboType = 5;
		} else {
			this.getEntityData().set(DATA_TYPE, "base");
			this.zomboType = 6;
		}
	}

	public void setZombo(int i) {
		this.zomboType = i;
		if (i <= 1) {
			this.getEntityData().set(DATA_TYPE, "pirate_captain");
		} else if (i == 2) {
			this.getEntityData().set(DATA_TYPE, "pirate");
		} else if (i == 3) {
			this.getEntityData().set(DATA_TYPE, "warrior");
		} else if (i == 4) {
			this.getEntityData().set(DATA_TYPE, "engineer");
		} else if (i == 5) {
			this.getEntityData().set(DATA_TYPE, "enchanter");
		} else if (i >= 6) {
			this.getEntityData().set(DATA_TYPE, "base");
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
		builder = builder.add(Attributes.MAX_HEALTH, 18);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
		return builder;
	}

	public static void init() {
		SpawnPlacements.register(KoboldsMobs.KOBOLD_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}
}