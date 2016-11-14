package io.github.hsyyid.commandsigns;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import io.github.hsyyid.commandsigns.cmdexecutors.AddCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.CommandSignsExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.ListCommandsExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.RemoveCommandExecutor;
import io.github.hsyyid.commandsigns.cmdexecutors.SetCommandSignExecutor;
import io.github.hsyyid.commandsigns.data.commands.CommandsData;
import io.github.hsyyid.commandsigns.data.commands.CommandsDataBuilder;
import io.github.hsyyid.commandsigns.data.commands.ImmutableCommandsData;
import io.github.hsyyid.commandsigns.data.commands.ImmutableSpongeCommandsData;
import io.github.hsyyid.commandsigns.data.commands.SpongeCommandsData;
import io.github.hsyyid.commandsigns.data.iscommandsign.ImmutableIsCommandSignData;
import io.github.hsyyid.commandsigns.data.iscommandsign.ImmutableSpongeIsCommandSignData;
import io.github.hsyyid.commandsigns.data.iscommandsign.IsCommandSignData;
import io.github.hsyyid.commandsigns.data.iscommandsign.IsCommandSignDataBuilder;
import io.github.hsyyid.commandsigns.data.iscommandsign.SpongeIsCommandSignData;
import io.github.hsyyid.commandsigns.data.isonetime.ImmutableIsOneTimeData;
import io.github.hsyyid.commandsigns.data.isonetime.ImmutableSpongeIsOneTimeData;
import io.github.hsyyid.commandsigns.data.isonetime.IsOneTimeData;
import io.github.hsyyid.commandsigns.data.isonetime.IsOneTimeDataBuilder;
import io.github.hsyyid.commandsigns.data.isonetime.SpongeIsOneTimeData;
import io.github.hsyyid.commandsigns.data.users.ImmutableSpongeUsersData;
import io.github.hsyyid.commandsigns.data.users.ImmutableUsersData;
import io.github.hsyyid.commandsigns.data.users.SpongeUsersData;
import io.github.hsyyid.commandsigns.data.users.UsersData;
import io.github.hsyyid.commandsigns.data.users.UsersDataBuilder;
import io.github.hsyyid.commandsigns.listeners.HitBlockListener;
import io.github.hsyyid.commandsigns.listeners.InteractBlockListener;
import io.github.hsyyid.commandsigns.utils.CommandSignModifier;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "commandsigns", name = "CommandSigns", version = "1.3.1", description = "This plugins enables server admins to create signs that run a list of commands, targeting the player who clicks them.")
public class CommandSigns
{
	public static List<CommandSignModifier> commandSignModifiers = Lists.newArrayList();
	public static Set<UUID> listCommands = Sets.newHashSet();

	// Keys
	@SuppressWarnings("serial")
	public static final Key<Value<Boolean>> IS_COMMAND_SIGN = KeyFactory.makeSingleKey(new TypeToken<Boolean>() {} , new TypeToken<Value<Boolean>>() {}, DataQuery.of("IsCommandSign"), "commandsigns:is_command_sign", "Whether a sign is a CommandSign");
	@SuppressWarnings("serial")
	public static final Key<Value<Boolean>> IS_ONE_TIME = KeyFactory.makeSingleKey(new TypeToken<Boolean>() {} , new TypeToken<Value<Boolean>>() {}, DataQuery.of("IsOneTime"), "commandsigns:is_one_time", "Whether a CommandSign is one time only");
	@SuppressWarnings("serial")
	public static final Key<Value<String>> USERS = KeyFactory.makeSingleKey(new TypeToken<String>() {} , new TypeToken<Value<String>>() {}, DataQuery.of("Users"), "commandsigns:users", "Get the users that used a CommandSign");
	@SuppressWarnings("serial")
	public static final Key<ListValue<String>> COMMANDS = KeyFactory.makeListKey(new TypeToken<List<String>>() {}, new TypeToken<ListValue<String>>() {}, DataQuery.of("Commands"), "commandsigns:commands", "Get the commands of a CommandSign");
	
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

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("setcommandsign"), CommandSpec.builder()
			.description(Text.of("Creates CommandSigns"))
			.permission("commandsigns.setcommandsign")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.bool(Text.of("one time"))), 
				GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command")))))
			.executor(new SetCommandSignExecutor())
			.build());

		subcommands.put(Arrays.asList("addcommand"), CommandSpec.builder()
			.description(Text.of("Adds Command to CommandSign"))
			.permission("commandsigns.addcommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command"))))
			.executor(new AddCommandExecutor())
			.build());

		subcommands.put(Arrays.asList("removecommand"), CommandSpec.builder()
			.description(Text.of("Removes Command on CommandSign"))
			.permission("commandsigns.removecommand")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("command number"))))
			.executor(new RemoveCommandExecutor())
			.build());

		subcommands.put(Arrays.asList("listcommands"), CommandSpec.builder()
			.description(Text.of("List Commands on CommandSign"))
			.permission("commandsigns.listcommands")
			.executor(new ListCommandsExecutor())
			.build());

		CommandSpec commandSignsCommandSpec = CommandSpec.builder()
			.description(Text.of("CommandSigns Command"))
			.permission("commandsigns.command")
			.executor(new CommandSignsExecutor())
			.children(subcommands)
			.build();

		Sponge.getCommandManager().register(this, commandSignsCommandSpec, "cs", "commandsign", "commandsigns");

		// One-Time
		Sponge.getDataManager().register(IsOneTimeData.class, ImmutableIsOneTimeData.class, new IsOneTimeDataBuilder());
		Sponge.getDataManager().register(SpongeIsOneTimeData.class, ImmutableSpongeIsOneTimeData.class, new IsOneTimeDataBuilder());

		// IsCommandSign
		Sponge.getDataManager().register(IsCommandSignData.class, ImmutableIsCommandSignData.class, new IsCommandSignDataBuilder());
		Sponge.getDataManager().register(SpongeIsCommandSignData.class, ImmutableSpongeIsCommandSignData.class, new IsCommandSignDataBuilder());

		// Commands
		Sponge.getDataManager().register(CommandsData.class, ImmutableCommandsData.class, new CommandsDataBuilder());
		Sponge.getDataManager().register(SpongeCommandsData.class, ImmutableSpongeCommandsData.class, new CommandsDataBuilder());

		// Users
		Sponge.getDataManager().register(UsersData.class, ImmutableUsersData.class, new UsersDataBuilder());
		Sponge.getDataManager().register(SpongeUsersData.class, ImmutableSpongeUsersData.class, new UsersDataBuilder());

		Sponge.getEventManager().registerListeners(this, new HitBlockListener());
		Sponge.getEventManager().registerListeners(this, new InteractBlockListener());

		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("CommandSigns loaded!");
	}
}
