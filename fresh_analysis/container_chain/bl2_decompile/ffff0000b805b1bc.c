
int FUN_ffff0000b805b1bc(void)

{
  int iVar1;
  undefined8 uVar2;
  
  uVar2 = FUN_ffff0000b806a254();
  FUN_ffff0000b8052b44(uVar2,0x20);
  iVar1 = FUN_ffff0000b806a724(&DAT_ffff0000b8107288,0x100);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c(s__SBC__Fail_to_set_oemkey___0x_x__ffff0000b8096bf2,iVar1);
  }
  iVar1 = FUN_ffff0000b806a3c8(0x800);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c(s__SBC__Fail_to_initialize_sec_fun_ffff0000b8086bb3,iVar1);
  }
  return iVar1;
}


