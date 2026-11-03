// ffff000090784b04  size=360  FUN_ffff000090784b04

void FUN_ffff000090784b04(undefined8 param_1)

{
  ulong uVar1;
  undefined4 uVar2;
  int iVar3;
  ulong uVar4;
  undefined4 local_534;
  undefined4 local_530 [320];
  
  FUN_ffff00009077eb88(local_530,0,0x500);
  local_534 = 0x140;
  uVar4 = 0;
  do {
    uVar2 = thunk_FUN_ffff00009074af94(uVar4 & 0xffffffff);
    uVar1 = uVar4 + 1;
    local_530[uVar4] = uVar2;
    uVar4 = uVar1;
  } while (uVar1 != 0x140);
  uVar2 = thunk_FUN_ffff00009074af94(0);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(1);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(2);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(3);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(4);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(0x14);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(0x15);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  uVar2 = thunk_FUN_ffff00009074af94(0x1e);
  FUN_ffff00009077de54(s_SSSS_0x_x_ffff000090802da8,uVar2);
  iVar3 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (-1 < iVar3) {
    iVar3 = FUN_ffff00009076e40c(param_1,iVar3,s_atag_devinfo_ffff0000907fe107,&local_534,0x504);
    if (iVar3 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_devinfo_dts_write_failed_ffff000090802db3);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s_get__chosen_offset_failed__d__ffff0000907db720,iVar3);
}


