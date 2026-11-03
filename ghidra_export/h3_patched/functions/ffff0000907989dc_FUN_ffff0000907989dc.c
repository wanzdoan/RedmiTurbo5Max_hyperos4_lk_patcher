// ffff0000907989dc  size=116  FUN_ffff0000907989dc

int FUN_ffff0000907989dc(void)

{
  int iVar1;
  undefined8 uVar2;
  
  uVar2 = FUN_ffff0000907ad79c();
  FUN_ffff0000907880ac(uVar2,0x20);
  iVar1 = FUN_ffff0000907adc64(&DAT_ffff000090837a38,0x100);
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s__SBC__Fail_to_set_oemkey___0x_x__ffff0000907eef23,iVar1);
  }
  iVar1 = FUN_ffff0000907ad910(0x800);
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s__SBC__Fail_to_initialize_sec_fun_ffff0000907d2161,iVar1);
  }
  return iVar1;
}


