
package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.utils.TimeUtils;
import com.vabrant.actionsystem.test.performancetests.PerformanceTimer.PerformanceIteration;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PerformanceTimer implements Iterable<PerformanceIteration> {

	private final String noNamePrintHeader = "[PerformanceTimer]";
	private String printHeader;
	private int iteration = 0;
	private int maxIterations;
	private String name;
	private PerformanceIteration[] iterations;
	private PerformanceIterationIterator iterator;

	public PerformanceTimer (int maxIterations) {
		this.maxIterations = maxIterations;
		iterations = new PerformanceIteration[maxIterations];
		for (int i = 0; i < maxIterations; i++) {
			iterations[i] = new PerformanceIteration();
		}
		iterator = new PerformanceIterationIterator();
		printHeader = noNamePrintHeader;
	}

	public void setName (String name) {
		if (name == null) throw new IllegalArgumentException("Name is null.");
		this.name = name;
		printHeader = noNamePrintHeader.substring(0, noNamePrintHeader.length() - 1) + " " + name + "]";
	}

	public void start () {
		if (iteration == maxIterations) clear();
		iteration++;
		iterations[iteration - 1].start(TimeUtils.nanoTime(), iteration);
	}

	public void end () {
		iterations[iteration - 1].setTotalTime(TimeUtils.nanoTime());
	}

	public float getAverage () {
		long sum = 0;
		for (int i = 0, end = iteration; i < end; i++) {
			sum += iterations[i].totalTime;
		}
		return (float)(sum / iteration) / 1000000f;
	}

	public void clear () {
		iteration = 0;

		for (int i = 0; i < maxIterations; i++) {
			iterations[i].clear();
		}
	}

	public void reset () {
		name = null;
		printHeader = noNamePrintHeader;
		clear();
	}

	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder(100);

		builder.append(printHeader);

		builder.append(' ');
		builder.append("Iterations:");
		builder.append(iteration);
		builder.append('\n');

		builder.append(printHeader);

		builder.append(' ');
		builder.append("Average:" + getAverage());

		for (int i = 0; i < iteration; i++) {
			PerformanceIteration it = iterations[i];

			builder.append('\n');

			builder.append(printHeader);

			builder.append(' ');
			builder.append("Iteration:");
			builder.append(it.iteration);

			builder.append(' ');
			builder.append("TotalTime:");
			builder.append(it.getTotalTimeMilli());
		}

		return builder.toString();
	}

	@Override
	public Iterator<PerformanceIteration> iterator () {
		iterator.reset();
		return iterator;
	}

	public class PerformanceIteration {

		private boolean isEmpty = true;
		long totalTime;
		long startTime;
		int iteration;
		// String description;

		private void start (long startTime, int iteration) {
			this.startTime = startTime;
			this.iteration = iteration;
			isEmpty = false;
		}

		public boolean isEmpty () {
			return isEmpty;
		}

		private void setTotalTime (long currentTime) {
			totalTime = currentTime - startTime;
		}

		public float getTotalTimeMilli () {
			return (float)totalTime / 1000000f;
		}

		public void clear () {
			iteration = 0;
			startTime = 0;
			totalTime = 0;
			isEmpty = true;
		}

		@Override
		public String toString () {
			StringBuilder builder = new StringBuilder(70);

			builder.append('[');
			builder.append(PerformanceTimer.class.getSimpleName());
			builder.append(' ');
			if (name != null) builder.append(name);
			builder.append(']');

			builder.append(' ');

			builder.append("Iteration:");
			builder.append(iteration);

			builder.append(' ');

			builder.append("TotalTime:");
			builder.append(getTotalTimeMilli());

			return builder.toString();
		}
	}

	private class PerformanceIterationIterator implements Iterator<PerformanceIteration> {
		int index;

		public void reset () {
			index = 0;
		}

		@Override
		public boolean hasNext () {
			return index < iterations.length;
		}

		@Override
		public PerformanceIteration next () {
			if (index >= iterations.length) throw new NoSuchElementException(String.valueOf(index));
			return iterations[index++];
		}
	}
}
