/*******************************************************************************
 * Copyright (c) 2017, Xavier Miret Andres <xavier.mires@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package org.ai.sa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing<T>
{
    private final Random rand = new Random(System.nanoTime());
    private final int[] swap = new int[2];

    private final SolutionFactory<T> sf;
    private final EnergyCalculator<T> ec;

    private final double cr;
    private double t;

    public SimulatedAnnealing(SolutionFactory<T> solutionFactory, EnergyCalculator<T> energyCalculator, double temperature,
            double coolingRate)
    {
        this.sf = solutionFactory;
        this.ec = energyCalculator;
        this.t = temperature;
        this.cr = coolingRate;
    }

    public List<T> simulate()
    {
        final List<T> solution = sf.create();
        List<T> best = new ArrayList<>(solution);
        double bestE = ec.calculate(best);

        while (t > 1e-10)
        {
            final double before = ec.calculate(solution);
            final int[] swap = randomSwap(solution);
            final double after = ec.calculate(solution);

            final double ap = acceptanceProbability(before, after, t);
            if (rand.nextDouble() > ap)
                swapItems(solution, swap[0], swap[1]);
            else if (ap == 1d && bestE > after)
            {
                best = new ArrayList<>(solution);
                bestE = after;
            }
            t *= 1 - cr;
        }
        return best;
    }

    private int[] randomSwap(List<T> s)
    {
        swap[0] = rand.nextInt(s.size());
        swap[1] = rand.nextInt(s.size());
        swapItems(s, swap[0], swap[1]);
        return swap;
    }

    private void swapItems(List<T> s, int lhs, int rhs)
    {
        final T item = s.get(lhs);
        s.set(lhs, s.get(rhs));
        s.set(rhs, item);
    }

    double acceptanceProbability(double before, double after, double temperature)
    {
        return before > after ? 1d : Math.exp((before - after) / temperature);
    }
}
