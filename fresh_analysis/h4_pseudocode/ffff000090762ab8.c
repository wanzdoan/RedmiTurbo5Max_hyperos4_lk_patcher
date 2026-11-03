
undefined8 FUN_ffff000090762ab8(void)

{
  ulong uVar1;
  undefined8 uVar2;
  
  if (DAT_ffff00009087cc3c == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___normal_hardreset_fl_ffff0000907e0119,
               s_CheckBlackboxCurrentLogHeader_ffff0000907d6a79,0x4d6,s_BLACKBOX_ffff0000907d9a1f);
  }
  else {
    uVar1 = FUN_ffff000090760f14
                      (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cc48,
                       DAT_ffff00009087cc38 + DAT_ffff00009087cc40 * (DAT_ffff00009087cc3c + -1),
                       0x80);
    if ((uVar1 & 1) == 0) {
      FUN_ffff00009077e11c
                (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
                 s_CheckBlackboxCurrentLogHeader_ffff0000907d6a79,0x4ce,s_BLACKBOX_ffff0000907d9a1f)
      ;
      return 0;
    }
    if (DAT_ffff00009087cc48 != -0x4a3b2d5a16c92487) {
      FUN_ffff00009077e11c
                (s__s__d____s___init_log_header_buf_ffff0000907d4f73,
                 s_CheckBlackboxCurrentLogHeader_ffff0000907d6a79,0x4d2,s_BLACKBOX_ffff0000907d9a1f)
      ;
      DAT_ffff00009087cc78 = 0;
      DAT_ffff00009087cc48 = 0xb5c4d2a5e936db79;
      DAT_ffff00009087cc50 = 0x100000001000;
      DAT_ffff00009087cc60 = 0x4000000040000;
      DAT_ffff00009087cc68 = 0x12000;
      DAT_ffff00009087cc6c = 0x112000;
      DAT_ffff00009087cc70 = 0x312000;
      DAT_ffff00009087cc74 = 0x512000;
      uVar2 = FUN_ffff00009077f250(s_pllk_hardreset_log_ffff0000907cff9e);
      FUN_ffff00009077eb20(&DAT_ffff00009087cc7c,s_pllk_hardreset_log_ffff0000907cff9e,uVar2);
      uVar2 = FUN_ffff00009077f250(s_uefi_log_ffff0000907e824f);
      FUN_ffff00009077eb20(&DAT_ffff00009087cc90,s_uefi_log_ffff0000907e824f,uVar2);
      DAT_ffff00009087cca4 = 0x7c1c918adf6bc8cc;
      DAT_ffff00009087ccac = 0x1000;
      DAT_ffff00009087ccb4 = 0xffffffff00000000;
      DAT_ffff00009087ccbc = 0xbb800000200;
      DAT_ffff00009087ccc4 = 0;
      return 1;
    }
  }
  return 1;
}


