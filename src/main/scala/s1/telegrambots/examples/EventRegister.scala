package s1.telegrambots.examples

import s1.telegrambots.BasicBot
import s1.telegrambots.Event
import scala.collection.mutable

object EventRegister extends App {

  val bot = new BasicBot() {

    var tapahtumat = mutable.Buffer[Event]()

    def lisaaTapahtuma(msg: Message) = {
      val string = this.getString(msg).split(" ")
      val tunnus = string(0)

      if (tapahtumaLoytyy(tunnus)) {
        s"Tunnuksella ${tunnus} löytyy jo tapahtuma, käytä toista tunnusta!"
      } else {
        val tapahtumanimi = string(1)
        var paikat = string(2).toInt
        val event = new Event(tunnus, tapahtumanimi, paikat)
        tapahtumat += event
        s"Lisäsin tapahtuman nimeltä ${tapahtumanimi} tunnuksella: ${tunnus}, paikat: ${paikat}"
      }
    }


    // Listaa kaikki tapahtumat
    def listaaTapahtumat(msg: Message) = {
      var kaikkiTapahtumat = ""
      for (event <- tapahtumat) {
        kaikkiTapahtumat += s"Tapahtuman tunnus: ${event.tunnus}, nimi: ${event.nimi}, paikkamäärä: ${event.ilmoittautuneet}/${event.paikat}\n"
      }
      kaikkiTapahtumat
    }


    // Tarkistaa löytyykö tapahtuma
    def tapahtumaLoytyy(tunnus: String) = {
      var loytyy = false
      for (event <- tapahtumat) {
        if (event.tunnus == tunnus) {
          loytyy = true
        }
      }
      loytyy
    }


    // Tarkistaa onko henkilö jo ilmoittautunut kyseiseen tapahtumaan
    def ilmoittautunut(tunnus: String, nimi: String) = {
      var ilmoittautunut = false
      for (event <- tapahtumat) {
        if (event.tunnus == tunnus && event.rekisteroitunut(nimi)) {
          ilmoittautunut = true
        }
      }
      ilmoittautunut
    }


    // tarkistaa mahtuuko tapahtumaan ja jos mahtuu lisää henkilön nimen perusteella
    private def onkoTilaa(tunnus: String, nimi: String) = {
      var tilaa = false
      for (event <- tapahtumat) {
        if (event.tunnus == tunnus && event.ilmoittautuneet < event.paikat) {
          event.lisaaHenkilo(nimi)
          tilaa = true
        }
        else if(event.tunnus == tunnus) {
          event.lisaaHenkilo(nimi)
        }
      }
      tilaa
    }


    // Ilmoittaa henkilön tapahtumaan mikäli sellainen on olemassa ja se ei ole täynnä tai käyttäjä ei ole jo ilmoittautunut
    def ilmoittaudu(msg: Message) = {
      val string = this.getString(msg).split(" ")
      val tunnus = string(0)

      if (!tapahtumaLoytyy(tunnus)) {
        "Tapahtumaa ei löytynyt."
      } else if (ilmoittautunut(tunnus, this.getUserFirstName(msg))) {
        "Olet jo ilmoittautunut."
      } else if (onkoTilaa(tunnus, this.getUserFirstName(msg))) {
        "Tapahtumassa on tilaa, lisäsin sinut."
      } else {
        "Sori tapahtuma on täynnä. Olet varasijalla."
      }
    }


    // Peruu henkilön osallistumisen tapahtumaan mikäli hän on ilmoittautunut siihen
    def peru(msg: Message) = {
      val string = this.getString(msg).split(" ")
      val tunnus = string(0)

      if ((tapahtumaLoytyy(tunnus) && !ilmoittautunut(tunnus, this.getUserFirstName(msg)))) {
        "Et ole ilmoittautunut tähän tapahtumaan"
      } else if (tapahtumaLoytyy(tunnus)) {
        for (event <- tapahtumat) {
          if (event.tunnus == tunnus) {
            event.poistaHenkilo(this.getUserFirstName(msg))
          }
        }
        "Ilmoittautuminen peruttu."
      } else {
        "Tapahtumaa ei löytynyt."
      }
    }

    def apua(msg: Message) = {
      "Komennot:\n" +
      "Lisää tapahtuma: /add \"tapahtuman tunnus\" \"tapahtuman nimi\"\"max. osallistuja määrä\"\n" +
      "Listaa tapahtumat: /list\n" +
      "Ilmoittaudu tapahtumaan: /register \"tapahtuman tunnus\"\n" +
      "Peru ilmoittautuminen tapahtumaan: /cancel \"tapahtuman tunnus\"\n"
    }

    this.command("add", lisaaTapahtuma)       // Lisää uuden tapahtuman muodossa "/add tunnus tapahtumannimi paikat
    this.command("list", listaaTapahtumat)    // Listaa kaikki tapahtumat chattiin
    this.command("register", ilmoittaudu)     // Ilmoittautuu käyttäjä antaman tunnuksen mukaiseen tapahtumaan "/register tunnus"
    this.command("cancel", peru)              // Peruuttaa ilmoittautumisen kyseiseen tapahtumaan (vain jos ilmoittautunut) "/cancel tunnus"
    this.command("help", apua)                // Kirjoittaa chattiin ohjeet

    // Lopuksi Botti pitää vielä saada käyntiin
    this.run()


    println("Started")
  }
}
