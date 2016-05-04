package io.github.hsyyid.commandsigns.data.iscommandsign;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface IsCommandSignData extends DataManipulator<IsCommandSignData, ImmutableIsCommandSignData>
{
	Value<Boolean> isCommandSign();
}
