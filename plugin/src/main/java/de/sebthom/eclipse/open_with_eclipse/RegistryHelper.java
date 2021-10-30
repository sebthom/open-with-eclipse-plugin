/*
 * Copyright 2021 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse;

import static de.sebthom.eclipse.commons.system.win.WindowsRegistry.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.annotation.NonNull;

import de.sebthom.eclipse.commons.system.win.WindowsRegistry;
import net.sf.jstuff.core.Strings;
import net.sf.jstuff.core.validation.Args;

/**
 * @author Sebastian Thomschke
 */
public abstract class RegistryHelper {

   private static final String SOFTWARE_CLASSES_WILDCARD_SHELL = WindowsRegistry.join("SOFTWARE", "Classes", "*", "shell");

   public static void createOpenWithRegistryEntry(@NonNull final Path launcherExe, @NonNull final String contextMenuEntryLabel) {
      Args.exists("launcherExe", launcherExe);
      Args.notBlank("contextMenuEntryLabel", contextMenuEntryLabel);

      final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, contextMenuEntryLabel);
      HKEY_CURRENT_USER.createKey(keyPath, "command");
      HKEY_CURRENT_USER.setStringValue(keyPath, "icon", launcherExe.toAbsolutePath());
      HKEY_CURRENT_USER.setStringValue(keyPath, "entryCreatedBy", OpenWithEclipsePlugin.PLUGIN_ID);
      HKEY_CURRENT_USER.setStringValue(keyPath, "command", "@", getOpenWithCommand(launcherExe));
   }

   private static String getOpenWithCommand(@NonNull final Path launcherExe) {
      return "\"" + launcherExe.toAbsolutePath() + "\" --launcher.openFile=\"%V\"";
   }

   /**
    * @return a corresponding Open with entry from the registry for the given Eclipse exe or null
    */
   private static String getOpenWithRegistryEntry(@NonNull final Path launcherExe) {
      final var openWithCommand = getOpenWithCommand(launcherExe);

      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         if (openWithCommand.equals(HKEY_CURRENT_USER.getStringValue(keyPath, "command", "@")))
            return keyPath;
      }
      return null;
   }

   public static boolean hasOpenWithRegistryEntry() {
      return hasOpenWithRegistryEntry(Constants.LAUNCHER_EXE);
   }

   private static boolean hasOpenWithRegistryEntry(@NonNull final Path launcherExe) {
      Args.notNull("launcherExe", launcherExe);

      return getOpenWithRegistryEntry(launcherExe) != null;
   }

   public static int purgeInvalidOpenWithRegistryEntries() {
      var deleted = 0;
      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         final var entryCreatedBy = HKEY_CURRENT_USER.getStringValue(keyPath, "entryCreatedBy");
         if (OpenWithEclipsePlugin.PLUGIN_ID.equals(entryCreatedBy)) {
            final var command = HKEY_CURRENT_USER.getStringValue(keyPath, "command", "@");
            final var exePath = Strings.substringBetween(command, "\"", "\"");
            if (Strings.endsWithIgnoreCase(exePath, ".exe") //
               && !Files.exists(Path.of(exePath))) {
               HKEY_CURRENT_USER.deleteKey(keyPath, true);
               deleted++;
            }
         }
      }
      return deleted;
   }

   public static int removeOpenWithRegistryEntries(@NonNull final Path launcherExe) {
      Args.exists("launcherExe", launcherExe);
      var deleted = 0;
      final var launcherExePath = launcherExe.toAbsolutePath().toString();
      for (final var keyName : HKEY_CURRENT_USER.getKeys(SOFTWARE_CLASSES_WILDCARD_SHELL)) {
         final var keyPath = WindowsRegistry.join(SOFTWARE_CLASSES_WILDCARD_SHELL, keyName);
         final var entryCreatedBy = HKEY_CURRENT_USER.getStringValue(keyPath, "entryCreatedBy");
         if (OpenWithEclipsePlugin.PLUGIN_ID.equals(entryCreatedBy)) {
            final var command = HKEY_CURRENT_USER.getStringValue(keyPath, "command", "@");
            final var exePath = Strings.substringBetween(command, "\"", "\"");
            if (launcherExePath.equalsIgnoreCase(exePath)) {
               HKEY_CURRENT_USER.deleteKey(keyPath, true);
               deleted++;
            }
         }
      }
      return deleted;
   }
}
