
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000be04ea2c(void)

{
  uint uVar1;
  
  uVar1 = FUN_ffff0000be04cff4
                    (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e60,DAT_ffff0000be126e34,0x1c);
  if ((uVar1 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxLogContentHeader_ffff0000be0beb05,0x4b8,s_BLACKBOX_ffff0000be0aa9ee);
  }
  else {
    if (DAT_ffff0000be126e60 != -0x16644b7f22da1e10) {
      FUN_ffff0000be05fd98
                (s__s__d____s___init_log_content_he_ffff0000be0b1339,
                 s_CheckBlackboxLogContentHeader_ffff0000be0beb05,0x4bd,s_BLACKBOX_ffff0000be0aa9ee)
      ;
      _DAT_ffff0000be126e70 = 0x1000;
      DAT_ffff0000be126e60 = -0x16644b7f22da1e10;
      _DAT_ffff0000be126e68 = 0x40000001000;
      DAT_ffff0000be126e78 = 0x200000;
    }
    FUN_ffff0000be05fd98
              (s__s__d__print_black_box_log_conte_ffff0000be0aaa7f,
               s_PrintLogContentHeader_ffff0000be0a74f0,0x31a);
    FUN_ffff0000be05fd98
              (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintLogContentHeader_ffff0000be0a74f0,
               0x31b,DAT_ffff0000be126e60);
    FUN_ffff0000be05fd98
              (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintLogContentHeader_ffff0000be0a74f0,
               0x31c,_DAT_ffff0000be126e68 & 0xffffffff);
    FUN_ffff0000be05fd98
              (s__s__d__first_current_log_offset__ffff0000be0bb3cd,
               s_PrintLogContentHeader_ffff0000be0a74f0,0x31d,_DAT_ffff0000be126e70 & 0xffffffff);
    FUN_ffff0000be05fd98
              (s__s__d__dump_count__0x_x_ffff0000be0b479d,s_PrintLogContentHeader_ffff0000be0a74f0,
               0x31e,DAT_ffff0000be126e74);
    FUN_ffff0000be05fd98
              (s__s__d__dump_size__0x_x_ffff0000be0ba2e2,s_PrintLogContentHeader_ffff0000be0a74f0,
               799,DAT_ffff0000be126e78);
  }
  return uVar1 & 1;
}


