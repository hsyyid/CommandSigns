package io.github.hsyyid.commandsigns.data.isonetime;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface IsOneTimeData extends DataManipulator<IsOneTimeData, ImmutableIsOneTimeData>
{
	Value<Boolean> isOneTime();
}
