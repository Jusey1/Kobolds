package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
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

public class KoboldEnchanterRenderer extends MobRenderer<AbstractKoboldEntity, AbstractKoboldState, KoboldModel<AbstractKoboldState>> {
	public KoboldEnchanterRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel(context.bakeLayer(KoboldsClient.KOBOLD)), 0.36f);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_OUTER)), context.getEquipmentRenderer()));
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
		if (kobold.getName().getString().equals("James")) {
			state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/special/james.png");
		} else if (kobold.getName().getString().equals("Popper")) {
			state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/special/popper.png");
			state.isPopper = true;
		} else {
			state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/kobolds/enchanter.png");
		}
		state.isDiamond = kobold.isDiamond();
		state.isLeftHanded = kobold.isLeftHanded();
		state.rightStack = kobold.getItemHeldByArm(HumanoidArm.RIGHT);
		state.leftStack = kobold.getItemHeldByArm(HumanoidArm.LEFT);
	}

	@Override
	public void render(AbstractKoboldState kobold, PoseStack stack, MultiBufferSource buffer, int i) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, stack, buffer, i);
		stack.popPose();
	}
}