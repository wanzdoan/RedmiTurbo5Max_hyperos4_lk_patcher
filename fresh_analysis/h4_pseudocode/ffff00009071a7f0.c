
undefined8 FUN_ffff00009071a7f0(long param_1)

{
  int iVar1;
  long *plVar2;
  
  if ((DAT_ffff000090848c84 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_lib_mblock_mblock_c_ffff0000907f7d89,0x468,s_mblock_init_done_ffff0000907f5edf);
  }
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  if (DAT_ffff00009085b8e0 != 0) {
    plVar2 = (long *)(DAT_ffff000090850c90 + 0xc18);
    do {
      if ((*plVar2 != 0) && (*plVar2 == param_1)) {
        iVar1 = FUN_ffff00009071a8ec(param_1,plVar2[1]);
        if (iVar1 == 0) {
          FUN_ffff0000907177d0(&LAB_ffff000090818208);
          return 0;
        }
        break;
      }
      plVar2 = plVar2 + 0x13;
    } while (plVar2 <= (long *)(DAT_ffff000090850c90 + 0x5780U));
  }
  FUN_ffff00009077e11c
            (s__s__d__Error__addr___llx_ffff0000907dc320,s_mblock_free_ffff0000907e0cdb,0x474,
             param_1);
  FUN_ffff000090719710(0);
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__s_failed_panic_ffff0000907e4465,s_mblock_free_ffff0000907e0cdb);
}


