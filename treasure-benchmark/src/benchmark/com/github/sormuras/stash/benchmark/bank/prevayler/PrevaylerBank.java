package com.github.sormuras.stash.benchmark.bank.prevayler;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.foundation.monitor.Monitor;
import org.prevayler.foundation.monitor.SimpleMonitor;
import org.prevayler.foundation.serialization.JavaSerializer;
import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.implementation.PrevaylerDirectory;
import org.prevayler.implementation.PrevaylerImpl;
import org.prevayler.implementation.clock.MachineClock;
import org.prevayler.implementation.journal.Journal;
import org.prevayler.implementation.journal.PersistentJournal;
import org.prevayler.implementation.journal.TransientJournal;
import org.prevayler.implementation.publishing.CentralPublisher;
import org.prevayler.implementation.publishing.TransactionPublisher;
import org.prevayler.implementation.snapshot.GenericSnapshotManager;
import org.prevayler.implementation.snapshot.NullSnapshotManager;

import com.github.sormuras.stash.benchmark.bank.Bank;
import com.github.sormuras.stash.benchmark.bank.Status;

public class PrevaylerBank implements Bank<Integer> {

  private static final long serialVersionUID = 1L;

  /**
   * @return default prevayler using prevalent system class loader and given journaling folder
   */
  public static <P> Prevayler<P> prevayler(P prevalentSystem, File folder) throws Exception {
    return prevayler(prevalentSystem, folder, prevalentSystem.getClass().getClassLoader());
  }

  /**
   * @return default prevayler using given class loader and given journaling folder
   */
  public static <P> Prevayler<P> prevayler(P prevalentSystem, File folder, ClassLoader loader) throws Exception {
    PrevaylerDirectory directory = new PrevaylerDirectory(folder);
    Monitor monitor = new SimpleMonitor(System.err);
    Journal journal = new PersistentJournal(directory, 0, 0, true, "journal", monitor);
    Serializer serializer = new JavaSerializer(loader);
    Map<String, Serializer> map = Collections.singletonMap("snapshot", serializer);
    GenericSnapshotManager<P> snapshotManager = new GenericSnapshotManager<>(map, "snapshot", prevalentSystem, directory, serializer);
    Clock clock = new MachineClock();
    TransactionPublisher publisher = new CentralPublisher(clock, journal);
    boolean transactionDeepCopyMode = false;
    return new PrevaylerImpl<>(snapshotManager, publisher, serializer, transactionDeepCopyMode);
  }

  /**
   * @return default prevayler using given class loader and given journaling folder
   */
  public static <P> Prevayler<P> prevaylerTransient(P prevalentSystem) throws Exception {
    Journal journal = new TransientJournal();
    Serializer serializer = new JavaSerializer(prevalentSystem.getClass().getClassLoader());
    GenericSnapshotManager<P> snapshotManager = new NullSnapshotManager<P>(prevalentSystem, "No snap, no bite.");
    Clock clock = new MachineClock();
    TransactionPublisher publisher = new CentralPublisher(clock, journal);
    boolean transactionDeepCopyMode = false;
    return new PrevaylerImpl<>(snapshotManager, publisher, serializer, transactionDeepCopyMode);
  }

  private final Prevayler<Map<Integer, PrevaylerAccount>> prevayler;

  public PrevaylerBank(Path folder) {
    try {
      Map<Integer, PrevaylerAccount> prevalentSystem = new HashMap<>();
      this.prevayler = folder != null ? prevayler(prevalentSystem, folder.toFile()) : prevaylerTransient(prevalentSystem);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Integer[] create(Integer numberOfAccounts) {
    return prevayler.execute(new CreateAccountsTransaction(numberOfAccounts));
  }

  @Override
  public Status status(Integer id) {
    try {
      return prevayler.execute(new GetAccountStatusQuery(id));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void transfer(Integer from, Integer to, int amount) {
    prevayler.execute(new TransferTransaction(from, to, amount));
  }

}
