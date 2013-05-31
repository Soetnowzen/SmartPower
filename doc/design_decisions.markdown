Design decisions
================

## Initial design

Our initial design was done with fluidui and can be found [here](https://www.fluidui.com/editor/live/preview/p_qDdXxKS8pfpCBSiu2IerV8nc8RJLmxso.1364381332109). It a prototype with limited functionality, but it's easy to see that it's a long way away from what we now have done. We noticed many flaws with the prototype during the development - many overlapping activities led to forcing users to hit the back button many times. The current interface we design enables much more functionality with less button presses. It also looks awesome, which is a huge plus.


## Design elements

### ViewPager

We decied to base our layout around sliding screens becuse it's used by a lot of other projects and looks nice. This was accomplished with a ViewPager in conjunction with FragmentPagerAdapter.

## External dependencies motivations
In this project we have experimented heavily with external libraries. 

### ActionBarSerlock

This library is used because we want to have an action bar for all supported targets, not only for post 3.0. This is mainly to keep the look-and-feel of the app consistent, because as of current there's not much functionality in the action bar itself.

### ViewPagerIndicator

This library gives us the possibility of adding a title to each of the sliding fragments. This gives a clear indication to the user that there's more fragments that can be accessed. Without this, users would definitively be confused and may not even find the other fragments at all before uninstalling the app, so it's definitively important.

### Robotium

For acceptance testing, we chose to use Robotium since it's easy to test the GUI using this library, which would be hard to automate otherwise. It was also recommended by our supervisor.


