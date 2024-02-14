package net.salju.kobolds.worldgen;

import net.salju.kobolds.init.KoboldsStructures;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Holder;
import java.util.Optional;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class KoboldSurfaceStructure extends AbstractKoboldStructure {
	public static final Codec<KoboldSurfaceStructure> CODEC = RecordCodecBuilder.<KoboldSurfaceStructure>mapCodec((codex) -> {
		return codex.group(settingsCodec(codex), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((temppool) -> {
			return temppool.startPool;
		}), HeightProvider.CODEC.fieldOf("start_height").forGetter((heighty) -> {
			return heighty.startHeight;
		}), Heightmap.Types.CODEC.optionalFieldOf("heightmap").forGetter((mapster) -> {
			return mapster.heightmap;
		})).apply(codex, KoboldSurfaceStructure::new);
	}).codec();

	public KoboldSurfaceStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> pool, HeightProvider height, Optional<Heightmap.Types> map) {
		super(config, pool, height, map, null);
	}

	@Override
	public StructureType<?> type() {
		return KoboldsStructures.KOBOLD_SURFACE.get();
	}
}