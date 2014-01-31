using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;

namespace Networking
{
	/// <summary>
	/// A network stream directed inwards.
	/// </summary>
	public interface NetworkIn : NetworkBase
	{
		/// <summary>
		/// Gets the next packet from the stream in string form.
		/// </summary>
		/// <returns>Returns the next packet from the stream in string form.</returns>
		String GetString();

		/// <summary>
		/// Gets the next packet from the stream in raw byte form.
		/// </summary>
		/// <returns>Returns the next packet from the stream in raw byte form.</returns>
		byte[] GetRaw();

		/// <summary>
		/// Obtains the next packet from the stream in string form without removing it from the stream.
		/// </summary>
		/// <returns>Returns the next packet in the stream in string form.</returns>
		String PeekString();

		/// <summary>
		/// Obtains the next packet from the stream as raw data in a byte array without removing it from the stream.
		/// </summary>
		/// <returns>Returns the next packet in the stream in raw byte form.</returns>
		byte[] PeekRaw();

		/// <summary>
		/// If true then there is currently a packet waiting in the stream.
		/// </summary>
		bool HasPacket
		{get;}

		/// <summary>
		/// The list of IP addresses that this network stream is recieving data from.
		/// </summary>
		IEnumerable<IPAddress> RecievingFrom
		{get;}

		/// <summary>
		/// How long the network stream is willing to wait in synchronous calls (in milliseconds) before giving up.
		/// </summary>
		/// <remarks>The default value is 0 indicating infinite wait time. Behvaiour is undefined for negative numbers.</remarks>
		int RecievingTimeout
		{get; set;}
	}
}
