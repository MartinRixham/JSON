package yirgacheffe.json;

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
			"https://raw.githubusercontent.com" +
				"/zemirco/sf-city-lots-json/master/citylots.json");
		String data =
			new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

		System.out.println("---------- parsing JSON ----------");
		long startTime = getCPUTime();
		JSONObject json = new JSONObject(data);
		long endTime = getCPUTime();

		System.out.println(
			"---------- parsing took " +
				Duration.ofNanos(endTime - startTime).getSeconds() +
				" seconds ----------");

		//System.out.println("---------- printing ----------");
		//System.out.println(json.toString());
	}

	private static long getCPUTime()
	{
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		return threadMXBean.getCurrentThreadCpuTime();
	}
}

// previous runs:
// ---------- parsing took 56 seconds ----------
// ---------- parsing took 47 seconds ----------
// ---------- parsing took 45 seconds ----------
// ---------- parsing took 9 seconds ----------
// ---------- parsing took 1 seconds ----------

// bigger example JSON file
// ---------- parsing took 37 seconds ----------
// ---------- parsing took 13 seconds ----------
