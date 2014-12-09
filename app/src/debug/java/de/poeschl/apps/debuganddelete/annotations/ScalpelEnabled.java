package de.poeschl.apps.debuganddelete.annotations;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Taken from the sample app u2020 from Jake Wharton.
 *
 * @see <a href="https://github.com/JakeWharton/u2020">GitHub</a>
 */
@Qualifier
@Retention(RUNTIME)
public @interface ScalpelEnabled {
}
