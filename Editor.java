import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
class Editor extends WindowAdapter implements ActionListener,MouseListener,KeyListener
{
	JFrame f;	JMenuBar mb; JMenu m1,m2;
	JMenuItem nw,opn,sve,svea,ext,fnd,rep,prt,cut,copy,paste;
	JTextArea t;		JTextField tf,tf1,tf2;
	JDialog d,d1,d2,d3;
	JDialog d11;
	JButton b1,b2,b3,b4,b5,b6,b7,b8,b9;
	JButton b11,b12,b13;
	int count=0; //To check whether file is saved or not(Warning).
	int k=0;	//Different functions of b2.
	int flag1,flag2; //For Replace & Replace all.
	int flag3=0;
	int flag=0;
	Writer w;
	String path,name;
	int position=0;
	int flag11=0;
	int flag12=0; //For Text Value changed conflict in save.
	WindowCloser w1=new WindowCloser();
	WindowCloser w2=new WindowCloser();
	JScrollPane sp;
	public Editor()
	{
		f=new JFrame();		f.setSize(400,300);
		f.addWindowListener(this);
		t=new JTextArea();
		Font font = new Font("Comic Sans MS", Font.PLAIN, 20);
	    t.setFont(font);
		sp=new JScrollPane(t,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		f.getContentPane().add(sp);
		//sp.setVisible(true);
		t.addKeyListener(this);
		mb=new JMenuBar();
		m1=new JMenu("File");
		m2=new JMenu("Edit");
		nw=new JMenuItem("New");		opn=new JMenuItem("Open");
		sve=new JMenuItem("Save");		svea=new JMenuItem("Save As..");
		ext=new JMenuItem("Exit");		fnd=new JMenuItem("Find");
		rep=new JMenuItem("Find & Replace");
		prt=new JMenuItem("Print");
		cut=new JMenuItem("Cut");	copy=new JMenuItem("Copy");	paste=new JMenuItem("Paste");
		
		nw.addActionListener(this);		opn.addActionListener(this);
		sve.addActionListener(this);	svea.addActionListener(this);
		ext.addActionListener(this);	fnd.addActionListener(this);
		prt.addActionListener(this);	cut.addActionListener(this);
		rep.addActionListener(this);	copy.addActionListener(this);
		paste.addActionListener(this);
		
		m1.add(nw);		m1.add(opn);	m1.add(sve);	m1.add(svea);	m1.add(prt);
		m1.addSeparator();				m1.add(ext);	
		m2.add(cut);	m2.add(copy);	m2.add(paste);
		m2.addSeparator(); m2.add(fnd);	m2.add(rep);
		mb.add(m1);		mb.add(m2);
		f.setJMenuBar(mb);
		f.add(t);
		d=new JDialog(f,"Editor");
		d.setSize(300,100); 
		d.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.weightx=1.0;	gbc.weighty=1.0;
		Label l1=new Label("Do you want to save changes:");
		gbc.gridx=0;		gbc.gridy=0;	d.add(l1,gbc);
		b1=new JButton("Save");			b1.addActionListener(this);
		gbc.gridx=0;		gbc.gridy=1;	d.add(b1,gbc);
		b2=new JButton("Don't Save");	b2.addActionListener(this);
		gbc.gridx=1;		gbc.gridy=1;	d.add(b2,gbc);
		b3=new JButton("Cancel");		b3.addActionListener(this);
		gbc.gridx=3;		gbc.gridy=1;	d.add(b3,gbc);
		d3=new JDialog(f,"Error");
		d3.setSize(200,80);
		d3.setLayout(new GridLayout());
		GridBagConstraints gbc1=new GridBagConstraints();gbc1.weightx=1.0;gbc1.weighty=1.0;
		Label l3=new Label("End of String");
		gbc1.gridx=0; gbc1.gridy=0;				d3.add(l3);
		d3.addWindowListener(w2);
		d.addWindowListener(w1);
		
		d11=new JDialog(f,"Editor");
		d11.setSize(300,100);
		d11.setLayout(new GridBagLayout());
		Label l2=new Label("Do you want to save changes:");
		gbc.gridx=0;		gbc.gridy=0;	d11.add(l2,gbc);
		b11=new JButton("Save");			b11.addActionListener(this);
		gbc.gridx=0;		gbc.gridy=1;	d11.add(b11,gbc);
		b12=new JButton("Don't Save");	b12.addActionListener(this);
		gbc.gridx=1;		gbc.gridy=1;	d11.add(b12,gbc);
		b13=new JButton("Cancel");		b13.addActionListener(this);
		gbc.gridx=3;		gbc.gridy=1;	d11.add(b13,gbc);
		d11.addWindowListener(w1);
		
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent e1)
	{
		String str=e1.getActionCommand();
		System.out.println(str+" was clicked.");
		
		if(e1.getSource()==nw)
		{
			if(count==0 && flag11==1 || t.getText().equals(""))
			{
				t.setText("");
				path=null; name=null;
			}
			else
			{
				d11.setVisible(true);
			}
		}

		Open:if(e1.getSource()==opn)
		{
			if(count==0 && flag11==1 || t.getText().equals("")) //File is saved or file is blank.
			{	
				FileDialog fd=new FileDialog(f,"Open",FileDialog.LOAD);
				fd.setVisible(true);
				name=fd.getFile();
				if(fd.getFile()==null)
				{		
					break Open;
				}	
				path=fd.getDirectory();
				t.setText("");
				try
				{
					FileInputStream fis=new FileInputStream(path+name);
					BufferedInputStream bis=new BufferedInputStream(fis);
					int ch;
					while((ch=bis.read())!=-1)		
						t.setText(""+t.getText()+(char)ch);
					bis.close();
					count=0;
					flag11=1;
				}	
				catch(IOException e)
				{
					System.out.print(e.getMessage());
				}
				
			}
			
			else
			{
				k=2;
				d.setVisible(true);
			}
		
		}
		
		if(e1.getSource()==sve)
		{
			if(flag11==1 && count==1) //File should be saved & overwritten.
				if(path!=null)
					try
					{
						System.out.println("File Saved");
						w = new FileWriter(path+name);  
						w.write(t.getText());
						count=0;
						w.close();  
					}	
					catch(IOException e3)
					{
						System.out.print(e3.getMessage());
					}
			
			Save:if(flag11==0 || path==null) //Entry at first time only OR when path is NULL.
			{
				FileDialog fd=new FileDialog(f,"SELECT",FileDialog.SAVE);
				fd.setVisible(true);
				name=fd.getFile();
				if(fd.getFile()==null)
				{		
					break Save;
				}
				path=fd.getDirectory();
				count=0;
				flag11=1;
				try
				{
				 	Writer w = new FileWriter(path+name);  
		         	w.write(t.getText());  
		         	w.close();  
				}	
				catch(IOException e3)
				{
					System.out.print(e3.getMessage());
				}
				
			}
				
			
		}

		SaveAs:if(e1.getSource()==svea)
		{
			FileDialog fd=new FileDialog(f,"SELECT",FileDialog.SAVE);
			fd.setVisible(true);
			name=fd.getFile();
			if(fd.getFile()==null)
				break SaveAs;
			path=fd.getDirectory();
			count=0;
			flag11=1;
			try
			{
			 	w = new FileWriter(path+name);  
	         	w.write(t.getText());  
	         	w.close();  
			}	
			catch(IOException e3)
			{
				System.out.print(e3.getMessage());
			}

		}
		
		if(e1.getSource()==prt)
		{
			try {
				t.print();
			}
			catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(e1.getSource()==ext)
		{
			if(count==0 && flag11==1)
			{	
				f.setVisible(false);
				f.dispose();
				System.exit(1);
			}
			else
			{
				k=3;
				d.setVisible(true);
			}
		}

		B1:if(e1.getSource()==b1) //Save Button
		{
			if(flag11==0)         //File was not saved.
			{
				FileDialog fd=new FileDialog(f,"SELECT",FileDialog.SAVE);
				d.setVisible(false);
				d.dispose();
				fd.setVisible(true);
				name=fd.getFile();
				if(fd.getFile()==null)
					break B1;
				
				path=fd.getDirectory();
				count=0;
				flag11=1;
				try
				{
					w = new FileWriter(path+name);  
					w.write(t.getText());  
					w.close();  
				}	
				catch(IOException e4)
				{
					System.out.print(e4.getMessage());
				}
				d.setVisible(false);
				d.dispose();
				
				if(k==3)
				{
					f.setVisible(false);
					f.dispose();
					System.exit(1);
				}
				
			}
			
			else
			{
				try
				{
					w = new FileWriter(path+name);  
					w.write(t.getText());  
					w.close();
					count=0;
					flag11=1;
					System.out.println("File Saved");
					if(k==3)
					{
						f.setVisible(false);
						f.dispose();
						System.exit(1);
					}
				}
				
				catch(IOException e3)
				{
					System.out.print(e3.getMessage());
				}
				
				d.setVisible(false);
				d.dispose();
				
				Open1:if(k==2) //Flag to save and open the open file dialog. //&&flag11==1
				{
					FileDialog fd=new FileDialog(f,"Open",FileDialog.LOAD);
					fd.setVisible(true);
					name=fd.getFile();
					if(fd.getFile()==null)		
						break Open1;
					path=fd.getDirectory();
					t.getText();
					System.out.print("Blank");
					t.setText("");
					try
					{
						
						FileInputStream fis=new FileInputStream(path+name);
						int ch;
						while((ch=fis.read())!=-1)		
							t.setText(""+t.getText()+(char)ch);
						fis.close();
						k=0;
					}
					catch(IOException e)
					{
						System.out.print(e.getMessage());
					}
					
				}
				
			}
		}

		if(e1.getSource()==b2) //Don't Save Button
		{
			if(k==0||k==3)
			{	f.setVisible(false);
				f.dispose();
			}
			
			Open2:if(k==2)
			{
				d.setVisible(false);
				d.dispose();
				FileDialog fd=new FileDialog(f,"Open",FileDialog.LOAD);
				fd.setVisible(true);
				name=fd.getFile();
				if(fd.getFile()==null)
				{		
					break Open2;
				}
				path=fd.getDirectory();
				t.getText();
				t.setText("");
				try
				{
					
					FileInputStream fis=new FileInputStream(path+name);
					int ch;
					while((ch=fis.read())!=-1)		
						t.setText(""+t.getText()+(char)ch);
					fis.close();
					flag11=1;
					count=0;
					k=0;
					
				}
				catch(IOException e)
				{
					System.out.print(e.getMessage());
				}
			}
			
		}

		if(e1.getSource()==b3 || e1.getSource()==b13)
		{
			d.setVisible(false);
			d.dispose();
			d11.setVisible(false);
			d11.dispose();
		}
		
		if(e1.getSource()==b11)
		{
			Save:if(flag11==0 || path==null) //Entry at first time only OR when path is NULL.
			{
				d11.setVisible(false);
				d11.dispose();
				FileDialog fd=new FileDialog(f,"SELECT",FileDialog.SAVE);
				fd.setVisible(true);
				name=fd.getFile();
				if(fd.getFile()==null)
				{		
					break Save;
				}
				path=fd.getDirectory();
				count=0;
				flag11=1;
				try
				{
				 	Writer w = new FileWriter(path+name);  
		         	w.write(t.getText());  
		         	w.close();  
				}	
				catch(IOException e3)
				{
					System.out.print(e3.getMessage());
				}
				
				t.setText("");
				path=null; name=null;
				
			}
		
			else if(flag11==1 && count==1) //File should be saved & overwritten.
				if(path!=null)
				try
				{
					d11.setVisible(false);
					d11.dispose();
					System.out.println("File Saved");
					w = new FileWriter(path+name);  
					w.write(t.getText());
					count=0;
					w.close();  
				}	
				catch(IOException e3)
				{
					System.out.print(e3.getMessage());
				}
				t.setText("");
				path=null; name=null;
		}
		
		if(e1.getSource()==b12)
		{
			d11.setVisible(false);
			d11.dispose();	
			t.setText("");
			path=null; name=null;
		}
		
		
		
		
//EDIT MENU---------------------------------------------------------------------------------
		Find:if(e1.getSource()==fnd)
		{
			t.addMouseListener(this);
			if(t.getText().equals(""))
				{
					break Find;
				}
			d1=new JDialog(f,"Find");
			d1.setSize(300,100); 
			d1.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.weightx=1.0;	gbc.weighty=1.0;
			JLabel l1=new JLabel("Find what:");
			gbc.gridx=0;		gbc.gridy=0;	d1.add(l1,gbc);
			tf=new JTextField(16);
			gbc.gridx=1;		gbc.gridy=0;	d1.add(tf, gbc);		
			b4=new JButton("Find Next");			b4.addActionListener(this);
			gbc.gridx=0;		gbc.gridy=1;	d1.add(b4,gbc);
			b5=new JButton("Cancel");	b5.addActionListener(this);
			gbc.gridx=1;		gbc.gridy=1;	d1.add(b5,gbc);
			d1.setVisible(true);
			d1.addWindowListener(w2);
		}
		
		if(e1.getSource()==b4) //Find Next
		{
			Pattern p=Pattern.compile(tf.getText());
			String s5=t.getText();
			Matcher m=p.matcher(t.getText());
			s5=s5.replaceAll("\r\n","\n");
            t.setText(s5);
			if(m.find(position))
			{
				t.select(m.start(),m.end());
				f.toFront();
				position=m.end();
			}
			else
			{
				d3.setVisible(true);
			}
			flag1=1;
		}
		
		if(e1.getSource()==b5)
		{
			d1.setVisible(false);
			d1.dispose();
		}
		
		if(e1.getSource()==rep)
		{
			t.addMouseListener(this);
			d2=new JDialog(f,"Replace");
			d2.setSize(300,100); 
			d2.setLayout(new GridBagLayout());
			GridBagConstraints gbc=new GridBagConstraints();
			gbc.weightx=1.0;	gbc.weighty=1.0;
			Label l1=new Label("Find what:");
			gbc.gridx=0;		gbc.gridy=0;	d2.add(l1,gbc);
			tf1=new JTextField(16);
			gbc.gridx=1;		gbc.gridy=0;	d2.add(tf1, gbc);
			Label l2=new Label("Replace with:");
			gbc.gridx=0;		gbc.gridy=1;	d2.add(l2,gbc);
			tf2=new JTextField(16);
			gbc.gridx=1;		gbc.gridy=1;	d2.add(tf2,gbc);
			b6=new JButton("Find Next");			b6.addActionListener(this);
			gbc.gridx=2;		gbc.gridy=0;	d2.add(b6,gbc);
			b7=new JButton("Replace");			b7.addActionListener(this);
			gbc.gridx=2;		gbc.gridy=1;	d2.add(b7,gbc);
			b8=new JButton("Replace All");		b8.addActionListener(this);
			gbc.gridx=2;		gbc.gridy=2;	d2.add(b8,gbc);
			b9=new JButton("Cancel");			b9.addActionListener(this);
			gbc.gridx=1;		gbc.gridy=2;	d2.add(b9,gbc);
			d2.setVisible(true);
			d2.addWindowListener(w2);
			
		}
		
		if(e1.getSource()==b6) //Find Next of Replace
		{
			Pattern p=Pattern.compile(tf1.getText());
			String s5=t.getText();
			Matcher m=p.matcher(t.getText());
			s5=s5.replaceAll("\r\n","\n");
            t.setText(s5);
			if(m.find(position))
			{
				t.select(m.start(),m.end());
				f.toFront();
				position=m.end();
			}
			else
			{
				d3.setVisible(true);
			}	
			flag1=1;
			
		}
		
		Replace:if(e1.getSource()==b7)
		{
			if(t.getText().equals(""))
				break Replace;
			
			if(flag2==0)
			{	
				t.replaceRange(tf2.getText(),t.getSelectionStart(),t.getSelectionEnd());
				flag2=1;
			}
			
			Pattern p=Pattern.compile(tf1.getText());
			String s5=t.getText();
			Matcher m=p.matcher(t.getText());
			s5=s5.replaceAll("\r\n","\n");
			t.setText(s5);
			if(m.find(position))
			{
				t.select(m.start(),m.end());
				f.toFront();
				position=m.end();
				flag2=0;
			}
			else
				d3.setVisible(true);
		}
		
		if(e1.getSource()==b8) //Replace All
		{
			String s=null;
			Pattern p=Pattern.compile(tf1.getText());
			String s5=t.getText();
			Matcher m=p.matcher(t.getText());
			s5=s5.replaceAll("\r\n","\n");
			t.setText(s5);
			if(m.find())
				s=m.replaceAll(tf2.getText());
			t.setText(s);
		}
		
		if(e1.getSource()==b9)
		{
			d2.setVisible(false);
			d2.dispose();
		}
		
		if(e1.getSource()==cut)
			t.cut();
		
		if(e1.getSource()==copy)
			t.copy();
		
		if(e1.getSource()==paste)
			t.paste();

	}
//----------------------------------------------------------------------------------------
	
	public void keyTyped(KeyEvent arg0) 
	{
		count=1;	
	}
	
	public void mouseClicked(MouseEvent arg0) 
	{
		position=t.getCaretPosition();
	}


	public void windowClosing(WindowEvent e2)
	{
		if(count==0)
		{	
			Window w=e2.getWindow();
			w.setVisible(false);
			w.dispose();
			System.exit(1);
		}
		else
		{
			k=3;
			d.setVisible(true);
		}
	}

	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		Editor e=new Editor();
	}

	
//-------------------------------------------------------------------------------------
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}