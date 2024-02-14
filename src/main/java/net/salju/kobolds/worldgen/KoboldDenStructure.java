package net.salju.kobolds.worldgen;

import net.salju.kobolds.init.KoboldsStructures;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class KoboldDenStructure extends AbstractKoboldStructure {
	public static final Codec<KoboldDenStructure> CODEC = RecordCodecBuilder.<KoboldDenStructure>mapCodec((codex) -> {
		return codex.group(settingsCodec(codex), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((temppool) -> {
			return temppool.startPool;
		}), HeightProvider.CODEC.fieldOf("start_height").forGetter((heighty) -> {
			return heighty.startHeight;
		}), ResourceLocation.CODEC.fieldOf("surface_biome_tag").forGetter((tag) -> {
			return tag.underBiomes;
		})).apply(codex, KoboldDenStructure::new);
	}).codec();

	public KoboldDenStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> pool, HeightProvider height, ResourceLocation under) {
		super(config, pool, height, null, under);
	}

	@Override
	public StructureType<?> type() {
		return KoboldsStructures.KOBOLD_DEN.get();
	}
}