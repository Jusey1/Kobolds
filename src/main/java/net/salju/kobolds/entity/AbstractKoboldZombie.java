package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import javax.annotation.Nullable;

public abstract class AbstractKoboldZombie extends Zombie {
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING = SynchedEntityData.defineId(AbstractKoboldZombie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(AbstractKoboldZombie.class, EntityDataSerializers.STRING);
	private int zomboType;
	private int convert;

	public AbstractKoboldZombie(EntityType<? extends AbstractKoboldZombie> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		this.getEyePosition(0.5F);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CONVERTING, false);
		builder.define(DATA_TYPE, "base");
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ZomboType", this.zomboType);
		tag.putInt("Convert", this.convert);
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		this.setZombo(tag.getInt("ZomboType").orElse(6));
		this.convert = tag.getInt("Convert").orElse(0);
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
							KoboldCaptain capty = this.convertTo(KoboldsMobs.KOBOLD_CAPTAIN.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							capty.setCanPickUpLoot(true);
						} else if (this.zomboType == 2) {
							Kobold pirate = this.convertTo(KoboldsMobs.KOBOLD_PIRATE.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							pirate.setCanPickUpLoot(true);
						} else if (this.zomboType == 3) {
							KoboldWarrior war = this.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							war.setCanPickUpLoot(true);
						} else if (this.zomboType == 4) {
							KoboldEngineer engi = this.convertTo(KoboldsMobs.KOBOLD_ENGINEER.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							engi.setCanPickUpLoot(true);
						} else if (this.zomboType == 5) {
							KoboldEnchanter magic = this.convertTo(KoboldsMobs.KOBOLD_ENCHANTER.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							magic.setCanPickUpLoot(true);
						} else {
							Kobold basic = this.convertTo(KoboldsMobs.KOBOLD.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
							basic.setCanPickUpLoot(true);
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
			this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, waitTicks, potionLevel));
			this.convert = waitTicks;
			this.getEntityData().set(DATA_CONVERTING, true);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		SpawnGroupData spawndata = super.finalizeSpawn(world, difficulty, reason, data);
		if (reason != EntitySpawnReason.CONVERSION) {
			this.setZombo(Mth.nextInt(world.getRandom(), 1, 6));
			this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
			this.populateDefaultEquipmentEnchantments(world, world.getRandom(), difficulty);
		}
		return spawndata;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		if (this.getZomboType().equals("warrior")) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_AXE.get()));
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		} else if (this.getZomboType().equals("pirate_captain")) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
		} else if (this.getZomboType().equals("engineer")) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
		} else if (this.getZomboType().equals("base") || this.getZomboType().equals("pirate")) {
			if (Math.random() >= 0.6) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
			} else {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
			}
		}
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
	}

	public boolean isConvert() {
		return this.getEntityData().get(DATA_CONVERTING);
	}

	public String getZomboType() {
		return this.getEntityData().get(DATA_TYPE);
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
}