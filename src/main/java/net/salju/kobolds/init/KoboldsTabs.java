package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class KoboldsTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Kobolds.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KOBOLDS = REGISTRY.register("kobolds",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.kobolds")).icon(() -> new ItemStack(KoboldsItems.KOBOLD_SKULL.get())).displayItems((parameters, tabData) -> {
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
				tabData.accept(KoboldsItems.KOBOLD_SKULL.get());
			}).build());
}