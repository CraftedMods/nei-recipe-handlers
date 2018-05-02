/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.utils;

import java.lang.annotation.Annotation;
import java.util.*;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Loader;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ClassDiscoverer {

	private final Logger logger;

	private Map<Class<? extends Annotation>, Set<Class<?>>> registeredClasses = new HashMap<>();
	private Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses = new HashMap<>();

	private Thread discovererThread;

	private boolean canRegister = true;

	public ClassDiscoverer(Logger logger) {
		this.logger = logger;
	}

	public boolean registerClassToDiscover(Class<? extends Annotation> annotationClass, Class<?> interfaceClass) {
		if (this.canRegister) {
			if (!this.registeredClasses.containsKey(annotationClass)) {
				this.registeredClasses.put(annotationClass, new HashSet<>());
			}
			if (!this.discoveredClasses.containsKey(annotationClass)) {
				this.discoveredClasses.put(annotationClass, new HashMap<>());
			}
			if (!this.discoveredClasses.get(annotationClass).containsKey(interfaceClass)) {
				this.discoveredClasses.get(annotationClass).put(interfaceClass, new HashSet<>());
			}
			return this.registeredClasses.get(annotationClass).add(interfaceClass);
		}
		return false;
	}

	public void discoverClassesAsync() {
		this.canRegister = false;
		this.discovererThread = new Thread(() -> {
			long start = System.currentTimeMillis();
			FastClasspathScanner scanner = new FastClasspathScanner();
			for (Class<? extends Annotation> annotationClass : this.registeredClasses.keySet()) {
				scanner.matchClassesWithAnnotation(annotationClass, clazz -> {
					try {
						Class<?> loadedClass = Loader.instance().getModClassLoader().loadClass(clazz.getName());
						for (Class<?> interfaceClass : this.registeredClasses.get(annotationClass))
							if (interfaceClass.isAssignableFrom(loadedClass)) {
								this.discoveredClasses.get(annotationClass).get(interfaceClass).add(loadedClass);
							}

					} catch (Exception e) {
						this.logger.error("Couldn't load class \"" + clazz.getName() + "\"", e);
					}
				});
			}
			scanner.scan(Runtime.getRuntime().availableProcessors());
			this.logger.info("Scanned the classpath in " + (System.currentTimeMillis() - start) + " milliseconds");
		});
		this.discovererThread.start();
	}

	public Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> getDiscoveredClasses(long timeout) {
		try {
			this.discovererThread.join(timeout);
		} catch (InterruptedException e) {
			this.logger.error("The class discoverer thread was interrupted", e);
		}
		return this.discoveredClasses;
	}

}
