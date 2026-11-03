
void FUN_ffff0000b800fcac(undefined4 param_1)

{
  int iVar1;
  
  DAT_ffff0000b811ae98 = FUN_ffff0000b804bccc(4);
  if (DAT_ffff0000b811ae98 != 0) {
    FUN_ffff0000b804d070(DAT_ffff0000b811ae98,0,4);
    FUN_ffff0000b804d6a4(DAT_ffff0000b811ae98,&DAT_ffff0000b80a0e76,param_1);
    iVar1 = FUN_ffff0000b8027d24(s_off_mode_charge_ffff0000b809e28f,DAT_ffff0000b811ae98);
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c(s_set_off_mode_charge_failed_ffff0000b808e952);
    }
    thunk_FUN_ffff0000b8082558(DAT_ffff0000b811ae98);
    return;
  }
  FUN_ffff0000b804c33c(s_off_mode_charge_malloc_failed_ffff0000b80907e7);
  return;
}


