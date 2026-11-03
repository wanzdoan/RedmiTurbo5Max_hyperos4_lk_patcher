// ffff00009071ad2c  size=284  FUN_ffff00009071ad2c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009071ad2c(ulong param_1,undefined8 param_2)

{
  int iVar1;
  long *plVar2;
  
  if ((DAT_ffff000090848c44 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_lib_mblock_mblock_c_ffff0000907f7bed,0x481,s_mblock_init_done_ffff0000907f5d51);
  }
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  if (_DAT_ffff00009085b8a0 != 0) {
    plVar2 = (long *)(_DAT_ffff000090850c50 + 0xc18);
    do {
      if ((*plVar2 - 1U < param_1) && (param_1 < (ulong)(plVar2[1] + *plVar2))) {
        iVar1 = FUN_ffff00009071a7a8(param_1,param_2);
        if (iVar1 == 0) {
          FUN_ffff00009071768c(&LAB_ffff0000908181f8);
          return 0;
        }
        break;
      }
      plVar2 = plVar2 + 0x13;
    } while (plVar2 <= (long *)(_DAT_ffff000090850c50 + 0x5780U));
  }
  FUN_ffff00009077de54
            (s__s__d__Error__addr___llx__size____ffff0000907ea6e8,
             s_mblock_free_partial_ffff0000907fb1a2,0x48e,param_1,param_2);
  FUN_ffff0000907195cc(0);
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s__s_failed_panic_ffff0000907e434a,s_mblock_free_partial_ffff0000907fb1a2);
}


