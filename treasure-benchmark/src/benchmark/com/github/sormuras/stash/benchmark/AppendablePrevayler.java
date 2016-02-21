package com.github.sormuras.stash.benchmark;

import java.util.Date;

import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.TransactionWithQuery;
import org.prevayler.foundation.serialization.JavaSerializer;
import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.implementation.PrevaylerImpl;
import org.prevayler.implementation.clock.MachineClock;
import org.prevayler.implementation.journal.Journal;
import org.prevayler.implementation.journal.TransientJournal;
import org.prevayler.implementation.publishing.CentralPublisher;
import org.prevayler.implementation.publishing.TransactionPublisher;
import org.prevayler.implementation.snapshot.GenericSnapshotManager;
import org.prevayler.implementation.snapshot.NullSnapshotManager;

@SuppressWarnings("serial")
public interface AppendablePrevayler {

  class AppendCharTransaction implements TransactionWithQuery<Appendable, Appendable> {
    final char c;

    public AppendCharTransaction(char c) {
      this.c = c;
    }

    @Override
    public Appendable executeAndQuery(Appendable appendable, Date time) throws Exception {
      return appendable.append(c);
    }
  }

  class AppendCharSequenceTransaction implements TransactionWithQuery<Appendable, Appendable> {
    final CharSequence csq;

    public AppendCharSequenceTransaction(CharSequence csq) {
      this.csq = csq;
    }

    @Override
    public Appendable executeAndQuery(Appendable appendable, Date time) throws Exception {
      return appendable.append(csq);
    }
  }

  class AppendCharSequenceRangeTransaction implements TransactionWithQuery<Appendable, Appendable> {
    final CharSequence csq;
    final int start;
    final int end;

    public AppendCharSequenceRangeTransaction(CharSequence csq, int start, int end) {
      this.csq = csq;
      this.start = start;
      this.end = end;
    }

    @Override
    public Appendable executeAndQuery(Appendable appendable, Date time) throws Exception {
      return appendable.append(csq, start, end);
    }
  }

  /**
   * @return default prevayler using given class loader and given journaling folder
   */
  static <P> Prevayler<P> prevaylerTransient(P prevalentSystem) {
    Journal journal = new TransientJournal();
    Serializer serializer = new JavaSerializer(prevalentSystem.getClass().getClassLoader());
    GenericSnapshotManager<P> snapshotManager = new NullSnapshotManager<P>(prevalentSystem, "No snap, no bite.");
    Clock clock = new MachineClock();
    TransactionPublisher publisher = new CentralPublisher(clock, journal);
    boolean transactionDeepCopyMode = false;
    try {
      return new PrevaylerImpl<>(snapshotManager, publisher, serializer, transactionDeepCopyMode);
    } catch (Exception e) {
      throw new Error(e);
    }
  }
}
