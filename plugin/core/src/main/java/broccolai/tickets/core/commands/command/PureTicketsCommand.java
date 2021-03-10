package broccolai.tickets.core.commands.command;

public final class PureTicketsCommand {
//
//    private final Jdbi jdbi;
//
//    public PureTicketsCommand(
//            final @NonNull CommandManager<@NonNull OnlineSoul> manager,
//            final @NonNull CommandConfirmationManager<@NonNull OnlineSoul> confirmationManager,
//            final @NonNull Jdbi jdbi
//    ) {
//        this.jdbi = jdbi;
//
//        final Command.Builder<@NonNull OnlineSoul> builder = manager.commandBuilder("puretickets", "pt");
//
//        manager.command(builder.literal("info")
//                .handler(this::processInfo));
//
//        manager.command(builder.literal("settings")
//                .permission(Constants.USER_PERMISSION + ".settings")
//                .argument(EnumArgument.of(UserSettings.Options.class, "setting"))
//                .argument(BooleanArgument.of("value"))
//                .handler(this::processSettings));
//
//        manager.command(builder.literal("reload")
//                .permission(Constants.STAFF_PERMISSION + ".reload")
//                .handler(this::processReload));
//
//        manager.command(builder.literal("confirm")
//                .handler(confirmationManager.createConfirmationExecutionHandler()));
//
//        manager.command(builder.literal("PURGE")
//                .meta(CommandConfirmationManager.META_CONFIRMATION_REQUIRED, true)
//                .permission(Constants.STAFF_PERMISSION + ".purge")
//                .handler(this::processPurge));
//    }
//
//    private void processInfo(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        Component component = Component.text("PureTickets: ")
//                .append(Component.text(this.pureTickets.version()));
//        c.getSender().sendMessage(component);
//    }
//
//    private void processSettings(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        PlayerSoul<?, ?> soul = (PlayerSoul<?, ?>) c.getSender();
//        UserSettings.Options setting = c.get("setting");
//        Boolean value = c.get("value");
//        String status = value ? "enabled" : "disabled";
//
//        soul.modifyPreferences(settings -> settings.set(c.get("setting"), c.get("value")));
//
//        Component component = Message.FORMAT__SETTING_UPDATE.use(
//                Template.of("setting", setting.name().toLowerCase()),
//                Template.of("status", status)
//        );
//        soul.sendMessage(component);
//    }
//
//    private void processReload(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        this.pureTickets.stop();
//        this.pureTickets.start();
//
//        Component component = Component.text("PureTickets reloaded");
//        c.getSender().sendMessage(component);
//    }
//
//    private void processPurge(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
//        this.jdbi.useHandle(handle -> {
//            SQLQueries.PURGE_EVERYTHING.forEach(statement -> {
//                handle.createUpdate(statement).execute();
//            });
//        });
//
//        Component component = Component.text("PureTickets purged and reloaded");
//        c.getSender().sendMessage(component);
//
//        this.pureTickets.stop();
//        this.pureTickets.start();
//    }

}
