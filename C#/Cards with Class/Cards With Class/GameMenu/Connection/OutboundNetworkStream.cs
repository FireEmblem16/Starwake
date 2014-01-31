using System;
using System.Net;
using System.Net.Sockets;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;



namespace GameMenu.Connection
{
    /// <summary>
    ///  This class implements interface NetworkOut
    /// </summary>
    /// <author>
    /// by Can Liang
    /// </author>
    public class OutboundNetworkStream : NetworkOut
    {
        // data receiver
        protected static string data = null;
        // data queue to store received data
        protected SocketType DType = SocketType.Stream;
        protected static List<byte[]> PacketQueue = new List<byte[]>();
        protected int pacPtr = 0;
        protected IPAddress[] IPCollection;
        protected AddressFamily AddrType = System.Net.Sockets.AddressFamily.InterNetworkV6;
        protected ProtocolType PType = ProtocolType.Tcp;
        protected Thread sender;
        IPEndPoint hostEP;
        protected bool halt = false;
        protected bool connected = false;
        protected int backlog = 10;
        protected int timeout = 0;
        protected int port = 2001;
        protected short ttl = 32;

        public OutboundNetworkStream()
        {
            sender = new Thread(send);
            sender.Start();
        }

        // sends datas in the queue
        // the method is started in a thread
        public void send()
        {
            while (!Connected) ;
            Socket sender = new Socket(AddressingType, DataType, Protocol);
            sender.Ttl = ttl;
            sender.SendTimeout = timeout;
            sender.Connect(hostEP);
            Console.WriteLine(">> Outbound Network: Connected.\n");

            while (!halt)
            {
                while (pacPtr < PacketQueue.Count)
                {
                    byte[] toSend = PacketQueue[pacPtr];
                    if (toSend != null)
                    {
                        pacPtr++;
                        sender.Send(toSend);
                        //Console.WriteLine(">> Outbound Network: sent {0}.\n", Encoding.ASCII.GetString(toSend, 0, toSend.Length));
                        Thread.Sleep(50);
                    }
                }
            }

            Console.WriteLine(">> Outbound Network: halted\n");
            sender.Close();
            return;
        }

        // queue a byte array to be sent
        public void QueuePacket(byte[] data)
        {
            string data_in = Encoding.ASCII.GetString(data);
            string end = "89dkjie7Jo?(dII8454DOIK)()*3df4d00";
            string temp = data_in + end;
            byte[] data_out = Encoding.ASCII.GetBytes(temp);

            PacketQueue.Add(data_out);
        }

        // queue a string to be sent
        public void QueuePacket(String data)
        {
            string temp = data + "89dkjie7Jo?(dII8454DOIK)()*3df4d00";
            byte[] bytes = new byte[temp.Length];
            //System.Buffer.BlockCopy(data.ToCharArray(), 0, bytes, 0, bytes.Length);
            bytes = Encoding.UTF8.GetBytes(temp);
            PacketQueue.Add(bytes);
        }

        // queue a byte array to be sent
        // remove from array if cannot be sent with ttl
        public void QueuePacket(byte[] data, long ttl)
        {
            this.ttl = (short)ttl;
            QueuePacket(data);
        }

        // queue string array to be sent
        // remove if cannot be sent before ttl
        public void QueuePacket(String data, long ttl)
        {
            this.ttl = (short)ttl;
            QueuePacket(data);
        }

        // clear network stream of sent datas
        public void Flush()
        {
            if(pacPtr > 0)
            { 
                PacketQueue.RemoveRange(0, pacPtr);
                pacPtr = 0;
            }
        }

        // Connect to host indicated by its array of IPAddresses
        public bool Connect(IPAddress[] to)
        {
            if (to.Count() <= 0)
            {
                Console.WriteLine(">> Outbound Network: Connection Attempt failed, Illegal Address.\n");
                return (connected = false);
            }
            IPCollection = (IPAddress[])to.Clone();
            IPAddress local = Dns.GetHostEntry(Dns.GetHostName()).AddressList[1];

            AddrType = to[0].AddressFamily;
            hostEP = new IPEndPoint(to[0], port);

            QueuePacket("a;odfhdskjhfksajdhf;safg;dskjzhnfvm,g");
            return (connected = true);
        }

        // reconnect to host
        public bool Reconnect()
        {
            halt = true;
            System.Threading.Thread.Sleep(2000);
            return Connect(IPCollection);
        }

        // disconnect from host
        public void Disconnect()
        {
            halt = true;
            connected = false;
        }

        // get connection status
        public bool Connected
        {
            get { return connected; }
        }

        // get the IPAddress the network stream sends to
        public IEnumerable<IPAddress> SendingTo
        {
            get { return IPCollection; }
        }

        // get the address type of network stream
        public AddressFamily AddressingType
        {
            get { return AddrType; }
        }

        // get teh data type of network stream
        public SocketType DataType
        {
            get { return DType; }
        }

        // get the protocol of network stream
        public ProtocolType Protocol
        {
            get { return PType; }
        }

        // get the port of network stream
        public int Port
        {
            get { return port; }
        }

        // get the timeout of network stream
        public int SendingTimout
        {
            get { return timeout; }
            set { timeout = value; }
        }

        // get the packet ttl associated to the network stream
        public short PacketTTL
        {
            get { return ttl; }
            set { ttl = value; }
        }
    }
}