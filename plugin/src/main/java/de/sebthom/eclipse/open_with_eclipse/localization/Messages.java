/*
 * Copyright 2021-2022 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse.localization;

import de.sebthom.eclipse.commons.localization.MessagesInitializer;

/**
 * @author Sebastian Thomschke
 */
public final class Messages {

   private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages";

   // Keys with default values directly assigned in this class are only used by Java classes.
   // Keys without default values are loaded from messages.properties, because they are also referenced in plugin.xml

   // CHECKSTYLE:IGNORE .* FOR NEXT 100 LINES

   public static String OpenWithEclipse_PluginName;
   public static String OpenWithEclipse_OpenWith = "Open with {0}";
   public static String OpenWithEclipse_Pref_CreateOpenWithRegistration = "Create Windows context/right-click menu entry";
   public static String OpenWithEclipse_Pref_ContextMentyEntryLabel = "Menu entry name:";
   public static String OpenWithEclipse_NoOpenWithContextMenuEntry_Title = "Register {0} with Windows right-click menu?";
   public static String OpenWithEclipse_NoOpenWithContextMenuEntry_Question = "Do you want to create a menu entry for {0} now?";

   static {
      MessagesInitializer.initializeMessages(BUNDLE_NAME, Messages.class);
   }

   private Messages() {
   }
}
