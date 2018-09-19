import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.ScriptCategory;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.ui.Log;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.component.Production;

@ScriptMeta(name = "MinaCBall ",  desc = "Smelt Cannonballs at Edgeville", developer = "Mina", category = ScriptCategory.SMITHING)
public class MinaCBall extends Script {

    public boolean HAVE_BARS = false;

    @Override
    public void onStart() {
        if (!Inventory.contains("Ammo mould")) {
            Log.info("No mould found, withdrawing");
            Npcs.getNearest("Banker").interact("Bank");
            if (Bank.open()) {
                Bank.withdraw("Ammo mould", 1);
                Time.sleep(300);
            }
            if (Bank.open() && !Bank.contains("Ammo mould")) {
                Log.info("No ammo mould found, stopping");
                setStopping(true);
            }
        }
    }

    @Override
    public int loop() {
        Player Local = Players.getLocal();
        SceneObject Furnace = SceneObjects.getNearest("Furnace");

        if (!Movement.isRunEnabled() && Movement.getRunEnergy() > 20 && !Bank.isOpen()) {
            Log.info("We have energy, let's run");
            Movement.toggleRun(true);
            return 1000;
        }

        if (Inventory.contains("Steel bar")) {
            HAVE_BARS = true;
        }

        else if (!Inventory.contains("Steel bar")) {
            HAVE_BARS = false;
        }

        if (Inventory.contains("Ammo mould") && !HAVE_BARS && (!Bank.isOpen())) {
            Npcs.getNearest("Banker").interact("Bank");
            Time.sleep(500);
            Log.info("Out of bars, withdrawing");
            return 2000;
        }
        if (Bank.isOpen() && !HAVE_BARS) {
            Bank.depositAll("Cannonball");
            Time.sleep(1000);
            Bank.withdrawAll("Steel bar");
            Bank.close();
            Time.sleep(1000);
        }

        if (Inventory.contains("Ammo mould") && HAVE_BARS && !Production.isOpen()) {
            Log.info("Smelting");
            Furnace.interact("Smelt");
            return 3000;
        }
        if (Production.isOpen()) {
            Time.sleep(500);
            Production.initiate();
            Time.sleepUntil(() -> !HAVE_BARS || Dialog.canContinue(), 165000);
        }

        if (Bank.isOpen() && !Bank.contains("Steel bar")) {
            Log.info("Out of bars, stopping");
            setStopping(true);
        }

        return 1000;
    }

    @Override
    public void onStop() {

    }
}