package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldChildModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldChildRenderer extends AbstractKoboldRenderer {
	public KoboldChildRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldChildModel<>(context.bakeLayer(KoboldsClient.KOBOLD_CHILD)), false);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

	@Override
	public void submit(AbstractKoboldState kobold, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
		pose.pushPose();
		pose.translate(-0.025, 0, 0);
		float scale = 0.45F;
		pose.scale(scale, scale, scale);
		super.submit(kobold, pose, buffer, c);
		pose.popPose();
	}

    @Override
    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/child/" + kobold.getDragonColor();
        }
        return "kobolds/child";
    }
}