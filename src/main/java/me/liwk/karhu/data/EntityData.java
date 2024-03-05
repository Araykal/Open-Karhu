/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.data;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import java.util.ArrayList;
import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.handler.collision.CollisionBoxParser;
import me.liwk.karhu.handler.collision.enums.Boxes;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import org.bukkit.util.Vector;

public class EntityData {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	public double lastMinX;
	public double lastMaxX;
	public double lastMinY;
	public double lastMaxY;
	public double lastMinZ;
	public double lastMaxZ;
	public double newX;
	public double newY;
	public double newZ;
	public boolean uncertainBox = false;
	public boolean riding;
	public float height = 1.8F;
	public float width = 0.6F;
	public int posIncrements;
	public List<Vector> newLocations = new ArrayList<>();
	public Vector lastUpdate;
	public int flyingsBetween;
	public int eid;
	public int vehicleId;
	public long creationTime;
	public EntityType type;

	public EntityData(double newX, double newY, double newZ, int eid, EntityType type) {
		this.newX = newX;
		this.newY = newY;
		this.newZ = newZ;
		this.newLocations.add(new Vector(newX, newY, newZ));
		this.minX = newX;
		this.minY = newY;
		this.minZ = newZ;
		this.maxX = this.minX;
		this.maxY = this.minY;
		this.maxZ = this.minZ;
		this.posIncrements = 0;
		this.eid = eid;
		this.creationTime = Karhu.getInstance().getServerTick();
		this.type = type;
	}

	public void interpolate() {
		if (this.posIncrements > 0) {
			double newMinX = Double.MAX_VALUE;
			double newMinY = Double.MAX_VALUE;
			double newMinZ = Double.MAX_VALUE;
			double newMaxX = -Double.MAX_VALUE;
			double newMaxY = -Double.MAX_VALUE;
			double newMaxZ = -Double.MAX_VALUE;

			for (Vector vector : this.newLocations) {
				newMinX = Math.min(vector.getX(), newMinX);
				newMinY = Math.min(vector.getY(), newMinY);
				newMinZ = Math.min(vector.getZ(), newMinZ);
				newMaxX = Math.max(vector.getX(), newMaxX);
				newMaxY = Math.max(vector.getY(), newMaxY);
				newMaxZ = Math.max(vector.getZ(), newMaxZ);
			}

			this.updateLast();
			this.minX += (newMinX - this.minX) / (double)this.posIncrements;
			this.maxX += (newMaxX - this.maxX) / (double)this.posIncrements;
			this.minY += (newMinY - this.minY) / (double)this.posIncrements;
			this.maxY += (newMaxY - this.maxY) / (double)this.posIncrements;
			this.minZ += (newMinZ - this.minZ) / (double)this.posIncrements;
			this.maxZ += (newMaxZ - this.maxZ) / (double)this.posIncrements;
		}

		--this.posIncrements;
	}

	public void postTransaction() {
		if (this.newLocations.size() > (Karhu.getInstance().getConfigManager().isReachSafe() ? 2 : 1)) {
			this.newLocations.remove(0);
		}

		this.uncertainBox = this.newLocations.size() > (Karhu.getInstance().getConfigManager().isReachSafe() ? 2 : 1);
	}

	private void updateLast() {
		this.lastMinX = this.minX;
		this.lastMaxX = this.maxX;
		this.lastMinY = this.minY;
		this.lastMaxY = this.maxY;
		this.lastMinZ = this.minZ;
		this.lastMaxZ = this.maxZ;
	}

	public void setSize(float width, float height) {
		if (width != this.width || height != this.height) {
			this.width = width;
			this.height = height;
		}
	}

	public AxisAlignedBB getEntityBoundingBox() {
		Boxes box = CollisionBoxParser.from(this.type);
		float f = box.getWidth();
		float f1 = box.getHeight();
		return new AxisAlignedBB(this.minX - (double)f, this.minY, this.minZ - (double)f, this.maxX + (double)f, this.maxY + (double)f1, this.maxZ + (double)f);
	}

	public AxisAlignedBB getEntityBoundingBoxLast() {
		float f = this.width / 2.0F;
		float f1 = this.height;
		return new AxisAlignedBB(this.lastMinX - (double)f, this.lastMinY, this.lastMinZ - (double)f, this.lastMaxX + (double)f, this.lastMaxY + (double)f1, this.lastMaxZ + (double)f);
	}

	public long getExist() {
		return Karhu.getInstance().getServerTick() - this.creationTime;
	}

	public double getMinX() {
		return this.minX;
	}

	public double getMinY() {
		return this.minY;
	}

	public double getMinZ() {
		return this.minZ;
	}

	public double getMaxX() {
		return this.maxX;
	}

	public double getMaxY() {
		return this.maxY;
	}

	public double getMaxZ() {
		return this.maxZ;
	}

	public double getLastMinX() {
		return this.lastMinX;
	}

	public double getLastMaxX() {
		return this.lastMaxX;
	}

	public double getLastMinY() {
		return this.lastMinY;
	}

	public double getLastMaxY() {
		return this.lastMaxY;
	}

	public double getLastMinZ() {
		return this.lastMinZ;
	}

	public double getLastMaxZ() {
		return this.lastMaxZ;
	}

	public double getNewX() {
		return this.newX;
	}

	public double getNewY() {
		return this.newY;
	}

	public double getNewZ() {
		return this.newZ;
	}

	public boolean isUncertainBox() {
		return this.uncertainBox;
	}

	public boolean isRiding() {
		return this.riding;
	}

	public float getHeight() {
		return this.height;
	}

	public float getWidth() {
		return this.width;
	}

	public int getPosIncrements() {
		return this.posIncrements;
	}

	public List<Vector> getNewLocations() {
		return this.newLocations;
	}

	public Vector getLastUpdate() {
		return this.lastUpdate;
	}

	public int getFlyingsBetween() {
		return this.flyingsBetween;
	}

	public int getEid() {
		return this.eid;
	}

	public int getVehicleId() {
		return this.vehicleId;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public EntityType getType() {
		return this.type;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public void setMinZ(double minZ) {
		this.minZ = minZ;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public void setMaxZ(double maxZ) {
		this.maxZ = maxZ;
	}

	public void setLastMinX(double lastMinX) {
		this.lastMinX = lastMinX;
	}

	public void setLastMaxX(double lastMaxX) {
		this.lastMaxX = lastMaxX;
	}

	public void setLastMinY(double lastMinY) {
		this.lastMinY = lastMinY;
	}

	public void setLastMaxY(double lastMaxY) {
		this.lastMaxY = lastMaxY;
	}

	public void setLastMinZ(double lastMinZ) {
		this.lastMinZ = lastMinZ;
	}

	public void setLastMaxZ(double lastMaxZ) {
		this.lastMaxZ = lastMaxZ;
	}

	public void setNewX(double newX) {
		this.newX = newX;
	}

	public void setNewY(double newY) {
		this.newY = newY;
	}

	public void setNewZ(double newZ) {
		this.newZ = newZ;
	}

	public void setUncertainBox(boolean uncertainBox) {
		this.uncertainBox = uncertainBox;
	}

	public void setRiding(boolean riding) {
		this.riding = riding;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setPosIncrements(int posIncrements) {
		this.posIncrements = posIncrements;
	}

	public void setNewLocations(List<Vector> newLocations) {
		this.newLocations = newLocations;
	}

	public void setLastUpdate(Vector lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setFlyingsBetween(int flyingsBetween) {
		this.flyingsBetween = flyingsBetween;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setType(EntityType type) {
		this.type = type;
	}
}
