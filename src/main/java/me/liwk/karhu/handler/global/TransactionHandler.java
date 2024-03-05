/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.pending.VelocityPending;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.ChatColor;

public class TransactionHandler {
	public void handlePlayReceive(PacketPlayReceiveEvent e, long nanoTime, KarhuPlayer data) {
		Client type = e.getPacketType();
		long now = e.getTimestamp();
		switch (type) {
			case PONG:
			case WINDOW_CONFIRMATION:
				short number;
				if (type == Client.PONG) {
					number = (short)new WrapperPlayClientPong(e).getId();
				} else {
					number = new WrapperPlayClientWindowConfirmation(e).getActionId();
				}

				data.setLastLastClientTransaction(data.getLastClientTransaction());
				data.setLastClientTransaction(data.getCurrentClientTransaction());
				if (number <= -3000) {
					data.setCurrentClientTransaction(number);
				}

				if (data.getScheduledTransactions().containsKey(number)) {
					((Consumer)data.getScheduledTransactions().remove(number)).accept(number);
					++data.receivedConfirms;
				}

				ObjectArrayList<Consumer<Short>> packetList = data.getWaitingConfirms().get(number);
				if (packetList != null && !packetList.isEmpty()) {
					ObjectListIterator slots = packetList.iterator();

					while (slots.hasNext()) {
						Consumer<Short> consumer = (Consumer)slots.next();
						consumer.accept(number);
					}

					packetList.clear();
					data.getWaitingConfirms().remove(number);
				}

				Deque<Integer> slots = data.getBackSwitchSlots().get(Integer.valueOf(number));
				if (data.isPendingBackSwitch() && slots != null) {
					data.setPendingBackSwitch(false);
					if (slots.peekFirst() != null) {
						int slot = slots.peekFirst();
						PlayerUtil.sendPacket(data.getBukkitPlayer(), new WrapperPlayServerHeldItemChange(slot));
					}

					slots.remove(Integer.valueOf(number));
				}

				if (data.getTransactionTime().containsKey(number)) {
					long transactionStamp = data.getTransactionTime().get(number);
					if (!data.isHasReceivedTransaction()) {
						data.setTransactionClock(transactionStamp);
					}

					data.setHasReceivedTransaction(true);
					data.setLastTransactionPing(data.getTransactionPing());
					data.setTransactionPing(TimeUnit.NANOSECONDS.toMillis(nanoTime - transactionStamp));
					data.getTransactionTime().remove(number);
					data.setLastTransactionPingUpdate(transactionStamp);
					data.setPingInTicks(Math.min(15, MathUtil.getPingInTicks(data.getTransactionPing() + 50L)));
				}

				data.getNetHandler().handleClientTransaction(number);
				data.setLastTransaction(now);
		}
	}

	public void handlePacketPlaySend(PacketPlaySendEvent e, long nanoTime, KarhuPlayer data) {
		// $QF: Couldn't be decompiled
		// Please report this to the Quiltflower issue tracker, at https://github.com/QuiltMC/quiltflower/issues with a copy of the class file (if you have the rights to distribute it!)
		// java.lang.NullPointerException: Cannot invoke "String.equals(Object)" because the return value of "java.util.Map$Entry.getValue()" is null
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingExprent(VarDefinitionHelper.java:1578)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1449)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1492)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1492)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1492)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1492)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.remapClashingNames(VarDefinitionHelper.java:1426)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.setVarDefinitions(VarDefinitionHelper.java:244)
		//   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.setVarDefinitions(VarProcessor.java:51)
		//   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:409)
		//
		// Bytecode:
		// 000: aload 1
		// 001: invokevirtual com/github/retrooper/packetevents/event/simple/PacketPlaySendEvent.getPacketType ()Lcom/github/retrooper/packetevents/protocol/packettype/PacketType$Play$Server;
		// 004: astore 5
		// 006: aload 4
		// 008: invokevirtual me/liwk/karhu/data/KarhuPlayer.isObjectLoaded ()Z
		// 00b: ifeq 6ed
		// 00e: getstatic me/liwk/karhu/handler/global/TransactionHandler$1.$SwitchMap$com$github$retrooper$packetevents$protocol$packettype$PacketType$Play$Server [I
		// 011: aload 5
		// 013: invokevirtual com/github/retrooper/packetevents/protocol/packettype/PacketType$Play$Server.ordinal ()I
		// 016: iaload
		// 017: tableswitch 1750 1 27 121 414 504 581 608 608 743 806 869 932 989 1023 1063 1112 1127 1142 1213 1254 1295 1403 1465 1503 1526 1553 1702 1729 1744
		// 090: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook
		// 093: dup
		// 094: aload 1
		// 095: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 098: astore 6
		// 09a: new com/github/retrooper/packetevents/util/Vector3d
		// 09d: dup
		// 09e: aload 6
		// 0a0: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.getX ()D
		// 0a3: aload 6
		// 0a5: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.getY ()D
		// 0a8: aload 6
		// 0aa: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.getZ ()D
		// 0ad: invokespecial com/github/retrooper/packetevents/util/Vector3d.<init> (DDD)V
		// 0b0: astore 7
		// 0b2: aload 4
		// 0b4: invokevirtual me/liwk/karhu/data/KarhuPlayer.getLocation ()Lme/liwk/karhu/util/location/CustomLocation;
		// 0b7: astore 8
		// 0b9: aload 4
		// 0bb: invokevirtual me/liwk/karhu/data/KarhuPlayer.isNewerThan8 ()Z
		// 0be: ifne 145
		// 0c1: aload 6
		// 0c3: getstatic com/github/retrooper/packetevents/protocol/teleport/RelativeFlag.X Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;
		// 0c6: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.isRelativeFlag (Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;)Z
		// 0c9: ifeq 0e1
		// 0cc: aload 7
		// 0ce: new com/github/retrooper/packetevents/util/Vector3d
		// 0d1: dup
		// 0d2: aload 8
		// 0d4: getfield me/liwk/karhu/util/location/CustomLocation.x D
		// 0d7: dconst_0
		// 0d8: dconst_0
		// 0d9: invokespecial com/github/retrooper/packetevents/util/Vector3d.<init> (DDD)V
		// 0dc: invokevirtual com/github/retrooper/packetevents/util/Vector3d.add (Lcom/github/retrooper/packetevents/util/Vector3d;)Lcom/github/retrooper/packetevents/util/Vector3d;
		// 0df: astore 7
		// 0e1: aload 6
		// 0e3: getstatic com/github/retrooper/packetevents/protocol/teleport/RelativeFlag.Y Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;
		// 0e6: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.isRelativeFlag (Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;)Z
		// 0e9: ifeq 101
		// 0ec: aload 7
		// 0ee: new com/github/retrooper/packetevents/util/Vector3d
		// 0f1: dup
		// 0f2: dconst_0
		// 0f3: aload 8
		// 0f5: getfield me/liwk/karhu/util/location/CustomLocation.y D
		// 0f8: dconst_0
		// 0f9: invokespecial com/github/retrooper/packetevents/util/Vector3d.<init> (DDD)V
		// 0fc: invokevirtual com/github/retrooper/packetevents/util/Vector3d.add (Lcom/github/retrooper/packetevents/util/Vector3d;)Lcom/github/retrooper/packetevents/util/Vector3d;
		// 0ff: astore 7
		// 101: aload 6
		// 103: getstatic com/github/retrooper/packetevents/protocol/teleport/RelativeFlag.Z Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;
		// 106: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.isRelativeFlag (Lcom/github/retrooper/packetevents/protocol/teleport/RelativeFlag;)Z
		// 109: ifeq 121
		// 10c: aload 7
		// 10e: new com/github/retrooper/packetevents/util/Vector3d
		// 111: dup
		// 112: dconst_0
		// 113: dconst_0
		// 114: aload 8
		// 116: getfield me/liwk/karhu/util/location/CustomLocation.z D
		// 119: invokespecial com/github/retrooper/packetevents/util/Vector3d.<init> (DDD)V
		// 11c: invokevirtual com/github/retrooper/packetevents/util/Vector3d.add (Lcom/github/retrooper/packetevents/util/Vector3d;)Lcom/github/retrooper/packetevents/util/Vector3d;
		// 11f: astore 7
		// 121: aload 6
		// 123: aload 7
		// 125: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getX ()D
		// 128: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.setX (D)V
		// 12b: aload 6
		// 12d: aload 7
		// 12f: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 132: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.setY (D)V
		// 135: aload 6
		// 137: aload 7
		// 139: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getZ ()D
		// 13c: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.setZ (D)V
		// 13f: aload 6
		// 141: bipush 0
		// 142: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.setRelativeMask (B)V
		// 145: getstatic me/liwk/karhu/Karhu.SERVER_VERSION Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 148: getstatic com/github/retrooper/packetevents/manager/server/ServerVersion.V_1_7_10 Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 14b: invokevirtual com/github/retrooper/packetevents/manager/server/ServerVersion.isOlderThanOrEquals (Lcom/github/retrooper/packetevents/manager/server/ServerVersion;)Z
		// 14e: istore 9
		// 150: aload 7
		// 152: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getX ()D
		// 155: dstore 10
		// 157: iload 9
		// 159: ifne 164
		// 15c: aload 7
		// 15e: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 161: goto 16d
		// 164: aload 7
		// 166: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 169: ldc2_w 1.6200000047683716
		// 16c: dsub
		// 16d: dstore 12
		// 16f: aload 7
		// 171: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getZ ()D
		// 174: dstore 14
		// 176: new me/liwk/karhu/util/Teleport
		// 179: dup
		// 17a: new me/liwk/karhu/util/TeleportPosition
		// 17d: dup
		// 17e: dload 10
		// 180: dload 12
		// 182: dload 14
		// 184: invokespecial me/liwk/karhu/util/TeleportPosition.<init> (DDD)V
		// 187: invokespecial me/liwk/karhu/util/Teleport.<init> (Lme/liwk/karhu/util/TeleportPosition;)V
		// 18a: astore 16
		// 18c: aload 4
		// 18e: aload 4
		// 190: aload 16
		// 192: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lme/liwk/karhu/util/Teleport;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$0 (Lme/liwk/karhu/data/KarhuPlayer;Lme/liwk/karhu/util/Teleport;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 197: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 19a: aload 4
		// 19c: aload 4
		// 19e: aload 16
		// 1a0: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lme/liwk/karhu/util/Teleport;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$2 (Lme/liwk/karhu/data/KarhuPlayer;Lme/liwk/karhu/util/Teleport;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 1a5: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPostPing (Lme/liwk/karhu/util/gui/Callback;)V
		// 1a8: aload 4
		// 1aa: aload 4
		// 1ac: invokevirtual me/liwk/karhu/data/KarhuPlayer.getServerTick ()J
		// 1af: invokevirtual me/liwk/karhu/data/KarhuPlayer.setLastTeleportPacket (J)V
		// 1b2: goto 6ed
		// 1b5: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityVelocity
		// 1b8: dup
		// 1b9: aload 1
		// 1ba: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityVelocity.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 1bd: astore 6
		// 1bf: aload 6
		// 1c1: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityVelocity.getVelocity ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 1c4: astore 7
		// 1c6: aload 6
		// 1c8: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityVelocity.getEntityId ()I
		// 1cb: aload 1
		// 1cc: invokevirtual com/github/retrooper/packetevents/event/simple/PacketPlaySendEvent.getUser ()Lcom/github/retrooper/packetevents/protocol/player/User;
		// 1cf: invokevirtual com/github/retrooper/packetevents/protocol/player/User.getEntityId ()I
		// 1d2: if_icmpne 6ed
		// 1d5: aload 4
		// 1d7: invokevirtual me/liwk/karhu/data/KarhuPlayer.getCurrentServerTransaction ()I
		// 1da: istore 8
		// 1dc: aload 4
		// 1de: bipush 1
		// 1df: invokevirtual me/liwk/karhu/data/KarhuPlayer.setConfirmingVelocity (Z)V
		// 1e2: aload 4
		// 1e4: iload 8
		// 1e6: aload 7
		// 1e8: aload 4
		// 1ea: invokedynamic call (ILcom/github/retrooper/packetevents/util/Vector3d;Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$3 (ILcom/github/retrooper/packetevents/util/Vector3d;Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 1ef: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 1f2: aload 4
		// 1f4: aload 4
		// 1f6: iload 8
		// 1f8: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;I)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$4 (Lme/liwk/karhu/data/KarhuPlayer;ILjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 1fd: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPostPing (Lme/liwk/karhu/util/gui/Callback;)V
		// 200: aload 4
		// 202: bipush 0
		// 203: invokevirtual me/liwk/karhu/data/KarhuPlayer.setPlayerVelocityCalled (Z)V
		// 206: aload 4
		// 208: bipush 0
		// 209: invokevirtual me/liwk/karhu/data/KarhuPlayer.setPlayerExplodeCalled (Z)V
		// 20c: goto 6ed
		// 20f: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerExplosion
		// 212: dup
		// 213: aload 1
		// 214: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerExplosion.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 217: astore 8
		// 219: aload 8
		// 21b: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerExplosion.getPlayerMotion ()Lcom/github/retrooper/packetevents/util/Vector3f;
		// 21e: astore 9
		// 220: aload 9
		// 222: invokevirtual com/github/retrooper/packetevents/util/Vector3f.getX ()F
		// 225: fconst_0
		// 226: fcmpl
		// 227: ifne 23f
		// 22a: aload 9
		// 22c: invokevirtual com/github/retrooper/packetevents/util/Vector3f.getY ()F
		// 22f: fconst_0
		// 230: fcmpl
		// 231: ifne 23f
		// 234: aload 9
		// 236: invokevirtual com/github/retrooper/packetevents/util/Vector3f.getZ ()F
		// 239: fconst_0
		// 23a: fcmpl
		// 23b: ifne 23f
		// 23e: return
		// 23f: aload 4
		// 241: aload 4
		// 243: aload 9
		// 245: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/util/Vector3f;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$5 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/util/Vector3f;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 24a: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 24d: aload 4
		// 24f: aload 4
		// 251: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$6 (Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 256: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPostPing (Lme/liwk/karhu/util/gui/Callback;)V
		// 259: goto 6ed
		// 25c: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRotation
		// 25f: dup
		// 260: aload 1
		// 261: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRotation.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 264: astore 10
		// 266: aload 4
		// 268: aload 4
		// 26a: aload 10
		// 26c: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRotation;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$7 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRotation;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 271: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 274: goto 6ed
		// 277: aload 5
		// 279: getstatic com/github/retrooper/packetevents/protocol/packettype/PacketType$Play$Server.ENTITY_RELATIVE_MOVE Lcom/github/retrooper/packetevents/protocol/packettype/PacketType$Play$Server;
		// 27c: if_acmpne 2a8
		// 27f: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove
		// 282: dup
		// 283: aload 1
		// 284: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 287: astore 18
		// 289: aload 18
		// 28b: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.getEntityId ()I
		// 28e: istore 11
		// 290: aload 18
		// 292: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.getDeltaX ()D
		// 295: dstore 12
		// 297: aload 18
		// 299: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.getDeltaY ()D
		// 29c: dstore 14
		// 29e: aload 18
		// 2a0: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.getDeltaZ ()D
		// 2a3: dstore 16
		// 2a5: goto 2ce
		// 2a8: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation
		// 2ab: dup
		// 2ac: aload 1
		// 2ad: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 2b0: astore 18
		// 2b2: aload 18
		// 2b4: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.getEntityId ()I
		// 2b7: istore 11
		// 2b9: aload 18
		// 2bb: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.getDeltaX ()D
		// 2be: dstore 12
		// 2c0: aload 18
		// 2c2: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.getDeltaY ()D
		// 2c5: dstore 14
		// 2c7: aload 18
		// 2c9: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.getDeltaZ ()D
		// 2cc: dstore 16
		// 2ce: iload 11
		// 2d0: invokestatic io/github/retrooper/packetevents/util/SpigotReflectionUtil.getEntityById (I)Lorg/bukkit/entity/Entity;
		// 2d3: instanceof org/bukkit/entity/Player
		// 2d6: ifeq 6ed
		// 2d9: aload 4
		// 2db: aload 4
		// 2dd: iload 11
		// 2df: dload 12
		// 2e1: dload 14
		// 2e3: dload 16
		// 2e5: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;IDDD)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$8 (Lme/liwk/karhu/data/KarhuPlayer;IDDDLjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 2ea: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 2ed: aload 4
		// 2ef: aload 4
		// 2f1: iload 11
		// 2f3: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;I)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$9 (Lme/liwk/karhu/data/KarhuPlayer;ILjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 2f8: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPostPing (Lme/liwk/karhu/util/gui/Callback;)V
		// 2fb: goto 6ed
		// 2fe: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity
		// 301: dup
		// 302: aload 1
		// 303: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 306: astore 11
		// 308: aload 11
		// 30a: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 30d: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getX ()D
		// 310: dstore 12
		// 312: aload 11
		// 314: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 317: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 31a: dstore 14
		// 31c: aload 11
		// 31e: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 321: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getZ ()D
		// 324: dstore 16
		// 326: aload 4
		// 328: aload 4
		// 32a: aload 11
		// 32c: dload 12
		// 32e: dload 14
		// 330: dload 16
		// 332: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity;DDD)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$10 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnLivingEntity;DDDLjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 337: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 33a: goto 6ed
		// 33d: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity
		// 340: dup
		// 341: aload 1
		// 342: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 345: astore 11
		// 347: aload 11
		// 349: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 34c: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getX ()D
		// 34f: dstore 12
		// 351: aload 11
		// 353: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 356: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 359: dstore 14
		// 35b: aload 11
		// 35d: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 360: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getZ ()D
		// 363: dstore 16
		// 365: aload 4
		// 367: aload 4
		// 369: aload 11
		// 36b: dload 12
		// 36d: dload 14
		// 36f: dload 16
		// 371: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity;DDD)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$11 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnEntity;DDDLjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 376: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 379: goto 6ed
		// 37c: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer
		// 37f: dup
		// 380: aload 1
		// 381: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 384: astore 11
		// 386: aload 11
		// 388: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 38b: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getX ()D
		// 38e: dstore 12
		// 390: aload 11
		// 392: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 395: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getY ()D
		// 398: dstore 14
		// 39a: aload 11
		// 39c: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 39f: invokevirtual com/github/retrooper/packetevents/util/Vector3d.getZ ()D
		// 3a2: dstore 16
		// 3a4: aload 4
		// 3a6: aload 4
		// 3a8: dload 12
		// 3aa: dload 14
		// 3ac: dload 16
		// 3ae: aload 11
		// 3b0: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;DDDLcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$12 (Lme/liwk/karhu/data/KarhuPlayer;DDDLcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSpawnPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 3b5: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 3b8: goto 6ed
		// 3bb: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityTeleport
		// 3be: dup
		// 3bf: aload 1
		// 3c0: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityTeleport.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 3c3: astore 11
		// 3c5: aload 11
		// 3c7: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityTeleport.getPosition ()Lcom/github/retrooper/packetevents/util/Vector3d;
		// 3ca: astore 12
		// 3cc: aload 11
		// 3ce: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityTeleport.getEntityId ()I
		// 3d1: istore 13
		// 3d3: aload 4
		// 3d5: aload 4
		// 3d7: iload 13
		// 3d9: aload 12
		// 3db: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;ILcom/github/retrooper/packetevents/util/Vector3d;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$13 (Lme/liwk/karhu/data/KarhuPlayer;ILcom/github/retrooper/packetevents/util/Vector3d;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 3e0: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 3e3: aload 4
		// 3e5: aload 4
		// 3e7: iload 13
		// 3e9: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;I)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$14 (Lme/liwk/karhu/data/KarhuPlayer;ILjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 3ee: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPostPing (Lme/liwk/karhu/util/gui/Callback;)V
		// 3f1: goto 6ed
		// 3f4: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerDestroyEntities
		// 3f7: dup
		// 3f8: aload 1
		// 3f9: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerDestroyEntities.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 3fc: astore 11
		// 3fe: aload 11
		// 400: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerDestroyEntities.getEntityIds ()[I
		// 403: astore 12
		// 405: aload 4
		// 407: aload 4
		// 409: aload 12
		// 40b: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;[I)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$15 (Lme/liwk/karhu/data/KarhuPlayer;[ILjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 410: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 413: goto 6ed
		// 416: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUseBed
		// 419: dup
		// 41a: aload 1
		// 41b: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUseBed.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 41e: astore 11
		// 420: aload 4
		// 422: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 425: aload 11
		// 427: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUseBed.getEntityId ()I
		// 42a: if_icmpne 6ed
		// 42d: aload 4
		// 42f: aload 4
		// 431: aload 11
		// 433: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUseBed;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$16 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUseBed;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 438: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 43b: goto 6ed
		// 43e: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation
		// 441: dup
		// 442: aload 1
		// 443: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 446: astore 12
		// 448: aload 4
		// 44a: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 44d: aload 12
		// 44f: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation.getEntityId ()I
		// 452: if_icmpne 6ed
		// 455: aload 12
		// 457: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation.getType ()Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation$EntityAnimationType;
		// 45a: getstatic com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation$EntityAnimationType.WAKE_UP Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityAnimation$EntityAnimationType;
		// 45d: if_acmpne 6ed
		// 460: aload 4
		// 462: aload 4
		// 464: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$17 (Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 469: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 46c: goto 6ed
		// 46f: aload 4
		// 471: aload 4
		// 473: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$18 (Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 478: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 47b: goto 6ed
		// 47e: aload 4
		// 480: aload 4
		// 482: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$19 (Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 487: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 48a: goto 6ed
		// 48d: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect
		// 490: dup
		// 491: aload 1
		// 492: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 495: astore 13
		// 497: aload 13
		// 499: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect.getEntityId ()I
		// 49c: istore 14
		// 49e: aload 4
		// 4a0: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 4a3: iload 14
		// 4a5: if_icmpeq 4a9
		// 4a8: return
		// 4a9: aload 13
		// 4ab: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect.getPotionType ()Lcom/github/retrooper/packetevents/protocol/potion/PotionType;
		// 4ae: aload 4
		// 4b0: invokevirtual me/liwk/karhu/data/KarhuPlayer.getClientVersion ()Lcom/github/retrooper/packetevents/protocol/player/ClientVersion;
		// 4b3: invokeinterface com/github/retrooper/packetevents/protocol/potion/PotionType.getId (Lcom/github/retrooper/packetevents/protocol/player/ClientVersion;)I 2
		// 4b8: istore 15
		// 4ba: aload 13
		// 4bc: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect.getEffectAmplifier ()I
		// 4bf: istore 16
		// 4c1: aload 4
		// 4c3: aload 4
		// 4c5: iload 15
		// 4c7: iload 16
		// 4c9: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;II)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$20 (Lme/liwk/karhu/data/KarhuPlayer;IILjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 4ce: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 4d1: goto 6ed
		// 4d4: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect
		// 4d7: dup
		// 4d8: aload 1
		// 4d9: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 4dc: astore 13
		// 4de: aload 4
		// 4e0: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 4e3: aload 13
		// 4e5: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect.getEntityId ()I
		// 4e8: if_icmpeq 4ec
		// 4eb: return
		// 4ec: aload 4
		// 4ee: aload 4
		// 4f0: aload 13
		// 4f2: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$21 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 4f7: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 4fa: goto 6ed
		// 4fd: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes
		// 500: dup
		// 501: aload 1
		// 502: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 505: astore 13
		// 507: aload 13
		// 509: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes.getEntityId ()I
		// 50c: aload 4
		// 50e: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 511: if_icmpeq 515
		// 514: return
		// 515: aload 4
		// 517: aload 13
		// 519: aload 4
		// 51b: invokedynamic call (Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes;Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$22 (Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes;Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 520: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 523: goto 6ed
		// 526: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityMetadata
		// 529: dup
		// 52a: aload 1
		// 52b: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityMetadata.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 52e: astore 13
		// 530: aload 13
		// 532: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityMetadata.getEntityId ()I
		// 535: aload 4
		// 537: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 53a: if_icmpne 6ed
		// 53d: getstatic me/liwk/karhu/Karhu.SERVER_VERSION Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 540: getstatic com/github/retrooper/packetevents/manager/server/ServerVersion.V_1_14 Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 543: invokevirtual com/github/retrooper/packetevents/manager/server/ServerVersion.isNewerThanOrEquals (Lcom/github/retrooper/packetevents/manager/server/ServerVersion;)Z
		// 546: ifeq 6ed
		// 549: bipush 12
		// 54b: istore 14
		// 54d: getstatic me/liwk/karhu/Karhu.SERVER_VERSION Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 550: getstatic com/github/retrooper/packetevents/manager/server/ServerVersion.V_1_16_5 Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 553: invokevirtual com/github/retrooper/packetevents/manager/server/ServerVersion.isOlderThanOrEquals (Lcom/github/retrooper/packetevents/manager/server/ServerVersion;)Z
		// 556: ifeq 560
		// 559: bipush 13
		// 55b: istore 14
		// 55d: goto 570
		// 560: getstatic me/liwk/karhu/Karhu.SERVER_VERSION Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 563: getstatic com/github/retrooper/packetevents/manager/server/ServerVersion.V_1_17 Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 566: invokevirtual com/github/retrooper/packetevents/manager/server/ServerVersion.isNewerThanOrEquals (Lcom/github/retrooper/packetevents/manager/server/ServerVersion;)Z
		// 569: ifeq 570
		// 56c: bipush 14
		// 56e: istore 14
		// 570: aload 13
		// 572: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityMetadata.getEntityMetadata ()Ljava/util/List;
		// 575: iload 14
		// 577: invokestatic me/liwk/karhu/handler/global/TransactionHandler.getIndex (Ljava/util/List;I)Lcom/github/retrooper/packetevents/protocol/entity/data/EntityData;
		// 57a: astore 15
		// 57c: aload 15
		// 57e: ifnull 58f
		// 581: aload 4
		// 583: aload 15
		// 585: aload 4
		// 587: invokedynamic call (Lcom/github/retrooper/packetevents/protocol/entity/data/EntityData;Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$23 (Lcom/github/retrooper/packetevents/protocol/entity/data/EntityData;Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 58c: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 58f: goto 6ed
		// 592: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerJoinGame
		// 595: dup
		// 596: aload 1
		// 597: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerJoinGame.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 59a: astore 13
		// 59c: aload 13
		// 59e: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerJoinGame.getGameMode ()Lcom/github/retrooper/packetevents/protocol/player/GameMode;
		// 5a1: ifnull 5c3
		// 5a4: aload 4
		// 5a6: aload 13
		// 5a8: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerJoinGame.getGameMode ()Lcom/github/retrooper/packetevents/protocol/player/GameMode;
		// 5ab: putfield me/liwk/karhu/data/KarhuPlayer.gameMode Lcom/github/retrooper/packetevents/protocol/player/GameMode;
		// 5ae: aload 4
		// 5b0: aload 4
		// 5b2: invokevirtual me/liwk/karhu/data/KarhuPlayer.getGameMode ()Lcom/github/retrooper/packetevents/protocol/player/GameMode;
		// 5b5: getstatic com/github/retrooper/packetevents/protocol/player/GameMode.SPECTATOR Lcom/github/retrooper/packetevents/protocol/player/GameMode;
		// 5b8: if_acmpne 5bf
		// 5bb: bipush 1
		// 5bc: goto 5c0
		// 5bf: bipush 0
		// 5c0: invokevirtual me/liwk/karhu/data/KarhuPlayer.setSpectating (Z)V
		// 5c3: aload 4
		// 5c5: aload 13
		// 5c7: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerJoinGame.getEntityId ()I
		// 5ca: invokevirtual me/liwk/karhu/data/KarhuPlayer.setEntityId (I)V
		// 5cd: goto 6ed
		// 5d0: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState
		// 5d3: dup
		// 5d4: aload 1
		// 5d5: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 5d8: astore 13
		// 5da: aload 13
		// 5dc: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState.getReason ()Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState$Reason;
		// 5df: getstatic com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState$Reason.CHANGE_GAME_MODE Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState$Reason;
		// 5e2: if_acmpne 6ed
		// 5e5: aload 4
		// 5e7: aload 4
		// 5e9: aload 13
		// 5eb: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$24 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 5f0: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 5f3: goto 6ed
		// 5f6: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerAbilities
		// 5f9: dup
		// 5fa: aload 1
		// 5fb: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerAbilities.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 5fe: astore 13
		// 600: aload 4
		// 602: invokevirtual me/liwk/karhu/data/KarhuPlayer.getAbilityManager ()Lme/liwk/karhu/handler/AbilityManager;
		// 605: aload 13
		// 607: invokevirtual me/liwk/karhu/handler/AbilityManager.onAbilityServer (Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerAbilities;)V
		// 60a: goto 6ed
		// 60d: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerHeldItemChange
		// 610: dup
		// 611: aload 1
		// 612: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerHeldItemChange.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 615: astore 13
		// 617: aload 4
		// 619: aload 4
		// 61b: aload 13
		// 61d: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerHeldItemChange;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$25 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerHeldItemChange;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 622: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 625: goto 6ed
		// 628: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity
		// 62b: dup
		// 62c: aload 1
		// 62d: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 630: astore 13
		// 632: getstatic me/liwk/karhu/Karhu.SERVER_VERSION Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 635: getstatic com/github/retrooper/packetevents/manager/server/ServerVersion.V_1_9 Lcom/github/retrooper/packetevents/manager/server/ServerVersion;
		// 638: invokevirtual com/github/retrooper/packetevents/manager/server/ServerVersion.isNewerThanOrEquals (Lcom/github/retrooper/packetevents/manager/server/ServerVersion;)Z
		// 63b: ifeq 63f
		// 63e: return
		// 63f: aload 13
		// 641: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity.isLeash ()Z
		// 644: ifne 6ed
		// 647: aload 13
		// 649: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity.getAttachedId ()I
		// 64c: aload 4
		// 64e: invokevirtual me/liwk/karhu/data/KarhuPlayer.getEntityId ()I
		// 651: if_icmpne 694
		// 654: aload 13
		// 656: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity.getHoldingId ()I
		// 659: istore 14
		// 65b: iload 14
		// 65d: invokestatic io/github/retrooper/packetevents/util/SpigotReflectionUtil.getEntityById (I)Lorg/bukkit/entity/Entity;
		// 660: astore 15
		// 662: iload 14
		// 664: bipush -1
		// 665: if_icmpeq 66c
		// 668: bipush 1
		// 669: goto 66d
		// 66c: bipush 0
		// 66d: istore 16
		// 66f: aload 4
		// 671: aload 4
		// 673: iload 16
		// 675: iload 14
		// 677: aload 15
		// 679: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;ZILorg/bukkit/entity/Entity;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$26 (Lme/liwk/karhu/data/KarhuPlayer;ZILorg/bukkit/entity/Entity;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 67e: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 681: aload 4
		// 683: iload 14
		// 685: bipush -1
		// 686: if_icmpeq 68d
		// 689: bipush 1
		// 68a: goto 68e
		// 68d: bipush 0
		// 68e: invokevirtual me/liwk/karhu/data/KarhuPlayer.setRidingUncertain (Z)V
		// 691: goto 6ed
		// 694: aload 13
		// 696: invokevirtual com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity.getHoldingId ()I
		// 699: istore 14
		// 69b: iload 14
		// 69d: bipush -1
		// 69e: if_icmpeq 6a5
		// 6a1: bipush 1
		// 6a2: goto 6a6
		// 6a5: bipush 0
		// 6a6: istore 15
		// 6a8: aload 4
		// 6aa: aload 4
		// 6ac: aload 13
		// 6ae: iload 14
		// 6b0: iload 15
		// 6b2: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity;IZ)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$27 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerAttachEntity;IZLjava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 6b7: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 6ba: goto 6ed
		// 6bd: new com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSetPassengers
		// 6c0: dup
		// 6c1: aload 1
		// 6c2: invokespecial com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSetPassengers.<init> (Lcom/github/retrooper/packetevents/event/PacketSendEvent;)V
		// 6c5: astore 13
		// 6c7: aload 4
		// 6c9: aload 4
		// 6cb: aload 13
		// 6cd: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSetPassengers;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$28 (Lme/liwk/karhu/data/KarhuPlayer;Lcom/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerSetPassengers;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 6d2: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 6d5: goto 6ed
		// 6d8: aload 4
		// 6da: aload 4
		// 6dc: invokedynamic call (Lme/liwk/karhu/data/KarhuPlayer;)Lme/liwk/karhu/util/gui/Callback; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)V, me/liwk/karhu/handler/global/TransactionHandler.lambda$handlePacketPlaySend$29 (Lme/liwk/karhu/data/KarhuPlayer;Ljava/lang/Integer;)V, (Ljava/lang/Integer;)V ]
		// 6e1: invokevirtual me/liwk/karhu/data/KarhuPlayer.queueToPrePing (Lme/liwk/karhu/util/gui/Callback;)V
		// 6e4: goto 6ed
		// 6e7: aload 4
		// 6e9: lload 2
		// 6ea: invokevirtual me/liwk/karhu/data/KarhuPlayer.setLastPingTime (J)V
		// 6ed: return
	}

	public void handleTransaction(short number, long nanoTime, KarhuPlayer data) {
		if (data.getScheduledTransactions().containsKey(number)) {
			++data.sentConfirms;
		}

		boolean wasFirst = false;
		if (number == -32767) {
			wasFirst = !data.sentPingRequest;
			data.sentPingRequest = true;
		}

		if ((number < -20000 || number > -3000) && !wasFirst) {
			if (!data.sendingPledgePackets && data.getTotalTicks() > 300) {
				Tasker.run(() -> data.getBukkitPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getUninjectedKick()) + " (Time out)"));
			}
		} else {
			data.sentPingRequest = true;
			if (data.hasSentTickFirst) {
				data.hasSentTickFirst = false;
			} else {
				data.hasSentTickFirst = true;
				data.transactionTime.put(number, nanoTime);
				data.useOldTransaction(uid -> data.setServerTick(data.getServerTick() + 1L), number);
			}

			data.sendingPledgePackets = true;
		}

		int absT = Math.abs(data.getCurrentServerTransaction());
		int absTL = Math.abs(number);
		if (absT - absTL > 1
			&& absT - absTL < 50
			&& Karhu.getInstance().getConfigManager().isNethandler()
			&& (Karhu.getInstance().getConfigManager().isDelay() || Karhu.getInstance().getConfigManager().isSpoof())) {
			Karhu.getInstance().printCool("&b> &fTransactions have been skipped, proxy issue? " + absT + " | " + absTL);
		}

		if (number < 0) {
			data.setCurrentServerTransaction(number);
		}

		data.getNetHandler().handleServerTransaction(number, nanoTime);
		data.setFirstTransactionSent(true);
	}

	public static EntityData getIndex(List<EntityData> objects, int index) {
		for (EntityData object : objects) {
			if (object.getIndex() == index) {
				return object;
			}
		}

		return null;
	}
}
