package io.github.hsyyid.commandsigns.data.iscommandsign;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableIsCommandSignData extends ImmutableDataManipulator<ImmutableIsCommandSignData, IsCommandSignData>
{
	ImmutableValue<Boolean> isCommandSign();
}
