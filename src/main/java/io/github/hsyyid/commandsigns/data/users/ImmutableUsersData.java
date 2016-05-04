package io.github.hsyyid.commandsigns.data.users;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableUsersData extends ImmutableDataManipulator<ImmutableUsersData, UsersData>
{
	ImmutableValue<String> users();
}
