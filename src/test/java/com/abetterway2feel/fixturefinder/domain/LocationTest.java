package com.abetterway2feel.fixturefinder.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocationTest {

    @Test
    public void toStringForENGLAND_ShouldBeENGLAND() {
        assertThat(Location.ENGLAND.toString(), is("England"));
    }

    @Test
    public void toStringForTHE_UNITED_STATES_ShoulBeThe_United_StatesWithSpaces() {
        assertThat(Location.THE_UNITED_STATES.toString(), is("The United States"));
    }
}
