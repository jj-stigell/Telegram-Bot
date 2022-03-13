package EventBot
import scala.collection.mutable

/**
  * EventRegister is a telegram bot for managing events to which people can sign up for.
  * Like afternoon jogging meeting or evening bar-hopping
  */
object EventRegister extends App {

  // Initialize new BasicBot
  val bot = new BasicBot() {

    // All the events are stored in a mutable buffer
    var allEvents: mutable.Buffer[Event] = mutable.Buffer[Event]()

    /**
      * Add new event
      *
      * @param msg message inputted by the user
      * @return String
      */
    def addEvent(msg: Message): String = {
      val inputString = this.getString(msg).split(" ")
      val identifier = inputString(0)

      if (eventExists(identifier)) {
        s"Identifier ${identifier} already has an event assigned to it, use different identifier!"
      } else {
        val eventName = inputString(1)
        var seats = inputString(2).toInt
        val event = new Event(identifier, eventName, seats)
        allEvents += event
        s"Added new event with the name ${eventName}, identifier: ${identifier}, seats: ${seats}"
      }
    }

    /**
      * Form a string from all the events
      *
      * @param msg message inputted by the user
      * @return String with all events
      */
    def listEvents(msg: Message): String =
      this.allEvents.map(event => s"Tapahtuman identifier: ${event.eventIdentifier}, nimi: ${event.name}, paikkamäärä: ${event.registeredSeats}/${event.seats}\n")
        .mkString

    /**
      * Check if the event already exists
      *
      * @param identifier Unique identifier of the event
      * @return True if the event already exists, false if not
      */
    def eventExists(identifier: String): Boolean = this.allEvents.exists(_.eventIdentifier == identifier)

    /**
      * Check if person has signed up for the event
      *
      * @param identifier Unique identifier of the event
      * @param person Unique name of the participant
      * @return True if the person has signed up for the event, false if not
      */
    def registered(identifier: String, person: String): Boolean = this.allEvents.exists(event => event.eventIdentifier == identifier && event.registered(person))

    /**
      * Checks if the event has open seats left
      *
      * @param identifier Unique identifier of the event
      * @return Boolean value if the is available seats for the event, true if open seats, false otherwise
      */
    private def openSeats(identifier: String): Boolean = this.allEvents.exists(event => event.eventIdentifier == identifier && event.registeredSeats < event.seats)

    /**
      * Register person to the event, but only if they are not already registered and there are seats available
      *
      * @param msg message inputted by the user
      * @return String description
      */
    def register(msg: Message): String = {
      val identifier = this.getString(msg).split(" ")(0)

      if (!eventExists(identifier)) {
        "Event not found."
      } else if (registered(identifier, this.getUserFirstName(msg))) {
        "You have already signed up for this event."
      } else if (openSeats(identifier)) {
        val event = this.allEvents.filter(event => event.eventIdentifier == identifier).head
        event.addPerson(this.getUserFirstName(msg))
        "Event has open seats, I added you!"
      } else {
        "Sorry, event is full. I added you to the line."
      }
    }

    /**
      * Cancel persons seats at the event if person has signed up for the event
      *
      * @param msg message inputted by the user
      * @return String
      */
    def cancel(msg: Message): String = {
      val identifier = this.getString(msg).split(" ")(0)

      if ((eventExists(identifier) && !registered(identifier, this.getUserFirstName(msg)))) {
        "You have not registered for this event!"
      } else if (eventExists(identifier)) {
        this.allEvents.foreach(event => if (event.eventIdentifier == identifier) event.removePerson(this.getUserFirstName(msg)))
        "Registration cancelled!"
      } else {
        "Event not found!"
      }
    }

    /**
      * All the commands for using the bot
      *
      * @param msg message inputted by the user
      * @return String of commands
      */
    def help(msg: Message): String = {
      "Commands:\n" +
        "Add new event: /add \"Event identifier\" \"Event name\"\"Maximum participants\"\n" +
        "List all events: /list\n" +
        "Sign up for an event: /register \"Event identifier\"\n" +
        "Cancel event participation: /cancel \"Event identifier\"\n"
    }

    // Different commands for the bot
    this.command("add", addEvent) // Add new event for the event in a form of "/add identifier eventName seats"
    this.command("list", listEvents) // List all the events to the chat
    this.command("register", register) // Register for the event with persons name and event identifier "/register identifier"
    this.command("cancel", cancel) // Cancel participation for the particular event, but only if the person has previously registered for it "/cancel identifier"
    this.command("help", help) // Writes the commands to the chat

    // Start the bot
    this.run()

    println("Started")
  }
}
