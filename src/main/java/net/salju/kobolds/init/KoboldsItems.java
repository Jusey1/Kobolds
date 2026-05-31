package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;

public class KoboldsItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.createItems(Kobolds.MODID);
	public static final DeferredHolder<Item, Item> KOBOLD_SPAWN_EGG = REGISTRY.register("kobold_spawn_egg", () -> new SpawnEggItem(createBaseProps("kobold_spawn_egg").spawnEgg(KoboldsMobs.KOBOLD.get())));
	public static final DeferredHolder<Item, Item> KOBOLD_ZOMBIE_SPAWN_EGG = REGISTRY.register("kobold_zombie_spawn_egg", () -> new SpawnEggItem(createBaseProps("kobold_zombie_spawn_egg").spawnEgg(KoboldsMobs.KOBOLD_ZOMBIE.get())));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_SWORD = REGISTRY.register("kobold_iron_sword", () -> new Item(createBaseProps("kobold_iron_sword").sword(KoboldsItems.KOBOLD_IRON, 1.0F, -2.2F)));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_SHOVEL = REGISTRY.register("kobold_iron_shovel", () -> new ShovelItem(KoboldsItems.KOBOLD_IRON, 0.5F, -3.0F, createBaseProps("kobold_iron_shovel")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_PICKAXE = REGISTRY.register("kobold_iron_pickaxe", () -> new Item(createBaseProps("kobold_iron_pickaxe").pickaxe(KoboldsItems.KOBOLD_IRON, 0.0F, -2.8F)));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_AXE = REGISTRY.register("kobold_iron_axe", () -> new AxeItem(KoboldsItems.KOBOLD_IRON, 4.0F, -3.0F, createBaseProps("kobold_iron_axe")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_HOE = REGISTRY.register("kobold_iron_hoe", () -> new HoeItem(KoboldsItems.KOBOLD_IRON, -3.0F, -1.0F, createBaseProps("kobold_iron_hoe")));
    public static final DeferredHolder<Item, Item> KOBOLD_IRON_SPEAR = REGISTRY.register("kobold_iron_spear", () -> new Item(createBaseProps("kobold_iron_spear").spear(KoboldsItems.KOBOLD_IRON, 0.85F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F)));
	public static final DeferredHolder<Item, Item> BANNER_PATTERN_KOBOLD = REGISTRY.register("banner_pattern_kobold", () -> new Item(createBaseProps("banner_pattern_kobold").stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.PROVIDES_BANNER_PATTERNS, KoboldsTags.BANNER_PATTERN_KOBOLD)));
	public static final DeferredHolder<Item, Item> MUSIC_DISC_KOBBLESTONE = REGISTRY.register("music_disc_kobblestone", () -> new Item(createBaseProps("music_disc_kobblestone").stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobblestone")))));
	public static final DeferredHolder<Item, Item> KOBOLD_TEMPLATE = REGISTRY.register("kobold_template", () -> SmithingTemplateItem.createArmorTrimTemplate(createBaseProps("kobold_template").rarity(Rarity.UNCOMMON)));

	public static final ToolMaterial KOBOLD_IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1028, 5.0F, 3.0F, 12, ItemTags.IRON_TOOL_MATERIALS);

	public static Item.Properties createBaseProps(String name) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Kobolds.MODID, name)));
	}
}