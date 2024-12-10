package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;

public class KoboldsItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.createItems(Kobolds.MODID);
	public static final DeferredHolder<Item, Item> KOBOLD_SPAWN_EGG = REGISTRY.register("kobold_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD.get(), -10066330, -6684775, createBaseProps("kobold_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_WARRIOR_SPAWN_EGG = REGISTRY.register("kobold_warrior_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_WARRIOR.get(), -10066330, -16738048, createBaseProps("kobold_warrior_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_ENCHANTER_SPAWN_EGG = REGISTRY.register("kobold_enchanter_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_ENCHANTER.get(), -10066330, -13057, createBaseProps("kobold_enchanter_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_RASCAL_SPAWN_EGG = REGISTRY.register("kobold_rascal_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_RASCAL.get(), -10066330, -3355393, createBaseProps("kobold_rascal_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_ENGINEER_SPAWN_EGG = REGISTRY.register("kobold_engineer_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_ENGINEER.get(), -10066330, -65536, createBaseProps("kobold_engineer_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_PIRATE_SPAWN_EGG = REGISTRY.register("kobold_pirate_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_PIRATE.get(), -10066330, -3355648, createBaseProps("kobold_pirate_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_CAPTAIN_SPAWN_EGG = REGISTRY.register("kobold_captain_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_CAPTAIN.get(), -6750208, -3355648, createBaseProps("kobold_captain_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_ZOMBIE_SPAWN_EGG = REGISTRY.register("kobold_zombie_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_ZOMBIE.get(), -16724788, -6684775, createBaseProps("kobold_zombie_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_SKELETON_SPAWN_EGG = REGISTRY.register("kobold_skeleton_spawn_egg", () -> new SpawnEggItem(KoboldsMobs.KOBOLD_SKELETON.get(), -3355444, -13421773, createBaseProps("kobold_skeleton_spawn_egg")));
	public static final DeferredHolder<Item, Item> KOBOLD_SKULL = REGISTRY.register("kobold_skull", () -> new StandingAndWallBlockItem(KoboldsBlocks.KOBOLD_SKULL.get(), KoboldsBlocks.KOBOLD_SKULL_WALL.get(), Direction.DOWN, createBaseProps("kobold_skull").rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_SWORD = REGISTRY.register("kobold_iron_sword", () -> new SwordItem(KoboldsItems.KOBOLD_IRON, 3.0F, -2.2F, createBaseProps("kobold_iron_sword")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_SHOVEL = REGISTRY.register("kobold_iron_shovel", () -> new ShovelItem(KoboldsItems.KOBOLD_IRON, 1.5F, -3.0F, createBaseProps("kobold_iron_shovel")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_PICKAXE = REGISTRY.register("kobold_iron_pickaxe", () -> new PickaxeItem(KoboldsItems.KOBOLD_IRON, 1.0F, -2.8F, createBaseProps("kobold_iron_pickaxe")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_AXE = REGISTRY.register("kobold_iron_axe", () -> new AxeItem(KoboldsItems.KOBOLD_IRON, 5.0F, -3.0F, createBaseProps("kobold_iron_axe")));
	public static final DeferredHolder<Item, Item> KOBOLD_IRON_HOE = REGISTRY.register("kobold_iron_hoe", () -> new HoeItem(KoboldsItems.KOBOLD_IRON, -2.0F, -1.0F, createBaseProps("kobold_iron_hoe")));
	public static final DeferredHolder<Item, Item> BANNER_PATTERN_KOBOLD = REGISTRY.register("banner_pattern_kobold", () -> new BannerPatternItem(TagKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "pattern_for_kobold")), createBaseProps("banner_pattern_kobold").stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> MUSIC_DISC_KOBBLESTONE = REGISTRY.register("music_disc_kobblestone", () -> new Item(createBaseProps("music_disc_kobblestone").stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobblestone")))));
	public static final DeferredHolder<Item, Item> KOBOLD_TEMPLATE = REGISTRY.register("kobold_template", () -> SmithingTemplateItem.createArmorTrimTemplate(createBaseProps("kobold_template").rarity(Rarity.UNCOMMON)));

	public static final ToolMaterial KOBOLD_IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1028, 5.0F, 2.0F, 12, ItemTags.IRON_TOOL_MATERIALS);

	public static Item.Properties createBaseProps(String name) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, name)));
	}
}