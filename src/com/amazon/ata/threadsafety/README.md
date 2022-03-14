## Ice Cream Party

**branch-name:** `threadsafety-classroom`

You do not need to be running `ada` for this activity.

**RDE workflows:**
- threadsafety-classroom-phase0
- threadsafety-classroom-phase1
- threadsafety-classroom-phase2

### Introduction

Remember our `IceCreamParlorService` from prior lessons?
Jeff was so impressed that he wants us to offer sundaes at the Amazon Summer Picnic!

That's a lot more traffic than we imagined when we built our humble ice cream service,
so we're making some changes. We're starting by improving the ice cream production.
 
The `IceCreamMaker` now represents a single ice cream carton making machine.
Each `IceCreamMaker` runs in its own thread, watching the `FlavorRequestQueue` that
it shares with the other `IceCreamMaker`s.
When a flavor is added to the queue, one `IceCreamMaker` removes it, creates a carton of that flavor,
and adds it to the `CartonDao`. 

Unfortunately, it's... having some trouble. You'll be taking care of that.

### Phase 0: Sundae mornings, go for a ride

1. Build your snippets package
1. Run the `threadsafety-classroom-phase0` RDE workflow and verify that it passes

GOAL: Make sure your Snippets package builds properly, and the starter test passes.

Phase 0 is complete when:
- The `threadsafety-classroom-phase0` RDE workflow runs and passes

### Phase 1: A month of sundaes

If you run the `threadsafety-classroom-phase1` RDE workflow, you'll see that all the requests
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
- The `threadsafety-classroom-phase1` RDE workflow is passing for your group

### Phase 2: We all scream

Look at the Phase1Test in `tests/java/com/amazon/ata/threadsafety/classroom/suppliers`.
It populates a shared request queue, then kicks off a bunch of `IceCreamMaker` threads.
That seems to work fine.

Now check out the Phase2Test in the same directory. It first kicks off many `IceCreamMaker` threads
in an `ExecutorService`, then adds many flavors to the shared request queue.
Is that going to work with our synchronization? Run the `threadsafety-classroom-phase2` test
to verify. If it runs the test for longer than a few seconds, you've got a deadlock.

You'll see this deadlock if you `synchronized` both `needFlavor` and `nextNeededFlavor`.
But there's only one `FlavorRequestQueue`, not multiple objects. For a deadlock to occur,
two threads must be waiting on each other. 

While we could probably resolve this using a thread-safe data structure for the underlying
list, we want to implement our own synchronization so that we understand how `ConcurrentLinkedList`
and similar structures work.

Discuss with your team:

1. When this deadlock occurs, what object's thread has the lock on the `FlavorRequestQueue`?
1. Threads usually release their lock when they return from the synchronized method.
   What's stopping this thread from releasing its lock?
1. Would changing the length of the `sleep()` have any effect?
1. Does the entire method need to be synchronized?

GOAL: Understand why this deadlock occurred and fix it.

Phase 2 is complete when:
- You've shared & reviewed the solution with another member of your group
- `threadsafety-classroom-phase2` RDE workflow is passing
  (This also runs the previous phase tests to ensure that they're still passing)

### Extension: Pre-packaged

You've created your own thread-safe queue. But Java has a built-in `LinkedBlockingQueue`
that's also thread-safe.

Update FlavorRequestQueue to use a `LinkedBlockingQueue` instead. Make sure `threadsafety-classroom-phase1`
and `threadsafety-classroom-phase2` still pass.
