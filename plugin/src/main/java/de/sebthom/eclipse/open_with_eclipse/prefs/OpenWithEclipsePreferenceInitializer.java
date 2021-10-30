/*
 * Copyright 2021 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.osgi.util.NLS;

import de.sebthom.eclipse.open_with_eclipse.Constants;
import de.sebthom.eclipse.open_with_eclipse.localization.Messages;

/**
 * @author Sebastian Thomschke
 */
public final class OpenWithEclipsePreferenceInitializer extends AbstractPreferenceInitializer {

   @Override
   public void initializeDefaultPreferences() {
      OpenWithEclipsePreference.STORE.setDefault(OpenWithEclipsePreference.PREFKEY_CREATE_OPEN_WITH_REGISTRATION, true);
      OpenWithEclipsePreference.STORE.setDefault(OpenWithEclipsePreference.PREFKEY_OPEN_WITH_CONTEXT_MENU_ENTRY_LABEL, //
         NLS.bind(Messages.OpenWithEclipse_OpenWith, Constants.LAUNCHER_NAME) //
      );
   }
}
