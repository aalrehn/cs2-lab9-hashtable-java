package edu.luc.cs271.myhashmap;

import java.util.*;

/**
 * A generic HashMap custom implementation using chaining. Concretely, the table is an ArrayList of
 * chains represented as LinkedLists.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class MyHashMap<K, V> implements Map<K, V> {

  private static final int DEFAULT_TABLE_SIZE = 11; // a prime

  private List<List<Entry<K, V>>> table;

  public MyHashMap() {
    this(DEFAULT_TABLE_SIZE);
  }

  public MyHashMap(final int tableSize) {
    // allocate a table of the given size
    table = new ArrayList<>(tableSize);
    // then create an empty chain at each position
    for (int i = 0; i < tableSize; i += 1) {
      table.add(new LinkedList<>());
    }
  }

  @Override
  public int size() {
    int result = 0;
    for(int i = 0;i<table.size();i++){
      result += table.get(i).size();
    }

    return result;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(final Object key) {
    final int index = calculateIndex(key);

    final Iterator<Entry<K, V>> iter = table.get(index).iterator();
    while (iter.hasNext()) {
      final Entry<K, V> entry = iter.next();
      if (entry.getKey().equals(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsValue(final Object value) {
    for(int i = 0;i<table.size();i++){
      for(int j = 0;j<table.get(i).size();j++){
        if(table.get(i).get(j).getValue() == value){
          return true;
        }
      }
    }


    return false;
  }

  @Override
  public V get(final Object key) {
    final int index = calculateIndex(key);

    final Iterator<Entry<K, V>> iter = table.get(index).iterator();
    while (iter.hasNext()) {
      final Entry<K, V> entry = iter.next();
      if (entry.getKey().equals(key)) {
        return entry.getValue();
      }
    }

    return null;
  }

  @Override
  public V put(final K key, final V value) {
    final int index = calculateIndex(key);

    final Iterator<Entry<K, V>> iter = table.get(index).iterator();
    while (iter.hasNext()) {
      final Entry<K, V> entry = iter.next();
      if (entry.getKey().equals(key)) {
        V val = entry.getValue();
        entry.setValue(value);
        return val;
      }
    }

    table.get(index).add(new AbstractMap.SimpleEntry<K, V>(key,value));
    return null;
  }

  @Override
  public V remove(final Object key) {
    final int index = calculateIndex(key);
    final Iterator<Entry<K, V>> iter = table.get(index).iterator();
    while (iter.hasNext()) {
      final Entry<K, V> entry = iter.next();
      if (entry.getKey().equals(key)) {
        final V oldValue = entry.getValue();
        iter.remove();
        return oldValue;
      }
    }
    return null;
  }

  @Override
  public void putAll(final Map<? extends K, ? extends V> m) {
    for(Entry<? extends K, ? extends V> e : m.entrySet()){
      this.put(e.getKey(), e.getValue());
    }
  }

  @Override
  public void clear() {
    for(int i = 0;i<table.size();i++){
      table.get(i).clear();
    }
  }

  /** The resulting keySet is not "backed" by the Map, so we keep it unmodifiable. */
  @Override
  public Set<K> keySet() {
    final Set<K> result = new HashSet<>();
    for(int i = 0;i<table.size();i++){
      for(Entry<K,V> e: table.get(i)){
        result.add(e.getKey());
      }
    }

    return Collections.unmodifiableSet(result);
  }

  /** The resulting values collection is not "backed" by the Map, so we keep it unmodifiable. */
  @Override
  public Collection<V> values() {
    final List<V> result = new LinkedList<>();
    for(int i = 0;i<table.size();i++){
      for(Entry<K,V> e: table.get(i)){
        result.add(e.getValue());
      }
    }

    return Collections.unmodifiableCollection(result);
  }

  /** The resulting entrySet is not "backed" by the Map, so we keep it unmodifiable. */
  @Override
  public Set<Entry<K, V>> entrySet() {
    final Set<Entry<K, V>> result = new HashSet<>();
    for(int i = 0;i<table.size();i++){
      for(Entry<K,V> e: table.get(i)){
        result.add(e);
      }
    }
    return Collections.unmodifiableSet(result);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (List<Entry<K, V>> aTable : table) {
      for (Entry<K, V> e : aTable) {
        result.append(e.toString());
      }
    }
    return String.valueOf(result);
  }

  public boolean equals(final Object that) {
    if (this == that) {
      return true;
    } else if (!(that instanceof Map)) {
      return false;
    } else {
      return this.entrySet().equals(((Map) that).entrySet());
    }
  }

  private int calculateIndex(final Object key) {
    // positive remainder (as opposed to %)
    // required in case hashCode is negative!
    return Math.floorMod(key.hashCode(), table.size());
  }
}