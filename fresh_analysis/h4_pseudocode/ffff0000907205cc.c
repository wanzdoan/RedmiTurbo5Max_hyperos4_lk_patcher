
void FUN_ffff0000907205cc(undefined4 param_1)

{
  int iVar1;
  
  DAT_ffff000090860638 = FUN_ffff00009077d514(4);
  if (DAT_ffff000090860638 != 0) {
    FUN_ffff00009077ee50(DAT_ffff000090860638,0,4);
    FUN_ffff00009077f654(DAT_ffff000090860638,&DAT_ffff000090801edd,param_1);
    iVar1 = FUN_ffff000090741cb8(s_off_mode_charge_ffff0000907fcb2a,DAT_ffff000090860638);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_set_off_mode_charge_failed_ffff0000907e0ea6);
    }
    thunk_FUN_ffff0000907c314c(DAT_ffff000090860638);
    return;
  }
  FUN_ffff00009077e11c(s_off_mode_charge_malloc_failed_ffff0000907e44d5);
  return;
}


