package broccolai.tickets.core.commands.cloud;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
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

    private final OldMessageService oldMessageService;

    private final MinecraftExceptionHandler<OnlineSoul> adventureHandler;

    @Inject
    public ExceptionHandler(final @NonNull OldMessageService oldMessageService) {
        this.oldMessageService = oldMessageService;
        this.adventureHandler = this.generateAdventureHandler();
    }

    private MinecraftExceptionHandler<OnlineSoul> generateAdventureHandler() {
        return new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, ex -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    return this.oldMessageService.exceptionWrongSender(icse.getRequiredSender());
                })
                .withHandler(
                        MinecraftExceptionHandler.ExceptionType.NO_PERMISSION,
                        ex -> this.oldMessageService.exceptionNoPermission()
                )
                .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, ex -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_ARGUMENT_PARSING_FUNCTION.apply(ex);
                    }

                    return pureException.message(this.oldMessageService);
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, ex -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_COMMAND_EXECUTION_FUNCTION.apply(ex);
                    }

                    return pureException.message(this.oldMessageService);
                });
    }

    public void apply(final @NonNull CommandManager<OnlineSoul> commandManager) {
        this.adventureHandler.apply(commandManager, ForwardingAudience.Single::audience);
    }

}
