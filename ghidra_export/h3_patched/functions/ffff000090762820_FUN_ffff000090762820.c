// ffff000090762820  size=416  FUN_ffff000090762820

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762820(void)

{
  ulong uVar1;
  undefined8 uVar2;
  
  if (_DAT_ffff00009087cba4 == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___normal_hardreset_fl_ffff0000907e005f,
               s_CheckBlackboxCurrentLogHeader_ffff0000907d69e3,0x4d6,s_BLACKBOX_ffff0000907d9988);
  }
  else {
    uVar1 = FUN_ffff000090760c7c
                      (s_blackbox_ffff0000907f0c04,&DAT_ffff00009087cbb0,
                       _DAT_ffff00009087cba0 + _DAT_ffff00009087cba8 * (_DAT_ffff00009087cba4 + -1),
                       0x80);
    if ((uVar1 & 1) == 0) {
      FUN_ffff00009077de54
                (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
                 s_CheckBlackboxCurrentLogHeader_ffff0000907d69e3,0x4ce,s_BLACKBOX_ffff0000907d9988)
      ;
      return 0;
    }
    if (_DAT_ffff00009087cbb0 != -0x4a3b2d5a16c92487) {
      FUN_ffff00009077de54
                (s__s__d____s___init_log_header_buf_ffff0000907d4f36,
                 s_CheckBlackboxCurrentLogHeader_ffff0000907d69e3,0x4d2,s_BLACKBOX_ffff0000907d9988)
      ;
      _DAT_ffff00009087cbe0 = 0;
      _DAT_ffff00009087cbb0 = 0xb5c4d2a5e936db79;
      _DAT_ffff00009087cbb8 = 0x100000001000;
      _DAT_ffff00009087cbc8 = 0x4000000040000;
      _DAT_ffff00009087cbd0 = 0x12000;
      _DAT_ffff00009087cbd4 = 0x112000;
      _DAT_ffff00009087cbd8 = 0x312000;
      _DAT_ffff00009087cbdc = 0x512000;
      uVar2 = FUN_ffff00009077ef88(s_pllk_hardreset_log_ffff0000907cff7f);
      FUN_ffff00009077e858(0xffff00009087cbe4,s_pllk_hardreset_log_ffff0000907cff7f,uVar2);
      uVar2 = FUN_ffff00009077ef88(s_uefi_log_ffff0000907e80f4);
      FUN_ffff00009077e858(0xffff00009087cbf8,s_uefi_log_ffff0000907e80f4,uVar2);
      _DAT_ffff00009087cc0c = 0x7c1c918adf6bc8cc;
      _DAT_ffff00009087cc14 = 0x1000;
      _DAT_ffff00009087cc1c = 0xffffffff00000000;
      _DAT_ffff00009087cc24 = 0xbb800000200;
      _DAT_ffff00009087cc2c = 0;
      return 1;
    }
  }
  return 1;
}


