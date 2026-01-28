package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldChildModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldChildRenderer extends AbstractKoboldRenderer {
	public KoboldChildRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldChildModel<>(context.bakeLayer(KoboldsClient.KOBOLD_CHILD)), false);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/child/" + kobold.getDragonColor();
        }
        return "kobolds/child";
    }
}