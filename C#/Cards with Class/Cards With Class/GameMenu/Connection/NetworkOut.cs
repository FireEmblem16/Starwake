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
    /// A network stream directed outwards.
    /// </summary>
    public interface NetworkOut
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
        /// Emptys the stream of all data not yet sent.
        /// </summary>
        /// <remarks>Only data not already sent can be removed.</remarks>
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
        /// The list of IP addresses that this network stream is sending data to.
        /// </summary>
        IEnumerable<IPAddress> SendingTo
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
        /// The port the network stream is sending on.
        /// </summary>
        int Port
        { get; }

        /// <summary>
        /// How long the network stream is willing to wait in synchronous calls (in milliseconds) before giving up.
        /// </summary>
        /// <remarks>The default value is 0 indicating infinite wait time. A value less than 500 will otherwise be treated as 500. Behvaiour is undefined for negative numbers.</remarks>
        int SendingTimout
        { get; set; }

        /// <summary>
        /// The time to live applied to packets by this network stream.
        /// </summary>
        /// <remarks>Default value is 32.</remarks>
        short PacketTTL
        { get; set; }
    }

}
