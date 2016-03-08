package io.github.hsyyid.commandsigns;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import io.github.hsyyid.commandsigns.cmdexecutors.AddCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.RemoveCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.SetCommandExecutor;
import io.github.hsyyid.commandsigns.utils.Command;
import io.github.hsyyid.commandsigns.utils.CommandSign;
import io.github.hsyyid.commandsigns.utils.DatabaseManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Plugin(id = "io.github.hsyyid.commandsigns", name = "CommandSigns", version = "1.0", description = "This plugins enables server admins to create signs that run a list of commands, targeting the player who clicks them.")
public class CommandSigns
{

	public static Game game = null;
	public static ConfigurationNode config = null;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static ArrayList<CommandSign> commandSigns = Lists.newArrayList();
	public static ArrayList<Command> commands = Lists.newArrayList();

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
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("CommandSigns loading...");

		game = Sponge.getGame();

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

		CommandSpec addCommandSpec = CommandSpec.builder()
			.description(Text.of("Adds Command to CommandSign"))
			.permission("commandsigns.addcommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command"))))
			.executor(new AddCommandExecutor())
			.build();

		game.getCommandManager().register(this, addCommandSpec, "addcommand");

		CommandSpec setCommandSpec = CommandSpec.builder()
			.description(Text.of("Sets Command on CommandSign"))
			.permission("commandsigns.setcommand")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.integer(Text.of("command number"))), GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command")))))
			.executor(new SetCommandExecutor())
			.build();

		game.getCommandManager().register(this, setCommandSpec, "setcommand");

		CommandSpec removeCommandSpec = CommandSpec.builder()
			.description(Text.of("Removes Command on CommandSign"))
			.permission("commandsigns.removecommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("command number"))))
			.executor(new RemoveCommandExecutor())
			.build();

		game.getCommandManager().register(this, removeCommandSpec, "removecommand");

		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns loaded!");
	}

	@Listener
	public void onServerStart(GameStartingServerEvent event)
	{
		DatabaseManager.readCommandSigns();
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		DatabaseManager.writeCommandSigns();
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
			String line0 = signData.getValue(Keys.SIGN_LINES).get().get(0).toPlain();
			String line1 = signData.getValue(Keys.SIGN_LINES).get().get(1).toPlain();

			if (line0.equals("[CommandSign]"))
			{
				if (player.hasPermission("commandsigns.create"))
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_BLUE, "[CommandSign]")));
					player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully created a CommandSign!"));

					if (line1.equalsIgnoreCase("onetime"))
					{
						signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(1, Text.of(TextColors.GREEN, "One-time Use")));
						commandSigns.add(new CommandSign(signLocation, true));
					}
					else
						commandSigns.add(new CommandSign(signLocation, false));

					DatabaseManager.writeCommandSigns();
				}
			}
		}
	}

	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();

			for (Transaction<BlockSnapshot> transaction : event.getTransactions())
			{
				CommandSign targetCommandSign = null;

				for (CommandSign cmdSign : commandSigns)
				{
					if (cmdSign.getLocation().getX() == transaction.getFinal().getLocation().get().getX() && cmdSign.getLocation().getY() == transaction.getFinal().getLocation().get().getY() && cmdSign.getLocation().getZ() == transaction.getFinal().getLocation().get().getZ() && cmdSign.getLocation().getExtent()
						.getUniqueId().toString().equals(cmdSign.getLocation().getExtent().getUniqueId().toString()))
					{
						targetCommandSign = cmdSign;
						break;
					}
				}

				if (targetCommandSign != null)
				{
					if (player.hasPermission("commandsigns.destroy"))
					{
						commandSigns.remove(targetCommandSign);
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed CommandSign!"));
						DatabaseManager.writeCommandSigns();
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You do not have permission to break CommandSigns!"));
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}

	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event, @First Player player)
	{
		if (event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN) || event.getTargetBlock().getState().getType().equals(BlockTypes.STANDING_SIGN))
		{
			CommandSign targetCommandSign = null;

			for (CommandSign cmdSign : commandSigns)
			{
				if (cmdSign.getLocation().getX() == event.getTargetBlock().getLocation().get().getX() && cmdSign.getLocation().getY() == event.getTargetBlock().getLocation().get().getY() && cmdSign.getLocation().getZ() == event.getTargetBlock().getLocation().get().getZ() && cmdSign.getLocation().getExtent().getUniqueId().toString()
					.equals(event.getTargetBlock().getLocation().get().getExtent().getUniqueId().toString()))
				{
					targetCommandSign = cmdSign;
					break;
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
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully set new command!"));
						}

						commandSigns.add(targetCommandSign);
						commands.remove(targetCommand);

						DatabaseManager.writeCommandSigns();
					}
					else if (targetCommand != null && targetCommand.getToRemove())
					{
						commandSigns.remove(targetCommandSign);

						if (targetCommand.getCommandNumber().isPresent() && targetCommand.getCommandNumber() != Optional.<Integer> absent())
						{
							player.sendMessage(targetCommandSign.removeCommand(targetCommand.getCommandNumber().get() - 1));
						}

						commandSigns.add(targetCommandSign);
						commands.remove(targetCommand);

						DatabaseManager.writeCommandSigns();
					}
				}

				if (player.hasPermission("commandsigns.use"))
				{
					if (targetCommandSign.getOneTime() && targetCommandSign.getUsers().contains(player.getUniqueId().toString()))
					{
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "Error! You have already used this sign."));
						return;
					}
					else
					{
						ArrayList<String> commands = targetCommandSign.getCommands();

						if (commands.size() > 0)
						{
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Success! Executing commands."));
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "Error! No commands set on this CommandSign."));
						}

						for (String command : commands)
						{
							if (command.contains("@p"))
							{
								command = command.replaceAll("@p", player.getName());
								game.getCommandManager().process(game.getServer().getConsole(), command);
							}
							else
							{
								game.getCommandManager().process(player, command);
							}
						}

						if (targetCommandSign.getOneTime())
							targetCommandSign.setUsers(targetCommandSign.getUsers() + ", " + player.getUniqueId().toString());
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
