package io.github.hsyyid.commandsigns.cmdexecutors;

import io.github.hsyyid.commandsigns.CommandSigns;
import io.github.hsyyid.commandsigns.utils.Command;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class AddCommandExecutor implements CommandExecutor
{
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
    {
        String command = ctx.<String>getOne("command").get();
        
        if (src instanceof Player)
        {
            Player player = (Player) src;
            CommandSigns.commands.add(new Command(command, player.getUniqueId(), false));
            player.sendMessage(Texts.of(TextColors.DARK_RED, "[CommandSigns]: ", TextColors.GOLD, "Right click a CommandSign!"));
        }
        else if (src instanceof ConsoleSource)
        {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setitem!"));
        }
        else if (src instanceof CommandBlockSource)
        {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setitem!"));
        }
        
        return CommandResult.success();
    }
}