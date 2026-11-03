
void FUN_ffff0000be01ce94(int *param_1)

{
  if (((param_1[0x30e] == -0x66666667) && ((uint)param_1[0x30f] < 4)) && (*param_1 == 0x58b0)) {
    FUN_ffff0000be0607b0(&DAT_ffff0000be111cd8,param_1 + 2,0x58a8);
    DAT_ffff0000be111cc8 = &DAT_ffff0000be111d00;
    DAT_ffff0000be111cd0 = &DAT_ffff0000be117518;
    DAT_ffff0000be111cc4 = 1;
    FUN_ffff0000be05fd98
              (s__s__d___s_init_done_ffff0000be0c6138,s_mblock_pl_boottag_hook_ffff0000be0c3e02,
               0x781,s_mblock_pl_boottag_hook_ffff0000be0c3e02);
    FUN_ffff0000be01cf5c();
    FUN_ffff0000be01c1e8(1);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_lib_mblock_mblock_c_ffff0000be0bf570,0x776,
             s____mem_>mblock_info_mblock_magic_ffff0000be0a7c20);
}


