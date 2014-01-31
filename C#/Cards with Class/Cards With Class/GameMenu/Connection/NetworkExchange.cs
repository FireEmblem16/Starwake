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
    ///  This class implements interface NetworkStream
    ///  This class manages the exchange of data in a stream
    /// </summary>
    /// <author>
    /// by Can Liang
    /// </author>
    public class NetworkExchange : NetworkStream
    {
        protected int bufferSize = 1024;
        protected int sending_timeout = 0;
        protected int receive_timeout = 0;
        protected IPAddress[] IPCollection = new IPAddress[] { };
        protected bool connected = false;      
        // port for connection // backlog
        protected int PortofSever = 2001;
        protected int PortofClient = 2002;
        protected int localport;
        protected int OutPort;
        protected List<Socket> connections = new List<Socket>();
        protected int backlog = 10;
        protected short ttl = 32;
        // data queue to store received data
        protected List<byte[]> dataQueue = new List<byte[]>();
        protected List<byte[]> PacketQueue = new List<byte[]>();
        // terminate socket connections
        protected bool halt = false;
        protected Thread listener;
        protected Thread sender;
        protected AddressFamily AddrType;
        protected ProtocolType PType = ProtocolType.Tcp;
        protected SocketType SType = SocketType.Stream;
        protected string signature = "#$knd8&)@/n";
        protected string request = "!!#$B*&#*HC9cdEE*7^&Kl##FJOICODUJ::ASJD";

        public NetworkExchange(bool isClient)
        {
            if (isClient)
            {
                localport = PortofClient;
                OutPort = PortofSever;
            }
            else
            {
                localport = PortofSever;
                OutPort = PortofClient;
            }

            listener = new Thread(Listen);
            sender = new Thread(send);
            listener.Start();
            sender.Start();

            OnConnect += ConnectionMade;
        }

        // listens for incoming data
        // started in a thread
        protected void Listen()
        {
            Socket Listener = new Socket(AddressingType, DataType, Protocol);
            Listener.ReceiveTimeout = receive_timeout;
            IPEndPoint localInterface = new IPEndPoint(IPAddress.Any, localport);
            Listener.Bind(localInterface);
            Listener.Listen(backlog);
            Socket handler = handler = Listener.Accept(); ;
            Console.WriteLine(">> Begin Listening...");

            while (!halt)
            {
                try
                {
                    String dataRec = "";
                    byte[] bytes = new byte[bufferSize];
                    int bytesRec = handler.Receive(bytes);
                    dataRec += Encoding.ASCII.GetString(bytes, 0, bytesRec);
                    if (dataRec.IndexOf(signature) > -1)
                    {
                        string data = dataRec.Substring(0, dataRec.IndexOf(signature));
                        dataRec.Remove(0, (dataRec.IndexOf(signature) + signature.Length));
                        //Console.WriteLine(">> Received: {0}", data);
                        
                        if (data == request)
                        {
                            Console.WriteLine(">> Inbound Connection Request Received");
                            IPEndPoint anyEP = (IPEndPoint)handler.RemoteEndPoint;
                            OnConnect(anyEP, true);    // event triggers outbound to connect to the address.
                        }
                        else
                        {
                            //Console.WriteLine(">> Data Received: {0}", data);
                            dataQueue.Add(Encoding.UTF8.GetBytes(data));
                        }
                    }
                    if(handler.Poll(1000, SelectMode.SelectRead) && handler.Available == 0)
                        handler = Listener.Accept();
                }
                catch
                {
                    handler.Shutdown(SocketShutdown.Both);
                    handler.Close();
                    handler = Listener.Accept();
                }
            }

            try
            {
                handler.Shutdown(SocketShutdown.Both);
                handler.Close();
                Listener.Shutdown(SocketShutdown.Both);
                Listener.Close();
                Console.WriteLine(">> Listening Halted");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        // sends datas in the queue
        // the method is started in a thread
        protected void send()
        {
            while (!halt)
            {
                while (PacketQueue.Count > 0 && connections.Count > 0)
                {
                    Thread.Sleep(3);
                    byte[] toSend = PacketQueue[0];
                    PacketQueue.RemoveAt(0);
                    
                    for (int i = 0; i < connections.Count; i++)
                    {
                        try
                        {
                            if (connections[i].Poll(1000, SelectMode.SelectRead) && connections[i].Available == 0)
                            {
                                connections[i].Shutdown(SocketShutdown.Both);
                                connections.RemoveAt(i);
                                Console.WriteLine(">> A connection has been disconnected. Remaining connections {0}", connections.Count);
                                i--;
                            }
                            else
                            {
                                connections[i].Send(toSend);
                                //Console.WriteLine(">> Packet {0} sent to {1}", Encoding.UTF8.GetString(toSend), ((IPEndPoint)(connections[i].RemoteEndPoint)).Address.ToString());
                            }
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine(">> Cannot reach server" + e.ToString());
                        }
                    }
                }
            }

            for (int j = 0; j < connections.Count; j++)
            {
                connections[j].Shutdown(SocketShutdown.Both);
                connections[j].Close();
                connections.RemoveAt(j);
                j--;
            }
            Console.WriteLine(">> Data sending has ceased.");
        }

        // removes already obtained datas from stream
        public void Flush()
        {
            PacketQueue.RemoveRange(0, PacketQueue.Count - 1);
            dataQueue.RemoveRange(0, PacketQueue.Count - 1);
        }

        // clients connect to sever 
		public bool Connect(IPAddress[] to)
        {
            IPEndPoint remoteEP = null;

            if (to.Count() <= 0)
            {
                Console.WriteLine(">> Connection Attempt failed, Illegal Address");
                connected = false;
            }
            IPCollection = (IPAddress[])to.Clone();

            try
            {
                AddrType = to[0].AddressFamily;
                remoteEP = new IPEndPoint(to[0], OutPort);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                connected = false;
            }
            Console.WriteLine(">> Requesting Connection...");
            OnConnect(remoteEP, false);

            return connected = true;
        }

        // reconnect to server
        public bool Reconnect()
        {
            Disconnect();

            return Connect(IPCollection);
        }

        // Disconnect from server
        public void Disconnect()
        {
            halt = true;
            Thread.Sleep(2000);
            Console.WriteLine(">> All Connections Has Been Disconnected.");
            connected = false;
        }

        // Connection status of network
        public bool Connected
		{
            get { return connected; }
        }

        public AddressFamily AddressingType
		{
            get { return AddrType; }
        }

        public SocketType DataType
        { 
            get { return SType; }
        }

        public ProtocolType Protocol
		{
            get {return PType; }
        }

        public int Port
		{
            get { return localport; }
        }

        // Queue a packet as bytes to be sent
        public void QueuePacket(byte[] data)
        {
            string temp = Encoding.ASCII.GetString(data) + signature;
            byte[] bytes = new byte[temp.Length];
            bytes = Encoding.ASCII.GetBytes(temp);
            PacketQueue.Add(bytes);
        }

        // Queue a as a string to be sent
        public void QueuePacket(String data)
        {
            string temp = data + signature;
            byte[] bytes = new byte[temp.Length];
            bytes = Encoding.UTF8.GetBytes(temp);
            PacketQueue.Add(bytes);
        }

        public void QueuePacket(byte[] data, long ttl)
        {
            this.ttl = (short)ttl;
            QueuePacket(data);
        }

        public void QueuePacket(String data, long ttl)
        {
            this.ttl = (short)ttl;
            QueuePacket(data);
        }

        public IEnumerable<IPAddress> SendingTo
        {
            get { return IPCollection;}
        }

        public short PacketTTL
        {
            get { return ttl; }
            set { ttl = value; }
        }

        public int SendingTimout
        {
            get { return sending_timeout; }
            set { sending_timeout = value; }
        }
    
        // return the next packet in the stream as a string
        public string GetString()
        {
            // return string if arraylist is not out of range
            if (dataQueue.Count > 0)
            {
                String temp = Encoding.ASCII.GetString(dataQueue[0], 0, dataQueue[0].Length);
                dataQueue.RemoveAt(0);
                return temp;
            }
            // return 0 if index is out of range
            else
                return null;
        }

        // return the next packet in the stream as a byte array
        public byte[] GetRaw()
        {
            if (dataQueue.Count > 0 && dataQueue[0] != null)
            {
                byte[] temp = (byte[])dataQueue[0].Clone();
                dataQueue.RemoveAt(0);
                return temp;
            }
            else
                return null;
        }

        // peek the next packet in the stream as a string
        // without removing it
        public string PeekString()
        {
            if (dataQueue.Count > 0 && dataQueue[0] != null)
            {
                string temp = Encoding.ASCII.GetString(dataQueue[0], 0, dataQueue[0].Length);
                return temp;
            }
            else
                return null;
        }

        // peek the next packet in the stream as a byte array
        // without removing it
        public byte[] PeekRaw()
        {
            if (dataQueue.Count > 0 && dataQueue[0] != null)
            {
                return (byte[])dataQueue[0];
            }
            else
                return null;
        }

        public bool HasPacket
        { 
            get 
            {
                return dataQueue.Count > 0;
            }
        }

        public IEnumerable<IPAddress> RecievingFrom
        {
            get
            { return IPCollection; }
        }

        public int RecievingTimeout
        {
            get { return receive_timeout; }
            set { receive_timeout = value; }
        }

        public event ConnectionEvent OnConnect;

        protected void ConnectionMade(EndPoint ep, bool inbound = false)
        {

            if (inbound)
            {
                connected = true;
                IPAddress ip = ((IPEndPoint)ep).Address;
                Socket to_client = new Socket(ip.AddressFamily, DataType, Protocol);
                to_client.Connect(ip, OutPort);
                connections.Add(to_client);
                for (int i = 0; i < connections.Count; i++)
                {
                    if (connections[i].Available == 0 && connections[i].Poll(1000, SelectMode.SelectRead))
                    {
                        connections[i].Shutdown(SocketShutdown.Both);
                        connections[i].Close();
                        connections.RemoveAt(i);
                        i--;
                    }
                }
                Console.WriteLine(">> Connection Made On Contact, active Connections: {0}", connections.Count);
                for (int i = 0; i < connections.Count; i++)
                    Console.WriteLine(">> Active IP: {0}", ((IPEndPoint)(connections[i].RemoteEndPoint)).Address.ToString());
                QueuePacket("Congratulations! You are connected.");
            }
            else
            {
                try
                {
                    Socket to_server = new Socket(AddrType, DataType, Protocol);
                    IPAddress ip = ((IPEndPoint)ep).Address;
                    to_server.Connect(ip, OutPort);
                    connections.Add(to_server);
                    to_server.Send(Encoding.UTF8.GetBytes(request + signature));
                    Console.WriteLine(">> Connection Request Sent.");
                    return;
                }
                catch
                {
                    Console.WriteLine(">> Connection Attempt Failed.");
                    return;
                }
            }
        }
    }

    public delegate void ConnectionEvent(EndPoint ep, bool inbound = false);
}