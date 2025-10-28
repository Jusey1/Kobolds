package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldSkeletonRenderer extends AbstractKoboldRenderer {
	public KoboldSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeleboldModel<>(context.bakeLayer(KoboldsClient.SKELEBOLD)), true);
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        return "undead/skeleton";
    }
}