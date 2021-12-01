package org.camunda.bpm.engine.impl.metrics;/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.camunda.bpm.engine.management.Metrics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Kelemen <daniel.kelemen@camunda.com>
 */
@RunWith(Parameterized.class)
public class MetricsQueryImplTest {

  private final MetricsQueryImpl metricsQuery = new MetricsQueryImpl(null);

  @Parameterized.Parameter
  public String parameterName;
  @Parameterized.Parameter(1)
  public String expectedName;

  @Parameterized.Parameters(name = "Scenario {index} - {0}")
  public static Collection<Object[]> getRequestUrls() {
    return Arrays.asList(new Object[][] {
        // metrics without mapping
        { "something", "something" },
        { Metrics.JOB_LOCKED_EXCLUSIVE, Metrics.JOB_LOCKED_EXCLUSIVE },
        // metrics with mapping
        { Metrics.FLOW_NODE_INSTANCES_START, Metrics.ACTIVTY_INSTANCE_START },
        { Metrics.FLOW_NODE_INSTANCES_END, Metrics.ACTIVTY_INSTANCE_END },
        { Metrics.PROCESS_INSTANCES, Metrics.ROOT_PROCESS_INSTANCE_START },
        { Metrics.DECISION_INSTANCES, Metrics.EXECUTED_DECISION_INSTANCES },
        { Metrics.TASK_USERS, Metrics.UNIQUE_TASK_WORKERS }
    });
  }

  @Test
  public void testName_shouldResolveInternalNames() {
    metricsQuery.name(parameterName);
    Assert.assertEquals(expectedName, metricsQuery.getName());
  }

}
