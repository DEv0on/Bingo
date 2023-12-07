package pl.dev0on.bingo;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.minecraft.commands.CommandSourceStack;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.dev0on.bingo.data.Card;
import pl.dev0on.bingo.inventory.BingoGUI;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static net.minecraft.commands.Commands.literal;

public final class Bingo extends JavaPlugin implements Listener {

    private HashMap<UUID, Card> players = new HashMap<>();
    public static Bingo INSTANCE;

    private BukkitTask endTimerScheduler;

    @Override
    public void onEnable() {
        Bingo.INSTANCE = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new BingoGUI.InventoryListener(), this);

        this.registerPluginBrigadierCommand(
                "bingo",
                l -> l.then(
                                literal("start")
                                        .requires(source -> source.getBukkitSender().hasPermission("bingo.start"))
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getBukkitSender() instanceof Player player)) {
                                                ctx.getSource().getBukkitSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cTa komenda może zostać użyta jedynie przez gracza!"));
                                                return 0;
                                            }
                                            WorldCreator wc = new WorldCreator("bingo");
                                            wc.environment(World.Environment.NORMAL);
                                            wc.type(WorldType.NORMAL);

                                            @Nullable
                                            World world = wc.createWorld();
                                            if (world == null) {
                                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cWystąpił błąd podczas tworzenia świata gry!"));
                                                return 0;
                                            }
                                            var wb = world.getWorldBorder();
                                            wb.setCenter(0, 0);
                                            wb.setSize(2000);

                                            Bukkit.getOnlinePlayers().forEach(pl -> this.players.put(player.getUniqueId(), new Card(pl)));

                                            int[] colors = new int[]{0xa7f542, 0xe6f542, 0xf0981d, 0xf24c0a, 0xff0000, 0xc9083f};
                                            AtomicInteger sec = new AtomicInteger(colors.length - 1);
                                            Bukkit.getScheduler().runTaskTimer(this, bukkitTask -> {
                                                Bukkit.getOnlinePlayers().forEach(p -> {
                                                    p.showTitle(Title.title(
                                                            Component.text(sec.get()).color(TextColor.color(colors[5 - sec.get()])),
                                                            Component.text(""),
                                                            Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(700), Duration.ofMillis(150)))
                                                    );
                                                });
                                                if (sec.get() == 0) {
                                                    var x = new Random().nextInt(-1000, 1000);
                                                    var z = new Random().nextInt(-1000, 1000);
                                                    var pos = world.getHighestBlockAt(x, z);
                                                    Bukkit.getOnlinePlayers().forEach(p -> {
                                                        p.setHealth(20);
                                                        p.setFoodLevel(20);
                                                        p.setExp(0);
                                                        p.getInventory().clear();
                                                        p.teleport(pos.getLocation());
                                                    });

                                                    endTimerScheduler = Bukkit.getScheduler().runTaskLater(this, () -> {
                                                        Card winner = this.players.values().stream().sorted((card, card1) -> Integer.compare(card1.getPoints(), card.getPoints())).findFirst().orElse(null);
                                                        if (winner == null) return;
                                                        endGame(Bukkit.getPlayer(winner.getPlayer()));
                                                    }, 30 * 60 * 20);

                                                    bukkitTask.cancel();
                                                }
                                                sec.getAndDecrement();
                                            }, 0, 20);
                                            return 0;
                                        })
                        )
                        .then(
                                literal("reset")
                                        .requires(source -> source.getBukkitSender().hasPermission("bingo.reset"))
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getBukkitSender() instanceof Player player)) {
                                                ctx.getSource().getBukkitSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cTa komenda może zostać użyta jedynie przez gracza!"));
                                                return 0;
                                            }
                                            if (Bukkit.getWorld("bingo") == null) {
                                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cGra nie jest rozpoczęta!"));
                                                return 0;
                                            }

                                            endGame(null);
                                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aZresetowano stan gry!"));
                                            return 0;
                                        })
                        )
                        .requires(source -> source.getBukkitSender().hasPermission("bingo.gui"))
                        .executes(ctx -> {
                            if (!(ctx.getSource().getBukkitSender() instanceof Player player)) {
                                ctx.getSource().getBukkitSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cTa komenda może zostać użyta jedynie przez gracza!"));
                                return 0;
                            }
                            Card card = players.get(player.getUniqueId());
                            if (card == null) {
                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cGra się nie rozpoczeła!"));
                                return 0;
                            }
                            ;
                            new BingoGUI(card).openInventory(player);
                            return 0;
                        })
        );
    }

    private PluginBrigadierCommand registerPluginBrigadierCommand(final String label, final Consumer<LiteralArgumentBuilder<CommandSourceStack>> command) {
        final PluginBrigadierCommand pluginBrigadierCommand = new PluginBrigadierCommand(this, label, command);
        this.getServer().getCommandMap().register(this.getName(), pluginBrigadierCommand);
        ((CraftServer) this.getServer()).syncCommands();
        return pluginBrigadierCommand;
    }

    private void endGame(Player winner) {
        endTimerScheduler.cancel();
        if (winner != null) {
            Bukkit.broadcast(Component.text("Gra się zakończyła!").color(TextColor.color(Color.RED.asRGB())));
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("&aZdobyłeś &c%d &apkt!", this.players.get(winner.getUniqueId()).getPoints()))));
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("&aZwyciężył &c%s &azdobywając %d pkt!", winner.getName(), this.players.get(winner.getUniqueId()).getPoints())));

            this.players.remove(winner.getUniqueId());
            var winners = this.players.values().stream().sorted((card, card1) -> Integer.compare(card1.getPoints(), card.getPoints())).limit(2).toList();
            if (!winners.isEmpty())
                Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("&aDrugie miejsce zajął &c%s &azdobywając %d pkt!", Bukkit.getPlayer(winners.get(0).getPlayer()).getName(), winners.get(0).getPoints())));
            if (winners.size() > 1)
                Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("&Trzecie miejsce zajął &c%s &azdobywając %d pkt!", Bukkit.getPlayer(winners.get(1).getPlayer()).getName(), winners.get(1).getPoints())));
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            this.players.clear();

            try {
                Bukkit.getWorld("bingo").getPlayers().forEach(p -> {
                    p.teleport(new Location(Bukkit.getWorlds().get(0), 0, 64, 0));
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setExp(0);
                    p.getInventory().clear();
                });
                Bukkit.unloadWorld("bingo", false);

                FileUtils.deleteDirectory(new File("bingo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5 * 20);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @EventHandler
    public void onCommandRegistered(final CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
        if (!(event.getCommand() instanceof PluginBrigadierCommand pluginBrigadierCommand)) {
            return;
        }
        final LiteralArgumentBuilder<CommandSourceStack> node = literal(event.getCommandLabel());
        pluginBrigadierCommand.command().accept(node);
        event.setLiteral((LiteralCommandNode) node.build());
    }

    @EventHandler
    public void onInventoryChange(PlayerInventorySlotChangeEvent ev) {
        var card = this.players.get(ev.getPlayer().getUniqueId());
        if (card == null) return;

        if (card.solveItem(ev.getNewItemStack())) {
            ev.getPlayer().sendMessage(
                    Component.text("Zdobyłeś ")
                            .color(TextColor.color(Color.LIME.asRGB()))
                            .append(Component.translatable(ev.getNewItemStack().getType().getItemTranslationKey()).color(TextColor.color(Color.RED.asRGB())))
                            .append(Component.text(" do bingo!").color(TextColor.color(Color.LIME.asRGB())))
            );
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("&aGracz &c%s &azdobył &c%d &apunktów w bingo!", ev.getPlayer().getName(), card.getPoints())));
            if (card.checkForBingo())
                endGame(ev.getPlayer());
        }
    }


    @EventHandler
    public void onDeath(PlayerRespawnEvent ev) {
        @Nullable
        World bingo;
        if ((bingo = Bukkit.getWorld("bingo")) == null) return;

        int x = new Random().nextInt(-1000, 1000);
        int z = new Random().nextInt(-1000, 1000);
        ev.setRespawnLocation(bingo.getHighestBlockAt(x, z).getLocation());
    }
}
