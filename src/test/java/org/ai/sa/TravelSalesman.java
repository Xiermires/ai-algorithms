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

import org.junit.Test;

public class TravelSalesman
{
    @Test
    public void testTravelSalesman()
    {
        final SimulatedAnnealing<City> sa = new SimulatedAnnealing<>(sf, ec, 1e9, 1e-3);
        final List<City> best = sa.simulate();
        System.out.println(best);
        System.out.println("Best energy: " + ec.calculate(best));
    }

    static class City
    {
        int x, y;

        City(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString()
        {
            return "City [x=" + x + ", y=" + y + "]";
        }
    }

    final SolutionFactory<City> sf = new SolutionFactory<City>()
    {
        final Random rand = new Random(System.nanoTime());

        @Override
        public List<City> create()
        {
            final List<City> sol = new ArrayList<>();
            for (int i = 0; i < 20; i++)
                sol.add(new City(rand.nextInt(100), rand.nextInt(100)));
            return sol;
        }
    };

    final EnergyCalculator<City> ec = new EnergyCalculator<City>()
    {
        @Override
        public double calculate(List<City> solution)
        {
            double e = 0;
            for (int i = 0; i < solution.size() - 1; i++)
            {
                final City lhs = solution.get(i);
                final City rhs = solution.get(i + 1);
                e += Math.hypot(Math.abs(lhs.x - rhs.x), Math.abs(lhs.y - rhs.y));
            }
            return e;
        }
    };
}
