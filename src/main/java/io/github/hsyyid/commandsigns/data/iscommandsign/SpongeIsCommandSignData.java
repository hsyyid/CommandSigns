package io.github.hsyyid.commandsigns.data.iscommandsign;

import com.google.common.base.Preconditions;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class SpongeIsCommandSignData extends AbstractBooleanData<IsCommandSignData, ImmutableIsCommandSignData> implements IsCommandSignData
{
	public SpongeIsCommandSignData(boolean value)
	{
		super(value, CommandSigns.IS_ONE_TIME, false);
	}

	public SpongeIsCommandSignData()
	{
		this(false);
	}

	@Override
	public Value<Boolean> isCommandSign()
	{
		return getValueGetter();
	}

	@Override
	public Optional<IsCommandSignData> fill(DataHolder dataHolder, MergeFunction overlap)
	{
		IsCommandSignData chestRewardData = Preconditions.checkNotNull(overlap).merge(copy(), from(dataHolder.toContainer()).orElse(null));
		return Optional.of(set(CommandSigns.IS_COMMAND_SIGN, chestRewardData.get(CommandSigns.IS_COMMAND_SIGN).get()));
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.IS_COMMAND_SIGN.getQuery(), this.getValue());
	}

	@Override
	public Optional<IsCommandSignData> from(DataContainer container)
	{
		boolean value = (Boolean) container.get(DataQuery.of("IsCommandSign")).orElse(false);

		if (value)
			return Optional.of(new SpongeIsCommandSignData(value));
		else
			return Optional.empty();
	}

	@Override
	public IsCommandSignData copy()
	{
		return new SpongeIsCommandSignData(getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public ImmutableIsCommandSignData asImmutable()
	{
		return new ImmutableSpongeIsCommandSignData(getValue());
	}

}
