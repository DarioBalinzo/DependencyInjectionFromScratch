package org.example.ci;

import java.lang.annotation.*;

/**
 * Client class should use this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

}
