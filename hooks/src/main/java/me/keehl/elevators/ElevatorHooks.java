package me.keehl.elevators;

import me.keehl.elevators.services.ElevatorHookService;
import me.keehl.elevators.services.hooks.*;

public class ElevatorHooks {

    public static void buildHooks() {
        ElevatorHookService.registerHook("GriefPrevention", GriefPreventionHook.class);
        ElevatorHookService.registerHook("GriefDefender", GriefDefenderHook.class);
        ElevatorHookService.registerHook("RedProtect", RedProtectHook.class);
        ElevatorHookService.registerHook("PlotSquared", PlotSquaredHook.class);
        ElevatorHookService.registerHook("BentoBox", BentoBoxHook.class);
        ElevatorHookService.registerHook("SuperiorSkyblock2", SuperiorSkyblock2Hook.class, false);
        ElevatorHookService.registerHook("Protect", ProtectHook.class);
        ElevatorHookService.registerHook("Lands", LandsHook.class, false);

        ElevatorHookService.registerHook("DecentHolograms", DecentHologramsHook.class);
        ElevatorHookService.registerHook("FancyHolograms", FancyHologramsHook.class);

        ElevatorHookService.registerHook("PlaceholderAPI", PlaceholderAPIHook.class);

        ElevatorHookService.registerHook("ItemsAdder", ItemsAdderHook.class);
        ElevatorHookService.registerHook("Oraxen", OraxenHook.class);
        ElevatorHookService.registerHook("Nexo", NexoHook.class);
    }

}
