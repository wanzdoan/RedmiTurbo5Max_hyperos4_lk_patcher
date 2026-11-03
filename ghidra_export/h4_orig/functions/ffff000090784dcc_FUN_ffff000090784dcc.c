// ffff000090784dcc  size=360  FUN_ffff000090784dcc

void FUN_ffff000090784dcc(undefined8 param_1)

{
  ulong uVar1;
  undefined4 uVar2;
  int iVar3;
  ulong uVar4;
  undefined4 local_534;
  undefined4 local_530 [320];
  
  FUN_ffff00009077ee50(local_530,0,0x500);
  local_534 = 0x140;
  uVar4 = 0;
  do {
    uVar2 = thunk_FUN_ffff00009074b22c(uVar4 & 0xffffffff);
    uVar1 = uVar4 + 1;
    local_530[uVar4] = uVar2;
    uVar4 = uVar1;
  } while (uVar1 != 0x140);
  uVar2 = thunk_FUN_ffff00009074b22c(0);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(1);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(2);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(3);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(4);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(0x14);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(0x15);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  uVar2 = thunk_FUN_ffff00009074b22c(0x1e);
  FUN_ffff00009077e11c(s_SSSS_0x_x_ffff000090802fac,uVar2);
  iVar3 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (-1 < iVar3) {
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar3,s_atag_devinfo_ffff0000907fe2af,&local_534,0x504);
    if (iVar3 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_devinfo_dts_write_failed_ffff000090802fb7);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s_get__chosen_offset_failed__d__ffff0000907db82b,iVar3);
}


