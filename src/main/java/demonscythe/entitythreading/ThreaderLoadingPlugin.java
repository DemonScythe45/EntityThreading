package demonscythe.entitythreading;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class ThreaderLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        System.out.println("ASM transformer class requested");
        return new String[] {"demonscythe.entitythreading.transform.WorldClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "demonscythe.entitythreading.ThreaderContainer";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
