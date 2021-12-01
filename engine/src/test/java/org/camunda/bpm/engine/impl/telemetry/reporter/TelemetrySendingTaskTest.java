package org.camunda.bpm.engine.impl.telemetry.reporter;

import junit.framework.TestCase;
import org.camunda.bpm.engine.impl.metrics.Meter;
import org.camunda.bpm.engine.impl.metrics.MetricsRegistry;
import org.camunda.bpm.engine.impl.telemetry.dto.Metric;
import org.camunda.bpm.engine.management.Metrics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.management.Metrics.*;

/**
 * @author Daniel Kelemen <daniel.kelemen@camunda.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class TelemetrySendingTaskTest extends TestCase {

  private TelemetrySendingTask telemetrySendingTask;
  @Mock
  private MetricsRegistry metricsRegistry;

  @Before
  public void setUp() {
    telemetrySendingTask = new TelemetrySendingTask(null, null, 1, null, null, null, metricsRegistry, 1, false);
  }

  private void putMeter(final Map<String, Meter> meters, final String metric) {
    final Meter meter = new Meter(metric);
    meter.markTimes((long) (Math.random() * 10));
    meters.put(metric, meter);
  }

  @Test
  public void testCalculateMetrics_shouldReturnInternalAndPublicNames() {
    // given
    final Map<String, Meter> meters = new HashMap<>();
    putMeter(meters, PROCESS_INSTANCES);
    putMeter(meters, DECISION_INSTANCES);
    putMeter(meters, FLOW_NODE_INSTANCES_START);
    putMeter(meters, EXECUTED_DECISION_ELEMENTS);
    Mockito.doReturn(meters).when(metricsRegistry).getTelemetryMeters();

    // when
    final Map<String, Metric> actual = telemetrySendingTask.calculateMetrics();

    // then
    Mockito.verify(metricsRegistry).getTelemetryMeters();

    Assert.assertEquals(7, actual.size());
    Assert.assertEquals(actual.get(ROOT_PROCESS_INSTANCE_START).getCount(), actual.get(PROCESS_INSTANCES).getCount());
    Assert.assertEquals(actual.get(EXECUTED_DECISION_INSTANCES).getCount(), actual.get(DECISION_INSTANCES).getCount());
    Assert.assertEquals(actual.get(ACTIVTY_INSTANCE_START).getCount(), actual.get(FLOW_NODE_INSTANCES_START).getCount());
  }
}