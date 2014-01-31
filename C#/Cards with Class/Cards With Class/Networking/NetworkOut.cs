using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;

namespace Networking
{
	/// <summary>
	/// A network stream directed outwards.
	/// </summary>
	public interface NetworkOut : NetworkBase
	{
		/// <summary>
		/// Adds a packet to the end of the list of packets that need to be sent.
		/// The packet will be sent as soon as possible.
		/// <param name="data">The packet data to send.</param>
		/// </summary>
		void QueuePacket(byte[] data);

		/// <summary>
		/// Adds a packet to the end of the list of packets that need to be sent.
		/// The packet will be sent as soon as possible.
		/// </summary>
		/// <param name="data">The packet data to send.</param>
		void QueuePacket(String data);

		/// <summary>
		/// Adds a packet to the end of the list of packets that need to be sent.
		/// The packet will be sent as soon as possible. If it can not be sent in time then it will be discarded.
		/// </summary>
		/// <param name="data">The packet data to send.</param>
		/// <param name="ttl">The maximum amount of time the packet can linger in the queue.</param>
		void QueuePacket(byte[] data, long ttl);

		/// <summary>
		/// Adds a packet to the end of the list of packets that need to be sent.
		/// The packet will be sent as soon as possible. If it can not be sent in time then it will be discarded.
		/// </summary>
		/// <param name="data">The packet data to send.</param>
		/// <param name="ttl">The maximum amount of time the packet can linger in the queue.</param>
		void QueuePacket(String data, long ttl);

		/// <summary>
		/// The list of IP addresses that this network stream is sending data to.
		/// </summary>
		IEnumerable<IPAddress> SendingTo
		{get;}

		/// <summary>
		/// How long the network stream is willing to wait in synchronous calls (in milliseconds) before giving up.
		/// </summary>
		/// <remarks>The default value is 0 indicating infinite wait time. A value less than 500 will otherwise be treated as 500. Behvaiour is undefined for negative numbers.</remarks>
		int SendingTimout
		{get; set;}

		/// <summary>
		/// The time to live applied to packets by this network stream.
		/// </summary>
		/// <remarks>Default value is 32.</remarks>
		short PacketTTL
		{get; set;}
	}
}
