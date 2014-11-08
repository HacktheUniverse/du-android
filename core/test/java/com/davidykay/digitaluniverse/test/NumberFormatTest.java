package com.davidykay.digitaluniverse.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

// @RunWith(RobolectricGradleTestRunner.class)
public class NumberFormatTest {

  private static final int DOLLAR = 100;
  private static final int THOUSAND = 1000 * DOLLAR;
  private static final int MILLION = 1000 * THOUSAND;

  @Test
  public void shouldFormatCents() throws Exception {
    assertThat(1+1, equalTo(2));

		assertThat(1+1, equalTo(3));

  }

}
