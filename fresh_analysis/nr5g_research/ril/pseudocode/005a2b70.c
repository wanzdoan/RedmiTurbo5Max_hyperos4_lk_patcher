
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool xiaomi_vendor_build_region_is_cfged(void)

{
  bool bVar1;
  bool bVar2;
  
  uRam00000000005ef565 = 0;
  _DAT_005ef55d = 0;
  uRam00000000005ef575 = 0;
  _DAT_005ef56d = 0;
  uRam00000000005ef585 = 0;
  _DAT_005ef57d = 0;
  uRam00000000005ef595 = 0;
  _DAT_005ef58d = 0;
  uRam00000000005ef5a5 = 0;
  _DAT_005ef59d = 0;
  uRam00000000005ef5b5 = 0;
  _DAT_005ef5ad = 0;
  uRam00000000005ef5c5 = 0;
  _DAT_005ef5bd = 0;
  uRam00000000005ef5d5 = 0;
  _DAT_005ef5cd = 0;
  uRam00000000005ef5e5 = 0;
  _DAT_005ef5dd = 0;
  uRam00000000005ef5f5 = 0;
  _DAT_005ef5ed = 0;
  uRam00000000005ef605 = 0;
  _DAT_005ef5fd = 0;
  uRam00000000005ef615 = 0;
  _DAT_005ef60d = 0;
  uRam00000000005ef625 = 0;
  _DAT_005ef61d = 0;
  uRam00000000005ef635 = 0;
  _DAT_005ef62d = 0;
  uRam00000000005ef645 = 0;
  _DAT_005ef63d = 0;
  uRam00000000005ef655 = 0;
  _DAT_005ef64d = 0;
  xiaomi_get_property_value("persist.vendor.radio.build_region",&DAT_005ef55d,&DAT_001f5b96);
  bVar1 = DAT_005ef55d != 0x4c4c554e;
  bVar2 = DAT_005ef561 != '\0';
  uRam00000000005ef655 = uRam00000000005ef655 & 0xffffffffffffff;
  return bVar1 || bVar2;
}


