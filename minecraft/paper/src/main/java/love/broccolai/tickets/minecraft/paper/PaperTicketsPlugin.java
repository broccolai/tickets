package love.broccolai.tickets.minecraft.paper;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.TicketsPackage;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.minecraft.common.command.AdminCommands;
import love.broccolai.tickets.minecraft.common.command.StaffCommands;
import love.broccolai.tickets.minecraft.common.command.UserCommands;
import love.broccolai.tickets.minecraft.common.inject.CommandArgumentModule;
import love.broccolai.tickets.minecraft.common.inject.MessageModule;
import love.broccolai.tickets.minecraft.common.listener.TicketActionRelay;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.paper.inject.PaperPlatformModule;
import love.broccolai.tickets.minecraft.paper.listeners.UpdateProfileListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperTicketsPlugin extends JavaPlugin {

    private static final Key<CommandManager<Commander>> COMMAND_MANAGER_KEY = Key.get(new TypeLiteral<>() {
    });

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        TicketsPackage ticketsPackage = new TicketsPackage();

        Injector injector = ticketsPackage.startup(
            this.getClass().getClassLoader(),
            new CommandArgumentModule(),
            new MessageModule(),
            new PaperPlatformModule(this)
        );

        CommandManager<Commander> commandManager = injector.getInstance(COMMAND_MANAGER_KEY);

        injector.getInstance(UserCommands.class).register(commandManager);
        injector.getInstance(StaffCommands.class).register(commandManager);
        injector.getInstance(AdminCommands.class).register(commandManager);

        this.registerListeners(injector);
        this.setupNotifications(injector);
    }

    private void registerListeners(final Injector injector) {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(
            injector.getInstance(UpdateProfileListener.class),
            this
        );
    }

    private void setupNotifications(final Injector injector) {
        DatabaseConfiguration databaseConfiguration = injector.getInstance(DatabaseConfiguration.class);

        if (databaseConfiguration.type != DatabaseConfiguration.Type.POSTGRES) {
            return;
        }

        StorageService storageService = injector.getInstance(StorageService.class);

        storageService.addTicketActionRelay(
            injector.getInstance(TicketActionRelay.class)
        );
    }
}
