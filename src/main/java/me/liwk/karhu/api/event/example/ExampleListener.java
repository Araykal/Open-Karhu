/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.example;

import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.KarhuListener;
import me.liwk.karhu.api.event.impl.KarhuAlertEvent;
import org.bukkit.entity.Player;

public final class ExampleListener implements KarhuListener {
	@Override
	public void onEvent(KarhuEvent event) {
		if (event instanceof KarhuAlertEvent) {
			CheckData check = ((KarhuAlertEvent)event).getCheck();
			Player var3 = ((KarhuAlertEvent)event).getPlayer();
		}
	}
}
