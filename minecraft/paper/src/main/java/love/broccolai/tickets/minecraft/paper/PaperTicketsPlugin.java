package love.broccolai.tickets.minecraft.paper;

import cloud.commandframework.CommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import javax.sql.DataSource;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.common.inject.ConfigurationModule;
import love.broccolai.tickets.common.inject.ServiceModule;
import love.broccolai.tickets.common.packaged.PackagedActions;
import love.broccolai.tickets.common.packaged.PackagedMigrations;
import love.broccolai.tickets.minecraft.common.command.StaffCommands;
import love.broccolai.tickets.minecraft.common.command.UserCommands;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.paper.inject.PaperPlatformModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTicketsPlugin extends JavaPlugin {

    private static final Key<CommandManager<Commander>> COMMAND_MANAGER_KEY = Key.get(new TypeLiteral<>() {
    });

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(
            new ConfigurationModule(),
            new ServiceModule(),
            new PaperPlatformModule(this)
        );

        PackagedMigrations.migrate(
            this.getClass().getClassLoader(),
            injector.getInstance(DataSource.class)
        );

        PackagedActions.register(
            injector.getInstance(ActionRegistry.class)
        );

        CommandManager<Commander> commandManager = injector.getInstance(COMMAND_MANAGER_KEY);

        injector.getInstance(UserCommands.class).register(commandManager);
        injector.getInstance(StaffCommands.class).register(commandManager);
    }
}
