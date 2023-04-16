package org.example.ci;


import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ApplicationContext {
    private final ClassScanner classScanner = new ClassScanner();

    private final Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Class<?>[]> componentsToDep;

    public static void start(String basePackage) {
        new ApplicationContext().startContext(basePackage);
    }

    public void startContext(String basePackage) {
        var classesInPackage = classScanner.getClasses(basePackage);
        var componentClasses = filterInscopeComponent(classesInPackage);
        validateCompontens(componentClasses);

        componentsToDep = componentClasses.stream()
                .collect(
                        Collectors.toMap(
                                comp -> comp,
                                comp -> comp.getDeclaredConstructors()[0].getParameterTypes()
                        )

                );

        componentClasses.forEach(this::createInstance);
    }

    private void createInstance(Class<?> clazz) {
        try {
            if (instances.containsKey(clazz)) {
                return;
            }

            var deps = componentsToDep.get(clazz);
            for (Class<?> dep : deps) {
                createInstance(dep);
            }

            Object instance;
            if (deps.length == 0) {
                instance = clazz.getDeclaredConstructors()[0].newInstance();
            } else {
                var paramsInstance = stream(deps)
                        .map(instances::get)
                        .toArray();
                var constructor = clazz.getDeclaredConstructors()[0];
                instance = constructor.newInstance(paramsInstance);
            }
            instances.put(clazz, instance);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateCompontens(List<Class<?>> compontentClasses) {
        boolean tooManyConstructor = compontentClasses.stream()
                .anyMatch(comp -> comp.getDeclaredConstructors().length > 1);

        if (tooManyConstructor) {
            throw new IllegalStateException("to many constructor on class");
        }
    }

    private static List<Class<?>> filterInscopeComponent(Collection<Class<?>> classesInPackage) {
        return classesInPackage.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Component.class))
                .toList();
    }


}
