
ulong FUN_ffff0000b805a190(long param_1)

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
  
  lVar1 = DAT_ffff0000b81b6940;
  FUN_ffff0000b804cd40(DAT_ffff0000b81b6940,param_1 + 0xe4,0x100);
  FUN_ffff0000b804cd40(lVar1 + 0x100,param_1 + 0x1e4,0x10);
  FUN_ffff0000b804cd40(lVar1 + 0x110,param_1 + 500,4);
  FUN_ffff0000b804cd40(lVar1 + 0x114,param_1 + 0x1f8,2);
  FUN_ffff0000b804cd40(lVar1 + 0x116,param_1 + 0x1fa,2);
  FUN_ffff0000b804cd40(lVar1 + 0x118,param_1 + 0x1fc,2);
  FUN_ffff0000b804cd40(lVar1 + 0x11a,param_1 + 0x1fe,2);
  FUN_ffff0000b80075ac(DAT_ffff0000b81b6940,0x1000);
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  uVar3 = FUN_ffff0000b8056844();
  if ((uVar3 & 1) == 0) {
    FUN_ffff0000b804d070(local_50,0,0x20);
    FUN_ffff0000b800e6ac(0xc200040d,0x10,2,0,0,0,0,0,local_50);
    uVar3 = local_50[0] & 0xffffffff;
    if ((int)local_50[0] == 0) {
      return uVar3;
    }
  }
  else {
    uVar2 = thunk_FUN_ffff0000b80729e0(0,&local_70,0x20);
    if (uVar2 == 0) {
      uVar2 = FUN_ffff0000b80528d8(&local_70,0x20,DAT_ffff0000b81b6940,0x11c,DAT_ffff0000b81b6948);
      if (uVar2 == 0) {
        return 0;
      }
      pcVar4 = s___s___RPMB_rpmb_get_mac_fail__re_ffff0000b8085c74;
    }
    else {
      pcVar4 = s___s___RPMB_Key_derive_failed__re_ffff0000b8091fd5;
    }
    uVar3 = (ulong)uVar2;
    FUN_ffff0000b804c33c(pcVar4,s_rpmb_hmac_ffff0000b809a32a,uVar3);
  }
  FUN_ffff0000b804c33c
            (s___s___RPMB_rpmb_hmac_error__ret___ffff0000b8091faf,s_rpmb_get_mac_ffff0000b8097a2d,
             uVar3);
  return uVar3;
}


