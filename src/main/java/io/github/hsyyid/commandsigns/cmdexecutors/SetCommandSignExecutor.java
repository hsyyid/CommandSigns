package io.github.hsyyid.commandsigns.cmdexecutors;

import io.github.hsyyid.commandsigns.CommandSigns;
import io.github.hsyyid.commandsigns.utils.CommandSignModifier;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class SetCommandSignExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		boolean oneTime = ctx.<Boolean> getOne("one time").get();
		String command = ctx.<String> getOne("command").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			Optional<CommandSignModifier> commandSignModifier = CommandSigns.commandSignModifiers.stream().filter(m -> m.getPlayerUniqueId().equals(player.getUniqueId())).findAny();

			if (commandSignModifier.isPresent())
			{
				CommandSigns.commandSignModifiers.remove(commandSignModifier.get());
			}

			CommandSigns.commandSignModifiers.add(new CommandSignModifier(command, player.getUniqueId(), oneTime));
			player.sendMessage(Text.of(TextColors.DARK_RED, "[CommandSigns]: ", TextColors.GOLD, "Right click a Tile Entity (something like a sign)!"));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setcommandsign!"));
		}

		return CommandResult.success();
	}
}
