package io.github.hsyyid.commandsigns.listeners;

import io.github.hsyyid.commandsigns.data.iscommandsign.IsCommandSignData;
import io.github.hsyyid.commandsigns.data.iscommandsign.SpongeIsCommandSignData;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class HitBlockListener
{
	@Listener
	public void onPlayerHitBlock(InteractBlockEvent.Primary event, @First Player player)
	{
		if (event.getTargetBlock().getLocation().isPresent())
		{
			Location<World> location = event.getTargetBlock().getLocation().get();

			if (location.getTileEntity().isPresent())
			{
				TileEntity tile = location.getTileEntity().get();
				Optional<IsCommandSignData> isCommandSignData = tile.get(IsCommandSignData.class);

				if (isCommandSignData.isPresent() && isCommandSignData.get().isCommandSign().get())
				{
					if (player.hasPermission("commandsigns.destroy"))
					{
						tile.offer(new SpongeIsCommandSignData(false));
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed CommandSign!"));
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to break CommandSigns!"));
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
