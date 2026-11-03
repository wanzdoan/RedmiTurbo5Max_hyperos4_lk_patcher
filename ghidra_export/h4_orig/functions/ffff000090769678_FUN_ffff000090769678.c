// ffff000090769678  size=216  FUN_ffff000090769678

void FUN_ffff000090769678(char *param_1)

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
  if ((param_1 != (char *)0x0) && (uVar1 = FUN_ffff00009077f250(), 0 < (int)uVar1)) {
    if (0x1e < (int)uVar1) {
      uVar1 = 0x1f;
    }
    if (*param_1 == '/') {
      param_1 = param_1 + 1;
      uVar1 = uVar1 - 1;
    }
    FUN_ffff00009077eb20(&local_40,param_1,(ulong)uVar1);
    *(undefined1 *)((long)&local_40 + (ulong)uVar1) = 0;
  }
  FUN_ffff000090745cec();
  iVar2 = FUN_ffff000090745cd8();
  if (iVar2 < 0) {
    iVar2 = iVar2 + 1;
  }
  FUN_ffff000090745c94(iVar2 >> 1,0);
  FUN_ffff000090745b60(s_Red_State_ffff0000907fe174);
  FUN_ffff000090745b60(s_Device_Cannot_Boot__Enter_Repair_ffff0000907ffc9d);
  FUN_ffff000090745b60(s_Dismiss_After_5_Seconds__ffff0000907f367f);
  FUN_ffff000090746154();
  FUN_ffff00009077cea0(5000000);
  FUN_ffff000090746154();
  return;
}


