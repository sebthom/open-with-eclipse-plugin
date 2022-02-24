/*
 * Copyright 2021-2022 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse.prefs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.sebthom.eclipse.commons.prefs.fieldeditor.LabelFieldEditor;
import de.sebthom.eclipse.open_with_eclipse.Constants;
import de.sebthom.eclipse.open_with_eclipse.WindowsRegistryHelper;
import de.sebthom.eclipse.open_with_eclipse.localization.Messages;

/**
 * @author Sebastian Thomschke
 */
public final class PluginPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

   @Override
   protected void createFieldEditors() {
      final var parent = getFieldEditorParent();

      addField(new BooleanFieldEditor( //
         PluginPreference.PREFKEY_CREATE_OPEN_WITH_REGISTRATION, //
         Messages.OpenWithEclipse_Pref_CreateOpenWithRegistration, //
         parent //
      ));

      addField(new LabelFieldEditor("", parent));

      addField(new StringFieldEditor( //
         PluginPreference.PREFKEY_OPEN_WITH_CONTEXT_MENU_ENTRY_LABEL, //
         Messages.OpenWithEclipse_Pref_ContextMentyEntryLabel, //
         parent //
      ));
   }

   @Override
   public boolean performOk() {
      if (super.performOk()) {
         WindowsRegistryHelper.removeOpenWithRegistryEntries(Constants.LAUNCHER_EXE);
         if (PluginPreference.isCreateOpenWithRegistration()) {
            WindowsRegistryHelper.createOpenWithRegistryEntry(Constants.LAUNCHER_EXE, PluginPreference.getOpenWithContextMenuEntryLabel());
         }
         return true;
      }
      return false;
   }

   public PluginPreferencePage() {
      super(FieldEditorPreferencePage.GRID);
   }

   @Override
   public void init(final IWorkbench workbench) {
      setPreferenceStore(PluginPreference.STORE);
   }
}
