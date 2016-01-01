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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetCommandExecutor implements CommandExecutor
{
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
    {
        int commandNumber = ctx.<Integer>getOne("command number").get();
        String command = ctx.<String>getOne("command").get();

        if (src instanceof Player)
        {
            Player player = (Player) src;
            CommandSigns.commands.add(new Command(command, commandNumber, player.getUniqueId(), false));
            player.sendMessage(Text.of(TextColors.DARK_RED, "[CommandSigns]: ", TextColors.GOLD, "Right click a CommandSign!"));
        }
        else if (src instanceof ConsoleSource)
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setitem!"));
        }
        else if (src instanceof CommandBlockSource)
        {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setitem!"));
        }
        
        return CommandResult.success();
    }
}