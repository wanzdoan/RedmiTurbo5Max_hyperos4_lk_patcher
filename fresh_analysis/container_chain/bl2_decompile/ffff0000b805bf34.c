
int FUN_ffff0000b805bf34(long param_1,int param_2,long param_3,ulong param_4)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  ulong local_48;
  
  local_48 = 0;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_hmac____NULL_ffff0000b8086be2,s_platform_mediatek_mt6991_sboot_c_ffff0000b808b20a,
               0x24);
  }
  if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_key____NULL_ffff0000b8093eac,s_platform_mediatek_mt6991_sboot_c_ffff0000b808b20a,
               0x25);
  }
  iVar1 = FUN_ffff0000b805b788(param_2);
  if (iVar1 != 0) {
    return iVar1;
  }
  *(int *)(param_1 + 0x70) = param_2;
  if (param_4 == 0) {
    return 3;
  }
  uVar5 = *(ulong *)((&DAT_ffff0000b81b6cc8)[param_2] + 0x10);
  lVar2 = FUN_ffff0000b804bccc(*(undefined8 *)((&DAT_ffff0000b81b6cc8)[param_2] + 0x18));
  if (lVar2 == 0) {
    return 0xd;
  }
  uVar3 = *(ulong *)((&DAT_ffff0000b81b6cc8)[param_2] + 0x18);
  if (uVar3 < 0x91) {
    if (uVar3 < param_4) {
      local_48 = uVar3;
      iVar1 = FUN_ffff0000b805b840(param_2,param_3,param_4,param_1 + 0xe8,&local_48);
      if (iVar1 != 0) goto LAB_ffff0000b805c0b8;
    }
    else {
      FUN_ffff0000b804cd40(param_1 + 0xe8,param_3,param_4);
      uVar5 = param_4;
    }
    lVar4 = (&DAT_ffff0000b81b6cc8)[param_2];
    uVar3 = *(ulong *)(lVar4 + 0x18);
    if (uVar5 <= uVar3 && uVar3 - uVar5 != 0) {
      FUN_ffff0000b805e5ac(param_1 + uVar5 + 0xe8,uVar3 - uVar5);
      lVar4 = (&DAT_ffff0000b81b6cc8)[param_2];
      uVar3 = *(ulong *)(lVar4 + 0x18);
    }
    if (uVar3 != 0) {
      uVar5 = 0;
      do {
        *(byte *)(lVar2 + uVar5) = *(byte *)(param_1 + 0xe8 + uVar5) ^ 0x36;
        uVar5 = uVar5 + 1;
      } while (uVar5 < *(ulong *)(lVar4 + 0x18));
    }
    iVar1 = (**(code **)(lVar4 + 0xa8))(param_1);
    if (iVar1 == 0) {
      iVar1 = (**(code **)((&DAT_ffff0000b81b6cc8)[param_2] + 0xb0))
                        (param_1,lVar2,*(undefined8 *)((&DAT_ffff0000b81b6cc8)[param_2] + 0x18));
    }
  }
  else {
    iVar1 = 6;
  }
LAB_ffff0000b805c0b8:
  FUN_ffff0000b805e5ac(lVar2,*(undefined8 *)((&DAT_ffff0000b81b6cc8)[param_2] + 0x18));
  thunk_FUN_ffff0000b8082558(lVar2);
  return iVar1;
}


