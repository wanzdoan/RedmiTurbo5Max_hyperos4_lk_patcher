// ffff0000907b5aa0  size=456  FUN_ffff0000907b5aa0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4
FUN_ffff0000907b5aa0
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
  FUN_ffff0000907aef08(8,&local_68);
  FUN_ffff0000907aeb08(1,&local_68);
  FUN_ffff0000907aee3c(param_2,&local_68);
  FUN_ffff0000907aeb08(2,&local_68);
  FUN_ffff0000907aee3c(param_3,&local_68);
  FUN_ffff0000907aeb08(0xffffffffffb8bbaf,&local_68);
  FUN_ffff0000907aed90(_DAT_ffff00009090d740,param_1,&local_68);
  uVar1 = _DAT_ffff00009090d740;
  uVar3 = (ulong)_DAT_ffff00009090d740;
  FUN_ffff0000907aeb08(0xffffffffffb8bbac,&local_68);
  FUN_ffff0000907aed90(_DAT_ffff00009090d7e4,param_1 + uVar3,&local_68);
  uVar1 = _DAT_ffff00009090d7e4 + uVar1;
  FUN_ffff0000907aeb08(0xffffffffffb8bbab,&local_68);
  FUN_ffff0000907aed90(_DAT_ffff00009090d740,param_1 + (ulong)uVar1,&local_68);
  local_6c[0] = *(undefined1 *)(param_1 + (ulong)(_DAT_ffff00009090d740 + uVar1));
  FUN_ffff0000907aeb08(0xffffffffffb8bba9,&local_68);
  FUN_ffff0000907aed90(1,local_6c,&local_68);
  FUN_ffff0000907aeb08(0xffffffffffb8bba8,&local_68);
  FUN_ffff0000907aed90(param_5,param_4,&local_68);
  local_70[0] = 0x20;
  FUN_ffff0000907aeb08(0xffffffffffb8bba7,&local_68);
  FUN_ffff0000907aed90(1,local_70,&local_68);
  if ((local_58 == 0xffffffffffffffff) || (local_60 < local_58)) {
    uVar2 = 0x10d002;
    FUN_ffff00009077de54
              (s__s_DICE_Payload_CwtEncode_failed_ffff0000907dd607,s__DICE__ffff0000907f547f,
               0x10d002);
  }
  else {
    uVar2 = 0;
    *param_8 = local_58;
  }
  return uVar2;
}


