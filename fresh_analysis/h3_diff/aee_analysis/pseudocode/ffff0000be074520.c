
ulong FUN_ffff0000be074520(long param_1)

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
  
  lVar1 = DAT_ffff0000be1b4c00;
  FUN_ffff0000be0607b0(DAT_ffff0000be1b4c00,param_1 + 0xe4,0x100);
  FUN_ffff0000be0607b0(lVar1 + 0x100,param_1 + 0x1e4,0x10);
  FUN_ffff0000be0607b0(lVar1 + 0x110,param_1 + 500,4);
  FUN_ffff0000be0607b0(lVar1 + 0x114,param_1 + 0x1f8,2);
  FUN_ffff0000be0607b0(lVar1 + 0x116,param_1 + 0x1fa,2);
  FUN_ffff0000be0607b0(lVar1 + 0x118,param_1 + 0x1fc,2);
  FUN_ffff0000be0607b0(lVar1 + 0x11a,param_1 + 0x1fe,2);
  FUN_ffff0000be007328(DAT_ffff0000be1b4c00,0x1000);
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  uVar3 = FUN_ffff0000be0705d0();
  if ((uVar3 & 1) == 0) {
    FUN_ffff0000be060ae0(local_50,0,0x20);
    FUN_ffff0000be01d644(0xc200040d,0x10,2,0,0,0,0,0,local_50);
    uVar3 = local_50[0] & 0xffffffff;
    if ((int)local_50[0] == 0) {
      return uVar3;
    }
  }
  else {
    uVar2 = thunk_FUN_ffff0000be090708(0,&local_70,0x20);
    if (uVar2 == 0) {
      uVar2 = FUN_ffff0000be066df0(&local_70,0x20,DAT_ffff0000be1b4c00,0x11c,DAT_ffff0000be1b4c08);
      if (uVar2 == 0) {
        return 0;
      }
      pcVar4 = s___s___RPMB_rpmb_get_mac_fail__re_ffff0000be0a3e8a;
    }
    else {
      pcVar4 = s___s___RPMB_Key_derive_failed__re_ffff0000be0b37fd;
    }
    uVar3 = (ulong)uVar2;
    FUN_ffff0000be05fd98(pcVar4,s_rpmb_hmac_ffff0000be0bdbb9,uVar3);
  }
  FUN_ffff0000be05fd98
            (s___s___RPMB_rpmb_hmac_error__ret___ffff0000be0b37d7,s_rpmb_get_mac_ffff0000be0ba754,
             uVar3);
  return uVar3;
}


