package io.github.hsyyid.commandsigns.data.commands;

import org.spongepowered.api.data.manipulator.mutable.ListData;
import org.spongepowered.api.data.value.mutable.ListValue;

public interface CommandsData extends ListData<String, CommandsData, ImmutableCommandsData>
{
	ListValue<String> commands();
}
