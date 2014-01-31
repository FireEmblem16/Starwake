using System.Net;
using System.Net.Sockets;

namespace GameMenu.Connection
{
	public interface NetworkBase
	{
		/// <summary>
		/// Emptys the stream of all data.
		/// </summary>
		/// <remarks>Only data already recieved can be removed.</remarks>
		void Flush();

		/// <summary>
		/// Makes all connections for this socket and begins data exchange.
		/// </summary>
		/// <param name="to">The addresses to connect to.</param>
		/// <returns>Returns true if the connection was made sucessfully.</returns>
		bool Connect(IPAddress[] to);

		/// <summary>
		/// Halts all socket functions and then connects again.
		/// </summary>
		/// <returns>Returns true if the connection was made successfully.</returns>
		bool Reconnect();

		/// <summary>
		/// Halts all data exchanges and connections made by this socket.
		/// </summary>
		void Disconnect();

		/// <summary>
		/// If true then the network stream has sucessfully connected.
		/// </summary>
		bool Connected
		{get;}

		/// <summary>
		/// The addressing scheme that this network stream uses.
		/// </summary>
		AddressFamily AddressingType
		{get;}
		
		/// <summary>
		/// The type of data send over the socket.
		/// </summary>
		SocketType DataType
		{get;}

		/// <summary>
		/// The protocol used for this network stream.
		/// </summary>
		ProtocolType Protocol
		{get;}

		/// <summary>
		/// The port the network stream is sending on.
		/// </summary>
		int Port
		{get;}
	}
}
