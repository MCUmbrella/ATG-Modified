package ttftcuts.atg.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import ttftcuts.atg.ATG;

import java.lang.reflect.Field;

public class ObfuscationReflectionHelperEx {
    public static Field findField(Class clz, String fieldName) {
        String[] obfNames = ObfuscationReflectionHelper.remapFieldNames(clz.getName(), fieldName);
        if(obfNames.length == 0) {
            return null;
        }

        try {
            return ReflectionHelper.findField(clz, obfNames);
        } catch (ReflectionHelper.UnableToFindFieldException e) {
            ATG.logger.warn("Could not find field '%s' in class '%s'! Exception=%s", fieldName, clz.getName(), e);
        }

        return null;
    }
}
