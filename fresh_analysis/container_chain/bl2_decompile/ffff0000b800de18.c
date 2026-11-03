
void FUN_ffff0000b800de18(int *param_1)

{
  if (((param_1[0x30e] == -0x66666667) && ((uint)param_1[0x30f] < 4)) && (*param_1 == 0x58b0)) {
    FUN_ffff0000b804cd40(&DAT_ffff0000b8115520,param_1 + 2,0x58a8);
    DAT_ffff0000b8115508 = &DAT_ffff0000b8115548;
    DAT_ffff0000b8115510 = &DAT_ffff0000b811ad60;
    DAT_ffff0000b8115504 = 1;
    FUN_ffff0000b804c33c
              (s__s__d___s_init_done_ffff0000b80a0e61,s_mblock_pl_boottag_hook_ffff0000b809f2e3,
               0x781,s_mblock_pl_boottag_hook_ffff0000b809f2e3);
    FUN_ffff0000b800dee0();
    FUN_ffff0000b800c828(1);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_lib_mblock_mblock_c_ffff0000b809b560,0x776,
             s____mem_>mblock_info_mblock_magic_ffff0000b80889c4);
}


