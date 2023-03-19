package stresstest.stresstest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public final class Stresstest extends JavaPlugin {

    public int taskID = -1, seconds = 0, entities = 0, rest = 0;

    @Override
    public void onEnable() {

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings on");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (rest <= 0) {
                for (int i = 0; i < 500; i++)
                    getServer().getWorld("world").spawnEntity(getServer().getWorld("world").getSpawnLocation(), EntityType.CHICKEN);
                entities += 500;
                Bukkit.getServer().sendMessage(Component.text(entities + "마리, tps: " + Bukkit.getServer().getTPS()[0]));
                if (Bukkit.getServer().getTPS()[0] <= 18.0f) {
                    Bukkit.getServer().sendMessage(Component.text("tps가 18이하 이므로 모든 엔티티를 제거 후 5초간 휴식을 취합니다."));
                    for (LivingEntity livingEntity : getServer().getWorld("world").getLivingEntities()) {
                        if (livingEntity instanceof Chicken chicken) chicken.damage(chicken.getHealth());
                        if (livingEntity instanceof Item item) item.remove();
                    }
                    entities = 0;
                    rest = 5;
                }
            }
            seconds++;
            rest--;
            if (seconds >= 180) {
                Bukkit.getServer().sendMessage(Component.text("엔티티 테스트를 종료합니다."));
                for (LivingEntity livingEntity : getServer().getWorld("world").getLivingEntities()) {
                    if (livingEntity instanceof Chicken chicken) chicken.damage(chicken.getHealth());
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings report");
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }, 200L, 20L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
