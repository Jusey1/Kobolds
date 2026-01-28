package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BannerPattern;

public class KoboldsTags {
	public static final TagKey<EntityType<?>> KOBOLD = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobolds"));
	public static final TagKey<EntityType<?>> TRADERS = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, "traders"));
	public static final TagKey<EntityType<?>> TARGETS = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, "normal_targets"));
	public static final TagKey<EntityType<?>> TARGETZ = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, "warrior_targets"));
    public static final TagKey<EntityType<?>> DRAGON = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, "likes_dragons"));
	public static final TagKey<BannerPattern> BANNER_PATTERN_KOBOLD = TagKey.create(Registries.BANNER_PATTERN, Identifier.fromNamespaceAndPath(Kobolds.MODID, "pattern_for_kobold"));
	public static final TagKey<Biome> BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_den_biomes"));
	public static final TagKey<Enchantment> ENCHS = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_preferred"));
	public static final TagKey<Item> ARMOR = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_armor"));
	public static final TagKey<Item> BASIC = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_weapons"));
	public static final TagKey<Item> WAR = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_warrior_weapons"));
	public static final TagKey<Item> ENGI = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_engineer_weapons"));
    public static final TagKey<Item> CAPTAIN = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_captain_desirables"));
    public static final TagKey<Item> VALUABLE = ItemTags.create(Identifier.fromNamespaceAndPath(Kobolds.MODID, "kobold_captain_valuable"));
}