package io.github.hsyyid.commandsigns.data.isonetime;

import com.google.common.base.Preconditions;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class SpongeIsOneTimeData extends AbstractBooleanData<IsOneTimeData, ImmutableIsOneTimeData> implements IsOneTimeData
{
	public SpongeIsOneTimeData(boolean value)
	{
		super(value, CommandSigns.IS_ONE_TIME, false);
	}

	public SpongeIsOneTimeData()
	{
		this(false);
	}

	@Override
	public Value<Boolean> isOneTime()
	{
		return getValueGetter();
	}

	@Override
	public Optional<IsOneTimeData> fill(DataHolder dataHolder, MergeFunction overlap)
	{
		IsOneTimeData chestRewardData = Preconditions.checkNotNull(overlap).merge(copy(), from(dataHolder.toContainer()).orElse(null));
		return Optional.of(set(CommandSigns.IS_ONE_TIME, chestRewardData.get(CommandSigns.IS_ONE_TIME).get()));
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.IS_ONE_TIME.getQuery(), this.getValue());
	}

	@Override
	public Optional<IsOneTimeData> from(DataContainer container)
	{
		boolean value = (Boolean) container.get(DataQuery.of("IsOneTime")).orElse(false);

		if (value)
			return Optional.of(new SpongeIsOneTimeData(value));
		else
			return Optional.empty();
	}

	@Override
	public IsOneTimeData copy()
	{
		return new SpongeIsOneTimeData(getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public ImmutableIsOneTimeData asImmutable()
	{
		return new ImmutableSpongeIsOneTimeData(getValue());
	}

}
