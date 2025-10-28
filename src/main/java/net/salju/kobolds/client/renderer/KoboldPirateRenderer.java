package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldPirateEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldPirateRenderer extends AbstractKoboldRenderer {
	public KoboldPirateRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel<>(context.bakeLayer(KoboldsClient.KOBOLD)), true);
		this.addLayer(new KoboldPirateEyesLayer<>(this));
	}

	@Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/pirate/" + kobold.getDragonColor();
        }
        return "kobolds/pirate";
    }
}