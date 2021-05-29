package me.kodysimpson.simpapi.command;

import java.util.List;

@FunctionalInterface
public interface CommandList {

    /**
     * @param subCommandList A list of all the subcommands you can display
     */
    void displayCommandList(List<SubCommand> subCommandList);

}
