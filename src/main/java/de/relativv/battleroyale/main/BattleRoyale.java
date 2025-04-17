package de.relativv.battleroyale.main;

import de.relativv.battleroyale.commands.*;
import de.relativv.battleroyale.listener.*;
import de.relativv.battleroyale.utils.*;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public final class BattleRoyale extends JavaPlugin {

    public static BattleRoyale instance;

    private ConsoleCommandSender cs;
    private PluginManager pm;

    public Countdowns countdowns;

    public String prefix;
    public String systemPrefix;
    public String noPerm;
    public String noPermSys;

    public StatsAPI statsAPI;
    public CreditAPI creditAPI;
    public MySQL sql;

    public GameState state;

    public WorldBorder border;

    public GameUtils gameUtils;


    public int minTeams;


    public int lobbyTime;
    public int waitTime;
    public int ingameTime;
    public int restartTime;

    public int lobbySched;
    public int waitSched;
    public int ingameSched;
    public int restartSched;

    public Location worldCenter;

    public ArrayList<Player> build;

    public ArrayList<Location> blocksPlaced;

    public static ArrayList<Location> chests;
    public static ArrayList<ItemStack> chestItems;

    public ArrayList<Player> alive;
    public ArrayList<Player> spectator;

    public ArrayList<Team> teams;
    public ArrayList<Team> teamsAlive;


    @Override
    public void onEnable() {
        instance = this;

        cs = Bukkit.getServer().getConsoleSender();
        pm = Bukkit.getServer().getPluginManager();

        Bukkit.getWorld("world").getPopulators().add(new ChestPopulator());

        prefix = "§8▌ §aBattleRoyale §8» §r";
        systemPrefix = "§8▌ §3System §8» §r";

        noPerm = prefix + "§cDu hast keine Berechtigung dazu!";
        noPermSys = systemPrefix + "§cDu hast keine Berechtigung dazu!";

        build = new ArrayList<Player>();


        state = GameState.LOBBY;

        minTeams = 2;

        lobbyTime = 61;
        waitTime = 10;
        ingameTime = 1201;
        restartTime = 15;

        blocksPlaced = new ArrayList<Location>();

        alive = new ArrayList<Player>();
        spectator = new ArrayList<Player>();

        chests = new ArrayList<Location>();
        chestItems = new ArrayList<ItemStack>();

        teams = new ArrayList<Team>();
        teamsAlive = new ArrayList<Team>();

        gameUtils = new GameUtils(this);

        countdowns = new Countdowns();
        countdowns.lobbyCountdown();

        border = Bukkit.getWorld("world").getWorldBorder();

        worldCenter = new Location(Bukkit.getWorld("world"), 10000, Bukkit.getWorld("world").getHighestBlockYAt(10000, 10000), 10000);
        Bukkit.getWorld("world").setSpawnLocation(worldCenter);
        worldCenter.getChunk().load();

        border.setCenter(worldCenter);
        border.setDamageBuffer(0);
        border.setDamageAmount(0.5);
        border.setWarningDistance(40);

        for (World worlds : Bukkit.getWorlds()) {
            worlds.setWeatherDuration(0);
            worlds.setStorm(false);
            worlds.setThundering(false);
            worlds.setTime(16000L);
            worlds.setDifficulty(Difficulty.NORMAL);
            worlds.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }


        sql = new MySQL();
        sql.connect("localhost", "test", "test", "test123", "3306");

        creditAPI = new CreditAPI();
        creditAPI.createTables();

        statsAPI = new StatsAPI();
        statsAPI.createTables();


        registerThings();
        registerChestItems();


        sendConsoleMessage("§e=========== §9BattleRoyale §e============");
        sendConsoleMessage("");
        sendConsoleMessage("§3Author§8: §7" + this.getDescription().getAuthors());
        sendConsoleMessage("§3Version§8: §7" + this.getDescription().getVersion());
        sendConsoleMessage("");
        sendConsoleMessage("§a§lLOADED");
        sendConsoleMessage("");
        sendConsoleMessage("§3Prefix§8: §r" + this.prefix);
        sendConsoleMessage("");
        sendConsoleMessage("§e=========== §9BattleRoyale §e============");

    }

    @Override
    public void onDisable() {
        sql.disconnect();

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.kickPlayer("§cServer is restarting!");
        }

        sendConsoleMessage("§e=========== §9BattleRoyale §e============");
        sendConsoleMessage("");
        sendConsoleMessage("§3Author§8: §7" + this.getDescription().getAuthors());
        sendConsoleMessage("§3Version§8: §7" + this.getDescription().getVersion());
        sendConsoleMessage("");
        sendConsoleMessage("§4§lUNLOADED");
        sendConsoleMessage("");
        sendConsoleMessage("§3Prefix§8: §r" + this.prefix);
        sendConsoleMessage("");
        sendConsoleMessage("§e=========== §9BattleRoyale §e============");
    }


    public static BattleRoyale getInstance() {
        return instance;
    }

    public void sendConsoleMessage(String msg) {
        cs.sendMessage(msg);
    }


    private void registerThings() {
        this.getCommand("build").setExecutor(new Build(this));
        this.getCommand("credits").setExecutor(new Credit(this));
        this.getCommand("setlocation").setExecutor(new SetLocation(this));
        this.getCommand("start").setExecutor(new Start(this));
        this.getCommand("stats").setExecutor(new Stats(this));
        this.getCommand("top").setExecutor(new Top(this));

        pm.registerEvents(new AsyncPlayerChat(this), this);
        pm.registerEvents(new BlockBreak(this), this);
        pm.registerEvents(new BlockPlace(this), this);
        pm.registerEvents(new EntityDamage(this), this);
        pm.registerEvents(new FoodLevelChange(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerDropItem(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new PlayerPickupItem(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
        pm.registerEvents(new ServerListPing(this), this);
        pm.registerEvents(new WorldEvents(this), this);
        pm.registerEvents(new ChestListener(this), this);
        pm.registerEvents(new SpecialItems(this), this);
    }


    private void registerChestItems() {
        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.COBBLESTONE, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.OAK_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.BIRCH_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.DARK_OAK_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.ACACIA_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.SPRUCE_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.JUNGLE_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.OAK_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.BIRCH_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.SPRUCE_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.DARK_OAK_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.ACACIA_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.JUNGLE_LOG, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.JUNGLE_PLANKS, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.STONE, new Random().nextInt(64)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.OAK_BOAT, 1));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.BIRCH_BOAT, 1));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.SPRUCE_BOAT, 1));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.DARK_OAK_BOAT, 1));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.ACACIA_BOAT, 1));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.JUNGLE_BOAT, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.STRING, new Random().nextInt(4)));
        }

        for(int i = 0; i < 9; i++) {
            chestItems.add(new ItemStack(Material.STICK, new Random().nextInt(9)));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.CRAFTING_TABLE, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.ENDER_PEARL, new Random().nextInt(2)));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.BOW, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.ARROW, new Random().nextInt(11)));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.FEATHER, new Random().nextInt(14)));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.FLINT, new Random().nextInt(14)));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.WOODEN_SWORD, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.STONE_SWORD, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.IRON_SWORD, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_SWORD, 1));
        }

        for(int i = 0; i < 3; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_SWORD, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.IRON_AXE, 1));
        }

        for(int i = 0; i < 4; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_AXE, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.LEATHER_HELMET, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.LEATHER_BOOTS, 1));
        }




        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_HELMET, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_CHESTPLATE, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_LEGGINGS, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.GOLDEN_BOOTS, 1));
        }




        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        }



        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.IRON_HELMET, 1));
        }

        for(int i = 0; i < 4; i++) {
            chestItems.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.IRON_BOOTS, 1));
        }




        for(int i = 0; i < 4; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_HELMET, 1));
        }

        for(int i = 0; i < 3; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        }

        for(int i = 0; i < 3; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        }

        for(int i = 0; i < 4; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
        }



        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.LAPIS_LAZULI, new Random().nextInt(18)));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.IRON_INGOT, new Random().nextInt(12)));
        }

        for(int i = 0; i < 8; i++) {
            chestItems.add(new ItemStack(Material.LEATHER, new Random().nextInt(9)));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.PAPER, new Random().nextInt(3)));
        }

        for(int i = 0; i < 7; i++) {
            chestItems.add(new ItemStack(Material.BOOK, new Random().nextInt(2)));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.EMERALD, 1));
        }

        for(int i = 0; i < 6; i++) {
            chestItems.add(new ItemStack(Material.GOLD_INGOT, 1));
        }

        for(int i = 0; i < 2; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND, 1));
        }

        for(int i = 0; i < 2; i++) {
            chestItems.add(new ItemStack(Material.DIAMOND_BLOCK, 1));
        }

        for(int i = 0; i < 2; i++) {
            chestItems.add(new ItemStack(Material.IRON_BLOCK,1));
        }

        for(int i = 0; i < 2; i++) {
            chestItems.add(new ItemStack(Material.GOLD_BLOCK, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.PUMPKIN, 1));
        }

        for(int i = 0; i < 4; i++) {
            chestItems.add(new ItemStack(Material.OBSIDIAN, 1));
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemStack(Material.TNT, 2));
        }



        for(int i = 0; i < 3; i++) {
            chestItems.add(new ItemBuilder(Material.BOW, 1)
                    .setDisPlayname("§cExplosionsbogen")
                    .setLore("", "§7Schiesse Explosionspfeile!")
                    .addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1)
                    .build());
        }

        for(int i = 0; i < 3; i++) {
            chestItems.add(new ItemBuilder(Material.BOW, 1)
                    .setDisPlayname("§4Feuerbogen")
                    .setLore("", "§7Schiesse Feuerpfeile!")
                    .addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1)
                    .addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1)
                    .build());
        }

        for(int i = 0; i < 5; i++) {
            chestItems.add(new ItemBuilder(Material.FEATHER, 1)
                    .setDisPlayname("§6Fallschirm")
                    .setLore("", "§7Erzeuge einen Fallschirm, der dich", "§7sanft zu Boden gleiten lässt!")
                    .build());
        }
    }
}
