package com.luzi82.koharurepeat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.nio.charset.Charset;

public class KoharuRepeat{
	String[] charsets=null;
	KoharuRepeat(){
		Object[] ts=Charset.availableCharsets().keySet().toArray();
		charsets=new String[ts.length+4];
		int i;
		for(i=0;i<ts.length;i++){
			charsets[i]=(String)ts[i];
		}
		charsets[i++]=r02;
		charsets[i++]=r08;
		charsets[i++]=r10;
		charsets[i++]=r16;
		KrGUI gui=new KrGUI(this, new KrCore(this));
	}
	Reader r02Reader(final InputStream in){
		final PipedReader pr;
		final PipedWriter pw;
		try{
			pr=new PipedReader();
			pw=new PipedWriter(pr);
		}catch(IOException e){
			throw new Error();
		}
		new Thread(new Runnable(){
			boolean first=true;
			public void run(){
				try{
					while(true){
						if(first){
							first=false;
						}else{
							pw.write(" ");
						}
						int i=in.read();
						if(i==-1)break;
						StringBuffer t=new StringBuffer(Integer.toBinaryString(i));
						while(t.length()<8){
							t.insert(0,'0');
						}
						pw.write(new String(t));
					}
				}catch(IOException e){
					throw new Error();
				}
				try{
					pw.close();
				}catch(IOException e){
					throw new Error();
				}
			}
		}).start();
		return pr;
	}
	Reader r08Reader(final InputStream in){
		final PipedReader pr;
		final PipedWriter pw;
		try{
			pr=new PipedReader();
			pw=new PipedWriter(pr);
		}catch(IOException e){
			throw new Error();
		}
		new Thread(new Runnable(){
			boolean first=true;
			public void run(){
				try{
					while(true){
						if(first){
							first=false;
						}else{
							pw.write(" ");
						}
						int i=in.read();
						if(i==-1)break;
						StringBuffer t=new StringBuffer(Integer.toOctalString(i));
						while(t.length()<3){
							t.insert(0,'0');
						}
						pw.write(new String(t));
					}
				}catch(IOException e){
					throw new Error();
				}
				try{
					pw.close();
				}catch(IOException e){
					throw new Error();
				}
			}
		}).start();
		return pr;
	}
	Reader r10Reader(final InputStream in){
		final PipedReader pr;
		final PipedWriter pw;
		try{
			pr=new PipedReader();
			pw=new PipedWriter(pr);
		}catch(IOException e){
			throw new Error();
		}
		new Thread(new Runnable(){
			boolean first=true;
			public void run(){
				try{
					while(true){
						if(first){
							first=false;
						}else{
							pw.write(" ");
						}
						int i=in.read();
						if(i==-1)break;
						StringBuffer t=new StringBuffer(Integer.toString(i));
						while(t.length()<3){
							t.insert(0,'0');
						}
						pw.write(new String(t));
					}
				}catch(IOException e){
					throw new Error();
				}
				try{
					pw.close();
				}catch(IOException e){
					throw new Error();
				}
			}
		}).start();
		return pr;
	}
	Reader r16Reader(final InputStream in){
		final PipedReader pr;
		final PipedWriter pw;
		try{
			pr=new PipedReader();
			pw=new PipedWriter(pr);
		}catch(IOException e){
			throw new Error();
		}
		new Thread(new Runnable(){
			boolean first=true;
			public void run(){
				try{
					while(true){
						if(first){
							first=false;
						}else{
							pw.write(" ");
						}
						int i=in.read();
						if(i==-1)break;
						StringBuffer t=new StringBuffer(Integer.toHexString(i));
						while(t.length()<2){
							t.insert(0,'0');
						}
						pw.write(new String(t));
					}
				}catch(IOException e){
					throw new Error();
				}
				try{
					pw.close();
				}catch(IOException e){
					throw new Error();
				}
			}
		}).start();
		return pr;
	}
	static String r02="Base 2 Number";
	static String r08="Base 8 Number";
	static String r10="Base 10 Number";
	static String r16="Base 16 Number";
	static public void main(String[] arg){
		new KoharuRepeat();
	}
}