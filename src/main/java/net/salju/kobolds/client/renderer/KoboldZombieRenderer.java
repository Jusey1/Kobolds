package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.model.ZomboldModel;
import net.salju.kobolds.client.renderer.layers.KoboldZombieEyesLayer;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldZombieRenderer extends AbstractKoboldRenderer {
	public KoboldZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new ZomboldModel<>(context.bakeLayer(KoboldsClient.ZOMBOLD)), true);
		this.addLayer(new KoboldZombieEyesLayer<>(this));
	}

	@Override
	protected boolean isShaking(AbstractKoboldState kobold) {
		return kobold.isZomboConverting;
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        return "undead/zombie_base";
    }
}