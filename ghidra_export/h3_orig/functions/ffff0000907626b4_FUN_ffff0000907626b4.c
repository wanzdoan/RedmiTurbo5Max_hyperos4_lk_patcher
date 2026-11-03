// ffff0000907626b4  size=364  FUN_ffff0000907626b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907626b4(void)

{
  uint uVar1;
  
  uVar1 = FUN_ffff000090760c7c
                    (s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cb90,_DAT_ffff00009087cb64,0x1c);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxLogContentHeader_ffff0000907f6df6,0x4b8,s_BLACKBOX_ffff0000907d9988);
  }
  else {
    if (_DAT_ffff00009087cb90 != -0x16644b7f22da1e10) {
      FUN_ffff00009077de54
                (s__s__d____s___init_log_content_he_ffff0000907e3654,
                 s_CheckBlackboxLogContentHeader_ffff0000907f6df6,0x4bd,s_BLACKBOX_ffff0000907d9988)
      ;
      _DAT_ffff00009087cba0 = 0x1000;
      _DAT_ffff00009087cb90 = -0x16644b7f22da1e10;
      _DAT_ffff00009087cb98 = 0x40000001000;
      _DAT_ffff00009087cba8 = 0x200000;
    }
    FUN_ffff00009077de54
              (s__s__d__print_black_box_log_conte_ffff0000907d9a19,
               s_PrintLogContentHeader_ffff0000907d4e4e,0x31a);
    FUN_ffff00009077de54
              (s__s__d__magic__0x_llx_ffff0000907db484,s_PrintLogContentHeader_ffff0000907d4e4e,
               0x31b,_DAT_ffff00009087cb90);
    FUN_ffff00009077de54
              (s__s__d__version__0x_x_ffff00009080122c,s_PrintLogContentHeader_ffff0000907d4e4e,
               0x31c,_DAT_ffff00009087cb98 & 0xffffffff);
    FUN_ffff00009077de54
              (s__s__d__first_current_log_offset__ffff0000907f19b7,
               s_PrintLogContentHeader_ffff0000907d4e4e,0x31d,_DAT_ffff00009087cba0 & 0xffffffff);
    FUN_ffff00009077de54
              (s__s__d__dump_count__0x_x_ffff0000907e8114,s_PrintLogContentHeader_ffff0000907d4e4e,
               0x31e,_DAT_ffff00009087cba4);
    FUN_ffff00009077de54
              (s__s__d__dump_size__0x_x_ffff0000907f01c0,s_PrintLogContentHeader_ffff0000907d4e4e,
               799,_DAT_ffff00009087cba8);
  }
  return uVar1 & 1;
}


