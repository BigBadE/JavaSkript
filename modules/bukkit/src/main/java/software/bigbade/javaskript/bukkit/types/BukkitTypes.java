package software.bigbade.javaskript.bukkit.types;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.javaskript.api.variables.GenericType;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.SkriptTypes;

import java.util.Map;

public final class BukkitTypes {
    private BukkitTypes() {}

    public static final SkriptType<CommandSender> COMMAND_SENDER = new GenericType<CommandSender>() {
        @Override
        public boolean isSerializable() {
            return false;
        }

        @Override
        public boolean isSet() {
            return false;
        }

        @Override
        public boolean isType(String input) {
            return false;
        }

        @Override
        public String getName() {
            return "commandsender";
        }
    };

    public static final SkriptType<Command> COMMAND = new GenericType<Command>() {
        @Override
        public boolean isSerializable() {
            return false;
        }

        @Override
        public boolean isSet() {
            return false;
        }

        @Override
        public boolean isType(String input) {
            input = input.toLowerCase();
            for(Map.Entry<String, String[]> entry : Bukkit.getCommandAliases().entrySet()) {
                for(String alias : entry.getValue()) {
                    if(alias.equals(input)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String getName() {
            return "command";
        }
    };

    public static final SkriptType<Player> PLAYER = new GenericType<Player>() {
        @Override
        public boolean isSerializable() {
            return false;
        }

        @Override
        public boolean isSet() {
            return true;
        }

        @Override
        public boolean isType(String input) {
            return Bukkit.getPlayer(input) != null;
        }

        @Override
        public String getName() {
            return "player";
        }
    };

    static {
        SkriptTypes.registerSkriptTypes(COMMAND_SENDER);
    }
}
