package io.github.hsyyid.commandsigns.data.commands;

import com.google.common.collect.ComparisonChain;
import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableListData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;

import java.util.List;
import java.util.Optional;

public class ImmutableSpongeCommandsData extends AbstractImmutableListData<String, ImmutableCommandsData, CommandsData> implements ImmutableCommandsData
{
	public ImmutableSpongeCommandsData(List<String> value)
	{
		super(value, CommandSigns.COMMANDS);
	}

	@Override
	public DataContainer toContainer()
	{
		return super.toContainer().set(CommandSigns.COMMANDS.getQuery(), this.getValue());
	}

	@Override
	public CommandsData asMutable()
	{
		return new SpongeCommandsData(this.value);
	}

	@Override
	public ImmutableListValue<String> commands()
	{
		return Sponge.getRegistry().getValueFactory().createListValue(CommandSigns.COMMANDS, this.getValue()).asImmutable();
	}

	@Override
	public int compareTo(ImmutableCommandsData o)
	{
		return ComparisonChain.start()
			.compare(commands().size(), o.commands().size())
			.result();
	}

	@Override
	public <E> Optional<ImmutableCommandsData> with(Key<? extends BaseValue<E>> key, E value)
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
