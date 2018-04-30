package craftedMods.recipes.api;

import java.lang.annotation.*;

/**
 * An annotation marking several handlers of the API as registered. Registered handler will be - if
 * they're enabled - automatically loaded by the provider. </br>
 * <b>Important:</b> Every registered handler needs a zero argument constructor to be instantiated.
 * 
 * @author CraftedMods
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredHandler {

	/**
	 * If true, a handler instance will be created and the handler will be loaded, if false, not
	 * 
	 * @return Whether the handler is enabled
	 */
	boolean isEnabled() default true;

}
