
undefined8 FUN_ffff0000b800c180(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  if (param_1 == 0) {
    uVar3 = 0xfffffff8;
  }
  else {
    if (DAT_ffff0000b81104d0 == 0) {
      DAT_ffff0000b81104d0 = FUN_ffff0000b804bccc(0x1000);
      if (DAT_ffff0000b81104d0 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_lib_kcmdline_kcmdline_c_ffff0000b80998f4,0x17a,
                   s_append_cmdline_buf____NULL_ffff0000b808db74);
      }
      DAT_ffff0000b81104e0 = DAT_ffff0000b81104d0 + 0x1000;
      DAT_ffff0000b81104d8 = DAT_ffff0000b81104d0;
    }
    FUN_ffff0000b800ad90(&LAB_ffff0000b80adc98,0xffffffff);
    lVar2 = FUN_ffff0000b804d3cc(param_1);
    if (DAT_ffff0000b81104e0 <= DAT_ffff0000b81104d8 + lVar2 + 1U) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s__ERROR__CMDLINE_overflow_tail____ffff0000b8085f9d);
    }
    iVar1 = FUN_ffff0000b804d74c
                      (DAT_ffff0000b81104d8,DAT_ffff0000b81104e0 - DAT_ffff0000b81104d8,
                       &DAT_ffff0000b808f86e,param_1);
    DAT_ffff0000b81104d8 = DAT_ffff0000b81104d8 + iVar1;
    FUN_ffff0000b800ae28(&LAB_ffff0000b80adc98);
    uVar3 = 0;
  }
  return uVar3;
}


