using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Twilight
{
	public class Normal
	{
		public Normal(double mean = 0.0, double variance = 1.0)
		{
			Mu = mean;
			Sigma = variance;

			rand = new Random();
			return;
		}

		public double next()
		{
			if(ready)
			{
				ready = false;
				return Math.Sqrt(Sigma * rand1) * Math.Sin(rand2) + Mu;
			}

			rand1 = rand.NextDouble();

			if(rand1 < 1e-100)
				rand1 = 1e-100;

			rand1 = -2.0 * Math.Log(rand1);

			rand2 = rand.NextDouble() * 2.0 * Math.PI;

			ready = true;
			return Math.Sqrt(Sigma * rand1) * Math.Cos(rand2) + Mu;
		}

		protected Random rand;

		protected double Mu;
		protected double Sigma;

		protected double rand1, rand2;
		protected bool ready = false;
	}
}
