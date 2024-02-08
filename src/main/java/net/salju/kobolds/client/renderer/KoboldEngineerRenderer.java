package net.salju.kobolds.client.renderer;

import net.salju.kobolds.entity.KoboldEngineer;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldEngineerRenderer extends MobRenderer<KoboldEngineer, KoboldModel<KoboldEngineer>> {
	public KoboldEngineerRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel(context.bakeLayer(KoboldModel.KOBOLD_MODEL)), 0.36f);
		this.addLayer(new ItemInHandLayer<KoboldEngineer, KoboldModel<KoboldEngineer>>(this, context.getItemInHandRenderer()));
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldArmorModel.KOBOLD_ARMOR_INNER_MODEL)), new KoboldArmorModel(context.bakeLayer(KoboldArmorModel.KOBOLD_ARMOR_OUTER_MODEL)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(KoboldEngineer kobold) {
		if (kobold.getDisplayName().getString().equals("Dell") || kobold.getDisplayName().getString().equals("Conagher") || kobold.getDisplayName().getString().equals("Dell Conagher")) {
			return new ResourceLocation("kobolds:textures/entity/kobold_engineer_tf2.png");
		} else if (kobold.getDisplayName().getString().equals("Popper")) {
			return new ResourceLocation("kobolds:textures/entity/kobold_popper.png");
		} else {
			return new ResourceLocation("kobolds:textures/entity/kobold_engineer.png");
		}
	}

	@Override
	public void render(KoboldEngineer kobold, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}