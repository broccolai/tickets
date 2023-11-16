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
import love.broccolai.tickets.minecraft.common.command.TicketCommand;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.paper.inject.PaperPlatformModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTicketsPlugin extends JavaPlugin {

    private static final Key<CommandManager<Commander>> COMMAND_MANAGER_KEY = Key.get(new TypeLiteral<>() {});

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(
            new ConfigurationModule(),
            new ServiceModule(),
            new PaperPlatformModule(this)
        );

        PackagedMigrations.migrate(
            PaperTicketsPlugin.class.getClassLoader(),
            injector.getInstance(DataSource.class)
        );

        PackagedActions.register(
            injector.getInstance(ActionRegistry.class)
        );

        injector.getInstance(COMMAND_MANAGER_KEY).command(
            injector.getInstance(TicketCommand.class)
        );
    }
}
