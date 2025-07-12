package me.keehl.elevators.models.settings;

import me.keehl.elevators.models.Elevator;
import me.keehl.elevators.models.ElevatorType;
import me.keehl.elevators.util.InternalElevatorSettingType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckPermsSetting extends InternalElevatorSetting<Boolean> {

    public CheckPermsSetting(JavaPlugin plugin) {
        super(plugin, InternalElevatorSettingType.CHECK_PERMS.getSettingName(),"Check Perms", "If enabled, the player must have access to elevator 'use', 'dye', and 'craft' permissions to have access to their respective abilities.", Material.ANVIL, ChatColor.DARK_GRAY);
    }

    @Override
    public boolean canBeEditedIndividually(Elevator elevator) {
        return false;
    }

    @Override
    public void onClickGlobal(Player player, ElevatorType elevatorType, Runnable returnMethod, InventoryClickEvent clickEvent, Boolean currentValue) {
        elevatorType.setElevatorRequiresPermissions(!currentValue);
        returnMethod.run();
    }

    @Override
    public void onClickIndividual(Player player, Elevator elevator, Runnable returnMethod, InventoryClickEvent clickEvent, Boolean currentValue) {
        returnMethod.run();
    }

    @Override
    public Boolean getGlobalValue(ElevatorType elevatorType) {
        return elevatorType.doesElevatorRequirePermissions();
    }

}
