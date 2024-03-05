/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistA;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistB;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistC;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistD;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistE;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistF;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistG;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistH;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistI;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistJ;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistM;
import me.liwk.karhu.check.impl.combat.aimassist.AimAssistN;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisA;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisB;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisC;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisD;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisE;
import me.liwk.karhu.check.impl.combat.aimassist.analysis.AnalysisF;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerA;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerB;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerC;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerD;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerE;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerF;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerG;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerH;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerI;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerJ;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerK;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerL;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerP;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerU;
import me.liwk.karhu.check.impl.combat.autoclicker.AutoClickerW;
import me.liwk.karhu.check.impl.combat.hitbox.HitboxA;
import me.liwk.karhu.check.impl.combat.killaura.KillauraA;
import me.liwk.karhu.check.impl.combat.killaura.KillauraB;
import me.liwk.karhu.check.impl.combat.killaura.KillauraC;
import me.liwk.karhu.check.impl.combat.killaura.KillauraE;
import me.liwk.karhu.check.impl.combat.killaura.KillauraF;
import me.liwk.karhu.check.impl.combat.killaura.KillauraG;
import me.liwk.karhu.check.impl.combat.killaura.KillauraH;
import me.liwk.karhu.check.impl.combat.killaura.KillauraI;
import me.liwk.karhu.check.impl.combat.killaura.KillauraJ;
import me.liwk.karhu.check.impl.combat.killaura.KillauraK;
import me.liwk.karhu.check.impl.combat.killaura.KillauraM;
import me.liwk.karhu.check.impl.combat.killaura.KillauraN;
import me.liwk.karhu.check.impl.combat.reach.ReachA;
import me.liwk.karhu.check.impl.combat.velocity.VelocityA;
import me.liwk.karhu.check.impl.combat.velocity.VelocityB;
import me.liwk.karhu.check.impl.mouse.Mouse;
import me.liwk.karhu.check.impl.mouse.Sensitivity;
import me.liwk.karhu.check.impl.movement.fly.FlyA;
import me.liwk.karhu.check.impl.movement.fly.FlyB;
import me.liwk.karhu.check.impl.movement.fly.FlyC;
import me.liwk.karhu.check.impl.movement.fly.FlyD;
import me.liwk.karhu.check.impl.movement.fly.FlyE;
import me.liwk.karhu.check.impl.movement.fly.FlyF;
import me.liwk.karhu.check.impl.movement.inventory.InventoryA;
import me.liwk.karhu.check.impl.movement.inventory.InventoryB;
import me.liwk.karhu.check.impl.movement.motion.MotionA;
import me.liwk.karhu.check.impl.movement.motion.MotionB;
import me.liwk.karhu.check.impl.movement.motion.MotionC;
import me.liwk.karhu.check.impl.movement.motion.MotionD;
import me.liwk.karhu.check.impl.movement.motion.MotionE;
import me.liwk.karhu.check.impl.movement.motion.MotionF;
import me.liwk.karhu.check.impl.movement.motion.MotionI;
import me.liwk.karhu.check.impl.movement.motion.MotionJ;
import me.liwk.karhu.check.impl.movement.omnisprint.OmniSprintA;
import me.liwk.karhu.check.impl.movement.speed.SpeedA;
import me.liwk.karhu.check.impl.movement.speed.SpeedB;
import me.liwk.karhu.check.impl.movement.speed.SpeedC;
import me.liwk.karhu.check.impl.movement.step.StepA;
import me.liwk.karhu.check.impl.movement.vehicle.VehicleFly;
import me.liwk.karhu.check.impl.movement.water.JesusA;
import me.liwk.karhu.check.impl.movement.water.JesusB;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsA;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsB;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsC;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsD;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsE;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsF;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsG;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsH;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsI;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsJ;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsK;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsM;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsN;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsO;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsQ;
import me.liwk.karhu.check.impl.packet.badpackets.BadPacketsR;
import me.liwk.karhu.check.impl.packet.pingspoof.PingA;
import me.liwk.karhu.check.impl.packet.timer.TimerA;
import me.liwk.karhu.check.impl.packet.timer.TimerB;
import me.liwk.karhu.check.impl.packet.timer.TimerC;
import me.liwk.karhu.check.impl.world.block.BlockReach;
import me.liwk.karhu.check.impl.world.block.FastBreakA;
import me.liwk.karhu.check.impl.world.block.FastBreakB;
import me.liwk.karhu.check.impl.world.block.FastBreakC;
import me.liwk.karhu.check.impl.world.block.GhostBreak;
import me.liwk.karhu.check.impl.world.block.NoLookBreak;
import me.liwk.karhu.check.impl.world.ground.GroundA;
import me.liwk.karhu.check.impl.world.ground.GroundB;
import me.liwk.karhu.check.impl.world.ground.GroundC;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldA;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldB;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldC;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldD;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldE;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldF;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldG;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldH;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldI;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldJ;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldK;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldL;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldM;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldN;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldO;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldP;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldQ;
import me.liwk.karhu.check.impl.world.scaffold.ScaffoldR;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.check.type.RotationCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.APICaller;
import me.liwk.karhu.util.benchmark.Benchmark;
import me.liwk.karhu.util.benchmark.BenchmarkType;
import me.liwk.karhu.util.benchmark.KarhuBenchmarker;

public final class CheckManager {
	private final Check[] checks;
	private final KarhuPlayer kp;
	private final List<RotationCheck> rotationChecks;
	private final List<PositionCheck> positionChecks;
	private final List<PacketCheck> packetChecks;

	public CheckManager(KarhuPlayer karhuPlayer, Karhu karhu) {
		this.kp = karhuPlayer;
		List<Check> c = Arrays.asList(
			new AutoClickerA(karhuPlayer, karhu),
			new AutoClickerB(karhuPlayer, karhu),
			new AutoClickerC(karhuPlayer, karhu),
			new AutoClickerD(karhuPlayer, karhu),
			new AutoClickerE(karhuPlayer, karhu),
			new AutoClickerF(karhuPlayer, karhu),
			new AutoClickerG(karhuPlayer, karhu),
			new AutoClickerH(karhuPlayer, karhu),
			new AutoClickerI(karhuPlayer, karhu),
			new AutoClickerJ(karhuPlayer, karhu),
			new AutoClickerK(karhuPlayer, karhu),
			new AutoClickerL(karhuPlayer, karhu),
			new AutoClickerP(karhuPlayer, karhu),
			new AutoClickerU(karhuPlayer, karhu),
			new AutoClickerW(karhuPlayer, karhu),
			new VelocityA(karhuPlayer, karhu),
			new VelocityB(karhuPlayer, karhu),
			new ReachA(karhuPlayer, karhu),
			new HitboxA(karhuPlayer, karhu),
			new AimAssistA(karhuPlayer, karhu),
			new AimAssistB(karhuPlayer, karhu),
			new AimAssistC(karhuPlayer, karhu),
			new AimAssistD(karhuPlayer, karhu),
			new AimAssistE(karhuPlayer, karhu),
			new AimAssistF(karhuPlayer, karhu),
			new AimAssistG(karhuPlayer, karhu),
			new AimAssistH(karhuPlayer, karhu),
			new AimAssistI(karhuPlayer, karhu),
			new AimAssistJ(karhuPlayer, karhu),
			new AimAssistM(karhuPlayer, karhu),
			new AimAssistN(karhuPlayer, karhu),
			new AnalysisA(karhuPlayer, karhu),
			new AnalysisB(karhuPlayer, karhu),
			new AnalysisC(karhuPlayer, karhu),
			new AnalysisD(karhuPlayer, karhu),
			new AnalysisE(karhuPlayer, karhu),
			new AnalysisF(karhuPlayer, karhu),
			new KillauraA(karhuPlayer, karhu),
			new KillauraB(karhuPlayer, karhu),
			new KillauraC(karhuPlayer, karhu),
			new KillauraE(karhuPlayer, karhu),
			new KillauraF(karhuPlayer, karhu),
			new KillauraG(karhuPlayer, karhu),
			new KillauraH(karhuPlayer, karhu),
			new KillauraI(karhuPlayer, karhu),
			new KillauraJ(karhuPlayer, karhu),
			new KillauraK(karhuPlayer, karhu),
			new KillauraM(karhuPlayer, karhu),
			new KillauraN(karhuPlayer, karhu),
			new FlyA(karhuPlayer, karhu),
			new FlyB(karhuPlayer, karhu),
			new FlyC(karhuPlayer, karhu),
			new FlyD(karhuPlayer, karhu),
			new FlyE(karhuPlayer, karhu),
			new FlyF(karhuPlayer, karhu),
			new VehicleFly(karhuPlayer, karhu),
			new MotionA(karhuPlayer, karhu),
			new MotionB(karhuPlayer, karhu),
			new MotionC(karhuPlayer, karhu),
			new MotionD(karhuPlayer, karhu),
			new MotionE(karhuPlayer, karhu),
			new MotionF(karhuPlayer, karhu),
			new MotionI(karhuPlayer, karhu),
			new MotionJ(karhuPlayer, karhu),
			new StepA(karhuPlayer, karhu),
			new SpeedA(karhuPlayer, karhu),
			new SpeedB(karhuPlayer, karhu),
			new SpeedC(karhuPlayer, karhu),
			new OmniSprintA(karhuPlayer, karhu),
			new JesusA(karhuPlayer, karhu),
			new JesusB(karhuPlayer, karhu),
			new InventoryA(karhuPlayer, karhu),
			new InventoryB(karhuPlayer, karhu),
			new BadPacketsA(karhuPlayer, karhu),
			new BadPacketsB(karhuPlayer, karhu),
			new BadPacketsC(karhuPlayer, karhu),
			new BadPacketsD(karhuPlayer, karhu),
			new BadPacketsE(karhuPlayer, karhu),
			new BadPacketsF(karhuPlayer, karhu),
			new BadPacketsG(karhuPlayer, karhu),
			new BadPacketsH(karhuPlayer, karhu),
			new BadPacketsI(karhuPlayer, karhu),
			new BadPacketsJ(karhuPlayer, karhu),
			new BadPacketsK(karhuPlayer, karhu),
			new BadPacketsM(karhuPlayer, karhu),
			new BadPacketsN(karhuPlayer, karhu),
			new BadPacketsO(karhuPlayer, karhu),
			new BadPacketsQ(karhuPlayer, karhu),
			new BadPacketsR(karhuPlayer, karhu),
			new PingA(karhuPlayer, karhu),
			new TimerA(karhuPlayer, karhu),
			new TimerB(karhuPlayer, karhu),
			new TimerC(karhuPlayer, karhu),
			new ScaffoldA(karhuPlayer, karhu),
			new ScaffoldB(karhuPlayer, karhu),
			new ScaffoldC(karhuPlayer, karhu),
			new ScaffoldD(karhuPlayer, karhu),
			new ScaffoldE(karhuPlayer, karhu),
			new ScaffoldF(karhuPlayer, karhu),
			new ScaffoldG(karhuPlayer, karhu),
			new ScaffoldH(karhuPlayer, karhu),
			new ScaffoldI(karhuPlayer, karhu),
			new ScaffoldJ(karhuPlayer, karhu),
			new ScaffoldK(karhuPlayer, karhu),
			new ScaffoldL(karhuPlayer, karhu),
			new ScaffoldM(karhuPlayer, karhu),
			new ScaffoldN(karhuPlayer, karhu),
			new ScaffoldO(karhuPlayer, karhu),
			new ScaffoldP(karhuPlayer, karhu),
			new ScaffoldQ(karhuPlayer, karhu),
			new ScaffoldR(karhuPlayer, karhu),
			new FastBreakA(karhuPlayer, karhu),
			new FastBreakB(karhuPlayer, karhu),
			new FastBreakC(karhuPlayer, karhu),
			new GhostBreak(karhuPlayer, karhu),
			new BlockReach(karhuPlayer, karhu),
			new NoLookBreak(karhuPlayer, karhu),
			new GroundA(karhuPlayer, karhu),
			new GroundB(karhuPlayer, karhu),
			new GroundC(karhuPlayer, karhu),
			new Sensitivity(karhuPlayer, karhu),
			new Mouse(karhuPlayer, karhu)
		);
		this.checks = c.toArray(new Check[0]);
		this.packetChecks = this.getAllOfType(PacketCheck.class);
		this.positionChecks = this.getAllOfType(PositionCheck.class);
		this.rotationChecks = this.getAllOfType(RotationCheck.class);
	}

	public void runChecks(List<? extends Check> paskat, Object e, Object packet) {
		long start = System.nanoTime();

		for (Check c : paskat) {
			if (Karhu.getInstance().getCheckState().isEnabled(c.getName()) || c.isSilent()) {
				if (Karhu.isAPIAvailable()) {
					if (APICaller.callPreCheck(c.getCheckInfo(), c, this.kp.getBukkitPlayer(), packet)) {
						c.setDidFail(false);
						c.handle(e);
						APICaller.callPostCheck(this.kp.getBukkitPlayer(), c.getCheckInfo(), c, packet);
					}
				} else {
					c.setDidFail(false);
					c.handle(e);
				}
			}
		}

		Benchmark profileData = KarhuBenchmarker.getProfileData(BenchmarkType.CHECKS);
		profileData.insertResult(start, System.nanoTime());
	}

	public Check[] getChecks() {
		return this.checks;
	}

	public int checkAmount() {
		return this.checks.length;
	}

	public <T> T getCheck(Class<T> clazz) {
		return (T)Arrays.stream(this.checks).filter(check -> check.getClass() == clazz).findFirst().orElse(null);
	}

	private <T> List<T> getAllOfType(Class<T> clazz) {
		return (List<T>) Arrays.stream(this.checks).filter(clazz::isInstance).collect(Collectors.toList());
	}

	public List<RotationCheck> getRotationChecks() {
		return this.rotationChecks;
	}

	public List<PositionCheck> getPositionChecks() {
		return this.positionChecks;
	}

	public List<PacketCheck> getPacketChecks() {
		return this.packetChecks;
	}
}
