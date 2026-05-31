package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.entity.Kobold;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KoboldRenderer extends AbstractKoboldRenderer {
	public KoboldRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel<>(context.bakeLayer(KoboldsClient.KOBOLD)), true);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity target) {
        if (target.isBaby()) {
            return "kobolds/child";
        }
        if (target.getName().getString().equals("Popper")) {
            return "special/popper";
        }
        if (target instanceof Kobold kobold) {
            if (kobold.isEnchanter()) {
                if (target.getName().getString().equals("James")) {
                    return "special/james";
                }
                return "kobolds/enchanter";
            } else if (kobold.isEngineer()) {
                if (kobold.getName().getString().equals("Dell") || kobold.getName().getString().equals("Conagher") || kobold.getName().getString().equals("Dell Conagher")) {
                    return "special/dell";
                }
                return "kobolds/engineer";
            } else if (kobold.isWarrior()) {
                return "kobolds/warrior";
            }
        }
        return "kobolds/base";
    }
}