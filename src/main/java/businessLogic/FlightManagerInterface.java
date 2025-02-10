package businessLogic;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import domain.ConcreteFlight;

public interface FlightManagerInterface{
	public List<String> getAllDepartingCities();
	public List<String> getArrivalCitiesFrom(String departingCity);
	public Collection<ConcreteFlight> getConcreteFlights(String departingCity, String arrivingCity, Date date);

}