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
			"https://raw.githubusercontent.com/zemirco/sf-city-lots-json/" +
				"33c27c137784a96d0fbd7f329dceda6cc7f49fa3/citylots.json");
		String data =
			new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

		System.out.println("---------- parsing JSON ----------");
		long startTime = getCPUTime();
		JSONObject.read(data);
		long endTime = getCPUTime();

		System.out.println(
			"---------- parsing took " +
			Duration.ofNanos(endTime - startTime).toMillis() +
			" milliseconds ----------");
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

// validation:
// ---------- validation took 11,865 milliseconds ----------
// ---------- validation took 10,735 milliseconds ----------
