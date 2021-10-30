/*
 * Copyright 2021 by Sebastian Thomschke and contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package de.sebthom.eclipse.open_with_eclipse;

import java.nio.file.Path;

/**
 * @author Sebastian Thomschke
 */
public interface Constants {

   String IMAGE_ICON = "src/main/resources/images/open_with_eclipse.png";

   Path LAUNCHER_EXE = Path.of(System.getProperty("eclipse.launcher", "eclipse.exe"));
   //Path LAUNCHER_EXE = Path.of("D:\\33\\HaxeStudio.exe");

   String LAUNCHER_NAME = System.getProperty("eclipse.launcher.name", "Eclipse");
}
