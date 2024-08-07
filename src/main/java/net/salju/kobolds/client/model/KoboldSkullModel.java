package net.salju.kobolds.client.model;

import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.SkullModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldSkullModel extends SkullModelBase {
	private final ModelPart head;

	public KoboldSkullModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = KoboldSkullModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static MeshDefinition createMesh(CubeDeformation cube, float f1) {
		MeshDefinition meshdefinition = SkullModel.createHeadModel();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(22, 0)
				.addBox(-2.5F, -3.0F, -6.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(1, 3).addBox(-0.5F, -3.85F, -5.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftHorn = head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(36, 0).addBox(-0.5F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.5F, -7.0F, 2.0F, -0.6109F, 0.3054F, 0.1745F));
		PartDefinition rightHorn = head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(45, 0).addBox(-1.5F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.5F, -7.0F, 2.0F, -0.6109F, -0.3054F, -0.1745F));
		return meshdefinition;
	}

	@Override
	public void setupAnim(float ani, float yaw, float pitch) {
		this.head.yRot = yaw * ((float) Math.PI / 180);
		this.head.xRot = pitch * ((float) Math.PI / 180);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, buffer, packedLight, packedOverlay);
	}
}