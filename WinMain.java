package arduinoEj1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import com.panamahitek.PanamaHitek_Arduino;

//import arduinoswitch.Window;
//import java.util.Timer;

public class WinMain extends JFrame {

	private static final long serialVersionUID = 1L;
	PanamaHitek_Arduino Arduino;
	private JComboBox<String> jCBoxPorts;
	private JButton jbtnConectar, jbtnRefresh, jbtnEncender, jbtnApagar;
	private JPanel jpnlNorte, jpnlCentro, jpnlSur;
	private JLabel jlblTiempo;
	private int contador;
	private Timer t;

	public WinMain() {
		super("Controlador Arduino");
		Arduino = new PanamaHitek_Arduino();
		setSize(new Dimension(400, 200));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		InitComponents();
		getPorts();
		t = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				++contador;
				jlblTiempo.setText("" + (contador));
			}

		});
	}

	private void InitComponents() {
		// Panel Norte
		jpnlNorte = new JPanel();
		jpnlNorte.setBorder(javax.swing.BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		// jpnlNorte.setLayout(new BoxLayout(jpnlNorte, BoxLayout.X_AXIS));
		jpnlNorte.setLayout(new GridLayout(1, 3, 10, 3));
		jCBoxPorts = new JComboBox<String>();
		jCBoxPorts.setMinimumSize(new Dimension(150, 25));
		jCBoxPorts.setPreferredSize(new Dimension(200, 25));
		jCBoxPorts.setMaximumSize(new Dimension(300, 25));
		jpnlNorte.add(jCBoxPorts);
		jbtnConectar = new JButton("Conectar");
		jbtnConectar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnConectarActionPerformed(evt);
			}
		});
		jpnlNorte.add(jbtnConectar);
		jbtnRefresh = new JButton("Buscar puertos");
		jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getPorts();
			}
		});
		jpnlNorte.add(jbtnRefresh);
		add(jpnlNorte, BorderLayout.NORTH);

		// Panel Central
		jpnlCentro = new JPanel();
		jpnlCentro.setLayout(new BoxLayout(jpnlCentro, BoxLayout.Y_AXIS));
		jbtnEncender = new JButton("Encender");
		jbtnEncender.setEnabled(false);
		jbtnEncender.setMinimumSize(new Dimension(120, 25));
		jbtnEncender.setPreferredSize(new Dimension(120, 25));
		jbtnEncender.setMaximumSize(new Dimension(120, 25));
		jbtnEncender.setAlignmentX(CENTER_ALIGNMENT);
		jbtnEncender.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnEncenderActionPerformed(evt);
			}
		});
		jpnlCentro.add(jbtnEncender);
		jbtnApagar = new JButton("Apagar");
		jbtnApagar.setEnabled(false);
		jbtnApagar.setMinimumSize(new Dimension(120, 25));
		jbtnApagar.setPreferredSize(new Dimension(120, 25));
		jbtnApagar.setMaximumSize(new Dimension(120, 25));
		jbtnApagar.setAlignmentX(CENTER_ALIGNMENT);
		jbtnApagar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtnApagarActionPerformed(evt);
			}
		});
		jpnlCentro.add(jbtnApagar);
		add(jpnlCentro, BorderLayout.CENTER);
		// Panel Sur
		jpnlSur = new JPanel();
		contador = 0;
		jlblTiempo = new JLabel("" + contador);
		jpnlSur.add(jlblTiempo);
		add(jpnlSur, BorderLayout.SOUTH);
	}

	private void jbtnConectarActionPerformed(java.awt.event.ActionEvent evt) {

		if (jbtnConectar.getText().equals("Desconectar")) {
			try {
				t.stop();
				Arduino.sendData("0");
				Arduino.killArduinoConnection();
				jbtnConectar.setText("Conectar");
				jbtnApagar.setEnabled(false);
				jbtnEncender.setEnabled(false);
				jbtnRefresh.setEnabled(true);
				jCBoxPorts.setEnabled(true);
			} catch (Exception ex) {
				System.out.println("Error al desconectar: " + ex.getMessage());
				// Logger.getLogger(Window.class.getName()).log(Level.SEVERE,
				// null, ex);
			}
		} else {
			try {
				Arduino.arduinoTX(jCBoxPorts.getSelectedItem().toString(), 9600);
				t.start();
				jbtnConectar.setText("Desconectar");
				jbtnApagar.setEnabled(false);
				jbtnEncender.setEnabled(true);
				jbtnRefresh.setEnabled(false);
				jCBoxPorts.setEnabled(false);
			} catch (Exception ex) {
				System.out.println("Error al Conectar: " + ex.getMessage());
				// Logger.getLogger(Window.class.getName()).log(Level.SEVERE,
				// null, ex);
			}
		}
	}

	private void jbtnEncenderActionPerformed(java.awt.event.ActionEvent evt) {

		// Se modifica la interface gráfica del botón precionado
		jbtnApagar.setEnabled(true);
		jbtnEncender.setEnabled(false);
		// Se envían un -1- para encender el LED
		try {
			Arduino.sendData("1");
		} catch (Exception ex) {
			System.out.println("Error al encender: " + ex.getMessage());
			// Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null,
			// ex);
		}
	}

	private void jbtnApagarActionPerformed(java.awt.event.ActionEvent evt) {

		// Se modifica la interface gráfica del botón precionado
		jbtnApagar.setEnabled(false);
		jbtnEncender.setEnabled(true);
		// Se envía un -0- para apagar el LED
		try {
			Arduino.sendData("0");
		} catch (Exception ex) {
			System.out.println("Error al apagar: " + ex.getMessage());
			// Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null,
			// ex);
		}
	}

	public void getPorts() {
		jCBoxPorts.removeAllItems();
		if (Arduino.getPortsAvailable() > 0) {
			Arduino.getSerialPorts().forEach(i -> jCBoxPorts.addItem(i));
			jbtnConectar.setEnabled(true);
			jbtnConectar.setBackground(new Color(255, 255, 255));
		} else {
			jbtnConectar.setEnabled(false);
			jbtnConectar.setBackground(new Color(204, 204, 204));
		}
	}
}
