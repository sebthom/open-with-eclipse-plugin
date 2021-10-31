/*
 * Copyright 2021 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse.prefs;

import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import de.sebthom.eclipse.open_with_eclipse.Plugin;
import net.sf.jstuff.core.io.RuntimeIOException;

/**
 * @author Sebastian Thomschke
 */
public final class PluginPreference {

   public static final String PREFKEY_CREATE_OPEN_WITH_REGISTRATION = "createOpenWithRegistration";
   public static final String PREFKEY_OPEN_WITH_CONTEXT_MENU_ENTRY_LABEL = "openWithContextMenuEntryLabel";

   public static final IPersistentPreferenceStore STORE = Plugin.get().getPreferenceStore();

   public static void addListener(final IPropertyChangeListener listener) {
      STORE.addPropertyChangeListener(listener);
   }

   public static String getOpenWithContextMenuEntryLabel() {
      return STORE.getString(PREFKEY_OPEN_WITH_CONTEXT_MENU_ENTRY_LABEL);
   }

   public static boolean isCreateOpenWithRegistration() {
      return STORE.getBoolean(PREFKEY_CREATE_OPEN_WITH_REGISTRATION);
   }

   public static void removeListener(final IPropertyChangeListener listener) {
      STORE.removePropertyChangeListener(listener);
   }

   public static void save() {
      if (STORE.needsSaving()) {
         try {
            STORE.save();
         } catch (final IOException ex) {
            throw new RuntimeIOException(ex);
         }
      }
   }

   public static void setCreateOpenWithRegistration(final boolean value) {
      STORE.setValue(PREFKEY_CREATE_OPEN_WITH_REGISTRATION, value);
   }

   public static void setOpenWithContextMenuEntryLabel(final String value) {
      STORE.setValue(PREFKEY_OPEN_WITH_CONTEXT_MENU_ENTRY_LABEL, value);
   }

   private PluginPreference() {
   }
}
