package s1.telegrambots

import scala.collection.mutable

class Event(val tunnus: String, val nimi: String, val paikat: Int) {

  private var ilmoittautuneideMaara = 0
  private var tapahtumaanIlmoittautuneet = mutable.Buffer[String]()

  // Palauttaa tosi jos henkilön nimi löytyy listalta
  def rekisteroitunut(henkilo: String) = this.tapahtumaanIlmoittautuneet.contains(henkilo)

  // Lisää henkilön tapahtumaan ilmoittautuneiden joukkoon
  def lisaaHenkilo(henkilo: String) = {
    this.tapahtumaanIlmoittautuneet.append(henkilo)
    this.ilmoittautuneideMaara += 1
  }

  // Poistaa henkilön tapahtumaan ilmoittautuneiden joukosta
  def poistaHenkilo(henkilo: String) = {
    this.tapahtumaanIlmoittautuneet.remove(this.tapahtumaanIlmoittautuneet.indexOf(henkilo))
    this.ilmoittautuneideMaara -= 1
  }

  // Palauttaa ilmoittautuneiden määrän
  def ilmoittautuneet = this.ilmoittautuneideMaara

}