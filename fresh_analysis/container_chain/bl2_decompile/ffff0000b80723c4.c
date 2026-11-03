
undefined4 FUN_ffff0000b80723c4(long param_1,uint param_2,undefined8 param_3,ulong *param_4)

{
  undefined8 uVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  ulong uVar4;
  undefined8 local_58;
  ulong local_50;
  ulong local_48;
  
  uVar2 = 0xffffffffffffffdd;
  if (param_2 != 0x60) {
    uVar2 = 0xfffffffffffffff9;
  }
  uVar1 = 1;
  if (param_2 == 0x60) {
    uVar1 = 2;
  }
  local_50 = 0x80;
  local_48 = 0;
  local_58 = param_3;
  FUN_ffff0000b806b9c8(6,&local_58);
  FUN_ffff0000b806b5c8(1,&local_58);
  FUN_ffff0000b806b5c8(2,&local_58);
  FUN_ffff0000b806b5c8(3,&local_58);
  FUN_ffff0000b806b5c8(uVar2,&local_58);
  FUN_ffff0000b806b5c8(4,&local_58);
  FUN_ffff0000b806b9b0(1,&local_58);
  FUN_ffff0000b806b5c8(2,&local_58);
  FUN_ffff0000b806b5c8(0xffffffffffffffff,&local_58);
  FUN_ffff0000b806b5c8(uVar1,&local_58);
  FUN_ffff0000b806b5c8(0xfffffffffffffffe,&local_58);
  uVar4 = (ulong)(param_2 >> 1);
  FUN_ffff0000b806b850(uVar4,param_1,&local_58);
  FUN_ffff0000b806b5c8(0xfffffffffffffffd,&local_58);
  FUN_ffff0000b806b850(uVar4,param_1 + uVar4,&local_58);
  if ((local_48 == 0xffffffffffffffff) || (local_50 < local_48)) {
    uVar3 = 0x10d002;
    FUN_ffff0000b804c33c
              (s__s_DICE_PublicKey_CoseEncode_fai_ffff0000b8087baf,s__DICE__ffff0000b8099de7,
               0x10d002);
  }
  else {
    uVar3 = 0;
    *param_4 = local_48;
  }
  return uVar3;
}


