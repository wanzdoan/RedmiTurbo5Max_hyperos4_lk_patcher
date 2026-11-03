// ffff000090762b10  size=336  FUN_ffff000090762b10

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762b10(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_record_zo_ffff0000908011d7,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f0247,0x507);
    return 0;
  }
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf30,0xa420000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f0247,0x50e,s_BLACKBOX_ffff0000907d9988);
    return 0;
  }
  if (_DAT_ffff00009087bf30 != -0x71b3d560f4c2a184) {
    FUN_ffff00009077de54
              (s__s__d____s___init_record_zone_he_ffff0000907e1c31,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f0247,0x512,s_BLACKBOX_ffff0000907d9988);
    if (DAT_ffff00009087bf14 == '\x03') {
      _DAT_ffff00009087bf38 = 0x3000;
    }
    else if (DAT_ffff00009087bf14 == '\x02') {
      _DAT_ffff00009087bf38 = 0x2000;
    }
    else if (DAT_ffff00009087bf14 == '\x01') {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v1__no_record_zo_ffff0000908011d7,
                 s_InitRecordZoneHeader_ffff0000907dced5,0x228);
      goto LAB_ffff000090762c3c;
    }
    _DAT_ffff00009087bf30 = -0x71b3d560f4c2a184;
    _DAT_ffff00009087bf3c = 0xa42040000000400;
  }
LAB_ffff000090762c3c:
  FUN_ffff000090761634(&DAT_ffff00009087bf30);
  return 1;
}


