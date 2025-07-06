package net.salju.kobolds.block;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SkullBlock;

public class KoboldWitherSkull extends KoboldSkull {
	public KoboldWitherSkull(SkullBlock.Type type, BlockBehaviour.Properties props) {
		super(type, props);
	}

	@Override
	protected EntityType<?> getKoboldSkeleton() {
		return KoboldsMobs.WITHERBOLD.get();
	}

	public enum Types implements SkullBlock.Type {
		WITHERBOLD;

		@Override
		public String getSerializedName() {
			return "witherbold";
		}
	}
}