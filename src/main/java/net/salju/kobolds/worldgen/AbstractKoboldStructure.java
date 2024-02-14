package net.salju.kobolds.worldgen;

import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import java.util.Optional;

public abstract class AbstractKoboldStructure extends Structure {
	public final Holder<StructureTemplatePool> startPool;
	public final HeightProvider startHeight;
	public final Optional<Heightmap.Types> heightmap;
	public final ResourceLocation underBiomes;

	public AbstractKoboldStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> pool, HeightProvider height, Optional<Heightmap.Types> map, ResourceLocation under) {
		super(config);
		this.startPool = pool;
		this.startHeight = height;
		this.heightmap = map;
		this.underBiomes = under;
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		BlockPos pos = new BlockPos(context.chunkPos().getMinBlockX(), this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())), context.chunkPos().getMinBlockZ());
		if (!waterCheck(context, pos)) {
			return Optional.empty();
		} else if (underBiomes != null) {
			if (!underCheck(context, pos)) {
				return Optional.empty();
			}
		}
		return JigsawPlacement.addPieces(context, this.startPool, Optional.empty(), 1, pos, false, (heightmap == null ? Optional.empty() : heightmap), 80);
	}

	protected boolean waterCheck(GenerationContext context, BlockPos pos) {
		if (!(context.biomeSource() instanceof CheckerboardColumnBiomeSource)) {
			for (int x = context.chunkPos().x - 3; x <= context.chunkPos().x + 3; x++) {
				for (int z = context.chunkPos().z - 3; z <= context.chunkPos().z + 3; z++) {
					Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromSection(x), QuartPos.fromBlock(pos.getY()), QuartPos.fromSection(z), context.randomState().sampler());
					if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_RIVER)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean underCheck(GenerationContext context, BlockPos pos) {
		if (!(context.biomeSource() instanceof CheckerboardColumnBiomeSource)) {
			for (int x = context.chunkPos().x - 1; x <= context.chunkPos().x + 1; x++) {
				for (int z = context.chunkPos().z - 1; z <= context.chunkPos().z + 1; z++) {
					Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromSection(x), QuartPos.fromBlock(pos.getY()), QuartPos.fromSection(z), context.randomState().sampler());
					if (biome.is(TagKey.create(Registries.BIOME, underBiomes))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}