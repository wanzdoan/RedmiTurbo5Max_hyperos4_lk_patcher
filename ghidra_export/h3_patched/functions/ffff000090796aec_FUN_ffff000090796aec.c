// ffff000090796aec  size=408  FUN_ffff000090796aec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090796aec(long param_1)

{
  long lVar1;
  uint uVar2;
  ulong uVar3;
  char *pcVar4;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  ulong local_50 [4];
  
  lVar1 = _DAT_ffff00009090a4c0;
  FUN_ffff00009077e858(_DAT_ffff00009090a4c0,param_1 + 0xe4,0x100);
  FUN_ffff00009077e858(lVar1 + 0x100,param_1 + 0x1e4,0x10);
  FUN_ffff00009077e858(lVar1 + 0x110,param_1 + 500,4);
  FUN_ffff00009077e858(lVar1 + 0x114,param_1 + 0x1f8,2);
  FUN_ffff00009077e858(lVar1 + 0x116,param_1 + 0x1fa,2);
  FUN_ffff00009077e858(lVar1 + 0x118,param_1 + 0x1fc,2);
  FUN_ffff00009077e858(lVar1 + 0x11a,param_1 + 0x1fe,2);
  FUN_ffff000090707ce4(_DAT_ffff00009090a4c0,0x1000);
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  uVar3 = FUN_ffff000090792870();
  if ((uVar3 & 1) == 0) {
    FUN_ffff00009077eb88(local_50,0,0x20);
    FUN_ffff00009071ea6c(0xc200040d,0x10,2,0,0,0,0,0,local_50);
    uVar3 = local_50[0] & 0xffffffff;
    if ((int)local_50[0] == 0) {
      return uVar3;
    }
  }
  else {
    uVar2 = thunk_FUN_ffff0000907b6074(0,&local_70,0x20);
    if (uVar2 == 0) {
      uVar2 = FUN_ffff000090787e40(&local_70,0x20,_DAT_ffff00009090a4c0,0x11c,_DAT_ffff00009090a4c8)
      ;
      if (uVar2 == 0) {
        return 0;
      }
      pcVar4 = s___s___RPMB_rpmb_get_mac_fail__re_ffff0000907d052c;
    }
    else {
      pcVar4 = s___s___RPMB_Key_derive_failed__re_ffff0000907e6e45;
    }
    uVar3 = (ulong)uVar2;
    FUN_ffff00009077de54(pcVar4,s_rpmb_hmac_ffff0000907f566a,uVar3);
  }
  FUN_ffff00009077de54
            (s___s___RPMB_rpmb_hmac_error__ret___ffff0000907e6e1f,s_rpmb_get_mac_ffff0000907f076b,
             uVar3);
  return uVar3;
}


