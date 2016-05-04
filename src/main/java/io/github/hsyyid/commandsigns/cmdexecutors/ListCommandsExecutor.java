package io.github.hsyyid.commandsigns.cmdexecutors;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ListCommandsExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (!CommandSigns.listCommands.contains(player.getUniqueId()))
			{
				CommandSigns.listCommands.add(player.getUniqueId());
				player.sendMessage(Text.of(TextColors.DARK_RED, "[CommandSigns]: ", TextColors.GOLD, "Right click a CommandSign!"));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "[CommandSigns]: ", TextColors.RED, "You can already view CommandSign commands!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /listcommands!"));
		}

		return CommandResult.success();
	}
}
