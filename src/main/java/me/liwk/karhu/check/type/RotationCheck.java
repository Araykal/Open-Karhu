/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.type;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.update.MovementUpdate;

public abstract class RotationCheck extends Check<MovementUpdate> {
	public RotationCheck(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	public void handle(MovementUpdate update) {
	}
}
