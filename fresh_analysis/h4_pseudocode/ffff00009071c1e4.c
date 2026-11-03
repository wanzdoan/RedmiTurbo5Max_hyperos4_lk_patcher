
void FUN_ffff00009071c1e4(int *param_1)

{
  if (((param_1[0x30e] == -0x66666667) && ((uint)param_1[0x30f] < 4)) && (*param_1 == 0x58b0)) {
    FUN_ffff00009077eb20(&DAT_ffff00009085aca8,param_1 + 2,0x58a8);
    DAT_ffff000090850c90 = &DAT_ffff00009085acd0;
    DAT_ffff000090850c98 = &DAT_ffff0000908604e8;
    DAT_ffff000090848c84 = 1;
    DAT_ffff000090848c88 = 0;
    DAT_ffff000090850ca4 = 0;
    FUN_ffff00009077e11c
              (s__s__d___s_init_done_ffff000090802154,s_mblock_pl_boottag_hook_ffff0000907feb6d,
               0x781,s_mblock_pl_boottag_hook_ffff0000907feb6d);
    FUN_ffff00009071c2bc();
    FUN_ffff000090719710(1);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_lib_mblock_mblock_c_ffff0000907f7d89,0x776,
             s____mem_>mblock_info_mblock_magic_ffff0000907d5aa9);
}


