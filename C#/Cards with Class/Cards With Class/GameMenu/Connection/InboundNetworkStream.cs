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
    ///  This class implements interface NetworkIn
    /// </summary>
    /// <author>
    /// by Can Liang
    /// </author>
    public class InboundNetworkStream : NetworkIn
    {
        // size of data buffer
        protected int bufferSize = 1024;
        protected int arrPtr = 0;
        protected int pacPtr = 0;
        protected int timeout = 0;
        protected IPAddress[] IPCollection = new IPAddress[] { };
        protected bool IsConnected = false;
        // port for connection // backlog
        protected int port = 2001;
        protected int backlog = 0;
        // data queue to store received data
        protected List<byte[]> dataQueue = new List<byte[]>();
        // terminate socket connections
        protected bool halt = false;
        protected Thread listener;
        protected IPEndPoint remoteEP = null;
        protected AddressFamily AddrType;
        protected ProtocolType PType = ProtocolType.Tcp;
        protected SocketType SType = SocketType.Stream;

        public InboundNetworkStream()
        {
            listener = new Thread(Listen);
            listener.Start();

            return;
        }

        // listens for incoming data
        // started in a thread
        protected void Listen()
        {
            Console.WriteLine(">> Inbound Network: Waiting for Connection...\n");
            while (!Connected) ;
            Console.WriteLine(">> Inbound Network: Connected.\n");
            Socket Listener = new Socket(AddressingType, DataType, Protocol);
            Listener.ReceiveTimeout = timeout;
            Listener.Bind(remoteEP);
            Listener.Listen(backlog);
            Socket handler = Listener.Accept();
            
            while (!halt)
            {
                String dataRec = "";
                byte[] bytes = new byte[bufferSize];
                while (dataRec.IndexOf("89dkjie7Jo?(dII8454DOIK)()*3df4d00") == -1 && !halt)
                {
                    int bytesRec = handler.Receive(bytes);
                    dataRec += Encoding.ASCII.GetString(bytes, 0, bytesRec);
                    Thread.Sleep(50);
                }

                if (dataRec == "a;odfhdskjhfksajdhf;safg;dskjzhnfvm,g89dkjie7Jo?(dII8454DOIK)()*3df4d00")
                {
                    //  Connect(wherever that came from);
                }
                else
                {
                    byte[] received = Encoding.UTF8.GetBytes(dataRec);
                    byte[] toQueue = new byte[received.Length - 34];
                    for (int i = 0; i < received.Length - 34; i++)
                    {
                        toQueue[i] = received[i];
                    }
                    dataQueue.Add(toQueue);
                    Console.WriteLine(">> Inbound Network: Received {0}.\n", Encoding.ASCII.GetString(toQueue));
                }
            }

            handler.Close();
            Listener.Close();
            Console.WriteLine(">> Inbound Network: halted\n");
            return;
        }

        // returns the next string in queue
        public String GetString()
        {
            // return string if arraylist is not out of range
            if (arrPtr < dataQueue.Count)
            {
                String temp = Encoding.ASCII.GetString(dataQueue[arrPtr], 0, dataQueue[arrPtr].Length);
                arrPtr++;
                return temp;
            }
            // return 0 if index is out of range
            else
                return null;
        }

        // returns the next data in queue as a byte array
        public byte[] GetRaw()
        {
            if (arrPtr < dataQueue.Count)
            {
                byte[] temp = (byte[])dataQueue[arrPtr];
                arrPtr++;
                return temp;
            }
            else
                return null;
        }

        // returns the next string without moving to the next data in queue
        public String PeekString()
        {
            if (arrPtr < dataQueue.Count && dataQueue[arrPtr] != null)
            {
                return Encoding.ASCII.GetString(dataQueue[arrPtr], 0, dataQueue[arrPtr].Length);
            }
            else
                return null;
        }

        // returns the next byte array without moving to the next data in queue
        public byte[] PeekRaw()
        {
            if (arrPtr < dataQueue.Count && dataQueue[arrPtr] != null)
                return (byte[])dataQueue[arrPtr];
            else 
                return null;
        }

        // clear the stream of all received data
        public void Flush()
        {
            if (arrPtr > 0)
            {
                dataQueue.RemoveRange(0, arrPtr);
                arrPtr = 0;
            }
        }

        // connects to destinated host indicated by IP addresses
        public bool Connect(IPAddress[] to)
        {
            if (to.Count() <= 0)
            {
                Console.WriteLine(">> Inbound Network: Connection Attempt failed, Illegal Address.\n");
                return (IsConnected = false);
            }
            IPCollection = (IPAddress[])to.Clone();
            // connection fails if attempts to connect to local host entry
            IPAddress local = Dns.GetHostEntry(Dns.GetHostName()).AddressList[1];

            try
            {
                remoteEP = new IPEndPoint(to[0], port);
                AddrType = to[0].AddressFamily;
            }

            catch (Exception e)
            {
                Console.WriteLine(e);
            }

            return (IsConnected = true);
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
            IsConnected = false;
        }

        // returns true if the stream is connected
        public bool Connected
        {
            get { return IsConnected; }
        }

        // returns true if stream has packets
        public bool HasPacket
        {
            get { return (dataQueue.Count > arrPtr); }
        }

        // the IPAddresses the stream listens from
        public IEnumerable<IPAddress> RecievingFrom
        {
            get { return IPCollection; }
        }

        // the Address type used by this network stream
        public AddressFamily AddressingType
        {
            get { return AddrType; }
        }

        // the data type used by this stream
        public SocketType DataType
        {
            get { return SocketType.Stream; }
        }

        // the protocol used by this stream
        public ProtocolType Protocol
        {
            get { return ProtocolType.Tcp; }
        }

        // the port used in the stream
        public int Port
        {
            get { return port; }
        }

        // set or get the timeout of the network
        public int RecievingTimeout
        {
            get { return timeout; }
            set { timeout = value; }
        }
    }
}
