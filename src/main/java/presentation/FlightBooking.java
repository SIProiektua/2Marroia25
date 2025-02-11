package presentation;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.*;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import businessLogic.FlightManager;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

import domain.ConcreteFlight;
import domain.Flight;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import dataBase.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//GALDETU ZE BERTSIO ERABILI BEHAR DUGUN METODOA EGIN AHAL IZATEKO

public class FlightBooking extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = null;
	private JLabel lblDepartCity = new JLabel("Departing city:");
	private JLabel lblArrivalCity = new JLabel("Arrival City");
	private JLabel lblYear = new JLabel("Year:");
	private JLabel lblRoomType = new JLabel("Room Type:");
	private JLabel lblMonth = new JLabel("Month:");
	private JLabel lblDay = new JLabel("Day:");;
	private JLabel jLabelResult = new JLabel();
	private JLabel searchResult = new JLabel();
	private JTextField day = null;
	private JComboBox<String> months = null;
	private DefaultComboBoxModel<String> monthNames = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> depart = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> arrive = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<ConcreteFlight> flightInfo_c = new DefaultComboBoxModel<ConcreteFlight>();
	private DefaultComboBoxModel<String> nextdate = new DefaultComboBoxModel<String>();
	private JTextField year = null;
	private ConcreteFlight cf;
	private Collection<ConcreteFlight> savercollect;
	private JRadioButton bussinesTicket = null;
	private JRadioButton firstTicket = null;
	private JRadioButton touristTicket = null;
	private ButtonGroup fareButtonGroup = new ButtonGroup();
	private ConcreteFlight datuBasekoCF;
	private JButton lookforFlights = null;
	private DefaultListModel<ConcreteFlight> flightInfo = new DefaultListModel<ConcreteFlight>();
	private JList<ConcreteFlight> flightList = null;
	private JComboBox<ConcreteFlight> flightList_1;
	private JButton bookFlight = null;
	private boolean aldatu = false;
	private Collection<ConcreteFlight> concreteFlightCollection;

	private FlightManager businessLogic; // @jve:decl-index=0:
	private JScrollPane flightListScrollPane = new JScrollPane();

	private ConcreteFlight selectedConcreteFlight;
	private JComboBox dCities;
	private JComboBox aCities;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightBooking frame = new FlightBooking();
					// frame.setBusinessLogic(new FlightManager());
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Custom operations
	public void setBusinessLogic(FlightManager g) {
		businessLogic = g;
	}

	private Date newDate(int year, int month, int day) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Create the frame
	 * 
	 * @return void
	 */
	private FlightBooking() {
		datuBasea db = new datuBasea();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				datuBasea.clearDatabase();
				db.close();
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				db.startDataBase();
			}
		});
		businessLogic = new FlightManager();
		setTitle("Book Flight");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 353);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblDepartCity = new JLabel("Depart City");
		lblDepartCity.setBounds(21, 11, 84, 16);
		contentPane.add(lblDepartCity);

		lblYear = new JLabel("Year:");
		lblYear.setBounds(21, 62, 33, 16);
		contentPane.add(lblYear);

		lblMonth = new JLabel("Month:");
		lblMonth.setBounds(117, 62, 50, 16);
		contentPane.add(lblMonth);

		months = new JComboBox<String>();
		months.setBounds(163, 58, 116, 27);
		contentPane.add(months);
		months.setModel(monthNames);
		
		monthNames.addElement("January");
		monthNames.addElement("February");
		monthNames.addElement("March");
		monthNames.addElement("April");
		monthNames.addElement("May");
		monthNames.addElement("June");
		monthNames.addElement("July");
		monthNames.addElement("August");
		monthNames.addElement("September");
		monthNames.addElement("October");
		monthNames.addElement("November");
		monthNames.addElement("December");
		months.setSelectedIndex(1);

		lblDay = new JLabel("Day:");
		lblDay.setBounds(291, 62, 38, 16);
		contentPane.add(lblDay);

		
		day = new JTextField();
		day.setText("23");
		day.setBounds(331, 57, 50, 26);
		contentPane.add(day);
		day.setColumns(10);

		lblRoomType = new JLabel("Seat Type:");
		lblRoomType.setBounds(21, 242, 84, 16);
		contentPane.add(lblRoomType);

		bussinesTicket = new JRadioButton("Business");
		bussinesTicket.setSelected(false);
		fareButtonGroup.add(bussinesTicket);
		bussinesTicket.setBounds(99, 238, 101, 23);
		contentPane.add(bussinesTicket);

		firstTicket = new JRadioButton("First");
		fareButtonGroup.add(firstTicket);
		firstTicket.setBounds(202, 238, 77, 23);
		contentPane.add(firstTicket);

		touristTicket = new JRadioButton("Tourist");
		fareButtonGroup.add(touristTicket);
		touristTicket.setBounds(278, 238, 77, 23);
		contentPane.add(touristTicket);
		//HEMEN ALDAKETAK: db Ireki eta hegaldien bilaketa
		lookforFlights = new JButton("Look for Concrete Flights");
		lookforFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookFlight.setEnabled(true);
				flightInfo.clear();
				flightInfo_c.removeAllElements();
				bookFlight.setText("");
				int yearI = 2025;
				int dayI = 23;
				try {
					yearI = Integer.parseInt(year.getText());
				} catch (NumberFormatException e1) {
					year.setText("2025");
				}
				try {
					dayI = Integer.parseInt(day.getText());
				} catch (NumberFormatException e2) {
					day.setText("01");
				}

				java.util.Date date = newDate(yearI, months.getSelectedIndex(), dayI);
				String z = (String) dCities.getSelectedItem();
				String a = (String) aCities.getSelectedItem();
				if (z.equals(a)) {
					return;
				} else {
					concreteFlightCollection = businessLogic.getConcreteFlights(((String) dCities.getSelectedItem()),
							((String) aCities.getSelectedItem()), date);
				}
				System.out.println("Tamaina: " + concreteFlightCollection.size());
				Iterator<ConcreteFlight> flights = concreteFlightCollection.iterator();
				int i = 0;
				while (flights.hasNext()) {
					cf = flights.next();
					try {
						 datuBasekoCF = db.findConcreteFlight(cf);
						if (datuBasekoCF != null) {
							flightInfo_c.addElement(datuBasekoCF);
						}else {
							db.gordeHegaldia(cf);
							flightInfo_c.addElement(cf);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (concreteFlightCollection.isEmpty())
					searchResult.setText("No flights in that city in that date");
				else
					searchResult.setText("Choose an available flight in this list:");
			}
		});
		lookforFlights.setBounds(81, 90, 261, 40);
		contentPane.add(lookforFlights);

		jLabelResult = new JLabel("");
		jLabelResult.setBounds(109, 180, 243, 16);
		contentPane.add(jLabelResult);

		flightList_1 = new JComboBox();
		flightList_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if (flightList_1.getSelectedItem() != null) {
						selectedConcreteFlight = (ConcreteFlight) flightList_1.getSelectedItem();
						bookFlight.setText("Book: " + selectedConcreteFlight.toString());

						if (selectedConcreteFlight != null) {
							if (selectedConcreteFlight.getBussinesNumber() <= 0) {
								bussinesTicket.setEnabled(false);
							} else if (selectedConcreteFlight.getBussinesNumber() > 0) {
								bussinesTicket.setEnabled(true);
							}
							if (selectedConcreteFlight.getFirstNumber() <= 0) {
								firstTicket.setEnabled(false);
							} else if (selectedConcreteFlight.getFirstNumber() > 0) {
								firstTicket.setEnabled(true);
							}
							if (selectedConcreteFlight.getTouristNumber() <= 0) {
								touristTicket.setEnabled(false);
							} else if (selectedConcreteFlight.getTouristNumber() > 0) {
								touristTicket.setEnabled(true);
							}
						}
					}
			}
		});

		flightList_1.setModel(flightInfo_c);
		flightListScrollPane.setBounds(new Rectangle(64, 159, 336, 71));
		flightListScrollPane.setViewportView(flightList_1);
		contentPane.add(flightListScrollPane);
		//HEMEN ALDAKETAK Db-an get egin hegaldia berritzeko
		bookFlight = new JButton("");
		bookFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = false;
				int num = 0;
				ConcreteFlight cff;
				boolean error = false;
				if (bussinesTicket.isSelected()) {
					num = selectedConcreteFlight.getBussinesNumber();
					if (num > 0) {
						aldatu = true;
						selectedConcreteFlight.setBusinessNumber(num - 1);
						cff = db.changeConcreteFlight(selectedConcreteFlight, "business", num - 1);
					} else
						error = true;
				}
				if (firstTicket.isSelected()) {
					num = selectedConcreteFlight.getFirstNumber();
					if (num > 0) {
						aldatu = true;
						selectedConcreteFlight.setFirstNumber(num - 1);
						cff = db.changeConcreteFlight(selectedConcreteFlight, "first", num - 1);
					} else
						error = true;
				}
				if (touristTicket.isSelected()) {
					num = selectedConcreteFlight.getTouristNumber();
					if (num > 0) {
						aldatu = true;
						selectedConcreteFlight.setTouristNumber(num - 1);
						cff = db.changeConcreteFlight(selectedConcreteFlight, "tourist", num - 1);
					} else
						error = true;
				}
				if (error) {
					bookFlight.setText("Error: There were no seats available!");
				} else {

					bookFlight.setText("Booked. #seat left: " + (num - 1));
				}
			}
		});
		bookFlight.setBounds(31, 273, 399, 40);
		contentPane.add(bookFlight);

		year = new JTextField();
		year.setText("2025");
		year.setBounds(57, 57, 50, 26);
		contentPane.add(year);
		year.setColumns(10);

		lblArrivalCity.setBounds(21, 39, 84, 16);
		contentPane.add(lblArrivalCity);

		searchResult.setBounds(57, 130, 314, 16);
		contentPane.add(searchResult);

		Iterator<String> it = businessLogic.getAllDepartingCities().iterator();

		while (it.hasNext()) {
			String a3 = it.next();
			if (depart.getIndexOf(a3) == -1) {
				depart.addElement(a3);
			}
		}

		dCities = new JComboBox();

		dCities.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == e.SELECTED) {
					String s = (String) dCities.getSelectedItem();
					arrive.removeAllElements();
					Iterator<String> it2 = businessLogic.getArrivalCitiesFrom(s).iterator();
					while (it2.hasNext()) {
						String a2 = it2.next();
						if (arrive.getIndexOf(a2) < 0) {
							arrive.addElement(a2);
						}
					}
				}
			}
		});

		dCities.setBounds(95, 10, 247, 18);
		contentPane.add(dCities);
		dCities.setModel(depart);
		dCities.setSelectedIndex(1);

		aCities = new JComboBox();
		aCities.setBounds(95, 31, 247, 21);
		arrive.addListDataListener(flightList_1);
		contentPane.add(aCities);
		aCities.setModel(arrive);
		aCities.setSelectedIndex(1);
		
	}
}
