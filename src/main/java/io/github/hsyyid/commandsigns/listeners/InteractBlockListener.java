package io.github.hsyyid.commandsigns.listeners;

import com.google.common.collect.Lists;
import io.github.hsyyid.commandsigns.CommandSigns;
import io.github.hsyyid.commandsigns.data.commands.CommandsData;
import io.github.hsyyid.commandsigns.data.commands.SpongeCommandsData;
import io.github.hsyyid.commandsigns.data.iscommandsign.IsCommandSignData;
import io.github.hsyyid.commandsigns.data.iscommandsign.SpongeIsCommandSignData;
import io.github.hsyyid.commandsigns.data.isonetime.IsOneTimeData;
import io.github.hsyyid.commandsigns.data.isonetime.SpongeIsOneTimeData;
import io.github.hsyyid.commandsigns.data.users.SpongeUsersData;
import io.github.hsyyid.commandsigns.data.users.UsersData;
import io.github.hsyyid.commandsigns.utils.CommandSignModifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InteractBlockListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent.Secondary event, @First Player player)
	{
		if (event.getTargetBlock().getLocation().isPresent())
		{
			Location<World> location = event.getTargetBlock().getLocation().get();

			if (location.getTileEntity().isPresent())
			{
				TileEntity tile = location.getTileEntity().get();
				Optional<IsCommandSignData> isCommandSignData = tile.get(IsCommandSignData.class);
				Optional<CommandSignModifier> commandSignModifier = CommandSigns.commandSignModifiers.stream().filter(m -> m.getPlayerUniqueId().equals(player.getUniqueId())).findAny();

				if (isCommandSignData.isPresent() && isCommandSignData.get().isCommandSign().get())
				{
					if (CommandSigns.listCommands.contains(player.getUniqueId()))
					{
						CommandSigns.listCommands.remove(player.getUniqueId());
						List<String> commands = tile.get(CommandsData.class).get().commands().get();
						PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
						List<Text> commandsText = Lists.newArrayList();
						int i = 0;

						for (String cmd : commands)
						{
							i++;
							commandsText.add(Text.of(TextColors.WHITE, i + ") ", TextColors.GOLD, cmd));
						}

						PaginationList.Builder paginationBuilder = paginationService.builder().contents(commandsText).title(Text.of(TextColors.GREEN, "Showing Commands")).padding(Text.of("-"));
						paginationBuilder.sendTo(player);
					}
					else if (player.hasPermission("commandsigns.modify") && commandSignModifier.isPresent())
					{
						CommandSignModifier modifier = commandSignModifier.get();

						// If it's an add modifier
						if (!modifier.isToRemove())
						{
							List<String> commands = tile.get(CommandsData.class).get().commands().get();
							commands.add(modifier.getCommand());
							tile.offer(new SpongeCommandsData(commands));
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Added command to CommandSign."));
						}
						// If it's a delete modifier
						else
						{
							List<String> commands = tile.get(CommandsData.class).get().commands().get();

							if (commands.size() >= modifier.getCommandNumber())
							{
								commands.remove(modifier.getCommandNumber() - 1);
								tile.offer(new SpongeCommandsData(commands));
								player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Removed command from CommandSign."));
							}
							else
							{
								player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "Number does not correspond to a command on this CommandSign."));
							}
						}

						CommandSigns.commandSignModifiers.remove(modifier);
					}
					else if (player.hasPermission("commandsigns.use"))
					{
						List<String> commands = tile.get(CommandsData.class).get().commands().get();
						boolean isOneTime = tile.get(IsOneTimeData.class).get().isOneTime().get();
						String users = tile.getOrCreate(UsersData.class).get().users().get();

						if (isOneTime && users.contains(player.getUniqueId().toString()))
						{
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "You have already used this sign."));
							return;
						}

						if (commands.size() > 0)
						{
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Executing commands."));
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.RED, "No commands are set on this CommandSign."));
						}

						for (String command : commands)
						{
							if (command.contains("@p"))
							{
								command = command.replaceAll("@p", player.getName());
								Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
							}
							else
							{
								Sponge.getCommandManager().process(player, command);
							}
						}

						if (isOneTime)
						{
							if (!users.isEmpty())
							{
								tile.offer(new SpongeUsersData(users + ", " + player.getUniqueId().toString()));
							}
							else
							{
								tile.offer(new SpongeUsersData(player.getUniqueId().toString() + ", "));
							}
						}
					}
				}
				else if (player.hasPermission("commandsigns.create") && commandSignModifier.isPresent())
				{
					CommandSignModifier modifier = commandSignModifier.get();

					if (!modifier.isToRemove() && modifier.getCommand() != null && !modifier.getCommand().isEmpty())
					{
						tile.offer(new SpongeIsCommandSignData(true));
						tile.offer(new SpongeIsOneTimeData(modifier.isOneTime()));
						tile.offer(new SpongeCommandsData(Arrays.asList(modifier.getCommand())));
						player.sendMessage(Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Created CommandSign."));
					}

					CommandSigns.commandSignModifiers.remove(modifier);
				}
			}
		}
	}
}
