package broccolai.tickets.bukkit;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.bukkit.inject.BukkitModule;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;

import cloud.commandframework.paper.PaperCommandManager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.concurrent.TimeUnit;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public final class BukkitPlatform extends JavaPlugin implements PluginPlatform {

    private @MonotonicNonNull PureTickets pureTickets;

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(
                new BukkitModule(this, this)
        );

        this.pureTickets = injector.getInstance(PureTickets.class);
        this.pureTickets.load();

        try {
            CommandManager<OnlineSoul> commandManager = this.commandManager(injector.getInstance(UserService.class));
            this.pureTickets.commands(commandManager, COMMANDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.pureTickets.subscribers(SUBSCRIBERS);
    }

    @Override
    public void onDisable() {
        this.pureTickets.unload();
    }

    private CommandManager<OnlineSoul> commandManager(final @NonNull UserService userService) throws Exception {
        CommandManager<@NonNull OnlineSoul> cloudManager = new PaperCommandManager<>(
                this,
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().withAsynchronousParsing().build(),
                sender -> {
                    if (sender instanceof ConsoleCommandSender) {
                        return userService.console();
                    }

                    Player player = (Player) sender;
                    return userService.player(player.getUniqueId());
                },
                user -> Bukkit.getPlayer(user.uuid())
        );

        CommandConfirmationManager<@NonNull OnlineSoul> confirmationManager = new CommandConfirmationManager<>(
                30L,
                TimeUnit.SECONDS,
                context -> context.getCommandContext()
                        .getSender()
                        .sendMessage(Component.text("Confirmation required! run /puretickets confirm")),
                sender -> sender.sendMessage(Component.text("You don't have any pending commands"))
        );

        confirmationManager.registerConfirmationProcessor(cloudManager);

        new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .apply(cloudManager, ForwardingAudience.Single::audience);

        return cloudManager;
    }

    @Override
    public ClassLoader loader() {
        return this.getClassLoader();
    }

}
