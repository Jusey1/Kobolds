package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.RascalModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldRascalRenderer extends AbstractKoboldRenderer {
	public KoboldRascalRenderer(EntityRendererProvider.Context context) {
		super(context, new RascalModel<>(context.bakeLayer(KoboldsClient.KOBOLD_RASCAL)), true);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

	@Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/rascal/" + kobold.getDragonColor();
        }
        return "kobolds/rascal";
    }
}