package lila.chess
package model

import Pos._

class BishopTest extends LilaSpec {

  "a bishop" should {

    val bishop = White - Bishop

    def moves(pos: Pos): Valid[Set[Pos]] = Board.empty place bishop at pos flatMap { b ⇒
      b actorAt pos map (_.moves)
    }

    "move in any of 8 positions, 2 and 1 squares away" in {
      moves(E4) must bePoss(F3, G2, H1, D5, C6, B7, A8, D3, C2, B1, F5, G6, H7)
    }

    "move in any of 8 positions, 2 and 1 squares away, even when at the edges" in {
      moves(H7) must bePoss(G8, G6, F5, E4, D3, C2, B1)
    }

    "not move to positions that are occupied by the same colour" in {
      val board = """
k B



N B    P

PPPPPPPP
 NBQKBNR
"""
      board movesFrom C4 must bePoss(board, """
k B   x
     x
x   x
 x x
N B    P
 x x
PPPPPPPP
 NBQKBNR
""")
    }

    "capture opponent pieces" in {
      val board = """
k B
     q
p

N B    P

PPPPPPPP
 NBQKBNR
"""
      board movesFrom C4 must bePoss(board, """
k B
     x
x   x
 x x
N B    P
 x x
PPPPPPPP
 NBQKBNR
""")
    }
    "threaten" in {
      val board = """
k B
  q  q
p

N B    P

PPPPPPPP
 NBQKBNR
"""
      "a reachable enemy" in {
        board actorAt C4 map (_ threatens A6) must succeedWith(true)
      }
      "an unreachable enemy" in {
        board actorAt C4 map (_ threatens C7) must succeedWith(false)
      }
      "a reachable friend" in {
        board actorAt C4 map (_ threatens A2) must succeedWith(false)
      }
      "nothing up left" in {
        board actorAt C4 map (_ threatens B5) must succeedWith(false)
      }
      "nothing down right" in {
        board actorAt C4 map (_ threatens D3) must succeedWith(false)
      }
    }
  }
}
