package io.github.hsyyid.commandsigns.data.iscommandsign;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.Optional;

public class IsCommandSignDataBuilder implements DataManipulatorBuilder<IsCommandSignData, ImmutableIsCommandSignData>
{
	@Override
	public IsCommandSignData create()
	{
		return new SpongeIsCommandSignData();
	}

	@Override
	public Optional<IsCommandSignData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<IsCommandSignData> build(DataView container)
	{
		if (!container.contains(CommandSigns.IS_COMMAND_SIGN.getQuery()))
		{
			return Optional.empty();
		}

		IsCommandSignData data = new SpongeIsCommandSignData((Boolean) container.get(CommandSigns.IS_COMMAND_SIGN.getQuery()).get());
        return Optional.of(data);
	}
}
