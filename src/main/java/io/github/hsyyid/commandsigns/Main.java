package io.github.hsyyid.commandsigns;

import io.github.hsyyid.commandsigns.cmdexecutors.AddCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.RemoveCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.SetCommandExecutor;
import io.github.hsyyid.commandsigns.utils.Command;
import io.github.hsyyid.commandsigns.utils.CommandSign;
import io.github.hsyyid.commandsigns.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.BreakBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Plugin(id = "CommandSigns", name = "CommandSigns", version = "0.4")
public class Main
{
	public static Game game = null;
	public static ConfigurationNode config = null;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static ArrayList<CommandSign> commandSigns = new ArrayList<CommandSign>();
	public static ArrayList<Command> commands = new ArrayList<Command>();

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File dConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> confManager;

	@Listener
	public void onServerStart(GameStartedServerEvent event)
	{
		getLogger().info("CommandSigns loading...");

		game = event.getGame();

		// Config File
		try
		{
			if (!dConfig.exists())
			{
				dConfig.createNewFile();
				config = confManager.load();
				confManager.save(config);
			}
			configurationManager = confManager;
			config = confManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		Utils.readCommandSigns();

		CommandSpec addCommandSpec = CommandSpec.builder()
			.description(Texts.of("Adds Command to CommandSign"))
			.permission("commandsigns.addcommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Texts.of("command"))))
			.executor(new AddCommandExecutor())
			.build();

		game.getCommandDispatcher().register(this, addCommandSpec, "addcommand");

		CommandSpec setCommandSpec = CommandSpec.builder()
			.description(Texts.of("Sets Command on CommandSign"))
			.permission("commandsigns.setcommand")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.integer(Texts.of("command number"))),
				GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Texts.of("command")))))
			.executor(new SetCommandExecutor())
			.build();

		game.getCommandDispatcher().register(this, setCommandSpec, "setcommand");

		CommandSpec removeCommandSpec = CommandSpec.builder()
			.description(Texts.of("Removes Command on CommandSign"))
			.permission("commandsigns.removecommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Texts.of("command number"))))
			.executor(new RemoveCommandExecutor())
			.build();

		game.getCommandDispatcher().register(this, removeCommandSpec, "removecommand");

		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns loaded!");
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		Utils.writeCommandSigns();
	}

	@Listener
	public void onSignChange(ChangeSignEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			Sign sign = event.getTargetTile();
			Location<World> signLocation = sign.getLocation();
			SignData signData = event.getText();
			String line0 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(0));

			commandSigns.add(new CommandSign(signLocation));
			Utils.writeCommandSigns();

			if (line0.equals("[CommandSign]"))
			{
				if (player.hasPermission("commandsigns.create"))
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Texts.of(TextColors.DARK_BLUE, "[CommandSign]")));
					player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully created a CommandSign!"));
				}
			}
		}
	}

	@Listener
	public void onPlayerBreakBlock(BreakBlockEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();

			for (BlockTransaction transaction : event.getTransactions())
			{

				if (player.hasPermission("commandsigns.destroy"))
				{
					CommandSign targetCommandSign = null;

					for (CommandSign cmdSign : commandSigns)
					{
						if (cmdSign.getLocation().getX() == transaction.getFinalReplacement().getLocation().get().getX() && 
							cmdSign.getLocation().getY() == transaction.getFinalReplacement().getLocation().get().getY() &&
							cmdSign.getLocation().getZ() == transaction.getFinalReplacement().getLocation().get().getZ() &&
							cmdSign.getLocation().getExtent().getUniqueId().toString().equals(cmdSign.getLocation().getExtent().getUniqueId().toString()))
						{
							targetCommandSign = cmdSign;
							break;
						}
					}

					if (targetCommandSign != null)
					{
						commandSigns.remove(targetCommandSign);
						player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed CommandSign!"));

						Utils.writeCommandSigns();
					}
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to break CommandSigns!"));
					event.setCancelled(true);
					break;
				}
			}
		}
	}

	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();

			if (event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN) || event.getTargetBlock().getState().getType().equals(BlockTypes.STANDING_SIGN))
			{
				CommandSign targetCommandSign = null;

				for (CommandSign cmdSign : commandSigns)
				{
					if (cmdSign.getLocation().getX() == event.getTargetBlock().getLocation().get().getX() && cmdSign.getLocation().getY() == event.getTargetBlock().getLocation().get().getY() && cmdSign.getLocation().getZ() == event.getTargetBlock().getLocation().get().getZ())
					{
						targetCommandSign = cmdSign;
					}
				}

				if (targetCommandSign != null)
				{
					if (player.hasPermission("commandsigns.modify"))
					{
						Command targetCommand = null;

						for (Command command : commands)
						{
							if (command.getPlayerUUID().equals(player.getUniqueId()))
							{
								targetCommand = command;
								break;
							}
						}

						if (targetCommand != null && targetCommand.getCommand() != null && !(targetCommand.getToRemove()))
						{
							commandSigns.remove(targetCommandSign);

							if (targetCommand.getCommandNumber().isPresent() && targetCommand.getCommandNumber() != Optional.<Integer> absent())
							{
								player.sendMessage(targetCommandSign.addCommand(targetCommand.getCommand(), targetCommand.getCommandNumber().get() - 1));
							}
							else
							{
								targetCommandSign.addCommand(targetCommand.getCommand());
								player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully set new command!"));
								getLogger().info("At line 260");
							}

							commandSigns.add(targetCommandSign);
							commands.remove(targetCommand);

							Utils.writeCommandSigns();
						}
						else if (targetCommand != null && targetCommand.getToRemove())
						{
							commandSigns.remove(targetCommandSign);

							if (targetCommand.getCommandNumber().isPresent() && targetCommand.getCommandNumber() != Optional.<Integer> absent())
							{
								player.sendMessage(targetCommandSign.removeCommand(targetCommand.getCommandNumber().get() - 1));
								getLogger().info("At line 275");
							}

							commandSigns.add(targetCommandSign);
							commands.remove(targetCommand);

							Utils.writeCommandSigns();
							getLogger().info("At line 282");
						}
					}

					if (targetCommandSign != null && player.hasPermission("commandsigns.use"))
					{
						ArrayList<String> commands = targetCommandSign.getCommands();

						if (commands.size() > 0)
						{
							player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Success! Executing commands."));
						}
						else
						{
							player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "Error! No commands set on this CommandSign."));
						}

						for (String command : commands)
						{
							command = command.replaceAll("@p", player.getName());
							game.getCommandDispatcher().process(player, command);
						}
					}
				}
			}
		}
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
