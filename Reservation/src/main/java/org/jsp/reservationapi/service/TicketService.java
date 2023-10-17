package org.jsp.reservationapi.service;

import java.util.Optional;

import org.jsp.reservationapi.dao.BusDao;
import org.jsp.reservationapi.dao.TicketDao;
import org.jsp.reservationapi.dao.UserDao;
import org.jsp.reservationapi.dto.Bus;
import org.jsp.reservationapi.dto.ResponseStructure;
import org.jsp.reservationapi.dto.Ticket;
import org.jsp.reservationapi.dto.User;
import org.jsp.reservationapi.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
	
	@Autowired
	public TicketDao tdao;
	
	@Autowired
	public BusDao bdao;
	
	@Autowired
	public UserDao udao;

	public ResponseEntity<ResponseStructure<Ticket>> saveTicket(Ticket ticket,int bus_id,int user_id) {
		ResponseStructure<Ticket> str=new ResponseStructure<>();
		Optional<Bus> busdata=bdao.findById(bus_id);
		Optional<User> userdata=udao.findById(user_id);
		if(busdata.isPresent() && userdata.isPresent())
		{
			busdata.get().getTickets().add(ticket);
			userdata.get().getTickets().add(ticket);
			ticket.setBus(busdata.get());
			ticket.setUser(userdata.get() );
			bdao.updateBus(busdata.get());
			udao.updateUser(userdata.get());
		   str.setBody(tdao.saveTicket(ticket));
		   str.setMessage("ticket saved");
		   str.setCode(HttpStatus.CREATED.value());
		   return new ResponseEntity<ResponseStructure<Ticket>>(str,HttpStatus.ACCEPTED);
		}
		throw new IdNotFoundException();
		
	}

	

}
