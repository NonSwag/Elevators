package me.keehl.elevators.services;

import me.keehl.elevators.Elevators;
import me.keehl.elevators.actions.*;
import me.keehl.elevators.events.ElevatorRegisterActionsEvent;
import me.keehl.elevators.helpers.ItemStackHelper;
import me.keehl.elevators.models.ElevatorAction;
import me.keehl.elevators.models.ElevatorType;
import me.keehl.elevators.util.TriFunction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorActionService {

    // This should probably be turned into a DTO.
    private static final Map<String, TriFunction<JavaPlugin,ElevatorType, String, ElevatorAction>> actionConstructors = new HashMap<>();
    private static final Map<String, ItemStack> actionIcons = new HashMap<>();
    private static final Map<String, JavaPlugin> actionPlugins = new HashMap<>();

    private static boolean initialized = false;
    private static boolean allowSelfRegister = false;

    public static void init() {
        if(ElevatorActionService.initialized)
            return;
        Elevators.pushAndHoldLog();

        ElevatorActionService.registerDefaultActions();
        ElevatorActionService.initialized = true;
        Elevators.popLog(logData -> Elevators.log("Action service enabled. "+ ChatColor.YELLOW + "Took " + logData.getElapsedTime() + "ms"));
    }

    private static void registerDefaultActions() {

        allowSelfRegister = true;

        registerElevatorAction(Elevators.getInstance(), "sound", SoundAction::new, ChatColor.GREEN.toString(), "Sound", Material.MUSIC_DISC_MALL);
        registerElevatorAction(Elevators.getInstance(), "command-console", CommandConsoleAction::new, ChatColor.DARK_RED.toString(), "Console Command", Material.COMMAND_BLOCK);
        registerElevatorAction(Elevators.getInstance(), "command-player", CommandPlayerAction::new, ChatColor.LIGHT_PURPLE.toString(), "Player Command", Material.REPEATING_COMMAND_BLOCK);
        registerElevatorAction(Elevators.getInstance(), "message-player", MessagePlayerAction::new, ChatColor.YELLOW.toString(), "Message User", Material.WRITTEN_BOOK);
        registerElevatorAction(Elevators.getInstance(), "message-all", MessageAllAction::new, ChatColor.RED.toString(), "Broadcast Message", Material.ENCHANTED_BOOK);

        registerElevatorAction(Elevators.getInstance(), "effect", EffectAction::new, ChatColor.BLUE.toString(), "Effect", Material.FIREWORK_ROCKET);

        registerElevatorAction(Elevators.getInstance(), "title", TitleAction::new, ChatColor.LIGHT_PURPLE.toString(), "Title", Material.NAME_TAG);
        registerElevatorAction(Elevators.getInstance(), "action-bar", ActionBarAction::new, ChatColor.YELLOW.toString(), "Action Bar", Material.BELL);
        registerElevatorAction(Elevators.getInstance(), "boss-bar", BossBarAction::new, ChatColor.RED.toString(), "Boss Bar", Material.DRAGON_HEAD);

        registerElevatorAction(Elevators.getInstance(), "charge-exp", ChargeExpAction::new, ChatColor.GOLD.toString(), "Charge EXP", Material.EXPERIENCE_BOTTLE);
        registerElevatorAction(Elevators.getInstance(), "trigger-observer", TriggerObserverAction::new, ChatColor.RED.toString(), "Trigger Observer", Material.OBSERVER);

        allowSelfRegister = false;

        Bukkit.getPluginManager().callEvent(new ElevatorRegisterActionsEvent());
    }

    public static void registerElevatorAction(JavaPlugin plugin, String key, TriFunction<JavaPlugin,ElevatorType, String, ElevatorAction> actionConstructor, ItemStack icon) {

        if(plugin.getName().equalsIgnoreCase(Elevators.getInstance().getName()) && !allowSelfRegister)
            throw new RuntimeException("An invalid Plugin was provided when trying to register an Elevator Action.");

        key = key.toLowerCase().trim();
        actionIcons.put(key, icon);
        actionPlugins.put(key, plugin);
        actionConstructors.put(key, actionConstructor);

        if(!Elevators.isInitialized())
            return;

        for(ElevatorType type : ElevatorTypeService.getExistingElevatorTypes()) {
            type.onLoad();
        }
    }

    public static void registerElevatorAction(JavaPlugin plugin, String key, TriFunction<JavaPlugin,ElevatorType, String, ElevatorAction> actionConstructor, String chatColor, String displayName, Material itemType) {
        registerElevatorAction(plugin, key, actionConstructor, ItemStackHelper.createItem(chatColor + ChatColor.BOLD + displayName, itemType, 1));
    }

    public static ElevatorAction createActionFromString(ElevatorType elevatorType, String actionString) {
        if(!actionString.contains(":"))
            return null;
        String key = actionString.substring(0, actionString.indexOf(':')).toLowerCase();
        actionString = actionString.substring(actionString.indexOf(':') + 1);
        if (!actionConstructors.containsKey(key))
            return null;
        if (!actionPlugins.containsKey(key))
            return null;

        ElevatorAction action = actionConstructors.get(key).apply(actionPlugins.get(key), elevatorType, key);
        action.initialize(actionString);
        action.setIcon(actionIcons.get(key));

        return action;
    }

    public static List<String> getRegisteredActions() {
        return new ArrayList<>(actionIcons.keySet());
    }

    public static ItemStack getActionIcon(String actionKey) {
        return actionIcons.getOrDefault(actionKey, null);
    }

    public static ElevatorAction createBlankAction(ElevatorType elevatorType, String actionKey) {
        actionKey = actionKey.toLowerCase().trim();
        if(!actionConstructors.containsKey(actionKey))
            return null;
        if (!actionPlugins.containsKey(actionKey))
            return null;

        ElevatorAction action = actionConstructors.get(actionKey).apply(actionPlugins.get(actionKey), elevatorType, actionKey);
        action.initialize("");
        action.setIcon(actionIcons.get(actionKey));

        return action;
    }



}
