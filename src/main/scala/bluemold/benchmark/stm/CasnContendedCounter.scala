package bluemold.benchmark.stm

import bluemold.concurrent.casn.{CasnVar,CasnSequence}

object CasnContendedCounter {
  val maxCounter = 10000000L
  val counter = new CasnVar( 0L )
  val counter2 = new CasnVar( 0L )
  def main( args: Array[String] ) {
    val threadA = new Thread( new Counting )
    val threadB = new Thread( new Counting )
    val start = System.currentTimeMillis()
    threadA.start()
    threadB.start()
    threadA.join()
    threadB.join()
    val end = System.currentTimeMillis()
    val duration = end - start
    System.out.println( "Duration: " + duration + "ms" )
    System.out.println( "Transactions/Second: " + ( maxCounter * 1000 / duration ) )
  }
  class Counting extends Runnable {
//    val counter = new CasnVar( 0L )
    def run() {
      while ( counter.getValue < maxCounter ) {
        CasnSequence
        .update( counter, ( value: Long ) => value + 1 )
//        .update( counter2, ( value: Long ) => value + 1 )
        .execute()
      }
    }
  }
}