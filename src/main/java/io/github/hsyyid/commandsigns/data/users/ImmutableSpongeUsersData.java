package io.github.hsyyid.commandsigns.data.users;

import com.google.common.collect.ComparisonChain;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableSpongeUsersData extends AbstractImmutableSingleData<String, ImmutableUsersData, UsersData> implements ImmutableUsersData
{
	public ImmutableSpongeUsersData(String value)
	{
		super(value, CommandSigns.USERS);
	}

	@Override
	protected ImmutableValue<String> getValueGetter()
	{
		return users();
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.USERS.getQuery(), this.getValue());
	}
	
	@Override
	public UsersData asMutable()
	{
		return new SpongeUsersData(this.value);
	}

	@Override
	public ImmutableValue<String> users()
	{
		return Sponge.getRegistry().getValueFactory().createValue(CommandSigns.USERS, this.getValue()).asImmutable();
	}

	@Override
	public int compareTo(ImmutableUsersData o)
	{
		return ComparisonChain.start()
			.compare(users().get(), o.users().get())
			.result();
	}

	@Override
	public <E> Optional<ImmutableUsersData> with(Key<? extends BaseValue<E>> key, E value)
	{
		if (this.supports(key))
		{
			return Optional.of(asMutable().set(key, value).asImmutable());
		}
		else
		{
			return Optional.empty();
		}
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}
}
