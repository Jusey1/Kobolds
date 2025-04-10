package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.KoboldSkeleton;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldSkeletonRenderer extends MobRenderer<KoboldSkeleton, AbstractKoboldState, SkeleboldModel<AbstractKoboldState>> {
	public KoboldSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeleboldModel(context.bakeLayer(KoboldsClient.SKELEBOLD)), 0.36f);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_OUTER)), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractKoboldState skelebold) {
		return skelebold.texture;
	}

	@Override
	public AbstractKoboldState createRenderState() {
		return new AbstractKoboldState();
	}

	@Override
	public void extractRenderState(KoboldSkeleton skelebold, AbstractKoboldState state, float f1) {
		super.extractRenderState(skelebold, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(skelebold, state, f1, this.itemModelResolver);
		state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton.png");
		state.isAggressive = skelebold.isAggressive();
		state.isCharging = skelebold.isCharging();
		state.isLeftHanded = skelebold.isLeftHanded();
		state.rightStack = skelebold.getItemHeldByArm(HumanoidArm.RIGHT);
		state.leftStack = skelebold.getItemHeldByArm(HumanoidArm.LEFT);
	}

	@Override
	public void render(AbstractKoboldState skelebold, PoseStack stack, MultiBufferSource buffer, int i) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(skelebold, stack, buffer, i);
		stack.popPose();
	}
}