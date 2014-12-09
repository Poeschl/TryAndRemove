package de.poeschl.apps.debuganddelete.annotations;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Markus Pöschl on 09.12.2014.
 */
@Qualifier
@Retention(RUNTIME)
public @interface SettingsDrawerSeen {
}
