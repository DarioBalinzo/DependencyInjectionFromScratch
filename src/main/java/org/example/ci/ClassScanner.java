package org.example.ci;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;

import java.util.Collection;

public class ClassScanner {
    public Collection<Class<?>> getClasses(String packageName) {
        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        String packageRelPath = packageName.replace(".", "/");
        SearchConfig config = SearchConfig.forResources(
                packageRelPath
        );

        try (ClassHunter.SearchResult result = classHunter.findBy(config)) {
            return result.getClasses();
        }
    }
}
