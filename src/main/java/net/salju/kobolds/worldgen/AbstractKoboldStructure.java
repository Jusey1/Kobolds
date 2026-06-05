package net.salju.kobolds.worldgen;

import net.salju.kobolds.init.KoboldsStructures;
import net.minecraft.core.*;
import net.minecraft.resources.Identifier;
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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;
import java.util.Optional;

public class AbstractKoboldStructure extends Structure {
	public static final MapCodec<AbstractKoboldStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(AbstractKoboldStructure.settingsCodec(instance),
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
					Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.jiggy),
					HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
					Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.heightmap),
					DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING).forGetter(structure -> structure.dims),
					LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS).forGetter(structure -> structure.water)
			).apply(instance, AbstractKoboldStructure::new));

	private final Holder<StructureTemplatePool> startPool;
	private final Optional<Identifier> jiggy;
	private final HeightProvider startHeight;
	private final Optional<Heightmap.Types> heightmap;
	private final DimensionPadding dims;
	private final LiquidSettings water;

	public AbstractKoboldStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> pool, Optional<Identifier> jigsaw, HeightProvider height, Optional<Heightmap.Types> map, DimensionPadding d, LiquidSettings w) {
		super(config);
		this.startPool = pool;
		this.jiggy = jigsaw;
		this.startHeight = height;
		this.heightmap = map;
		this.dims = d;
		this.water = w;
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		BlockPos pos = new BlockPos(context.chunkPos().getMinBlockX(), this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())), context.chunkPos().getMinBlockZ());
        int min = context.chunkGenerator().getFirstOccupiedHeight(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
        int max = context.chunkGenerator().getFirstOccupiedHeight(context.chunkPos().getMaxBlockX(), context.chunkPos().getMaxBlockZ(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, context.heightAccessor(), context.randomState());
        if (heightCheck(min) || heightCheck(max)) {
            return Optional.empty();
        }
		return JigsawPlacement.addPieces(context, this.startPool, this.jiggy, 7, pos, false, this.heightmap, new JigsawStructure.MaxDistance(120), PoolAliasLookup.EMPTY, this.dims, this.water);
	}

	@Override
	public StructureType<?> type() {
		return KoboldsStructures.KOBOLD_DEN.get();
	}

    protected boolean heightCheck(int i) {
        return i <= 64 || i >= 86;
    }
}