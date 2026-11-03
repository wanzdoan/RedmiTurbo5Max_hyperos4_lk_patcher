// ffff0000907693b0  size=216  FUN_ffff0000907693b0

void FUN_ffff0000907693b0(char *param_1)

{
  uint uVar1;
  int iVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  if ((param_1 != (char *)0x0) && (uVar1 = FUN_ffff00009077ef88(), 0 < (int)uVar1)) {
    if (0x1e < (int)uVar1) {
      uVar1 = 0x1f;
    }
    if (*param_1 == '/') {
      param_1 = param_1 + 1;
      uVar1 = uVar1 - 1;
    }
    FUN_ffff00009077e858(&local_40,param_1,(ulong)uVar1);
    *(undefined1 *)((long)&local_40 + (ulong)uVar1) = 0;
  }
  FUN_ffff000090745a54();
  iVar2 = FUN_ffff000090745a40();
  if (iVar2 < 0) {
    iVar2 = iVar2 + 1;
  }
  FUN_ffff0000907459fc(iVar2 >> 1,0);
  FUN_ffff0000907458c8(s_Red_State_ffff0000907fdfcc);
  FUN_ffff0000907458c8(s_Device_Cannot_Boot__Enter_Repair_ffff0000907ffaf5);
  FUN_ffff0000907458c8(s_Dismiss_After_5_Seconds__ffff0000907f34dc);
  FUN_ffff000090745ebc();
  FUN_ffff00009077cbd8(5000000);
  FUN_ffff000090745ebc();
  return;
}


