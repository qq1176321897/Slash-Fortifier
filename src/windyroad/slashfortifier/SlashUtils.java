package windyroad.slashfortifier;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class SlashUtils {
    static boolean isSlash(ItemStack item){
        try{
            net.minecraft.server.v1_7_R4.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
            if(itemStack==null)return false;
            boolean isSlash = itemStack.getTag().hasKey("AttackAmplifier");
            return isSlash;
        }catch (Exception e){
            return false;
        }
    }
}
