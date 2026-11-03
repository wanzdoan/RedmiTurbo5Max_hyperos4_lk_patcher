
void FUN_ffff0000b8044a88(void)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  
  iVar1 = FUN_ffff0000b803e1b8();
  FUN_ffff0000b804c33c
            (s__LK_LCM__s_lh__first_pre_init_po_ffff0000b80969ad,
             s_lcm_pre_init_power_ffff0000b809fd2b,iVar1 == 0);
  if (iVar1 == 0) {
    FUN_ffff0000b805098c(0x5a,0);
    FUN_ffff0000b8050878(0x5a,1);
    FUN_ffff0000b80508e8(0x5a,0);
    (*DAT_ffff0000b8129a08)(10);
    FUN_ffff0000b8046240(1);
    (*DAT_ffff0000b8129a08)(5);
    lVar3 = FUN_ffff0000b80263f0(1,4);
    if (lVar3 == 0) {
      FUN_ffff0000b804c33c
                (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000b8093c28,
                 s_mt6363_ldo_1p2_enable_ffff0000b80859c5);
    }
    else {
      FUN_ffff0000b802634c(lVar3,0x149e,2,2,0);
      FUN_ffff0000b802634c(lVar3,0x1b3f,3,0xf,0);
      FUN_ffff0000b802634c(lVar3,0x1f88,5,0xf,0);
      uVar2 = FUN_ffff0000b802634c(lVar3,0x1d07,1,1,0);
      FUN_ffff0000b804c33c
                (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000b808f4db,
                 s_mt6363_ldo_1p2_enable_ffff0000b80859c5,uVar2,1);
    }
    (*DAT_ffff0000b8129a08)(5);
    FUN_ffff0000b8046378(1);
    (*DAT_ffff0000b8129a08)(10);
    FUN_ffff0000b803e1cc(1);
  }
  FUN_ffff0000b804c33c(s__LK_LCM__s_lh_____ffff0000b809dcb2,s_lcm_pre_init_power_ffff0000b809fd2b);
  return;
}


