package io.github.hsyyid.commandsigns.data.isonetime;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableIsOneTimeData extends ImmutableDataManipulator<ImmutableIsOneTimeData, IsOneTimeData>
{
	ImmutableValue<Boolean> isOneTime();
}
