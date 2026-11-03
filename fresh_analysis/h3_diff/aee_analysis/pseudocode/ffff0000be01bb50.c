
undefined8 FUN_ffff0000be01bb50(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  if (param_1 == 0) {
    uVar3 = 0xfffffff8;
  }
  else {
    if (DAT_ffff0000be10cc90 == 0) {
      DAT_ffff0000be10cc90 = FUN_ffff0000be05f1bc(0x1000);
      if (DAT_ffff0000be10cc90 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_lib_kcmdline_kcmdline_c_ffff0000be0bcf1d,0x17a,
                   s_append_cmdline_buf____NULL_ffff0000be0ae5eb);
      }
      DAT_ffff0000be10cca0 = DAT_ffff0000be10cc90 + 0x1000;
      DAT_ffff0000be10cc98 = DAT_ffff0000be10cc90;
    }
    FUN_ffff0000be01a730(&LAB_ffff0000be0d61b8,0xffffffff);
    lVar2 = FUN_ffff0000be060e5c(param_1);
    if (DAT_ffff0000be10cca0 <= DAT_ffff0000be10cc98 + lVar2 + 1U) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s__ERROR__CMDLINE_overflow_tail____ffff0000be0a44c5);
    }
    iVar1 = FUN_ffff0000be0611dc
                      (DAT_ffff0000be10cc98,DAT_ffff0000be10cca0 - DAT_ffff0000be10cc98,
                       &DAT_ffff0000be0b0a6a,param_1);
    DAT_ffff0000be10cc98 = DAT_ffff0000be10cc98 + iVar1;
    FUN_ffff0000be01a7c8(&LAB_ffff0000be0d61b8);
    uVar3 = 0;
  }
  return uVar3;
}


