/*
 * Copyright 2021-2022 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IStartup;

import de.sebthom.eclipse.commons.system.win.WindowsRegistry;
import de.sebthom.eclipse.commons.ui.Texts;
import de.sebthom.eclipse.commons.ui.UI;
import de.sebthom.eclipse.open_with_eclipse.localization.Messages;
import de.sebthom.eclipse.open_with_eclipse.prefs.PluginPreference;
import net.sf.jstuff.core.Strings;
import net.sf.jstuff.core.ref.MutableObservableRef;

/**
 * @author Sebastian Thomschke
 */
public class StartupListener implements IStartup {

   @Override
   public void earlyStartup() {
      if (!WindowsRegistry.isSupported())
         return;

      WindowsRegistryHelper.purgeInvalidOpenWithRegistryEntries();

      if (PluginPreference.isCreateOpenWithRegistration() //
            && !WindowsRegistryHelper.hasOpenWithRegistryEntry()) {

         UI.run(() -> {
            final var menuEntryLabel = MutableObservableRef.of(PluginPreference.getOpenWithContextMenuEntryLabel());

            final var dlg = new MessageDialog( //
               UI.getShell(), //
               NLS.bind(Messages.OpenWithEclipse_NoOpenWithContextMenuEntry_Title, Constants.LAUNCHER_NAME), // window title
               Plugin.get().getSharedImage(Constants.IMAGE_ICON), // window icon
               NLS.bind("No \"{0}\" entry in the Windows right-click/context menu yet.", PluginPreference
                  .getOpenWithContextMenuEntryLabel()), // top message
               0, // top message icon, e.g. MessageDialog.INFORMATION
               1, // default button: OK
               IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL // button labels
            ) {
               @Override
               @SuppressWarnings("unused")
               protected Control createCustomArea(final Composite parent) {
                  new Label(parent, SWT.NONE); // spacer

                  final var question = new Label(parent, SWT.NONE);
                  question.setText(NLS.bind(Messages.OpenWithEclipse_NoOpenWithContextMenuEntry_Question, Constants.LAUNCHER_NAME));

                  final var group = new Composite(parent, SWT.BORDER);
                  group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                  group.setLayout(new GridLayout(2, false));

                  final var entryLabel = new Label(group, SWT.NONE);
                  entryLabel.setText(Messages.OpenWithEclipse_Pref_ContextMentyEntryLabel);
                  entryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

                  final var entryText = new Text(group, SWT.BORDER);
                  entryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                  Texts.bind(entryText, menuEntryLabel);
                  Texts.onModified(entryText, txt -> getButton(IDialogConstants.OK_ID).setEnabled(Strings.isNotBlank(txt)));

                  return group;
               }
            };

            if (dlg.open() == IDialogConstants.OK_ID) {
               PluginPreference.setCreateOpenWithRegistration(true);
               PluginPreference.setOpenWithContextMenuEntryLabel(menuEntryLabel.get());
               PluginPreference.save();
               WindowsRegistryHelper.createOpenWithRegistryEntry(Constants.LAUNCHER_EXE, menuEntryLabel.get());
            } else {
               PluginPreference.setCreateOpenWithRegistration(false);
               PluginPreference.save();
            }
         });
      }
   }
}
