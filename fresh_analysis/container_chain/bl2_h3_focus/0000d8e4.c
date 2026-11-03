// File offset entry: 0xd8e4

undefined8 FUN_ffff0000b800d8e4(ulong param_1,undefined8 param_2)

{
  int iVar1;
  long *plVar2;
  
  if ((DAT_ffff0000b8114504 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_lib_mblock_mblock_c_ffff0000b809a47d,0x481,s_mblock_init_done_ffff0000b80995a8);
  }
  FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  if (DAT_ffff0000b8115158 != 0) {
    plVar2 = (long *)(DAT_ffff0000b8114508 + 0xc18);
    do {
      if ((*plVar2 - 1U < param_1) && (param_1 < (ulong)(plVar2[1] + *plVar2))) {
        iVar1 = FUN_ffff0000b800d360(param_1,param_2);
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
            (s__s__d__Error__addr___llx__size____ffff0000b809308c,
             s_mblock_free_partial_ffff0000b809c2cf,0x48e,param_1,param_2);
  FUN_ffff0000b800c828(0);
  FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b2b8(s__s_failed_panic_ffff0000b808f750,s_mblock_free_partial_ffff0000b809c2cf);
}


