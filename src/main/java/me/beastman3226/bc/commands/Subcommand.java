package me.beastman3226.bc.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Subcommand {
    
    public boolean consoleUse() default true;
    public int minArgs() default 0;
    public String permission() default "businesscore.core";
    public String usage();

}
