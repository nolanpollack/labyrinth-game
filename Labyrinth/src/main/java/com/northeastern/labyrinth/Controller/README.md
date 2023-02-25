## Logical Interactions Between a Referee and Players

A referee's logical interactions with a player work according to the spec, reiterated (for playing turns) as follows: 

### Playing Turns

        referee                         player (p_1) . . . player (p_n)
          |                                |                 |
          |   takeTurn(state)              |                 | % player receives:
          | -----------------------------> |                 | % - current state            

    action 1:
          |     Optional.empty()           |                 |
          | <============================  |                 | % pass on this turn
          |                                |                 |


    action 2:
          |     Optional<Action> (present) |                 | % an Action that includes
          | <============================  |                 | - a row or column index
          |                                |                 | - a direction
          |                                |                 | - a number of degrees
          |                                |                 | - a place to go to
          |                                |                 |   for the player
      +-- |                                |                 |
      |
      |
     ----------------
    |                |
    |    Validate    |
    |   Through the  | 
    |    Rule Book   |
    |                |
     ----------------
      |   
      |   .                                .                 . % referee modifies game state
      +-> .                                .                 . % otherwise:
          .                                .                 . % kick player out
          .                                .                 .

    if the player reaches the assigned treasures-target with this turn:

          |                                |                 |
          | setup(Optional.empty(), targetTile               | % no state; just a reminder
          | -----------------------------> |                 | % to go home


    if the player reaches home with this turn, the referee terminates the game:

          |                                |                 |

         --- Update Player data to reflect game termination.


    REPEAT FOR N Players

          |   takeTurn(state)              |                 |
          | -----------------------------------------------> |
          |     response                   |                 |
          | <=============================================== |
          |                                |                 |
          .                                .                 .

      +-- |                                |                 |
      |
     ----------------
    |                |
    |    Validate    |
    |   Through the  | 
    |    Rule Book   |
    |                |
     ----------------
      |
     ----------------
    |                |
    |    Check End   |
    |   Through the  |  
    |    Rule Book   |
    |                |
     ----------------
      |   .                                                
      |   .
      +-> .
            All remaining players reply with Empty Optional in one round
                                    or 
            One player has returned to its home after visiting its target



    

    

    

    

    

          |   takeTurn(state)              |                 |

          | -----------------------------------------------> |

          |     response                   |                 |

          | <=============================================== |

          |                                |                 |

          .                                .                 .

          .                                .                 . % repeat until all remaining players

          .                                .                 . % reply with a pass (empty optional) in one round

          .                                .                 . % or one player has returned to

          .                                .                 . % its home after visiting its target

          .                                .                 .