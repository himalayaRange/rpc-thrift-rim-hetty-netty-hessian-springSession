/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.github.wangyi.thrift.rpc.service;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.EncodingUtils;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import javax.annotation.Generated;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-12-01")
public class Order implements org.apache.thrift.TBase<Order, Order._Fields>, java.io.Serializable, Cloneable, Comparable<Order> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Order");

  private static final org.apache.thrift.protocol.TField SERIAL_VERSION_UID_FIELD_DESC = new org.apache.thrift.protocol.TField("serialVersionUID", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField ORDER_NO_FIELD_DESC = new org.apache.thrift.protocol.TField("orderNo", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField ORDER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("orderName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField ORDER_PRICE_FIELD_DESC = new org.apache.thrift.protocol.TField("orderPrice", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("createTime", org.apache.thrift.protocol.TType.STRUCT, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new OrderStandardSchemeFactory());
    schemes.put(TupleScheme.class, new OrderTupleSchemeFactory());
  }

  public long serialVersionUID; // required
  public String orderNo; // required
  public String orderName; // required
  public long orderPrice; // required
  public Date createTime; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SERIAL_VERSION_UID((short)1, "serialVersionUID"),
    ORDER_NO((short)2, "orderNo"),
    ORDER_NAME((short)3, "orderName"),
    ORDER_PRICE((short)4, "orderPrice"),
    CREATE_TIME((short)5, "createTime");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SERIAL_VERSION_UID
          return SERIAL_VERSION_UID;
        case 2: // ORDER_NO
          return ORDER_NO;
        case 3: // ORDER_NAME
          return ORDER_NAME;
        case 4: // ORDER_PRICE
          return ORDER_PRICE;
        case 5: // CREATE_TIME
          return CREATE_TIME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __SERIALVERSIONUID_ISSET_ID = 0;
  private static final int __ORDERPRICE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SERIAL_VERSION_UID, new org.apache.thrift.meta_data.FieldMetaData("serialVersionUID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ORDER_NO, new org.apache.thrift.meta_data.FieldMetaData("orderNo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ORDER_NAME, new org.apache.thrift.meta_data.FieldMetaData("orderName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ORDER_PRICE, new org.apache.thrift.meta_data.FieldMetaData("orderPrice", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CREATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("createTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Date.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Order.class, metaDataMap);
  }

  public Order() {
  }

  public Order(
    long serialVersionUID,
    String orderNo,
    String orderName,
    long orderPrice,
    Date createTime)
  {
    this();
    this.serialVersionUID = serialVersionUID;
    setSerialVersionUIDIsSet(true);
    this.orderNo = orderNo;
    this.orderName = orderName;
    this.orderPrice = orderPrice;
    setOrderPriceIsSet(true);
    this.createTime = createTime;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Order(Order other) {
    __isset_bitfield = other.__isset_bitfield;
    this.serialVersionUID = other.serialVersionUID;
    if (other.isSetOrderNo()) {
      this.orderNo = other.orderNo;
    }
    if (other.isSetOrderName()) {
      this.orderName = other.orderName;
    }
    this.orderPrice = other.orderPrice;
    if (other.isSetCreateTime()) {
      this.createTime = new Date(other.createTime);
    }
  }

  public Order deepCopy() {
    return new Order(this);
  }

  @Override
  public void clear() {
    setSerialVersionUIDIsSet(false);
    this.serialVersionUID = 0;
    this.orderNo = null;
    this.orderName = null;
    setOrderPriceIsSet(false);
    this.orderPrice = 0;
    this.createTime = null;
  }

  public long getSerialVersionUID() {
    return this.serialVersionUID;
  }

  public Order setSerialVersionUID(long serialVersionUID) {
    this.serialVersionUID = serialVersionUID;
    setSerialVersionUIDIsSet(true);
    return this;
  }

  public void unsetSerialVersionUID() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID);
  }

  /** Returns true if field serialVersionUID is set (has been assigned a value) and false otherwise */
  public boolean isSetSerialVersionUID() {
    return EncodingUtils.testBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID);
  }

  public void setSerialVersionUIDIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID, value);
  }

  public String getOrderNo() {
    return this.orderNo;
  }

  public Order setOrderNo(String orderNo) {
    this.orderNo = orderNo;
    return this;
  }

  public void unsetOrderNo() {
    this.orderNo = null;
  }

  /** Returns true if field orderNo is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderNo() {
    return this.orderNo != null;
  }

  public void setOrderNoIsSet(boolean value) {
    if (!value) {
      this.orderNo = null;
    }
  }

  public String getOrderName() {
    return this.orderName;
  }

  public Order setOrderName(String orderName) {
    this.orderName = orderName;
    return this;
  }

  public void unsetOrderName() {
    this.orderName = null;
  }

  /** Returns true if field orderName is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderName() {
    return this.orderName != null;
  }

  public void setOrderNameIsSet(boolean value) {
    if (!value) {
      this.orderName = null;
    }
  }

  public long getOrderPrice() {
    return this.orderPrice;
  }

  public Order setOrderPrice(long orderPrice) {
    this.orderPrice = orderPrice;
    setOrderPriceIsSet(true);
    return this;
  }

  public void unsetOrderPrice() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ORDERPRICE_ISSET_ID);
  }

  /** Returns true if field orderPrice is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderPrice() {
    return EncodingUtils.testBit(__isset_bitfield, __ORDERPRICE_ISSET_ID);
  }

  public void setOrderPriceIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ORDERPRICE_ISSET_ID, value);
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public Order setCreateTime(Date createTime) {
    this.createTime = createTime;
    return this;
  }

  public void unsetCreateTime() {
    this.createTime = null;
  }

  /** Returns true if field createTime is set (has been assigned a value) and false otherwise */
  public boolean isSetCreateTime() {
    return this.createTime != null;
  }

  public void setCreateTimeIsSet(boolean value) {
    if (!value) {
      this.createTime = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SERIAL_VERSION_UID:
      if (value == null) {
        unsetSerialVersionUID();
      } else {
        setSerialVersionUID((Long)value);
      }
      break;

    case ORDER_NO:
      if (value == null) {
        unsetOrderNo();
      } else {
        setOrderNo((String)value);
      }
      break;

    case ORDER_NAME:
      if (value == null) {
        unsetOrderName();
      } else {
        setOrderName((String)value);
      }
      break;

    case ORDER_PRICE:
      if (value == null) {
        unsetOrderPrice();
      } else {
        setOrderPrice((Long)value);
      }
      break;

    case CREATE_TIME:
      if (value == null) {
        unsetCreateTime();
      } else {
        setCreateTime((Date)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SERIAL_VERSION_UID:
      return getSerialVersionUID();

    case ORDER_NO:
      return getOrderNo();

    case ORDER_NAME:
      return getOrderName();

    case ORDER_PRICE:
      return getOrderPrice();

    case CREATE_TIME:
      return getCreateTime();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SERIAL_VERSION_UID:
      return isSetSerialVersionUID();
    case ORDER_NO:
      return isSetOrderNo();
    case ORDER_NAME:
      return isSetOrderName();
    case ORDER_PRICE:
      return isSetOrderPrice();
    case CREATE_TIME:
      return isSetCreateTime();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Order)
      return this.equals((Order)that);
    return false;
  }

  public boolean equals(Order that) {
    if (that == null)
      return false;

    boolean this_present_serialVersionUID = true;
    boolean that_present_serialVersionUID = true;
    if (this_present_serialVersionUID || that_present_serialVersionUID) {
      if (!(this_present_serialVersionUID && that_present_serialVersionUID))
        return false;
      if (this.serialVersionUID != that.serialVersionUID)
        return false;
    }

    boolean this_present_orderNo = true && this.isSetOrderNo();
    boolean that_present_orderNo = true && that.isSetOrderNo();
    if (this_present_orderNo || that_present_orderNo) {
      if (!(this_present_orderNo && that_present_orderNo))
        return false;
      if (!this.orderNo.equals(that.orderNo))
        return false;
    }

    boolean this_present_orderName = true && this.isSetOrderName();
    boolean that_present_orderName = true && that.isSetOrderName();
    if (this_present_orderName || that_present_orderName) {
      if (!(this_present_orderName && that_present_orderName))
        return false;
      if (!this.orderName.equals(that.orderName))
        return false;
    }

    boolean this_present_orderPrice = true;
    boolean that_present_orderPrice = true;
    if (this_present_orderPrice || that_present_orderPrice) {
      if (!(this_present_orderPrice && that_present_orderPrice))
        return false;
      if (this.orderPrice != that.orderPrice)
        return false;
    }

    boolean this_present_createTime = true && this.isSetCreateTime();
    boolean that_present_createTime = true && that.isSetCreateTime();
    if (this_present_createTime || that_present_createTime) {
      if (!(this_present_createTime && that_present_createTime))
        return false;
      if (!this.createTime.equals(that.createTime))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_serialVersionUID = true;
    list.add(present_serialVersionUID);
    if (present_serialVersionUID)
      list.add(serialVersionUID);

    boolean present_orderNo = true && (isSetOrderNo());
    list.add(present_orderNo);
    if (present_orderNo)
      list.add(orderNo);

    boolean present_orderName = true && (isSetOrderName());
    list.add(present_orderName);
    if (present_orderName)
      list.add(orderName);

    boolean present_orderPrice = true;
    list.add(present_orderPrice);
    if (present_orderPrice)
      list.add(orderPrice);

    boolean present_createTime = true && (isSetCreateTime());
    list.add(present_createTime);
    if (present_createTime)
      list.add(createTime);

    return list.hashCode();
  }

  @Override
  public int compareTo(Order other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetSerialVersionUID()).compareTo(other.isSetSerialVersionUID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSerialVersionUID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serialVersionUID, other.serialVersionUID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOrderNo()).compareTo(other.isSetOrderNo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderNo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderNo, other.orderNo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOrderName()).compareTo(other.isSetOrderName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderName, other.orderName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOrderPrice()).compareTo(other.isSetOrderPrice());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderPrice()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderPrice, other.orderPrice);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCreateTime()).compareTo(other.isSetCreateTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCreateTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.createTime, other.createTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Order(");
    boolean first = true;

    sb.append("serialVersionUID:");
    sb.append(this.serialVersionUID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("orderNo:");
    if (this.orderNo == null) {
      sb.append("null");
    } else {
      sb.append(this.orderNo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("orderName:");
    if (this.orderName == null) {
      sb.append("null");
    } else {
      sb.append(this.orderName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("orderPrice:");
    sb.append(this.orderPrice);
    first = false;
    if (!first) sb.append(", ");
    sb.append("createTime:");
    if (this.createTime == null) {
      sb.append("null");
    } else {
      sb.append(this.createTime);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (createTime != null) {
      createTime.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class OrderStandardSchemeFactory implements SchemeFactory {
    public OrderStandardScheme getScheme() {
      return new OrderStandardScheme();
    }
  }

  private static class OrderStandardScheme extends StandardScheme<Order> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Order struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SERIAL_VERSION_UID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.serialVersionUID = iprot.readI64();
              struct.setSerialVersionUIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ORDER_NO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.orderNo = iprot.readString();
              struct.setOrderNoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ORDER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.orderName = iprot.readString();
              struct.setOrderNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ORDER_PRICE
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.orderPrice = iprot.readI64();
              struct.setOrderPriceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CREATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.createTime = new Date();
              struct.createTime.read(iprot);
              struct.setCreateTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Order struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(SERIAL_VERSION_UID_FIELD_DESC);
      oprot.writeI64(struct.serialVersionUID);
      oprot.writeFieldEnd();
      if (struct.orderNo != null) {
        oprot.writeFieldBegin(ORDER_NO_FIELD_DESC);
        oprot.writeString(struct.orderNo);
        oprot.writeFieldEnd();
      }
      if (struct.orderName != null) {
        oprot.writeFieldBegin(ORDER_NAME_FIELD_DESC);
        oprot.writeString(struct.orderName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ORDER_PRICE_FIELD_DESC);
      oprot.writeI64(struct.orderPrice);
      oprot.writeFieldEnd();
      if (struct.createTime != null) {
        oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
        struct.createTime.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OrderTupleSchemeFactory implements SchemeFactory {
    public OrderTupleScheme getScheme() {
      return new OrderTupleScheme();
    }
  }

  private static class OrderTupleScheme extends TupleScheme<Order> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Order struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetSerialVersionUID()) {
        optionals.set(0);
      }
      if (struct.isSetOrderNo()) {
        optionals.set(1);
      }
      if (struct.isSetOrderName()) {
        optionals.set(2);
      }
      if (struct.isSetOrderPrice()) {
        optionals.set(3);
      }
      if (struct.isSetCreateTime()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetSerialVersionUID()) {
        oprot.writeI64(struct.serialVersionUID);
      }
      if (struct.isSetOrderNo()) {
        oprot.writeString(struct.orderNo);
      }
      if (struct.isSetOrderName()) {
        oprot.writeString(struct.orderName);
      }
      if (struct.isSetOrderPrice()) {
        oprot.writeI64(struct.orderPrice);
      }
      if (struct.isSetCreateTime()) {
        struct.createTime.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Order struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.serialVersionUID = iprot.readI64();
        struct.setSerialVersionUIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.orderNo = iprot.readString();
        struct.setOrderNoIsSet(true);
      }
      if (incoming.get(2)) {
        struct.orderName = iprot.readString();
        struct.setOrderNameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.orderPrice = iprot.readI64();
        struct.setOrderPriceIsSet(true);
      }
      if (incoming.get(4)) {
        struct.createTime = new Date();
        struct.createTime.read(iprot);
        struct.setCreateTimeIsSet(true);
      }
    }
  }

}

