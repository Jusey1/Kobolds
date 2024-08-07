package net.salju.kobolds.init;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class KoboldsTags {
	public static final TagKey<Item> CAPTAIN_ONE = ItemTags.create(new ResourceLocation("kobolds:captain_tier_one"));
	public static final TagKey<Item> CAPTAIN_TWO = ItemTags.create(new ResourceLocation("kobolds:captain_tier_two"));
	public static final TagKey<Item> CAPTAIN_THREE = ItemTags.create(new ResourceLocation("kobolds:captain_tier_three"));
	public static final TagKey<Item> RASCAL = ItemTags.create(new ResourceLocation("kobolds:rascal_items"));
	public static final TagKey<Item> RANGED = ItemTags.create(new ResourceLocation("kobolds:usable_ranged_weapons"));
	public static final TagKey<Biome> BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("kobolds:kobold_den_biomes"));
}