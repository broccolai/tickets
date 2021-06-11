package broccolai.tickets.core.commands.cloud;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.core.exceptions.PureException;
import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class ExceptionHandler {

    private final MessageService messageService;

    private final MinecraftExceptionHandler<OnlineSoul> adventureHandler;

    @Inject
    public ExceptionHandler(final @NonNull MessageService messageService) {
        this.messageService = messageService;
        this.adventureHandler = this.generateAdventureHandler();
    }

    private MinecraftExceptionHandler<OnlineSoul> generateAdventureHandler() {
        return new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, ex -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    return this.messageService.exceptionWrongSender(icse.getRequiredSender());
                })
                .withHandler(
                        MinecraftExceptionHandler.ExceptionType.NO_PERMISSION,
                        ex -> this.messageService.exceptionNoPermission()
                )
                .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, ex -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_ARGUMENT_PARSING_FUNCTION.apply(ex);
                    }

                    return pureException.message(this.messageService);
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, ex -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_COMMAND_EXECUTION_FUNCTION.apply(ex);
                    }

                    return pureException.message(this.messageService);
                });
    }

    public void apply(final @NonNull CommandManager<OnlineSoul> commandManager) {
        this.adventureHandler.apply(commandManager, ForwardingAudience.Single::audience);
    }

}
