package dataBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;

import domain.ConcreteFlight;
import domain.Flight;
import businessLogic.FlightManager;

public class datuBasea {
    private EntityManager db;
    private EntityManagerFactory emf;
    private static String fileName = "erreserba_data.odb";

    public datuBasea() {
        emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
        db = emf.createEntityManager();
        System.out.println("DatuBasea irekia");
    }

    /**
     * Initializes the database structure.
     */
    public void sortu() {
        try {
            db.getTransaction().begin();
            // Perform any necessary initialization here (e.g., create tables if needed)
            db.getTransaction().commit();
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public void garbitu() {
    	db.getTransaction().begin();
        db.clear();
        db.getTransaction().commit();
    }

    /**
     * Stores a ConcreteFlight object in the database.
     *
     * @param cf1 The ConcreteFlight object to store.
     */
    public void gordeHegaldia(ConcreteFlight cf1) {
        try {
            db.getTransaction().begin();
            db.persist(cf1);
            db.getTransaction().commit();
            System.out.println("Stored ConcreteFlight: " + cf1.toString());
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            System.err.println("Error storing ConcreteFlight: " + e.getMessage());
        }
    }

    /**
     * Updates or replaces a ConcreteFlight object in the database.
     *
     * @param f1 The old ConcreteFlight object to remove.
     * @param t2 The new ConcreteFlight object to persist.
     */
    public void eraberritu(ConcreteFlight f1, ConcreteFlight t2) {
        try {
            db.getTransaction().begin();
            if (f1 != null) {
                db.remove(f1);
            }
            db.persist(t2);
            db.getTransaction().commit();
            System.out.println("Updated ConcreteFlight: " + t2.toString());
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            System.err.println("Error updating ConcreteFlight: " + e.getMessage());
        }
    }


    /**
     * Checks if the database contains any information.
     *
     * @return True if the database is empty, false otherwise.
     */
    public boolean hasinfo() {
        try {
            TypedQuery<ConcreteFlight> query = db.createQuery("SELECT p FROM ConcreteFlight p", ConcreteFlight.class);
            List<ConcreteFlight> results = query.getResultList();
            return results.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking database content: " + e.getMessage());
            return true; // Assume empty if an error occurs
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
        if (emf.isOpen()) {
        	
            emf.close();
        }
        System.out.println("DatuBasea Itxi da");
    }
    
    public static void clearDatabase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(fileName);
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Object o").executeUpdate();
        em.getTransaction().commit();
        
        em.close();
        emf.close();
    }

    



    /**
     * Makes the old values as the main for the new user
     */
    public void refresh(Collection<ConcreteFlight> savercollect) {
        EntityTransaction transaction = db.getTransaction();
        try {
            transaction.begin();
            
            
            db.createQuery("DELETE FROM ConcreteFlight").executeUpdate();
            
            
            for (ConcreteFlight flight : savercollect) {
                db.persist(flight);
            }
            
            transaction.commit();
            System.out.println("La base de datos se ha reiniciado con los nuevos vuelos.");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al reiniciar la base de datos: " + e.getMessage());
        }
    }

    

    /**
     * Loads all ConcreteFlight objects into the database from FlightManager.
     */
    public void startDataBase() {
        try {
            for (Flight flight : FlightManager.getAllFlights()) {
                for (ConcreteFlight cflight : flight.getConcreteFlights()) {
                    if (!containsConcreteFlight(cflight)) {
                        gordeHegaldia(cflight);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading flights into the database: " + e.getMessage());
        }
    }

    /**
     * Checks if a ConcreteFlight object exists in the database.
     *
     * @param cf The ConcreteFlight object to check.
     * @return True if the object exists, false otherwise.
     */
    private boolean containsConcreteFlight(ConcreteFlight cf) {
        try {
            TypedQuery<ConcreteFlight> query = db.createQuery(
                "SELECT p FROM ConcreteFlight p WHERE p.concreteFlightCode = :concreteFlightCode AND p.date = :date AND p.flight = :flight",
                ConcreteFlight.class
            );
            query.setParameter("concreteFlightCode", cf.getConcreteFlightCode());
            query.setParameter("date", cf.getDate());
            query.setParameter("flight", cf.getFlight());
            List<ConcreteFlight> results = query.getResultList();
            return !results.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking for ConcreteFlight: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a ConcreteFlight object in the database.
     *
     * @param a The ConcreteFlight object to search for.
     * @return The found ConcreteFlight object, or null if not found.
     */
    public ConcreteFlight findConcreteFlight(ConcreteFlight a) {
        try {
        	System.out.println("a" + a.concreteFlightCode);
        	ConcreteFlight c = db.find(ConcreteFlight.class, a.concreteFlightCode);
        	if (c!=null) {
                System.out.println("Found ConcreteFlight: " + c);
                return c;
            } else {
                System.out.println("ConcreteFlight not found.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error finding ConcreteFlight: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates the seat count for a ConcreteFlight object.
     *
     * @param a        The ConcreteFlight object to update.
     * @param seatType The type of seat to update ("business", "first", "tourist").
     * @param value    The new seat count.
     * @return The updated ConcreteFlight object, or null if not found.
     */
    public ConcreteFlight changeConcreteFlight(ConcreteFlight a, String seatType, int value) {
        try {
            ConcreteFlight cf = findConcreteFlight(a);
            if (cf == null) {
                System.out.println("ConcreteFlight not found for update.");
                return null;
            }

            db.getTransaction().begin();
            if ("business".equalsIgnoreCase(seatType)) {
                cf.setBusinessNumber(value);
            } else if ("first".equalsIgnoreCase(seatType)) {
                cf.setFirstNumber(value);
            } else if ("tourist".equalsIgnoreCase(seatType)) {
                cf.setTouristNumber(value);
            } else {
                System.out.println("Invalid seat type: " + seatType);
                db.getTransaction().rollback();
                return null;
            }
            db.merge(cf);
            db.getTransaction().commit();

            System.out.println("Updated ConcreteFlight: " + cf.toString());
            return cf;
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            System.err.println("Error updating ConcreteFlight: " + e.getMessage());
            return null;
        }
    }
}
