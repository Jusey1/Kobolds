package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.ai.KoboldRevengeGoal;
import net.salju.kobolds.entity.ai.KoboldPotionGoal;
import net.salju.kobolds.entity.ai.KoboldMeleeGoal;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Optional;

public class KoboldRascal extends AbstractKoboldEntity {
	public boolean isFound;
	private int despawnDelay;

	public KoboldRascal(EntityType<KoboldRascal> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide && --this.despawnDelay <= 0) {
			this.discard();
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!this.isFound) {
			if (!this.level().isClientSide()) {
				player.swing(hand);
				this.removeEffect(MobEffects.INVISIBILITY);
				this.swing(InteractionHand.MAIN_HAND, true);
				if (this.level() instanceof ServerLevel lvl) {
					this.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					ItemStack stack = new ItemStack(Items.BUNDLE);
					CompoundTag tag = stack.getOrCreateTag();
					tag.put("Items", new ListTag());
					ListTag list = tag.getList("Items", 10);
					if (Math.random() >= 0.85) {
						ItemStack loot = new ItemStack(KoboldsItems.KOBOLD_IRON_PICKAXE.get());
						loot.hurt(Mth.nextInt(RandomSource.create(), 128, 596), null, null);
						EnchantmentHelper.enchantItem(RandomSource.create(), loot, Mth.nextInt(RandomSource.create(), 18, 32), true);
						CompoundTag loottag = new CompoundTag();
						loot.save(loottag);
						list.add(0, (Tag) loottag);
					} else {
						int max = Mth.nextInt(RandomSource.create(), 7, 38);
						for (int i = 0; i < max; ++i) {
							ItemStack loot = new ItemStack((ForgeRegistries.ITEMS.tags().getTag(KoboldsTags.RASCAL).getRandomElement(RandomSource.create()).orElseGet(() -> Items.EMERALD)));
							if (Math.random() >= 0.99) {
								loot = new ItemStack(Items.DIAMOND);
							}
							Optional<CompoundTag> optional = getMatchingItem(loot, list);
							if (optional.isPresent()) {
								CompoundTag loottag = optional.get();
								ItemStack bagged = ItemStack.of(loottag);
								bagged.grow(loot.getCount());
								bagged.save(loottag);
								list.remove(loottag);
								list.add(0, (Tag) loottag);
							} else {
								CompoundTag loottag = new CompoundTag();
								loot.save(loottag);
								list.add(0, (Tag) loottag);
							}
						}
					}
					ItemEntity bundle = new ItemEntity(lvl, this.getX(), this.getY(), this.getZ(), stack);
					bundle.setPickUpDelay(10);
					lvl.addFreshEntity(bundle);
				}
			}
			this.isFound = true;
		}
		for (AbstractKoboldEntity kobold : this.level().getEntitiesOfClass(AbstractKoboldEntity.class, this.getBoundingBox().inflate(128.0D))) {
			if (kobold != null && !(kobold.is(this))) {
				this.getNavigation().moveTo(kobold, 1.2);
			}
		}
		return super.mobInteract(player, hand);
	}

	private static Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag tag) {
		return tag.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((loot) -> {
			return ItemStack.isSameItemSameTags(ItemStack.of(loot), stack);
		}).findFirst();
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof SwordItem || drop.getItem() instanceof ArmorItem) {
			return super.canReplaceCurrentItem(drop, hand);
		} else {
			return false;
		}
	}

	public void setDespawnDelay(int i) {
		this.despawnDelay = i;
	}

	public int getDespawnDelay() {
		return this.despawnDelay;
	}
}
