package io.github.hsyyid.commandsigns.data.commands;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.List;
import java.util.Optional;

public class CommandsDataBuilder implements DataManipulatorBuilder<CommandsData, ImmutableCommandsData>
{
	@Override
	public CommandsData create()
	{
		return new SpongeCommandsData();
	}

	@Override
	public Optional<CommandsData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<CommandsData> build(DataView container)
	{
		if (!container.contains(CommandSigns.COMMANDS.getQuery()))
		{
			return Optional.empty();
		}

		CommandsData data = new SpongeCommandsData((List<String>) container.get(CommandSigns.COMMANDS.getQuery()).get());
		return Optional.of(data);
	}
}
