using System;
using System.Net;
using System.Net.Sockets;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace GameMenu.Connection
{
    /// <summary>
    /// A network stream directed inwards.
    /// </summary>
    public interface NetworkIn
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
        { get; }

        /// <summary>
        /// If true then there is currently a packet waiting in the stream.
        /// </summary>
        bool HasPacket
        { get; }

        /// <summary>
        /// The list of IP addresses that this network stream is recieving data from.
        /// </summary>
        IEnumerable<IPAddress> RecievingFrom
        { get; }

        /// <summary>
        /// The addressing scheme that this network stream uses.
        /// </summary>
        AddressFamily AddressingType
        { get; }

        /// <summary>
        /// The type of data send over the socket.
        /// </summary>
        SocketType DataType
        { get; }

        /// <summary>
        /// The protocol used for this network stream.
        /// </summary>
        ProtocolType Protocol
        { get; }

        /// <summary>
        /// The port the network stream is listening on.
        /// </summary>
        int Port
        { get; }

        /// <summary>
        /// How long the network stream is willing to wait in synchronous calls (in milliseconds) before giving up.
        /// </summary>
        /// <remarks>The default value is 0 indicating infinite wait time. Behvaiour is undefined for negative numbers.</remarks>
        int RecievingTimeout
        { get; set; }
    }



}
