package s1.telegrambots

import s1.telegrambots.BasicBot

object EventRegister extends App {

  val bot = new BasicBot() {

     var tapahtumat = Array[(String, String, Int)]()

  def lisaaTapahtuma(tunnus: String, nimi: String, paikat: Int) {
  tapahtumat += (tunnus, nimi, paikat)
  }
  }

}
