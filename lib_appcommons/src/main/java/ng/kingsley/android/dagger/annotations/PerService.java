package ng.kingsley.android.dagger.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author ADIO Kingsley O.
 * @since 05 Jun, 2015
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
