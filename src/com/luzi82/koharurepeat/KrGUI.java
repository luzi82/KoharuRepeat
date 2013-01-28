package com.luzi82.koharurepeat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import com.luzi82.koharurepeat.KrCore.Killable;
import com.luzi82.koharurepeat.KrCore.L;
import com.luzi82.koharurepeat.KrCore.SL;
import com.luzi82.koharurepeat.KrCore.SSL;

class KrGUI{
	/**
	 * 
	 */
	private final KoharuRepeat mSocketHack;
	class FrameK extends Frame{
		Killable k;
		FrameK(String title,Killable k){
			super(title);
			this.k=k;
		}
		public void dispose(){
			k.kill();
			k.join();
			super.dispose();
		}
	}
	class Close extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			e.getWindow().dispose();
		}
	}
	final Font font=new Font("Monospaced",Font.PLAIN,12);
	void showSSC(final KrCore.ServerSocketCreater core){
		final Frame f=new Frame("ServerSocket creator");
		final TextField cPort=new TextField("",6),sHost=new TextField("",25),sPort=new TextField("",6);
		Button start=new Button("Start");
		f.setLayout(new FlowLayout());
		f.add(cPort);f.add(sHost);f.add(sPort);f.add(start);
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					showSS(core.create(Integer.parseInt(cPort.getText()),sHost.getText(),Integer.parseInt(sPort.getText())));
				}catch(NumberFormatException nfe){
					showE(f,"port field should be a number");
				}catch(IOException ioe){
					showE(f,ioe.getMessage());
				}
			}
		});
		f.addWindowListener(new Close());
		f.pack();
		f.setVisible(true);
	}
	void showSS(final KrCore.ServerSocketManager core){
		final Frame f=new FrameK("ServerSocket",core);
		Label label=new Label(core.getCPort()+" -> "+core.getSHost()+":"+core.getSPort());
		final java.awt.List list=new java.awt.List(10,true);
		for(int i=0;i<mSocketHack.charsets.length;i++){
			list.add(mSocketHack.charsets[i]);
		}
		f.setLayout(new BorderLayout());
		f.add(label,"North");f.add(list,"Center");
		core.setL(new SSL(){
			public void get(KrCore.SocketManager cs){
				int[] iv=list.getSelectedIndexes();
				for(int i=0;i<iv.length;i++){
					showSD(cs.add(KrGUI.this.mSocketHack.charsets[iv[i]]),cs.getCHost()+":"+cs.getCPort()+" -> "+KrGUI.this.mSocketHack.charsets[iv[i]]+" -> "+cs.getSHost()+":"+cs.getSPort());
				}
				showS(cs);
			}
			public void close(){
				list.removeAll();
				list.setEnabled(false);
			}
			public void error(Exception e){
				showE(f,e.getMessage());
				f.dispose();
			}
		});
		f.addWindowListener(new Close());
		f.pack();
		f.setVisible(true);
	}
	void showS(final KrCore.SocketManager core){
		final Frame f=new FrameK("Socket",core);
		Panel s=new Panel();
		Label l=new Label(core.getCHost()+":"+core.getCPort()+" -> "+core.getSHost()+":"+core.getSPort());
		final Choice choice=new Choice();
		final Button display=new Button("Display");
		for(int i=0;i<mSocketHack.charsets.length;i++){
			choice.add(mSocketHack.charsets[i]);
		}
		f.setLayout(new BorderLayout());
		s.setLayout(new FlowLayout());
		s.add(choice);s.add(display);
		f.add(l,"North");f.add(s,"South");
		display.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				KrCore.SD sdc=core.add(choice.getSelectedItem());
				if(sdc==null)return;
				showSD(sdc,core.getCHost()+":"+core.getCPort()+" -> "+choice.getSelectedItem()+" -> "+core.getSHost()+":"+core.getSPort());
			}
		});
		core.start(new SL(){
			public void close(){
				choice.setEnabled(false);
				display.setEnabled(false);
			}
		});
		f.addWindowListener(new Close());
		f.pack();
		f.setVisible(true);
	}
	void showSD(final KrCore.SD core,String title){
		class L2TA implements L{
			TextArea ta;
			L2TA(TextArea ta){
				this.ta=ta;
			}
			public void write(int i){
				ta.append(Character.toString((char)i));
			}
			public void close(){
				ta.setEditable(false);
			}
		}
		final Frame f=new FrameK("Socket display",core);
		final TextArea s=new TextArea(),c=new TextArea();
		Label l=new Label(title);
		Panel pc=new Panel();
		s.setFont(font);c.setFont(font);
		f.setLayout(new BorderLayout());
		pc.setLayout(new GridLayout(2,1));
		pc.add(s);pc.add(c);
		f.add(l,"North");f.add(pc,"Center");
		core.setLS(new L2TA(s));core.setLC(new L2TA(c));
		f.addWindowListener(new Close());
		f.pack();
		f.setVisible(true);
	}
	KrGUI(KoharuRepeat socketHack, KrCore core){
		mSocketHack = socketHack;
		showSSC(core.getSSC());
	}
	void showE(Frame owner,String m){
		final Dialog d=new Dialog(owner,"Error");
		Label l=new Label(m);
		Button ok=new Button("Ok");
		d.setLayout(new FlowLayout());
		d.add(l);d.add(ok);
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				d.dispose();
			}
		});
		d.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				d.dispose();
			}
		});
		d.pack();
		d.show();
	}
}