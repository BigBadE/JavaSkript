package software.bigbade.javaskript.bukkit.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.Map;

@RequiredArgsConstructor
public class BukkitCommandData {
    @Getter
    @Nonnull
    private final Map<String, String> commandData;
}
