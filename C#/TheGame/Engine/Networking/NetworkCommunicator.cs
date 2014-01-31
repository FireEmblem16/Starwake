#pragma warning disable 0162

using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace TheGame.Networking
{
	/// <summary>
	/// A communications device for network data.
	/// </summary>
	public class NetworkCommunicator
	{
		/// <summary>
		/// Creates a new network communications stream.
		/// </summary>
		/// <param name="default_addr">The default address to use for sending data.</param>
		/// <param name="port">The port to communicate over.</param>
		public NetworkCommunicator(IPAddress default_addr, int port = 777)
		{
			DefaultDestination = default_addr;
			Port = port;

			listener = new TcpListener(IPAddress.Any,Port);
			listener.Start();

			outbound_data = new List<Tuple<string,object>>();
			inbound_data = new List<Packet>();

			listen_thread = new Thread(Listen);
			write_thread = new Thread(Write);

			listen_thread.Start();
			write_thread.Start();

			return;
		}

		/// <summary>
		/// Closes this network stream.
		/// </summary>
		public void Close()
		{
			try
			{listen_thread.Abort();}
			catch
			{}

			try
			{write_thread.Abort();}
			catch
			{}
			
			listener.Stop();
			
			return;
		}

		/// <summary>
		/// The listening thread's main function.
		/// </summary>
		protected void Listen()
		{
			while(true)
			{
				Thread.Sleep(SleepTime); // Prevent lockups
				
				TcpClient incoming = listener.AcceptTcpClient();
				NetworkStream stream = incoming.GetStream();

				byte[] partial_packet = new byte[PacketSize];
				stream.Read(partial_packet,0,PacketSize);

				string packet = RipString(partial_packet);

				while(partial_packet[PacketSize - 1] != 0)
				{
					partial_packet[PacketSize - 1] = 0;
					int read = stream.Read(partial_packet,0,PacketSize);

					if(read != PacketSize)
						partial_packet[read] = 0;

					packet += RipString(partial_packet);
				}

				inbound_data.Add(new Packet(packet,incoming.Client.RemoteEndPoint as IPEndPoint));
			}

			return;
		}

		/// <summary>
		/// Pulls a string out the byte array. Stops when it hits a zero.
		/// </summary>
		/// <param name="src">The string source.</param>
		/// <returns>Returns the string in src.</returns>
		protected string RipString(byte[] src)
		{
			if(src[src.Length - 1] != 0)
				return Encoding.ASCII.GetString(src);

			int index = 0;

			for(;src[index] != 0;index++);

			return Encoding.ASCII.GetString(src,0,index);
		}

		/// <summary>
		/// The writing thread's main function.
		/// </summary>
		protected void Write()
		{
			while(true)
			{
				Thread.Sleep(SleepTime); // Prevent lockups

				if(outbound_data.Count == 0)
					continue;

				TcpClient sender = (outbound_data[0].Item2 is string ? new TcpClient(outbound_data[0].Item2 as string,Port) : new TcpClient(outbound_data[0].Item2 as IPEndPoint));

				byte[] send = Encoding.ASCII.GetBytes(outbound_data[0].Item1);
				outbound_data.RemoveAt(0);

				NetworkStream nout = sender.GetStream();
				nout.Write(send,0,send.Length);

				nout.Close();
				sender.Close();
			}

			return;
		}

		/// <summary>
		/// Sends the given message to the default destination.
		/// </summary>
		/// <param name="msg">The message to send.</param>
		public void Write(string msg)
		{
			outbound_data.Add(new Tuple<string,object>(msg,new IPEndPoint(DefaultDestination,Port)));
			return;
		}

		/// <summary>
		/// Sends the given message.
		/// </summary>
		/// <param name="msg">The message to send.</param>
		/// <param name="hostname">The destination to send the message to.</param>
		public void Write(string msg, IPAddress dest)
		{
			outbound_data.Add(new Tuple<string,object>(msg,new IPEndPoint(dest,Port)));
			return;
		}

		/// <summary>
		/// Sends the given message.
		/// </summary>
		/// <param name="msg">The message to send.</param>
		/// <param name="dest">The destination to send the message to.</param>
		public void Write(string msg, string hostname)
		{
			outbound_data.Add(new Tuple<string,object>(msg,hostname));
			return;
		}

		/// <summary>
		/// Gets the next available packet.
		/// </summary>
		/// <returns>Returns the next available packet or null if no such packet exists.</returns>
		public Packet NextPacket()
		{
			if(!HasNextPacket)
				return null;

			Packet ret = inbound_data[0];
			inbound_data.RemoveAt(0);

			return ret;
		}

		/// <summary>
		/// Gets the next available packet without removing it from the stream.
		/// </summary>
		/// <returns>Returns the next available packet or null if not such packet exists.</returns>
		public Packet PeekPacket()
		{
			if(!HasNextPacket)
				return null;

			return inbound_data[0];
		}

		/// <summary>
		/// Waits for a packet to arrive.
		/// </summary>
		/// <param name="e">The function that determines if the packet has arrived.</param>
		/// <param name="nio">The network stream we can expect the packet on.</param>
		/// <param name="max_time">The maximum time we are willing to wait for the packet (or negative for an unlimited wait time).</param>
		/// <returns>Returns true if the packet arrived and false otherwise.</returns>
		public bool WaitFor(Evaluator e, int max_time = -1)
		{
			while(true)
			{
				// Wait for a packet
				while(!HasNextPacket)
				{
					if(max_time > 0)
					{
						max_time -= 10;

						if(max_time <= 0)
							return false;
					}

					Thread.Sleep(10);
				}

				// Check if we have the packet we need
				if(e(PeekPacket().Data))
					break;

				// This packet was trash so trash it
				NextPacket();
			}

			return true;
		}

		/// <summary>
		/// The port this network stream communicates over.
		/// </summary>
		public int Port
		{get; protected set;}

		/// <summary>
		/// The default destination to send packets to if none is provided.
		/// </summary>
		public IPAddress DefaultDestination
		{get; set;}

		/// <summary>
		/// If true then there is incoming data available.
		/// </summary>
		public bool HasNextPacket
		{
			get
			{return inbound_data.Count != 0;}
		}

		/// <summary>
		/// The amount of time to sleep when waiting for things or preventing lockups.
		/// </summary>
		protected int SleepTime
		{
			get
			{return 10;}
		}

		/// <summary>
		/// The size of each packet.
		/// </summary>
		protected int PacketSize
		{
			get
			{return 0x400;}
		}

		/// <summary>
		/// The thread listening for connections.
		/// </summary>
		protected Thread listen_thread;

		/// <summary>
		/// The thread writing data.
		/// </summary>
		protected Thread write_thread;

		/// <summary>
		/// The device listening for tcp requests.
		/// </summary>
		protected TcpListener listener;

		/// <summary>
		/// Contains outbound data along with the addresses to send each to.
		/// </summary>
		protected List<Tuple<string,object>> outbound_data;

		/// <summary>
		/// Conatins data recieved from other computers and to location it arrived from.
		/// </summary>
		protected List<Packet> inbound_data;

		/// <summary>
		/// A packet containing the location and data.
		/// </summary>
		public class Packet
		{
			/// <summary>
			/// Creates a new packet.
			/// </summary>
			/// <param name="data">The data in the packet.</param>
			/// <param name="source">The source of the packet.</param>
			public Packet(string data, IPEndPoint source)
			{
				Data = data;
				Source = source;

				return;
			}

			/// <summary>
			/// The data in this packet.
			/// </summary>
			public string Data
			{get; protected set;}

			/// <summary>
			/// The source of this packet.
			/// </summary>
			public IPEndPoint Source
			{get; protected set;}
		}

		/// <summary>
		/// Determines if the packet we are waiting for has arrived.
		/// </summary>
		/// <param name="msg">The latest packet.</param>
		/// <returns>Returns true if this is the packet we want and false otherwise.</returns>
		public delegate bool Evaluator(string msg);
	}
}
