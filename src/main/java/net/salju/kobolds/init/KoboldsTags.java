package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class KoboldsTags {
	public static final TagKey<EntityType<?>> KOBOLD = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobolds"));
	public static final TagKey<EntityType<?>> TRADERS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "traders"));
	public static final TagKey<EntityType<?>> TARGETS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "normal_targets"));
	public static final TagKey<EntityType<?>> TARGETZ = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "warrior_targets"));
	public static final TagKey<Biome> BIOMES = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_den_biomes"));
	public static final TagKey<Enchantment> ENCHS = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "kobold_preferred"));
	public static final TagKey<Item> RANGED = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "usable_ranged_weapons"));
}