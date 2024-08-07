package net.salju.kobolds.client.model;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class RascalModel<T extends AbstractKoboldEntity> extends KoboldModel<T> {
	public final ModelPart bag;

	public RascalModel(ModelPart root) {
		super(root);
		this.bag = root.getChild("bag");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = KoboldModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bag = partdefinition.addOrReplaceChild("bag", CubeListBuilder.create().texOffs(26, 30).addBox(-1.0F, 0.0F, 2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(33, 7).addBox(-5.0F, -0.85F, -1.5F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T kobold, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.xRot = -0.7854F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = 0.0F;
		this.leftLeg.xRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.bag.xRot = 0.0F;
		this.bag.yRot = 0.0F;
		this.bag.zRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.head.yRot = headYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.hat.yRot = headYaw * ((float) Math.PI / 180F);
		this.hat.xRot = headPitch * ((float) Math.PI / 180F);
		this.rightArm.zRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (this.riding) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (kobold.isPartyKobold()) {
			this.head.xRot += Mth.cos(ageInTicks * 0.4F) * 0.04F + 0.04F;
			this.hat.xRot += Mth.cos(ageInTicks * 0.4F) * 0.04F + 0.04F;
		}
		if (kobold.hasItemInSlot(EquipmentSlot.MAINHAND)) {
			if (kobold.isAggressive()) {
				this.rightArm.xRot = -2.0944F;
				this.rightArm.yRot = 0.1745F;
			}
		}
		if (this.attackTime > 0.0F) {
			if (kobold.isAggressive()) {
				float progress = this.attackTime;
				progress = 1.0F - this.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
			} else {
				float progress = this.attackTime;
				this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
				this.rightArm.yRot += this.body.yRot;
				this.leftArm.yRot += this.body.yRot;
				this.leftArm.xRot += this.body.yRot;
				progress = 1.0F - this.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				float f3 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
				rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 * 1.2D + (double) f3));
				rightArm.yRot += this.body.yRot * 2.0F;
				rightArm.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
			}
		}
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		switch (arm) {
			case LEFT -> {
				this.rightArm.translateAndRotate(poseStack);
				poseStack.translate(-0.045, 0.096, 0.0);
				poseStack.scale(0.75F, 0.75F, 0.75F);
			}
			case RIGHT -> {
				this.rightArm.translateAndRotate(poseStack);
				poseStack.translate(-0.045, 0.096, 0.0);
				poseStack.scale(0.75F, 0.75F, 0.75F);
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		hat.render(poseStack, buffer, packedLight, packedOverlay);
		bag.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		rightArm.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftArm.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}
}