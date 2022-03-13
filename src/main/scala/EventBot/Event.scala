package EventBot
import scala.collection.mutable

/**
  * Class for creating new events
  *
  * @param identifier Unique identifier for the event
  * @param eventName Event name
  * @param seats How many seats available for the particular event
  */

class Event(private val identifier: String, private val eventName: String, val seats: Int) {

  private var seatsTaken: Int = 0                                                    // Seats taken by people
  private var personsRegistered: mutable.Buffer[String] = mutable.Buffer[String]()   // Buffer of peoples names who have registered for the event
  private var lineForTheEvent: mutable.Buffer[String] = mutable.Buffer[String]()     // If the event is full, put people on the waiting line

  /**
    * Check if the person has registered for the event.
    *
    * @param person Unique name of the person
    * @return Boolean, true if the person is registered in the event, false otherwise
    */
  def registered(person: String): Boolean = this.personsRegistered.contains(person) || this.lineForTheEvent.contains(person)

  /**
    * Add person to the event. If event is full, person is assigned to the waiting line
    *
    * @param person Unique name of the person
    */
  def addPerson(person: String): Unit = {
    if(personsRegistered.length < this.seats) {
      this.personsRegistered.append(person)
      this.seatsTaken += 1
    } else {
      this.lineForTheEvent.append(person)
    }
  }

  /**
    * Removes person from the event and moves one person from the line to the event.
    *
    * @param person Unique name of the person
    * @return
    */
  def removePerson(person: String): Unit = {
    this.personsRegistered.remove(this.personsRegistered.indexOf(person))
    this.seatsTaken -= 1

    if(this.lineForTheEvent.nonEmpty) {
      this.personsRegistered.append(lineForTheEvent.head)
      println(s"${lineForTheEvent.head} has been added to the event participant list!")
      this.seatsTaken += 1
      this.lineForTheEvent.remove(0)
    }
  }

  /**
    * Amount of seats taken by people
    *
    * @return Amount of seats taken in the event
    */
  def registeredSeats: Int = this.seatsTaken

  /**
    * Check and return the amount of seats open for the event
    *
    * @return Amount of seats open for the event
    */
  def seatsLeft: Int = this.seats - this.personsRegistered.length

  /**
    * Return the identifier for the particular event
    *
    * @return Identifier
    */
  def eventIdentifier: String = this.identifier

  /**
    * Returns the event name
    *
    * @return Event name
    */
  def name: String = this.eventName

}
