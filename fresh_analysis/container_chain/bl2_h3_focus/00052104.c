// File offset entry: 0x52104

void FUN_ffff0000b8052104(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  char *pcVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  undefined8 local_28;
  
  iVar2 = FUN_ffff0000b800a01c();
  local_28 = 0;
  iVar3 = FUN_ffff0000b804cdbc(param_1,s_pvmfw_ffff0000b808f35c);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b80700cc(param_2,param_3,0);
    if (iVar3 == 0) {
      FUN_ffff0000b80715d0((long)&local_28 + 4,&local_28);
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      iVar3 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_s_ffff0000b808a031,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,0,iVar3 - iVar2,local_28._4_4_,local_28 & 0xffffffff);
    }
    else {
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_f_ffff0000b8090d9d,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,0,iVar3);
    }
  }
  iVar3 = FUN_ffff0000b804cdbc(param_1,&DAT_ffff0000b809d10c);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b80700cc(param_2,param_3,1);
    if (iVar3 == 0) {
      FUN_ffff0000b80715d0((long)&local_28 + 4,&local_28);
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      iVar3 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_s_ffff0000b808a031,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,1,iVar3 - iVar2,local_28._4_4_,local_28 & 0xffffffff);
    }
    else {
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_f_ffff0000b8090d9d,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,1,iVar3);
    }
  }
  iVar3 = FUN_ffff0000b804cdbc(param_1,&DAT_ffff0000b8088c01);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b80700cc(param_2,param_3,2);
    if (iVar3 == 0) {
      FUN_ffff0000b80715d0((long)&local_28 + 4,&local_28);
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      iVar3 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_s_ffff0000b808a031,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,2,iVar3 - iVar2,local_28._4_4_,local_28 & 0xffffffff);
    }
    else {
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_f_ffff0000b8090d9d,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,2,iVar3);
    }
  }
  iVar3 = FUN_ffff0000b804cdbc(param_1,&DAT_ffff0000b808a665);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b80700cc(param_2,param_3,3);
    if (iVar3 == 0) {
      FUN_ffff0000b80715d0((long)&local_28 + 4,&local_28);
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      iVar3 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_s_ffff0000b808a031,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,3,iVar3 - iVar2,local_28._4_4_,local_28 & 0xffffffff);
    }
    else {
      uVar4 = FUN_ffff0000b80715f4();
      pcVar1 = s__HWRoT__ffff0000b80a0473;
      if ((uVar4 & 1) == 0) {
        pcVar1 = s__APMCU__ffff0000b8090dcb;
      }
      FUN_ffff0000b804beb0
                (s__s_s_BL2_EXT___s__DICE_flow___d_f_ffff0000b8090d9d,s__DICE__ffff0000b8098d0e,
                 pcVar1,param_1,3,iVar3);
    }
  }
  return;
}


