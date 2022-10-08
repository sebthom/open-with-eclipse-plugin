/*
 * Copyright 2021-2022 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse;

import static net.sf.jstuff.core.validation.NullAnalysisHelper.*;

import java.nio.file.Path;

/**
 * @author Sebastian Thomschke
 */
public interface Constants {

   String IMAGE_ICON = "src/main/resources/images/open_with_eclipse.png";

   Path LAUNCHER_EXE = Path.of(asNonNull(System.getProperty("eclipse.launcher", "eclipse.exe")));

   String LAUNCHER_NAME = asNonNull(System.getProperty("eclipse.launcher.name", "Eclipse"));
}
