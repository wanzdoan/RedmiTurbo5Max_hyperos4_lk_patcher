
undefined8 FUN_ffff000090718ab8(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;

  if (param_1 == 0) {
    uVar3 = 0xfffffff8;
  }
  else {
    if (DAT_ffff00009083bc10 == 0) {
      DAT_ffff00009083bc10 = FUN_ffff00009077de7c(0x1000);
      if (DAT_ffff00009083bc10 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077d934
                  (s_lib_kcmdline_kcmdline_c_ffff0000907ed02b,0x17a,
                   s_append_cmdline_buf____NULL_ffff0000907d7b15);
      }
      DAT_ffff00009083bc20 = DAT_ffff00009083bc10 + 0x1000;
      DAT_ffff00009083bc18 = DAT_ffff00009083bc10;
    }
    FUN_ffff000090716f9c(&LAB_ffff000090810170,0xffffffff);
    lVar2 = FUN_ffff00009077fb34(param_1);
    if (DAT_ffff00009083bc20 <= DAT_ffff00009083bc18 + lVar2 + 1U) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d848(s__ERROR__CMDLINE_overflow_tail____ffff0000907c8c06);
    }
    iVar1 = FUN_ffff00009077ffe0
                      (DAT_ffff00009083bc18,DAT_ffff00009083bc20 - DAT_ffff00009083bc18,
                       &DAT_ffff0000907dacd4,param_1);
    DAT_ffff00009083bc18 = DAT_ffff00009083bc18 + iVar1;
    FUN_ffff000090717060(&LAB_ffff000090810170);
    uVar3 = 0;
  }
  return uVar3;
}
