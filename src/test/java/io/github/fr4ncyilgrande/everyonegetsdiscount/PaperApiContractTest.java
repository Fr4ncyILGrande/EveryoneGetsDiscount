package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaperApiContractTest {

    @Test
    void paper263ExposesTheReputationContractUsedAtRuntime() throws ReflectiveOperationException {
        Class<?> villagerClass = Class.forName("org.bukkit.entity.Villager");
        Class<?> reputationClass = Class.forName("com.destroystokyo.paper.entity.villager.Reputation");
        Class<?> reputationTypeClass = Class.forName("com.destroystokyo.paper.entity.villager.ReputationType");

        Method getVillagerReputation = villagerClass.getMethod("getReputation", UUID.class);
        Method setVillagerReputation = villagerClass.getMethod("setReputation", UUID.class, reputationClass);
        Method getReputationValue = reputationClass.getMethod("getReputation", reputationTypeClass);
        Method setReputationValue = reputationClass.getMethod("setReputation", reputationTypeClass, int.class);

        assertNotNull(getVillagerReputation);
        assertNotNull(setVillagerReputation);
        assertNotNull(getReputationValue);
        assertNotNull(setReputationValue);
        assertNotNull(reputationTypeClass.getField("MAJOR_POSITIVE").get(null));
        assertNotNull(reputationTypeClass.getField("MINOR_POSITIVE").get(null));
    }
}
