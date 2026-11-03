
int FUN_ffff0000be0753ac(void)

{
  int iVar1;
  undefined8 uVar2;
  
  uVar2 = FUN_ffff0000be087c50();
  FUN_ffff0000be0676ac(uVar2,0x20);
  iVar1 = FUN_ffff0000be088120(&DAT_ffff0000be0f3c30,0x100);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98(s__SBC__Fail_to_set_oemkey___0x_x__ffff0000be0b9469,iVar1);
  }
  iVar1 = FUN_ffff0000be087dc4(0x800);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98(s__SBC__Fail_to_initialize_sec_fun_ffff0000be0a523c,iVar1);
  }
  return iVar1;
}


