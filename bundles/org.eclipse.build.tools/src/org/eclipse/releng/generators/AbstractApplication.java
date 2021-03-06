/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.releng.generators;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines common behaviour for PDE Core applications.
 */
public abstract class AbstractApplication {

    /**
     * Helper method to ensure an array is converted into an ArrayList.
     */
    public static ArrayList getArrayList(final Object[] args) {
        // We could be using Arrays.asList() here, but it does not specify
        // what kind of list it will return. We do need a list that
        // implements the method List.remove(int) and ArrayList does.
        final ArrayList result = new ArrayList(args.length);
        for (int i = 0; i < args.length; i++) {
            result.add(args[i]);
        }
        return result;
    }

    /**
     * From a command line list, get the array of arguments of a given
     * parameter. The parameter and its arguments are removed from the list.
     * 
     * @return null if the parameter is not found or has no arguments
     */
    protected String[] getArguments(final List commands, final String param) {
        final int index = commands.indexOf(param);
        if (index == -1) {
            return null;
        }
        commands.remove(index);
        if (index == commands.size()) {
            return null;
        }
        final List args = new ArrayList(commands.size());
        while (index < commands.size()) { // while not the last command
            final String command = (String) commands.get(index);
            if (command.startsWith("-")) {
                break;
            }
            args.add(command);
            commands.remove(index);
        }
        if (args.isEmpty()) {
            return null;
        }
        return (String[]) args.toArray(new String[args.size()]);
    }

    /**
     * Looks for interesting command line arguments.
     */
    protected void processCommandLine(final List commands) {
    }

    /**
     * Starting point for application logic.
     */
    protected abstract void run() throws Exception;

    /*
     * @see IPlatformRunnable#run(Object)
     */
    public Object run(final Object args) throws Exception {
        processCommandLine(getArrayList((String[]) args));
        try {
            run();
        }
        catch (final Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

}
