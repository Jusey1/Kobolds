package net.salju.kobolds.client.renderer;

import net.salju.kobolds.entity.KoboldChild;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldChildModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldChildRenderer extends MobRenderer<KoboldChild, KoboldChildModel<KoboldChild>> {
	public KoboldChildRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldChildModel(context.bakeLayer(KoboldChildModel.KOBOLD_CHILD_MODEL)), 0.36f);
		this.addLayer(new KoboldEyesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(KoboldChild entity) {
		return new ResourceLocation("kobolds:textures/entities/kobold_classic.png");
	}

	@Override
	public void render(KoboldChild kobold, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.45F;
		stack.scale(scale, scale, scale);
		super.render(kobold, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}