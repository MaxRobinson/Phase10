Phase10
=======

Android Phase 10 Game for CS301 University of Portland 

Github repo can be found at https://github.com/MaxRobinson/Phase10

Additions since first release

Removed the hit button from the gui and added a dummy method in smart AI for
changes that will be made for smart hitting.

Fixed bugs with network play. Now have a network player working. There were
a few bugs that occured after network play started working. There was a crash
on playing a skip card, and a bug with hitting on players phases that caused
both of the player's phases to be the same. This issue would also have
occured on the local game only but we could never get a computer to phase.
These are both fixed now. The only issues left for network play I believe are
that the laid Phases aren't being updated correctly... If a phase is hit on
the remote player does not see this. I have no idea why. I will look into
this.

new smart discarding based on value of cards
improved skipping now also bases choice on points

Theoretically, the Smart AI is now able to hit on a phase after it has phased
. I am unsure as to how well it works but, it hasn't crashed yet.. though it
hasn't laid a phase yet either.