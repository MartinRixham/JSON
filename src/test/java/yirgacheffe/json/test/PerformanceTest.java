package yirgacheffe.json.test;

import yirgacheffe.json.JSONObject;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

public final class PerformanceTest
{
	private PerformanceTest()
	{
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("---------- downloading data ----------");
		URL url = new URL(
			"file:///home/martin/citylots.json");
		String data =
			new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

		System.out.println("---------- parsing JSON ----------");
		long startTime = getCPUTime();
		String obj = JSONObject.read(data).validate();
		long endTime = getCPUTime();

		System.out.println(
			"---------- parsing took " +
			Duration.ofNanos(endTime - startTime).toMillis() +
			" milliseconds ----------");
		System.out.println(obj);
	}

	private static long getCPUTime()
	{
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		return threadMXBean.getCurrentThreadCpuTime();
	}
}

// old JSONP implementation runs:
// ---------- parsing took 37 seconds ----------
// ---------- parsing took 13 seconds ----------
// ---------- parsing took 8 seconds ----------
// ---------- parsing took 2 seconds ----------

// initial parsing:
// ---------- parsing took 458 milliseconds ----------
// ---------- parsing took 464 milliseconds ----------
// ---------- parsing took 448 milliseconds ----------
// ---------- parsing took 442 milliseconds ----------

// validation:
// ---------- validation took 11,865 milliseconds ----------
// ---------- validation took 10,735 milliseconds ----------
// ---------- validation took 5,144 milliseconds ----------
// ---------- validation took 4,834 milliseconds ----------
