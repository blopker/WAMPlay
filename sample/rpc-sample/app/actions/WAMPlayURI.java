package actions;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WAMPlayURI {
	String value() default "";
}
