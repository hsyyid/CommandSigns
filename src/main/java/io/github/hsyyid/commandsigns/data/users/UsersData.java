package io.github.hsyyid.commandsigns.data.users;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface UsersData extends DataManipulator<UsersData, ImmutableUsersData>
{
	Value<String> users();
}
