package io.github.hsyyid.commandsigns.data.isonetime;

import io.github.hsyyid.commandsigns.CommandSigns;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.Optional;

public class IsOneTimeDataBuilder implements DataManipulatorBuilder<IsOneTimeData, ImmutableIsOneTimeData>
{
	@Override
	public IsOneTimeData create()
	{
		return new SpongeIsOneTimeData();
	}

	@Override
	public Optional<IsOneTimeData> createFrom(DataHolder dataHolder)
	{
		return create().fill(dataHolder);
	}

	@Override
	public Optional<IsOneTimeData> build(DataView container)
	{
		if (!container.contains(CommandSigns.IS_ONE_TIME.getQuery()))
		{
			return Optional.empty();
		}

		IsOneTimeData data = new SpongeIsOneTimeData((Boolean) container.get(CommandSigns.IS_ONE_TIME.getQuery()).get());
        return Optional.of(data);
	}
}
