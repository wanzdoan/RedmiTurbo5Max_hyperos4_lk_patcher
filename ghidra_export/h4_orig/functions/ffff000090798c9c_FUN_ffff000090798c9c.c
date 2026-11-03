// ffff000090798c9c  size=116  FUN_ffff000090798c9c

int FUN_ffff000090798c9c(void)

{
  int iVar1;
  undefined8 uVar2;
  
  uVar2 = FUN_ffff0000907ada5c();
  FUN_ffff00009078836c(uVar2,0x20);
  iVar1 = FUN_ffff0000907adf2c(&DAT_ffff000090837a58,0x100);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s__SBC__Fail_to_set_oemkey___0x_x__ffff0000907ef0ab,iVar1);
  }
  iVar1 = FUN_ffff0000907adbd0(0x800);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s__SBC__Fail_to_initialize_sec_fun_ffff0000907d219e,iVar1);
  }
  return iVar1;
}


