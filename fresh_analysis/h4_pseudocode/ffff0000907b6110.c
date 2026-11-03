
undefined4
FUN_ffff0000907b6110
          (long param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,undefined8 param_5,
          undefined8 param_6,ulong param_7,ulong *param_8)

{
  uint uVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined1 local_70 [4];
  undefined1 local_6c [4];
  undefined8 local_68;
  ulong local_60;
  ulong local_58;
  
  local_58 = 0;
  local_68 = param_6;
  local_60 = param_7;
  FUN_ffff0000907af1d0(8,&local_68);
  FUN_ffff0000907aedd0(1,&local_68);
  FUN_ffff0000907af104(param_2,&local_68);
  FUN_ffff0000907aedd0(2,&local_68);
  FUN_ffff0000907af104(param_3,&local_68);
  FUN_ffff0000907aedd0(0xffffffffffb8bbaf,&local_68);
  FUN_ffff0000907af058(DAT_ffff00009090d800,param_1,&local_68);
  uVar1 = DAT_ffff00009090d800;
  uVar3 = (ulong)DAT_ffff00009090d800;
  FUN_ffff0000907aedd0(0xffffffffffb8bbac,&local_68);
  FUN_ffff0000907af058(DAT_ffff00009090d8a4,param_1 + uVar3,&local_68);
  uVar1 = DAT_ffff00009090d8a4 + uVar1;
  FUN_ffff0000907aedd0(0xffffffffffb8bbab,&local_68);
  FUN_ffff0000907af058(DAT_ffff00009090d800,param_1 + (ulong)uVar1,&local_68);
  local_6c[0] = *(undefined1 *)(param_1 + (ulong)(DAT_ffff00009090d800 + uVar1));
  FUN_ffff0000907aedd0(0xffffffffffb8bba9,&local_68);
  FUN_ffff0000907af058(1,local_6c,&local_68);
  FUN_ffff0000907aedd0(0xffffffffffb8bba8,&local_68);
  FUN_ffff0000907af058(param_5,param_4,&local_68);
  local_70[0] = 0x20;
  FUN_ffff0000907aedd0(0xffffffffffb8bba7,&local_68);
  FUN_ffff0000907af058(1,local_70,&local_68);
  if ((local_58 == 0xffffffffffffffff) || (local_60 < local_58)) {
    uVar2 = 0x10d002;
    FUN_ffff00009077e11c
              (s__s_DICE_Payload_CwtEncode_failed_ffff0000907dd6aa,s__DICE__ffff0000907f55ec,
               0x10d002);
  }
  else {
    uVar2 = 0;
    *param_8 = local_58;
  }
  return uVar2;
}


