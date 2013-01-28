package com.luzi82.koharurepeat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class KrCore{
	/**
	 * 
	 */
	private final KoharuRepeat mKoharuRepeat;
	/**
	 * @param aKoharuRepeat
	 */
	public KrCore(KoharuRepeat aKoharuRepeat) {
		mKoharuRepeat = aKoharuRepeat;
	}
	class ServerSocketCreater{
		ServerSocketManager create(int cPort,String sHost,int sPort)throws IOException{
			return new ServerSocketManager(new ServerSocket(cPort),sHost,sPort);
		}
	}
	class ServerSocketManager implements Killable{
		class SC implements Runnable{
			SSL l;
			SC(SSL l){
				this.l=l;
			}
			public void run(){
				Socket c=null,s=null;
				try{
					while(!ss.isClosed()){
						c=s=null;
						c=ss.accept();
						s=new Socket(sHost,sPort);
						l.get(new SocketManager(c,s));
					}
				}catch(SocketException e){
				}catch(UnknownHostException e){
					l.error(e);
				}catch(IOException e){
					throw new Error();
				}
				try{
					if(c!=null)c.close();
					if(s!=null)s.close();
				}catch(IOException e){
					throw new Error();
				}
				try{
					ss.close();
				}catch(IOException e){
					throw new Error();
				}
				l.close();
				//System.out.println("hello");
			}
		}
		ServerSocket ss;
		String sHost;
		int sPort;
		Thread sc;
		ServerSocketManager(ServerSocket ss,String sHost,int sPort){
			this.ss=ss;
			this.sHost=sHost;
			this.sPort=sPort;
		}
		void setL(SSL l){
			sc=new Thread(new SC(l));
			sc.start();
		}
		public void kill(){
			try{
				ss.close();
			}catch(IOException e){
				throw new Error();
			}
		}
		public void join(){
			while(sc.isAlive()){
				try{
					sc.join();
				}catch(InterruptedException e){
					throw new Error();
				}
			}
		}
		int getCPort(){
			return ss.getLocalPort();
		}
		String getSHost(){
			return sHost;
		}
		int getSPort(){
			return sPort;
		}
	}
	class SocketManager implements Killable{
		class IO implements Runnable{
			InputStream in;
			OutputStream out;
			L l;
			Socket s;
			IO(InputStream in,OutputStream out,L l,Socket s){
				this.in=in;
				this.out=out;
				this.l=l;
				this.s=s;
			}
			public void run(){
				try{
					while(true){
						int i=in.read();
						if(i==-1)break;
						out.write(i);
						l.write(i);
					}
				}catch(SocketException e){
				}catch(IOException e){
					throw new Error();
				}
				try{
					in.close();
					out.close();
					l.close();
					s.close();
				}catch(IOException e){
					throw new Error();
				}
			}
		}
		class SS implements L{
			public void write(int i){
				writeS(i);
			}
			public void close(){
				closeS();
			}
		}
		class CC implements L{
			public void write(int i){
				writeC(i);
			}
			public void close(){
				closeC();
			}
		}
		Socket c;
		Socket s;
		Map sds;
		Thread cs;
		Thread sc;
		SocketManager(Socket c,Socket s){
			this.c=c;
			this.s=s;
			sds=new TreeMap();
			OutputStream co,so;
			InputStream ci,si;
			try{
				co=c.getOutputStream();
				so=s.getOutputStream();
				ci=c.getInputStream();
				si=s.getInputStream();
			}catch(IOException e){
				throw new Error();
			}
			cs=new Thread(new IO(ci,so,new CC(),s));
			sc=new Thread(new IO(si,co,new SS(),c));
		}
		String getSHost(){
			return s.getInetAddress().getHostAddress();
		}
		String getCHost(){
			return c.getInetAddress().getHostAddress();
		}
		int getSPort(){
			return s.getPort();
		}
		int getCPort(){
			return c.getPort();
		}
		void start(final SL sl){
			cs.start();
			sc.start();
			final SocketManager t=this;
			new Thread(new Runnable(){
				public void run(){
					t.join();
					sl.close();
				}
			}).start();
		}
		synchronized SD add(String code){
			if(sds.containsKey(code))return null;
			SD out=new SD(this,code);
			sds.put(code,out);
			return out;
		}
		synchronized void remove(String code){
			SD tar=(SD)sds.remove(code);
			if(tar==null)return;
			tar.closeC();
			tar.closeS();
		}
		synchronized void closeC(){
			Iterator t=sds.values().iterator();
			while(t.hasNext()){
				SD sd=(SD)t.next();
				sd.closeC();
			}
		}
		synchronized void closeS(){
			Iterator t=sds.values().iterator();
			while(t.hasNext()){
				SD sd=(SD)t.next();
				sd.closeS();
			}
		}
		synchronized void writeC(int i){
			Iterator t=sds.values().iterator();
			while(t.hasNext()){
				SD sd=(SD)t.next();
				sd.writeC(i);
			}
		}
		synchronized void writeS(int i){
			Iterator t=sds.values().iterator();
			while(t.hasNext()){
				SD sd=(SD)t.next();
				sd.writeS(i);
			}
		}
		public void kill(){
			try{
				c.close();
				s.close();
			}catch(IOException e){
				throw new Error();
			}
		}
		public void join(){
			while(cs.isAlive()||sc.isAlive()){
				try{
					cs.join();
					sc.join();
				}catch(InterruptedException e){
					throw new Error();
				}
			}
		}
	}
	class SD implements Killable{
		class R2L implements Runnable{
			Reader r;
			L l;
			R2L(Reader r,L l){
				this.r=r;this.l=l;
			}
			public void run(){
				try{
					while(true){
						int i=r.read();
						if(i==-1)break;
						l.write(i);
						//System.out.println(i);
					}
				}catch(IOException e){
					throw new Error();
				}
				l.close();
			}
		}
		OutputStream ss;
		OutputStream cc;
		Reader s;
		Reader c;
		Death ds=new Death();
		Death dc=new Death();
		boolean isKilled=false;
		SocketManager mother;
		String coding;
		SD(SocketManager mother,String coding){
			this.mother=mother;
			this.coding=coding;
			PipedOutputStream sss=new PipedOutputStream();
			PipedOutputStream ccc=new PipedOutputStream();
			ss=sss;
			cc=ccc;
			try{
				if(coding.equals(KoharuRepeat.r02)){
					s=KrCore.this.mKoharuRepeat.r02Reader(new PipedInputStream(sss));
					c=KrCore.this.mKoharuRepeat.r02Reader(new PipedInputStream(ccc));
				}else if(coding.equals(KoharuRepeat.r08)){
					s=KrCore.this.mKoharuRepeat.r08Reader(new PipedInputStream(sss));
					c=KrCore.this.mKoharuRepeat.r08Reader(new PipedInputStream(ccc));
				}else if(coding.equals(KoharuRepeat.r10)){
					s=KrCore.this.mKoharuRepeat.r10Reader(new PipedInputStream(sss));
					c=KrCore.this.mKoharuRepeat.r10Reader(new PipedInputStream(ccc));
				}else if(coding.equals(KoharuRepeat.r16)){
					s=KrCore.this.mKoharuRepeat.r16Reader(new PipedInputStream(sss));
					c=KrCore.this.mKoharuRepeat.r16Reader(new PipedInputStream(ccc));
				}else{
					s=new InputStreamReader(new PipedInputStream(sss),coding);
					c=new InputStreamReader(new PipedInputStream(ccc),coding);
				}
			}catch(IOException ioe){
				throw new Error();
			}
		}
		void writeC(int i){
			try{
				cc.write(i);
			}catch(IOException ioe){
				throw new Error();
			}
		}
		void writeS(int i){
			try{
				ss.write(i);
			}catch(IOException ioe){
				throw new Error();
			}
		}
		void closeC(){
			try{
				cc.close();
			}catch(IOException ioe){
				throw new Error();
			}
			dc.kill();
		}
		void closeS(){
			try{
				ss.close();
			}catch(IOException ioe){
				throw new Error();
			}
			ds.kill();
		}
		void setLC(L l){
			new Thread(new R2L(c,l)).start();
		}
		void setLS(L l){
			new Thread(new R2L(s,l)).start();
		}
		/*
		void setSL(final SL l){
			L lc=new L(){
				public void write(int i){
					l.writeC(i);
				}
				public void close(){
					l.closeC();
				}
			};
			L ls=new L(){
				public void write(int i){
					l.writeS(i);
				}
				public void close(){
					l.closeS();
				}
			};
			new Thread(new R2L(c,lc)).start();
			new Thread(new R2L(s,ls)).start();
		}
		*/
		public void kill(){
			if(ds.isDeath()&&dc.isDeath())return;
			mother.remove(coding);
		}
		public void join(){
			ds.join();
			dc.join();
		}
	}
	class Death implements Killable{
		boolean death=false;
		public synchronized void kill(){
			death=true;
			notify();
		}
		synchronized boolean isDeath(){
			return death;
		}
		public synchronized void join(){
			while(!death){
				try{
					wait();
				}catch(InterruptedException e){}
				notify();
			}
		}
	}
	interface Killable{
		void kill();
		void join();
	}
	interface L{
		void write(int i);
		void close();
	}
	interface SL{
		void close();
	}
	interface SSL{
		void get(KrCore.SocketManager s);
		void close();
		void error(Exception e);
	}
	ServerSocketCreater getSSC(){
		return new ServerSocketCreater();
	}
}