// File offset entry: 0xd264

undefined8 FUN_ffff0000b800d264(long param_1)

{
  int iVar1;
  long *plVar2;
  
  if ((DAT_ffff0000b8114504 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_lib_mblock_mblock_c_ffff0000b809a47d,0x468,s_mblock_init_done_ffff0000b80995a8);
  }
  FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  if (DAT_ffff0000b8115158 != 0) {
    plVar2 = (long *)(DAT_ffff0000b8114508 + 0xc18);
    do {
      if ((*plVar2 != 0) && (*plVar2 == param_1)) {
        iVar1 = FUN_ffff0000b800d360(param_1,plVar2[1]);
        if (iVar1 == 0) {
          FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
          return 0;
        }
        break;
      }
      plVar2 = plVar2 + 0x13;
    } while (plVar2 <= (long *)(DAT_ffff0000b8114508 + 0x5780U));
  }
  FUN_ffff0000b804beb0
            (s__s__d__Error__addr___llx_ffff0000b808b2e2,s_mblock_free_ffff0000b808d80f,0x474,
             param_1);
  FUN_ffff0000b800c828(0);
  FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b2b8(s__s_failed_panic_ffff0000b808f750,s_mblock_free_ffff0000b808d80f);
}


