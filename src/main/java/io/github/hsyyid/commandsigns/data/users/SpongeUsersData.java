package io.github.hsyyid.commandsigns.data.users;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class SpongeUsersData extends AbstractSingleData<String, UsersData, ImmutableUsersData> implements UsersData
{
	public SpongeUsersData()
	{
		this("");
	}

	public SpongeUsersData(String value)
	{
		super(value, CommandSigns.USERS);
	}

	@Override
	public Optional<UsersData> fill(DataHolder dataHolder, MergeFunction overlap)
	{
		UsersData typeOfChestData = Preconditions.checkNotNull(overlap).merge(copy(), from(dataHolder.toContainer()).orElse(null));
		return Optional.of(set(CommandSigns.USERS, typeOfChestData.get(CommandSigns.USERS).get()));
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.USERS.getQuery(), this.getValue());
	}

	@Override
	public Optional<UsersData> from(DataContainer container)
	{
		String value = (String) container.get(DataQuery.of("Users")).orElse(null);

		if (value != null)
			return Optional.of(new SpongeUsersData(value));
		else
			return Optional.empty();
	}

	@Override
	public UsersData copy()
	{
		return new SpongeUsersData(getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public ImmutableUsersData asImmutable()
	{
		return new ImmutableSpongeUsersData(getValue());
	}

	@Override
	public Value<String> users()
	{
		return Sponge.getRegistry().getValueFactory().createValue(CommandSigns.USERS, this.getValue());
	}

	@Override
	protected Value<?> getValueGetter()
	{
		return users();
	}

	@Override
	public int compareTo(UsersData o)
	{
		return ComparisonChain.start()
			.compare(this.users().get(), o.users().get())
			.result();
	}
}
