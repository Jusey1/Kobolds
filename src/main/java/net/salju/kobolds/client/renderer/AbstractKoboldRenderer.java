package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.entity.KoboldSkeleton;
import net.salju.kobolds.entity.KoboldZombie;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;

public abstract class AbstractKoboldRenderer extends MobRenderer<Mob, AbstractKoboldState, KoboldModel<AbstractKoboldState>> {
	public AbstractKoboldRenderer(EntityRendererProvider.Context context, KoboldModel<AbstractKoboldState> model, boolean check) {
		super(context, model, 0.36F);
        if (check) {
            this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(KoboldsClient.KOBOLD_ARMOR, context.getModelSet(), KoboldModel::new), ArmorModelSet.bake(KoboldsClient.KOBOLD_ARMOR, context.getModelSet(), KoboldModel::new), context.getEquipmentRenderer()));
            this.addLayer(new ItemInHandLayer<>(this));
        }
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
	public void extractRenderState(Mob target, AbstractKoboldState state, float f1) {
		super.extractRenderState(target, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(target, state, f1, this.itemModelResolver);
        if (target instanceof AbstractKoboldEntity kobold) {
            state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/" + this.getKoboldType(kobold) + ".png");
            state.isCharging = kobold.isCharging();
            state.isDiamond = kobold.isDiamond();
            state.dragonColor = kobold.getDragonColor();
        } else {
            state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/" + this.getKoboldType(null) + ".png");
            if (target instanceof KoboldSkeleton kobold) {
                state.isCharging = kobold.isCharging();
            } else if (target instanceof KoboldZombie kobold) {
                if (kobold.getName().getString().equals("James") && kobold.getZomboType().equals("enchanter")) {
                    state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/zombie_james.png");
                } else {
                    state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/zombie_" + kobold.getZomboType() + ".png");
                }
                state.getZomboType = kobold.getZomboType();
                state.isZomboConverting = kobold.isConvert();
            }
        }
		state.isAggressive = target.isAggressive();
		state.isBlocking = target.isBlocking();
		state.isLeftHanded = target.isLeftHanded();
		state.rightStack = target.getItemHeldByArm(HumanoidArm.RIGHT);
		state.leftStack = target.getItemHeldByArm(HumanoidArm.LEFT);
	}

	@Override
	public void submit(AbstractKoboldState kobold, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
		pose.pushPose();
		pose.translate(-0.025, 0, 0);
		float scale = 0.875F;
		pose.scale(scale, scale, scale);
		super.submit(kobold, pose, buffer, c);
		pose.popPose();
	}

    public abstract String getKoboldType(@Nullable AbstractKoboldEntity kobold);
}