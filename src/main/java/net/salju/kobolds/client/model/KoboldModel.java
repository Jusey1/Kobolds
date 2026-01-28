package net.salju.kobolds.client.model;

import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.*;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldModel<T extends AbstractKoboldState> extends HumanoidModel<T> {
	public KoboldModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = KoboldModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		return LayerDefinition.create(mesh, 64, 64);
	}

	public static MeshDefinition createMesh(CubeDeformation cube, float f) {
		MeshDefinition mesh = HumanoidModel.createMesh(cube, f);
		PartDefinition root = mesh.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(22, 0).addBox(-2.5F, -3.0F, -6.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(1, 3).addBox(-0.5F, -3.85F, -5.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -0.5F));
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 50).addBox(-3.5F, -12.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.5F)).texOffs(17, 52).addBox(-6.0F, -10.215F, -5.5F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -0.5F));
		head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(36, 0).addBox(-0.5F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -7.0F, 2.0F, -0.6109F, 0.3054F, 0.1745F));
		head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(45, 0).addBox(-1.5F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.0F, 2.0F, -0.6109F, -0.3054F, -0.1745F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(46, 16).addBox(-3.0F, -0.85F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(33, 16).addBox(0.0F, -0.85F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(13, 31).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 14.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 31).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 14.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 15).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 15).addBox(-1.0F, 6.0F, -4.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));
		return mesh;
	}

    @Override
    public void translateToHand(AbstractKoboldState state, HumanoidArm arm, PoseStack pose) {
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

	@Override
	public void setupAnim(T target) {
		this.defaultPose(target);
		this.poseArms(target, target.getMainHandItemStack().getItem(), target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
		if (target.attackTime > 0.0F) {
			this.setupAttackAnimation(target);
		}
	}

	@Override
	protected void setupAttackAnimation(T target) {
		if (target.isAggressive) {
			switch (target.swingAnimationType) {
				case WHACK:
					ModelPart arm = this.getArm(target.attackArm);
					float progress = 1.0F - target.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					arm.xRot = (float) ((double) arm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
				case NONE:
				case STAB:
					SpearAnimations.thirdPersonAttackHand(this, target);
				default:
					break;
			}
		} else {
			float progress = target.attackTime;
			this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			progress = 1.0F - target.attackTime;
			progress = progress * progress;
			progress = progress * progress;
			progress = 1.0F - progress;
			float f2 = Mth.sin(progress * (float) Math.PI);
			float f3 = Mth.sin(target.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 * 1.2D + (double) f3));
			rightArm.yRot += this.body.yRot * 2.0F;
			rightArm.zRot += Mth.sin(target.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	protected void defaultPose(T target) {
		this.rightArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * target.walkAnimationSpeed * 0.5F;
		this.leftArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 2.0F * target.walkAnimationSpeed * 0.5F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 1.4F * target.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * target.walkAnimationSpeed;
		this.head.yRot = target.yRot * ((float) Math.PI / 180F);
		this.head.xRot = target.xRot * ((float) Math.PI / 180F);
		this.rightArm.zRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.zRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (target.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
	}

	protected void poseArms(T target, Item item, ModelPart mainArm, ModelPart offArm) {
		if (target.isAggressive) {
			if (item instanceof CrossbowItem || item instanceof BowItem) {
				if (target.isCharging) {
					mainArm.xRot = -0.6981F;
					mainArm.yRot = target.isLeftHanded ? 0.3491F : -0.3491F;
					offArm.xRot = -1.1345F;
					offArm.yRot = target.isLeftHanded ? -0.5672F : 0.5672F;
				} else {
					mainArm.xRot = -1.4399F;
					mainArm.yRot = target.isLeftHanded ? 0.2618F : -0.2618F;
					offArm.xRot = -1.3963F;
					offArm.yRot = target.isLeftHanded ? -0.3054F : 0.3054F;
				}
			} else if (item instanceof TridentItem) {
			   mainArm.xRot = 2.8798F;
			   offArm.xRot = 0.0F;
			} else {
				mainArm.xRot = target.getMainHandItemStack().has(DataComponents.KINETIC_WEAPON) ? -1.0422F : -2.0944F;
				mainArm.yRot = target.isLeftHanded ? -0.1745F : 0.1745F;
			}
		}
		if (target.isBlocking) {
			offArm.xRot = -0.6981F;
			offArm.yRot = target.isLeftHanded ? -0.2618F : 0.2618F;
		} else if (target.hasOffhandItem) {
			offArm.xRot = -0.8727F;
			offArm.yRot = 0.0873F;
			this.head.xRot = 0.1745F;
		}
	}
}