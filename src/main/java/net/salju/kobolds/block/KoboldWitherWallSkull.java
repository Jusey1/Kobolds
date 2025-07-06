package net.salju.kobolds.block;

import net.minecraft.world.entity.EntityType;
import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SkullBlock;

public class KoboldWitherWallSkull extends KoboldWallSkull {
	public KoboldWitherWallSkull(SkullBlock.Type type, BlockBehaviour.Properties props) {
		super(type, props);
	}

	@Override
	protected EntityType<?> getKoboldSkeleton() {
		return KoboldsMobs.WITHERBOLD.get();
	}
}