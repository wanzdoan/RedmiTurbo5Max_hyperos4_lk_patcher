package com.android.internal.telephony;

import com.android.internal.telephony.protobuf.AbstractMessageLite;
import com.android.internal.telephony.protobuf.ByteString;
import com.android.internal.telephony.protobuf.CodedInputStream;
import com.android.internal.telephony.protobuf.ExtensionRegistryLite;
import com.android.internal.telephony.protobuf.GeneratedMessageLite;
import com.android.internal.telephony.protobuf.Internal;
import com.android.internal.telephony.protobuf.InvalidProtocolBufferException;
import com.android.internal.telephony.protobuf.MessageLiteOrBuilder;
import com.android.internal.telephony.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyConfigData {

    public interface CarrierRoamingConfigProtoOrBuilder extends MessageLiteOrBuilder {
        String getDeviceSatellitePlmn(int i);

        ByteString getDeviceSatellitePlmnBytes(int i);

        int getDeviceSatellitePlmnCount();

        List<String> getDeviceSatellitePlmnList();

        int getMaxAllowedDataMode();

        boolean getOverrideWfcRoamingModeWhileUsingNtn();

        boolean hasMaxAllowedDataMode();

        boolean hasOverrideWfcRoamingModeWhileUsingNtn();
    }

    public interface CarrierSupportedSatelliteServicesProtoOrBuilder extends MessageLiteOrBuilder {
        boolean getAttachSupported();

        int getCarrierId();

        int getDataSupportMode();

        boolean getEmergencyMessagingSupported();

        String getEntitlementServerUrl();

        ByteString getEntitlementServerUrlBytes();

        boolean getEntitlementSupported();

        int getNtnConnectType();

        SatelliteProviderCapabilityProto getSupportedSatelliteProviderCapabilities(int i);

        int getSupportedSatelliteProviderCapabilitiesCount();

        List<SatelliteProviderCapabilityProto> getSupportedSatelliteProviderCapabilitiesList();

        boolean hasAttachSupported();

        boolean hasCarrierId();

        boolean hasDataSupportMode();

        boolean hasEmergencyMessagingSupported();

        boolean hasEntitlementServerUrl();

        boolean hasEntitlementSupported();

        boolean hasNtnConnectType();
    }

    public interface ConnectionCapabilityConfigOrBuilder extends MessageLiteOrBuilder {
        ConnectionCapabilityMap getCarrierConnectionCapabilityConfigs(int i);

        int getCarrierConnectionCapabilityConfigsCount();

        List<ConnectionCapabilityMap> getCarrierConnectionCapabilityConfigsList();

        ConnectionCapabilityMap getDefaultConnectionCapabilityConfig();

        boolean hasDefaultConnectionCapabilityConfig();
    }

    public interface ConnectionCapabilityMapOrBuilder extends MessageLiteOrBuilder {
        int getCarrierId();

        String getRules(int i);

        ByteString getRulesBytes(int i);

        int getRulesCount();

        List<String> getRulesList();

        boolean hasCarrierId();
    }

    public interface DataConfigProtoOrBuilder extends MessageLiteOrBuilder {
        ConnectionCapabilityConfig getConnectionCapabilityConfigs();

        MeteredCapabilityConfig getHomeMeteredCapabilityConfigs();

        MeteredCapabilityConfig getRoamMeteredCapabilityConfigs();

        int getVersion();

        boolean hasConnectionCapabilityConfigs();

        boolean hasHomeMeteredCapabilityConfigs();

        boolean hasRoamMeteredCapabilityConfigs();

        boolean hasVersion();
    }

    public interface MeteredCapabilitiesOrBuilder extends MessageLiteOrBuilder {
        int getCapabilityIds(int i);

        int getCapabilityIdsCount();

        List<Integer> getCapabilityIdsList();

        int getCarrierId();

        boolean hasCarrierId();
    }

    public interface MeteredCapabilityConfigOrBuilder extends MessageLiteOrBuilder {
        MeteredCapabilities getCarrierMeteredCapabilityConfigs(int i);

        int getCarrierMeteredCapabilityConfigsCount();

        List<MeteredCapabilities> getCarrierMeteredCapabilityConfigsList();

        MeteredCapabilities getDefaultMeteredCapabilityConfig();

        boolean hasDefaultMeteredCapabilityConfig();
    }

    public interface SatelliteConfigProtoOrBuilder extends MessageLiteOrBuilder {
        CarrierRoamingConfigProto getCarrierRoamingConfig();

        CarrierSupportedSatelliteServicesProto getCarrierSupportedSatelliteServices(int i);

        int getCarrierSupportedSatelliteServicesCount();

        List<CarrierSupportedSatelliteServicesProto> getCarrierSupportedSatelliteServicesList();

        SatelliteRegionProto getDeviceSatelliteRegion();

        int getVersion();

        boolean hasCarrierRoamingConfig();

        boolean hasDeviceSatelliteRegion();

        boolean hasVersion();
    }

    public interface SatelliteProviderCapabilityProtoOrBuilder extends MessageLiteOrBuilder {
        int getAllowedServices(int i);

        int getAllowedServicesCount();

        List<Integer> getAllowedServicesList();

        String getCarrierPlmn();

        ByteString getCarrierPlmnBytes();

        int getNtnConnectType();

        boolean hasCarrierPlmn();

        boolean hasNtnConnectType();
    }

    public interface SatelliteRegionProtoOrBuilder extends MessageLiteOrBuilder {
        String getCountryCodes(int i);

        ByteString getCountryCodesBytes(int i);

        int getCountryCodesCount();

        List<String> getCountryCodesList();

        boolean getIsAllowed();

        ByteString getS2CellFile();

        ByteString getSatelliteAccessConfigFile();

        boolean hasIsAllowed();

        boolean hasS2CellFile();

        boolean hasSatelliteAccessConfigFile();
    }

    public interface TelephonyConfigProtoOrBuilder extends MessageLiteOrBuilder {
        DataConfigProto getData();

        SatelliteConfigProto getSatellite();

        boolean hasData();

        boolean hasSatellite();
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }

    public static final class TelephonyConfigProto extends GeneratedMessageLite<TelephonyConfigProto, Builder> implements TelephonyConfigProtoOrBuilder {
        public static final int DATA_FIELD_NUMBER = 2;
        private static final TelephonyConfigProto DEFAULT_INSTANCE;
        private static volatile Parser<TelephonyConfigProto> PARSER = null;
        public static final int SATELLITE_FIELD_NUMBER = 1;
        private int bitField0_;
        private DataConfigProto data_;
        private SatelliteConfigProto satellite_;

        private TelephonyConfigProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
        public boolean hasSatellite() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
        public SatelliteConfigProto getSatellite() {
            SatelliteConfigProto satelliteConfigProto = this.satellite_;
            return satelliteConfigProto == null ? SatelliteConfigProto.getDefaultInstance() : satelliteConfigProto;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSatellite(SatelliteConfigProto satelliteConfigProto) {
            satelliteConfigProto.getClass();
            this.satellite_ = satelliteConfigProto;
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeSatellite(SatelliteConfigProto satelliteConfigProto) {
            satelliteConfigProto.getClass();
            SatelliteConfigProto satelliteConfigProto2 = this.satellite_;
            if (satelliteConfigProto2 != null && satelliteConfigProto2 != SatelliteConfigProto.getDefaultInstance()) {
                this.satellite_ = SatelliteConfigProto.newBuilder(this.satellite_).mergeFrom(satelliteConfigProto).buildPartial();
            } else {
                this.satellite_ = satelliteConfigProto;
            }
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSatellite() {
            this.satellite_ = null;
            this.bitField0_ &= -2;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
        public boolean hasData() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
        public DataConfigProto getData() {
            DataConfigProto dataConfigProto = this.data_;
            return dataConfigProto == null ? DataConfigProto.getDefaultInstance() : dataConfigProto;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setData(DataConfigProto dataConfigProto) {
            dataConfigProto.getClass();
            this.data_ = dataConfigProto;
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeData(DataConfigProto dataConfigProto) {
            dataConfigProto.getClass();
            DataConfigProto dataConfigProto2 = this.data_;
            if (dataConfigProto2 != null && dataConfigProto2 != DataConfigProto.getDefaultInstance()) {
                this.data_ = DataConfigProto.newBuilder(this.data_).mergeFrom(dataConfigProto).buildPartial();
            } else {
                this.data_ = dataConfigProto;
            }
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearData() {
            this.data_ = null;
            this.bitField0_ &= -3;
        }

        public static TelephonyConfigProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static TelephonyConfigProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static TelephonyConfigProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static TelephonyConfigProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static TelephonyConfigProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static TelephonyConfigProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static TelephonyConfigProto parseFrom(InputStream inputStream) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static TelephonyConfigProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static TelephonyConfigProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static TelephonyConfigProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static TelephonyConfigProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static TelephonyConfigProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (TelephonyConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(TelephonyConfigProto telephonyConfigProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(telephonyConfigProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<TelephonyConfigProto, Builder> implements TelephonyConfigProtoOrBuilder {
            private Builder() {
                super(TelephonyConfigProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
            public boolean hasSatellite() {
                return ((TelephonyConfigProto) this.instance).hasSatellite();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
            public SatelliteConfigProto getSatellite() {
                return ((TelephonyConfigProto) this.instance).getSatellite();
            }

            public Builder setSatellite(SatelliteConfigProto satelliteConfigProto) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).setSatellite(satelliteConfigProto);
                return this;
            }

            public Builder setSatellite(SatelliteConfigProto.Builder builder) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).setSatellite(builder.build());
                return this;
            }

            public Builder mergeSatellite(SatelliteConfigProto satelliteConfigProto) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).mergeSatellite(satelliteConfigProto);
                return this;
            }

            public Builder clearSatellite() {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).clearSatellite();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
            public boolean hasData() {
                return ((TelephonyConfigProto) this.instance).hasData();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.TelephonyConfigProtoOrBuilder
            public DataConfigProto getData() {
                return ((TelephonyConfigProto) this.instance).getData();
            }

            public Builder setData(DataConfigProto dataConfigProto) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).setData(dataConfigProto);
                return this;
            }

            public Builder setData(DataConfigProto.Builder builder) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).setData(builder.build());
                return this;
            }

            public Builder mergeData(DataConfigProto dataConfigProto) {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).mergeData(dataConfigProto);
                return this;
            }

            public Builder clearData() {
                copyOnWrite();
                ((TelephonyConfigProto) this.instance).clearData();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new TelephonyConfigProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001", new Object[]{"bitField0_", "satellite_", "data_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<TelephonyConfigProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (TelephonyConfigProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            TelephonyConfigProto telephonyConfigProto = new TelephonyConfigProto();
            DEFAULT_INSTANCE = telephonyConfigProto;
            GeneratedMessageLite.registerDefaultInstance(TelephonyConfigProto.class, telephonyConfigProto);
        }

        public static TelephonyConfigProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<TelephonyConfigProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.TelephonyConfigData$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke;

        static {
            int[] iArr = new int[GeneratedMessageLite.MethodToInvoke.values().length];
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke = iArr;
            try {
                iArr[GeneratedMessageLite.MethodToInvoke.NEW_MUTABLE_INSTANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.NEW_BUILDER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.BUILD_MESSAGE_INFO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_DEFAULT_INSTANCE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_PARSER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_MEMOIZED_IS_INITIALIZED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.SET_MEMOIZED_IS_INITIALIZED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public static final class SatelliteConfigProto extends GeneratedMessageLite<SatelliteConfigProto, Builder> implements SatelliteConfigProtoOrBuilder {
        public static final int CARRIER_ROAMING_CONFIG_FIELD_NUMBER = 4;
        public static final int CARRIER_SUPPORTED_SATELLITE_SERVICES_FIELD_NUMBER = 2;
        private static final SatelliteConfigProto DEFAULT_INSTANCE;
        public static final int DEVICE_SATELLITE_REGION_FIELD_NUMBER = 3;
        private static volatile Parser<SatelliteConfigProto> PARSER = null;
        public static final int VERSION_FIELD_NUMBER = 1;
        private int bitField0_;
        private CarrierRoamingConfigProto carrierRoamingConfig_;
        private Internal.ProtobufList<CarrierSupportedSatelliteServicesProto> carrierSupportedSatelliteServices_ = GeneratedMessageLite.emptyProtobufList();
        private SatelliteRegionProto deviceSatelliteRegion_;
        private int version_;

        private SatelliteConfigProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public int getVersion() {
            return this.version_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setVersion(int i) {
            this.bitField0_ |= 1;
            this.version_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearVersion() {
            this.bitField0_ &= -2;
            this.version_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public List<CarrierSupportedSatelliteServicesProto> getCarrierSupportedSatelliteServicesList() {
            return this.carrierSupportedSatelliteServices_;
        }

        public List<? extends CarrierSupportedSatelliteServicesProtoOrBuilder> getCarrierSupportedSatelliteServicesOrBuilderList() {
            return this.carrierSupportedSatelliteServices_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public int getCarrierSupportedSatelliteServicesCount() {
            return this.carrierSupportedSatelliteServices_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public CarrierSupportedSatelliteServicesProto getCarrierSupportedSatelliteServices(int i) {
            return this.carrierSupportedSatelliteServices_.get(i);
        }

        public CarrierSupportedSatelliteServicesProtoOrBuilder getCarrierSupportedSatelliteServicesOrBuilder(int i) {
            return this.carrierSupportedSatelliteServices_.get(i);
        }

        private void ensureCarrierSupportedSatelliteServicesIsMutable() {
            Internal.ProtobufList<CarrierSupportedSatelliteServicesProto> protobufList = this.carrierSupportedSatelliteServices_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.carrierSupportedSatelliteServices_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
            carrierSupportedSatelliteServicesProto.getClass();
            ensureCarrierSupportedSatelliteServicesIsMutable();
            this.carrierSupportedSatelliteServices_.set(i, carrierSupportedSatelliteServicesProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierSupportedSatelliteServices(CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
            carrierSupportedSatelliteServicesProto.getClass();
            ensureCarrierSupportedSatelliteServicesIsMutable();
            this.carrierSupportedSatelliteServices_.add(carrierSupportedSatelliteServicesProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
            carrierSupportedSatelliteServicesProto.getClass();
            ensureCarrierSupportedSatelliteServicesIsMutable();
            this.carrierSupportedSatelliteServices_.add(i, carrierSupportedSatelliteServicesProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCarrierSupportedSatelliteServices(Iterable iterable) {
            ensureCarrierSupportedSatelliteServicesIsMutable();
            AbstractMessageLite.addAll(iterable, this.carrierSupportedSatelliteServices_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierSupportedSatelliteServices() {
            this.carrierSupportedSatelliteServices_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeCarrierSupportedSatelliteServices(int i) {
            ensureCarrierSupportedSatelliteServicesIsMutable();
            this.carrierSupportedSatelliteServices_.remove(i);
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public boolean hasDeviceSatelliteRegion() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public SatelliteRegionProto getDeviceSatelliteRegion() {
            SatelliteRegionProto satelliteRegionProto = this.deviceSatelliteRegion_;
            return satelliteRegionProto == null ? SatelliteRegionProto.getDefaultInstance() : satelliteRegionProto;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDeviceSatelliteRegion(SatelliteRegionProto satelliteRegionProto) {
            satelliteRegionProto.getClass();
            this.deviceSatelliteRegion_ = satelliteRegionProto;
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeDeviceSatelliteRegion(SatelliteRegionProto satelliteRegionProto) {
            satelliteRegionProto.getClass();
            SatelliteRegionProto satelliteRegionProto2 = this.deviceSatelliteRegion_;
            if (satelliteRegionProto2 != null && satelliteRegionProto2 != SatelliteRegionProto.getDefaultInstance()) {
                this.deviceSatelliteRegion_ = SatelliteRegionProto.newBuilder(this.deviceSatelliteRegion_).mergeFrom(satelliteRegionProto).buildPartial();
            } else {
                this.deviceSatelliteRegion_ = satelliteRegionProto;
            }
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearDeviceSatelliteRegion() {
            this.deviceSatelliteRegion_ = null;
            this.bitField0_ &= -3;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public boolean hasCarrierRoamingConfig() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
        public CarrierRoamingConfigProto getCarrierRoamingConfig() {
            CarrierRoamingConfigProto carrierRoamingConfigProto = this.carrierRoamingConfig_;
            return carrierRoamingConfigProto == null ? CarrierRoamingConfigProto.getDefaultInstance() : carrierRoamingConfigProto;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierRoamingConfig(CarrierRoamingConfigProto carrierRoamingConfigProto) {
            carrierRoamingConfigProto.getClass();
            this.carrierRoamingConfig_ = carrierRoamingConfigProto;
            this.bitField0_ |= 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeCarrierRoamingConfig(CarrierRoamingConfigProto carrierRoamingConfigProto) {
            carrierRoamingConfigProto.getClass();
            CarrierRoamingConfigProto carrierRoamingConfigProto2 = this.carrierRoamingConfig_;
            if (carrierRoamingConfigProto2 != null && carrierRoamingConfigProto2 != CarrierRoamingConfigProto.getDefaultInstance()) {
                this.carrierRoamingConfig_ = CarrierRoamingConfigProto.newBuilder(this.carrierRoamingConfig_).mergeFrom(carrierRoamingConfigProto).buildPartial();
            } else {
                this.carrierRoamingConfig_ = carrierRoamingConfigProto;
            }
            this.bitField0_ |= 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierRoamingConfig() {
            this.carrierRoamingConfig_ = null;
            this.bitField0_ &= -5;
        }

        public static SatelliteConfigProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SatelliteConfigProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SatelliteConfigProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SatelliteConfigProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SatelliteConfigProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SatelliteConfigProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static SatelliteConfigProto parseFrom(InputStream inputStream) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteConfigProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteConfigProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteConfigProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteConfigProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SatelliteConfigProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SatelliteConfigProto satelliteConfigProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(satelliteConfigProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SatelliteConfigProto, Builder> implements SatelliteConfigProtoOrBuilder {
            private Builder() {
                super(SatelliteConfigProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public boolean hasVersion() {
                return ((SatelliteConfigProto) this.instance).hasVersion();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public int getVersion() {
                return ((SatelliteConfigProto) this.instance).getVersion();
            }

            public Builder setVersion(int i) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setVersion(i);
                return this;
            }

            public Builder clearVersion() {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).clearVersion();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public List<CarrierSupportedSatelliteServicesProto> getCarrierSupportedSatelliteServicesList() {
                return Collections.unmodifiableList(((SatelliteConfigProto) this.instance).getCarrierSupportedSatelliteServicesList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public int getCarrierSupportedSatelliteServicesCount() {
                return ((SatelliteConfigProto) this.instance).getCarrierSupportedSatelliteServicesCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public CarrierSupportedSatelliteServicesProto getCarrierSupportedSatelliteServices(int i) {
                return ((SatelliteConfigProto) this.instance).getCarrierSupportedSatelliteServices(i);
            }

            public Builder setCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setCarrierSupportedSatelliteServices(i, carrierSupportedSatelliteServicesProto);
                return this;
            }

            public Builder setCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto.Builder builder) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setCarrierSupportedSatelliteServices(i, builder.build());
                return this;
            }

            public Builder addCarrierSupportedSatelliteServices(CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).addCarrierSupportedSatelliteServices(carrierSupportedSatelliteServicesProto);
                return this;
            }

            public Builder addCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).addCarrierSupportedSatelliteServices(i, carrierSupportedSatelliteServicesProto);
                return this;
            }

            public Builder addCarrierSupportedSatelliteServices(CarrierSupportedSatelliteServicesProto.Builder builder) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).addCarrierSupportedSatelliteServices(builder.build());
                return this;
            }

            public Builder addCarrierSupportedSatelliteServices(int i, CarrierSupportedSatelliteServicesProto.Builder builder) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).addCarrierSupportedSatelliteServices(i, builder.build());
                return this;
            }

            public Builder addAllCarrierSupportedSatelliteServices(Iterable<? extends CarrierSupportedSatelliteServicesProto> iterable) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).addAllCarrierSupportedSatelliteServices(iterable);
                return this;
            }

            public Builder clearCarrierSupportedSatelliteServices() {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).clearCarrierSupportedSatelliteServices();
                return this;
            }

            public Builder removeCarrierSupportedSatelliteServices(int i) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).removeCarrierSupportedSatelliteServices(i);
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public boolean hasDeviceSatelliteRegion() {
                return ((SatelliteConfigProto) this.instance).hasDeviceSatelliteRegion();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public SatelliteRegionProto getDeviceSatelliteRegion() {
                return ((SatelliteConfigProto) this.instance).getDeviceSatelliteRegion();
            }

            public Builder setDeviceSatelliteRegion(SatelliteRegionProto satelliteRegionProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setDeviceSatelliteRegion(satelliteRegionProto);
                return this;
            }

            public Builder setDeviceSatelliteRegion(SatelliteRegionProto.Builder builder) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setDeviceSatelliteRegion(builder.build());
                return this;
            }

            public Builder mergeDeviceSatelliteRegion(SatelliteRegionProto satelliteRegionProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).mergeDeviceSatelliteRegion(satelliteRegionProto);
                return this;
            }

            public Builder clearDeviceSatelliteRegion() {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).clearDeviceSatelliteRegion();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public boolean hasCarrierRoamingConfig() {
                return ((SatelliteConfigProto) this.instance).hasCarrierRoamingConfig();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteConfigProtoOrBuilder
            public CarrierRoamingConfigProto getCarrierRoamingConfig() {
                return ((SatelliteConfigProto) this.instance).getCarrierRoamingConfig();
            }

            public Builder setCarrierRoamingConfig(CarrierRoamingConfigProto carrierRoamingConfigProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setCarrierRoamingConfig(carrierRoamingConfigProto);
                return this;
            }

            public Builder setCarrierRoamingConfig(CarrierRoamingConfigProto.Builder builder) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).setCarrierRoamingConfig(builder.build());
                return this;
            }

            public Builder mergeCarrierRoamingConfig(CarrierRoamingConfigProto carrierRoamingConfigProto) {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).mergeCarrierRoamingConfig(carrierRoamingConfigProto);
                return this;
            }

            public Builder clearCarrierRoamingConfig() {
                copyOnWrite();
                ((SatelliteConfigProto) this.instance).clearCarrierRoamingConfig();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new SatelliteConfigProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001င\u0000\u0002\u001b\u0003ဉ\u0001\u0004ဉ\u0002", new Object[]{"bitField0_", "version_", "carrierSupportedSatelliteServices_", CarrierSupportedSatelliteServicesProto.class, "deviceSatelliteRegion_", "carrierRoamingConfig_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<SatelliteConfigProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (SatelliteConfigProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            SatelliteConfigProto satelliteConfigProto = new SatelliteConfigProto();
            DEFAULT_INSTANCE = satelliteConfigProto;
            GeneratedMessageLite.registerDefaultInstance(SatelliteConfigProto.class, satelliteConfigProto);
        }

        public static SatelliteConfigProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SatelliteConfigProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class CarrierRoamingConfigProto extends GeneratedMessageLite<CarrierRoamingConfigProto, Builder> implements CarrierRoamingConfigProtoOrBuilder {
        private static final CarrierRoamingConfigProto DEFAULT_INSTANCE;
        public static final int DEVICE_SATELLITE_PLMN_FIELD_NUMBER = 2;
        public static final int MAX_ALLOWED_DATA_MODE_FIELD_NUMBER = 1;
        public static final int OVERRIDE_WFC_ROAMING_MODE_WHILE_USING_NTN_FIELD_NUMBER = 3;
        private static volatile Parser<CarrierRoamingConfigProto> PARSER;
        private int bitField0_;
        private Internal.ProtobufList<String> deviceSatellitePlmn_ = GeneratedMessageLite.emptyProtobufList();
        private int maxAllowedDataMode_;
        private boolean overrideWfcRoamingModeWhileUsingNtn_;

        private CarrierRoamingConfigProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public boolean hasMaxAllowedDataMode() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public int getMaxAllowedDataMode() {
            return this.maxAllowedDataMode_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMaxAllowedDataMode(int i) {
            this.bitField0_ |= 1;
            this.maxAllowedDataMode_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearMaxAllowedDataMode() {
            this.bitField0_ &= -2;
            this.maxAllowedDataMode_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public List<String> getDeviceSatellitePlmnList() {
            return this.deviceSatellitePlmn_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public int getDeviceSatellitePlmnCount() {
            return this.deviceSatellitePlmn_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public String getDeviceSatellitePlmn(int i) {
            return this.deviceSatellitePlmn_.get(i);
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public ByteString getDeviceSatellitePlmnBytes(int i) {
            return ByteString.copyFromUtf8(this.deviceSatellitePlmn_.get(i));
        }

        private void ensureDeviceSatellitePlmnIsMutable() {
            Internal.ProtobufList<String> protobufList = this.deviceSatellitePlmn_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.deviceSatellitePlmn_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDeviceSatellitePlmn(int i, String str) {
            str.getClass();
            ensureDeviceSatellitePlmnIsMutable();
            this.deviceSatellitePlmn_.set(i, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDeviceSatellitePlmn(String str) {
            str.getClass();
            ensureDeviceSatellitePlmnIsMutable();
            this.deviceSatellitePlmn_.add(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllDeviceSatellitePlmn(Iterable iterable) {
            ensureDeviceSatellitePlmnIsMutable();
            AbstractMessageLite.addAll(iterable, this.deviceSatellitePlmn_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearDeviceSatellitePlmn() {
            this.deviceSatellitePlmn_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDeviceSatellitePlmnBytes(ByteString byteString) {
            ensureDeviceSatellitePlmnIsMutable();
            this.deviceSatellitePlmn_.add(byteString.toStringUtf8());
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public boolean hasOverrideWfcRoamingModeWhileUsingNtn() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
        public boolean getOverrideWfcRoamingModeWhileUsingNtn() {
            return this.overrideWfcRoamingModeWhileUsingNtn_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setOverrideWfcRoamingModeWhileUsingNtn(boolean z) {
            this.bitField0_ |= 2;
            this.overrideWfcRoamingModeWhileUsingNtn_ = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearOverrideWfcRoamingModeWhileUsingNtn() {
            this.bitField0_ &= -3;
            this.overrideWfcRoamingModeWhileUsingNtn_ = false;
        }

        public static CarrierRoamingConfigProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static CarrierRoamingConfigProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static CarrierRoamingConfigProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static CarrierRoamingConfigProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static CarrierRoamingConfigProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static CarrierRoamingConfigProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static CarrierRoamingConfigProto parseFrom(InputStream inputStream) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static CarrierRoamingConfigProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static CarrierRoamingConfigProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static CarrierRoamingConfigProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static CarrierRoamingConfigProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static CarrierRoamingConfigProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierRoamingConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(CarrierRoamingConfigProto carrierRoamingConfigProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(carrierRoamingConfigProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<CarrierRoamingConfigProto, Builder> implements CarrierRoamingConfigProtoOrBuilder {
            private Builder() {
                super(CarrierRoamingConfigProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public boolean hasMaxAllowedDataMode() {
                return ((CarrierRoamingConfigProto) this.instance).hasMaxAllowedDataMode();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public int getMaxAllowedDataMode() {
                return ((CarrierRoamingConfigProto) this.instance).getMaxAllowedDataMode();
            }

            public Builder setMaxAllowedDataMode(int i) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).setMaxAllowedDataMode(i);
                return this;
            }

            public Builder clearMaxAllowedDataMode() {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).clearMaxAllowedDataMode();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public List<String> getDeviceSatellitePlmnList() {
                return Collections.unmodifiableList(((CarrierRoamingConfigProto) this.instance).getDeviceSatellitePlmnList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public int getDeviceSatellitePlmnCount() {
                return ((CarrierRoamingConfigProto) this.instance).getDeviceSatellitePlmnCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public String getDeviceSatellitePlmn(int i) {
                return ((CarrierRoamingConfigProto) this.instance).getDeviceSatellitePlmn(i);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public ByteString getDeviceSatellitePlmnBytes(int i) {
                return ((CarrierRoamingConfigProto) this.instance).getDeviceSatellitePlmnBytes(i);
            }

            public Builder setDeviceSatellitePlmn(int i, String str) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).setDeviceSatellitePlmn(i, str);
                return this;
            }

            public Builder addDeviceSatellitePlmn(String str) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).addDeviceSatellitePlmn(str);
                return this;
            }

            public Builder addAllDeviceSatellitePlmn(Iterable<String> iterable) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).addAllDeviceSatellitePlmn(iterable);
                return this;
            }

            public Builder clearDeviceSatellitePlmn() {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).clearDeviceSatellitePlmn();
                return this;
            }

            public Builder addDeviceSatellitePlmnBytes(ByteString byteString) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).addDeviceSatellitePlmnBytes(byteString);
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public boolean hasOverrideWfcRoamingModeWhileUsingNtn() {
                return ((CarrierRoamingConfigProto) this.instance).hasOverrideWfcRoamingModeWhileUsingNtn();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierRoamingConfigProtoOrBuilder
            public boolean getOverrideWfcRoamingModeWhileUsingNtn() {
                return ((CarrierRoamingConfigProto) this.instance).getOverrideWfcRoamingModeWhileUsingNtn();
            }

            public Builder setOverrideWfcRoamingModeWhileUsingNtn(boolean z) {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).setOverrideWfcRoamingModeWhileUsingNtn(z);
                return this;
            }

            public Builder clearOverrideWfcRoamingModeWhileUsingNtn() {
                copyOnWrite();
                ((CarrierRoamingConfigProto) this.instance).clearOverrideWfcRoamingModeWhileUsingNtn();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new CarrierRoamingConfigProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001င\u0000\u0002\u001a\u0003ဇ\u0001", new Object[]{"bitField0_", "maxAllowedDataMode_", "deviceSatellitePlmn_", "overrideWfcRoamingModeWhileUsingNtn_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<CarrierRoamingConfigProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (CarrierRoamingConfigProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            CarrierRoamingConfigProto carrierRoamingConfigProto = new CarrierRoamingConfigProto();
            DEFAULT_INSTANCE = carrierRoamingConfigProto;
            GeneratedMessageLite.registerDefaultInstance(CarrierRoamingConfigProto.class, carrierRoamingConfigProto);
        }

        public static CarrierRoamingConfigProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<CarrierRoamingConfigProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class CarrierSupportedSatelliteServicesProto extends GeneratedMessageLite<CarrierSupportedSatelliteServicesProto, Builder> implements CarrierSupportedSatelliteServicesProtoOrBuilder {
        public static final int ATTACH_SUPPORTED_FIELD_NUMBER = 3;
        public static final int CARRIER_ID_FIELD_NUMBER = 1;
        public static final int DATA_SUPPORT_MODE_FIELD_NUMBER = 4;
        private static final CarrierSupportedSatelliteServicesProto DEFAULT_INSTANCE;
        public static final int EMERGENCY_MESSAGING_SUPPORTED_FIELD_NUMBER = 6;
        public static final int ENTITLEMENT_SERVER_URL_FIELD_NUMBER = 8;
        public static final int ENTITLEMENT_SUPPORTED_FIELD_NUMBER = 7;
        public static final int NTN_CONNECT_TYPE_FIELD_NUMBER = 5;
        private static volatile Parser<CarrierSupportedSatelliteServicesProto> PARSER = null;
        public static final int SUPPORTED_SATELLITE_PROVIDER_CAPABILITIES_FIELD_NUMBER = 2;
        private boolean attachSupported_;
        private int bitField0_;
        private int carrierId_;
        private int dataSupportMode_;
        private boolean emergencyMessagingSupported_;
        private boolean entitlementSupported_;
        private int ntnConnectType_;
        private Internal.ProtobufList<SatelliteProviderCapabilityProto> supportedSatelliteProviderCapabilities_ = GeneratedMessageLite.emptyProtobufList();
        private String entitlementServerUrl_ = "";

        private CarrierSupportedSatelliteServicesProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasCarrierId() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public int getCarrierId() {
            return this.carrierId_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierId(int i) {
            this.bitField0_ |= 1;
            this.carrierId_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierId() {
            this.bitField0_ &= -2;
            this.carrierId_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public List<SatelliteProviderCapabilityProto> getSupportedSatelliteProviderCapabilitiesList() {
            return this.supportedSatelliteProviderCapabilities_;
        }

        public List<? extends SatelliteProviderCapabilityProtoOrBuilder> getSupportedSatelliteProviderCapabilitiesOrBuilderList() {
            return this.supportedSatelliteProviderCapabilities_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public int getSupportedSatelliteProviderCapabilitiesCount() {
            return this.supportedSatelliteProviderCapabilities_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public SatelliteProviderCapabilityProto getSupportedSatelliteProviderCapabilities(int i) {
            return this.supportedSatelliteProviderCapabilities_.get(i);
        }

        public SatelliteProviderCapabilityProtoOrBuilder getSupportedSatelliteProviderCapabilitiesOrBuilder(int i) {
            return this.supportedSatelliteProviderCapabilities_.get(i);
        }

        private void ensureSupportedSatelliteProviderCapabilitiesIsMutable() {
            Internal.ProtobufList<SatelliteProviderCapabilityProto> protobufList = this.supportedSatelliteProviderCapabilities_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.supportedSatelliteProviderCapabilities_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
            satelliteProviderCapabilityProto.getClass();
            ensureSupportedSatelliteProviderCapabilitiesIsMutable();
            this.supportedSatelliteProviderCapabilities_.set(i, satelliteProviderCapabilityProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSupportedSatelliteProviderCapabilities(SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
            satelliteProviderCapabilityProto.getClass();
            ensureSupportedSatelliteProviderCapabilitiesIsMutable();
            this.supportedSatelliteProviderCapabilities_.add(satelliteProviderCapabilityProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
            satelliteProviderCapabilityProto.getClass();
            ensureSupportedSatelliteProviderCapabilitiesIsMutable();
            this.supportedSatelliteProviderCapabilities_.add(i, satelliteProviderCapabilityProto);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllSupportedSatelliteProviderCapabilities(Iterable iterable) {
            ensureSupportedSatelliteProviderCapabilitiesIsMutable();
            AbstractMessageLite.addAll(iterable, this.supportedSatelliteProviderCapabilities_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSupportedSatelliteProviderCapabilities() {
            this.supportedSatelliteProviderCapabilities_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeSupportedSatelliteProviderCapabilities(int i) {
            ensureSupportedSatelliteProviderCapabilitiesIsMutable();
            this.supportedSatelliteProviderCapabilities_.remove(i);
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasAttachSupported() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean getAttachSupported() {
            return this.attachSupported_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAttachSupported(boolean z) {
            this.bitField0_ |= 2;
            this.attachSupported_ = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearAttachSupported() {
            this.bitField0_ &= -3;
            this.attachSupported_ = false;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasDataSupportMode() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public int getDataSupportMode() {
            return this.dataSupportMode_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDataSupportMode(int i) {
            this.bitField0_ |= 4;
            this.dataSupportMode_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearDataSupportMode() {
            this.bitField0_ &= -5;
            this.dataSupportMode_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasNtnConnectType() {
            return (this.bitField0_ & 8) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public int getNtnConnectType() {
            return this.ntnConnectType_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setNtnConnectType(int i) {
            this.bitField0_ |= 8;
            this.ntnConnectType_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearNtnConnectType() {
            this.bitField0_ &= -9;
            this.ntnConnectType_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasEmergencyMessagingSupported() {
            return (this.bitField0_ & 16) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean getEmergencyMessagingSupported() {
            return this.emergencyMessagingSupported_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEmergencyMessagingSupported(boolean z) {
            this.bitField0_ |= 16;
            this.emergencyMessagingSupported_ = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearEmergencyMessagingSupported() {
            this.bitField0_ &= -17;
            this.emergencyMessagingSupported_ = false;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasEntitlementSupported() {
            return (this.bitField0_ & 32) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean getEntitlementSupported() {
            return this.entitlementSupported_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEntitlementSupported(boolean z) {
            this.bitField0_ |= 32;
            this.entitlementSupported_ = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearEntitlementSupported() {
            this.bitField0_ &= -33;
            this.entitlementSupported_ = false;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public boolean hasEntitlementServerUrl() {
            return (this.bitField0_ & 64) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public String getEntitlementServerUrl() {
            return this.entitlementServerUrl_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
        public ByteString getEntitlementServerUrlBytes() {
            return ByteString.copyFromUtf8(this.entitlementServerUrl_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEntitlementServerUrl(String str) {
            str.getClass();
            this.bitField0_ |= 64;
            this.entitlementServerUrl_ = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearEntitlementServerUrl() {
            this.bitField0_ &= -65;
            this.entitlementServerUrl_ = getDefaultInstance().getEntitlementServerUrl();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEntitlementServerUrlBytes(ByteString byteString) {
            this.entitlementServerUrl_ = byteString.toStringUtf8();
            this.bitField0_ |= 64;
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(InputStream inputStream) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static CarrierSupportedSatelliteServicesProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static CarrierSupportedSatelliteServicesProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static CarrierSupportedSatelliteServicesProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (CarrierSupportedSatelliteServicesProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(carrierSupportedSatelliteServicesProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<CarrierSupportedSatelliteServicesProto, Builder> implements CarrierSupportedSatelliteServicesProtoOrBuilder {
            private Builder() {
                super(CarrierSupportedSatelliteServicesProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasCarrierId() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasCarrierId();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public int getCarrierId() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getCarrierId();
            }

            public Builder setCarrierId(int i) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setCarrierId(i);
                return this;
            }

            public Builder clearCarrierId() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearCarrierId();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public List<SatelliteProviderCapabilityProto> getSupportedSatelliteProviderCapabilitiesList() {
                return Collections.unmodifiableList(((CarrierSupportedSatelliteServicesProto) this.instance).getSupportedSatelliteProviderCapabilitiesList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public int getSupportedSatelliteProviderCapabilitiesCount() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getSupportedSatelliteProviderCapabilitiesCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public SatelliteProviderCapabilityProto getSupportedSatelliteProviderCapabilities(int i) {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getSupportedSatelliteProviderCapabilities(i);
            }

            public Builder setSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setSupportedSatelliteProviderCapabilities(i, satelliteProviderCapabilityProto);
                return this;
            }

            public Builder setSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto.Builder builder) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setSupportedSatelliteProviderCapabilities(i, builder.build());
                return this;
            }

            public Builder addSupportedSatelliteProviderCapabilities(SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).addSupportedSatelliteProviderCapabilities(satelliteProviderCapabilityProto);
                return this;
            }

            public Builder addSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).addSupportedSatelliteProviderCapabilities(i, satelliteProviderCapabilityProto);
                return this;
            }

            public Builder addSupportedSatelliteProviderCapabilities(SatelliteProviderCapabilityProto.Builder builder) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).addSupportedSatelliteProviderCapabilities(builder.build());
                return this;
            }

            public Builder addSupportedSatelliteProviderCapabilities(int i, SatelliteProviderCapabilityProto.Builder builder) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).addSupportedSatelliteProviderCapabilities(i, builder.build());
                return this;
            }

            public Builder addAllSupportedSatelliteProviderCapabilities(Iterable<? extends SatelliteProviderCapabilityProto> iterable) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).addAllSupportedSatelliteProviderCapabilities(iterable);
                return this;
            }

            public Builder clearSupportedSatelliteProviderCapabilities() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearSupportedSatelliteProviderCapabilities();
                return this;
            }

            public Builder removeSupportedSatelliteProviderCapabilities(int i) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).removeSupportedSatelliteProviderCapabilities(i);
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasAttachSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasAttachSupported();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean getAttachSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getAttachSupported();
            }

            public Builder setAttachSupported(boolean z) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setAttachSupported(z);
                return this;
            }

            public Builder clearAttachSupported() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearAttachSupported();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasDataSupportMode() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasDataSupportMode();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public int getDataSupportMode() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getDataSupportMode();
            }

            public Builder setDataSupportMode(int i) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setDataSupportMode(i);
                return this;
            }

            public Builder clearDataSupportMode() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearDataSupportMode();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasNtnConnectType() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasNtnConnectType();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public int getNtnConnectType() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getNtnConnectType();
            }

            public Builder setNtnConnectType(int i) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setNtnConnectType(i);
                return this;
            }

            public Builder clearNtnConnectType() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearNtnConnectType();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasEmergencyMessagingSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasEmergencyMessagingSupported();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean getEmergencyMessagingSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getEmergencyMessagingSupported();
            }

            public Builder setEmergencyMessagingSupported(boolean z) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setEmergencyMessagingSupported(z);
                return this;
            }

            public Builder clearEmergencyMessagingSupported() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearEmergencyMessagingSupported();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasEntitlementSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasEntitlementSupported();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean getEntitlementSupported() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getEntitlementSupported();
            }

            public Builder setEntitlementSupported(boolean z) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setEntitlementSupported(z);
                return this;
            }

            public Builder clearEntitlementSupported() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearEntitlementSupported();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public boolean hasEntitlementServerUrl() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).hasEntitlementServerUrl();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public String getEntitlementServerUrl() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getEntitlementServerUrl();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.CarrierSupportedSatelliteServicesProtoOrBuilder
            public ByteString getEntitlementServerUrlBytes() {
                return ((CarrierSupportedSatelliteServicesProto) this.instance).getEntitlementServerUrlBytes();
            }

            public Builder setEntitlementServerUrl(String str) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setEntitlementServerUrl(str);
                return this;
            }

            public Builder clearEntitlementServerUrl() {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).clearEntitlementServerUrl();
                return this;
            }

            public Builder setEntitlementServerUrlBytes(ByteString byteString) {
                copyOnWrite();
                ((CarrierSupportedSatelliteServicesProto) this.instance).setEntitlementServerUrlBytes(byteString);
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new CarrierSupportedSatelliteServicesProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0001\u0000\u0001င\u0000\u0002\u001b\u0003ဇ\u0001\u0004င\u0002\u0005င\u0003\u0006ဇ\u0004\u0007ဇ\u0005\bဈ\u0006", new Object[]{"bitField0_", "carrierId_", "supportedSatelliteProviderCapabilities_", SatelliteProviderCapabilityProto.class, "attachSupported_", "dataSupportMode_", "ntnConnectType_", "emergencyMessagingSupported_", "entitlementSupported_", "entitlementServerUrl_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<CarrierSupportedSatelliteServicesProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (CarrierSupportedSatelliteServicesProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto = new CarrierSupportedSatelliteServicesProto();
            DEFAULT_INSTANCE = carrierSupportedSatelliteServicesProto;
            GeneratedMessageLite.registerDefaultInstance(CarrierSupportedSatelliteServicesProto.class, carrierSupportedSatelliteServicesProto);
        }

        public static CarrierSupportedSatelliteServicesProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<CarrierSupportedSatelliteServicesProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class SatelliteProviderCapabilityProto extends GeneratedMessageLite<SatelliteProviderCapabilityProto, Builder> implements SatelliteProviderCapabilityProtoOrBuilder {
        public static final int ALLOWED_SERVICES_FIELD_NUMBER = 2;
        public static final int CARRIER_PLMN_FIELD_NUMBER = 1;
        private static final SatelliteProviderCapabilityProto DEFAULT_INSTANCE;
        public static final int NTN_CONNECT_TYPE_FIELD_NUMBER = 3;
        private static volatile Parser<SatelliteProviderCapabilityProto> PARSER;
        private int bitField0_;
        private int ntnConnectType_;
        private String carrierPlmn_ = "";
        private Internal.IntList allowedServices_ = GeneratedMessageLite.emptyIntList();

        private SatelliteProviderCapabilityProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public boolean hasCarrierPlmn() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public String getCarrierPlmn() {
            return this.carrierPlmn_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public ByteString getCarrierPlmnBytes() {
            return ByteString.copyFromUtf8(this.carrierPlmn_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierPlmn(String str) {
            str.getClass();
            this.bitField0_ |= 1;
            this.carrierPlmn_ = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierPlmn() {
            this.bitField0_ &= -2;
            this.carrierPlmn_ = getDefaultInstance().getCarrierPlmn();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierPlmnBytes(ByteString byteString) {
            this.carrierPlmn_ = byteString.toStringUtf8();
            this.bitField0_ |= 1;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public List<Integer> getAllowedServicesList() {
            return this.allowedServices_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public int getAllowedServicesCount() {
            return this.allowedServices_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public int getAllowedServices(int i) {
            return this.allowedServices_.getInt(i);
        }

        private void ensureAllowedServicesIsMutable() {
            Internal.IntList intList = this.allowedServices_;
            if (intList.isModifiable()) {
                return;
            }
            this.allowedServices_ = GeneratedMessageLite.mutableCopy(intList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAllowedServices(int i, int i2) {
            ensureAllowedServicesIsMutable();
            this.allowedServices_.setInt(i, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllowedServices(int i) {
            ensureAllowedServicesIsMutable();
            this.allowedServices_.addInt(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllAllowedServices(Iterable iterable) {
            ensureAllowedServicesIsMutable();
            AbstractMessageLite.addAll(iterable, this.allowedServices_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearAllowedServices() {
            this.allowedServices_ = GeneratedMessageLite.emptyIntList();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public boolean hasNtnConnectType() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
        public int getNtnConnectType() {
            return this.ntnConnectType_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setNtnConnectType(int i) {
            this.bitField0_ |= 2;
            this.ntnConnectType_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearNtnConnectType() {
            this.bitField0_ &= -3;
            this.ntnConnectType_ = 0;
        }

        public static SatelliteProviderCapabilityProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SatelliteProviderCapabilityProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SatelliteProviderCapabilityProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SatelliteProviderCapabilityProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SatelliteProviderCapabilityProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SatelliteProviderCapabilityProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static SatelliteProviderCapabilityProto parseFrom(InputStream inputStream) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteProviderCapabilityProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteProviderCapabilityProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteProviderCapabilityProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteProviderCapabilityProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SatelliteProviderCapabilityProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteProviderCapabilityProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SatelliteProviderCapabilityProto satelliteProviderCapabilityProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(satelliteProviderCapabilityProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SatelliteProviderCapabilityProto, Builder> implements SatelliteProviderCapabilityProtoOrBuilder {
            private Builder() {
                super(SatelliteProviderCapabilityProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public boolean hasCarrierPlmn() {
                return ((SatelliteProviderCapabilityProto) this.instance).hasCarrierPlmn();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public String getCarrierPlmn() {
                return ((SatelliteProviderCapabilityProto) this.instance).getCarrierPlmn();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public ByteString getCarrierPlmnBytes() {
                return ((SatelliteProviderCapabilityProto) this.instance).getCarrierPlmnBytes();
            }

            public Builder setCarrierPlmn(String str) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).setCarrierPlmn(str);
                return this;
            }

            public Builder clearCarrierPlmn() {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).clearCarrierPlmn();
                return this;
            }

            public Builder setCarrierPlmnBytes(ByteString byteString) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).setCarrierPlmnBytes(byteString);
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public List<Integer> getAllowedServicesList() {
                return Collections.unmodifiableList(((SatelliteProviderCapabilityProto) this.instance).getAllowedServicesList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public int getAllowedServicesCount() {
                return ((SatelliteProviderCapabilityProto) this.instance).getAllowedServicesCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public int getAllowedServices(int i) {
                return ((SatelliteProviderCapabilityProto) this.instance).getAllowedServices(i);
            }

            public Builder setAllowedServices(int i, int i2) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).setAllowedServices(i, i2);
                return this;
            }

            public Builder addAllowedServices(int i) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).addAllowedServices(i);
                return this;
            }

            public Builder addAllAllowedServices(Iterable<? extends Integer> iterable) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).addAllAllowedServices(iterable);
                return this;
            }

            public Builder clearAllowedServices() {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).clearAllowedServices();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public boolean hasNtnConnectType() {
                return ((SatelliteProviderCapabilityProto) this.instance).hasNtnConnectType();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteProviderCapabilityProtoOrBuilder
            public int getNtnConnectType() {
                return ((SatelliteProviderCapabilityProto) this.instance).getNtnConnectType();
            }

            public Builder setNtnConnectType(int i) {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).setNtnConnectType(i);
                return this;
            }

            public Builder clearNtnConnectType() {
                copyOnWrite();
                ((SatelliteProviderCapabilityProto) this.instance).clearNtnConnectType();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new SatelliteProviderCapabilityProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001ဈ\u0000\u0002\u0016\u0003င\u0001", new Object[]{"bitField0_", "carrierPlmn_", "allowedServices_", "ntnConnectType_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<SatelliteProviderCapabilityProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (SatelliteProviderCapabilityProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            SatelliteProviderCapabilityProto satelliteProviderCapabilityProto = new SatelliteProviderCapabilityProto();
            DEFAULT_INSTANCE = satelliteProviderCapabilityProto;
            GeneratedMessageLite.registerDefaultInstance(SatelliteProviderCapabilityProto.class, satelliteProviderCapabilityProto);
        }

        public static SatelliteProviderCapabilityProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SatelliteProviderCapabilityProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class SatelliteRegionProto extends GeneratedMessageLite<SatelliteRegionProto, Builder> implements SatelliteRegionProtoOrBuilder {
        public static final int COUNTRY_CODES_FIELD_NUMBER = 2;
        private static final SatelliteRegionProto DEFAULT_INSTANCE;
        public static final int IS_ALLOWED_FIELD_NUMBER = 3;
        private static volatile Parser<SatelliteRegionProto> PARSER = null;
        public static final int S2_CELL_FILE_FIELD_NUMBER = 1;
        public static final int SATELLITE_ACCESS_CONFIG_FILE_FIELD_NUMBER = 4;
        private int bitField0_;
        private Internal.ProtobufList<String> countryCodes_;
        private boolean isAllowed_;
        private ByteString s2CellFile_;
        private ByteString satelliteAccessConfigFile_;

        private SatelliteRegionProto() {
            ByteString byteString = ByteString.EMPTY;
            this.s2CellFile_ = byteString;
            this.countryCodes_ = GeneratedMessageLite.emptyProtobufList();
            this.satelliteAccessConfigFile_ = byteString;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public boolean hasS2CellFile() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public ByteString getS2CellFile() {
            return this.s2CellFile_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setS2CellFile(ByteString byteString) {
            byteString.getClass();
            this.bitField0_ |= 1;
            this.s2CellFile_ = byteString;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearS2CellFile() {
            this.bitField0_ &= -2;
            this.s2CellFile_ = getDefaultInstance().getS2CellFile();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public List<String> getCountryCodesList() {
            return this.countryCodes_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public int getCountryCodesCount() {
            return this.countryCodes_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public String getCountryCodes(int i) {
            return this.countryCodes_.get(i);
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public ByteString getCountryCodesBytes(int i) {
            return ByteString.copyFromUtf8(this.countryCodes_.get(i));
        }

        private void ensureCountryCodesIsMutable() {
            Internal.ProtobufList<String> protobufList = this.countryCodes_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.countryCodes_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCountryCodes(int i, String str) {
            str.getClass();
            ensureCountryCodesIsMutable();
            this.countryCodes_.set(i, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCountryCodes(String str) {
            str.getClass();
            ensureCountryCodesIsMutable();
            this.countryCodes_.add(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCountryCodes(Iterable iterable) {
            ensureCountryCodesIsMutable();
            AbstractMessageLite.addAll(iterable, this.countryCodes_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCountryCodes() {
            this.countryCodes_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCountryCodesBytes(ByteString byteString) {
            ensureCountryCodesIsMutable();
            this.countryCodes_.add(byteString.toStringUtf8());
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public boolean hasIsAllowed() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public boolean getIsAllowed() {
            return this.isAllowed_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setIsAllowed(boolean z) {
            this.bitField0_ |= 2;
            this.isAllowed_ = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearIsAllowed() {
            this.bitField0_ &= -3;
            this.isAllowed_ = false;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public boolean hasSatelliteAccessConfigFile() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
        public ByteString getSatelliteAccessConfigFile() {
            return this.satelliteAccessConfigFile_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSatelliteAccessConfigFile(ByteString byteString) {
            byteString.getClass();
            this.bitField0_ |= 4;
            this.satelliteAccessConfigFile_ = byteString;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSatelliteAccessConfigFile() {
            this.bitField0_ &= -5;
            this.satelliteAccessConfigFile_ = getDefaultInstance().getSatelliteAccessConfigFile();
        }

        public static SatelliteRegionProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SatelliteRegionProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SatelliteRegionProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SatelliteRegionProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SatelliteRegionProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SatelliteRegionProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static SatelliteRegionProto parseFrom(InputStream inputStream) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteRegionProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteRegionProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SatelliteRegionProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SatelliteRegionProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SatelliteRegionProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SatelliteRegionProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SatelliteRegionProto satelliteRegionProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(satelliteRegionProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SatelliteRegionProto, Builder> implements SatelliteRegionProtoOrBuilder {
            private Builder() {
                super(SatelliteRegionProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public boolean hasS2CellFile() {
                return ((SatelliteRegionProto) this.instance).hasS2CellFile();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public ByteString getS2CellFile() {
                return ((SatelliteRegionProto) this.instance).getS2CellFile();
            }

            public Builder setS2CellFile(ByteString byteString) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).setS2CellFile(byteString);
                return this;
            }

            public Builder clearS2CellFile() {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).clearS2CellFile();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public List<String> getCountryCodesList() {
                return Collections.unmodifiableList(((SatelliteRegionProto) this.instance).getCountryCodesList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public int getCountryCodesCount() {
                return ((SatelliteRegionProto) this.instance).getCountryCodesCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public String getCountryCodes(int i) {
                return ((SatelliteRegionProto) this.instance).getCountryCodes(i);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public ByteString getCountryCodesBytes(int i) {
                return ((SatelliteRegionProto) this.instance).getCountryCodesBytes(i);
            }

            public Builder setCountryCodes(int i, String str) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).setCountryCodes(i, str);
                return this;
            }

            public Builder addCountryCodes(String str) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).addCountryCodes(str);
                return this;
            }

            public Builder addAllCountryCodes(Iterable<String> iterable) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).addAllCountryCodes(iterable);
                return this;
            }

            public Builder clearCountryCodes() {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).clearCountryCodes();
                return this;
            }

            public Builder addCountryCodesBytes(ByteString byteString) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).addCountryCodesBytes(byteString);
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public boolean hasIsAllowed() {
                return ((SatelliteRegionProto) this.instance).hasIsAllowed();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public boolean getIsAllowed() {
                return ((SatelliteRegionProto) this.instance).getIsAllowed();
            }

            public Builder setIsAllowed(boolean z) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).setIsAllowed(z);
                return this;
            }

            public Builder clearIsAllowed() {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).clearIsAllowed();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public boolean hasSatelliteAccessConfigFile() {
                return ((SatelliteRegionProto) this.instance).hasSatelliteAccessConfigFile();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.SatelliteRegionProtoOrBuilder
            public ByteString getSatelliteAccessConfigFile() {
                return ((SatelliteRegionProto) this.instance).getSatelliteAccessConfigFile();
            }

            public Builder setSatelliteAccessConfigFile(ByteString byteString) {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).setSatelliteAccessConfigFile(byteString);
                return this;
            }

            public Builder clearSatelliteAccessConfigFile() {
                copyOnWrite();
                ((SatelliteRegionProto) this.instance).clearSatelliteAccessConfigFile();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new SatelliteRegionProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001ည\u0000\u0002\u001a\u0003ဇ\u0001\u0004ည\u0002", new Object[]{"bitField0_", "s2CellFile_", "countryCodes_", "isAllowed_", "satelliteAccessConfigFile_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<SatelliteRegionProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (SatelliteRegionProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            SatelliteRegionProto satelliteRegionProto = new SatelliteRegionProto();
            DEFAULT_INSTANCE = satelliteRegionProto;
            GeneratedMessageLite.registerDefaultInstance(SatelliteRegionProto.class, satelliteRegionProto);
        }

        public static SatelliteRegionProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SatelliteRegionProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class DataConfigProto extends GeneratedMessageLite<DataConfigProto, Builder> implements DataConfigProtoOrBuilder {
        public static final int CONNECTION_CAPABILITY_CONFIGS_FIELD_NUMBER = 2;
        private static final DataConfigProto DEFAULT_INSTANCE;
        public static final int HOME_METERED_CAPABILITY_CONFIGS_FIELD_NUMBER = 3;
        private static volatile Parser<DataConfigProto> PARSER = null;
        public static final int ROAM_METERED_CAPABILITY_CONFIGS_FIELD_NUMBER = 4;
        public static final int VERSION_FIELD_NUMBER = 1;
        private int bitField0_;
        private ConnectionCapabilityConfig connectionCapabilityConfigs_;
        private MeteredCapabilityConfig homeMeteredCapabilityConfigs_;
        private MeteredCapabilityConfig roamMeteredCapabilityConfigs_;
        private int version_;

        private DataConfigProto() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public int getVersion() {
            return this.version_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setVersion(int i) {
            this.bitField0_ |= 1;
            this.version_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearVersion() {
            this.bitField0_ &= -2;
            this.version_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public boolean hasConnectionCapabilityConfigs() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public ConnectionCapabilityConfig getConnectionCapabilityConfigs() {
            ConnectionCapabilityConfig connectionCapabilityConfig = this.connectionCapabilityConfigs_;
            return connectionCapabilityConfig == null ? ConnectionCapabilityConfig.getDefaultInstance() : connectionCapabilityConfig;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setConnectionCapabilityConfigs(ConnectionCapabilityConfig connectionCapabilityConfig) {
            connectionCapabilityConfig.getClass();
            this.connectionCapabilityConfigs_ = connectionCapabilityConfig;
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeConnectionCapabilityConfigs(ConnectionCapabilityConfig connectionCapabilityConfig) {
            connectionCapabilityConfig.getClass();
            ConnectionCapabilityConfig connectionCapabilityConfig2 = this.connectionCapabilityConfigs_;
            if (connectionCapabilityConfig2 != null && connectionCapabilityConfig2 != ConnectionCapabilityConfig.getDefaultInstance()) {
                this.connectionCapabilityConfigs_ = ConnectionCapabilityConfig.newBuilder(this.connectionCapabilityConfigs_).mergeFrom(connectionCapabilityConfig).buildPartial();
            } else {
                this.connectionCapabilityConfigs_ = connectionCapabilityConfig;
            }
            this.bitField0_ |= 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearConnectionCapabilityConfigs() {
            this.connectionCapabilityConfigs_ = null;
            this.bitField0_ &= -3;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public boolean hasHomeMeteredCapabilityConfigs() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public MeteredCapabilityConfig getHomeMeteredCapabilityConfigs() {
            MeteredCapabilityConfig meteredCapabilityConfig = this.homeMeteredCapabilityConfigs_;
            return meteredCapabilityConfig == null ? MeteredCapabilityConfig.getDefaultInstance() : meteredCapabilityConfig;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setHomeMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
            meteredCapabilityConfig.getClass();
            this.homeMeteredCapabilityConfigs_ = meteredCapabilityConfig;
            this.bitField0_ |= 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeHomeMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
            meteredCapabilityConfig.getClass();
            MeteredCapabilityConfig meteredCapabilityConfig2 = this.homeMeteredCapabilityConfigs_;
            if (meteredCapabilityConfig2 != null && meteredCapabilityConfig2 != MeteredCapabilityConfig.getDefaultInstance()) {
                this.homeMeteredCapabilityConfigs_ = MeteredCapabilityConfig.newBuilder(this.homeMeteredCapabilityConfigs_).mergeFrom(meteredCapabilityConfig).buildPartial();
            } else {
                this.homeMeteredCapabilityConfigs_ = meteredCapabilityConfig;
            }
            this.bitField0_ |= 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearHomeMeteredCapabilityConfigs() {
            this.homeMeteredCapabilityConfigs_ = null;
            this.bitField0_ &= -5;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public boolean hasRoamMeteredCapabilityConfigs() {
            return (this.bitField0_ & 8) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
        public MeteredCapabilityConfig getRoamMeteredCapabilityConfigs() {
            MeteredCapabilityConfig meteredCapabilityConfig = this.roamMeteredCapabilityConfigs_;
            return meteredCapabilityConfig == null ? MeteredCapabilityConfig.getDefaultInstance() : meteredCapabilityConfig;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRoamMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
            meteredCapabilityConfig.getClass();
            this.roamMeteredCapabilityConfigs_ = meteredCapabilityConfig;
            this.bitField0_ |= 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeRoamMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
            meteredCapabilityConfig.getClass();
            MeteredCapabilityConfig meteredCapabilityConfig2 = this.roamMeteredCapabilityConfigs_;
            if (meteredCapabilityConfig2 != null && meteredCapabilityConfig2 != MeteredCapabilityConfig.getDefaultInstance()) {
                this.roamMeteredCapabilityConfigs_ = MeteredCapabilityConfig.newBuilder(this.roamMeteredCapabilityConfigs_).mergeFrom(meteredCapabilityConfig).buildPartial();
            } else {
                this.roamMeteredCapabilityConfigs_ = meteredCapabilityConfig;
            }
            this.bitField0_ |= 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearRoamMeteredCapabilityConfigs() {
            this.roamMeteredCapabilityConfigs_ = null;
            this.bitField0_ &= -9;
        }

        public static DataConfigProto parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static DataConfigProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static DataConfigProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static DataConfigProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static DataConfigProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static DataConfigProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static DataConfigProto parseFrom(InputStream inputStream) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static DataConfigProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static DataConfigProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static DataConfigProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static DataConfigProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static DataConfigProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (DataConfigProto) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(DataConfigProto dataConfigProto) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(dataConfigProto);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<DataConfigProto, Builder> implements DataConfigProtoOrBuilder {
            private Builder() {
                super(DataConfigProto.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public boolean hasVersion() {
                return ((DataConfigProto) this.instance).hasVersion();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public int getVersion() {
                return ((DataConfigProto) this.instance).getVersion();
            }

            public Builder setVersion(int i) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setVersion(i);
                return this;
            }

            public Builder clearVersion() {
                copyOnWrite();
                ((DataConfigProto) this.instance).clearVersion();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public boolean hasConnectionCapabilityConfigs() {
                return ((DataConfigProto) this.instance).hasConnectionCapabilityConfigs();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public ConnectionCapabilityConfig getConnectionCapabilityConfigs() {
                return ((DataConfigProto) this.instance).getConnectionCapabilityConfigs();
            }

            public Builder setConnectionCapabilityConfigs(ConnectionCapabilityConfig connectionCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setConnectionCapabilityConfigs(connectionCapabilityConfig);
                return this;
            }

            public Builder setConnectionCapabilityConfigs(ConnectionCapabilityConfig.Builder builder) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setConnectionCapabilityConfigs(builder.build());
                return this;
            }

            public Builder mergeConnectionCapabilityConfigs(ConnectionCapabilityConfig connectionCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).mergeConnectionCapabilityConfigs(connectionCapabilityConfig);
                return this;
            }

            public Builder clearConnectionCapabilityConfigs() {
                copyOnWrite();
                ((DataConfigProto) this.instance).clearConnectionCapabilityConfigs();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public boolean hasHomeMeteredCapabilityConfigs() {
                return ((DataConfigProto) this.instance).hasHomeMeteredCapabilityConfigs();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public MeteredCapabilityConfig getHomeMeteredCapabilityConfigs() {
                return ((DataConfigProto) this.instance).getHomeMeteredCapabilityConfigs();
            }

            public Builder setHomeMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setHomeMeteredCapabilityConfigs(meteredCapabilityConfig);
                return this;
            }

            public Builder setHomeMeteredCapabilityConfigs(MeteredCapabilityConfig.Builder builder) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setHomeMeteredCapabilityConfigs(builder.build());
                return this;
            }

            public Builder mergeHomeMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).mergeHomeMeteredCapabilityConfigs(meteredCapabilityConfig);
                return this;
            }

            public Builder clearHomeMeteredCapabilityConfigs() {
                copyOnWrite();
                ((DataConfigProto) this.instance).clearHomeMeteredCapabilityConfigs();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public boolean hasRoamMeteredCapabilityConfigs() {
                return ((DataConfigProto) this.instance).hasRoamMeteredCapabilityConfigs();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.DataConfigProtoOrBuilder
            public MeteredCapabilityConfig getRoamMeteredCapabilityConfigs() {
                return ((DataConfigProto) this.instance).getRoamMeteredCapabilityConfigs();
            }

            public Builder setRoamMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setRoamMeteredCapabilityConfigs(meteredCapabilityConfig);
                return this;
            }

            public Builder setRoamMeteredCapabilityConfigs(MeteredCapabilityConfig.Builder builder) {
                copyOnWrite();
                ((DataConfigProto) this.instance).setRoamMeteredCapabilityConfigs(builder.build());
                return this;
            }

            public Builder mergeRoamMeteredCapabilityConfigs(MeteredCapabilityConfig meteredCapabilityConfig) {
                copyOnWrite();
                ((DataConfigProto) this.instance).mergeRoamMeteredCapabilityConfigs(meteredCapabilityConfig);
                return this;
            }

            public Builder clearRoamMeteredCapabilityConfigs() {
                copyOnWrite();
                ((DataConfigProto) this.instance).clearRoamMeteredCapabilityConfigs();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new DataConfigProto();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001င\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဉ\u0003", new Object[]{"bitField0_", "version_", "connectionCapabilityConfigs_", "homeMeteredCapabilityConfigs_", "roamMeteredCapabilityConfigs_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<DataConfigProto> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (DataConfigProto.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            DataConfigProto dataConfigProto = new DataConfigProto();
            DEFAULT_INSTANCE = dataConfigProto;
            GeneratedMessageLite.registerDefaultInstance(DataConfigProto.class, dataConfigProto);
        }

        public static DataConfigProto getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<DataConfigProto> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class ConnectionCapabilityConfig extends GeneratedMessageLite<ConnectionCapabilityConfig, Builder> implements ConnectionCapabilityConfigOrBuilder {
        public static final int CARRIER_CONNECTION_CAPABILITY_CONFIGS_FIELD_NUMBER = 2;
        public static final int DEFAULT_CONNECTION_CAPABILITY_CONFIG_FIELD_NUMBER = 1;
        private static final ConnectionCapabilityConfig DEFAULT_INSTANCE;
        private static volatile Parser<ConnectionCapabilityConfig> PARSER;
        private int bitField0_;
        private Internal.ProtobufList<ConnectionCapabilityMap> carrierConnectionCapabilityConfigs_ = GeneratedMessageLite.emptyProtobufList();
        private ConnectionCapabilityMap defaultConnectionCapabilityConfig_;

        private ConnectionCapabilityConfig() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
        public boolean hasDefaultConnectionCapabilityConfig() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
        public ConnectionCapabilityMap getDefaultConnectionCapabilityConfig() {
            ConnectionCapabilityMap connectionCapabilityMap = this.defaultConnectionCapabilityConfig_;
            return connectionCapabilityMap == null ? ConnectionCapabilityMap.getDefaultInstance() : connectionCapabilityMap;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDefaultConnectionCapabilityConfig(ConnectionCapabilityMap connectionCapabilityMap) {
            connectionCapabilityMap.getClass();
            this.defaultConnectionCapabilityConfig_ = connectionCapabilityMap;
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeDefaultConnectionCapabilityConfig(ConnectionCapabilityMap connectionCapabilityMap) {
            connectionCapabilityMap.getClass();
            ConnectionCapabilityMap connectionCapabilityMap2 = this.defaultConnectionCapabilityConfig_;
            if (connectionCapabilityMap2 != null && connectionCapabilityMap2 != ConnectionCapabilityMap.getDefaultInstance()) {
                this.defaultConnectionCapabilityConfig_ = ConnectionCapabilityMap.newBuilder(this.defaultConnectionCapabilityConfig_).mergeFrom(connectionCapabilityMap).buildPartial();
            } else {
                this.defaultConnectionCapabilityConfig_ = connectionCapabilityMap;
            }
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearDefaultConnectionCapabilityConfig() {
            this.defaultConnectionCapabilityConfig_ = null;
            this.bitField0_ &= -2;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
        public List<ConnectionCapabilityMap> getCarrierConnectionCapabilityConfigsList() {
            return this.carrierConnectionCapabilityConfigs_;
        }

        public List<? extends ConnectionCapabilityMapOrBuilder> getCarrierConnectionCapabilityConfigsOrBuilderList() {
            return this.carrierConnectionCapabilityConfigs_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
        public int getCarrierConnectionCapabilityConfigsCount() {
            return this.carrierConnectionCapabilityConfigs_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
        public ConnectionCapabilityMap getCarrierConnectionCapabilityConfigs(int i) {
            return this.carrierConnectionCapabilityConfigs_.get(i);
        }

        public ConnectionCapabilityMapOrBuilder getCarrierConnectionCapabilityConfigsOrBuilder(int i) {
            return this.carrierConnectionCapabilityConfigs_.get(i);
        }

        private void ensureCarrierConnectionCapabilityConfigsIsMutable() {
            Internal.ProtobufList<ConnectionCapabilityMap> protobufList = this.carrierConnectionCapabilityConfigs_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.carrierConnectionCapabilityConfigs_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap connectionCapabilityMap) {
            connectionCapabilityMap.getClass();
            ensureCarrierConnectionCapabilityConfigsIsMutable();
            this.carrierConnectionCapabilityConfigs_.set(i, connectionCapabilityMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierConnectionCapabilityConfigs(ConnectionCapabilityMap connectionCapabilityMap) {
            connectionCapabilityMap.getClass();
            ensureCarrierConnectionCapabilityConfigsIsMutable();
            this.carrierConnectionCapabilityConfigs_.add(connectionCapabilityMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap connectionCapabilityMap) {
            connectionCapabilityMap.getClass();
            ensureCarrierConnectionCapabilityConfigsIsMutable();
            this.carrierConnectionCapabilityConfigs_.add(i, connectionCapabilityMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCarrierConnectionCapabilityConfigs(Iterable iterable) {
            ensureCarrierConnectionCapabilityConfigsIsMutable();
            AbstractMessageLite.addAll(iterable, this.carrierConnectionCapabilityConfigs_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierConnectionCapabilityConfigs() {
            this.carrierConnectionCapabilityConfigs_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeCarrierConnectionCapabilityConfigs(int i) {
            ensureCarrierConnectionCapabilityConfigsIsMutable();
            this.carrierConnectionCapabilityConfigs_.remove(i);
        }

        public static ConnectionCapabilityConfig parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static ConnectionCapabilityConfig parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static ConnectionCapabilityConfig parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static ConnectionCapabilityConfig parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static ConnectionCapabilityConfig parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static ConnectionCapabilityConfig parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static ConnectionCapabilityConfig parseFrom(InputStream inputStream) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectionCapabilityConfig parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectionCapabilityConfig parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectionCapabilityConfig parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectionCapabilityConfig parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static ConnectionCapabilityConfig parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(ConnectionCapabilityConfig connectionCapabilityConfig) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(connectionCapabilityConfig);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ConnectionCapabilityConfig, Builder> implements ConnectionCapabilityConfigOrBuilder {
            private Builder() {
                super(ConnectionCapabilityConfig.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
            public boolean hasDefaultConnectionCapabilityConfig() {
                return ((ConnectionCapabilityConfig) this.instance).hasDefaultConnectionCapabilityConfig();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
            public ConnectionCapabilityMap getDefaultConnectionCapabilityConfig() {
                return ((ConnectionCapabilityConfig) this.instance).getDefaultConnectionCapabilityConfig();
            }

            public Builder setDefaultConnectionCapabilityConfig(ConnectionCapabilityMap connectionCapabilityMap) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).setDefaultConnectionCapabilityConfig(connectionCapabilityMap);
                return this;
            }

            public Builder setDefaultConnectionCapabilityConfig(ConnectionCapabilityMap.Builder builder) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).setDefaultConnectionCapabilityConfig(builder.build());
                return this;
            }

            public Builder mergeDefaultConnectionCapabilityConfig(ConnectionCapabilityMap connectionCapabilityMap) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).mergeDefaultConnectionCapabilityConfig(connectionCapabilityMap);
                return this;
            }

            public Builder clearDefaultConnectionCapabilityConfig() {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).clearDefaultConnectionCapabilityConfig();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
            public List<ConnectionCapabilityMap> getCarrierConnectionCapabilityConfigsList() {
                return Collections.unmodifiableList(((ConnectionCapabilityConfig) this.instance).getCarrierConnectionCapabilityConfigsList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
            public int getCarrierConnectionCapabilityConfigsCount() {
                return ((ConnectionCapabilityConfig) this.instance).getCarrierConnectionCapabilityConfigsCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityConfigOrBuilder
            public ConnectionCapabilityMap getCarrierConnectionCapabilityConfigs(int i) {
                return ((ConnectionCapabilityConfig) this.instance).getCarrierConnectionCapabilityConfigs(i);
            }

            public Builder setCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap connectionCapabilityMap) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).setCarrierConnectionCapabilityConfigs(i, connectionCapabilityMap);
                return this;
            }

            public Builder setCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap.Builder builder) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).setCarrierConnectionCapabilityConfigs(i, builder.build());
                return this;
            }

            public Builder addCarrierConnectionCapabilityConfigs(ConnectionCapabilityMap connectionCapabilityMap) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).addCarrierConnectionCapabilityConfigs(connectionCapabilityMap);
                return this;
            }

            public Builder addCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap connectionCapabilityMap) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).addCarrierConnectionCapabilityConfigs(i, connectionCapabilityMap);
                return this;
            }

            public Builder addCarrierConnectionCapabilityConfigs(ConnectionCapabilityMap.Builder builder) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).addCarrierConnectionCapabilityConfigs(builder.build());
                return this;
            }

            public Builder addCarrierConnectionCapabilityConfigs(int i, ConnectionCapabilityMap.Builder builder) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).addCarrierConnectionCapabilityConfigs(i, builder.build());
                return this;
            }

            public Builder addAllCarrierConnectionCapabilityConfigs(Iterable<? extends ConnectionCapabilityMap> iterable) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).addAllCarrierConnectionCapabilityConfigs(iterable);
                return this;
            }

            public Builder clearCarrierConnectionCapabilityConfigs() {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).clearCarrierConnectionCapabilityConfigs();
                return this;
            }

            public Builder removeCarrierConnectionCapabilityConfigs(int i) {
                copyOnWrite();
                ((ConnectionCapabilityConfig) this.instance).removeCarrierConnectionCapabilityConfigs(i);
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new ConnectionCapabilityConfig();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001ဉ\u0000\u0002\u001b", new Object[]{"bitField0_", "defaultConnectionCapabilityConfig_", "carrierConnectionCapabilityConfigs_", ConnectionCapabilityMap.class});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<ConnectionCapabilityConfig> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (ConnectionCapabilityConfig.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            ConnectionCapabilityConfig connectionCapabilityConfig = new ConnectionCapabilityConfig();
            DEFAULT_INSTANCE = connectionCapabilityConfig;
            GeneratedMessageLite.registerDefaultInstance(ConnectionCapabilityConfig.class, connectionCapabilityConfig);
        }

        public static ConnectionCapabilityConfig getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ConnectionCapabilityConfig> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class ConnectionCapabilityMap extends GeneratedMessageLite<ConnectionCapabilityMap, Builder> implements ConnectionCapabilityMapOrBuilder {
        public static final int CARRIER_ID_FIELD_NUMBER = 1;
        private static final ConnectionCapabilityMap DEFAULT_INSTANCE;
        private static volatile Parser<ConnectionCapabilityMap> PARSER = null;
        public static final int RULES_FIELD_NUMBER = 2;
        private int bitField0_;
        private int carrierId_;
        private Internal.ProtobufList<String> rules_ = GeneratedMessageLite.emptyProtobufList();

        private ConnectionCapabilityMap() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public boolean hasCarrierId() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public int getCarrierId() {
            return this.carrierId_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierId(int i) {
            this.bitField0_ |= 1;
            this.carrierId_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierId() {
            this.bitField0_ &= -2;
            this.carrierId_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public List<String> getRulesList() {
            return this.rules_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public int getRulesCount() {
            return this.rules_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public String getRules(int i) {
            return this.rules_.get(i);
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
        public ByteString getRulesBytes(int i) {
            return ByteString.copyFromUtf8(this.rules_.get(i));
        }

        private void ensureRulesIsMutable() {
            Internal.ProtobufList<String> protobufList = this.rules_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.rules_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRules(int i, String str) {
            str.getClass();
            ensureRulesIsMutable();
            this.rules_.set(i, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addRules(String str) {
            str.getClass();
            ensureRulesIsMutable();
            this.rules_.add(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllRules(Iterable iterable) {
            ensureRulesIsMutable();
            AbstractMessageLite.addAll(iterable, this.rules_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearRules() {
            this.rules_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addRulesBytes(ByteString byteString) {
            ensureRulesIsMutable();
            this.rules_.add(byteString.toStringUtf8());
        }

        public static ConnectionCapabilityMap parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static ConnectionCapabilityMap parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static ConnectionCapabilityMap parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static ConnectionCapabilityMap parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static ConnectionCapabilityMap parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static ConnectionCapabilityMap parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static ConnectionCapabilityMap parseFrom(InputStream inputStream) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectionCapabilityMap parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectionCapabilityMap parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectionCapabilityMap parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectionCapabilityMap parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static ConnectionCapabilityMap parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ConnectionCapabilityMap) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(ConnectionCapabilityMap connectionCapabilityMap) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(connectionCapabilityMap);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ConnectionCapabilityMap, Builder> implements ConnectionCapabilityMapOrBuilder {
            private Builder() {
                super(ConnectionCapabilityMap.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public boolean hasCarrierId() {
                return ((ConnectionCapabilityMap) this.instance).hasCarrierId();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public int getCarrierId() {
                return ((ConnectionCapabilityMap) this.instance).getCarrierId();
            }

            public Builder setCarrierId(int i) {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).setCarrierId(i);
                return this;
            }

            public Builder clearCarrierId() {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).clearCarrierId();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public List<String> getRulesList() {
                return Collections.unmodifiableList(((ConnectionCapabilityMap) this.instance).getRulesList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public int getRulesCount() {
                return ((ConnectionCapabilityMap) this.instance).getRulesCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public String getRules(int i) {
                return ((ConnectionCapabilityMap) this.instance).getRules(i);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.ConnectionCapabilityMapOrBuilder
            public ByteString getRulesBytes(int i) {
                return ((ConnectionCapabilityMap) this.instance).getRulesBytes(i);
            }

            public Builder setRules(int i, String str) {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).setRules(i, str);
                return this;
            }

            public Builder addRules(String str) {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).addRules(str);
                return this;
            }

            public Builder addAllRules(Iterable<String> iterable) {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).addAllRules(iterable);
                return this;
            }

            public Builder clearRules() {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).clearRules();
                return this;
            }

            public Builder addRulesBytes(ByteString byteString) {
                copyOnWrite();
                ((ConnectionCapabilityMap) this.instance).addRulesBytes(byteString);
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new ConnectionCapabilityMap();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001င\u0000\u0002\u001a", new Object[]{"bitField0_", "carrierId_", "rules_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<ConnectionCapabilityMap> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (ConnectionCapabilityMap.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            ConnectionCapabilityMap connectionCapabilityMap = new ConnectionCapabilityMap();
            DEFAULT_INSTANCE = connectionCapabilityMap;
            GeneratedMessageLite.registerDefaultInstance(ConnectionCapabilityMap.class, connectionCapabilityMap);
        }

        public static ConnectionCapabilityMap getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ConnectionCapabilityMap> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class MeteredCapabilityConfig extends GeneratedMessageLite<MeteredCapabilityConfig, Builder> implements MeteredCapabilityConfigOrBuilder {
        public static final int CARRIER_METERED_CAPABILITY_CONFIGS_FIELD_NUMBER = 2;
        private static final MeteredCapabilityConfig DEFAULT_INSTANCE;
        public static final int DEFAULT_METERED_CAPABILITY_CONFIG_FIELD_NUMBER = 1;
        private static volatile Parser<MeteredCapabilityConfig> PARSER;
        private int bitField0_;
        private Internal.ProtobufList<MeteredCapabilities> carrierMeteredCapabilityConfigs_ = GeneratedMessageLite.emptyProtobufList();
        private MeteredCapabilities defaultMeteredCapabilityConfig_;

        private MeteredCapabilityConfig() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
        public boolean hasDefaultMeteredCapabilityConfig() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
        public MeteredCapabilities getDefaultMeteredCapabilityConfig() {
            MeteredCapabilities meteredCapabilities = this.defaultMeteredCapabilityConfig_;
            return meteredCapabilities == null ? MeteredCapabilities.getDefaultInstance() : meteredCapabilities;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDefaultMeteredCapabilityConfig(MeteredCapabilities meteredCapabilities) {
            meteredCapabilities.getClass();
            this.defaultMeteredCapabilityConfig_ = meteredCapabilities;
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeDefaultMeteredCapabilityConfig(MeteredCapabilities meteredCapabilities) {
            meteredCapabilities.getClass();
            MeteredCapabilities meteredCapabilities2 = this.defaultMeteredCapabilityConfig_;
            if (meteredCapabilities2 != null && meteredCapabilities2 != MeteredCapabilities.getDefaultInstance()) {
                this.defaultMeteredCapabilityConfig_ = MeteredCapabilities.newBuilder(this.defaultMeteredCapabilityConfig_).mergeFrom(meteredCapabilities).buildPartial();
            } else {
                this.defaultMeteredCapabilityConfig_ = meteredCapabilities;
            }
            this.bitField0_ |= 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearDefaultMeteredCapabilityConfig() {
            this.defaultMeteredCapabilityConfig_ = null;
            this.bitField0_ &= -2;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
        public List<MeteredCapabilities> getCarrierMeteredCapabilityConfigsList() {
            return this.carrierMeteredCapabilityConfigs_;
        }

        public List<? extends MeteredCapabilitiesOrBuilder> getCarrierMeteredCapabilityConfigsOrBuilderList() {
            return this.carrierMeteredCapabilityConfigs_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
        public int getCarrierMeteredCapabilityConfigsCount() {
            return this.carrierMeteredCapabilityConfigs_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
        public MeteredCapabilities getCarrierMeteredCapabilityConfigs(int i) {
            return this.carrierMeteredCapabilityConfigs_.get(i);
        }

        public MeteredCapabilitiesOrBuilder getCarrierMeteredCapabilityConfigsOrBuilder(int i) {
            return this.carrierMeteredCapabilityConfigs_.get(i);
        }

        private void ensureCarrierMeteredCapabilityConfigsIsMutable() {
            Internal.ProtobufList<MeteredCapabilities> protobufList = this.carrierMeteredCapabilityConfigs_;
            if (protobufList.isModifiable()) {
                return;
            }
            this.carrierMeteredCapabilityConfigs_ = GeneratedMessageLite.mutableCopy(protobufList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities meteredCapabilities) {
            meteredCapabilities.getClass();
            ensureCarrierMeteredCapabilityConfigsIsMutable();
            this.carrierMeteredCapabilityConfigs_.set(i, meteredCapabilities);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierMeteredCapabilityConfigs(MeteredCapabilities meteredCapabilities) {
            meteredCapabilities.getClass();
            ensureCarrierMeteredCapabilityConfigsIsMutable();
            this.carrierMeteredCapabilityConfigs_.add(meteredCapabilities);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities meteredCapabilities) {
            meteredCapabilities.getClass();
            ensureCarrierMeteredCapabilityConfigsIsMutable();
            this.carrierMeteredCapabilityConfigs_.add(i, meteredCapabilities);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCarrierMeteredCapabilityConfigs(Iterable iterable) {
            ensureCarrierMeteredCapabilityConfigsIsMutable();
            AbstractMessageLite.addAll(iterable, this.carrierMeteredCapabilityConfigs_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierMeteredCapabilityConfigs() {
            this.carrierMeteredCapabilityConfigs_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeCarrierMeteredCapabilityConfigs(int i) {
            ensureCarrierMeteredCapabilityConfigsIsMutable();
            this.carrierMeteredCapabilityConfigs_.remove(i);
        }

        public static MeteredCapabilityConfig parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static MeteredCapabilityConfig parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static MeteredCapabilityConfig parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static MeteredCapabilityConfig parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static MeteredCapabilityConfig parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static MeteredCapabilityConfig parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static MeteredCapabilityConfig parseFrom(InputStream inputStream) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static MeteredCapabilityConfig parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static MeteredCapabilityConfig parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static MeteredCapabilityConfig parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static MeteredCapabilityConfig parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static MeteredCapabilityConfig parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilityConfig) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(MeteredCapabilityConfig meteredCapabilityConfig) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(meteredCapabilityConfig);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<MeteredCapabilityConfig, Builder> implements MeteredCapabilityConfigOrBuilder {
            private Builder() {
                super(MeteredCapabilityConfig.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
            public boolean hasDefaultMeteredCapabilityConfig() {
                return ((MeteredCapabilityConfig) this.instance).hasDefaultMeteredCapabilityConfig();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
            public MeteredCapabilities getDefaultMeteredCapabilityConfig() {
                return ((MeteredCapabilityConfig) this.instance).getDefaultMeteredCapabilityConfig();
            }

            public Builder setDefaultMeteredCapabilityConfig(MeteredCapabilities meteredCapabilities) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).setDefaultMeteredCapabilityConfig(meteredCapabilities);
                return this;
            }

            public Builder setDefaultMeteredCapabilityConfig(MeteredCapabilities.Builder builder) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).setDefaultMeteredCapabilityConfig(builder.build());
                return this;
            }

            public Builder mergeDefaultMeteredCapabilityConfig(MeteredCapabilities meteredCapabilities) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).mergeDefaultMeteredCapabilityConfig(meteredCapabilities);
                return this;
            }

            public Builder clearDefaultMeteredCapabilityConfig() {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).clearDefaultMeteredCapabilityConfig();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
            public List<MeteredCapabilities> getCarrierMeteredCapabilityConfigsList() {
                return Collections.unmodifiableList(((MeteredCapabilityConfig) this.instance).getCarrierMeteredCapabilityConfigsList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
            public int getCarrierMeteredCapabilityConfigsCount() {
                return ((MeteredCapabilityConfig) this.instance).getCarrierMeteredCapabilityConfigsCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilityConfigOrBuilder
            public MeteredCapabilities getCarrierMeteredCapabilityConfigs(int i) {
                return ((MeteredCapabilityConfig) this.instance).getCarrierMeteredCapabilityConfigs(i);
            }

            public Builder setCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities meteredCapabilities) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).setCarrierMeteredCapabilityConfigs(i, meteredCapabilities);
                return this;
            }

            public Builder setCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities.Builder builder) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).setCarrierMeteredCapabilityConfigs(i, builder.build());
                return this;
            }

            public Builder addCarrierMeteredCapabilityConfigs(MeteredCapabilities meteredCapabilities) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).addCarrierMeteredCapabilityConfigs(meteredCapabilities);
                return this;
            }

            public Builder addCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities meteredCapabilities) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).addCarrierMeteredCapabilityConfigs(i, meteredCapabilities);
                return this;
            }

            public Builder addCarrierMeteredCapabilityConfigs(MeteredCapabilities.Builder builder) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).addCarrierMeteredCapabilityConfigs(builder.build());
                return this;
            }

            public Builder addCarrierMeteredCapabilityConfigs(int i, MeteredCapabilities.Builder builder) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).addCarrierMeteredCapabilityConfigs(i, builder.build());
                return this;
            }

            public Builder addAllCarrierMeteredCapabilityConfigs(Iterable<? extends MeteredCapabilities> iterable) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).addAllCarrierMeteredCapabilityConfigs(iterable);
                return this;
            }

            public Builder clearCarrierMeteredCapabilityConfigs() {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).clearCarrierMeteredCapabilityConfigs();
                return this;
            }

            public Builder removeCarrierMeteredCapabilityConfigs(int i) {
                copyOnWrite();
                ((MeteredCapabilityConfig) this.instance).removeCarrierMeteredCapabilityConfigs(i);
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new MeteredCapabilityConfig();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001ဉ\u0000\u0002\u001b", new Object[]{"bitField0_", "defaultMeteredCapabilityConfig_", "carrierMeteredCapabilityConfigs_", MeteredCapabilities.class});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<MeteredCapabilityConfig> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (MeteredCapabilityConfig.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            MeteredCapabilityConfig meteredCapabilityConfig = new MeteredCapabilityConfig();
            DEFAULT_INSTANCE = meteredCapabilityConfig;
            GeneratedMessageLite.registerDefaultInstance(MeteredCapabilityConfig.class, meteredCapabilityConfig);
        }

        public static MeteredCapabilityConfig getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<MeteredCapabilityConfig> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public static final class MeteredCapabilities extends GeneratedMessageLite<MeteredCapabilities, Builder> implements MeteredCapabilitiesOrBuilder {
        public static final int CAPABILITY_IDS_FIELD_NUMBER = 2;
        public static final int CARRIER_ID_FIELD_NUMBER = 1;
        private static final MeteredCapabilities DEFAULT_INSTANCE;
        private static volatile Parser<MeteredCapabilities> PARSER;
        private int bitField0_;
        private Internal.IntList capabilityIds_ = GeneratedMessageLite.emptyIntList();
        private int carrierId_;

        private MeteredCapabilities() {
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
        public boolean hasCarrierId() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
        public int getCarrierId() {
            return this.carrierId_;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCarrierId(int i) {
            this.bitField0_ |= 1;
            this.carrierId_ = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCarrierId() {
            this.bitField0_ &= -2;
            this.carrierId_ = 0;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
        public List<Integer> getCapabilityIdsList() {
            return this.capabilityIds_;
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
        public int getCapabilityIdsCount() {
            return this.capabilityIds_.size();
        }

        @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
        public int getCapabilityIds(int i) {
            return this.capabilityIds_.getInt(i);
        }

        private void ensureCapabilityIdsIsMutable() {
            Internal.IntList intList = this.capabilityIds_;
            if (intList.isModifiable()) {
                return;
            }
            this.capabilityIds_ = GeneratedMessageLite.mutableCopy(intList);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCapabilityIds(int i, int i2) {
            ensureCapabilityIdsIsMutable();
            this.capabilityIds_.setInt(i, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCapabilityIds(int i) {
            ensureCapabilityIdsIsMutable();
            this.capabilityIds_.addInt(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAllCapabilityIds(Iterable iterable) {
            ensureCapabilityIdsIsMutable();
            AbstractMessageLite.addAll(iterable, this.capabilityIds_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCapabilityIds() {
            this.capabilityIds_ = GeneratedMessageLite.emptyIntList();
        }

        public static MeteredCapabilities parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static MeteredCapabilities parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static MeteredCapabilities parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static MeteredCapabilities parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static MeteredCapabilities parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static MeteredCapabilities parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static MeteredCapabilities parseFrom(InputStream inputStream) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static MeteredCapabilities parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static MeteredCapabilities parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static MeteredCapabilities parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static MeteredCapabilities parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static MeteredCapabilities parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (MeteredCapabilities) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(MeteredCapabilities meteredCapabilities) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(meteredCapabilities);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<MeteredCapabilities, Builder> implements MeteredCapabilitiesOrBuilder {
            private Builder() {
                super(MeteredCapabilities.DEFAULT_INSTANCE);
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
            public boolean hasCarrierId() {
                return ((MeteredCapabilities) this.instance).hasCarrierId();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
            public int getCarrierId() {
                return ((MeteredCapabilities) this.instance).getCarrierId();
            }

            public Builder setCarrierId(int i) {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).setCarrierId(i);
                return this;
            }

            public Builder clearCarrierId() {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).clearCarrierId();
                return this;
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
            public List<Integer> getCapabilityIdsList() {
                return Collections.unmodifiableList(((MeteredCapabilities) this.instance).getCapabilityIdsList());
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
            public int getCapabilityIdsCount() {
                return ((MeteredCapabilities) this.instance).getCapabilityIdsCount();
            }

            @Override // com.android.internal.telephony.TelephonyConfigData.MeteredCapabilitiesOrBuilder
            public int getCapabilityIds(int i) {
                return ((MeteredCapabilities) this.instance).getCapabilityIds(i);
            }

            public Builder setCapabilityIds(int i, int i2) {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).setCapabilityIds(i, i2);
                return this;
            }

            public Builder addCapabilityIds(int i) {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).addCapabilityIds(i);
                return this;
            }

            public Builder addAllCapabilityIds(Iterable<? extends Integer> iterable) {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).addAllCapabilityIds(iterable);
                return this;
            }

            public Builder clearCapabilityIds() {
                copyOnWrite();
                ((MeteredCapabilities) this.instance).clearCapabilityIds();
                return this;
            }
        }

        @Override // com.android.internal.telephony.protobuf.GeneratedMessageLite
        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            Parser defaultInstanceBasedParser;
            int i = AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()];
            switch (i) {
                case 1:
                    return new MeteredCapabilities();
                case 2:
                    return new Builder();
                case 3:
                    return GeneratedMessageLite.newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001င\u0000\u0002\u0016", new Object[]{"bitField0_", "carrierId_", "capabilityIds_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    Parser<MeteredCapabilities> parser = PARSER;
                    if (parser != null) {
                        return parser;
                    }
                    synchronized (MeteredCapabilities.class) {
                        try {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                        break;
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return (byte) 1;
                default:
                    throw null;
            }
        }

        static {
            MeteredCapabilities meteredCapabilities = new MeteredCapabilities();
            DEFAULT_INSTANCE = meteredCapabilities;
            GeneratedMessageLite.registerDefaultInstance(MeteredCapabilities.class, meteredCapabilities);
        }

        public static MeteredCapabilities getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<MeteredCapabilities> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }
}
