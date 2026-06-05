package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.*;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Kobold extends AbstractKoboldEntity {
    private static final EntityDataAccessor<Boolean> ENCHANTER = SynchedEntityData.defineId(Kobold.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ENGINEER = SynchedEntityData.defineId(Kobold.class, EntityDataSerializers.BOOLEAN);

	public Kobold(EntityType<Kobold> type, Level world) {
		super(type, world);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Enchanter", this.isEnchanter());
        tag.putBoolean("Engineer", this.isEngineer());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(ENCHANTER, tag.getBooleanOr("Enchanter", false));
        this.getEntityData().set(ENGINEER, tag.getBooleanOr("Engineer", false));
        if (this.getMainHandItem().isEmpty()) {
            this.giveNewWeapon(Math.random());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENCHANTER, false);
        builder.define(ENGINEER, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
        this.goalSelector.addGoal(1, new KoboldTradeGoal(this));
        this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
        this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
        this.goalSelector.addGoal(2, new KoboldMeleeGoal<>(this, 1.2D, false));
        this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
    }

    @Override
    public void die(DamageSource source) {
        if (source.getDirectEntity() instanceof Zombie && !this.isBaby()) {
            this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
            if (this.level() instanceof ServerLevel lvl) {
                if (this.getOffhandItem().is(Items.POTION)) {
                    this.spawnAtLocation(lvl, this.getOffhandItem());
                    this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
                KoboldZombie zombo = this.convertTo(KoboldsMobs.KOBOLD_ZOMBIE.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
                zombo.setZombo(this);
            }
        }
        super.die(source);
    }

    @Override
    public Kobold getBreedOffspring(ServerLevel lvl, AgeableMob target) {
        return KoboldsMobs.KOBOLD.get().spawn(lvl, this.blockPosition(), EntitySpawnReason.BREEDING);
    }

    @Override
    public String getTradeType() {
        if (this.isEnchanter()) {
            return "gameplay/enchanter_loot";
        } else if (this.isEngineer()) {
            return "gameplay/engineer_loot";
        }
        return "gameplay/trader_loot";
    }

    @Override
    public void setType(int i) {
        if (i <= 10) {
            this.getEntityData().set(ENCHANTER, true);
        } else if (i >= 95) {
            this.getEntityData().set(ENGINEER, true);
        }
    }

    @Override
    public void spawnChild(ServerLevel lvl, AbstractKoboldEntity partner) {
        Kobold target = this.getBreedOffspring(lvl, partner);
        BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, partner, target);
        if (event.getChild() instanceof Kobold child) {
            target = child;
        }
        if (event.isCanceled()) {
            this.setAge(6000);
            partner.setAge(6000);
        } else {
            if (target != null) {
                this.setAge(18000);
                partner.setAge(18000);
                target.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                lvl.broadcastEntityEvent(this, (byte) 18);
                if (lvl.getGameRules().get(GameRules.MOB_DROPS)) {
                    lvl.addFreshEntity(new ExperienceOrb(lvl, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
                }
            }
        }
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
        if (this.isEnchanter()) {
            if (!drop.is(KoboldsTags.ARMOR) && !this.canTrade(drop)) {
                return false;
            }
        }
        if (this.isEngineer()) {
            if (this.isSpear(drop)) {
                return false;
            }
        }
        return super.canReplaceCurrentItem(drop, hand, slot);
    }

    @Override
    public boolean canReplaceEqualItem(ItemStack drop, ItemStack hand) {
        boolean check = super.canReplaceEqualItem(drop, hand);
        if (check && !this.isEngineer() && drop.getItem() instanceof CrossbowItem && drop.isEnchanted()) {
            int i = drop.getTagEnchantments().size() * 5;
            this.setType(Mth.nextInt(this.getRandom(), 65 + i, 95 + i));
        }
        return check;
    }

    @Override
    public void giveNewWeapon(double d) {
        if (this.isEnchanter()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else if (this.isEngineer()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.enchantWeapon(new ItemStack(Items.CROSSBOW), true));
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else {
            super.giveNewWeapon(d);
        }
    }

    @Override
    public boolean isSpear(ItemStack stack) {
        return stack.is(ItemTags.SPEARS);
    }

    @Override
    public boolean isBreedItem(ItemStack stack) {
        return stack.is(Items.AMETHYST_SHARD);
    }

    @Override
    public boolean canBlock(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    @Override
    public boolean canTrade(ItemStack stack) {
        return stack.is(Items.EMERALD);
    }

    @Override
    public boolean isPreferredWeapon(ItemStack stack) {
        if (this.isEngineer()) {
            return stack.getItem() instanceof CrossbowItem;
        }
        return stack.is(KoboldsTags.BASIC);
    }

    @Override
    public int getSpawnType(EntitySpawnReason reason) {
        int min = reason != EntitySpawnReason.BREEDING ? 10 : 5;
        int max = reason != EntitySpawnReason.BREEDING ? 95 : 100;
        return Mth.nextInt(this.getRandom(), min, max);
    }

    public boolean isEnchanter() {
        return this.getEntityData().get(ENCHANTER);
    }

    public boolean isEngineer() {
        return this.getEntityData().get(ENGINEER);
    }

    public boolean isWarrior() {
        return this.getOffhandItem().getItem() instanceof ShieldItem;
    }
}