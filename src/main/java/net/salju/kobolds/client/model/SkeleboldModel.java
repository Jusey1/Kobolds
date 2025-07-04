package net.salju.kobolds.client.model;

import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;

public class SkeleboldModel<T extends AbstractKoboldState> extends HumanoidModel<T> {
	public SkeleboldModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = KoboldModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(46, 16).addBox(-2.01F, -0.85F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(33, 16).addBox(0.01F, -0.85F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(13, 31).addBox(-1.0F, -0.01F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 14.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 31).addBox(-1.0F, -0.01F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 14.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 15).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 15).addBox(-0.5F, 6.0F, -4.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T kobold) {
		this.rightArm.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * kobold.walkAnimationSpeed * 0.5F;
		this.leftArm.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F) * 2.0F * kobold.walkAnimationSpeed * 0.5F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F) * 1.4F * kobold.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * kobold.walkAnimationSpeed;
		this.head.yRot = kobold.yRot * ((float) Math.PI / 180F);
		this.head.xRot = kobold.xRot * ((float) Math.PI / 180F);
		this.rightArm.zRot += Mth.cos(kobold.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.zRot -= Mth.cos(kobold.ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (kobold.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (!kobold.getMainHandItem().isEmpty()) {
			if (kobold.isAggressive) {
				if (kobold.isLeftHanded) {
					if (kobold.getMainhandItem().getItem() instanceof BowItem) {
						this.leftArm.xRot = -1.4399F;
						this.leftArm.yRot = 0.2618F;
						this.rightArm.xRot = -1.3963F;
						this.rightArm.yRot = -0.3054F;
					} else {
						this.leftArm.xRot = -2.0944F;
						this.leftArm.yRot = -0.1745F;
					}
				} else {
					if (kobold.getMainhandItem().getItem() instanceof BowItem) {
						this.rightArm.xRot = -1.4399F;
						this.rightArm.yRot = -0.2618F;
						this.leftArm.xRot = -1.3963F;
						this.leftArm.yRot = 0.3054F;
					} else {
						this.rightArm.xRot = -2.0944F;
						this.rightArm.yRot = 0.1745F;
					}
				}
			} else if (kobold.getMainhandItem().getItem() instanceof CrossbowItem) {
				if (kobold.isLeftHanded) {
					if (kobold.isCharging) {
						this.leftArm.xRot = -0.6981F;
						this.leftArm.yRot = 0.3491F;
						this.rightArm.xRot = -1.1345F;
						this.rightArm.yRot = -0.5672F;
					} else if (CrossbowItem.isCharged(kobold.getMainhandItem())) {
						this.leftArm.xRot = -1.4399F;
						this.leftArm.yRot = 0.2618F;
						this.rightArm.xRot = -1.3963F;
						this.rightArm.yRot = -0.3054F;
					}
				} else {
					if (kobold.isCharging) {
						this.rightArm.xRot = -0.6981F;
						this.rightArm.yRot = -0.3491F;
						this.leftArm.xRot = -1.1345F;
						this.leftArm.yRot = 0.5672F;
					} else if (CrossbowItem.isCharged(kobold.getMainhandItem())) {
						this.rightArm.xRot = -1.4399F;
						this.rightArm.yRot = -0.2618F;
						this.leftArm.xRot = -1.3963F;
						this.leftArm.yRot = 0.3054F;
					}
				}
			}
		}
		if (kobold.attackTime > 0.0F) {
			if (kobold.isLeftHanded) {
				float progress = kobold.attackTime;
				progress = 1.0F - kobold.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
			} else {
				float progress = kobold.attackTime;
				progress = 1.0F - kobold.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
			}
		}
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT -> {
				this.leftArm.translateAndRotate(pose);
				pose.translate(0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
			case RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
		}
	}
}