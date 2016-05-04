package io.github.hsyyid.commandsigns.data.isonetime;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableSpongeIsOneTimeData extends AbstractImmutableBooleanData<ImmutableIsOneTimeData, IsOneTimeData> implements ImmutableIsOneTimeData
{
	public ImmutableSpongeIsOneTimeData(boolean value)
	{
		super(value, CommandSigns.IS_ONE_TIME, false);
	}

	@Override
	public ImmutableValue<Boolean> isOneTime()
	{
		return getValueGetter();
	}

	@Override
	public <E> Optional<ImmutableIsOneTimeData> with(Key<? extends BaseValue<E>> key, E value)
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
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.IS_ONE_TIME.getQuery(), this.getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public IsOneTimeData asMutable()
	{
		return new SpongeIsOneTimeData(getValue());
	}

}
