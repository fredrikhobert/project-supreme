package supreme.paths;

import static javax.swing.JOptionPane.*;
import graphs.Graphs;
import graphs.Edge;
import graphs.ListGraph;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SupremePaths extends JFrame {

	private Background bg;
	private JPanel north;
	private JMenuBar menu;
	private JMenu archive, operations;
	private JButton bFindConnection, bShowConnection, bNewNode, bNewConnection, bChangeConnection; 
	private JMenuItem mNewMap, mQuit, mFindConnection, mShowConnection, mNewNode, mNewConnection, 
		mChangeConnection, mOpen, mSave, mSaveAs;
	private Node activeNodeOne, activeNodeTwo;
	private NodeMouseList nml;
	private ListGraph<Node> lg;
	private ArrayList<String> locationNames;
	private JFileChooser jfc;
	private boolean changes;
	private String fileName;
	private FileNameExtensionFilter pictureFilter;
	private FileNameExtensionFilter mapFilter;

	SupremePaths(){
		super("SUPREME PATH'S");

		north = new JPanel();
		add(north, BorderLayout.NORTH);		

		menu = new JMenuBar();
		setJMenuBar(menu);

		archive = new JMenu("Arkiv");
		operations = new JMenu("Operationer");

		mNewMap = new JMenuItem("Nytt");
		mNewMap.addActionListener(new NewMap());

		mSave = new JMenuItem("Spara");
		mSave.addActionListener(new Save());

		mSaveAs = new JMenuItem("Spara som");
		mSaveAs.addActionListener(new SaveAs());

		mOpen = new JMenuItem("Öppna");
		mOpen.addActionListener(new Open());

		mQuit = new JMenuItem("Avsluta");
		mFindConnection = new JMenuItem("Hitta väg");
		mFindConnection.addActionListener(new FindConnection());
		mShowConnection = new JMenuItem("Visa förbindelse");
		mShowConnection.addActionListener(new ShowConnection());

		mNewNode = new JMenuItem("Ny plats");
		mNewNode.addActionListener(new NewNode());

		mNewConnection = new JMenuItem("Ny förbindelse");
		mNewConnection.addActionListener(new NewConnection());
		mChangeConnection = new JMenuItem("Ändra förbindelse");
		mChangeConnection.addActionListener(new ChangeConnection());

		menu.add(archive);
		menu.add(operations);		
		archive.add(mNewMap);
		archive.add(mOpen);
		archive.add(mSave);
		archive.add(mSaveAs);
		archive.add(mQuit);	
		operations.add(mFindConnection);
		operations.add(mShowConnection);
		operations.add(mNewNode);
		operations.add(mNewConnection);
		operations.add(mChangeConnection);

		bFindConnection = new JButton("Hitta väg");
		bFindConnection.addActionListener(new FindConnection());
		bShowConnection = new JButton("Visa förbindelse");
		bShowConnection.addActionListener(new ShowConnection());

		bNewNode = new JButton("Ny plats");
		bNewNode.addActionListener(new NewNode());

		bNewConnection = new JButton("Ny förbindelse");
		bNewConnection.addActionListener(new NewConnection());
		bChangeConnection = new JButton("Ändra förbindelse");
		bChangeConnection.addActionListener(new ChangeConnection());

		north.add(bFindConnection);
		north.add(bShowConnection);
		north.add(bNewNode);
		north.add(bNewConnection);	
		north.add(bChangeConnection);

		addWindowListener(new ExitListener());
		pictureFilter = new FileNameExtensionFilter("Bilder", "png", "jpg", "gif");
		mapFilter = new FileNameExtensionFilter("Kartor", "karta");
		
		bg = null;
		activeNodeOne = null;
		activeNodeTwo = null;
		changes = false;
		lg = new ListGraph<Node>();
		nml = new NodeMouseList();
		locationNames = new ArrayList<String>();
		jfc = new JFileChooser(".");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}


	class Save implements ActionListener{			
		public void actionPerformed(ActionEvent ave){

			if(fileName == null){
				SaveAs sas = new SaveAs();
				sas.actionPerformed(null);
				return;
			}

			try{
				FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				removeMouseListeners();
				oos.writeObject(bg);
				oos.writeObject(lg);
				addMouseListeners();
				oos.close();
			}catch(FileNotFoundException fnfe){
				System.err.println(fnfe.getMessage());
			}catch(IOException ioe){
				System.err.println(ioe.getMessage());
			}

		}
	}


	class SaveAs implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			int reply = jfc.showSaveDialog(SupremePaths.this);
			if (reply != JFileChooser.APPROVE_OPTION)
				return;

			File file = jfc.getSelectedFile();
			fileName = file.getAbsolutePath();
			if(!fileName.endsWith(".karta"));
			fileName += ".karta";

			try{
				FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);			
				removeMouseListeners();
				oos.writeObject(bg);
				oos.writeObject(lg);
				addMouseListeners();
				oos.close();
			}catch(FileNotFoundException fnfe){
				System.err.println(fnfe.getMessage());
			}catch(IOException ioe){
				System.err.println(ioe.getMessage());
			}

		}
	}


	class Open implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(bg != null){
				prepareForNewMap();
			}
			jfc.addChoosableFileFilter(mapFilter);
			int reply = jfc.showOpenDialog(SupremePaths.this);
			if (reply != JFileChooser.APPROVE_OPTION)
				return;

			File file = jfc.getSelectedFile();
			fileName = file.getAbsolutePath();
			try{
				FileInputStream fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);

				bg = (Background)ois.readObject();	
				lg = (ListGraph<Node>)ois.readObject();		
	
				addMouseListeners();
				add(bg);
				validate();
				repaint();
				setSize(bg.getWidth(), bg.getHeight());
				ois.close();
			}catch(FileNotFoundException fnfe){
				System.err.println("Finns inte!");
			}catch(ClassNotFoundException e){
				System.err.println("Fel:" + e.getMessage());
			}catch(IOException ioe){
				System.err.println("Fel:" + ioe.getMessage());
			}
		}
	}
	
	
	class NewMap implements ActionListener{
		public void actionPerformed(ActionEvent ave){			
			if(bg != null){
				prepareForNewMap();
			}
			jfc.addChoosableFileFilter(pictureFilter);
			int reply = jfc.showOpenDialog(SupremePaths.this);			
			if (reply != JFileChooser.APPROVE_OPTION)
				return;
			File mapImg = jfc.getSelectedFile(); 
			String imgConnection = mapImg.getAbsolutePath();
			bg = new Background(imgConnection);	
			add(bg, BorderLayout.CENTER);
			setSize(bg.getWidth(), bg.getHeight());
			bg.setLayout(null);
			fileName = null;
			changes = true;
			repaint();
			setVisible(true);
		}			
	}

	
	class NewNode implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(bg != null){
				bg.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				bg.addMouseListener(new SelectNodePos());
				changes = true;
			}
		}
	}


	class NewConnection implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(activeNodeOne != null && activeNodeTwo != null){
				for(;;){
					try{
						AddConnectionPanel acp = new AddConnectionPanel(activeNodeOne, activeNodeTwo);
						int query = showConfirmDialog(SupremePaths.this, acp,"Ny förbindelse", OK_CANCEL_OPTION);
						if (query != YES_OPTION)
							return;
						lg.connectNodes(activeNodeOne, activeNodeTwo, acp.getConnectionName(), Integer.parseInt(acp.getTime()));
						changes = true;
						return;
					} catch(NumberFormatException e){
						showMessageDialog(SupremePaths.this, "Tid måste vara numeriskt!", "Fel", ERROR_MESSAGE);
					}
				}
			} else {
				showMessageDialog(SupremePaths.this, "Två noder måste vara markerade", "Fel", ERROR_MESSAGE);
			}
		}
	}

	
	
	class ChangeConnection implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(activeNodeOne != null && activeNodeTwo != null){
				for(;;){
					try{						
						ArrayList<Edge<Node>> edges = lg.getEdgesBetween(activeNodeOne, activeNodeTwo);
						Edge<Node> selectedEdge = null;

						if(edges.size() == 1){
							selectedEdge = edges.get(0);
						} else if(edges.size() > 1){
							SelectConnectionPanel scp = new SelectConnectionPanel (activeNodeOne, activeNodeTwo, edges);
							int query = showConfirmDialog(SupremePaths.this, scp,"Ändra förbindelse", OK_CANCEL_OPTION);							
							if (query != YES_OPTION)
								return;
							if(scp.getSelectedEdge() == null)
								throw new IllegalStateException();
							selectedEdge = scp.getSelectedEdge();
						} 

						ChangeConnectionPanel ccp = new ChangeConnectionPanel(activeNodeOne, activeNodeTwo, selectedEdge);
						int queryTwo = showConfirmDialog(SupremePaths.this, ccp,"Ändra förbindelse", OK_CANCEL_OPTION);
						if (queryTwo != YES_OPTION)
							return;
						lg.setConnectionWeight(activeNodeOne, activeNodeTwo, selectedEdge.getName(), Integer.parseInt(ccp.getTime()));
						changes = true;
						return;
					} catch(NumberFormatException e){
						showMessageDialog(SupremePaths.this, "Fel!!", "Fel", ERROR_MESSAGE);
					} catch(IllegalStateException ise){
						showMessageDialog(SupremePaths.this, "Välj förbindelse", "Fel", ERROR_MESSAGE);
					}
				}
			} else {
				showMessageDialog(SupremePaths.this, "Två noder måste vara markerade", "Fel", ERROR_MESSAGE);
			}
		}
	}


	class ShowConnection implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(activeNodeOne != null && activeNodeTwo != null){
				for(;;){
					try{
						ShowConnectionPanel scp = new ShowConnectionPanel(activeNodeOne, activeNodeTwo);
						int query = showConfirmDialog(SupremePaths.this, scp,"Visa förbindelse", OK_CANCEL_OPTION);
						if (query != YES_OPTION)
							return;					

						return;
					} catch(Exception e){
						showMessageDialog(SupremePaths.this, "Fel!!", "Fel", ERROR_MESSAGE);
					}
				}
			} else {
				showMessageDialog(SupremePaths.this, "Två noder måste vara markerade", "Fel", ERROR_MESSAGE);
			}
		}
	}

	
	class FindConnection implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			if(activeNodeOne != null && activeNodeTwo != null){
				for(;;){
					try{
						ArrayList<Edge<Node>> edges = Graphs.getPath(lg, activeNodeOne, activeNodeTwo);
						if(edges == null)
							throw new Exception();

						FindConnectionPanel fcp = new FindConnectionPanel(edges);
						int query = showConfirmDialog(SupremePaths.this, fcp, "Snabbaste vägen", DEFAULT_OPTION);
						if (query != YES_OPTION)
							return;					

						return;
					} catch(Exception e){
						showMessageDialog(SupremePaths.this, "Finns ingen väg mellan noderna", "Fel", ERROR_MESSAGE);
						break;
					}
				}
			} else {
				showMessageDialog(SupremePaths.this, "Två noder måste vara markerade", "Fel", ERROR_MESSAGE);
			}
		}
	}


	class SelectNodePos extends MouseAdapter{
		public void mouseClicked(MouseEvent pos){
			int x = pos.getX();
			int y = pos.getY();
			AddNodePanel anp = new AddNodePanel();			
			
			for(;;){
				try{
					int query = showConfirmDialog(SupremePaths.this, anp, "Ny plats", OK_CANCEL_OPTION);
					String name = anp.getLocationName();
					if (query != YES_OPTION)
						return;																							
					if(name.equals(""))
						throw new Exception();

					if(!locationNames.contains(name)){
						Node node = new Node(name);
						node.addMouseListener(nml);
						node.setBounds(x, y, 25, 25);					
						lg.addNode(node);
						bg.add(node);	
						locationNames.add(name);
						JLabel locationName = new JLabel(name);	
						locationName.setFont(new Font("Verdana", Font.BOLD, 24));
						locationName.setBounds(x+20, y+20, 200, 30);
						locationName.setForeground(Color.BLACK);
						bg.add(locationName);					
						bg.removeMouseListener(this);
						bg.setCursor(Cursor.getDefaultCursor());
						repaint();
						setVisible(true);
					}
					return;

				}catch(Exception e){
					showMessageDialog(SupremePaths.this, "Noden måste ha ett namn!", "Fel", ERROR_MESSAGE);
				}
			}			
		}
	}


	class AddNodePanel extends JPanel{		
		JLabel nodeName = new JLabel("Platsens namn: ");
		JPanel pan = new JPanel();
		JTextField tf = new JTextField(15);

		AddNodePanel(){
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			pan.add(nodeName);
			pan.add(tf);		
			add(pan);
		}

		public String getLocationName(){
			return tf.getText();		
		}
	}


	class ShowConnectionPanel extends JPanel{
		JPanel pan = new JPanel();
		JLabel connectionInfo;
		JTextArea textArea = new JTextArea(10, 30);
		JScrollPane scroll=new JScrollPane(textArea);
		ArrayList<Edge<Node>> edges;
		String str = "";

		ShowConnectionPanel(Node from, Node to){
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			connectionInfo = new JLabel("Förbindelse från " + from + " till " + to);
			edges = new ArrayList<Edge<Node>>(lg.getEdgesBetween(from, to));
			textArea.setEditable(false);
			createEdgeString();
			textArea.append(str);
			pan.add(connectionInfo);
			pan.add(textArea);
			pan.add(scroll);
			add(pan);
		}

		private void createEdgeString(){
			for(Edge<Node> e : edges){
				str += e.toString() + "\n";
			}
		}
	}


	class SelectConnectionPanel extends JPanel{
		ArrayList<Edge<Node>> edges;
		Edge<Node>[] edgesJList;
		JPanel pan = new JPanel();
		JLabel connectionInfo;
		JList<Edge<Node>> connections;

		SelectConnectionPanel(Node from, Node to, ArrayList<Edge<Node>> edges){
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			connectionInfo = new JLabel("Förbindelse från " + from + " till " + to);
			this.edges = edges; 
			edgesJList = new Edge[edges.size()];
			connections = new JList<Edge<Node>>(edgesJList);
			connections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			edgesArray();			
			pan.add(connectionInfo);
			pan.add(connections);
			add(pan);
		}

		private void edgesArray(){
			for(int i=0; i < edges.size(); i++){
				Edge<Node> e = edges.get(i);
				edgesJList[i] = e;
			}
		}

		public Edge<Node> getSelectedEdge(){
			return connections.getSelectedValue();
		}
	}


	class ChangeConnectionPanel extends JPanel{
		JPanel pan = new JPanel();
		JLabel connectionInfo;
		JLabel connectionName = new JLabel("Namn: ");
		JLabel time = new JLabel("Tid: ");
		JTextField conntf = new JTextField(15);
		JTextField timetf = new JTextField(5);


		ChangeConnectionPanel(Node from, Node to){
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			connectionInfo = new JLabel("Förbindelse från " + from.getName() + " till " + to.getName());

			conntf.setEditable(false);
			pan.add(connectionInfo);
			pan.add(connectionName);
			pan.add(conntf);
			pan.add(time);
			pan.add(timetf);
			add(pan);
		}

		ChangeConnectionPanel(Node from, Node to, Edge<Node> e){
			this(from, to);
			conntf.setText(e.getName());		
		}

		public String getConnectionName(){
			return conntf.getText();
		}

		public String getTime(){
			return timetf.getText();
		}

	}


	class AddConnectionPanel extends JPanel{
		JPanel pan = new JPanel();
		JLabel connectionInfo;
		JLabel connectionName = new JLabel("Namn: ");
		JLabel time = new JLabel("Tid: ");
		JTextField conntf = new JTextField(15);
		JTextField timetf = new JTextField(5);

		AddConnectionPanel(Node from, Node to){
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			connectionInfo = new JLabel("Förbindelse från " + from.getName() + " till " + to.getName());						
			pan.add(connectionInfo);
			pan.add(connectionName);
			pan.add(conntf);
			pan.add(time);
			pan.add(timetf);
			add(pan);
		}

		public String getConnectionName(){
			return conntf.getText();
		}

		public String getTime(){
			return timetf.getText();
		}
	}


	class FindConnectionPanel extends JPanel{
		JPanel pan = new JPanel();
		JTextArea connections = new JTextArea(10, 30);
		String connStr = "";
		int totalDistance;
		ArrayList<Edge<Node>> edges;

		FindConnectionPanel(ArrayList<Edge<Node>> shortestPathEdges){
			this.edges = shortestPathEdges;
			connectionsString();
			calcTotalDistance();
			pan.add(connections);
			connections.append(connStr + "\n" + "Totalt: " + totalDistance);
			connections.setEditable(false);
			add(pan);
		}

		private void connectionsString(){
			for(Edge<Node> e : edges){
				connStr += e + "\n";
			}
		}
		
		private void calcTotalDistance(){
			for(Edge<Node> e : edges){
				totalDistance += e.getWeight();
			}
		}
	}

	
	class NodeMouseList extends MouseAdapter {
		public void mouseClicked(MouseEvent mev){
			Node n = (Node)mev.getSource();
			if(activeNodeOne == null){
				activeNodeOne = n;
				n.setActiveNode(true);
			} else if(activeNodeOne != null && n == activeNodeOne){
				n.setActiveNode(false);
				activeNodeOne = null;
			} else if(activeNodeTwo == null && n != activeNodeOne){
				activeNodeTwo = n;
				n.setActiveNode(true);
			} else if(activeNodeTwo != null && n == activeNodeTwo){
				n.setActiveNode(false);
				activeNodeTwo = null;
			}

		}
	}

	
	class ExitListener extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent we){
			int reply = 0;
			if(changes){
				reply = JOptionPane.showConfirmDialog(SupremePaths.this, "Avsluta utan att spara ändringar?");
			if(reply != JOptionPane.YES_NO_OPTION)
				return;
			else
				System.exit(0);		
			}else{
				System.exit(0);
			}
		}
	}
	
	
	private void prepareForNewMap(){
		bg.setVisible(false);
		bg = null;
		activeNodeOne = null;
		activeNodeTwo = null;
		changes = false;
		lg = new ListGraph<Node>();
		locationNames.clear();
	}

	
	public void removeMouseListeners(){
		for(int i=0; i < bg.getComponentCount();i++){
			if(bg.getComponent(i) instanceof Node){
				Node n = (Node)bg.getComponent(i);
				n.removeMouseListener(nml);}
		}
	}
	
	
	public void addMouseListeners(){
		nml = new NodeMouseList();
		for(int i=0; i < bg.getComponentCount();i++){
			if(bg.getComponent(i) instanceof Node){
				Node node = (Node)bg.getComponent(i);
				node.addMouseListener(nml);}
		}
	}
	
	
	public static void main(String[] args) {		
		new SupremePaths();
	}	

}
