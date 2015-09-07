package io.github.hsyyid.commandsigns;

import io.github.hsyyid.commandsigns.cmdexecutors.SetCommandExecutor;
import io.github.hsyyid.commandsigns.utils.Command;
import io.github.hsyyid.commandsigns.utils.CommandSign;
import io.github.hsyyid.commandsigns.utils.LocationAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

@Plugin(id = "CommandSigns", name = "CommandSigns", version = "0.1")
public class Main
{
	public static Game game = null;
	public static ConfigurationNode config = null;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static ArrayList<CommandSign> commandSigns = new ArrayList<CommandSign>();
	public static ArrayList<Command> commands = new ArrayList<Command>();
	private Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).create();

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

		String json = null;

		try
		{
			json = readFile("CommandSigns.json", StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			getLogger().error("Could not read JSON file!");
		}

		if(json != null)
		{
			commandSigns = new ArrayList<CommandSign>(Arrays.asList(gson.fromJson(json, CommandSign[].class)));
		}
		else
		{
			getLogger().error("Could not read JSON file!");
		}

		CommandSpec setCommandSpec = CommandSpec.builder()
				.description(Texts.of("Sets Command for a CommandSign"))
				.permission("commandsigns.setcommand")
				.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Texts.of("command"))))
				.executor(new SetCommandExecutor())
				.build();

		game.getCommandDispatcher().register(this, setCommandSpec, "setcommand");

		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns loaded!");
	}

	static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		String json = gson.toJson(commandSigns);
		try
		{
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter("CommandSigns.json");

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(json);

			bufferedWriter.flush();
			// Always close files.
			bufferedWriter.close();
		}
		catch (IOException ex)
		{
			getLogger().error("Could not save JSON file!");
		}
	}

	@Listener
	public void onSignChange(ChangeSignEvent event)
	{
		if(event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			Sign sign = event.getTargetTile();
			Location<World> signLocation = sign.getLocation();
			SignData signData = event.getText();
			String line0 = Texts.toPlain(signData.getValue(Keys.SIGN_LINES).get().get(0));

			commandSigns.add(new CommandSign(signLocation, null));
			String json = gson.toJson(commandSigns);
			try
			{
				// Assume default encoding.
				FileWriter fileWriter = new FileWriter("CommandSigns.json");

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(json);

				bufferedWriter.flush();
				// Always close files.
				bufferedWriter.close();
			}
			catch (IOException ex)
			{
				getLogger().error("Could not save JSON file!");
			}

			if(line0.equals("[CommandSign]"))
			{
				if(player.hasPermission("commandsigns.create"))
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
		if(event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			ArrayList<Location<World>> commandSignLocations = new ArrayList<Location<World>>();

			for(CommandSign cmdSign : commandSigns)
			{
				commandSignLocations.add(cmdSign.getLocation());
			}

			for(BlockTransaction transaction : event.getTransactions())
			{
				if(commandSignLocations.contains(transaction.getFinalReplacement().getLocation().get()))
				{
					if(player.hasPermission("commandsigns.destroy"))
					{
						CommandSign targetCommandSign = null;

						for(CommandSign cmdSign : commandSigns)
						{
							if(cmdSign.getLocation().equals(transaction.getFinalReplacement().getLocation().get()))
							{
								targetCommandSign = cmdSign;
								break;
							}
						}

						if(targetCommandSign != null)
						{
							commandSigns.remove(targetCommandSign);
							player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed CommandSign!"));

							String json = gson.toJson(commandSigns);
							try
							{
								// Assume default encoding.
								FileWriter fileWriter = new FileWriter("CommandSigns.json");

								// Always wrap FileWriter in BufferedWriter.
								BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

								bufferedWriter.write(json);

								bufferedWriter.flush();
								// Always close files.
								bufferedWriter.close();
							}
							catch (IOException ex)
							{
								getLogger().error("Could not save JSON file!");
							}
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
	}

	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event)
	{
		if(event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			if(event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN) || event.getTargetBlock().getState().getType().equals(BlockTypes.STANDING_SIGN))
			{
				ArrayList<Location<World>> commandSignLocations = new ArrayList<Location<World>>();

				for(CommandSign cmdSign : commandSigns)
				{
					commandSignLocations.add(cmdSign.getLocation());
				}

				if(commandSignLocations.contains(event.getTargetBlock().getLocation().get()))
				{
					CommandSign targetCommandSign = null;

					for(CommandSign commandSign : commandSigns)
					{
						if(commandSign.getLocation().equals(event.getTargetBlock().getLocation()))
						{
							targetCommandSign = commandSign;
						}
					}

					if(targetCommandSign != null && player.hasPermission("commandsigns.modify"))
					{
						Command targetCommand = null;
						commandSigns.remove(targetCommandSign);

						for(Command command : commands)
						{
							if(command.getPlayerUUID().equals(player.getUniqueId()))
							{
								targetCommand = command;
							}
						}

						player.sendMessage(Texts.of(TextColors.GOLD, "[CommadSigns]: ", TextColors.GRAY, "Successfully set new command!"));
						targetCommandSign.setCommand(targetCommand.getCommand());
						commandSigns.add(targetCommandSign);

						String json = gson.toJson(commandSigns);
						try
						{
							// Assume default encoding.
							FileWriter fileWriter = new FileWriter("CommandSigns.json");

							// Always wrap FileWriter in BufferedWriter.
							BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

							bufferedWriter.write(json);

							bufferedWriter.flush();
							// Always close files.
							bufferedWriter.close();
						}
						catch (IOException ex)
						{
							getLogger().error("Could not save JSON file!");
						}
					}

					if(targetCommandSign != null && player.hasPermission("commandsigns.use"))
					{
						String command = targetCommandSign.getCommand();
						player.sendMessage(Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Success! Executing command."));
						game.getCommandDispatcher().process(player, command);
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
