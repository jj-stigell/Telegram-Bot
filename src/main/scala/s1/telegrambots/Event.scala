package s1.telegrambots

import scala.collection.mutable

class Event(private val tapahtumatunnus: String, private val tapahtumanNimi: String, val paikat: Int) {

  private var ilmoittautuneideMaara = 0
  private var tapahtumaanIlmoittautuneet = mutable.Buffer[String]()
  private var ilmoittautuneidenJono = mutable.Buffer[String]()

  // Palauttaa tosi jos henkilön nimi löytyy listalta
  def rekisteroitunut(henkilo: String) = this.tapahtumaanIlmoittautuneet.contains(henkilo) || this.ilmottautuneidenJono.contains(henkilo)

  // Lisää henkilön tapahtumaan ilmoittautuneiden joukkoon
  def lisaaHenkilo(henkilo: String) = {
    if(tapahtumaanIlmoittautuneet.length < this.paikat) {
      this.tapahtumaanIlmoittautuneet.append(henkilo)
      this.ilmoittautuneideMaara += 1
    } else {
      this.ilmoittautuneidenJono.append(henkilo)
    }
  }


  // Poistaa henkilön tapahtumaan ilmoittautuneiden joukosta
  def poistaHenkilo(henkilo: String) = {
    this.tapahtumaanIlmoittautuneet.remove(this.tapahtumaanIlmoittautuneet.indexOf(henkilo))
    this.ilmoittautuneideMaara -= 1
    if(!(ilmottautuneidenJono.isEmpty)) {
    this.tapahtumaanIlmottautuneet.append(ilmottautuneidenJono(0))
    this.ilmottautuneidenMaara += 1
    this.ilmottautuneidenJono.remove(0)
    }
    }


  // Palauttaa ilmoittautuneiden määrän
  def ilmoittautuneet = this.ilmoittautuneideMaara

  // Palauttaa vapaina olevien paikkojen määrän
  def paikkojaVapaana = {
   // this.paikat - this.ilmoittautuneideMaara
    this.paikat - this.tapahtumaanIlmoittautuneet.length
  }

  // Palauttaa tapahtuman tunnuksen
  def tunnus = this.tapahtumatunnus

  // Palauttaa tapahtuman nimen
  def nimi = this.tapahtumanNimi

}