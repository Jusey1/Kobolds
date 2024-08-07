package net.salju.kobolds.init;

import net.salju.kobolds.item.KoboldPotionUtils;
import net.salju.kobolds.KoboldsMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import java.util.List;

public class KoboldsTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KoboldsMod.MODID);
	public static final RegistryObject<CreativeModeTab> KOBOLDS = REGISTRY.register("kobolds",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.kobolds")).icon(() -> new ItemStack(KoboldsBlocks.KOBOLD_SKULL.get().asItem())).displayItems((parameters, tabData) -> {
				tabData.accept(KoboldsItems.KOBOLD_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_WARRIOR_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_ENCHANTER_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_RASCAL_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_ENGINEER_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_PIRATE_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_CAPTAIN_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_ZOMBIE_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_SKELETON_SPAWN_EGG.get());
				tabData.accept(KoboldsItems.KOBOLD_TEMPLATE.get());
				tabData.accept(KoboldsItems.KOBOLD_IRON_SWORD.get());
				tabData.accept(KoboldsItems.KOBOLD_IRON_SHOVEL.get());
				tabData.accept(KoboldsItems.KOBOLD_IRON_PICKAXE.get());
				tabData.accept(KoboldsItems.KOBOLD_IRON_AXE.get());
				tabData.accept(KoboldsItems.KOBOLD_IRON_HOE.get());
				tabData.accept(KoboldsItems.BANNER_PATTERN_KOBOLD.get());
				tabData.accept(KoboldsItems.MUSIC_DISC_KOBBLESTONE.get());
				tabData.accept(KoboldsBlocks.KOBOLD_SKULL.get().asItem());
			}).build());
			
	public static final RegistryObject<CreativeModeTab> KOBOLDS_POTS = REGISTRY.register("kobolds_pots",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.kobolds_pots")).icon(() -> new ItemStack(KoboldsItems.KOBOLD_POTION.get())).displayItems((parameters, tabData) -> {
				List<MobEffect> commons = KoboldPotionUtils.getCommon();
				List<MobEffect> specials = KoboldPotionUtils.getSpecial();
				tabData.accept(KoboldPotionUtils.makePotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), MobEffects.HEAL, MobEffects.REGENERATION, 1, 900));
				for (MobEffect effect : commons) {
					tabData.accept(KoboldPotionUtils.makePotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), effect, null, 3600, 0));
				}
				for (MobEffect effect : specials) {
					tabData.accept(KoboldPotionUtils.makePotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), effect, null, 1200, 0));
				}
				for (MobEffect effect : commons) {
					for (MobEffect secondary : commons) {
						if (effect != secondary) {
							tabData.accept(KoboldPotionUtils.makePotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), effect, secondary, 3600, 3600));
						}
					}
				}
				for (MobEffect effect : specials) {
					for (MobEffect secondary : commons) {
						tabData.accept(KoboldPotionUtils.makePotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), effect, secondary, 1200, 3600));
					}
				}
			}).build());
}