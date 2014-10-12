using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Equestrian_Geometry
{
	public partial class EquestrianGeometry : Form
	{
		public EquestrianGeometry()
		{
			InitializeComponent();

			colors = new List<Color>();
			
			for(int i = 0;i < 4;i++)
				for(int j = 0;j < 4;j++)
					for(int k = 0;k < 4;k++)
						colors.Add(Color.FromArgb(i * 255 / 3,j * 255 / 3,k * 255 / 3));
			
			richTextBox1.Text = "Controls: e - Enable manual (no going back!)\nSpace Bar - Make current line permanent   Up/Down - Change line color\nLeft/Right - Rotate line\nPage Up/Page Down - Change rotation rate\n\nParabola Form: 4p(y-k) = (x-h)^2\np - Focus distance";
			
			Enable(false);
			UpdateBoxes();
			return;
		}
		
		protected void UpdateBoxes()
		{
			textBox1.Text = p.ToString();
			textBox2.Text = k.ToString();
			textBox3.Text = h.ToString();
			textBox4.Text = min.ToString();
			textBox5.Text = max.ToString();
			textBox6.Text = EquusOffset.X.ToString();
			textBox7.Text = EquusOffset.Y.ToString();
			textBox8.Text = rotation.ToString();
			textBox9.Text = color.ToString();

			return;
		}

		protected void Enable(bool b)
		{
			textBox1.Enabled = b;
			textBox2.Enabled = b;
			textBox3.Enabled = b;
			textBox4.Enabled = b;
			textBox5.Enabled = b;
			textBox6.Enabled = b;
			textBox7.Enabled = b;
			textBox8.Enabled = b;
			textBox9.Enabled = b;
			button1.Visible = b;

			return;
		}

		protected override void OnPaint(PaintEventArgs e)
		{
			base.OnPaint(e);

			Pen pen = new Pen(Color.Black,1);
			e.Graphics.DrawLines(pen,GetParabola(p,k,h,min,max).ToArray());

			e.Graphics.DrawEllipse(pen,h - 1,k - 1 + p,2,2);
			e.Graphics.DrawLine(pen,new Point(min,k - 1 + p),new Point(max,k - 1 + p));

			e.Graphics.DrawEllipse(pen,h - 1 + EquusOffset.X,k - 1 + p + EquusOffset.Y,2,2);

			foreach(Tuple<Point,Color,float> l in marked)
				DrawRay(e.Graphics,l.Item2,new Point(h - 1 + l.Item1.X,k - 1 + p + l.Item1.Y),l.Item3,p,k,h);

			pen = new Pen(colors[color],1);
			DrawRay(e.Graphics,colors[color],new Point(h - 1 + EquusOffset.X,k - 1 + p + EquusOffset.Y),rotation,p,k,h);

			return;
		}

		// space marks
		// up/down changes color
		// left/right rotates
		// page up/down changed rotation speed
		protected override void OnKeyUp(KeyEventArgs e)
		{
			switch(e.KeyCode)
			{
			case Keys.Escape:
				Environment.Exit(0);
				break;
			case Keys.E:
				Enable(true);
				break;
			case Keys.Up:
				if(++color >= colors.Count)
					color = 0;

				break;
			case Keys.Down:
				if(--color < 0)
					color = colors.Count - 1;

				break;
			case Keys.Left:
				rotation -= delta;

				while(rotation < 0.0f)
					rotation += 2.0f * (float)Math.PI;

				break;
			case Keys.Right:
				rotation += delta;

				while(rotation >= 2.0f * (float)Math.PI)
					rotation -= 2.0f * (float)Math.PI;

				break;
			case Keys.PageUp:
				if(delta < 5.0f * fixed_delta)
					delta += fixed_delta;

				break;
			case Keys.PageDown:
				if(delta > fixed_delta)
					delta -= fixed_delta;

				break;
			case Keys.Space:
				marked.Add(new Tuple<Point,Color,float>(new Point(EquusOffset.X,EquusOffset.Y),colors[color],rotation));
				break;
			}

			UpdateBoxes();
			Refresh();
			return;
		}

		// 0 is stright left naturally
		protected void DrawRay(Graphics g, Color c, Point equus, float rot, float p, float k, float h)
		{
			Pen pen = new Pen(c,1);
			Point focus = new Point((int)h,(int)(k - 1 + p));
			Point intersection;

			if(WithingE(rot,(float)Math.PI / 2.0f))
				intersection = new Point(equus.X,(int)((equus.X - h) * (equus.X - h) / (4.0f * p) + k));
			else if(WithingE(rot,(float)Math.PI * 3.0f / 2.0f))
			{
				g.DrawLine(pen,equus,new Point(equus.X,0));
				return;
			}
			else
			{
				// Point-slope form: y - equus.y = m(x - equus.x)
				float m = (float)Math.Tan(rot);
				intersection = GetIntersection(equus,rot >= (float)Math.PI,m,p,k,h);
			}

			if(intersection.X < -100000)
				return;

			g.DrawLine(pen,equus,intersection);
			g.DrawLine(pen,focus,intersection);

			if(intersection.Y >= k - 1 + p)
				if(intersection.X > focus.X)
					intersection = GetIntersection(focus,true,-GetSlope(intersection,focus),p,k,h);
				else
					intersection = GetIntersection(focus,true,-GetSlope(intersection,focus),p,k,h);
			else
				if(intersection.X > focus.X)
					intersection = GetIntersection(focus,false,-GetSlope(intersection,focus),p,k,h);
				else
					intersection = GetIntersection(focus,false,-GetSlope(intersection,focus),p,k,h);

			g.DrawLine(pen,focus,intersection);
			g.DrawLine(pen,intersection,new Point(intersection.X,0));

			return;
		}

		protected float GetSlope(Point a, Point b)
		{return (float)(a.Y - b.Y) / (float)(a.X - b.X);}

		// upper should be true if rot >= Math.PI and false otherwise
		protected Point GetIntersection(Point point, bool upper, float m, float p, float k, float h)
		{
			float x0;

			if(m < 0.0f && !upper || m >= 0.0f && upper) // Look for a solution to the left
				x0 = -2.0f * (float)Math.Sqrt(h * m * p - k * p + m * m * p * p - m * p * point.X + p * point.Y) + h + 2.0f * m * p;
			else // Look for a solution to the right
				x0 = 2.0f * (float)Math.Sqrt(h * m * p - k * p + m * m * p * p - m * p * point.X + p * point.Y) + h + 2.0f * m * p;

			float y0 = m * (x0 - point.X) + point.Y;

			return new Point((int)x0,(int)y0);
		}

		// 4p(y-k) = (x-h)^2
		protected List<Point> GetParabola(float p, float k, float h, int min, int max)
		{
			List<Point> ret = new List<Point>();

			for(int i = min;i < max;i++)
				ret.Add(new Point(i,(int)((i - h) * (i - h) / (4.0f * p) + k)));

			return ret;
		}

		protected bool WithingE(float f, float exact)
		{
			if(Math.Abs(f - exact) < EPSILON)
				return true;

			return false;
		}

		private void button2_Click(object sender, EventArgs e)
		{
			Enable(false);
			return;
		}

		private void textBox1_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox1.Text,out i))
			{
				textBox1.Text = i.ToString();
				p = i;
			}
			
			Refresh();
			return;
		}

		private void textBox2_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox2.Text,out i))
			{
				textBox2.Text = i.ToString();
				k = i;
			}
			
			Refresh();
			return;
		}

		private void textBox3_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox3.Text,out i))
			{
				textBox3.Text = i.ToString();
				h = i;
			}
			
			Refresh();
			return;
		}

		private void textBox4_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox4.Text,out i))
			{
				textBox4.Text = i.ToString();
				min = i;
			}
			
			Refresh();
			return;
		}

		private void textBox5_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox5.Text,out i))
			{
				textBox5.Text = i.ToString();
				max = i;
			}
			
			Refresh();
			return;
		}

		private void textBox6_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox6.Text,out i))
			{
				textBox6.Text = i.ToString();
				EquusOffset.X = i;
			}

			Refresh();
			return;
		}

		private void textBox7_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox7.Text,out i))
			{
				textBox7.Text = i.ToString();
				EquusOffset.Y = i;
			}
			
			Refresh();
			return;
		}

		private void textBox8_TextChanged(object sender, EventArgs e)
		{
			float f;

			if(float.TryParse(textBox8.Text,out f))
			{
				rotation = f;

				while(rotation >= 2.0f * (float)Math.PI)
					rotation -= 2.0f * (float)Math.PI;

				while(rotation < 0.0f)
					rotation += 2.0f * (float)Math.PI;
			}
			
			Refresh();
			return;
		}

		private void textBox9_TextChanged(object sender, EventArgs e)
		{
			int i;

			if(int.TryParse(textBox9.Text,out i))
			{
				color = i;

				if(i < 0)
					color = 0;
				else if(i >= colors.Count)
					color = colors.Count - 1;

				textBox9.Text = color.ToString();
			}

			Refresh();
			return;
		}

		private void button1_Click(object sender, EventArgs e)
		{
			marked.Add(new Tuple<Point,Color,float>(new Point(EquusOffset.X,EquusOffset.Y),colors[color],rotation));
			Refresh();

			return;
		}

		protected int min = 300;
		protected int max = 1000;
		protected int p = -30;
		protected int k = 250;
		protected int h = 500;
		protected Point EquusOffset = new Point(0,-50);

		protected List<Color> colors;
		protected List<Tuple<Point,Color,float>> marked = new List<Tuple<Point,Color,float>>();
		protected float rotation = 0.0f;
		protected int color = 0;
		protected float delta = (float)Math.PI / 40.0f;
		protected float fixed_delta = (float)Math.PI / 40.0f;
		protected const float EPSILON = 0.001f;	
	}
}
