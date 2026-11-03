// ffff0000907b5d20  size=236  FUN_ffff0000907b5d20

undefined4
FUN_ffff0000907b5d20
          (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
          undefined8 param_5,ulong param_6,ulong *param_7)

{
  undefined4 uVar1;
  undefined8 local_58;
  ulong local_50;
  ulong local_48;
  
  local_48 = 0;
  local_58 = param_5;
  local_50 = param_6;
  FUN_ffff0000907aeef0(4,&local_58);
  FUN_ffff0000907aee3c(s_Signature1_ffff0000907f58c4,&local_58);
  FUN_ffff0000907aed90(param_2,param_1,&local_58);
  FUN_ffff0000907aed90(0,0,&local_58);
  FUN_ffff0000907aed90(param_4,param_3,&local_58);
  if ((local_48 == 0xffffffffffffffff) || (local_50 < local_48)) {
    uVar1 = 0x10d002;
    FUN_ffff00009077de54
              (s__s_DICE_Tbs_CoseEncode_failed__0_ffff0000907f3af4,s__DICE__ffff0000907f547f,
               0x10d002);
  }
  else {
    uVar1 = 0;
    *param_7 = local_48;
  }
  return uVar1;
}


