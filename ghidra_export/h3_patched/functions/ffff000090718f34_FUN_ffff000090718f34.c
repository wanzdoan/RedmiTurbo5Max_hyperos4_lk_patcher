// ffff000090718f34  size=236  FUN_ffff000090718f34

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090718f34(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  if (param_1 == 0) {
    uVar3 = 0xfffffff8;
  }
  else {
    if (_DAT_ffff000090843c10 == 0) {
      _DAT_ffff000090843c10 = FUN_ffff00009077d24c(0x1000);
      if (_DAT_ffff000090843c10 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_lib_kcmdline_kcmdline_c_ffff0000907f41de,0x17a,
                   s_append_cmdline_buf____NULL_ffff0000907df505);
      }
      _DAT_ffff000090843c20 = _DAT_ffff000090843c10 + 0x1000;
      _DAT_ffff000090843c18 = _DAT_ffff000090843c10;
    }
    FUN_ffff0000907175f4(&LAB_ffff000090818198,0xffffffff);
    lVar2 = FUN_ffff00009077ef88(param_1);
    if (_DAT_ffff000090843c20 <= _DAT_ffff000090843c18 + lVar2 + 1U) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s__ERROR__CMDLINE_overflow_tail____ffff0000907d0b9d);
    }
    iVar1 = FUN_ffff00009077f434
                      (_DAT_ffff000090843c18,_DAT_ffff000090843c20 - _DAT_ffff000090843c18,
                       &DAT_ffff0000907e24f2,param_1);
    _DAT_ffff000090843c18 = _DAT_ffff000090843c18 + iVar1;
    FUN_ffff00009071768c(&LAB_ffff000090818198);
    uVar3 = 0;
  }
  return uVar3;
}


