package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldChildModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldChildRenderer extends MobRenderer<AbstractKoboldEntity, AbstractKoboldState, KoboldChildModel<AbstractKoboldState>> {
	public KoboldChildRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldChildModel(context.bakeLayer(KoboldsClient.KOBOLD_CHILD)), 0.36f);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractKoboldState kobold) {
		return kobold.texture;
	}

	@Override
	public AbstractKoboldState createRenderState() {
		return new AbstractKoboldState();
	}

	@Override
	public void extractRenderState(AbstractKoboldEntity kobold, AbstractKoboldState state, float f1) {
		super.extractRenderState(kobold, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(kobold, state, f1, this.itemModelResolver);
        state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/" + this.getKoboldType(kobold) + ".png");
		state.isLeftHanded = kobold.isLeftHanded();
        state.dragonColor = kobold.getDragonColor();
	}

	@Override
	public void render(AbstractKoboldState kobold, PoseStack stack, MultiBufferSource buffer, int i) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.45F;
		stack.scale(scale, scale, scale);
		super.render(kobold, stack, buffer, i);
		stack.popPose();
	}

    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/child/" + kobold.getDragonColor();
        }
        return "kobolds/child";
    }
}