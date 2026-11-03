// ffff0000907629c0  size=336  FUN_ffff0000907629c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907629c0(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_control_z_ffff0000907d6951,
               s_CheckBlackboxControlZoneHeader_ffff0000907f338e,0x4f0);
    return 0;
  }
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf18,0xa400000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxControlZoneHeader_ffff0000907f338e,0x4f7,s_BLACKBOX_ffff0000907d9988);
    return 0;
  }
  if (_DAT_ffff00009087bf18 != 0x1a3f5d7e9c2b4a6d) {
    FUN_ffff00009077de54
              (s__s__d____s___init_control_zone_h_ffff0000907d19c0,
               s_CheckBlackboxControlZoneHeader_ffff0000907f338e,0x4fb,s_BLACKBOX_ffff0000907d9988);
    if (DAT_ffff00009087bf14 == '\x03') {
      _DAT_ffff00009087bf20 = 0x3000;
    }
    else if (DAT_ffff00009087bf14 == '\x02') {
      _DAT_ffff00009087bf20 = 0x2000;
    }
    else if (DAT_ffff00009087bf14 == '\x01') {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v1__no_control_z_ffff0000907d6951,
                 s_InitControlZoneHeader_ffff0000907eb4b0,0x215);
      goto LAB_ffff000090762aec;
    }
    _DAT_ffff00009087bf18 = 0x1a3f5d7e9c2b4a6d;
    _DAT_ffff00009087bf24 = 0xa40000000000400;
  }
LAB_ffff000090762aec:
  FUN_ffff000090761550(&DAT_ffff00009087bf18);
  return 1;
}


