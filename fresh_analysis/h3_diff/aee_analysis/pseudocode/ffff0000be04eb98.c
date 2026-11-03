
undefined8 FUN_ffff0000be04eb98(void)

{
  ulong uVar1;
  undefined8 uVar2;
  
  if (DAT_ffff0000be126e74 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___normal_hardreset_fl_ffff0000be0aed6c,
               s_CheckBlackboxCurrentLogHeader_ffff0000be0a86e7,0x4d6,s_BLACKBOX_ffff0000be0aa9ee);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4
                      (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e80,
                       DAT_ffff0000be126e70 + DAT_ffff0000be126e78 * (DAT_ffff0000be126e74 + -1),
                       0x80);
    if ((uVar1 & 1) == 0) {
      FUN_ffff0000be05fd98
                (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
                 s_CheckBlackboxCurrentLogHeader_ffff0000be0a86e7,0x4ce,s_BLACKBOX_ffff0000be0aa9ee)
      ;
      return 0;
    }
    if (DAT_ffff0000be126e80 != -0x4a3b2d5a16c92487) {
      FUN_ffff0000be05fd98
                (s__s__d____s___init_log_header_buf_ffff0000be0a75d8,
                 s_CheckBlackboxCurrentLogHeader_ffff0000be0a86e7,0x4d2,s_BLACKBOX_ffff0000be0aa9ee)
      ;
      DAT_ffff0000be126eb0 = 0;
      DAT_ffff0000be126e80 = 0xb5c4d2a5e936db79;
      DAT_ffff0000be126e88 = 0x100000001000;
      DAT_ffff0000be126e98 = 0x4000000040000;
      DAT_ffff0000be126ea0 = 0x12000;
      DAT_ffff0000be126ea4 = 0x112000;
      DAT_ffff0000be126ea8 = 0x312000;
      DAT_ffff0000be126eac = 0x512000;
      uVar2 = FUN_ffff0000be060e5c(s_pllk_hardreset_log_ffff0000be0a3ab8);
      FUN_ffff0000be0607b0(&DAT_ffff0000be126eb4,s_pllk_hardreset_log_ffff0000be0a3ab8,uVar2);
      uVar2 = FUN_ffff0000be060e5c(s_uefi_log_ffff0000be0b4794);
      FUN_ffff0000be0607b0(&DAT_ffff0000be126ec8,s_uefi_log_ffff0000be0b4794,uVar2);
      DAT_ffff0000be126edc = 0x7c1c918adf6bc8cc;
      DAT_ffff0000be126ee4 = 0x1000;
      DAT_ffff0000be126eec = 0xffffffff00000000;
      DAT_ffff0000be126ef4 = 0xbb800000200;
      DAT_ffff0000be126efc = 0;
      return 1;
    }
  }
  return 1;
}


