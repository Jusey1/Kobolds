package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldEnchanterRenderer extends AbstractKoboldRenderer {
	public KoboldEnchanterRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel<>(context.bakeLayer(KoboldsClient.KOBOLD)), true);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

	@Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getName().getString().equals("James")) {
            return "special/james";
        } else if (kobold.getName().getString().equals("Popper")) {
            return "special/popper";
        } else if (kobold.getDragonColor() >= 1) {
            return "special/dragon/enchanter/" + kobold.getDragonColor();
        }
        return "kobolds/enchanter";
    }
}