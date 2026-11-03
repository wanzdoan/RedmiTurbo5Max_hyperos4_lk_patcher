
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009076294c(void)

{
  uint uVar1;
  
  uVar1 = FUN_ffff000090760f14
                    (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cc28,DAT_ffff00009087cbfc,0x1c);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxLogContentHeader_ffff0000907f6f80,0x4b8,s_BLACKBOX_ffff0000907d9a1f);
  }
  else {
    if (DAT_ffff00009087cc28 != -0x16644b7f22da1e10) {
      FUN_ffff00009077e11c
                (s__s__d____s___init_log_content_he_ffff0000907e3725,
                 s_CheckBlackboxLogContentHeader_ffff0000907f6f80,0x4bd,s_BLACKBOX_ffff0000907d9a1f)
      ;
      _DAT_ffff00009087cc38 = 0x1000;
      DAT_ffff00009087cc28 = -0x16644b7f22da1e10;
      _DAT_ffff00009087cc30 = 0x40000001000;
      DAT_ffff00009087cc40 = 0x200000;
    }
    FUN_ffff00009077e11c
              (s__s__d__print_black_box_log_conte_ffff0000907d9ab0,
               s_PrintLogContentHeader_ffff0000907d4e8b,0x31a);
    FUN_ffff00009077e11c
              (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintLogContentHeader_ffff0000907d4e8b,
               0x31b,DAT_ffff00009087cc28);
    FUN_ffff00009077e11c
              (s__s__d__version__0x_x_ffff000090801408,s_PrintLogContentHeader_ffff0000907d4e8b,
               0x31c,_DAT_ffff00009087cc30 & 0xffffffff);
    FUN_ffff00009077e11c
              (s__s__d__first_current_log_offset__ffff0000907f1b28,
               s_PrintLogContentHeader_ffff0000907d4e8b,0x31d,_DAT_ffff00009087cc38 & 0xffffffff);
    FUN_ffff00009077e11c
              (s__s__d__dump_count__0x_x_ffff0000907e8258,s_PrintLogContentHeader_ffff0000907d4e8b,
               0x31e,DAT_ffff00009087cc3c);
    FUN_ffff00009077e11c
              (s__s__d__dump_size__0x_x_ffff0000907f0331,s_PrintLogContentHeader_ffff0000907d4e8b,
               799,DAT_ffff00009087cc40);
  }
  return uVar1 & 1;
}


