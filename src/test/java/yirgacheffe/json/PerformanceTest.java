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
			"https://raw.githubusercontent.com/prust/" +
			"wikipedia-movie-data/b8e3c086f4828520447a0d21835a509376a88099/movies.json");
		String data =
			new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

		System.out.println("---------- parsing JSON ----------");
		long startTime = getCPUTime();
		JSONArray json = new JSONArray(data);
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
