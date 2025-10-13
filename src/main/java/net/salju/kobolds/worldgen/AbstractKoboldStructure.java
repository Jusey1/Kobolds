package net.salju.kobolds.worldgen;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.init.KoboldsStructures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.*;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.biome.Biome;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;
import java.util.Optional;

public class AbstractKoboldStructure extends Structure {
	public static final MapCodec<AbstractKoboldStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(AbstractKoboldStructure.settingsCodec(instance),
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
					ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.jiggy),
					HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
					Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.heightmap),
					DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING).forGetter(structure -> structure.dims),
					LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS).forGetter(structure -> structure.water),
					RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("surface_biomes").forGetter(structure -> structure.underBiomes)
			).apply(instance, AbstractKoboldStructure::new));

	private final Holder<StructureTemplatePool> startPool;
	private final Optional<ResourceLocation> jiggy;
	private final HeightProvider startHeight;
	private final Optional<Heightmap.Types> heightmap;
	private final DimensionPadding dims;
	private final LiquidSettings water;
	private final Optional<HolderSet<Biome>> underBiomes;

	public AbstractKoboldStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> pool, Optional<ResourceLocation> jigsaw, HeightProvider height, Optional<Heightmap.Types> map, DimensionPadding d, LiquidSettings w, Optional<HolderSet<Biome>> under) {
		super(config);
		this.startPool = pool;
		this.jiggy = jigsaw;
		this.startHeight = height;
		this.heightmap = map;
		this.dims = d;
		this.water = w;
		this.underBiomes = under;
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		BlockPos pos = new BlockPos(context.chunkPos().getMinBlockX(), this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())), context.chunkPos().getMinBlockZ());
		if (!underCheck(context, pos) || waterCheck(context, pos)) {
			return Optional.empty();
		}
		return JigsawPlacement.addPieces(context, this.startPool, this.jiggy, 7, pos, false, this.heightmap, new JigsawStructure.MaxDistance(120), PoolAliasLookup.EMPTY, this.dims, this.water);
	}

	@Override
	public StructureType<?> type() {
		return KoboldsStructures.KOBOLD_DEN.get();
	}

	protected boolean underCheck(GenerationContext context, BlockPos pos) {
		if (!(context.biomeSource() instanceof CheckerboardColumnBiomeSource)) {
			for (int x = context.chunkPos().x - 1; x <= context.chunkPos().x + 1; x++) {
				for (int z = context.chunkPos().z - 1; z <= context.chunkPos().z + 1; z++) {
					Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromSection(x), QuartPos.fromBlock(pos.getY()), QuartPos.fromSection(z), context.randomState().sampler());
					if (underBiomes.isPresent()) {
						if (underBiomes.get().contains(biome)) {
							return true;
						}
					} else if (biome.is(KoboldsTags.BIOMES)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean waterCheck(GenerationContext context, BlockPos pos) {
		if (!(context.biomeSource() instanceof CheckerboardColumnBiomeSource)) {
			for (int x = context.chunkPos().x - 3; x <= context.chunkPos().x + 3; x++) {
				for (int z = context.chunkPos().z - 3; z <= context.chunkPos().z + 3; z++) {
					Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromSection(x), QuartPos.fromBlock(pos.getY()), QuartPos.fromSection(z), context.randomState().sampler());
					if (biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_OCEAN)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}