## Ice Cream Party

### Introduction

We're experiencing a lot more traffic than we imagined when we built our humble ice cream service,
so we're making some changes. We're starting by improving the ice cream production.
 
The `IceCreamMaker` now represents a single ice cream making machine.
Each `IceCreamMaker` runs in its own thread, watching the `FlavorRequestQueue`.
When a flavor is added to the queue, the `IceCreamMaker` removes it, creates a carton of that flavor,
and adds it to the `CartonDao`. 

Unfortunately, it's... having some trouble. You'll be taking care of that.

### Phase 0: Sundae mornings, go for a ride

1. Build your snippets package
1. Run the `Phase0Test` and verify that it passes

GOAL: Make sure your Snippets package builds properly, and the starter test passes.

Phase 0 is complete when:
- The `Phase0Test` passes

### Phase 1: A month of sundaes

If you run the `Phase1Test` test class, you'll see that all the requests
in our `FlavorRequestQueue` aren't being handled. We think it's a problem with the `LinkedList`
getting confused when multiple threads try to `poll()` at the same time.

If you look up the Javadoc for `LinkedList`, you'll see it's not thread-safe. If any operations
that modify the list (like `add()` and `poll()`) are called simultaneously, the list can end up
in a broken state.

Record your group's answers to these questions in the class digest:

1. What methods in `FlavorRequestQueue` modify the underlying list?
1. In the readings, we learned three ways to make our classes thread-safe: using atomic data structures,
   using `synchronized` methods, and using immutable types. Which srategy applies here?
   
Return to the main chat to discuss strategies among groups.

Modify `FlavorRequestQueue` to protect its list from simultaneous modification by multiple threads.

GOAL: determine what methods in `FlavorRequestQueue` must be synchronized.

Phase 1 is complete when:
- The class has agreed on how to make `FlavorRequestQueue` thread-safe.
- You've shared & reviewed the solution with another member of your group
- The `Phase1Test` test class is passing for your group

### Phase 2: We all scream

Look at the Phase1Test in `tests/java/com/amazon/ata/threadsafety/com.amazon.ata.threadsafety.suppliers`.
It populates a big request queue, then kicks off a bunch of `IceCreamMaker` threads.
That seems to work fine.

The Phase2Test first kicks off the `IceCreamMaker` thread, then populates the request queue.
Is that going to work with your synchronization? Run the `Phase2Test` test class
to verify. If it runs the test for longer than a few seconds, you've got a deadlock.

You'll see this deadlock if you `synchronized` both `needFlavor` and `nextNeededFlavor`.
But there's only one `FlavorRequestQueue`, not multiple objects. For a deadlock to occur,
two threads must be waiting on each other. 

Discuss with your team:

1. When this deadlock occurs, what object's thread has the lock?
1. What is that thread waiting for?
1. What thread is blocked waiting for the lock?
1. Would changing the length of the `sleep()` have any effect?

GOAL: Understand why this deadlock occurred and fix it.

Phase 2 is complete when:
- You've shared & reviewed the solution with another member of your group
- `Phase2Test` is passing
- `Phase0Test` and `Phase1Test` still pass as well

### Extension: Pre-packaged

You've created your own thread-safe queue. But Java has a built-in `LinkedBlockingQueue`
that's also thread-safe.

Update FlavorRequestQueue to use a `LinkedBlockingQueue` instead. Make sure `Phase1Test`
and `Phase2Test` still pass.
