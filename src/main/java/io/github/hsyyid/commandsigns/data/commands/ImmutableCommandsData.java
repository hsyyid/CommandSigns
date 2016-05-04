package io.github.hsyyid.commandsigns.data.commands;

import org.spongepowered.api.data.manipulator.immutable.ImmutableListData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;

public interface ImmutableCommandsData extends ImmutableListData<String, ImmutableCommandsData, CommandsData>
{
	ImmutableListValue<String> commands();
}
