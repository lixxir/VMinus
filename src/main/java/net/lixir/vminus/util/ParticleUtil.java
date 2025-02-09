package net.lixir.vminus.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {
	public static Vec3 calculateVelocity(double spawnX, double spawnY, double spawnZ, double targetX, double targetY, double targetZ, double speed) {
		double dirX = targetX - spawnX;
		double dirY = targetY - spawnY;
		double dirZ = targetZ - spawnZ;
		double length = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
		if (length > 0) {
			dirX /= length;
			dirY /= length;
			dirZ /= length;
		}
		return new Vec3(dirX * speed, dirY * speed, dirZ * speed);
	}

	public static void spawnParticlesBetweenPoints(ClientLevel world, Vec3 startPoint, Vec3 endPoint, double particleAmount, SimpleParticleType particleType, float spread) {
		double xStep = (endPoint.x - startPoint.x) / particleAmount;
		double yStep = (endPoint.y - startPoint.y) / particleAmount;
		double zStep = (endPoint.z - startPoint.z) / particleAmount;
		RandomSource random = RandomSource.create();
		for (int i = 0; i < particleAmount; i++) {
			double newX = startPoint.x + xStep * i;
			double newY = startPoint.y + yStep * i;
			double newZ = startPoint.z + zStep * i;
			world.addParticle(particleType, newX, newY, newZ, Mth.nextDouble(random, -spread, spread), Mth.nextDouble(random, -spread, spread), Mth.nextDouble(random, -spread, spread));
		}
	}

	public static void spawnParticlesBetweenPoints(ServerLevel world, Vec3 startPoint, Vec3 endPoint, int particleAmount, SimpleParticleType particleType, float spread) {
		double xStep = (endPoint.x - startPoint.x) / particleAmount;
		double yStep = (endPoint.y - startPoint.y) / particleAmount;
		double zStep = (endPoint.z - startPoint.z) / particleAmount;
		for (int i = 0; i < particleAmount; i++) {
			double newX = startPoint.x + xStep * i;
			double newY = startPoint.y + yStep * i;
			double newZ = startPoint.z + zStep * i;
			world.sendParticles(particleType, newX, newY, newZ, 1, spread, spread, spread, Mth.nextDouble(RandomSource.create(), -0.025, 0.025));
		}
	}

	public static void spawnParticlesBetweenEntities(ServerLevel world, Entity entity1, Entity entity2, int particleAmount, SimpleParticleType particleType, float spread) {
		double xOffset1 = 0;
		double yOffset1 = entity1.getBbHeight() / 2.0;
		double zOffset1 = 0;
		double xOffset2 = 0;
		double yOffset2 = entity2.getBbHeight() / 2.0;
		double zOffset2 = 0;
		Vec3 startPoint = entity1.position().add(xOffset1, yOffset1, zOffset1);
		Vec3 endPoint = entity2.position().add(xOffset2, yOffset2, zOffset2);
		double xStep = (endPoint.x - startPoint.x) / particleAmount;
		double yStep = (endPoint.y - startPoint.y) / particleAmount;
		double zStep = (endPoint.z - startPoint.z) / particleAmount;
		for (int i = 0; i < particleAmount; i++) {
			double newX = startPoint.x + xStep * i;
			double newY = startPoint.y + yStep * i;
			double newZ = startPoint.z + zStep * i;
			world.sendParticles(particleType, newX, newY, newZ, 1, spread, spread, spread, Mth.nextDouble(RandomSource.create(), -0.025, 0.025));
		}
	}
}
