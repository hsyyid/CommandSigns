package io.github.hsyyid.commandsigns.data.commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.ListValue;

import java.util.List;
import java.util.Optional;

public class SpongeCommandsData extends AbstractListData<String, CommandsData, ImmutableCommandsData> implements CommandsData
{
	public SpongeCommandsData()
	{
		this(Lists.newArrayList());
	}

	public SpongeCommandsData(List<String> value)
	{
		super(value, CommandSigns.COMMANDS);
	}

	@Override
	public Optional<CommandsData> fill(DataHolder dataHolder, MergeFunction overlap)
	{
		CommandsData typeOfChestData = Preconditions.checkNotNull(overlap).merge(copy(), from(dataHolder.toContainer()).orElse(null));
		return Optional.of(set(CommandSigns.COMMANDS, typeOfChestData.get(CommandSigns.COMMANDS).get()));
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.COMMANDS.getQuery(), this.getValue());
	}

	@Override
	public Optional<CommandsData> from(DataContainer container)
	{
		List<String> value = (List<String>) container.get(CommandSigns.COMMANDS.getQuery()).orElse(null);

		if (value != null)
			return Optional.of(new SpongeCommandsData(value));
		else
			return Optional.empty();
	}

	@Override
	public CommandsData copy()
	{
		return new SpongeCommandsData(getValue());
	}

	@Override
	public int getContentVersion()
	{
		return 1;
	}

	@Override
	public ImmutableCommandsData asImmutable()
	{
		return new ImmutableSpongeCommandsData(getValue());
	}

	@Override
	public ListValue<String> commands()
	{
		return Sponge.getRegistry().getValueFactory().createListValue(CommandSigns.COMMANDS, this.getValue());
	}

	@Override
	protected ListValue<String> getValueGetter()
	{
		return commands();
	}

	@Override
	public int compareTo(CommandsData o)
	{
		return ComparisonChain.start()
			.compare(this.commands().size(), o.commands().size())
			.result();
	}
}
