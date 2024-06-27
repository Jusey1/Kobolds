package net.salju.kobolds.client.renderer;

import net.salju.kobolds.entity.KoboldSkeleton;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldSkeletonRenderer extends MobRenderer<KoboldSkeleton, SkeleboldModel<KoboldSkeleton>> {
	public KoboldSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeleboldModel(context.bakeLayer(SkeleboldModel.SKELEBOLD_MODEL)), 0.36f);
		this.addLayer(new ItemInHandLayer<KoboldSkeleton, SkeleboldModel<KoboldSkeleton>>(this, context.getItemInHandRenderer()));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldArmorModel.KOBOLD_ARMOR_INNER_MODEL)), new KoboldArmorModel(context.bakeLayer(KoboldArmorModel.KOBOLD_ARMOR_OUTER_MODEL)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(KoboldSkeleton entity) {
		return new ResourceLocation("kobolds:textures/entity/undead/skeleton.png");
	}

	@Override
	public void render(KoboldSkeleton kobold, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}