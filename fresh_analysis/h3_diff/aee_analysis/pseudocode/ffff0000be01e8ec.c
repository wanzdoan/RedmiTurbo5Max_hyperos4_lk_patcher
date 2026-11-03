
void FUN_ffff0000be01e8ec(undefined4 param_1)

{
  int iVar1;
  
  DAT_ffff0000be117648 = FUN_ffff0000be05f1bc(4);
  if (DAT_ffff0000be117648 != 0) {
    FUN_ffff0000be060ae0(DAT_ffff0000be117648,0,4);
    FUN_ffff0000be061134(DAT_ffff0000be117648,&DAT_ffff0000be0c6087,param_1);
    iVar1 = FUN_ffff0000be031d1c(s_off_mode_charge_ffff0000be0c2a02,DAT_ffff0000be117648);
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98(s_set_off_mode_charge_failed_ffff0000be0af72d);
    }
    thunk_FUN_ffff0000be09937c(DAT_ffff0000be117648);
    return;
  }
  FUN_ffff0000be05fd98(s_off_mode_charge_malloc_failed_ffff0000be0b1b97);
  return;
}


