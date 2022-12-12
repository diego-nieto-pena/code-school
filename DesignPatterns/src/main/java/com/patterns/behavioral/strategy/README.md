# Strategy

Lets you define a family of algorithms, put each of them into a separate class, and make their objects interchangeable.

## Problem

There's a navigation map, it shows the fastest route by car over the city, then a new version also calculates the fastest 
route by walking, then a new version adds calculation by bike, then routes through all tourist attractions.

The navigation app code became bloated.

While the app is a business success, technically is a mess. Adding new routing algorithms makes the main class of the navigator
doubled in size. Any change, whether is a simple bugfix or a slight adjustment affected the whole class, increasing error code chances.

Additionally, developers spend too much time solving merge conflicts, any change represents code modification done by others. 

## Solution

The Strategy pattern suggest that you tke a class that does something specific in a lot of different ways and extract all of these
algorithms into separate classes called strategies.

The original class, called `context`, must have a field for storing a reference to one of the strategies. The context delegates 
the work to a linked strategy object instead of executing it on its own.

The context isn't responsible for selecting the appropriate algorithm for the job. The client passes the desired strategy to the context. 
It works with all the strategies through the same generic interface, which only exposes a single method for triggering 
the algorithm encapsulated with the selected strategy.

This way the context becomes independent of the strategies, so is possible to add or modify existing strategies, 
without modifying the code of the context or other strategies.

![](../../../../../resources/img/strategy/img.png)

In our routing app, each  routing algorithm can be extracted to its own class with a single `buildRoute` method. 
The method accepts an origin and destination and returns a collection of the route's checkpoints.

Each class might build a different route, the main navigation class doesn't really care which 
algorithm is selected since its primary job is to render a set of checkpoints on the map. The class has a method for switching 
the active routing strategy, so its clients can replace the currently selected routing behavior with another one.

You have to get to the airport. You can catch a bus, order a cab, get on your bike. The strategies are picked up depending 
on factors such as time and budget.

![](../../../../../resources/img/strategy/img_1.png)

## Structure

![](../../../../../resources/img/strategy/img_2.png)

1- The `Context` maintains a reference to one of the strategies and communicates with this via the strategy interface.
2- The `Strategy` interface is common to all the concrete strategies. It declares a method the context uses to execute a strategy.
3- `Concrete Strategies` implement different variations of an algorithm the context uses.
4- The context calls the execution method on the linked strategy each time it needs to run the algorithm. The context ignores 
the type of strategy it works with or how the algorithm is executed.
5- The `Client` creates a specific strategy object and passes it to the context. The context exposes a setter which lets 
clients replace the strategy associated with the context at runtime.

## Pseudocode

The `Strategy` interface declares operations common to all supported versions of some algorithm. The context uses this 
interface to call the algorithm defined by the concrete strategies.

```
interface Strategy is
    method execute(a, b)
```

Concrete Strategies implements the algorithm while follows the base strategy interface. The interface makes them 
interchangeable in the context.
```
class ConcreteStrategyAdd implements Strategy is
    method execute(a, b) is
        return a + b

class ConcreteStrategySubtract implements Strategy is
    method execute(a, b) is
        return a - b

class ConcreteStrategyMultiply implements Strategy is
    method execute(a, b) is
        return a * b
```
The context defines the interface of interest to clients.
```
class Context is
    // Usually the context accepts a strategy through the constructor.
    // Strategy reference
    private strategy: Strategy
    
    // Strategy can be switched at runtime
    method setStrategy(Strategy strategy) is
        this.strategy = strategy
        
    // The context delegates some work to the strategy object
    // instead of implementing multiple versions of the algorithm on its own.    
    method executeStrategy(int a, int b) is
        return strategy.execute(a, b)
```

The client code picks a concrete strategy and passes it to the context. The client must be aware of all different strategies
to be able to make the right decision.
```
class ExampleApplication is
    method main() is
        Create context object.

        Read first number.
        Read last number.
        Read the desired action from user input.

        if (action == addition) then
            context.setStrategy(new ConcreteStrategyAdd())

        if (action == subtraction) then
            context.setStrategy(new ConcreteStrategySubtract())

        if (action == multiplication) then
            context.setStrategy(new ConcreteStrategyMultiply())

        result = context.executeStrategy(First number, Second number)

        Print result.
```