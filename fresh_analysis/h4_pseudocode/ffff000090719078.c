
undefined8 FUN_ffff000090719078(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  if (param_1 == 0) {
    uVar3 = 0xfffffff8;
  }
  else {
    if (DAT_ffff000090843c50 == 0) {
      DAT_ffff000090843c50 = FUN_ffff00009077d514(0x1000);
      if (DAT_ffff000090843c50 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_lib_kcmdline_kcmdline_c_ffff0000907f4362,0x17a,
                   s_append_cmdline_buf____NULL_ffff0000907df5bf);
      }
      DAT_ffff000090843c60 = DAT_ffff000090843c50 + 0x1000;
      DAT_ffff000090843c58 = DAT_ffff000090843c50;
    }
    FUN_ffff000090717738(&LAB_ffff0000908181a8,0xffffffff);
    lVar2 = FUN_ffff00009077f250(param_1);
    if (DAT_ffff000090843c60 <= DAT_ffff000090843c58 + lVar2 + 1U) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s__ERROR__CMDLINE_overflow_tail____ffff0000907d0bd2);
    }
    iVar1 = FUN_ffff00009077f6fc
                      (DAT_ffff000090843c58,DAT_ffff000090843c60 - DAT_ffff000090843c58,
                       &DAT_ffff0000907e25da,param_1);
    DAT_ffff000090843c58 = DAT_ffff000090843c58 + iVar1;
    FUN_ffff0000907177d0(&LAB_ffff0000908181a8);
    uVar3 = 0;
  }
  return uVar3;
}


