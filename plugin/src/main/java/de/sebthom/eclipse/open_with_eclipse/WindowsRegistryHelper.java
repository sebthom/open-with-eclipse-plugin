/*
 * Copyright 2021-2022 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse;

import static de.sebthom.eclipse.commons.system.win.WindowsRegistry.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.annotation.Nullable;

import de.sebthom.eclipse.commons.system.win.WindowsRegistry;
import net.sf.jstuff.core.Strings;
import net.sf.jstuff.core.validation.Args;

/**
 * @author Sebastian Thomschke
 */
public abstract class WindowsRegistryHelper {

   private static final String SOFTWARE_CLASSES_WILDCARD_SHELL = WindowsRegistry.join("SOFTWARE", "Classes", "*", "shell");
   private static final @Nullable String DEFAULT_VALUE_NAME = null;

   public static void createOpenWithRegistryEntry(final Path launcherExe, final String contextMenuEntryLabel) {
      Args.isFileReadable("launcherExe", launcherExe);
      Args.notBlank("contextMenuEntryLabel", contextMenuEntryLabel);

      final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, contextMenuEntryLabel);
      HKEY_CURRENT_USER.createKey(keyPath, "command");
      HKEY_CURRENT_USER.setStringValue(keyPath, "icon", launcherExe.toAbsolutePath());
      HKEY_CURRENT_USER.setStringValue(keyPath, "entryCreatedBy", Plugin.PLUGIN_ID);
      HKEY_CURRENT_USER.setStringValue(keyPath, "command", DEFAULT_VALUE_NAME, getOpenWithCommand(launcherExe));
   }

   private static String getOpenWithCommand(final Path launcherExe) {
      return "\"" + launcherExe.toAbsolutePath() + "\" \"%V\"";
   }

   /**
    * @return a corresponding Open with entry from the registry for the given Eclipse exe or null
    */
   private static @Nullable String getOpenWithRegistryEntry(final Path launcherExe) {
      final var openWithCommand = getOpenWithCommand(launcherExe);

      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         if (openWithCommand.equals(HKEY_CURRENT_USER.getStringValue(keyPath, "command", DEFAULT_VALUE_NAME)))
            return keyPath;
      }
      return null;
   }

   public static boolean hasOpenWithRegistryEntry() {
      return hasOpenWithRegistryEntry(Constants.LAUNCHER_EXE);
   }

   private static boolean hasOpenWithRegistryEntry(final Path launcherExe) {
      Args.notNull("launcherExe", launcherExe);

      return getOpenWithRegistryEntry(launcherExe) != null;
   }

   public static int purgeInvalidOpenWithRegistryEntries() {
      var deleted = 0;
      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         final var entryCreatedBy = HKEY_CURRENT_USER.getStringValue(keyPath, "entryCreatedBy");
         if (Plugin.PLUGIN_ID.equals(entryCreatedBy)) {
            final var command = HKEY_CURRENT_USER.getStringValue(keyPath, "command", DEFAULT_VALUE_NAME);
            if (command != null) {
               final var exePath = Strings.substringBetween(command, "\"", "\"");
               if (Strings.endsWithIgnoreCase(exePath, ".exe") //
                     && !Files.exists(Path.of(exePath))) {
                  HKEY_CURRENT_USER.deleteKey(keyPath, true);
                  deleted++;
               }
            }
         }
      }
      return deleted;
   }

   public static int removeOpenWithRegistryEntries(final Path launcherExe) {
      var deleted = 0;
      final var launcherExePath = launcherExe.toAbsolutePath().toString();
      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         final var entryCreatedBy = HKEY_CURRENT_USER.getStringValue(keyPath, "entryCreatedBy");
         if (Plugin.PLUGIN_ID.equals(entryCreatedBy)) {
            final var command = HKEY_CURRENT_USER.getStringValue(keyPath, "command", DEFAULT_VALUE_NAME);
            if (command != null) {
               final var exePath = Strings.substringBetween(command, "\"", "\"");
               if (launcherExePath.equalsIgnoreCase(exePath)) {
                  HKEY_CURRENT_USER.deleteKey(keyPath, true);
                  deleted++;
               }
            }
         }
      }
      return deleted;
   }
}
