
undefined8 FUN_ffff0000be01c51c(long param_1)

{
  int iVar1;
  long *plVar2;
  
  if ((DAT_ffff0000be111cc4 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_lib_mblock_mblock_c_ffff0000be0bf570,0x468,s_mblock_init_done_ffff0000be0be105);
  }
  FUN_ffff0000be01a730(&LAB_ffff0000be0d6218,0xffffffff);
  if (DAT_ffff0000be112910 != 0) {
    plVar2 = (long *)(DAT_ffff0000be111cc8 + 0xc18);
    do {
      if ((*plVar2 != 0) && (*plVar2 == param_1)) {
        iVar1 = FUN_ffff0000be01c618(param_1,plVar2[1]);
        if (iVar1 == 0) {
          FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6218);
          return 0;
        }
        break;
      }
      plVar2 = plVar2 + 0x13;
    } while (plVar2 <= (long *)(DAT_ffff0000be111cc8 + 0x5780U));
  }
  FUN_ffff0000be05fd98
            (s__s__d__Error__addr___llx_ffff0000be0ac66e,s_mblock_free_ffff0000be0af675,0x474,
             param_1);
  FUN_ffff0000be01c1e8(0);
  FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6218);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee24(s__s_failed_panic_ffff0000be0b1b60,s_mblock_free_ffff0000be0af675);
}


