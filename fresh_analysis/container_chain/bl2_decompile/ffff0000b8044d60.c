
void FUN_ffff0000b8044d60(void)

{
  undefined4 uVar1;
  long lVar2;
  
  FUN_ffff0000b804c33c(s__LK_LCM__s____ffff0000b8094cc2,s_lcm_suspend_power_ffff0000b808875e);
  FUN_ffff0000b805098c(0x5a,0);
  FUN_ffff0000b8050878(0x5a,1);
  FUN_ffff0000b80508e8(0x5a,0);
  (*DAT_ffff0000b8129a08)(1);
  FUN_ffff0000b8046378(0);
  (*DAT_ffff0000b8129a08)(1);
  lVar2 = FUN_ffff0000b80263f0(1,4);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c
              (s__LK_LCM__LCM__ERROR_get_mt6363_d_ffff0000b8093c28,
               s_mt6363_ldo_1p2_enable_ffff0000b80859c5);
  }
  else {
    FUN_ffff0000b802634c(lVar2,0x1d07,0,1,0);
    uVar1 = FUN_ffff0000b802634c(lVar2,0x149f,2,2,0);
    FUN_ffff0000b804c33c
              (s__LK_LCM__LCM__s_ret__d_en__d_ffff0000b808f4db,
               s_mt6363_ldo_1p2_enable_ffff0000b80859c5,uVar1,0);
  }
  (*DAT_ffff0000b8129a08)(1);
  FUN_ffff0000b8046240(0);
  return;
}


