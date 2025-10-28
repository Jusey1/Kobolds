package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.salju.kobolds.client.renderer.layers.KoboldWitherEyesLayer;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldWitherRenderer extends AbstractKoboldRenderer {
	public KoboldWitherRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeleboldModel<>(context.bakeLayer(KoboldsClient.WITHERBOLD)), true);
		this.addLayer(new KoboldWitherEyesLayer<>(this));
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        return "undead/skeleton_wither";
    }
}