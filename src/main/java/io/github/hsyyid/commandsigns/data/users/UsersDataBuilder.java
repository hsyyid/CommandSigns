package io.github.hsyyid.commandsigns.data.users;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.Optional;

public class UsersDataBuilder implements DataManipulatorBuilder<UsersData, ImmutableUsersData>
{
	@Override
	public UsersData create()
	{
		return new SpongeUsersData();
	}

	@Override
	public Optional<UsersData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<UsersData> build(DataView container)
	{
		if (!container.contains(CommandSigns.USERS.getQuery()))
		{
			return Optional.empty();
		}

		UsersData data = new SpongeUsersData((String) container.get(CommandSigns.USERS.getQuery()).get());
		return Optional.of(data);
	}
}
